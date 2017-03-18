/**
 * 
 */
package org.smarterbalanced.itemreviewviewer.managed.beans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarterbalanced.itemreviewviewer.model.ItemCommit;
import org.smarterbalanced.itemreviewviewer.model.ItemTag;
import org.smarterbalanced.itemreviewviewer.services.GitLabService;
import org.smarterbalanced.itemreviewviewer.services.IGitLabService;
import org.smarterbalanced.itemviewerservice.dal.Config.SettingsReader;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;

/**
 * @author kthotti
 *
 */
@ManagedBean(name="versionsBacking")
@Scope(value="request")
public class VersionsBacking {
	
	private static final Logger _logger = LoggerFactory.getLogger (VersionsBacking.class);

	private List<ItemTag> itemTags;
	private List<ItemCommit> itemCommits;

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
			String type = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("type");
			String bankId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("bankId");
			String itemNumber = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");

			
			String queryString = String.format("?type=%s&bankId=%s&id=%s", type, bankId, itemNumber) ;
			
			if(StringUtils.hasLength(type) && StringUtils.hasLength(bankId) && StringUtils.hasLength(itemNumber)) {
				
				irisPage = SettingsReader.get(ITEM_REVIEW_PAGE);

				String id = type+ "-" + bankId + "-" + itemNumber;
				IGitLabService gitLabService = new GitLabService();
				
				if(versionsType.equalsIgnoreCase("commits")) {
					itemCommits = gitLabService.getItemCommits(type, bankId, itemNumber);
					
					for (ItemCommit itemCommit : itemCommits) {
						itemCommit.setLink(request.getContextPath() + irisPage +  queryString + "&version=" + itemCommit.getCommitId());
					}
				}
				/*
				else if(versionsType.equalsIgnoreCase("tags")) {
					itemVersions = getItemVersions(br);
					
					for (ItemTag itemVersion : itemVersions) {
						itemVersion.setLink(request.getContextPath() + irisPage +  queryString + "&version=" + itemVersion.getCommitId());
					}
				}
				*/

			}

	
			
		} catch (Exception e) {
			  _logger.error ("Error getting commits form GitLab", e);
		}
		
	}
	
	public List<ItemTag> getItemTags() {
		init("tags");
		return itemTags;
	}

	public List<ItemCommit> getItemCommits() {
		init("commits");
		return itemCommits;
	}


}
