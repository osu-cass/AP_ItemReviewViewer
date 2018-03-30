package org.smarterbalanced.itemreviewviewer.web.controllers;

import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

}
