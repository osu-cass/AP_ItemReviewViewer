package org.smarterbalanced.itemreviewviewer.web.services;

import AIR.Common.Utilities.SpringApplicationContext;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarterbalanced.itemreviewviewer.web.config.SettingsReader;
import org.smarterbalanced.itemreviewviewer.web.models.ItemModel;
import org.smarterbalanced.itemreviewviewer.web.models.metadata.ItemMetadataModel;
import org.smarterbalanced.itemreviewviewer.web.models.scoring.ItemScoringModel;
import org.smarterbalanced.itemreviewviewer.web.models.scoring.ItemScoringOptionModel;
import org.smarterbalanced.itemreviewviewer.web.models.scoring.RubricModel;
import org.smarterbalanced.itemreviewviewer.web.services.models.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import tds.irisshared.repository.IContentBuilder;
import tds.itemrenderer.data.IITSDocument;
import tds.itemrenderer.data.ITSResource;
import tds.itemrenderer.data.ITSTutorial;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class GitLabService implements IGitLabService {
    private static final Logger _logger = LoggerFactory.getLogger(GitLabService.class);

    private static String ZIP_FILE_LOCATION;
    private static String CONTENT_LOCATION;
    private static final String ZIP_EXTENSION = ".zip";
    private static final String XML_EXTENSION = ".xml";
    private static final List<String> NON_ITEM_INTERACTION_TYPES = Arrays.asList("WIT", "TUT");

    private static final int BUFFER_SIZE = 4096;

    private static IContentBuilder _contentBuilder;

    public GitLabService() {
        try {
            ZIP_FILE_LOCATION = SettingsReader.getZipFileLocation();
            // NOTE: Do not change key 'iris.ContentPath' in settings-mysql.xml because iris uses it
            // irv's settings-mysql.xml overwrites iris's because we use overlays.
            CONTENT_LOCATION = SettingsReader.readIrisContentPath();
            _contentBuilder = SpringApplicationContext.getBean("iContentBuilder", IContentBuilder.class);
        } catch (Exception exp) {
            _logger.error("Error loading zip file location", exp);
        }
    }

    public boolean downloadItem(String namespace, String itemDirName, boolean isAssociatedItem) throws IOException {
        if (isItemExistsLocally(itemDirName)) return true;

        if (downloadItem(namespace, itemDirName)) {
            unzip(namespace, itemDirName, isAssociatedItem);
        } else {
            return false;
        }
        return true;
    }

    public boolean downloadItem(String namespace, String itemNumber) throws GitLabException {
        String itemURL = GitLabUtils.getGitLabItemUrl(namespace, itemNumber);

        try {
            URL gitLabItemURL = new URL(itemURL);

            String downloadLocation = ZIP_FILE_LOCATION + itemNumber + ZIP_EXTENSION;
            _logger.info("Getting the Item with URL:" + itemURL + " with file name:" + downloadLocation + " Started");
            boolean isSucceed = true;

            ReadableByteChannel rbc = Channels.newChannel(gitLabItemURL.openStream());
            FileOutputStream fos = new FileOutputStream(downloadLocation);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

            fos.close();
            rbc.close();

            _logger.info("Getting the Item with URL:" + itemURL + " with file name: " + downloadLocation + " Success");
            return isSucceed;

        } catch (Exception e) {
            _logger.error("IO Exception occurred while Getting the Item with URL:" + itemURL);
            throw new GitLabException(e);
        }
    }

    @Override
    public boolean isItemExistsLocally(String itemNumber) {
        String zipFilePath = ZIP_FILE_LOCATION + itemNumber + ZIP_EXTENSION;
        File f = new File(zipFilePath);
        _logger.info("Checking the File Already Exists:" + f.exists());

        return f.exists();
    }

    @Override
    public void unzip(String namespace, String itemDirName, boolean isAssociatedItem) throws IOException, GitLabException {
        String zipFilePath = ZIP_FILE_LOCATION + itemDirName + ZIP_EXTENSION;
        _logger.info("Unzipping the File:" + zipFilePath + " to Location:" + CONTENT_LOCATION + " Started");
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        File dir = null;
        ZipEntry entry = zipIn.getNextEntry();

        String rootDirectoryPath = null;
        int index = 1;

        while (entry != null) {
            String filePath = CONTENT_LOCATION + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
                byte[] bytesIn = new byte[BUFFER_SIZE];
                int read = 0;
                while ((read = zipIn.read(bytesIn)) != -1) {
                    bos.write(bytesIn, 0, read);
                }
                bos.close();
            } else {
                if (index == 1) {
                    rootDirectoryPath = filePath;
                }
                dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
            index++;
        }
        zipIn.close();

        _logger.info("Unzipping the File:" + zipFilePath + " to Location:" + CONTENT_LOCATION + " Success");

        _renameItemFolder(itemDirName, zipFilePath, rootDirectoryPath);
        _renameItemFile(namespace, itemDirName, isAssociatedItem);

    }

    private void _renameItemFolder(String itemDirName, String zipFilePath, String rootDirectoryPath) {
        String _contentPath = CONTENT_LOCATION + itemDirName;

        //rename root directory to Item-blankid-id-version format
        if (rootDirectoryPath != null) {
            File rootDirectory = new File(rootDirectoryPath);
            rootDirectory.renameTo(new File(_contentPath));
        } else {
            _logger.error("Downloaed File:" + zipFilePath + " to Location:" + CONTENT_LOCATION + " is corrupted make sure Item Exists");
            new File(zipFilePath).delete();
        }
    }

    private void _renameItemFile(String namespace, String itemDirName, boolean isAssociatedItem) {
        String _contentPath = CONTENT_LOCATION + itemDirName;
        boolean hasBankId = true;
        //rename item file if no bankKey
        String noBankKeyItemId = GitLabUtils.extractItemId(namespace, itemDirName);
        if (!noBankKeyItemId.equals(itemDirName)) {
            String itemName = itemDirName.toLowerCase();
            if (itemName.split("-").length > 3) {
                String itemSplit[] = itemName.split("-");
                itemName = itemSplit[0] + "-" + itemSplit[1] + "-" + itemSplit[2];
            }
            _logger.info("No BankKey in namespace: " + namespace + " | itemNumber: " + itemDirName);
            String orgFilePath = _contentPath + "/" + noBankKeyItemId + XML_EXTENSION;
            String newFilePath = _contentPath + "/" + itemName + XML_EXTENSION;
            File item = new File(orgFilePath);
            item.renameTo(new File(newFilePath));
            _logger.info("Renamed the file from " + orgFilePath + " to " + newFilePath);

            ItemIdUtils.ItemId itemId = ItemIdUtils.parseItemId(itemDirName);
            if (itemId == null) {
                _logger.info("Failed to parse an ItemId with string " + itemDirName);
            }
            _changeBankKey(newFilePath, itemId.bankKey);
            hasBankId = false;
        }

        //Download related Items
        if (!itemDirName.toLowerCase().contains("stim") &&
                !isAssociatedItem) {
            String itemId = itemDirName.toLowerCase();
            String qualifiedItemId = GitLabUtils.makeQualifiedItemId(itemId, null);
            try {
                IITSDocument document = _contentBuilder.getITSDocument(qualifiedItemId);
                _downloadAssociatedItems(namespace, document, hasBankId);
            } catch (Exception e) {
                _logger.info("Failed to render document (" + qualifiedItemId + ")", e);
            }
        }
    }

    private void _changeBankKey(String filePath, String bankKey) throws GitLabException {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(filePath);
            Node item = null;
            Node tutorial = null;
            // Get the staff element by tag name directly
            if (filePath.toLowerCase().contains("stim")) {
                item = doc.getElementsByTagName("passage").item(0);
            } else {
                item = doc.getElementsByTagName("item").item(0);
                tutorial = doc.getElementsByTagName("tutorial").item(0);
            }

            // update staff attribute
            NamedNodeMap attr = item.getAttributes();
            Node nodeAttr = attr.getNamedItem("bankkey");
            nodeAttr.setTextContent(bankKey);

            //changes bank key for tutorial if it exists.
            NamedNodeMap tutorialMap;
            Node tutorialAttr;
            if (tutorial != null) {
                tutorialMap = tutorial.getAttributes();
                tutorialAttr = tutorialMap.getNamedItem("bankkey");
                tutorialAttr.setTextContent(bankKey);
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);

            _logger.info("Changing the bankKey succeeded");
        } catch (Exception e) {
            _logger.error("Failed to change bankkey in file: " + filePath);
            throw new GitLabException(e);
        }
    }

    @Override
    public List<Namespace> getNamespaces() throws GitLabException {
        final String TOTAL_PAGES = "X-Total-Pages";
        final int PAGE_SIZE = 100; /* Gitlab API allows only 100 as the maximum page size */

        int curPage = 1, totalPages = 1;
        List<Namespace> namespaces;
        String nameSpacesUrl = null;

        try {
            nameSpacesUrl = GitLabUtils.getNamespacesUrl(PAGE_SIZE, curPage);

            WebResource webResource = Client.create().resource(nameSpacesUrl);
            ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);

            if (response.getStatus() != HttpStatus.OK.value()) {
                throw new GitLabException("Could not get NameSpaces; Failed : HTTP error code : " + response.getStatus()
                        + " : URL : " + nameSpacesUrl);
            }

            String output = response.getEntity(String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            namespaces = objectMapper.readValue(output, new TypeReference<List<Namespace>>() {
            });

            /* if the number of namespaces is over current page size, additional api calls are required */
            totalPages = Integer.parseInt(response.getHeaders().get(TOTAL_PAGES).get(0));
            ++curPage;
            for (; curPage <= totalPages; curPage++) {
                _getRemainingNamespaces(PAGE_SIZE, curPage, namespaces);
            }

            _removeNoProjectNamespaces(namespaces);

        } catch (Exception e) {
            //if the name gitlab API can't search for namespaces due to load use these namespaces.
            _logger.error("Failed to get namespaces, URL: " + nameSpacesUrl);
            namespaces = GitLabUtils.getDefaultNameSpaces();
            //throw new GitLabException("Failed to Get Namespaces from Gitlab");
        }

        return namespaces;
    }

    private void _getRemainingNamespaces(final int pageSize, int page, List<Namespace> namespaces) throws GitLabException {
        List<Namespace> _namespaces;
        String nameSpacesUrl = null;

        try {
            nameSpacesUrl = GitLabUtils.getNamespacesUrl(pageSize, page);

            WebResource webResource = Client.create().resource(nameSpacesUrl);
            ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);

            if (response.getStatus() != HttpStatus.OK.value()) {
                throw new GitLabException("Could not get NameSpaces; Failed : HTTP error code : " + response.getStatus() + " : URL : " + nameSpacesUrl);
            }

            String output = response.getEntity(String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            _namespaces = objectMapper.readValue(output, new TypeReference<List<Namespace>>() {
            });

            namespaces.addAll(_namespaces);

        } catch (Exception e) {
            _logger.error("Failed to get remaining namespaces, URL: " + nameSpacesUrl);
            throw new GitLabException(e);
        }
    }

    private void _removeNoProjectNamespaces(List<Namespace> namespaces) throws GitLabException {
        try {
            ListIterator<Namespace> iterator = namespaces.listIterator();
            while (iterator.hasNext()) {
                Namespace namespace = iterator.next();
                String projectsSearchUrl = GitLabUtils.getProjectsSearchUrl(namespace.getName());

                WebResource webResource = Client.create().resource(projectsSearchUrl);
                ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);

                if (response.getStatus() != HttpStatus.OK.value()) {
                    throw new GitLabException("Failed to get project list : HTTP error code : " + response.getStatus() + " : URL : " + projectsSearchUrl);
                }

                String output = response.getEntity(String.class);

                ObjectMapper objectMapper = new ObjectMapper();
                List<Project> projects = objectMapper.readValue(output, new TypeReference<List<Project>>() {
                });

                if (projects.size() == 0) {
                    iterator.remove();
                    continue;
                }

                String name = projects.get(0).getName();
                String parts[] = name.split("-");
                //TODO: Add name spaces to the namespace hash on the fly
                if (parts.length > 1) {
                    if (parts.length == 3) {
                        namespace.setHasBankKey(true);
                    } else {
                        namespace.setHasBankKey(false);
                        if(GitLabUtils.getBankKeyByNamespace(namespace.getName()) == null) {
                            GitLabUtils.addNameSpace(namespace.getName());
                        }
                        namespace.setBankKey(Integer.parseInt(GitLabUtils.getBankKeyByNamespace(namespace.getName())));
                    }
                } else {
                    namespace.setHasBankKey(false);
                    if(GitLabUtils.noBankKeyNamespaceHash.get(namespace.name) == null){
                        GitLabUtils.addNameSpace(namespace.getName());
                    }
                    namespace.setBankKey(Integer.parseInt(GitLabUtils.getBankKeyByNamespace(namespace.getName())));
                }
            }
        } catch (Exception e) {
            _logger.error("Failed to remove no project namespaces");
            throw new GitLabException(e);
        }
    }


    public ItemDocument getItemScoring(String itemNumber) throws GitLabException {
        String[] parts = itemNumber.split("-");
        String baseItemName = parts[0] + "-" + parts[1] + "-" + parts[2];
        String rubricFilePath = CONTENT_LOCATION + itemNumber + File.separator + baseItemName.toLowerCase() + XML_EXTENSION;

        try {
            _logger.debug("unmarshalling started " + rubricFilePath);

            ItemDocument content = (ItemDocument) loadXmlFile(rubricFilePath, ItemDocument.class);

            _logger.debug("unmarshalling completed " + rubricFilePath);

            return content;

        } catch (Exception e) {
            throw new GitLabException(e);
        }
    }

    public static Object loadXmlFile(String path, Class targetClass) throws IOException, XMLStreamException, JAXBException {
        FileInputStream fileStream = new FileInputStream(path);
        XMLStreamReader streamReader = XMLInputFactory.newFactory().createXMLStreamReader(fileStream);
        XMLReaderWithoutNamespace xmlReader = new XMLReaderWithoutNamespace(streamReader);

        JAXBContext jaxbContext = JAXBContext.newInstance(targetClass);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        Object obj = jaxbUnmarshaller.unmarshal(xmlReader);

        fileStream.close();

        return obj;
    }

    public List<ItemCommit> getItemCommits(String namespace, String itemNumber) throws GitLabException {
        try {
            String[] parts = itemNumber.split("-");

            return getItemCommits(namespace, parts[0], parts[1], parts[2]);

        } catch (Exception e) {
            // TODO: handle exception
            throw new GitLabException(e);
        }
    }

    public List<ItemCommit> getItemCommits(String namespace, String type, String bankId, String itemNumber) throws GitLabException {
        try {
            Client client = Client.create();
            String id = type + "-" + bankId + "-" + itemNumber;

            String itemCommitsUrl = GitLabUtils.getItemCommitsUrl(namespace, id);

            WebResource webResourceGet = client.resource(itemCommitsUrl);
            ClientResponse response = webResourceGet.accept("application/json").get(ClientResponse.class);

            if (response.getStatus() != 200) {
                throw new GitLabException("Could not get ItemCommits; Failed : HTTP error code : "
                        + response.getStatus()
                        + " : URL : " + itemCommitsUrl);
            }

            String output = response.getEntity(String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            List<ItemCommit> itemCommits = objectMapper.readValue(output, new TypeReference<List<ItemCommit>>() {
            });

            return itemCommits;
        } catch (Exception e) {
            throw new GitLabException(e);
        }
    }

    @Override
    public Metadata getMetadata(String itemDirName) throws GitLabException {
        try {
            String metadataFilePath = CONTENT_LOCATION + itemDirName + File.separator + "metadata.xml";

            _logger.debug("unmarshalling started " + metadataFilePath);

            Metadata metadata = (Metadata) loadXmlFile(metadataFilePath, Metadata.class);

            _logger.debug("unmarshalling completed " + metadataFilePath);

            return metadata;
        } catch (Exception e) {
            throw new GitLabException(e);
        }
    }

    public ItemRelease getItemData(String itemDirName) {
        try {
            String itemNumber = itemDirName.toLowerCase();
            ItemIdUtils.ItemId parsedItemId = ItemIdUtils.parseItemId(itemNumber);
            String itemPath = CONTENT_LOCATION + itemDirName + File.separator + parsedItemId.makeItemFileName() + ".xml";

            _logger.debug("unmarshalling started " + itemPath);

            ItemRelease itemData = (ItemRelease) loadXmlFile(itemPath, ItemRelease.class);

            _logger.debug("unmarshalling completed " + itemPath);

            return itemData;

        } catch (Exception e) {
            throw new GitLabException(e);
        }
    }

    @Override
    public String getClaim(String itemNumber) throws GitLabException {
        String itemPath = CONTENT_LOCATION + "ItemsPatch.xml";

        try {
            _logger.debug("unmarshalling ItemPatch file started");

            ItemsPatchModel itemData = (ItemsPatchModel) loadXmlFile(itemPath, ItemsPatchModel.class);

            _logger.debug("unmarshalling metadata file completed");

            for (int i = 0; i < itemData.getRow().length; i++) {
                if (itemData.getRow()[i].getItemId().equals(itemNumber)) {
                    return itemData.getRow()[i].getClaim();
                }
            }
        } catch (FileNotFoundException e) {
            _logger.error("Metadata file not found. " + e.getMessage());
            throw new GitLabException(e);
        } catch (JAXBException e) {
            _logger.error("Error parsing metadata file." + e.getMessage());
            throw new GitLabException(e);
        } catch (Exception e) {
            throw new GitLabException(e);
        }
        return null;
    }

    public ItemMetadataModel getItemMetadata(String namespace, String itemId, String section) throws GitLabException, IOException {


        if (!downloadItem(namespace, itemId, false)) {
            throw new GitLabException("Failed downloading (item: " + itemId + ") (namespace: " + namespace +")");
        }

        Metadata md;
        try {
            md = getMetadata(itemId);
        } catch (GitLabException e) {
            String[] parts = itemId.split("-");
            String baseItemName = parts[0] + "-" + parts[1] + "-" + parts[2];
            md = getMetadata(baseItemName);
        }
        ItemDocument item = getItemScoring(itemId);

        List<ItemScoringOptionModel> opts = new ArrayList<ItemScoringOptionModel>();

        List<RubricModel> rubrics = new ArrayList<RubricModel>();
        List<ItemScoringModel> contentList = item.item.content;
        if (contentList.size() > 0) {
            for (ItemScoringModel content : contentList) {
                List<RubricModel> list = content.getRubrics();
                if (content.getScoringOptions() != null && content.getScoringOptions().size() > 0) {
                    opts.addAll(content.getScoringOptions());
                }
                if (list != null && list.size() > 0) {
                    for (RubricModel rubric : list) {
                        rubric.setLanguage(content.getLanguage());
                        rubrics.add(rubric);
                    }
                }
            }
        }

        ItemScoringModel score = new ItemScoringModel(
                null,
                null,
                opts.size() > 0 ? opts : null,
                rubrics.size() > 0 ? rubrics : null
        );

        ItemIdUtils.ItemId parsedItemId = ItemIdUtils.parseItemId(itemId);
        return new ItemMetadataModel(namespace, parsedItemId.id, parsedItemId.bankKey, parsedItemId.commitId, section,
                md.getSmarterAppMetadata(), score);
    }

    public boolean isItemAccomExists(String itemDirName, String ext) {
        if (isItemExistsLocally(itemDirName)) {
            String itemPath = CONTENT_LOCATION + itemDirName + File.separator;
            File directory = new File(itemPath);
            Collection files = FileUtils.listFiles(directory, new WildcardFileFilter("*." + ext), null);
            return files.size() > 0;

        }
        return false;
    }

    private void _downloadAssociatedItems(String namespace, IITSDocument doc, boolean hasBankId) throws GitLabException {
        ITSTutorial tutorial = doc.getTutorial();
        List<ITSResource> resources = doc.getResources();
        long stimulusKey = doc.getStimulusKey();


        try {
            if (tutorial != null) {
                String tutorialId;
                if (hasBankId) {
                    tutorialId = GitLabUtils.makeDirId(Long.toString(tutorial._bankKey), Long.toString(tutorial._id));
                } else {
                    String bankKey = GitLabUtils.getBankKeyByNamespace(namespace);
                    tutorialId = GitLabUtils.makeDirId(bankKey, Long.toString(tutorial._id));
                }

                if (!isItemExistsLocally(tutorialId) && downloadItem(namespace, tutorialId)) {
                    unzip(namespace, tutorialId, true);
                }

            }

            if (resources != null) {
                for (ITSResource resource : resources) {
                    String resourceId;

                    if (hasBankId) {
                        resourceId = GitLabUtils.makeDirId(Long.toString(resource._bankKey), Long.toString(resource._id));
                    } else {
                        String bankKey = GitLabUtils.getBankKeyByNamespace(namespace);
                        resourceId = GitLabUtils.makeDirId(bankKey, Long.toString(resource._id));
                    }

                    if (!isItemExistsLocally(resourceId) && downloadItem(namespace, resourceId)) {
                        unzip(namespace, resourceId, true);
                    }
                }
            }

            if (stimulusKey > 0.0) {
                if (hasBankId) {
                    _downloadStim(namespace, Long.toString(doc.getBankKey()), Long.toString(stimulusKey));
                } else {
                    _downloadStim(namespace, null, Long.toString(stimulusKey));
                }
            }
        } catch (Exception e) {
            throw new GitLabException(e);
        }

    }

    //Downloads the stimulus for a given item.
    private void _downloadStim(String namespace, String bankKey, String id) {
        String itemDirName;
        if (bankKey == null && namespace != null) {

            bankKey = GitLabUtils.getBankKeyByNamespace(namespace);
        }
        itemDirName = GitLabUtils.makeStimId(bankKey, id);
        _logger.debug("Starting download of stim: " + id);

        if (!isItemExistsLocally(itemDirName) && downloadItem(namespace, itemDirName)) {
            try {
                unzip(namespace, itemDirName, true);
                _logger.debug("Stim " + itemDirName + "successfully unzipped");
            } catch (IOException e) {
                _logger.error("Error processing stim: " + itemDirName);
                throw new GitLabException(e);
            }
        }
    }

    @Override
    public int isItemExists(ItemModel item) throws IOException {
        String itemDirId = GitLabUtils.makeDirId(item.getBankKey(), item.getItemKey());

        // Checks if the item exits
        String itemUrl = GitLabUtils.getGitLabItemUrl(item.getNamespace(), itemDirId);
        URL url = new URL(itemUrl);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        int statusCode = connection.getResponseCode();

        String found = matchItemDirWithoutRevision(itemDirId);

        boolean itemExists = false;
        if(statusCode == 200) {
            if (StringUtils.isEmpty(found)) {
                if (!downloadItem(item.getNamespace(), itemDirId, false)) {
                    _logger.error("Failed downloading (item: " + itemDirId + ") (namespace: " + item.getNamespace() + ")");
                }
                found = itemDirId;
            }
            Metadata md = getMetadata(found);
            if (md != null && !NON_ITEM_INTERACTION_TYPES.contains(md.getSmarterAppMetadata().getInteractionType())) {
                itemExists = true;
            }
        }


        if(statusCode == 200 && !itemExists){
            statusCode = 418; //I'm a teapot
        }

        return statusCode;
    }

    public String matchItemDirWithoutRevision(String itemDir) {

        File file = new File(CONTENT_LOCATION);
        String[] names = file.list();

        for(String name : names)
        {
            if (new File(CONTENT_LOCATION + name).isDirectory())
            {
                System.out.println(name);

                String[] parts = name.split("-");

                String dirWithoutRevision = GitLabUtils.makeDirId(parts[1], parts[2]);
                if (dirWithoutRevision.equals(itemDir)) {
                    return name;
                }
            }
        }

        return null;
    }
}

