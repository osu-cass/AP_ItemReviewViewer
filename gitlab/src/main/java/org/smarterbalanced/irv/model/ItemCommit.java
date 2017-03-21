/**
 * 
 */
package org.smarterbalanced.irv.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author kthotti
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemCommit {
	

	private int index;
	
	@JsonProperty("id")
	private String id;
	

	@JsonProperty("short_id")
	private String shortId;
	
	@JsonProperty("title")
	private String title;
	
	@JsonProperty("author_name")
	private String authorName;
	
	@JsonProperty("author_email")
	private String authorEmail;
	
	@JsonProperty("committer_name")
	private String committerName;
	
	@JsonProperty("created_at")
	private Date creationDate;
	
	@JsonProperty("message")
	private String message;
	
	private String link;

	/**
	 * 
	 */
	public ItemCommit() {
		// TODO Auto-generated constructor stub
	}


	public String getShortId() {
		return shortId;
	}

	public void setShortId(String shortId) {
		this.shortId = shortId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getAuthorEmail() {
		return authorEmail;
	}

	public void setAuthorEmail(String authorEmail) {
		this.authorEmail = authorEmail;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	public void setLink(String string) {
		// TODO Auto-generated method stub
		this.link = string;
		
	}


	public String getLink() {
		return link;
	}


	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getCommitterName() {
		return committerName;
	}


	public void setCommitterName(String committerName) {
		this.committerName = committerName;
	}

}
