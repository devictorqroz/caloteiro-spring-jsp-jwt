package com.caloteiros.user.interfaces.controllers;

import com.caloteiros.user.application.dto.DeleteUserDTO;
import com.caloteiros.user.application.dto.UpdateUserDTO;
import com.caloteiros.user.application.dto.UserDTO;
import com.caloteiros.user.domain.entities.User;
import com.caloteiros.user.domain.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ModelAndView showProfile(ModelAndView mv, HttpSession session) {
        Long userId = (Long) session.getAttribute("loggedUserId");
        UserDTO updateUser = userService.findById(userId);
        mv.addObject("updateUser", updateUser);
        return mv;
    }

    @PutMapping("/profile")
    public ModelAndView updateProfile(
            @Valid @ModelAttribute UpdateUserDTO updateUserDTO,
            BindingResult bindingResult,
            HttpSession session) {

        ModelAndView mv = new ModelAndView();

        if (bindingResult.hasErrors()) {
            mv.setViewName("users/profile");
            return mv;
        }

        Long userId = (Long) session.getAttribute("loggedUserId");
        userService.update(userId, updateUserDTO);

        mv.setViewName("redirect:/users/confirmation-user");
        return mv;
    }

    @GetMapping("/delete")
    public ModelAndView displayDeleteProfileForm(HttpSession session) {
        var userId = session.getAttribute("loggedUserId");

        ModelAndView mv = new ModelAndView("users/confirm-delete");
        mv.addObject("userId", userId);

        return mv;
    }

    @PostMapping("/delete")
    public ModelAndView confirmDeleteUser(
            @RequestParam("userId") Long userId,
            @Valid @ModelAttribute DeleteUserDTO deleteUserDTO,
            BindingResult bindingResult) {

        ModelAndView mv = new ModelAndView();

        if (bindingResult.hasErrors()) {
            mv.setViewName("users/confirm-delete");
            return mv;
        }

        userService.delete(userId, deleteUserDTO);

        mv.setViewName("redirect:/logout");
        return mv;
    }

    @GetMapping("/confirmation-user")
    public ModelAndView confirmationUser() {
        return new ModelAndView("users/confirmation-user");
    }
}
