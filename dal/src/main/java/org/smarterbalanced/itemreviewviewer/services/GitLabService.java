package org.smarterbalanced.itemreviewviewer.services;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarterbalanced.itemreviewviewer.model.ItemCommit;
import org.smarterbalanced.itemreviewviewer.model.ItemTag;
import org.smarterbalanced.itemreviewviewer.model.MetaData;
import org.smarterbalanced.itemviewerservice.dal.Config.SettingsReader;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Scope("singleton")
public class GitLabService implements IGitLabService {
	
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
	

	/* (non-Javadoc)
	 * @see org.smarterbalanced.itemreviewviewer.services.IGitLabService#downloadItem(java.lang.String)
	 */
	@Override
	public boolean downloadItem(String itemNumber) throws ContentException {
		String itemURL = getGitLabItemUrl(itemNumber);
		
		try {

			URL gitLabItemURL = new URL(itemURL);
			
			String downloadLocation = DESTINATION_ZIP_FILE_LOCATION + itemNumber + FILE_EXTENTION;
			File zipFile = new File(downloadLocation);
			_logger.info("Getting the Item with URL:" + itemURL + " with file name:" + downloadLocation + " Started");
			boolean isSucceed = true;
			
			ReadableByteChannel rbc = Channels.newChannel(gitLabItemURL.openStream());
			FileOutputStream fos = new FileOutputStream(downloadLocation);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			
			fos.close();
			rbc.close();
			
			_logger.info("Getting the Item with URL:" + itemURL + " with file name:" + downloadLocation + " Success");
			return isSucceed;

		} catch (Exception e) {
			// TODO: handle exception
			_logger.error("IO Exception occurred while Getting the Item with URL:" + itemURL + e.getMessage());
			throw new ContentException(String.format("UnKnown Exception Occurred while getting item ID: %s" + e.getMessage()));

		}

	}
	
	/* (non-Javadoc)
	 * @see org.smarterbalanced.itemreviewviewer.services.IGitLabService#getMetaData(java.lang.String)
	 */
	@Override
	public MetaData getMetaData(String itemNumber) {
		try {

			if (!isItemExistsLocally(itemNumber) && downloadItem(itemNumber))
				unzip(itemNumber);

			String metadataFilePath = DESTINATION_ZIP_FILE_LOCATION + itemNumber + File.separator + "metadata.xml";
			//String xmlFile = metadataFile.getAbsolutePath();
			MetaData metaData = null;
			try {
				
				_logger.info("unmarshalling metadata file started");
				
				JAXBContext jc = JAXBContext.newInstance(MetaData.class);
				Unmarshaller unmarshaller = jc.createUnmarshaller();
				Path path = Paths.get(metadataFilePath);
		    	BufferedReader reader = Files.newBufferedReader(path, Charset.forName("UTF-8"));
				metaData = (MetaData) unmarshaller.unmarshal(reader);
				reader.close();
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
	

	
	/* (non-Javadoc)
	 * @see org.smarterbalanced.itemreviewviewer.services.IGitLabService#isItemExistsLocally(java.lang.String)
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
	 * @see org.smarterbalanced.itemreviewviewer.services.IGitLabService#unzip(java.lang.String)
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
	
    public String getGitLabUrl() {
    	return SettingsReader.get("gitlab.url");
    }

    public String getPrivateToken() {
    	return SettingsReader.get("gitlab.private.token");
    }
    
    /* (non-Javadoc)
	 * @see org.smarterbalanced.itemreviewviewer.services.IGitLabService#getGitLabItemUrl(java.lang.String)
	 */
    @Override
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

    
	public List<ItemTag> getItemTags(String itemName) {
		//init("tags");
		return null;
	}

	public void add(ItemTag itemTag) {
		//itemVersions.add(itemVersion);
	}

	
	public List<ItemCommit> getItemCommits(String itemName) {
		//init("commits");
		
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		return null;
	}

	
	public List<ItemTag> getItemTags(String type, String bankId, String itemNumber) {
		//init("tags");
		return null;
	}

	/* (non-Javadoc)
	 * @see org.smarterbalanced.itemreviewviewer.services.IGitLabService#getItemCommits(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<ItemCommit> getItemCommits(String type, String bankId, String itemNumber) {
		//init("commits");
		
		List<ItemCommit> itemCommits = new ArrayList<ItemCommit>();
		
		try {
			String id = type+ "-" + bankId + "-" + itemNumber;
			
			String queryString = String.format("?type=%s&bankId=%s&id=%s", type, bankId, itemNumber) ;
			
			String  gitLabUrl = getItemCommitsUrl(id);
			URL url = new URL(gitLabUrl);
			
			boolean isHttps = false;
			
			//handle https connections
			
			if(gitLabUrl.startsWith("https"))
				isHttps = true;
			
			HttpURLConnection conn = null;
			
			if(isHttps)
				conn = (HttpsURLConnection) url.openConnection();
			else
				conn = (HttpURLConnection) url.openConnection();

			
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			
			boolean redirect = false;
			
			// normally, 3xx is redirect
			int status = conn.getResponseCode();
			if (status != HttpURLConnection.HTTP_OK) {
				if (status == HttpURLConnection.HTTP_MOVED_TEMP
					|| status == HttpURLConnection.HTTP_MOVED_PERM
						|| status == HttpURLConnection.HTTP_SEE_OTHER
						|| status == 307)
				redirect = true;
			}

			_logger.info("Response Code ... " + status);
			
			if (redirect) {

				// get redirect url from "location" header field
				String newUrl = conn.getHeaderField("Location");
				
				isHttps = false;
				
				if(newUrl.startsWith("https"))
					isHttps = true;

				// get the cookie if need, for login
				//String cookies = conn.getHeaderField("Set-Cookie");

				// open the new connnection again
				if(isHttps)
					conn = (HttpsURLConnection) new URL(newUrl).openConnection();
				else
					conn = (HttpURLConnection) new URL(newUrl).openConnection();

				//conn.setRequestProperty("Cookie", cookies);
				conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
				conn.addRequestProperty("User-Agent", "Mozilla");
				conn.addRequestProperty("Referer", "google.com");

				_logger.info("Redirect to URL : " + newUrl);

			}

			
			if (status > 400) {
				
				throw new RuntimeException("Failed to connect to GitLab URL: " + url + " HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			//if(versionsType.equalsIgnoreCase("commits")) {
				itemCommits = getItemCommits(br);
				
				for (ItemCommit itemCommit : itemCommits) {
					//itemCommit.setLink(request.getContextPath() + irisPage +  queryString + "&version=" + itemCommit.getCommitId());
				}
			//}
			/*
			else if(versionsType.equalsIgnoreCase("tags")) {
				itemVersions = getItemVersions(br);
				
				for (ItemTag itemVersion : itemVersions) {
					itemVersion.setLink(request.getContextPath() + irisPage +  queryString + "&version=" + itemVersion.getCommitId());
				}
			}
			*/

			br.close();
			conn.disconnect();

		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return itemCommits;
		
		
	}
	
	
	private List<ItemTag> getItemVersions(Reader reader) {

		List<ItemTag> itemVersions = new ArrayList<ItemTag>();

		try {
			
			JSONParser jsonParser = new JSONParser();
			JSONArray names = (JSONArray) jsonParser.parse(reader);

			Iterator i = names.iterator();
			
			// take each value from the json array separately
			while (i.hasNext()) {
				ItemTag itemVersion = new ItemTag();
				JSONObject innerObj = (JSONObject) i.next();				
				String tagName = (String)innerObj.get("name");
				
				JSONObject commitObj = (JSONObject) innerObj.get("commit");
				String commiId = (String)commitObj.get("id");
				
				itemVersion.setTagName(tagName);
				itemVersion.setCommitId(commiId);
				itemVersion.setName("Version " + tagName);
				
				itemVersions.add(itemVersion);

			}

			
		} catch (Exception e) {
			// TODO: handle exception
			  _logger.error ("Error getting tags form GitLab", e);

		}
		return itemVersions;

	}

	private List<ItemCommit> getItemCommits(Reader reader) {

		List<ItemCommit> itemCommits_ = new ArrayList<ItemCommit>();

		try {
			
			JSONParser jsonParser = new JSONParser();
			JSONArray names = (JSONArray) jsonParser.parse(reader);

			Iterator i = names.iterator();
			
			// take each value from the json array separately
			int count = names.size();
			while (i.hasNext()) {
				ItemCommit itemCommit = new ItemCommit();
				JSONObject innerObj = (JSONObject) i.next();				
				
				itemCommit.setId(count--);
				itemCommit.setCommitId((String)innerObj.get("id"));
				itemCommit.setTitle((String)innerObj.get("title"));
				itemCommit.setShortId((String)innerObj.get("short_id"));
				itemCommit.setAuthorName((String)innerObj.get("author_name"));
				itemCommit.setAuthorEmail((String)innerObj.get("author_email"));
				
				//String creationDateStr = (String)innerObj.get("created_at");
				
				
				//itemCommit.setCreationDate((String)innerObj.get("created_at"));
				itemCommit.setMessage((String)innerObj.get("message"));

				itemCommits_.add(itemCommit);

			}

			
		} catch (Exception e) {
			// TODO: handle exception
			  _logger.error ("Error getting commits form GitLab", e);

		}
		return itemCommits_;

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
