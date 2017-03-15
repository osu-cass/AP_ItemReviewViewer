/**
 * 
 */
package org.smarterbalanced.itemreviewviewer.managed.beans;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarterbalanced.itemviewerservice.core.Models.ItemCommit;
import org.smarterbalanced.itemviewerservice.core.Models.ItemVersion;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;

import AIR.Common.Configuration.AppSettingsHelper;

/**
 * @author kthotti
 *
 */
@ManagedBean(name="versionsBacking")
@Scope(value="request")
public class VersionsBacking {
	
	private static final Logger _logger = LoggerFactory.getLogger (VersionsBacking.class);

	
	private List<ItemVersion> itemVersions;

	
	private List<ItemCommit> itemCommits;

	private String gitLabUrl;
	private  final String GIT_LAB_ITEM_VERSION_URL="iris.GitLabItemVersionUrl";
	private  final String ITEM_REVIEW_PAGE="iris.ItemReviewPage";

	private String irisPage;
	
	
	/**
	 * 
	 */
	public VersionsBacking() {

	}
	
	private void init(String versionsType) {
		try {
			HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			//String type = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("type");
			//String bankId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("bankId");
			//String itemNumber = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");

			String type = "I";
			String bankId = "187";
			String itemNumber = "3212";

			
			if(StringUtils.hasLength(type) && StringUtils.hasLength(bankId) && StringUtils.hasLength(itemNumber)) {
				
				String id = type+ "-" + bankId + "-" + itemNumber;
				
				String queryString = String.format("?type=%s&bankId=%s&id=%s", type, bankId, itemNumber) ;
				
				gitLabUrl = AppSettingsHelper.get(GIT_LAB_ITEM_VERSION_URL);
				irisPage = AppSettingsHelper.get(ITEM_REVIEW_PAGE);
				
				gitLabUrl = gitLabUrl + "/" + id;
				
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
	
				if(versionsType.equalsIgnoreCase("commits")) {
					itemCommits = getItemCommits(br);
					
					for (ItemCommit itemCommit : itemCommits) {
						itemCommit.setLink(request.getContextPath() + irisPage +  queryString + "&version=" + itemCommit.getCommitId());
					}
				}
	
				else if(versionsType.equalsIgnoreCase("tags")) {
					itemVersions = getItemVersions(br);
					
					for (ItemVersion itemVersion : itemVersions) {
						itemVersion.setLink(request.getContextPath() + irisPage +  queryString + "&version=" + itemVersion.getCommitId());
					}
				}
	
				br.close();
				conn.disconnect();
			}	
			

			
		} catch (Exception e) {
			  _logger.error ("Error getting commits form GitLab", e);
		}
		
	}

	
	private List<ItemVersion> getItemVersions(Reader reader) {

		List<ItemVersion> itemVersions = new ArrayList<ItemVersion>();

		try {
			
			JSONParser jsonParser = new JSONParser();
			JSONArray names = (JSONArray) jsonParser.parse(reader);

			Iterator i = names.iterator();
			
			// take each value from the json array separately
			while (i.hasNext()) {
				ItemVersion itemVersion = new ItemVersion();
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

	
	public List<ItemVersion> getItemVersions() {
		init("tags");
		return itemVersions;
	}

	public void setVersions(List<ItemVersion> itemVersions) {
		this.itemVersions = itemVersions;
	}
	
	public void add(ItemVersion itemVersion) {
		itemVersions.add(itemVersion);
	}

	
	public List<ItemCommit> getItemCommits() {
		init("commits");
		return itemCommits;
	}

	public void setItemCommits(List<ItemCommit> itemCommits) {
		this.itemCommits = itemCommits;
	}

	public void setItemVersions(List<ItemVersion> itemVersions) {
		this.itemVersions = itemVersions;
	}
	
	

}
