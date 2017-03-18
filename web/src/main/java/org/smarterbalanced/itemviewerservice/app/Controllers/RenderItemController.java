package org.smarterbalanced.itemviewerservice.app.Controllers;

import javax.servlet.http.HttpServletResponse;

import org.smarterbalanced.itemviewerservice.app.Exceptions.ItemNotFoundException;
import org.smarterbalanced.itemviewerservice.core.Models.ItemRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


/**
 * REST API controller for rendering items.
 */

@Controller
public class RenderItemController {

  /**
   * Returns content.
   *
   * @param itemId             Item bank and item ID separated by a "-"
   * @param accommodationCodes Feature codes delimited by semicolons.
   * @return content object.
   */
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

	@RequestMapping(value = "/{item:\\d+[-]\\d+}", method = RequestMethod.GET)
	@ResponseBody
	public String getContent(@PathVariable("item") String itemId,
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
