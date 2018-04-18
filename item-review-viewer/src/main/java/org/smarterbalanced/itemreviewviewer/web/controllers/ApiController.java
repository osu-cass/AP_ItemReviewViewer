package org.smarterbalanced.itemreviewviewer.web.controllers;

import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONException;
import org.smarterbalanced.itemreviewviewer.web.config.SettingsReader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@Controller
public class ApiController {

    @RequestMapping(value="GetAccessibility", method = RequestMethod.GET)
    @ResponseBody
    private ResponseEntity<String> getAccessibility(@RequestParam(value="interactionType") String interactionType,
                                            @RequestParam(value="subject") String subjectCode,
                                            @RequestParam(value="gradeLevel") String gradeLevels)
    {
        String url = SettingsReader.get("siw.accessibilityUrl") + "gradeLevels=" + gradeLevels;
        if(!subjectCode.equals("")){
            url = url + "&subjectCode=" + subjectCode;
        }
        if(!interactionType.equals("")){
            url = url + "&interactionType=" + interactionType;
        }

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
        JSONArray jsonResult = new JSONArray();
        try{
            jsonResult = new JSONArray(result);
        }catch(JSONException e) {
            String err = "Accessibility options for item not found.";
            return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(jsonResult.toString(), HttpStatus.OK);
    }

}
