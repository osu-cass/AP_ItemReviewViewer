package org.smarterbalanced.itemreviewviewer.web.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.smarterbalanced.itemreviewviewer.web.mocks.MockItemMetadataModel;
import org.smarterbalanced.itemreviewviewer.web.models.ItemMetadataModel;
import org.smarterbalanced.itemreviewviewer.web.models.Metadata;
import org.smarterbalanced.itemreviewviewer.web.services.GitLabService;
import org.smarterbalanced.itemviewerservice.core.DiagnosticApi.Models.ItemRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RenderItemController {

    @Autowired
    private GitLabService gitLabService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public String getMetadata(@RequestParam(value = "bankKey") String bankKey,
                             @RequestParam(value = "itemKey") String itemKey,
                             @RequestParam(value = "section", required = false, defaultValue = "") String section,
                             @RequestParam(value = "revision", required = false, defaultValue = "") String revision,
                             @RequestParam(value = "isaap", required = false, defaultValue = "") String isaapCodes
    ) {
        String itemId = new String("item-" + bankKey + "-" + itemKey);
        if(!revision.equals("")){
            itemId = new String(itemId + "-" + revision);


        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try{
            json = mapper.writeValueAsString(gitLabService.getMetadata(itemId));
        }catch(JsonProcessingException e){
            System.out.println(e.getMessage());
        }

        return json;
    }
}