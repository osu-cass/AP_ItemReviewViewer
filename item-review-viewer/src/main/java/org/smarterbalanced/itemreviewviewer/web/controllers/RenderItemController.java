package org.smarterbalanced.itemreviewviewer.web.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.smarterbalanced.itemreviewviewer.web.models.*;
import org.smarterbalanced.itemreviewviewer.web.services.GitLabService;
import org.smarterbalanced.itemreviewviewer.web.services.ItemReviewScoringService;
import org.springframework.web.servlet.ModelAndView;
import tds.blackbox.ContentRequestException;
import tds.irisshared.content.ContentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.smarterbalanced.itemreviewviewer.web.models.TokenModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Controller
public class RenderItemController {

    @Autowired
    private GitLabService gitLabService;

    @Autowired
    private ContentBuilder contentBuilder;

    @Autowired
    private ItemReviewScoringService scoreService;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public String getMetadata(@RequestParam(value = "bankKey") String bankKey,
                             @RequestParam(value = "itemKey") String itemKey,
                             @RequestParam(value = "section", required = false, defaultValue = "") String section,
                             @RequestParam(value = "revision", required = false, defaultValue = "") String revision,
                             @RequestParam(value = "isaap", required = false, defaultValue = "") String isaapCodes
    ) throws ContentRequestException {
        String itemId = new String("Item-" + bankKey + "-" + itemKey);
        String iitsId = new String("i-" + bankKey + "-" + itemKey);
        if (!revision.equals("")) {
            itemId = new String(itemId + "-" + revision);
        }

        ObjectMapper mapper = new ObjectMapper();
        String json = "";

        ItemMetadataModel meta = gitLabService.getItemMetadata(itemId, section);
        try {
            json = mapper.writeValueAsString(meta);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }

        return json;
    }


    @RequestMapping(value = "revisions", method = RequestMethod.GET)
    @ResponseBody
    public String getItemRevisions(@RequestParam(value = "bankKey") String bankKey,
                                   @RequestParam(value = "itemKey") String itemKey,
                                   @RequestParam(value = "section", required = false, defaultValue = "") String section,
                                   @RequestParam(value = "revision", required = false, defaultValue = "") String revision,
                                   @RequestParam(value = "isaap", required = false, defaultValue = "") String isaapCodes
    ){
        String itemId = new String("item-" + bankKey + "-" + itemKey);
        if (!revision.equals("")) {
            itemId = new String(itemId + "-" + revision);
        }

        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        List<ItemCommit> commits = gitLabService.getItemCommits(itemId);
        List<RevisionModel> revisions = new ArrayList<RevisionModel>();

        for(ItemCommit commit: commits){
            revisions.add(new RevisionModel(commit.getAuthorName(),commit.getMessage(), commit.getId(), false));
        }

        try{
            json = mapper.writeValueAsString(revisions);
        }catch(JsonProcessingException e){
            System.out.println(e.getMessage());
        }

        return json;
    }


    @RequestMapping(value = "banksections", method = RequestMethod.GET)
    @ResponseBody
    public String getSections(){
        List<SectionModel> sections = Arrays.asList(
                new SectionModel("ELA", "ELA"),
                new SectionModel("MATH", "MATH"),
                new SectionModel("SIW","SIW"));
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try{
            json = mapper.writeValueAsString(sections);
        }catch(JsonProcessingException e){
            System.out.println(e.getMessage());
        }

        return json;
    }

    @RequestMapping(
            value = {"/items"},
            method = {RequestMethod.GET}
    )
    @ResponseBody
    public ModelAndView getContent(@RequestParam(value = "ids",required = true) String[] itemId,
                                   @RequestParam(value = "revision", required = false, defaultValue = "") String revision,
                                   @RequestParam(value = "section", required = false, defaultValue = "") String section,
                                   @RequestParam(value = "scrollToId",required = false,defaultValue = "") String scrollToId,
                                   @RequestParam(value = "isaap",required = false,defaultValue = "") String accommodationCodes,
                                   @RequestParam(value = "readOnly",required = false,defaultValue = "false") boolean readOnly,
                                   @RequestParam(value = "loadFrom",required = false,defaultValue = "") String loadFrom)
    {
        HashSet<String> codeSet = new HashSet<>(Arrays.asList(accommodationCodes.split(";")));
        ArrayList<String> codes = new ArrayList<>(codeSet);
        ItemRequestModel item = new ItemRequestModel(itemId, codes, section, revision, loadFrom);
        String token = item.generateJsonToken();
        String scrollToDivId = "";
        if (!scrollToId.equals("")) {
            try {
                scrollToDivId = "QuestionNumber_" + scrollToId.split("-")[1];
            } catch (IndexOutOfBoundsException var12) {
                ;
            }
        }

        ModelAndView model = new ModelAndView();
        model.setViewName("item");
        model.addObject("readOnly", readOnly);
        model.addObject("revision", "Hello");
        model.addObject("section", section);
        model.addObject("token", token);
        model.addObject("scrollToDivId", scrollToDivId);
        model.addObject("item", itemId[0]);
        return model;
    }
}