package org.smarterbalanced.itemreviewviewer.web.controllers;

import AIR.Common.Utilities.SpringApplicationContext;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.smarterbalanced.itemreviewviewer.web.models.ItemRequestModel;
import org.smarterbalanced.itemreviewviewer.web.models.metadata.ItemMetadataModel;
import org.smarterbalanced.itemreviewviewer.web.models.revisions.RevisionModel;
import org.smarterbalanced.itemreviewviewer.web.models.revisions.SectionModel;
import org.smarterbalanced.itemreviewviewer.web.models.scoring.ItemScoreInfo;
import org.smarterbalanced.itemreviewviewer.web.services.GitLabService;
import org.smarterbalanced.itemreviewviewer.web.services.ItemReviewScoringService;
import org.smarterbalanced.itemreviewviewer.web.services.models.ItemCommit;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;
import tds.blackbox.ContentRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tds.irisshared.content.ContentException;
import tds.irisshared.repository.IContentBuilder;
import tds.itemrenderer.data.IITSDocument;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Controller
public class RenderItemController {

    @Autowired
    private GitLabService _gitLabService;

    @Autowired
    private IContentBuilder _contentBuilder;

    @Autowired
    private ItemReviewScoringService _itemReviewScoringService;


    @PostConstruct
    public synchronized void init() throws ContentException {
        _contentBuilder = SpringApplicationContext.getBean("iContentBuilder", IContentBuilder.class);
    }

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
            meta = _gitLabService.getItemMetadata(itemId, section);
            json = mapper.writeValueAsString(meta);
        } catch (Exception e) {
            String err = "Item ("+ itemId +") not found.";
            return new ResponseEntity<>(err,HttpStatus.NOT_FOUND);
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
            commits = _gitLabService.getItemCommits(itemId);
            revisions = new ArrayList<>();
            for(ItemCommit commit: commits){
                revisions.add(new RevisionModel(commit.getAuthorName(),commit.getMessage(), commit.getId(), false));
            }
            json = mapper.writeValueAsString(revisions);
        }catch(JsonProcessingException e){
            String err = "Item revisions for ("+ itemId +") not found.";
            return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
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
            String err = "Bank sections not found.";
            return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @RequestMapping(
            value = {"/items"},
            method = {RequestMethod.GET}
    )
    @ResponseBody
    public ModelAndView getIvsContent(@RequestParam(value = "ids",required = true) String[] itemId,
                                   @RequestParam(value = "revision", required = false, defaultValue = "") String revision,
                                   @RequestParam(value = "section", required = false, defaultValue = "") String section,
                                   @RequestParam(value = "scrollToId",required = false,defaultValue = "") String scrollToId,
                                   @RequestParam(value = "isaap",required = false,defaultValue = "") String accommodationCodes,
                                   @RequestParam(value = "readOnly",required = false,defaultValue = "false") boolean readOnly,
                                   @RequestParam(value = "loadFrom",required = false,defaultValue = "") String loadFrom)
    {
        HashSet<String> codeSet = new HashSet<>(Arrays.asList(accommodationCodes.split(";")));
        ArrayList<String> codes = new ArrayList<>(codeSet);
        ItemRequestModel item;
        if(section.isEmpty() && revision.isEmpty()){
            item = new ItemRequestModel(itemId, codes, loadFrom);
        } else {
            item = new ItemRequestModel(itemId, codes, section, revision, loadFrom);
        }

        String token = item.generateJsonToken();
        String scrollToDivId = "";
        if (!scrollToId.equals("")) {
            try {
                scrollToDivId = "QuestionNumber_" + scrollToId.split("-")[1];
            } catch (IndexOutOfBoundsException var12){}
        }



        ModelAndView model = new ModelAndView();
        model.setViewName("item");
        model.addObject("readOnly", readOnly);
        model.addObject("token", token);
        model.addObject("scrollToDivId", scrollToDivId);
        model.addObject("item", itemId[0]);
        return model;
    }

    @RequestMapping(value = "/score/{itemId}", method = RequestMethod.POST)
    @ResponseBody
    public String scoreItem(@PathVariable("itemId") String itemId,
                            @RequestParam(value = "version", required = false, defaultValue = "") String version,
                            @RequestBody String studentResponse) {

        try {
            String qualifiedItemId = "";

            if (version != null && version.trim().length() > 0)
                qualifiedItemId = "I-" + itemId + "-" + version;
            else
                qualifiedItemId = "I-" + itemId;

            IITSDocument iitsDocument = _contentBuilder.getITSDocument(qualifiedItemId);
            ItemScoreInfo itemScoreInfo = _itemReviewScoringService.scoreAssessmentItem(studentResponse, iitsDocument);

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(itemScoreInfo);
            return jsonString;

        } catch (Exception e) {
            // TODO: handle exception
            throw new ScoringException(e.getMessage(), itemId);
        }

    }
}