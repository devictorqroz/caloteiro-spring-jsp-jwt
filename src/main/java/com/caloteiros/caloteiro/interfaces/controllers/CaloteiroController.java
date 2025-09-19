package com.caloteiros.caloteiro.interfaces.controllers;

import com.caloteiros.caloteiro.application.dto.CaloteiroPageDTO;
import com.caloteiros.caloteiro.application.dto.UpdateCaloteiroDTO;
import com.caloteiros.caloteiro.application.dto.CreateCaloteiroDTO;
import com.caloteiros.caloteiro.domain.services.CaloteiroService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
@RequestMapping("/caloteiros")
public class CaloteiroController {

    private final CaloteiroService caloteiroService;

    public CaloteiroController(CaloteiroService caloteiroService) {
        this.caloteiroService = caloteiroService;
    }

    @GetMapping
    public String findAll(
            @RequestParam(defaultValue = "0") @PositiveOrZero int pageNumber,
            @RequestParam(defaultValue = "10") @Positive @Max(100) int pageSize,
            @RequestParam(defaultValue = "name") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(required = false) String name,
            Model model) {

        Sort.Direction direction = "desc".equalsIgnoreCase(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortField));

        CaloteiroPageDTO caloteirosPage;

        if (name == null || name.isBlank()) {
            caloteirosPage = caloteiroService.listByUser(pageable.getPageNumber(), pageable.getPageSize(), sortField, sortOrder);
        } else {
            caloteirosPage = caloteiroService.searchByName(name, pageable);
        }

        model.addAttribute("caloteirosPage", caloteirosPage);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("searchQuery", name);
        return "caloteiros/list-caloteiros";
    }

    @GetMapping("/new")
    public String displayCaloteiroForm(Model model) {
        model.addAttribute("createCaloteiroDTO",
                new CreateCaloteiroDTO("", "", BigDecimal.ZERO, LocalDate.now()));
        return "caloteiros/new-caloteiro";
    }

    @PostMapping
    public String createCaloteiro(
            @Valid @ModelAttribute CreateCaloteiroDTO createCaloteiroDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "caloteiros/new-caloteiro";
        }

        caloteiroService.create(createCaloteiroDTO);

        redirectAttributes.addFlashAttribute("successMessage", "Caloteiro cadastrado com sucesso");
        return "redirect:/caloteiros";
    }

    @GetMapping("/{id}/edit")
    public String displayUpdateCaloteiroForm(@PathVariable Long id, Model model) {
        model.addAttribute("updateCaloteiroDTO", caloteiroService.findById(id));
        model.addAttribute("caloteiroId", id);
        return "caloteiros/update-caloteiro";
    }

    @PutMapping("/{id}")
    public String updateCaloteiro(
            @PathVariable Long id,
            @Valid @ModelAttribute("updateCaloteiroDTO") UpdateCaloteiroDTO updateCaloteiroDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("caloteiroId", id);
            return "caloteiros/update-caloteiro";
        }

        caloteiroService.update(id, updateCaloteiroDTO);

        redirectAttributes.addFlashAttribute("successMessage", "Caloteiro atualizado com sucesso");
        return "redirect:/caloteiros";
    }

    @DeleteMapping("/{id}")
    public String deleteCaloteiroById(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        caloteiroService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Caloteiro excluído com sucesso");
        return "redirect:/caloteiros";
    }
}