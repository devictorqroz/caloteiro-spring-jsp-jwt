package com.caloteiros.user.interfaces.controllers;

import com.caloteiros.user.application.dto.DeleteUserDTO;
import com.caloteiros.user.application.dto.UpdateUserDTO;
import com.caloteiros.user.application.dto.UserDTO;
import com.caloteiros.user.domain.exceptions.PasswordException;
import com.caloteiros.user.domain.exceptions.UserException;
import com.caloteiros.user.domain.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String showProfile(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("loggedUserId");
        UserDTO currentUser = userService.findById(userId);

        UpdateUserDTO formObject = new UpdateUserDTO(
                currentUser.name(),
                currentUser.email(),
                "", "", ""
        );
        model.addAttribute("updateUserDTO", formObject);
        return "users/profile";
    }

    @PutMapping("/profile")
    public String updateProfile(
            @Valid @ModelAttribute("updateUserDTO") UpdateUserDTO updateUserDTO,
            BindingResult bindingResult,
            HttpSession session,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "users/profile";
        }

        Long userId = (Long) session.getAttribute("loggedUserId");

        try {
            userService.update(userId, updateUserDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Perfil atualizado com sucesso!");
            return "redirect:/users/profile";
        } catch (PasswordException | UserException e) {
            model.addAttribute("errors", e.getMessage());
            return "users/profile";
        }
    }

    @GetMapping("/delete")
    public String displayDeleteProfileForm(HttpSession session, Model model) {
        var userId = session.getAttribute("loggedUserId");
        model.addAttribute("userId", userId);
        model.addAttribute("deleteUserDTO", new DeleteUserDTO(""));
        return "users/confirm-delete";
    }

    @PostMapping("/delete")
    public String confirmDeleteUser(
            @RequestParam("userId") Long userId,
            @Valid @ModelAttribute DeleteUserDTO deleteUserDTO,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("userId", userId);
            return "users/confirm-delete";
        }

        try {
            userService.delete(userId, deleteUserDTO);
        } catch (UserException e) {
            model.addAttribute("userId", userId);
            model.addAttribute("errors", e.getMessage());
            return "users/confirm-delete";
        }

        return "redirect:/auth/logout";
    }

    @GetMapping("/confirmation-user")
    public String confirmationUser() {
        return "users/confirmation-user";
    }
}
