/**
 * 
 */
package org.smarterbalanced.itemviewerservice.core.Models;

import java.util.Date;

/**
 * @author kthotti
 *
 */
public class ItemCommit {
	
	private int id;
	private String commitId;
	private String shortId;
	private String title;
	private String authorName;
	private String authorEmail;
	private Date creationDate;
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


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getCommitId() {
		return commitId;
	}


	public void setCommitId(String commitId) {
		this.commitId = commitId;
	}


	public void setLink(String string) {
		// TODO Auto-generated method stub
		this.link = string;
		
	}


	public String getLink() {
		return link;
	}

}
