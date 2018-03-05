package org.smarterbalanced.itemreviewviewer.web.controllers;

import com.amazonaws.services.simpledb.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;



@PropertySource(value={"classpath:application.properties","classpath:application.config.properties"})
@Controller
public class HomeController {

    @Autowired
    private ItemBankConfig itemBankConfig;

    @RequestMapping(value = "/index")
    @ResponseBody
    public ModelAndView Index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        modelAndView.addObject("spring_config", itemBankConfig.getContentSource());

        return modelAndView;
    }

}