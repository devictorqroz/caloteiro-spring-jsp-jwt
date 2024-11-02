package com.caloteiros.caloteiro.interfaces.controllers;

import com.caloteiros.caloteiro.application.dto.CaloteiroDTO;
import com.caloteiros.caloteiro.application.dto.CaloteiroMinDTO;
import com.caloteiros.caloteiro.domain.entities.Caloteiro;
import com.caloteiros.caloteiro.domain.exceptions.CaloteiroNotFoundException;
import com.caloteiros.caloteiro.domain.services.CaloteiroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/caloteiros")
public class CaloteiroController {

    @Autowired
    CaloteiroService caloteiroService;

    @GetMapping
    public ModelAndView findAll() {
        List<Caloteiro> caloteiros = caloteiroService.findAll();
        ModelAndView mv = new ModelAndView("caloteiros/list-caloteiros");
        mv.addObject("caloteiros", caloteiros);
        return mv;
    }

    @GetMapping("/new")
    public ModelAndView displayCaloteiroForm() {
        ModelAndView model = new ModelAndView("caloteiros/new-caloteiro");
        return model;
    }

    @PostMapping
    public ModelAndView createCaloteiro(
            @Valid @ModelAttribute CaloteiroMinDTO newCaloteiroRequest,
            BindingResult bindingResult) {
        ModelAndView mv = new ModelAndView();

        if (bindingResult.hasErrors()) {
            mv.setViewName("caloteiros/new-caloteiro");
            return mv;
        }

        Caloteiro caloteiro = newCaloteiroRequest.toCaloteiro();
        this.caloteiroService.create(caloteiro);

        mv.setViewName("caloteiros/caloteiro-created");
        return mv;
    }

    @DeleteMapping("/{id}")
    public String deleteCaloteiroById(@PathVariable Long id) {
        caloteiroService.deleteById(id);
        return "redirect:/caloteiros/caloteiro-deleted";
    }

    @GetMapping("/{id}/edit")
    public ModelAndView displayUpdateCaloteiroForm(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView();

        try {
            Caloteiro caloteiroToUpdate = caloteiroService.findById(id);
            mv.setViewName("caloteiros/update-caloteiro");
            mv.addObject("caloteiro", caloteiroToUpdate);
        } catch  (CaloteiroNotFoundException e) {
            mv.setViewName("error/caloteiro-error");
            mv.addObject("errorMessage", e.getMessage());
        }
        return mv;
    }

    @PutMapping("/{id}")
    public ModelAndView updateCaloteiro(
            @Valid @ModelAttribute CaloteiroDTO updateCaloteiro,
            BindingResult bindingResult) {

        ModelAndView mv = new ModelAndView();

        if (bindingResult.hasErrors()) {
            mv.setViewName("caloteiros/update-caloteiro");
            return mv;
        }

        Caloteiro caloteiro = updateCaloteiro.toCaloteiro();
        caloteiroService.update(caloteiro);

        mv.setViewName("redirect:/caloteiros/caloteiro-updated");
        return mv;
    }

    @GetMapping("/caloteiro-deleted")
    public ModelAndView caloteiroDeleted() {
        return new ModelAndView("caloteiros/caloteiro-deleted");
    }


    @GetMapping("/caloteiro-updated")
    public ModelAndView caloteiroUpdated() {
        return new ModelAndView("caloteiros/caloteiro-updated");
    }
}
