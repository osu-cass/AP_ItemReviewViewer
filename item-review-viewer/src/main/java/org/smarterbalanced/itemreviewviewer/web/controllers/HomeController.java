package org.smarterbalanced.itemreviewviewer.web.controllers;

import org.smarterbalanced.itemreviewviewer.web.config.ItemBankConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @RequestMapping(value = "/index")
    @ResponseBody
    public ModelAndView Index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        modelAndView.addObject("spring_config", ItemBankConfig.get("content.source"));

        return modelAndView;
    }

}