package org.smarterbalanced.itemreviewviewer.web.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.smarterbalanced.itemreviewviewer.web.mocks.MockAboutItemMetadata;
import org.smarterbalanced.itemreviewviewer.web.models.ItemMetadata;
import org.smarterbalanced.itemreviewviewer.web.models.RevisionModel;
import org.smarterbalanced.itemreviewviewer.web.models.SectionModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
public class ApiController {

    // I realize we probably wont have all of the parameters in the URL
    // path once we get the front end up and running
    @RequestMapping(value = "AboutItem", method = RequestMethod.GET)
    @ResponseBody
    public String getAboutItem(@RequestParam("bankKey") String bankKey,
                               @RequestParam("itemKey") String itemKey,
                               @RequestParam("section") String section,
                               @RequestParam(value="revision", required = false) String revision){
        MockAboutItemMetadata md = new MockAboutItemMetadata();
        ItemMetadata meta = md.getMetadata(itemKey, bankKey, revision, section);
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try{
            json = mapper.   writeValueAsString(meta);
        }catch(JsonProcessingException e){
            System.out.println(e.getMessage());
        }

        return json;
    }

    @RequestMapping(value = "ItemRevisions", method = RequestMethod.GET)
    @ResponseBody
    public String getItemRevisions(@RequestParam("bankKey") String bankKey,
                                    @RequestParam("itemKey") String itemKey,
                                    @RequestParam("section") String section,
                                    @RequestParam(value="revision", required = false) String revision){
        List<RevisionModel> revisions = new ArrayList<RevisionModel>();
        revisions.add(new RevisionModel("Hannah Hacker","commit mssg", "hash1234"));
        revisions.add(new RevisionModel("Timothy Typist","type stuff", "hash9876"));
        revisions.add(new RevisionModel("Peter Parker","I'm actually spiderman", "spidey1234"));
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try{
            json = mapper.writeValueAsString(revisions);
        }catch(JsonProcessingException e){
            System.out.println(e.getMessage());
        }
        return json;
    }

    @RequestMapping(value = "BankSections", method = RequestMethod.GET)
    @ResponseBody
    public String getSections(){
        List<SectionModel> sections = new ArrayList<SectionModel>();
        sections.add(new SectionModel("SIW", "SIW"));
        sections.add(new SectionModel("MATH", "MATH"));
        sections.add(new SectionModel("IAT", "IAT"));
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try{
            json = mapper.writeValueAsString(sections);
        }catch(JsonProcessingException e){
            System.out.println(e.getMessage());
        }

        return json;
    }


}
