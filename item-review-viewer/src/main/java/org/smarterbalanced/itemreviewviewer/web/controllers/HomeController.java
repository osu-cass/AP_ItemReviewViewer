package org.smarterbalanced.itemreviewviewer.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class HomeController {

    @RequestMapping(value = "/index")
    @ResponseBody
    public ModelAndView Index(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        //TODO: read env variable
        modelAndView.addObject("isDev", true );
        return modelAndView;
    }

}