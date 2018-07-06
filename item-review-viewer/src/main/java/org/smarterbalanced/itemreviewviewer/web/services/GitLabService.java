package org.smarterbalanced.itemreviewviewer.web.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarterbalanced.itemreviewviewer.web.config.SettingsReader;
import org.smarterbalanced.itemreviewviewer.web.models.metadata.ItemMetadataModel;
import org.smarterbalanced.itemreviewviewer.web.models.scoring.ItemScoringModel;
import org.smarterbalanced.itemreviewviewer.web.models.scoring.ItemScoringOptionModel;
import org.smarterbalanced.itemreviewviewer.web.models.scoring.RubricModel;
import org.smarterbalanced.itemreviewviewer.web.services.models.ItemCommit;
import org.smarterbalanced.itemreviewviewer.web.services.models.ItemDocument;
import org.smarterbalanced.itemreviewviewer.web.services.models.Metadata;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import tds.itemrenderer.data.IITSDocument;
import tds.itemrenderer.data.ITSResource;
import tds.itemrenderer.data.ITSTutorial;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

;


@Component
@Scope("singleton")
public class GitLabService implements IGitLabService {

    private static String ZIP_FILE_LOCATION;
    private static String CONTENT_LOCATION;
    private static final String ZIP_EXTENSION = ".zip";
    private static final String XML_EXTENSION = ".xml";


    private static final int BUFFER_SIZE = 4096;

    private static final Logger _logger = LoggerFactory.getLogger(GitLabService.class);

    public GitLabService() {
        try {
            ZIP_FILE_LOCATION = SettingsReader.get("iris.ZipFileLocation");
            CONTENT_LOCATION = SettingsReader.get("iris.ContentPath") + "gitlab/";
        } catch (Exception exp) {
            _logger.error("Error loading zip file location", exp);
        }
    }


    /* (non-Javadoc)
     * @see org.smarterbalanced.irv.services.IGitLabService#downloadItem(java.lang.String)
     */
    public boolean downloadItem(String itemNumber) throws GitLabException {
        String itemURL = GitLabUtils.getGitLabItemUrl(itemNumber);
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
            // TODO: handle exception
            _logger.error("IO Exception occurred while Getting the Item with URL:" + itemURL + e.getMessage());
            throw new GitLabException("UnKnown Exception Occurred while getting item ID: %s" + e.getMessage());

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
    public String unzip(String itemNumber) throws IOException {

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

        return _contentPath;
    }


    public ItemDocument getItemScoring(String itemNumber) throws GitLabException{
        String[] parts = itemNumber.split("-");
        String baseItemName = parts[0] + "-" + parts[1] + "-" + parts[2];
        String rubricFilePath = CONTENT_LOCATION + itemNumber + File.separator + baseItemName.toLowerCase() + XML_EXTENSION;
        try{
            if (!isItemExistsLocally(itemNumber) && downloadItem(itemNumber))
                unzip(itemNumber);
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

    public List<ItemCommit> getItemCommits(String itemNumber) throws GitLabException {
        try {
            String[] parts = itemNumber.split("-");

            return getItemCommits(parts[0], parts[1], parts[2]);

        } catch (Exception e) {
            // TODO: handle exception
            throw new GitLabException(e);
        }
    }

    /* (non-Javadoc)
     * @see org.smarterbalanced.irv.services.IGitLabService#getItemCommits(java.lang.String, java.lang.String, java.lang.String)
    */
    public List<ItemCommit> getItemCommits(String type, String bankId, String itemNumber) throws GitLabException{
        try {
            Client client = Client.create();
            String id = type+ "-" + bankId + "-" + itemNumber;

            String  itemCommitsUrl = GitLabUtils.getItemCommitsUrl(id);

            WebResource webResourceGet = client.resource(itemCommitsUrl);
            ClientResponse response = webResourceGet.accept("application/json").get(ClientResponse.class);

            if (response.getStatus() != 200) {
                throw new GitLabException("Could not get ItemCommits; Failed : HTTP error code : "	+ response.getStatus());
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
    public Metadata getMetadata(String itemNumber) throws GitLabException  {
        try {

            if (!isItemExistsLocally(itemNumber) && downloadItem(itemNumber))
                unzip(itemNumber);

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

    public ItemMetadataModel getItemMetadata(String itemId, String section) throws GitLabException, FileNotFoundException{
        String[] parts = itemId.split("-");
        String baseItemName = parts[0] + "-" + parts[1] + "-" + parts[2];
        List<ItemScoringOptionModel> opts = new ArrayList<ItemScoringOptionModel>();
        Metadata md;
        ItemDocument item;
        try{
            md = getMetadata(itemId);
        } catch(GitLabException e){
            md = getMetadata(baseItemName);
        }
        try{
            item = getItemScoring(itemId);
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
    public void downloadAssociatedItems(IITSDocument doc) throws GitLabException{
        ITSTutorial tutorial = doc.getTutorial();
        List<ITSResource> resources = doc.getResources();
        try{
            if(tutorial != null){

                String tutorialId = new String("Item-" +tutorial._bankKey + "-" + tutorial._id);
                if (!isItemExistsLocally(tutorialId) && downloadItem(tutorialId)){
                    unzip(tutorialId);
                }

                if(resources != null){
                    for(ITSResource resource: resources){
                        String resourceId = new String("Item-" +resource._bankKey + "-" + resource._id);
                        if (!isItemExistsLocally(resourceId) && downloadItem(resourceId)){
                            unzip(resourceId);
                        }
                    }
                }
            }
        }catch(Exception e){
            throw new GitLabException(e);
        }

    }

}

