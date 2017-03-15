/**
 * 
 */
package org.smarterbalanced.itemviewerservice.core.Models;

/**
 * @author kthotti
 *
 */
public class ItemVersion {
	
	private String name;
	private String tagName;
	private String link;
	private String commitId;

	/**
	 * 
	 */
	public ItemVersion() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getCommitId() {
		return commitId;
	}

	public void setCommitId(String commitId) {
		this.commitId = commitId;
	}

}
