package org.smarterbalanced.itemreviewviewer.services;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarterbalanced.itemreviewviewer.model.MetaData;
import org.smarterbalanced.itemviewerservice.dal.Config.SettingsReader;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Scope("singleton")
public class GitLabService {
	
	private static String DESTINATION_ZIP_FILE_LOCATION;
	private static final String FILE_EXTENTION = ".zip";

	private static final int BUFFER_SIZE = 4096;
	private static final String USER_AGENT = "User-Agent";
	private static final String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT6.3; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0";
	//private static final String REFERRER = "Referer";

	private static final Logger _logger = LoggerFactory.getLogger(GitLabService.class);
	
	public GitLabService() {
		try {
			DESTINATION_ZIP_FILE_LOCATION = SettingsReader.get("iris.ZipFileLocation");
		} catch (Exception exp) {
			_logger.error("Error loading zip file location or git lab url", exp);
		}
	}
	

	public boolean downloadItem(String itemNumber) throws ContentException {

		try {

			String itemURL = getGitLabItemUrl(itemNumber);
			
			String downloadLocation = DESTINATION_ZIP_FILE_LOCATION + itemNumber + FILE_EXTENTION;
			_logger.info("Getting the Item with URL:" + itemURL + " with file name:" + downloadLocation + " Started");
			boolean isSucceed = true;
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet(itemURL.toString());
			httpGet.addHeader(USER_AGENT, USER_AGENT_VALUE);
			// httpGet.addHeader(REFERRER, REFERRER_VALUE);
			try {
				CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity fileEntity = httpResponse.getEntity();
				if (fileEntity != null) {
					FileUtils.copyInputStreamToFile(fileEntity.getContent(), new File(downloadLocation));
				}
			} catch (IOException e) {
				_logger.error("IO Exception occurred while Getting the Item with URL:" + itemURL + e.getMessage());
				throw new ContentException(String.format("UnKnown Exception Occurred while getting item ID: %s" + e.getMessage()));
			}
			httpGet.releaseConnection();
			_logger.info("Getting the Item with URL:" + itemURL + " with file name:" + downloadLocation + " Success");
			return isSucceed;

		} catch (Exception e) {
			// TODO: handle exception
		}

		return false;
	}
	
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
	
	public MetaData getMetaData(String itemNumber) {
		try {
			
			if(!isItemExistsLocally(itemNumber)) {
				downloadItem(itemNumber);
				unzip(itemNumber);
			}	
			
			String metadataFilePath = DESTINATION_ZIP_FILE_LOCATION + itemNumber + File.separator + "metadata.xml";
			File metadataFile = new File(metadataFilePath);
				String xmlFile = metadataFile.getAbsolutePath();
				MetaData metaData = null;
				try {
					_logger.info("unmarshalling metadata file started");
					JAXBContext jc = JAXBContext.newInstance(MetaData.class);
					Unmarshaller unmarshaller = jc.createUnmarshaller();
					File xml = new File(xmlFile);
					metaData = (MetaData) unmarshaller.unmarshal(xml);
					_logger.info("unmarshalling metadata file completed");
				} catch (JAXBException e) {
					_logger.error("Error parsing metadata file.", e);
				} catch (Exception e) {
					_logger.error("unknown error occurred while parsing metadata file.", e);
				}
			return metaData;
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return null;
	}

    public String getGitLabUrl() {
    	return SettingsReader.get("gitlab.url");
    }

    public String getPrivateToken() {
    	return SettingsReader.get("gitlab.private.token");
    }
    
    public String getGitLabItemUrl(String itemName) {
    	try {
    		String[] parts = itemName.split("-");
    		
    		String versionNumber = ""; 
    		if(parts.length > 3)
    			versionNumber = parts[3];
    		
    		String baseItemName = parts[0] + "-" + parts[1] + "-" + parts[2]; 
    		
    		if(StringUtils.isEmpty(versionNumber))
    			return getItemUrl(baseItemName);
    			
    		return getItemCommitUrl(baseItemName, versionNumber);
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
    	return "";
    }

    public String getItemUrl(String itemNumber) {
		return getGitLabUrl() + itemNumber + "/repository/archive.zip?private_token=" + getPrivateToken();
    }
    
    public String getItemCommitUrl(String itemName, String commitId) {
		return getGitLabUrl() + itemName + "/repository/archive.zip?private_token=" + getPrivateToken() + "&sha=" + commitId;

    }

    public String getItemCommitsUrl(String itemName) {
    	 return getGitLabUrl() + itemName + "/repository/commits?private_token=" + getPrivateToken();
    }

    public String getItemTagsUrl(String itemName) {
    	return getGitLabUrl() + itemName + "/repository/tags?private_token=" + getPrivateToken();
    }

    
}
