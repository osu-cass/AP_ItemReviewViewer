package org.smarterbalanced.itemreviewviewer.web.controllers;

import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONException;
import org.smarterbalanced.itemreviewviewer.web.config.SettingsReader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@Controller
public class ApiController {

    @RequestMapping(value="GetAccessibility", method = RequestMethod.GET)
    @ResponseBody
    private String getAccessibility(@RequestParam(value="interactionType", required = false) String interactionType,
                                    @RequestParam(value="subject", required = false) String subjectCode,
                                    @RequestParam(value="gradeLevel", required = false) String gradeLevels)
    {
        String url = new String(SettingsReader.get("siw.accessibilityUrl") + "gradeLevels=" + gradeLevels);
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
            System.out.println(e.getMessage());
        }

        return jsonResult.toString();
    }

}
