package org.smarterbalanced.itemreviewviewer.web.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.internal.Nullable;
import org.smarterbalanced.itemreviewviewer.web.mocks.MockAboutItemMetadata;
import org.smarterbalanced.itemreviewviewer.web.models.ItemMetadata;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
public class ItemController {

    // I realize we probably wont have all of the parameters in the URL
    // path once we get the front end up and running
    @RequestMapping(value = "/{bankKey}-{itemKey}?section={section}&revision={revision}", method = RequestMethod.GET)
    @ResponseBody
    public String getItem(@PathVariable("bankKey") String bankKey,
                                       @PathVariable("itemKey") String itemKey,
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

    @RequestMapping(value = "revision/accessibility", method = RequestMethod.GET)
    @ResponseBody
    public String getAccessibilityRevision(){
        MockAboutItemMetadata md = new MockAboutItemMetadata();
        ObjectMapper mapper = new ObjectMapper();
        String json = "";

        return json;
    }

    @RequestMapping(value = "revision/item", method = RequestMethod.GET)
    @ResponseBody
    public String getItemRevision(){
        MockAboutItemMetadata md = new MockAboutItemMetadata();
        ObjectMapper mapper = new ObjectMapper();
        String json = "";

        return json;
    }

    @RequestMapping(value = "revision/revisions", method = RequestMethod.GET)
    @ResponseBody
    public String getRevisions(){
        MockAboutItemMetadata md = new MockAboutItemMetadata();
        ObjectMapper mapper = new ObjectMapper();
        String json = "";

        return json;
    }

    @RequestMapping(value = "sections", method = RequestMethod.GET)
    @ResponseBody
    public String getSections(){
        MockAboutItemMetadata md = new MockAboutItemMetadata();
        ObjectMapper mapper = new ObjectMapper();
        String json = "";

        return json;
    }


}
