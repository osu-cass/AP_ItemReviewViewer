/**
 * 
 */
package org.smarterbalanced.irv.web.controllers;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarterbalanced.irv.core.model.ItemRequestModel;
import org.smarterbalanced.irv.model.ItemScoreInfo;
import org.smarterbalanced.irv.services.ItemReviewScoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import AIR.Common.Utilities.SpringApplicationContext;
import tds.iris.abstractions.repository.ContentException;
import tds.iris.abstractions.repository.IContentBuilder;
import tds.itemrenderer.data.IITSDocument;

/**
 * @author kthotti
 *
 */

/**
 * REST API controller for scoring items.
 */
@RestController
public class ItemController {

	@Autowired
	private ItemReviewScoringService itemReviewScoringService;

	private IContentBuilder _contentBuilder;

	private static final Logger _logger = LoggerFactory.getLogger(ItemController.class);

	/**
	 * 
	 */
	public ItemController() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public synchronized void init() throws ContentException {
		_contentBuilder = SpringApplicationContext.getBean("iContentBuilder", IContentBuilder.class);
	}
	
	@RequestMapping(value = "/{itemId}", method = RequestMethod.GET)
	@ResponseBody
	public String getToken(@PathVariable("itemId") String itemId,
			@RequestParam(value = "isaap", required = false, defaultValue = "") String accommodationCodes,
			@RequestParam(value = "version", required = false, defaultValue = "") String version,
			HttpServletResponse response) {
		String[] codes = accommodationCodes.split(";");
		ItemRequestModel item = null;
		
		if(version!=null && version.trim().length()>0)
			item = new ItemRequestModel("I-" + itemId + "-" + version, codes);
		else
			item = new ItemRequestModel("I-" + itemId, codes);
		
		return item.generateJsonToken();
	}


	@RequestMapping(value = "/score/{itemId}", method = RequestMethod.POST)
	@ResponseBody
	public String scoreItem(@PathVariable("itemId") String itemId,
							@RequestParam(value = "version", required = false, defaultValue = "") String version,
							@RequestBody String studentResponse) {

		try {
			String qualifiedItemId = "";

			if (version != null && version.trim().length() > 0)
				qualifiedItemId = "I-" + itemId + "-" + version;
			else
				qualifiedItemId = "I-" + itemId;

			IITSDocument iitsDocument = _contentBuilder.getITSDocument(qualifiedItemId);
			ItemScoreInfo itemScoreInfo = itemReviewScoringService.scoreAssessmentItem(studentResponse, iitsDocument);

			ObjectMapper objectMapper = new ObjectMapper();
			String jsonString = objectMapper.writeValueAsString(itemScoreInfo);
			return jsonString;

		} catch (Exception e) {
			// TODO: handle exception
			_logger.info("There is an error while scoring the item: " + itemId);
			throw new ScoringException(e.getMessage(), itemId);
		}

	}

}
