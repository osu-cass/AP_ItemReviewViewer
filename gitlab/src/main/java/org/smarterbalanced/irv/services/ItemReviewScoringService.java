/**
 * 
 */
package org.smarterbalanced.irv.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URL;
import java.util.UUID;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarterbalanced.irv.config.SettingsReader;
import org.smarterbalanced.irv.model.ItemScoreInfo;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import AIR.Common.Web.EncryptionHelper;
import AIR.Common.Web.WebValueCollectionCorrect;
import TDS.Shared.Exceptions.ReturnStatusException;
import tds.itempreview.ConfigBuilder;
import tds.itemrenderer.data.AccLookup;
import tds.itemrenderer.data.IITSDocument;
import tds.itemrenderer.data.ITSContent;
import tds.itemrenderer.data.ITSMachineRubric;
import tds.itemrenderer.data.ITSMachineRubric.ITSMachineRubricType;
import tds.itemrenderer.processing.ITSDocumentHelper;
import tds.itemscoringengine.ItemScore;
import tds.itemscoringengine.ItemScoreRequest;
import tds.itemscoringengine.ItemScoreResponse;
import tds.itemscoringengine.ResponseInfo;
import tds.itemscoringengine.RubricContentSource;
import tds.itemscoringengine.RubricContentType;
import tds.itemscoringengine.ScoreRationale;
import tds.itemscoringengine.ScorerInfo;
import tds.itemscoringengine.ScoringStatus;
import tds.itemscoringengine.WebProxyItemScorerCallback;
import tds.student.sql.data.IItemResponseScorable;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

/**
 * @author kthotti
 *
 */

@Component
@Scope("singleton")
public class ItemReviewScoringService {
	
	private static final Logger _logger = LoggerFactory.getLogger(ItemReviewScoringService.class);

	/**
	 * 
	 */
	public ItemReviewScoringService() {
		// TODO Auto-generated constructor stub
	}

	public ItemScoreResponse scoreItem(String studentResponse, String id) throws ItemScoringException {

		try {

			_logger.info("Socring the item.... " + id);
			return scoreItem(studentResponse, getIITSDocument(id));

		} catch (Exception e) {
			// TODO: handle exception
			throw new ItemScoringException("There is an error while communicating with Item Scoring Engine "+ e);
		}
	}

	public ItemScoreInfo scoreAssessmentItem(String studentResponse, String id) throws ItemScoringException {

		try {

			ItemScoreResponse itemScoreResponse = scoreItem(studentResponse, getIITSDocument(id));
			tds.itemscoringengine.ItemScoreInfo _iItemScoreInfo = itemScoreResponse.getScore().getScoreInfo();
			
			ItemScoreInfo itemScoreInfo = new ItemScoreInfo(_iItemScoreInfo.getPoints(), _iItemScoreInfo.getMaxScore(), _iItemScoreInfo.getStatus(), _iItemScoreInfo.getDimension(), _iItemScoreInfo.getRationale());
			return itemScoreInfo;

		} catch (Exception e) {
			// TODO: handle exception
			throw new ItemScoringException("There is an error while communicating with Item Scoring Engine "+ e);
		}
		
	}


	public ItemScoreInfo scoreAssessmentItem(String studentResponse, IITSDocument iitsDocument) throws ItemScoringException {

		try {

			_logger.info("Socring the item.... " + iitsDocument.getID() + " Item Type: " + iitsDocument.getFormat());
			
			ItemScoreResponse itemScoreResponse = scoreItem(studentResponse, iitsDocument);
			tds.itemscoringengine.ItemScoreInfo _iItemScoreInfo = itemScoreResponse.getScore().getScoreInfo();
			
			ItemScoreInfo itemScoreInfo = new ItemScoreInfo(_iItemScoreInfo.getPoints(), _iItemScoreInfo.getMaxScore(), _iItemScoreInfo.getStatus(), _iItemScoreInfo.getDimension(), _iItemScoreInfo.getRationale());
			return itemScoreInfo;

		} catch (Exception e) {
			// TODO: handle exception
			throw new ItemScoringException("There is an error while communicating with Item Scoring Engine "+ e);
		}
		
	}

	
	public ItemScoreResponse scoreItem(String studentResponse, IITSDocument iitsDocument) throws ItemScoringException {

		try {
			
			String id = iitsDocument.getID();
			
			String _itemName = "";
			if(id.startsWith("i") || id.startsWith("I"))
				_itemName = "Item" + id.substring(1);
			else
				_itemName = "Stim" + id.substring(1);

			_logger.info("Checking whether item is scorable?");
			if(!isScorable(iitsDocument)) {
				_logger.error("Item is not scorable. No rubric file found");
				throw new ItemScoringException("No rubric file for Item " + id + ". ScoringEngine can't be used for this item type.");
			}
			
			URI rubricUri = iitsDocument.getMachineRubric().createUri();
			_logger.info("Item: " + id + " is scorable. Rubric file path: " + rubricUri.toString());
			
			// create responseInfo
			
			
	        ITSMachineRubric machineRubric = iitsDocument.getMachineRubric();
	        machineRubric.setData(new String(readAllBytes(get(rubricUri))));
	
            String encryptedStudentResponse = EncryptionHelper.EncryptToBase64(studentResponse);
            String encryptedRubricContent = EncryptionHelper.EncryptToBase64(machineRubric.getData());
            
			ResponseInfo responseInfo = new ResponseInfo(iitsDocument.getFormat(), _itemName, encryptedStudentResponse, encryptedRubricContent, RubricContentType.ContentString, "", true);
			responseInfo.setRubricEncrypted(true);
			responseInfo.setStudentResponseEncrypted(true);
			ItemScoreRequest itemScoreRequest = new ItemScoreRequest(responseInfo);

			StringWriter writer = new StringWriter();
			XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
			XMLStreamWriter out = outputFactory.createXMLStreamWriter(writer);

			itemScoreRequest.writeXML(out);
			
			_logger.info("ItemScoreRequest " + writer.toString());

			Client client = Client.create();
			
			String scoringEngineUrl = getItemScoringUrl();
			_logger.info("Communicating with Scoring Engine....." + scoringEngineUrl);
			WebResource webResource = client.resource(scoringEngineUrl);
			
			_logger.info("POSTing the Item Score Request.....") ;
			ClientResponse response = webResource.accept("application/xml").post(ClientResponse.class, writer.toString());

			if (response.getStatus() != 200) {
				_logger.error("Error communicating with ItemScoringEngine; Failed : HTTP error code : " + response.getStatus());
				throw new ItemScoringException("Error communicating with ItemScoringEngine; Failed : HTTP error code : " + response.getStatus());
			}

			String output = response.getEntity(String.class);
			_logger.info("Response from Scoring Engine: ");
			_logger.info(output);
			ItemScoreResponse itemScoreResponse = ItemScoreResponse.getInstanceFromXml (output);
			
			return itemScoreResponse;
	
		} catch (Exception e) {
			// TODO: handle exception
			throw new ItemScoringException("There is an error while communicating with Item Scoring Engine "+ e);
		}
	}
	
	public boolean isScorable(IITSDocument iitsDocument) throws ItemScoringException {
		return iitsDocument.getMachineRubric() != null && iitsDocument.getMachineRubric().getIsValid();
	}

	public boolean isScorable(String id) throws ItemScoringException {
		IITSDocument iitsDocument = getIITSDocument(id);
		return iitsDocument.getMachineRubric() != null && iitsDocument.getMachineRubric().getIsValid();
	}

	
	private IITSDocument getIITSDocument(String id) throws ItemScoringException {
		
		try {
			_logger.info("Socring the item.... " + id);
			
			String _itemName = "";
			if(id.startsWith("i") || id.startsWith("I"))
				_itemName = "Item" + id.substring(1);
			else
				_itemName = "Stim" + id.substring(1);


			String contentPath = SettingsReader.getZipFileLocation() + _itemName;

			// get IITSDocument
			ConfigBuilder _directoryScanner = new ConfigBuilder(contentPath);
			_directoryScanner.create();
			IITSDocument iitsDocument = _directoryScanner.getRenderableDocument(getItemId(id), new AccLookup());
			
			return iitsDocument;

		} catch (Exception e) {
			// TODO: handle exception
			throw new ItemScoringException("There is an error while getting IITDocument " + e);
		}
		
	} 
	
	private String getItemId(String id) {
			
			String[] parts = id.split("-");
			
			String versionNumber = ""; 
			if(parts.length > 3)
				versionNumber = parts[3];
			
			String itemId = parts[0] + "-" + parts[1] + "-" + parts[2]; 
			
			return itemId;
			
	}
	
    public static String getItemScoringUrl() {
    	return SettingsReader.get("tds.itemPreview.itemScoringUrl");
    }
    
    
    public ITSMachineRubric parseMachineRubric (IITSDocument itsDocument, String language, RubricContentSource rubricContentSource) throws ReturnStatusException {
        ITSMachineRubric machineRubric = null;
        // if the source is item bank then parse the answer key attribute
        // NOTE: we use to get this from the response table
        if (rubricContentSource == RubricContentSource.AnswerKey) {
          machineRubric = new ITSMachineRubric (ITSMachineRubric.ITSMachineRubricType.Text, itsDocument.getAnswerKey ()+"|"+itsDocument.getMaxScore());
        }
        // if the source is item xml then get the machine rubric element
        else if (rubricContentSource == RubricContentSource.ItemXML) {
          // get top level machine rubric
          machineRubric = itsDocument.getMachineRubric ();
          // if empty try and get content elements machine rubric
          if (machineRubric == null) {
            // get its content for the current tests language
            ITSContent itsContent = itsDocument.getContent (language);
            // make sure this item has a machine rubric
            if (itsContent != null) {
              machineRubric = itsContent.getMachineRubric ();
            }
          }
        }
        return machineRubric;
      }
    
    
    /*
    public ItemScore scoreItem (UUID oppKey, IItemResponseScorable responseScorable, IITSDocument itsDoc) throws ReturnStatusException {
        final String itemID = getItemID (responseScorable);
        String itemFormat = itsDoc.getFormat ();

        // get the scorer information for this item type
        ScorerInfo scorerInfo = _itemScorer.GetScorerInfo (itemFormat);

        ITSMachineRubric machineRubric = new ITSMachineRubric (ITSMachineRubricType.Text, null);
        RubricContentType rubricContentType = RubricContentType.ContentString;

        if (scorerInfo.getRubricContentSource () != RubricContentSource.None) {
          // get items rubric
          machineRubric = _contentService.parseMachineRubric (itsDoc, responseScorable.getLanguage (), scorerInfo.getRubricContentSource ());
          if (machineRubric == null)
            return null;

          // create response info to pass to item scorer
          if (machineRubric.getType ().equals (ITSMachineRubricType.Uri))
            rubricContentType = RubricContentType.Uri;
          else
            rubricContentType = RubricContentType.ContentString;

          // if this is true then load the rubric manually for the scoring engine
          if (rubricContentType == RubricContentType.Uri && _itemScoringSettings.getAlwaysLoadRubric ()) {
            rubricContentType = RubricContentType.ContentString;

            // read text from stream
            try {
              InputStream stream = ITSDocumentHelper.getStream (machineRubric.getData ());
              BufferedReader streamReader = new BufferedReader (new InputStreamReader (stream));
              try {
                StringBuilder sb = new StringBuilder ();
                String line;

                line = streamReader.readLine ();

                while (line != null) {
                  sb.append (line);
                  sb.append (System.lineSeparator ());
                  line = streamReader.readLine ();
                }
                machineRubric.setData (sb.toString ());
              } finally {
                try {
                  streamReader.close ();
                } catch (Exception e) {
                }
              }
            } catch (IOException e) {
              _logger.error (e.getMessage (), e);
              throw new ReturnStatusException ("Failed to read rubric.");
            }
          }
        }

        // create rubric object
        Object rubricContent;
        if (rubricContentType == RubricContentType.Uri) {
          rubricContent = machineRubric.createUri ();
        } else {
          rubricContent = machineRubric.getData (); // xml
        }

        ResponseInfo responseInfo = new ResponseInfo (itemFormat, itemID, responseScorable.getValue (), rubricContent, rubricContentType, null, true);

        // perform sync item scoring
        if (!isScoringAsynchronous (itsDoc)) {
          try {
            return _itemScorer.ScoreItem (responseInfo, null);
          } catch (final Exception ex) {
            return new ItemScore (-1, -1, ScoringStatus.ScoringError, null, new ScoreRationale ()
            {
              {
                setMsg ("Exception scoring item " + itemID + ": " + ex);
              }
            }, null, null);
          }
        }

        // create callback token if there is a callback url
        // TODO: Add warning at app startup if ItemScoringCallbackUrl is missing
        if (!StringUtils.isEmpty (_itemScoringSettings.getCallbackUrl ())) {
          // Create the token
          WebValueCollectionCorrect tokenData = new WebValueCollectionCorrect ();
          tokenData.put ("oppKey", oppKey);
          tokenData.put ("testKey", responseScorable.getTestKey ());
          tokenData.put ("testID", responseScorable.getTestID ());
          tokenData.put ("language", responseScorable.getLanguage ());
          tokenData.put ("position", responseScorable.getPosition ());
          tokenData.put ("itsBank", responseScorable.getBankKey ());
          tokenData.put ("itsItem", responseScorable.getItemKey ());
          tokenData.put ("segmentID", responseScorable.getSegmentID ());
          tokenData.put ("sequence", responseScorable.getSequence ());
          tokenData.put ("scoremark", responseScorable.getScoreMark ());

          // encrypt token (do not url encode)
          String encryptedToken = EncryptionHelper.EncryptToBase64 (tokenData.toString (false));

          // save token
          responseInfo.setContextToken (encryptedToken);
        }

        ItemScore scoreItem = null;

        // perform scoring
        String message = null;
        try {
          WebProxyItemScorerCallback webProxyCallback = null;

          // check if there is a URL which will make this call asynchronous
          URL serverUri = getServerUri (itemFormat, responseScorable.getTestID ());
          URL callbackUri = getCallbackUri ();
          
          if (serverUri != null && callbackUri != null) {
            webProxyCallback = new WebProxyItemScorerCallback (serverUri.toString (), callbackUri.toString ());
          }

          // call web proxy scorer
          scoreItem = _itemScorer.ScoreItem (responseInfo, webProxyCallback);

          // validate results
          if (scoreItem == null) {
            log (responseScorable, "Web proxy returned NULL score.", "scoreItem", null);
          } else if (scoreItem.getScoreInfo ().getStatus () == ScoringStatus.ScoringError) {
            message = String.format ("Web proxy returned a scoring error status: '%s'.", (scoreItem.getScoreInfo ().getRationale () != null ? scoreItem.getScoreInfo ().getRationale () : ""));
            log (responseScorable, message, "scoreItem", null);
          } else if (webProxyCallback != null && scoreItem.getScoreInfo ().getStatus () != ScoringStatus.WaitingForMachineScore) {
            message = String.format ("Web proxy is in asynchronous mode and returned a score status of %s. It should return %s.", scoreItem.getScoreInfo ().getStatus ().toString (),
                ScoringStatus.WaitingForMachineScore.toString ());
            log (responseScorable, message, "scoreItem", null);
          } else if (webProxyCallback == null && scoreItem.getScoreInfo ().getStatus () == ScoringStatus.WaitingForMachineScore) {
            message = String.format ("Web proxy is in synchronous mode but returned incorrect status of %s.", scoreItem.getScoreInfo ().getStatus ().toString ());
            log (responseScorable, message, "scoreItem", null);
          }
        } catch (Exception ex) {
          message = String.format ("EXCEPTION = '%s'.", ex.getMessage ());
          log (responseScorable, message, "scoreItem", ex);
        }

        return scoreItem;
      }
      */

      /**
       * Is the scoring for this response asynchronous?
       */
    /*
      private boolean isScoringAsynchronous (IITSDocument itsDoc) {
        if (itsDoc != null && !StringUtils.isEmpty (itsDoc.getFormat ())) {
          ScorerInfo scorerInfo = _itemScorer.GetScorerInfo (itsDoc.getFormat ());

          // check if scorer exists - not all item types are scored in TDS
          if (scorerInfo != null) {
            return scorerInfo.isSupportsAsyncMode ();
          }
        }

        return false;
      }
      */



	
}
