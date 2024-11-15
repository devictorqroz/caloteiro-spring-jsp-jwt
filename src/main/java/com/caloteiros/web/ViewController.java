package com.caloteiros.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ViewController {

    @GetMapping(value = {"/", "/home"})
    public ModelAndView home() {
        return new ModelAndView("home");
    }
}
