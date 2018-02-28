package org.smarterbalanced.itemreviewviewer.web.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.smarterbalanced.itemreviewviewer.web.mocks.MockAboutItemMetadata;
import org.smarterbalanced.itemreviewviewer.web.models.ItemMetadata;
import org.smarterbalanced.itemreviewviewer.web.models.RubricList;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class ApiController {

    // I realize we probably wont have all of the parameters in the URL
    // path once we get the front end up and running
    @RequestMapping(value = "/{itemKey}-{bankKey}-{revision}-{section}", method = RequestMethod.GET)
    @ResponseBody
    public String getAboutItemMetadata(@PathVariable("itemKey") String itemKey,
                                       @PathVariable("bankKey") String bankKey,
                                       @PathVariable("revision") String revision,
                                       @PathVariable("section") String section){
        MockAboutItemMetadata md = new MockAboutItemMetadata();
        ItemMetadata meta = md.getMetadata(itemKey, bankKey, revision, section);
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try{
            json = mapper.writeValueAsString(meta);
        }catch(JsonProcessingException e){
            System.out.println(e.getMessage());
        }

        return json;
    }

    // Once again, the variables are sent in the URL path, we will change this
    // functionality in the future
    @RequestMapping(value = "/rubric/{itemKey}-{bankKey}-{revision}", method = RequestMethod.GET)
    @ResponseBody
    public String getItemRubric(@PathVariable("itemKey") String itemKey,
                                       @PathVariable("bankKey") String bankKey,
                                       @PathVariable("revision") String revision){
        String json = "";
        RubricList rubrics = new RubricList();
        for(int i = 0; i < 10; i++){
            rubrics.addRubric("Rubric" + i,  "100", "'<div>rubric "+i+" for revision "+revision+"</div>'");
        }
        ObjectMapper mapper = new ObjectMapper();
        try{
            json = mapper.writeValueAsString(rubrics);
        }catch(JsonProcessingException e){
            System.out.println(e.getMessage());
        }

        return json;
    }

}
