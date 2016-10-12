package pl.iis.paw.trello.controller;


import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Krystian on 2016-10-11.
 */

@Controller
public class MainController implements ErrorController {

    @RequestMapping("/")
    public String index(){
        return "index.html";
    }

    @RequestMapping("/error")
    public String error(){
        return "error";
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
