package org.smarterbalanced.itemreviewviewer.web.controllers;

import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONException;
import java.io.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang.StringUtils;
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

import java.nio.charset.StandardCharsets;

@Controller
public class ApiController {
    @Autowired
    private GitLabService _gitLabService;



    @RequestMapping(value="GetAccessibility", method = RequestMethod.GET)
    @ResponseBody
    private ResponseEntity<String> getAccessibility(@RequestParam(value="interactionType") String interactionType,
                                            @RequestParam(value="subject") String subjectCode,
                                            @RequestParam(value="gradeLevel") String gradeLevels,
                                            @RequestParam(value = "allowCalculator") String allowCalculator,
                                             @RequestParam(value = "itemKey") String itemKey,
                                                    @RequestParam(value = "bankKey") String bankKey) throws JSONException, IOException

    {
        String url = ItemBankConfig.get("siw.accessibilityUrl") + "gradeLevels=" + gradeLevels;
        String namespace;
        Boolean isPerformanceTask = false;
        if(!subjectCode.equals("")){
            //NOTE: subjectCode only works with uppercase letters, otherwise the api returns Internal Error(500)
            url = url + "&subjectCode=" + subjectCode.toUpperCase();
        }
        if(!interactionType.equals("")){
            url = url + "&interactionType=" + interactionType;
        }
        if(subjectCode.equals("ELA")) {
            namespace = "ELA";
        }
        else if(subjectCode.equals("MATH")) {
            namespace = "iat-development";
        }
        else {
            namespace = "itemreviewviewer";
        }
        String itemId = _makeItemId(bankKey,  itemKey);

        Metadata meta = _gitLabService.getMetadata(namespace, itemId);
        Attrib PT = _gitLabService.getItemData(namespace, itemId).getItem().getAttribList().getAttrib()[3];
        if (!PT.getVal().isEmpty()) {
            isPerformanceTask = true;
        }


        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
        byte[] jsonData = result.getBytes(StandardCharsets.UTF_8);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonData);
        if(!allowCalculator.equals("") && !meta.getSmarterAppMetadata().getInteractionType().equals("WER")) {
            disableResource(rootNode, "EnglishDictionary", 0);
            disableResource(rootNode, "Thesaurus", 0);
        }
        if(!isPerformanceTask) {
            disableResource(rootNode, "GlobalNotes", 0);
        }

        if (!_gitLabService.isItemAccomExists(itemId, "brf")) {
            disableResource(rootNode, "BrailleType", 2);
        }
        if (!_gitLabService.isItemAccomExists(itemId, "MP4")) {
            disableResource(rootNode, "AmericanSignLanguage", 2);
        }
        String claim = _gitLabService.getClaim(itemId);
        if(!StringUtils.isEmpty(claim) && claim.equals("ELA3")) {
            disableResource(rootNode, "ClosedCaptioning", 2);
        }
        String itemResult = rootNode.toString();
        JSONArray jsonResult = new JSONArray();
        try{
            jsonResult = new JSONArray(itemResult);
        }catch(JSONException e) {
            String err = "Accessibility options for item not found.";
            return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(jsonResult.toString(), HttpStatus.OK);
    }
    private String _makeItemId(String bankKey, String itemKey) {
        String itemId;

        if (StringUtils.isNotEmpty(bankKey)) {
            itemId = "item-" + bankKey + "-" + itemKey;
        } else {
            itemId = itemKey;
        }

        return itemId;
    }




    private void disableResource(JsonNode rootNode, String resourceCode, int branch) {
        JsonNode labelNode = rootNode.get(branch);
        JsonNode accNode = labelNode.path("accessibilityResources");
        JsonNode resourceNode = accNode.get(0);
        for(int i = 0; i < accNode.size();i++) {
            resourceNode = accNode.get(i);
            JsonNode valueNode = resourceNode.get("resourceCode");
            if(valueNode.toString().contains(resourceCode)) {
                ((ObjectNode) resourceNode).put("disabled", "true");
                return;

            }

        }


    }
}

