package org.smarterbalanced.itemreviewviewer.web.controllers;

import AIR.Common.Utilities.SpringApplicationContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarterbalanced.itemreviewviewer.web.config.ItemBankConfig;
import org.smarterbalanced.itemreviewviewer.web.models.ItemModel;
import org.smarterbalanced.itemreviewviewer.web.models.ItemRequestModel;
import org.smarterbalanced.itemreviewviewer.web.models.metadata.ItemMetadataModel;
import org.smarterbalanced.itemreviewviewer.web.models.revisions.RevisionModel;
import org.smarterbalanced.itemreviewviewer.web.models.revisions.SectionModel;
import org.smarterbalanced.itemreviewviewer.web.models.scoring.ItemScoreInfo;
import org.smarterbalanced.itemreviewviewer.web.services.GitLabException;
import org.smarterbalanced.itemreviewviewer.web.services.GitLabService;
import org.smarterbalanced.itemreviewviewer.web.services.GitLabUtils;
import org.smarterbalanced.itemreviewviewer.web.services.ItemReviewScoringService;
import org.smarterbalanced.itemreviewviewer.web.services.models.ItemCommit;
import org.smarterbalanced.itemreviewviewer.web.services.models.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tds.blackbox.ContentRequestException;
import tds.irisshared.content.ContentException;
import tds.irisshared.repository.IContentBuilder;
import tds.itemrenderer.data.IITSDocument;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Controller
public class RenderItemController {

    private static final Logger _logger = LoggerFactory.getLogger(RenderItemController.class);

    @Autowired
    private GitLabService _gitLabService;

    @Autowired
    private IContentBuilder _contentBuilder;

    @Autowired
    private ItemReviewScoringService _itemReviewScoringService;

    public static String namespacePatchCycle = ItemBankConfig.get("gitlab.namespace.patch.cycle");
    private static String namespaces;

    @PostConstruct
    public synchronized void init() throws ContentException {
        _contentBuilder = SpringApplicationContext.getBean("iContentBuilder", IContentBuilder.class);
        this.getNamespacesFromGitlab();
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> getMetadata(
            @RequestParam(value = "namespace") String namespace,
            @RequestParam(value = "bankKey", required = false, defaultValue = "") String bankKey,
            @RequestParam(value = "itemKey") String itemKey,
            @RequestParam(value = "section", required = false, defaultValue = "") String section,
            @RequestParam(value = "revision", required = false, defaultValue = "") String revision,
            @RequestParam(value = "isaap", required = false, defaultValue = "") String isaapCodes
    ) throws ContentRequestException {
        if (StringUtils.isEmpty(bankKey)) bankKey = _getBankKeyByNamespace(namespace);
        String itemId = GitLabUtils.makeDirId(bankKey, itemKey);
        if (!revision.equals("")) {
            itemId = itemId + "-" + revision;
        }

        ObjectMapper mapper = new ObjectMapper();
        String json = "";

        ItemMetadataModel meta;
        try {
            meta = _gitLabService.getItemMetadata(namespace, itemId, section);
            json = mapper.writeValueAsString(meta);
        } catch (Exception e) {
            String err = "Item (" + itemId + ") not found.";
            return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @RequestMapping(value = "revisions", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> getItemRevisions(
            @RequestParam(value = "namespace") String namespace,
            @RequestParam(value = "bankKey", required = false, defaultValue = "") String bankKey,
            @RequestParam(value = "itemKey") String itemKey,
            @RequestParam(value = "section", required = false, defaultValue = "") String section,
            @RequestParam(value = "isaap", required = false, defaultValue = "") String isaapCodes
    ) {
        if (StringUtils.isEmpty(bankKey)) {
            bankKey = _getBankKeyByNamespace(namespace);
        }

        String itemId = GitLabUtils.makeDirId(bankKey, itemKey);

        ObjectMapper mapper = new ObjectMapper();
        String json;
        List<ItemCommit> commits;
        List<RevisionModel> revisions;

        try {
            commits = _gitLabService.getItemCommits(namespace, itemId);
            revisions = new ArrayList<>();
            for (ItemCommit commit : commits) {
                revisions.add(new RevisionModel(commit.getAuthorName(), commit.getCreationDate().getTime(),
                        commit.getMessage(), commit.getId(), false));
            }
            json = mapper.writeValueAsString(revisions);
        } catch (JsonProcessingException e) {
            String err = "Item revisions for (" + itemId + ") not found.";
            return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(json, HttpStatus.OK);
    }


    private String _getBankKeyByNamespace(String namespace) {
        if (GitLabUtils.noBankKeyNamespaceHash.containsKey(namespace)) {
            return String.valueOf(GitLabUtils.noBankKeyNamespaceHash.get(namespace));
        }

        return null;
    }

    public void getNamespacesFromGitlab() {
        ObjectMapper mapper;

        try {
            List<Namespace> _namespaces = _gitLabService.getNamespaces();

            mapper = new ObjectMapper();
            namespaces = mapper.writeValueAsString(_namespaces);
        } catch (Exception e) {
            _logger.error("Failed to get namespaces from Gitlab", e);
            throw new GitLabException(e);
        }
    }

    @RequestMapping(value = "namespaces", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> getNamespaces() {
        return new ResponseEntity<>(namespaces, HttpStatus.OK);
    }

    @RequestMapping(value = "banksections", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> getSections() {
        List<SectionModel> sections = Arrays.asList(
                new SectionModel("ELA", "ELA"),
                new SectionModel("MATH", "MATH"),
                new SectionModel("SIW", "SIW"));
        ObjectMapper mapper = new ObjectMapper();
        String json;
        try {
            json = mapper.writeValueAsString(sections);
        } catch (JsonProcessingException e) {
            String err = "Bank sections not found.";
            return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @RequestMapping(value = {"/items"}, method = {RequestMethod.GET})
    @ResponseBody
    public ModelAndView getIvsContent(
            @RequestParam(value = "ids", required = true) String[] itemId,
            @RequestParam(value = "revision", required = false, defaultValue = "") String revision,
            @RequestParam(value = "section", required = false, defaultValue = "") String section,
            @RequestParam(value = "scrollToId", required = false, defaultValue = "") String scrollToId,
            @RequestParam(value = "isaap", required = false, defaultValue = "") String accommodationCodes,
            @RequestParam(value = "readOnly", required = false, defaultValue = "false") boolean readOnly,
            @RequestParam(value = "loadFrom", required = false, defaultValue = "/home/tomcat7/content/gitlab") String loadFrom) {
        HashSet<String> codeSet = new HashSet<>(Arrays.asList(accommodationCodes.split(";")));
        ArrayList<String> codes = new ArrayList<>(codeSet);
        ItemRequestModel item;
        if (section.isEmpty() && revision.isEmpty()) {
            item = new ItemRequestModel(itemId, codes, loadFrom);
        } else {
            item = new ItemRequestModel(itemId, codes, section, revision, loadFrom);
        }

        String token = item.generateJsonToken();
        String scrollToDivId = "";
        if (!scrollToId.equals("")) {
            try {
                scrollToDivId = "QuestionNumber_" + scrollToId.split("-")[1];
            } catch (IndexOutOfBoundsException var12) {
                // TODO: handle exception
                _logger.error("Exception occurred", var12);
            }
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
    public String scoreItem(
            @PathVariable("itemId") String itemId,
            @RequestParam(value = "version", required = false, defaultValue = "") String version,
            @RequestBody String studentResponse) {
        try {
            String qualifiedItemId = GitLabUtils.makeQualifiedItemId(itemId, version);

            IITSDocument iitsDocument = _contentBuilder.getITSDocument(qualifiedItemId);
            ItemScoreInfo itemScoreInfo = _itemReviewScoringService.scoreAssessmentItem(studentResponse, iitsDocument);

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(itemScoreInfo);

        } catch (Exception e) {
            // TODO: handle exception
            throw new ScoringException(e.getMessage(), itemId);
        }
    }

    @RequestMapping(value = "/{img}.png", method = RequestMethod.GET)
    @ResponseBody
    public byte[] rubricImage(
            @PathVariable("img") String img,
            @RequestParam("itemId") String itemId,
            @RequestParam(value = "version", required = false, defaultValue = "") String version) throws Exception {
        try {
            String qualifiedItemId = GitLabUtils.makeQualifiedItemId(itemId, version);
            IITSDocument iitsDocument = _contentBuilder.getITSDocument(qualifiedItemId);
            String path = StringUtils.join(iitsDocument.getBaseUriDirSegments(), File.separator) + File.separator + img + ".png";
            return Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            throw new Exception();
        }
    }

    @RequestMapping(value = "/checkItemExistence", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ItemModel[]> existItem(@RequestBody ItemModel[] items) {
        for (ItemModel item : items) {
            if (StringUtils.isEmpty(item.getBankKey()))
                item.setBankKey(_getBankKeyByNamespace(item.getNamespace()));
            try {
                item.setExists(_gitLabService.isItemExist(item));
            } catch (Exception e) {
                _logger.error("Exception occurred '\'" + item.toString(), e);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<>(items, HttpStatus.OK);
    }
}