package org.smarterbalanced.itemreviewviewer.web.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.smarterbalanced.itemreviewviewer.web.models.metadata.ItemMetadataModel;
import org.smarterbalanced.itemreviewviewer.web.models.revisions.RevisionModel;
import org.smarterbalanced.itemreviewviewer.web.models.revisions.SectionModel;
import org.smarterbalanced.itemreviewviewer.web.services.GitLabService;
import org.smarterbalanced.itemreviewviewer.web.services.ItemReviewScoringService;
import org.smarterbalanced.itemreviewviewer.web.services.models.ItemCommit;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tds.blackbox.ContentRequestException;
import tds.irisshared.content.ContentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
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
    public ResponseEntity<String> getMetadata(@RequestParam(value = "bankKey") String bankKey,
                             @RequestParam(value = "itemKey") String itemKey,
                             @RequestParam(value = "section", required = false, defaultValue = "") String section,
                             @RequestParam(value = "revision", required = false, defaultValue = "") String revision,
                             @RequestParam(value = "isaap", required = false, defaultValue = "") String isaapCodes
    ) throws ContentRequestException {
        String itemId = "Item-" + bankKey + "-" + itemKey;
        // String iitsId = new String("i-" + bankKey + "-" + itemKey);
        if (!revision.equals("")) {
            itemId = itemId + "-" + revision;
        }

        ObjectMapper mapper = new ObjectMapper();
        String json = "";

        ItemMetadataModel meta;
        try {
            meta = gitLabService.getItemMetadata(itemId, section);
            json = mapper.writeValueAsString(meta);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.toString(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(json, HttpStatus.OK);
    }


    @RequestMapping(value = "revisions", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> getItemRevisions(@RequestParam(value = "bankKey") String bankKey,
                                   @RequestParam(value = "itemKey") String itemKey,
                                   @RequestParam(value = "section", required = false, defaultValue = "") String section,
                                   @RequestParam(value = "isaap", required = false, defaultValue = "") String isaapCodes
    ){
        String itemId = "item-" + bankKey + "-" + itemKey;

        ObjectMapper mapper = new ObjectMapper();
        String json;
        List<ItemCommit> commits;
        List<RevisionModel> revisions;

        try{
            commits = gitLabService.getItemCommits(itemId);
            revisions = new ArrayList<>();
            for(ItemCommit commit: commits){
                revisions.add(new RevisionModel(commit.getAuthorName(),commit.getMessage(), commit.getId(), false));
            }
            json = mapper.writeValueAsString(revisions);
        }catch(JsonProcessingException e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.toString(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(json, HttpStatus.OK);
    }


    @RequestMapping(value = "banksections", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> getSections(){
        List<SectionModel> sections = Arrays.asList(
                new SectionModel("ELA", "ELA"),
                new SectionModel("MATH", "MATH"),
                new SectionModel("SIW","SIW"));
        ObjectMapper mapper = new ObjectMapper();
        String json;
        try{
            json = mapper.writeValueAsString(sections);
        }catch(JsonProcessingException e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.toString(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(json, HttpStatus.OK);
    }
}