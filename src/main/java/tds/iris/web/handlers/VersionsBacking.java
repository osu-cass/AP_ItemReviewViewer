/**
 * 
 */
package tds.iris.web.handlers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import tds.itemselection.base.ItemCandidatesData;

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

	//http://localhost:8090/webapi/api/item/commits/item-187-1167
	private String gitLabUrl;

	//http://localhost:8090/iris/IrisPages/sample.xhtml
	private String irisPage;
	
	
	/**
	 * 
	 */
	public VersionsBacking() {
		// TODO Auto-generated constructor stub

	}
	
	private void versionsInit() {
		try {
			String type = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("type");
			String bankId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("bankId");
			String itemNumber = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");

			String id = type+ "-" + bankId + "-" + itemNumber;
			
			String queryString = String.format("?type=%s&bankId=%s&id=%s", type, bankId, itemNumber) ;
			
			gitLabUrl = gitLabUrl + "/" + id;
			
			URL url = new URL(gitLabUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed to connect to GitLab: HTTP error code :" + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			itemVersions = getItemVersions(br);
			
			for (ItemVersion itemVersion : itemVersions) {
				itemVersion.setLink(irisPage +  queryString + "&version=" + itemVersion.getCommitId());
			}

			br.close();
			conn.disconnect();
			

			
		} catch (Exception e) {
			// TODO: handle exception
			  _logger.error ("Error getting tags form GitLab", e);
		}
		
	}
	private void commitsInit() {
		try {
			String type = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("type");
			String bankId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("bankId");
			String itemNumber = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");

			String id = type+ "-" + bankId + "-" + itemNumber;
			
			String queryString = String.format("?type=%s&bankId=%s&id=%s", type, bankId, itemNumber) ;
			
			gitLabUrl = gitLabUrl + "/" + id;
			
			URL url = new URL(gitLabUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed to connect to GitLab: HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			itemCommits = getItemCommits(br);
			
			for (ItemCommit itemCommit : itemCommits) {
				itemCommit.setLink(irisPage +  queryString + "&version=" + itemCommit.getCommitId());
			}

			br.close();
			conn.disconnect();
			

			
		} catch (Exception e) {
			// TODO: handle exception
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
		versionsInit();
		return itemVersions;
	}

	public void setVersions(List<ItemVersion> itemVersions) {
		this.itemVersions = itemVersions;
	}
	
	public void add(ItemVersion itemVersion) {
		itemVersions.add(itemVersion);
	}

	public String getGitLabUrl() {
		return gitLabUrl;
	}

	public void setGitLabUrl(String gitLabUrl) {
		this.gitLabUrl = gitLabUrl;
	}

	public String getIrisPage() {
		return irisPage;
	}

	public void setIrisPage(String irisPage) {
		this.irisPage = irisPage;
	}

	public List<ItemCommit> getItemCommits() {
		commitsInit();;
		return itemCommits;
	}

	public void setItemCommits(List<ItemCommit> itemCommits) {
		this.itemCommits = itemCommits;
	}

	public void setItemVersions(List<ItemVersion> itemVersions) {
		this.itemVersions = itemVersions;
	}
	
	

}
