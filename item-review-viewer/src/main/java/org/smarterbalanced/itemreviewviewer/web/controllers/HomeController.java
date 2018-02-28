package org.smarterbalanced.itemreviewviewer.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;



@Controller
public class HomeController {
    @Autowired
    private Environment environment;

    @RequestMapping(value = "/index")
    @ResponseBody
    public ModelAndView Index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        String spring_config_name = environment.getProperty("SPRING_CONFIG_NAME");
        if (spring_config_name != null) {
            modelAndView.addObject(spring_config_name, true);
        }

        return modelAndView;
    }

}