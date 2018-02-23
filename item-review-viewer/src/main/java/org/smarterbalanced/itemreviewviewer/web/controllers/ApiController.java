package org.smarterbalanced.itemreviewviewer.web.controllers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class ApiController {

    private class Foo {
        private int id;
        public String message;

        Foo(int id, String message){
            this.id = id;
            this.message = message;
        }

        public int getId() {
            return this.id;
        }

        public String getMessage() {
            return this.message;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setMessage(String message){
            this.message = message;
        }

    }


    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    @ResponseBody
    public String helloWorld() throws JsonProcessingException {
        Foo f = new Foo(1, "Hello world.");
        ObjectMapper mapper = new ObjectMapper();
        String str = mapper.writeValueAsString(f);
        return str;
    }
}
