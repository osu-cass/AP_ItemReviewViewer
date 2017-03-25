/**
 * 
 */
package org.smarterbalanced.irv.services;

import java.io.StringWriter;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarterbalanced.irv.config.SettingsReader;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import tds.itempreview.ConfigBuilder;
import tds.itemrenderer.data.AccLookup;
import tds.itemrenderer.data.IITSDocument;
import tds.itemscoringengine.ItemScoreRequest;
import tds.itemscoringengine.ItemScoreResponse;
import tds.itemscoringengine.ResponseInfo;
import tds.itemscoringengine.RubricContentType;

/**
 * @author kthotti
 *
ß */
public class ItemScoringService {
	
	private static final Logger _logger = LoggerFactory.getLogger(ItemScoringService.class);

	/**
	 * 
	 */
	public ItemScoringService() {
		// TODO Auto-generated constructor stub
	}

	public ItemScoreResponse scoreItem(String studentResponse, String id, AccLookup accLookup) throws ItemScoringException {

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

			return scoreItem(studentResponse, iitsDocument, accLookup);

		} catch (Exception e) {
			// TODO: handle exception
			throw new ItemScoringException("There is an error while communicating with Item Scoring Engine "+ e);
		}
	}

	public ItemScoreResponse scoreItem(String studentResponse, IITSDocument iitsDocument, AccLookup accLookup) throws ItemScoringException {

		try {
			
			String id = iitsDocument.getID();
			_logger.info("Socring the item.... " + iitsDocument.getID());
			
			String _itemName = "";
			if(id.startsWith("i") || id.startsWith("I"))
				_itemName = "Item" + id.substring(1);
			else
				_itemName = "Stim" + id.substring(1);

			
			if(iitsDocument.getMachineRubric() == null) {
				throw new ItemScoringException("No rubric file for Item " + id + ". ScoringEngine can't be used for this item type.");
			}
			
			if(iitsDocument.getMachineRubric() != null) {
				// create responseInfo
				ResponseInfo responseInfo = new ResponseInfo(iitsDocument.getFormat(), _itemName, studentResponse, iitsDocument.getMachineRubric(), RubricContentType.Uri, "", false);
				ItemScoreRequest itemScoreRequest = new ItemScoreRequest(responseInfo);

				StringWriter writer = new StringWriter();
				XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
				XMLStreamWriter out = outputFactory.createXMLStreamWriter(writer);

				itemScoreRequest.writeXML(out);

				Client client = Client.create();

				WebResource webResource = client.resource(getItemScoringUrl());
				ClientResponse response = webResource.accept("application/xml").post(ClientResponse.class, writer.toString());

				if (response.getStatus() != 200) {
					throw new ItemScoringException("Error communicating with ItemScoringEngine; Failed : HTTP error code : " + response.getStatus());
				}

				String output = response.getEntity(String.class);
				
				ItemScoreResponse itemScoreResponse = ItemScoreResponse.getInstanceFromXml (output);
				
				return itemScoreResponse;
			}
			
			//this item doesn't have rubric file so it can't be scored. Get the answer from ItemXML
			
			
			return null;
			

		} catch (Exception e) {
			// TODO: handle exception
			throw new ItemScoringException("There is an error while communicating with Item Scoring Engine "+ e);
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

	
}
