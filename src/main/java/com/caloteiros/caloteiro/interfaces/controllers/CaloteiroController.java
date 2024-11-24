package com.caloteiros.caloteiro.interfaces.controllers;

import com.caloteiros.caloteiro.application.dto.CaloteiroPageDTO;
import com.caloteiros.caloteiro.application.dto.UpdateCaloteiroDTO;
import com.caloteiros.caloteiro.application.dto.CreateCaloteiroDTO;
import com.caloteiros.caloteiro.application.dto.CaloteiroDTO;
import com.caloteiros.caloteiro.domain.services.CaloteiroService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/caloteiros")
public class CaloteiroController {

    private final CaloteiroService caloteiroService;

    public CaloteiroController(CaloteiroService caloteiroService) {
        this.caloteiroService = caloteiroService;
    }

    @GetMapping
    public ModelAndView findAll(
            @RequestParam(defaultValue = "0") @PositiveOrZero int pageNumber,
            @RequestParam(defaultValue = "10") @Positive @Max(100) int pageSize,
            @RequestParam(defaultValue = "name") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(required = false) String name) {

        ModelAndView mv = new ModelAndView("caloteiros/list-caloteiros");

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortField).ascending());

        if (sortOrder.equalsIgnoreCase("desc")) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortField).descending());
        }

        CaloteiroPageDTO caloteirosPage;

        if (name == null || name.isBlank()) {
            caloteirosPage = caloteiroService.list(pageNumber, pageSize, sortField, sortOrder);
        } else {
            caloteirosPage = caloteiroService.searchByName(name, pageable);
        }

        mv.addObject("caloteirosPage", caloteirosPage);
        mv.addObject("sortField", sortField);
        mv.addObject("sortOrder", sortOrder);
        mv.addObject("searchQuery", name);
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

        CaloteiroDTO updateCaloteiro = caloteiroService.findById(id);
        mv.setViewName("caloteiros/update-caloteiro");
        mv.addObject("updateCaloteiro", updateCaloteiro);

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
        caloteiroService.delete(id);
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