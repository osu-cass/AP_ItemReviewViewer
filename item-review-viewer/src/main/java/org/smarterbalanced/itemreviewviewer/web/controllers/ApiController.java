package org.smarterbalanced.itemreviewviewer.web.controllers;

import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONException;
import java.io.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarterbalanced.itemreviewviewer.web.config.ItemBankConfig;
import org.smarterbalanced.itemreviewviewer.web.services.models.Attrib;
import org.smarterbalanced.itemreviewviewer.web.services.models.Metadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.smarterbalanced.itemreviewviewer.web.services.GitLabService;
import org.smarterbalanced.itemreviewviewer.web.services.GitLabUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ApiController {
    @Autowired
    private GitLabService _gitLabService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiController.class);

    private static final Map<String, String> gradeEnum = createGrades();

    private static Map<String, String> createGrades(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("3", "1");
        map.put("4", "2");
        map.put("5", "4");
        map.put("6", "8");
        map.put("7", "16");
        map.put("8", "32");
        map.put("9", "64");
        map.put("10", "128");
        map.put("11", "256");
        map.put("12", "512");
        return map;
    }

    @RequestMapping(value="GetAccessibility", method = RequestMethod.GET)
    @ResponseBody
    private ResponseEntity<String> getAccessibility(
            @RequestParam(value="interactionType") String interactionType,
            @RequestParam(value="subject") String subjectCode,
            @RequestParam(value="gradeLevel") String gradeLevels,
            @RequestParam(value = "allowCalculator") String allowCalculator,
            @RequestParam(value = "itemKey") String itemKey,
            @RequestParam(value = "bankKey") String bankKey,
            @RequestParam(value = "namespace") String namespace) throws JSONException, IOException {

        String itemDir = GitLabUtils.makeDirId(bankKey, itemKey);
        String itemId = GitLabUtils.makeItemId(bankKey, itemKey);
        Metadata metadata = _gitLabService.getMetadata(itemDir);

        JsonNode rootNode = getSiwAccessibility(interactionType, subjectCode, gradeLevels, metadata);

        Boolean isPerformanceTask = false;

        Attrib PT = _gitLabService.getItemData(itemDir).getItem().getAttribList().getAttrib()[3];
        if (!PT.getVal().isEmpty()) {
            isPerformanceTask = true;
        }

        if(metadata.getSmarterAppMetadata().getLanguage().contains("spa")){
            setSelection(rootNode, "Language", "ESN", false);
        } else {
            setSelection(rootNode, "Language", "ESN", true);
        }

        if(!allowCalculator.equals("") && !metadata.getSmarterAppMetadata().getInteractionType().equals("WER")) {
            setResource(rootNode, "EnglishDictionary", true);
            setResource(rootNode, "Thesaurus", true);
        }
        if(!isPerformanceTask) {
            setResource(rootNode, "GlobalNotes", true);
        }

        if (!_gitLabService.isItemAccomExists(itemDir, "brf")) {
            setResource(rootNode, "BrailleType", true);
        }

        if (!_gitLabService.isItemAccomExists(itemDir, "mp4")) {
            setResource(rootNode, "AmericanSignLanguage", true);
        }
        String claim = _gitLabService.getClaim(itemId);
        if(!StringUtils.isEmpty(claim) && claim.equals("ELA3")) {
            setResource(rootNode, "ClosedCaptioning", true);
        }
        String itemResult = rootNode.toString();
        JSONArray jsonResult;
        try{
            jsonResult = new JSONArray(itemResult);
        }catch(JSONException e) {
            String err = "Accessibility options for item" + itemDir + " Not found.";
            return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
        }
            return new ResponseEntity<>(jsonResult.toString(), HttpStatus.OK);
    }

    private void setResource(JsonNode rootNode, String resourceCode, Boolean disable) {
        JsonNode resourceNode = findResource(rootNode, resourceCode);
        if(resourceNode != null){
            ((ObjectNode) resourceNode).put("disabled", disable);
        }
    }

    private void setSelection(JsonNode rootNode, String resource,String code, Boolean disable){
        JsonNode resourceNode = findResource(rootNode, resource);
        if(resourceNode != null){
            JsonNode selectionNode = resourceNode.path("selections");
            JsonNode selection = findSelection(selectionNode, code);
            if(selection != null){
                ((ObjectNode) selection).put("disabled", disable);
            }
        }
    }

    private JsonNode findResource(JsonNode rootNode, String code) {
        JsonNode accNode;
        JsonNode resourceNode;
        for(int i = 0; i < rootNode.size(); i++) {
            accNode = rootNode.get(i).path("accessibilityResources");
            for (int j = 0; j < accNode.size(); j++) {
                resourceNode = accNode.get(j);
                if (resourceNode.toString().contains(code)) {
                    return resourceNode;
                }
            }
        }
        return null;
    }

    private JsonNode findSelection(JsonNode node, String code){
        JsonNode selection;
        for(int i = 0; i < node.size(); i++){
            selection = node.get(i);
            if(selection.toString().contains(code)){
                return selection;
            }
        }
        return null;
    }


    private JsonNode getSiwAccessibility(String interactionType, String subjectCode, String gradeLevels, Metadata metadata)  throws IOException {

        String url = ItemBankConfig.get("siw.accessibilityUrl");
        if(StringUtils.isEmpty(gradeLevels)){
            url = url + "gradeLevels=1023";
        } else {
            String gradeValue = gradeEnum.get(gradeLevels);
            url = url + "gradeLevels=" + gradeValue;
        }
        Boolean isPerformanceTask = false;
        if(!subjectCode.equals("")){
            //NOTE: subjectCode only works with uppercase letters, otherwise the api returns Internal Error(500)
            url = url + "&subjectCode=" + subjectCode.toUpperCase();
        } else {
            url = url + "&subjectCode=ELA";
        }
        if(!interactionType.equals("")){
            url = url + "&interactionType=" + interactionType;
        } else {
            url = url + "&interactionType=TI";
        }

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
        byte[] jsonData = result.getBytes(StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonData);

        return rootNode;
    }
}

