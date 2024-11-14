package com.caloteiros.caloteiro.interfaces.controllers;

import com.caloteiros.caloteiro.application.dto.UpdateCaloteiroDTO;
import com.caloteiros.caloteiro.application.dto.CreateCaloteiroDTO;
import com.caloteiros.caloteiro.application.dto.CaloteiroDTO;
import com.caloteiros.caloteiro.domain.exceptions.CaloteiroException;
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
        List<CaloteiroDTO> caloteiros = caloteiroService.list();
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
            @Valid @ModelAttribute CreateCaloteiroDTO createCaloteiroDTO,
            BindingResult bindingResult) {
        ModelAndView mv = new ModelAndView();

        if (bindingResult.hasErrors()) {
            mv.setViewName("caloteiros/new-caloteiro");
            return mv;
        }

        this.caloteiroService.create(createCaloteiroDTO);

        mv.setViewName("caloteiros/caloteiro-created");
        return mv;
    }

    @GetMapping("/{id}/edit")
    public ModelAndView displayUpdateCaloteiroForm(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView();

        try {
            CaloteiroDTO updateCaloteiro = caloteiroService.findById(id);
            mv.setViewName("caloteiros/update-caloteiro");
            mv.addObject("updateCaloteiro", updateCaloteiro);
        } catch (CaloteiroException e) {
            mv.setViewName("error/caloteiro-error");
            mv.addObject("errorMessage", e.getMessage());
        }
        return mv;
    }

    @PutMapping("/{id}")
    public ModelAndView updateCaloteiro(
            @PathVariable Long id,
            @Valid @ModelAttribute UpdateCaloteiroDTO updateCaloteiro,
            BindingResult bindingResult) {

        ModelAndView mv = new ModelAndView();

        if (bindingResult.hasErrors()) {
            mv.setViewName("caloteiros/update-caloteiro");
            return mv;
        }

        caloteiroService.update(id, updateCaloteiro);

        mv.setViewName("redirect:/caloteiros/caloteiro-updated");
        return mv;
    }

    @DeleteMapping("/{id}")
    public ModelAndView deleteCaloteiroById(@PathVariable Long id) {
        caloteiroService.deleteById(id);
        return new ModelAndView("redirect:/caloteiros/caloteiro-deleted");
    }

    @GetMapping("/caloteiro-updated")
    public ModelAndView caloteiroUpdated() {
        return new ModelAndView("caloteiros/caloteiro-updated");
    }

    @GetMapping("/caloteiro-deleted")
    public ModelAndView caloteiroDeleted() {
        return new ModelAndView("caloteiros/caloteiro-deleted");
    }
}