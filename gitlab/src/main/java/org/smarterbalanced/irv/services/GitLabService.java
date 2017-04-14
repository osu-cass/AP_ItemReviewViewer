package org.smarterbalanced.irv.services;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarterbalanced.irv.config.SettingsReader;
import org.smarterbalanced.irv.model.Metadata;
import org.smarterbalanced.irv.model.ItemCommit;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Component
@Scope("singleton")
public class GitLabService implements IGitLabService {
	
	private static String DESTINATION_ZIP_FILE_LOCATION;
	private static final String FILE_EXTENTION = ".zip";

	private static final int BUFFER_SIZE = 4096;

	private static final Logger _logger = LoggerFactory.getLogger(GitLabService.class);
	
	public GitLabService() {
		try {
			DESTINATION_ZIP_FILE_LOCATION = SettingsReader.get("iris.ZipFileLocation");
		} catch (Exception exp) {
			_logger.error("Error loading zip file location", exp);
		}
	}
	

	/* (non-Javadoc)
	 * @see org.smarterbalanced.irv.services.IGitLabService#downloadItem(java.lang.String)
	 */
	@Override
	public boolean downloadItem(String itemNumber) throws GitLabException {
		String itemURL = GitLabUtils.getGitLabItemUrl(itemNumber);
		
		try {

			URL gitLabItemURL = new URL(itemURL);
			
			String downloadLocation = DESTINATION_ZIP_FILE_LOCATION + itemNumber + FILE_EXTENTION;
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
	 * @see org.smarterbalanced.irv.services.IGitLabService#getMetaData(java.lang.String)
	 */
	@Override
	public Metadata getMetadata(String itemNumber) throws GitLabException  {
		try {

			if (!isItemExistsLocally(itemNumber) && downloadItem(itemNumber))
				unzip(itemNumber);

			String metadataFilePath = DESTINATION_ZIP_FILE_LOCATION + itemNumber + File.separator + "metadata.xml";
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
			}catch (JAXBException e) {
				_logger.error("Error parsing metadata file." + e.getMessage());
			}
			
			return null;
			
			
		} catch (Exception e) {
			// TODO: handle exception
			
			throw new GitLabException(e);
		}

	}
	

	
	/* (non-Javadoc)
	 * @see org.smarterbalanced.irv.services.IGitLabService#isItemExistsLocally(java.lang.String)
	 */
	@Override
	public boolean isItemExistsLocally(String itemNumber) {

		try {
			String zipFilePath = DESTINATION_ZIP_FILE_LOCATION + itemNumber + FILE_EXTENTION;
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
		
		String zipFilePath = DESTINATION_ZIP_FILE_LOCATION + itemNumber + FILE_EXTENTION;
		_logger.info("Unzipping the File:" + zipFilePath + " to Location:" + DESTINATION_ZIP_FILE_LOCATION + " Started");
		String _contentPath = "";
		ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
		File dir = null;
		ZipEntry entry = zipIn.getNextEntry();
		
		String rootDirectoryPath = null;
		int index = 1;
		
		while (entry != null) {
			String filePath = DESTINATION_ZIP_FILE_LOCATION + File.separator + entry.getName();
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
			rootDirectory.renameTo(new File(DESTINATION_ZIP_FILE_LOCATION + itemNumber));
		}
		
		if (rootDirectoryPath != null) {
			_contentPath = DESTINATION_ZIP_FILE_LOCATION + itemNumber;
		} else {
			_logger.error("Downloaed File:" + zipFilePath + " to Location:" + DESTINATION_ZIP_FILE_LOCATION + " is corrupted.make sure Item Exists");
			new File(zipFilePath).delete();
		}
		_logger.info("Unzipping the File:" + zipFilePath + " to Location:" + DESTINATION_ZIP_FILE_LOCATION + " Success");

		return _contentPath;
	}
    
	public List<ItemCommit> getItemCommits(String itemName) throws GitLabException {
		try {
			String[] parts = itemName.split("-");
			
			return getItemCommits(parts[0], parts[1], parts[2]);
		
		} catch (Exception e) {
			// TODO: handle exception
			throw new GitLabException(e);
		}
	}

	
	/* (non-Javadoc)
	 * @see org.smarterbalanced.irv.services.IGitLabService#getItemCommits(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<ItemCommit> getItemCommits(String type, String bankId, String itemNumber) throws GitLabException{
		//init("commits");
		
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
			// TODO: handle exception
		}
		return new ArrayList<ItemCommit>();
	}
	
	
}
