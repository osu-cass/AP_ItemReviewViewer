/**
 * 
 */
package org.smarterbalanced.itemreviewviewer.content;

import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarterbalanced.itemreviewviewer.model.MetaData;
import org.smarterbalanced.itemreviewviewer.services.GitLabService;
import org.smarterbalanced.itemviewerservice.dal.Config.SettingsReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import tds.iris.abstractions.repository.ContentException;
import tds.iris.abstractions.repository.IContentBuilder;
import tds.itempreview.ConfigBuilder;
import tds.itemrenderer.data.AccLookup;
import tds.itemrenderer.data.IITSDocument;

/**
 * @author kthotti
 *
 */
@Component
@Scope("singleton")
public class ItemReviewViewerBuilder implements IContentBuilder {
	
	private static final Logger _logger = LoggerFactory.getLogger(ItemReviewViewerBuilder.class);

	private ConfigBuilder _directoryScanner = null;

	@Autowired
	private GitLabService gitLabService;

	/**
	 * 
	 */
	public ItemReviewViewerBuilder() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see tds.iris.abstractions.repository.IContentBuilder#getITSDocument(java.lang.String)
	 */
	public IITSDocument getITSDocument(String id) throws ContentException {
		try {

			getItem(id);
			return _directoryScanner.getRenderableDocument(getBaseItemName(id));

		}  catch (Exception e) {
			_logger.error("Un known Error while getting or loading  ITEM from GITLAB.", e);
			throw new ContentException(String.format("UnKnown Exception Occurred while getting item ID: %s", id));

		}
		//throw new ContentException(String.format("No content found by id %s", id));
	}

	/* (non-Javadoc)
	 * @see tds.iris.abstractions.repository.IContentBuilder#getITSDocumentAcc(java.lang.String, tds.itemrenderer.data.AccLookup)
	 */
	@Override
	public IITSDocument getITSDocumentAcc(String id, AccLookup accLookup) {
		// TODO Auto-generated method stub
		try {
			getItem(id);
			return _directoryScanner.getRenderableDocument(getBaseItemName(id), accLookup);

		}  catch (Exception e) {
			_logger.error("Un known Error while getting or loading  ITEM from GITLAB.", e);
			throw new ContentException(String.format("UnKnown Exception Occurred while getting item ID: %s", id, "Root Error " + e.getMessage()));

		}
	}
	
	private String getBaseItemName(String itemName) {
		
		String[] parts = itemName.split("-");
		
		String versionNumber = ""; 
		if(parts.length > 3)
			versionNumber = parts[3];
		
		String baseItemName = parts[0] + "-" + parts[1] + "-" + parts[2]; 
		
		return baseItemName;
		
	}

	private void getItem(String itemNumber) throws ContentException {
		try {
			
			_logger.info("Getting the Item with the ID:" + itemNumber);
			
			String itemName = "";
			if(itemNumber.startsWith("i") || itemNumber.startsWith("I"))
				itemName = "item" + itemNumber.substring(1);
			else
				itemName = "stim" + itemNumber.substring(1);
			
			//GitLabService gitLabService = new GitLabService();
			
			String[] parts = itemName.split("-");
    		boolean isItemMasterVersion = !(parts.length > 3 && parts[3].length() > 0);
			
			if(isItemMasterVersion) {
				gitLabService.downloadItem(itemName);
				gitLabService.unzip(itemName);
			}
			else {
	    		if(!gitLabService.isItemExistsLocally(itemName)) {
					gitLabService.downloadItem(itemName);
				}
				gitLabService.unzip(itemName);
			}
    		
			
			MetaData metaData = gitLabService.getMetaData(itemName);
			
			String contentPath = SettingsReader.getZipFileLocation() + itemName;
			
			_logger.info("Loading and scanning the  the Item in Directory Started");
			_directoryScanner = new ConfigBuilder(contentPath);
			_directoryScanner.create();
			_logger.info("Loading and scanning the  the Item in the Directory Complete");

		} catch (MalformedURLException e) {
			_logger.error("Error Getting Item File.Check the URL:", e);
			throw new ContentException(e);

		} catch (Exception e) {
			_logger.error("Un known Error while getting or loading  ITEM from GITLAB.", e);
			throw new ContentException(String.format("UnKnown Exception Occurred while getting item ID: %s", itemNumber));

		}
		
	}

	/* (non-Javadoc)
	 * @see tds.iris.abstractions.repository.IContentBuilder#init()
	 */
	@Override
	public void init() throws ContentException {
		// TODO Auto-generated method stub

	}

	public GitLabService getGitLabService() {
		return gitLabService;
	}

	public void setGitLabService(GitLabService gitLabService) {
		this.gitLabService = gitLabService;
	}

}
