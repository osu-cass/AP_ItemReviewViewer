/**
 * 
 */
package org.smarterbalanced.itemreviewviewer.web.services;

import java.io.StringWriter;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarterbalanced.itemreviewviewer.web.config.ItemBankConfig;
import org.smarterbalanced.itemreviewviewer.web.config.SettingsReader;
import org.smarterbalanced.itemreviewviewer.web.models.scoring.ItemScoreInfo;
import org.smarterbalanced.itemreviewviewer.web.services.models.Metadata;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import AIR.Common.Web.EncryptionHelper;
import TDS.Shared.Exceptions.ReturnStatusException;
import tds.itempreview.ConfigBuilder;
import tds.itemrenderer.data.AccLookup;
import tds.itemrenderer.data.IITSDocument;
import tds.itemrenderer.data.ITSContent;
import tds.itemrenderer.data.ITSDocument;
import tds.itemrenderer.data.ITSMachineRubric;
import tds.itemscoringengine.ItemScoreRequest;
import tds.itemscoringengine.ItemScoreResponse;
import tds.itemscoringengine.ResponseInfo;
import tds.itemscoringengine.RubricContentSource;
import tds.itemscoringengine.RubricContentType;
import tds.itemscoringengine.ScoringStatus;

/**
 * @author kthotti & noelcket
 *
 */

@Component
@Scope("singleton")
public class ItemReviewScoringService {

	private static final Logger _logger = LoggerFactory.getLogger(ItemReviewScoringService.class);
	public static final String MS_FORMAT = "MS";
	private GitLabService _gitlabService;

	/**
	 * 
	 */
	public ItemReviewScoringService() {
		_gitlabService = new GitLabService();
	}

	public ItemScoreInfo scoreItem(String studentResponse, String id) throws ItemScoringException {

		try {

			_logger.info("Scoring the item.... " + id);
			return scoreAssessmentItem(studentResponse, getITSDocument(id));

		} catch (Exception e) {
			// TODO: handle exception
			throw new ItemScoringException("There is an error while communicating with Item Scoring Engine " + e);
		}
	}

	public ItemScoreInfo scoreAssessmentItem(String studentResponse, IITSDocument iitsDocument)
			throws ItemScoringException {

		String itemFormat = null;
		try {

			ITSDocument itsDocument = (ITSDocument) iitsDocument;
			itemFormat = itsDocument.getFormat();

			_logger.info("Socring the item.... " + iitsDocument.getID() + " Item Format: " + itemFormat + " started");

			if (itemFormat.trim().equalsIgnoreCase("MC") || itemFormat.trim().equalsIgnoreCase("MS"))
				return scoreMCMSItems(studentResponse, itsDocument);

			ItemScoreResponse itemScoreResponse = scoreItem(studentResponse, itsDocument);
			tds.itemscoringengine.ItemScoreInfo _iItemScoreInfo = itemScoreResponse.getScore().getScoreInfo();
			if(itemFormat.trim().equalsIgnoreCase("SA") || itemFormat.trim().equalsIgnoreCase("WER")){
				_iItemScoreInfo.setPoints(-9);
				_iItemScoreInfo.setMaxScore(-9);
			}


			int maxScore = _getMaxScore(iitsDocument, _iItemScoreInfo);

			ItemScoreInfo itemScoreInfo = new ItemScoreInfo(_iItemScoreInfo.getPoints(), maxScore,
					_iItemScoreInfo.getStatus(), _iItemScoreInfo.getDimension(), _iItemScoreInfo.getRationale());
			_logger.info("creating itemscoreinfo object " + _iItemScoreInfo.getPoints(), maxScore,
					_iItemScoreInfo.getStatus(), _iItemScoreInfo.getDimension(), _iItemScoreInfo.getRationale());
			_logger.info("Socring the item.... " + iitsDocument.getID() + " Item Format: " + itemFormat + " success");

			return itemScoreInfo;
		} catch (Exception e) {
			// TODO: handle exception
			if(itemFormat.trim().equalsIgnoreCase("SA") || itemFormat.trim().equalsIgnoreCase("WER")){
				return new ItemScoreInfo(-9, -9, null, null, null);
			} else {
				throw new ItemScoringException("There is an error while communicating with Item Scoring Engine " + e);
			}
		}

	}

	private ItemScoreInfo scoreMCMSItems(String studentResponse, ITSDocument itsDocument) throws ItemScoringException {
		// TODO Auto-generated method stub
		try {
			_logger.info("Scoring the item of type: " + itsDocument.getFormat() + " by parsing the Item XML");
			StringBuilder answerKey1 = new StringBuilder();
			answerKey1.append(itsDocument.getAttributeValue("itm_att_Answer Key"));
			if (itsDocument.getAttributeValue("itm_att_Answer Key (Part II)") != null
					&& !itsDocument.getAttributeValue("itm_att_Answer Key (Part II)").isEmpty()
					&& !itsDocument.getAttributeValue("itm_att_Answer Key (Part II)").equals("NA")
					&& !itsDocument.getAttributeValue("itm_att_Answer Key (Part II)").equals("N/A")) {
				answerKey1.append(itsDocument.getAttributeValue("itm_att_Answer Key (Part II)"));
			}

			int maxScore = _getMaxScore(itsDocument, null);

			if (studentResponse.equalsIgnoreCase(answerKey1.toString())) {
				if (itsDocument.getFormat().equalsIgnoreCase(MS_FORMAT))
					return new ItemScoreInfo(maxScore, maxScore, ScoringStatus.Scored, null, null);
				else
					return new ItemScoreInfo(maxScore, maxScore, ScoringStatus.Scored, null, null);
			} else {
				return new ItemScoreInfo(-1, 1, ScoringStatus.Scored, null, null);
			}

		} catch (Exception e) {
			// TODO: handle exception
			_logger.error("There is an error while scoring Item " + itsDocument.getID());
			throw new ItemScoringException(
					"There is an error while scoring Item " + itsDocument.getID() + e.getMessage());

		}

	}

	public ItemScoreResponse scoreItem(String studentResponse, ITSDocument itsDocument) throws ItemScoringException {

		try {
			_logger.info("Scoring the item of type: " + itsDocument.getFormat() + " by parsing the Item XML");

			String id = itsDocument.getID();

			String _itemName = "";
			if (id.startsWith("i") || id.startsWith("I"))
				_itemName = "Item" + id.substring(1);
			else
				_itemName = "Stim" + id.substring(1);

			_logger.info("Checking whether item is scoreable?");
			if (!isScorable(itsDocument)) {
				_logger.error("Item is not scoreable. No rubric file found");
				throw new ItemScoringException(
						"No rubric file for Item " + id + ". ScoringEngine can't be used for this item type.");
			}

			// create responseInfo
			ITSMachineRubric machineRubric = parseMachineRubric(itsDocument, "ENU", RubricContentSource.ItemXML);
			URI rubricUri = machineRubric.createUri();
			_logger.info("Item: " + id + " is scorable. Rubric file path: " + rubricUri.toString());
			machineRubric.setData(new String(Files.readAllBytes(Paths.get(rubricUri))));

			ResponseInfo responseInfo = new ResponseInfo(itsDocument.getFormat(), _itemName, studentResponse,
					machineRubric.getData(), RubricContentType.ContentString, "", true);

			ItemScoreRequest itemScoreRequest = new ItemScoreRequest(responseInfo);

			StringWriter writer = new StringWriter();
			XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
			XMLStreamWriter out = outputFactory.createXMLStreamWriter(writer);

			itemScoreRequest.writeXML(out);

			_logger.info("ItemScoreRequest " + writer.toString());

			Client client = Client.create();

			String scoringUrl = ItemBankConfig.get("scoringServiceUrl");

			_logger.info("Communicating with Scoring Engine....." + scoringUrl);
			WebResource webResource = client.resource(scoringUrl);

			_logger.info("POSTing the Item Score Request.....");
			ClientResponse response = webResource.accept("application/xml").post(ClientResponse.class,
					writer.toString());

			if (response.getStatus() != 200) {
				_logger.error("Error communicating with ItemScoringEngine; Failed : HTTP error code : "
						+ response.getStatus());
				throw new ItemScoringException("Error communicating with ItemScoringEngine; Failed : HTTP error code : "
						+ response.getStatus());
			}

			String output = response.getEntity(String.class);
			_logger.info("Response from Scoring Engine: ");
			_logger.info(output);
			ItemScoreResponse itemScoreResponse = ItemScoreResponse.getInstanceFromXml(output);

			return itemScoreResponse;

		} catch (Exception e) {
			// TODO: handle exception
			throw new ItemScoringException("There is an error while communicating with Item Scoring Engine " + e);
		}
	}

	public boolean isScorable(IITSDocument iitsDocument) throws ItemScoringException {
		return iitsDocument.getMachineRubric() != null && iitsDocument.getMachineRubric().getIsValid();
	}

	public boolean isScorable(String id) throws ItemScoringException {
		IITSDocument iitsDocument = getITSDocument(id);
		return iitsDocument.getMachineRubric() != null && iitsDocument.getMachineRubric().getIsValid();
	}

	private ITSDocument getITSDocument(String id) throws ItemScoringException {

		try {
			_logger.info("Scoring the item.... " + id);

			String _itemName = "";
			if (id.startsWith("i") || id.startsWith("I"))
				_itemName = "Item" + id.substring(1);
			else
				_itemName = "Stim" + id.substring(1);

			String contentPath = SettingsReader.getZipFileLocation() + _itemName;

			// get IITSDocument
			ConfigBuilder _directoryScanner = new ConfigBuilder(contentPath);
			_directoryScanner.create();
			IITSDocument itsDocument = _directoryScanner.getRenderableDocument(getItemId(id), new AccLookup());

			return (ITSDocument) itsDocument;

		} catch (Exception e) {
			// TODO: handle exception
			throw new ItemScoringException("There is an error while getting IITDocument " + e);
		}

	}

	private String getItemId(String id) {

		String[] parts = id.split("-");

		String itemId = parts[0] + "-" + parts[1] + "-" + parts[2];

		return itemId;

	}

	public static String getItemScoringUrl() {
		return SettingsReader.get("tds.itemPreview.itemScoringUrl");
	}

	public ITSMachineRubric parseMachineRubric(IITSDocument itsDocument, String language,
			RubricContentSource rubricContentSource) throws ReturnStatusException {
		ITSMachineRubric machineRubric = null;
		// if the source is item bank then parse the answer key attribute
		// NOTE: we use to get this from the response table
		if (rubricContentSource == RubricContentSource.AnswerKey) {
			machineRubric = new ITSMachineRubric(ITSMachineRubric.ITSMachineRubricType.Text,
					itsDocument.getAnswerKey() + "|" + itsDocument.getMaxScore());
		}
		// if the source is item xml then get the machine rubric element
		else if (rubricContentSource == RubricContentSource.ItemXML) {
			// get top level machine rubric
			machineRubric = itsDocument.getMachineRubric();
			// if empty try and get content elements machine rubric
			if (machineRubric == null) {
				// get its content for the current tests language
				ITSContent itsContent = itsDocument.getContent(language);
				// make sure this item has a machine rubric
				if (itsContent != null) {
					machineRubric = itsContent.getMachineRubric();
				}
			}
		}
		return machineRubric;
	}

	//checks if the getMaxScore info returns a not found value. If it does it gets the max score from the metadata.
	private int _getMaxScore(IITSDocument iitsDocument, tds.itemscoringengine.ItemScoreInfo iItemScoreInfo){
		int maxScore = -1;
		if(iItemScoreInfo != null){
			maxScore = iItemScoreInfo.getMaxScore();
		}
		if(maxScore == -1){
			String bankKey = Long.toString(iitsDocument.getBankKey());
			String itemKey = Long.toString(iitsDocument.getItemKey());
			String itemDirName = GitLabUtils.makeDirId(bankKey, itemKey);

			Metadata metadata = _gitlabService.getMetadata(itemDirName);

			String maximumNumberOfPoints = metadata.getSmarterAppMetadata().getMaximumNumberOfPoints();
			if(maximumNumberOfPoints!= null && Integer.parseInt(maximumNumberOfPoints) > 0){
				maxScore = Integer.parseInt(maximumNumberOfPoints);
			}
		}

		return maxScore;
	}

}
