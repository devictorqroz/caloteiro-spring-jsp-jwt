package com.caloteiros.user.interfaces.controllers;

import com.caloteiros.user.application.dto.ForgotPasswordDTO;
import com.caloteiros.user.application.dto.ResetPasswordDTO;
import com.caloteiros.user.domain.exceptions.PasswordResetTokenException;
import com.caloteiros.user.domain.exceptions.UserException;
import com.caloteiros.user.domain.service.PasswordResetService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/password")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @GetMapping("/forgot")
    public String displayPasswordForgotPage(Model model) {
        model.addAttribute("forgotPasswordDTO", new ForgotPasswordDTO(""));
        return "users/forgot";
    }

    @PostMapping("/forgot")
    public String processForgotPassword(
            @Valid @ModelAttribute("forgotPasswordDTO") ForgotPasswordDTO forgotPasswordDTO,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "users/forgot";
        }

        try {
            String token = passwordResetService.generateResetToken(forgotPasswordDTO.email());
            passwordResetService.sendResetPasswordEmail(forgotPasswordDTO.email(), token);
            model.addAttribute("message", "Se o email estiver cadastrado, um link de redefinição de senha foi enviado.");
        } catch (UserException e) {
            model.addAttribute("message", "Se o email estiver cadastrado, um link de redefinição de senha foi enviado.");
        }
        return "users/forgot";
    }

    @GetMapping("/reset")
    public String displayResetPasswordPage(@RequestParam("token") String token, Model model) {
        model.addAttribute("resetPasswordDTO", new ResetPasswordDTO(token, "", ""));
        return "users/reset";
    }

    @PostMapping("/reset")
    public String processResetPassword(
            @Valid @ModelAttribute("resetPasswordDTO") ResetPasswordDTO resetPasswordDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (!resetPasswordDTO.password().equals(resetPasswordDTO.confirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "password.mismatch", "As senhas não coincidem.");
        }

        if (bindingResult.hasErrors()) {
            return "users/reset";
        }

        try {
            passwordResetService.resetPassword(resetPasswordDTO.token(), resetPasswordDTO.password());
            redirectAttributes.addFlashAttribute("successMessage", "Senha redefinida com sucesso. Por favor, faça o login.");
            return "redirect:/auth/login";
        } catch (PasswordResetTokenException e) {
            model.addAttribute("error", e.getMessage());
            return "users/reset";
        }
    }
}
