package org.smarterbalanced.irv.web.controllers;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarterbalanced.irv.core.model.ItemRequestModel;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;



/**
 * REST API controller for rendering items.
 */

@RestController
public class RenderItemController {
	
	
	  private static final Logger _logger = LoggerFactory.getLogger (RenderItemController.class);

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

	
}
