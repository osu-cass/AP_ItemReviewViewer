package org.smarterbalanced.itemreviewviewer.web.controllers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.smarterbalanced.itemreviewviewer.web.mocks.MockAboutItemMetadata;
import org.smarterbalanced.itemreviewviewer.web.models.ItemMetadata;
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
                                       @PathVariable("section") String section) throws JsonProcessingException {
        MockAboutItemMetadata md = new MockAboutItemMetadata();
        ItemMetadata meta = md.getMetadata(itemKey, bankKey, revision, section);
        ObjectMapper mapper = new ObjectMapper();
        String str = mapper.writeValueAsString(meta);
        return str;
    }
}
