package org.smarterbalanced.itemreviewviewer.web.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarterbalanced.itemreviewviewer.web.config.SettingsReader;
import org.smarterbalanced.itemreviewviewer.web.services.models.ItemRelease;
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
import tds.itemrenderer.data.IITSDocument;
import tds.itemrenderer.data.ITSResource;
import tds.itemrenderer.data.ITSTutorial;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


@Service
public class GitLabService implements IGitLabService {
    private static final Logger _logger = LoggerFactory.getLogger(GitLabService.class);

    private static String ZIP_FILE_LOCATION;
    private static String CONTENT_LOCATION;
    private static final String ZIP_EXTENSION = ".zip";
    private static final String XML_EXTENSION = ".xml";

    private static final int BUFFER_SIZE = 4096;

    public GitLabService() {
        try {
            ZIP_FILE_LOCATION = SettingsReader.getZipFileLocation();
            CONTENT_LOCATION = SettingsReader.readIrisContentPath() + "gitlab/";
        } catch (Exception exp) {
            _logger.error("Error loading zip file location", exp);
        }
    }


    /* (non-Javadoc)
     * @see org.smarterbalanced.irv.services.IGitLabService#downloadItem(java.lang.String)
     */
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

    /* (non-Javadoc)
     * @see org.smarterbalanced.irv.services.IGitLabService#isItemExistsLocally(java.lang.String)
     */
    @Override
    public boolean isItemExistsLocally(String itemNumber) {

        try {
            String zipFilePath = ZIP_FILE_LOCATION + itemNumber + ZIP_EXTENSION;
            File f = new File(zipFilePath);
            _logger.info("Checking the File Already Exists:" + f.exists());
            return f.exists();

        } catch (Exception e) {
            // TODO: handle exception
        }

        return false;

    }


    /* (non-Javadoc)
     * @see org.smarterbalanced.irv.services.IGitLabService#unzip(java.lang.String)
     */
    @Override
    public String unzip(String namespace, String itemNumber) throws IOException, GitLabException {

        String zipFilePath = ZIP_FILE_LOCATION + itemNumber + ZIP_EXTENSION;
        _logger.info("Unzipping the File:" + zipFilePath + " to Location:" + CONTENT_LOCATION + " Started");
        String _contentPath = "";
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
                if(index ==1) {
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

        //rename root directory to item-blankid-id-version format
        if(rootDirectoryPath!=null) {
            File rootDirectory = new File(rootDirectoryPath);
            rootDirectory.renameTo(new File(CONTENT_LOCATION + itemNumber));
            _contentPath = CONTENT_LOCATION + itemNumber;
        } else {
            _logger.error("Downloaed File:" + zipFilePath + " to Location:" + CONTENT_LOCATION + " is corrupted.make sure Item Exists");
            new File(zipFilePath).delete();
        }
        _logger.info("Unzipping the File:" + zipFilePath + " to Location:" + CONTENT_LOCATION + " Success");

        //rename item file if no bankKey
        String noBankKeyItemId = GitLabUtils.extractItemId(namespace, itemNumber);
        if (!noBankKeyItemId.equals(itemNumber)) {
            _logger.info("No BankKey in namespace: " + namespace + " | itemNumber: " + itemNumber);
            String orgFilePath = _contentPath + "/" + noBankKeyItemId + XML_EXTENSION;
            String newFilePath = _contentPath + "/" + itemNumber + XML_EXTENSION;
            File item = new File(orgFilePath);
            item.renameTo(new File(newFilePath));
            _logger.info("Renamed the file from " + orgFilePath + " to " + newFilePath);

            ItemIdUtils.ItemId itemId = ItemIdUtils.parseItemId(itemNumber);
            if (itemId == null) {
                _logger.info("Failed to parse an ItemId with string " + itemNumber);
            }
            _changeBankKey(newFilePath, itemId.bankKey);
        }

        return _contentPath;
    }

    private void _changeBankKey(String filePath, String bankKey) throws GitLabException {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(filePath);

            // Get the staff element by tag name directly
            Node item = doc.getElementsByTagName("item").item(0);

            // update staff attribute
            NamedNodeMap attr = item.getAttributes();
            Node nodeAttr = attr.getNamedItem("bankkey");
            nodeAttr.setTextContent(bankKey);

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
            namespaces = objectMapper.readValue(output, new TypeReference<List<Namespace>>(){});

            /* if the number of namespaces is over current page size, additional api calls are required */
            totalPages = Integer.parseInt(response.getHeaders().get(TOTAL_PAGES).get(0));
            ++curPage;
            for (; curPage <= totalPages; curPage++) {
                _getRemainingNamespaces(PAGE_SIZE, curPage, namespaces);
            }

            _removeNoProjectNamespaces(namespaces);

        } catch (Exception e) {
            _logger.error("Failed to get namespaces, URL: " + nameSpacesUrl);
            throw new GitLabException(e);
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
            _namespaces = objectMapper.readValue(output, new TypeReference<List<Namespace>>(){});

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
                List<Project> projects = objectMapper.readValue(output, new TypeReference<List<Project>>(){});

                if (projects.size() == 0) {
                    iterator.remove();
                    continue;
                }

                String name = projects.get(0).getName();
                String parts[] = name.split("-");
                // NOTE: It is temporary implementation before figuring out how bankKey is used in project names
                if (parts.length > 1) {
                    if (parts.length == 3) {
                        namespace.setHasBankKey(true);
                    } else {
                        namespace.setHasBankKey(false);
                    }
                } else {
                    namespace.setHasBankKey(false);
                    namespace.setBankKey(GitLabUtils.noBankKeyNamespaceHash.get(namespace.name));
                }
            }
        } catch (Exception e) {
            _logger.error("Failed to remove no project namespaces");
            throw new GitLabException(e);
        }
    }

    public ItemDocument getItemScoring(String namespace, String itemNumber) throws GitLabException{
        String[] parts = itemNumber.split("-");
        String baseItemName = parts[0] + "-" + parts[1] + "-" + parts[2];
        String rubricFilePath = CONTENT_LOCATION + itemNumber + File.separator + baseItemName.toLowerCase() + XML_EXTENSION;
        try{
            if (!isItemExistsLocally(itemNumber) && downloadItem(namespace, itemNumber))
                unzip(namespace, itemNumber);
            try {
                _logger.info("unmarshalling item file started");

                FileInputStream fis = new FileInputStream(rubricFilePath);
                XMLStreamReader xsr = XMLInputFactory.newFactory().createXMLStreamReader(fis);
                XMLReaderWithoutNamespace xr = new XMLReaderWithoutNamespace(xsr);

                JAXBContext jc = JAXBContext.newInstance(ItemDocument.class);
                Unmarshaller unmarshaller = jc.createUnmarshaller();
                    ItemDocument content = (ItemDocument) unmarshaller.unmarshal(xr);
                fis.close();

                _logger.info("unmarshalling metadata file completed");

                return content;

            }catch (FileNotFoundException e) {
                _logger.error("Metadata file not found. " + e.getMessage());
                throw new GitLabException(e);
            }
            catch (JAXBException e) {
                _logger.error("Error parsing metadata file." + e.getMessage());
                throw new GitLabException(e);
            }
        } catch (Exception e) {
            throw new GitLabException(e);
        }
    }

    public List<ItemCommit> getItemCommits(String namepspace, String itemNumber) throws GitLabException {
        try {
            String[] parts = itemNumber.split("-");

            return getItemCommits(namepspace, parts[0], parts[1], parts[2]);

        } catch (Exception e) {
            // TODO: handle exception
            throw new GitLabException(e);
        }
    }

    /* (non-Javadoc)
     * @see org.smarterbalanced.irv.services.IGitLabService#getItemCommits(java.lang.String, java.lang.String, java.lang.String)
    */
    public List<ItemCommit> getItemCommits(String namespace, String type, String bankId, String itemNumber) throws GitLabException{
        try {
            Client client = Client.create();
            String id = type+ "-" + bankId + "-" + itemNumber;

            String  itemCommitsUrl = GitLabUtils.getItemCommitsUrl(namespace, id);

            WebResource webResourceGet = client.resource(itemCommitsUrl);
            ClientResponse response = webResourceGet.accept("application/json").get(ClientResponse.class);

            if (response.getStatus() != 200) {
                throw new GitLabException("Could not get ItemCommits; Failed : HTTP error code : "
                        + response.getStatus()
                        + " : URL : " + itemCommitsUrl);
            }

            String output = response.getEntity(String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            List<ItemCommit> itemCommits = objectMapper.readValue(output, new TypeReference<List<ItemCommit>>(){});

            return itemCommits;

        } catch (Exception e) {
            throw new GitLabException(e);
        }
    }

    /* (non-Javadoc)
     * @see org.smarterbalanced.irv.services.IGitLabService#getMetaData(java.lang.String)
     */
    @Override
    public Metadata getMetadata(String namespace, String itemNumber) throws GitLabException  {
        try {

            if (!isItemExistsLocally(itemNumber) && downloadItem(namespace, itemNumber))
                unzip(namespace, itemNumber);

            String metadataFilePath = CONTENT_LOCATION + itemNumber + File.separator + "metadata.xml";

            try {
                _logger.info("unmarshalling metadata file started");

                FileInputStream fis = new FileInputStream(metadataFilePath);
                XMLStreamReader xsr = XMLInputFactory.newFactory().createXMLStreamReader(fis);

                XMLReaderWithoutNamespace xr = new XMLReaderWithoutNamespace(xsr);


                JAXBContext jc = JAXBContext.newInstance(Metadata.class);

                Unmarshaller unmarshaller = jc.createUnmarshaller();

                Metadata metadata = (Metadata) unmarshaller.unmarshal(xr);

                fis.close();


                _logger.info("unmarshalling metadata file completed");

                return metadata;

            }catch (FileNotFoundException e) {
                _logger.error("Metadata file not found. " + e.getMessage());
                throw new GitLabException(e);
            }catch (JAXBException e) {
                _logger.error("Error parsing metadata file." + e.getMessage());
                throw new GitLabException(e);
            }
        } catch (Exception e) {
            throw new GitLabException(e);
        }

    }
    public ItemRelease getItemData(String namespace, String itemNumber) {
        try {

            if (!isItemExistsLocally(itemNumber) && downloadItem(namespace, itemNumber))
                unzip(namespace, itemNumber);

            String itemPath = CONTENT_LOCATION + itemNumber+ File.separator + itemNumber + ".xml";
            try {
                _logger.info("unmarshalling ItemData file started");
                FileInputStream isIP = new FileInputStream(itemPath);
                XMLStreamReader isXSR = XMLInputFactory.newFactory().createXMLStreamReader(isIP);
                XMLReaderWithoutNamespace ipxr = new XMLReaderWithoutNamespace(isXSR);
                JAXBContext iJC = JAXBContext.newInstance(ItemRelease.class);
                Unmarshaller itemMarsh = iJC.createUnmarshaller();
                ItemRelease itemdata = (ItemRelease) itemMarsh.unmarshal(ipxr);
                isIP.close();
                _logger.info("unmarshalling metadata file completed");
                return itemdata;

            }catch (FileNotFoundException e) {
                _logger.error("Metadata file not found. " + e.getMessage());
                throw new GitLabException(e);
            }catch (JAXBException e) {
                _logger.error("Error parsing metadata file." + e.getMessage());
                throw new GitLabException(e);
            }
        } catch (Exception e) {
            throw new GitLabException(e);
        }
    }

    @Override
    public String getClaim( String itemNumber) throws GitLabException {

            String itemPath = CONTENT_LOCATION + "ItemsPatch.xml";
            try {
                _logger.info("unmarshalling ItemPatch file started");
                FileInputStream isIP = new FileInputStream(itemPath);
                XMLStreamReader isXSR = XMLInputFactory.newFactory().createXMLStreamReader(isIP);
                XMLReaderWithoutNamespace ipxr = new XMLReaderWithoutNamespace(isXSR);
                JAXBContext iJC = JAXBContext.newInstance(ItemsPatchModel.class);
                Unmarshaller itemMarsh = iJC.createUnmarshaller();
                ItemsPatchModel itemdata = (ItemsPatchModel) itemMarsh.unmarshal(ipxr);
                isIP.close();
                _logger.info("unmarshalling metadata file completed");
                for (int i = 0; i < itemdata.getRow().length; i++) {
                    if(itemdata.getRow()[i].getItemId() == itemNumber) {
                        return itemdata.getRow()[i].getClaim();
                    }
                }
            }catch (FileNotFoundException e) {
                _logger.error("Metadata file not found. " + e.getMessage());
                throw new GitLabException(e);
            }catch (JAXBException e) {
                _logger.error("Error parsing metadata file." + e.getMessage());
                throw new GitLabException(e);
            }
         catch (Exception e) {
            throw new GitLabException(e);
        }
        return null;
    }

    public ItemMetadataModel getItemMetadata(String namespace, String itemId, String section) throws GitLabException, FileNotFoundException{
        String[] parts = itemId.split("-");
        String baseItemName = parts[0] + "-" + parts[1] + "-" + parts[2];
        List<ItemScoringOptionModel> opts = new ArrayList<ItemScoringOptionModel>();
        Metadata md;
        ItemDocument item;
        try{
            md = getMetadata(namespace, itemId);
        } catch(GitLabException e){
            md = getMetadata(namespace, baseItemName);
        }
        try{
            item = getItemScoring(namespace, itemId);
        } catch (GitLabException e){
            throw new FileNotFoundException(e.getMessage());
        }

        List<RubricModel> rubrics = new ArrayList<RubricModel>();
        List<ItemScoringModel> contentList = item.item.content;
        if(contentList.size() > 0){
            for(ItemScoringModel content : contentList){
                List<RubricModel> list = content.getRubrics();
                if(content.getScoringOptions() != null && content.getScoringOptions().size() > 0){
                    opts.addAll(content.getScoringOptions());
                }
                if(list != null && list.size() > 0){
                    for(RubricModel rubric : list){
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
        return new ItemMetadataModel(parts[0], parts[1], parts[2], section, md.getSmarterAppMetadata(), score);
    }

    /* (non-Javadoc)
     * @see org.smarterbalanced.irv.services.IGitLabService#downloadAssociatedItems(tds.itemrenderer.data.IITSDocument)
     */


    public boolean isItemAccomExists (String itemNumber, String ext) {
        if(isItemExistsLocally(itemNumber)) {
            String itemPath = CONTENT_LOCATION + itemNumber + File.separator;
            File directory = new File(itemPath);
            Collection files = FileUtils.listFiles(directory, new WildcardFileFilter("*." + ext), null);
            if (files.size() > 0) {
                return true;
            }

        }
        return false;
    }

    public void downloadAssociatedItems(String namespace, IITSDocument doc) throws GitLabException{
        ITSTutorial tutorial = doc.getTutorial();
        List<ITSResource> resources = doc.getResources();
        try{
            if(tutorial != null){

                String tutorialId = new String("Item-" +tutorial._bankKey + "-" + tutorial._id);
                if (!isItemExistsLocally(tutorialId) && downloadItem(namespace, tutorialId)){
                    unzip(namespace, tutorialId);
                }

                if(resources != null){
                    for(ITSResource resource: resources){
                        String resourceId = new String("Item-" +resource._bankKey + "-" + resource._id);
                        if (!isItemExistsLocally(resourceId) && downloadItem(namespace, resourceId)){
                            unzip(namespace, resourceId);
                        }
                    }
                }
            }
        }catch(Exception e){
            throw new GitLabException(e);
        }

    }

}

