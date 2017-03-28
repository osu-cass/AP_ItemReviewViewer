package org.smarterbalanced.irv.web.controllers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

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
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import AIR.Common.Utilities.SpringApplicationContext;
import tds.blackbox.ContentRequestException;
import tds.iris.abstractions.repository.ContentException;
import tds.iris.abstractions.repository.IContentBuilder;
import tds.itemrenderer.data.IITSDocument;



/**
 * REST API controller for rendering items.
 */

@RestController
public class RenderItemController {
	
	
	  @Autowired
	  private ItemReviewScoringService itemReviewScoringService;
	  
	  private IContentBuilder     _contentBuilder;
	  
	  private static final Logger _logger = LoggerFactory.getLogger (RenderItemController.class);

	  @PostConstruct
	  public synchronized void init () throws ContentException {
	    _contentBuilder = SpringApplicationContext.getBean ("iContentBuilder", IContentBuilder.class);
	  }



  /**
   * Returns content.
   *
   * @param itemId             Item bank and item ID separated by a "-"
   * @param accommodationCodes Feature codes delimited by semicolons.
   * @return content object.
   */
	  /*
  @RequestMapping(value = "/sbac/{item:\\d+[-]\\d+}", method = RequestMethod.GET)
  @ResponseBody
  public ModelAndView getContent(@PathVariable("item") String itemId,
                                 @RequestParam(value = "isaap", required = false,
                                         defaultValue = "")
                                         String accommodationCodes
  ) {
    //Request is in the format
    String[] codes = accommodationCodes.split(";");
    ItemRequestModel item = new ItemRequestModel("I-" + itemId, codes);

    String token = item.generateJsonToken();
    ModelAndView model = new ModelAndView();
    model.setViewName("item");
    model.addObject("token", token);
    model.addObject("item", itemId);
    return model;
  }
  */

	@RequestMapping(value = "/{itemId}", method = RequestMethod.GET)
	@ResponseBody
	public String getContent(@PathVariable("itemId") String itemId,
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

	
	@RequestMapping(value="/score/{itemId}", method = RequestMethod.POST)
	@ResponseBody
    public String scoreItem(@PathVariable("itemId") String itemId,
    						@RequestParam(value = "version", required = false, defaultValue = "") String version,
    						@RequestBody String studentResponse) {

		try {
			String qualifiedItemId = "";
			
			if(version!=null && version.trim().length()>0)
				qualifiedItemId = "I-" + itemId + "-" + version;
			else	
				qualifiedItemId = "I-" + itemId;
			
			IITSDocument iitsDocument =  _contentBuilder.getITSDocument(qualifiedItemId);
			ItemScoreInfo itemScoreInfo = itemReviewScoringService.scoreAssessmentItem(studentResponse, iitsDocument);

			ObjectMapper objectMapper = new ObjectMapper();
			String jsonString = objectMapper.writeValueAsString(itemScoreInfo);
			return jsonString;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return null;
    }
	
	
	public String getStudentResponse(InputStream inputStream) throws ContentRequestException {
	    try {
	      BufferedReader bufferedReader = new BufferedReader (new InputStreamReader (inputStream));
	      String line = null;
	      StringBuilder builder = new StringBuilder ();
	      while ((line = bufferedReader.readLine ()) != null) {
	        builder.append (line);
	      }
	      return builder.toString();
	    } catch (Exception exp) {
	      _logger.error ("Error deserializing ContentRequest from JSON", exp);
	      throw new ContentRequestException ("Error deserializing ContentRequest from JSON. " + exp.getMessage ());
	    }
	  }

}
