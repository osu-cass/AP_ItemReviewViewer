package org.smarterbalanced.itemreviewviewer.web.controllers;

import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.smarterbalanced.itemreviewviewer.web.mocks.MockItemMetadataModel;
import org.smarterbalanced.itemreviewviewer.web.models.GradeLevel;
import org.smarterbalanced.itemreviewviewer.web.models.ItemMetadataModel;
import org.smarterbalanced.itemreviewviewer.web.models.RevisionModel;
import org.smarterbalanced.itemreviewviewer.web.models.SectionModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@Controller
public class ApiController {

    @RequestMapping(value="GetAccessibility", method = RequestMethod.GET)
    @ResponseBody
    private String getAccessibility(@RequestParam(value="gradeLevels", required = false) GradeLevel gradeLevels,
                                    @RequestParam(value="subject", required = false) String subjectCode,
                                    @RequestParam(value="interactionType", required = false) String interactionType)
    {
        final String uri = "http://siw-dev.cass.oregonstate.edu/Item/GetAccessibility?gradeLevels=7&subjectCode=ELA&interactionType=SA";

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);
        JSONArray jsonResult = new JSONArray();
        try{
            jsonResult = new JSONArray(result);

        }catch(JSONException e) {
            System.out.println(e.getMessage());
        }

        return jsonResult.toString();
    }

    @RequestMapping(value = "AboutItem", method = RequestMethod.GET)
    @ResponseBody
    public String getAboutItem(@RequestParam("bankKey") String bankKey,
                               @RequestParam("itemKey") String itemKey,
                               @RequestParam("section") String section,
                               @RequestParam(value="revision", required = false) String revision){
        ItemMetadataModel meta = new MockItemMetadataModel(bankKey, itemKey, section, revision).itemMeta;
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
