///**
// *
// */
//package org.smarterbalanced.irv.web.content;
//
//import java.net.MalformedURLException;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.smarterbalanced.irv.config.SettingsReader;
//import org.smarterbalanced.irv.services.GitLabService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Component;
//
//import TDS.Shared.Exceptions.ReturnStatusException;
//import tds.iris.abstractions.repository.ContentException;
//import tds.iris.abstractions.repository.IContentBuilder;
//import tds.itempreview.ConfigBuilder;
//import tds.itemrenderer.data.AccLookup;
//import tds.itemrenderer.data.IITSDocument;
//import tds.itemrenderer.data.ITSContent;
//import tds.itemrenderer.data.ITSMachineRubric;
//import tds.itemscoringengine.RubricContentSource;
//
///**
// * @author kthotti
// *
// */
//@Component
//@Scope("singleton")
//public class ItemReviewViewerBuilder implements IContentBuilder {
//
//	private static final Logger _logger = LoggerFactory.getLogger(ItemReviewViewerBuilder.class);
//
//	private ConfigBuilder _directoryScanner = null;
//
//	@Autowired
//	private GitLabService gitLabService;
//
//	/**
//	 *
//	 */
//	public ItemReviewViewerBuilder() {
//		// TODO Auto-generated constructor stub
//	}
//
//	/* (non-Javadoc)
//	 * @see tds.iris.abstractions.repository.IContentBuilder#getITSDocument(java.lang.String)
//	 */
//	public IITSDocument getITSDocument(String id) throws ContentException {
//		try {
//
//			getItem(id);
//			return _directoryScanner.getRenderableDocument(getBaseItemName(id));
//
//		}  catch (Exception e) {
//			_logger.error("Un known Error while getting or loading  ITEM from GITLAB.", e);
//			throw new ContentException(String.format("UnKnown Exception Occurred while getting item ID: %s", id));
//
//		}
//		//throw new GitLabException(String.format("No content found by id %s", id));
//	}
//
//	/* (non-Javadoc)
//	 * @see tds.iris.abstractions.repository.IContentBuilder#getITSDocumentAcc(java.lang.String, tds.itemrenderer.data.AccLookup)
//	 */
//	@Override
//	public IITSDocument getITSDocumentAcc(String id, AccLookup accLookup) {
//		// TODO Auto-generated method stub
//		try {
//			getItem(id);
//			IITSDocument iitsDocument = _directoryScanner.getRenderableDocument(getBaseItemName(id), accLookup);
//			parseMachineRubric(iitsDocument, "eng", RubricContentSource.ItemXML);
//			return iitsDocument;
//
//		}  catch (Exception e) {
//			_logger.error("Un known Error while getting or loading  ITEM from GITLAB.", e);
//			throw new ContentException(String.format("UnKnown Exception Occurred while getting item ID: %s", id, "Root Error " + e.getMessage()));
//
//		}
//	}
//
//	private String getBaseItemName(String itemName) {
//
//		String[] parts = itemName.split("-");
//
//		String versionNumber = "";
//		if(parts.length > 3)
//			versionNumber = parts[3];
//
//		String baseItemName = parts[0] + "-" + parts[1] + "-" + parts[2];
//
//		return baseItemName;
//
//	}
//
//	private void getItem(String itemNumber) throws ContentException {
//		try {
//
//			_logger.info("Getting the Item with the ID:" + itemNumber);
//
//			String itemName = "";
//			if(itemNumber.startsWith("i") || itemNumber.startsWith("I"))
//				itemName = "item" + itemNumber.substring(1);
//			else
//				itemName = "stim" + itemNumber.substring(1);
//
//			//GitLabService gitLabService = new GitLabService();
//
//			String[] parts = itemName.split("-");
//    		boolean isItemMasterVersion = !(parts.length > 3 && parts[3].length() > 0);
//
//    		if(!gitLabService.isItemExistsLocally(itemName)) {
//				gitLabService.downloadItem(itemName);
//				gitLabService.unzip(itemName);
//			}
//
//    		/*
//    		if(isItemMasterVersion) {
//				gitLabService.downloadItem(itemName);
//				gitLabService.unzip(itemName);
//			}
//			else {
//	    		if(!gitLabService.isItemExistsLocally(itemName)) {
//					gitLabService.downloadItem(itemName);
//					gitLabService.unzip(itemName);
//				}
//			}
//			*/
//
//			String contentPath = SettingsReader.getZipFileLocation() + itemName;
//
//			_logger.info("Loading and scanning the  the Item in Directory Started");
//			_directoryScanner = new ConfigBuilder(contentPath);
//			_directoryScanner.create();
//			_logger.info("Loading and scanning the  the Item in the Directory Complete");
//
//		} catch (MalformedURLException e) {
//			_logger.error("Error Getting Item File.Check the URL:", e);
//			throw new ContentException(e);
//
//		} catch (Exception e) {
//			_logger.error("Un known Error while getting or loading  ITEM from GITLAB.", e);
//			throw new ContentException(String.format("UnKnown Exception Occurred while getting item ID: %s", itemNumber));
//
//		}
//
//	}
//
//	  public ITSMachineRubric parseMachineRubric (IITSDocument itsDocument, String language, RubricContentSource rubricContentSource) throws ReturnStatusException {
//		    ITSMachineRubric machineRubric = null;
//		    // if the source is item bank then parse the answer key attribute
//		    // NOTE: we use to get this from the response table
//		    if (rubricContentSource == RubricContentSource.AnswerKey) {
//		      machineRubric = new ITSMachineRubric (ITSMachineRubric.ITSMachineRubricType.Text, itsDocument.getAnswerKey ()+"|"+itsDocument.getMaxScore());
//		    }
//		    // if the source is item xml then get the machine rubric element
//		    else if (rubricContentSource == RubricContentSource.ItemXML) {
//		      // get top level machine rubric
//		      machineRubric = itsDocument.getMachineRubric ();
//		      // if empty try and get content elements machine rubric
//		      if (machineRubric == null) {
//		        // get its content for the current tests language
//		        ITSContent itsContent = itsDocument.getContent (language);
//		        // make sure this item has a machine rubric
//		        if (itsContent != null) {
//		          machineRubric = itsContent.getMachineRubric ();
//		        }
//		      }
//		    }
//		    return machineRubric;
//		  }
//
//
//	/* (non-Javadoc)
//	 * @see tds.iris.abstractions.repository.IContentBuilder#init()
//	 */
//	@Override
//	public void init() throws ContentException {
//		// TODO Auto-generated method stub
//
//	}
//
//	public GitLabService getGitLabService() {
//		return gitLabService;
//	}
//
//	public void setGitLabService(GitLabService gitLabService) {
//		this.gitLabService = gitLabService;
//	}
//
//}
