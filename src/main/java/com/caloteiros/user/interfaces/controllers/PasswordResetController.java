package com.caloteiros.user.interfaces.controllers;

import com.caloteiros.user.domain.service.PasswordResetService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/password")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @GetMapping("/forgot")
    public ModelAndView displayPasswordForgotPage() {
        return new ModelAndView("users/forgot");
    }

    @PostMapping("/forgot")
    public ModelAndView processForgotPassword(@RequestParam("email") String email, ModelAndView mv) {
        String token = passwordResetService.generateResetToken(email);

        passwordResetService.sendResetPasswordEmail(email, token);
        mv.setViewName("users/forgot");
        mv.addObject("message", "Um link de redefinição de senha foi enviado para seu email");
        return mv;
    }

    @GetMapping("/reset")
    public ModelAndView displayResetPasswordPage(@RequestParam("token") String token, ModelAndView mv) {
        mv.setViewName("users/reset");
        mv.addObject("token", token);
        return mv;
    }

    @PostMapping("/reset")
    public ModelAndView processResetPassword(
            @RequestParam("token") String token,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            ModelAndView mv) {
        mv.setViewName("users/reset");

        if (!password.equals(confirmPassword)) {
            mv.addObject("error", "Passwords não coincidem");
            return mv;
        }

        passwordResetService.resetPassword(token, password);
        mv.addObject("message", "Password redefinido com sucesso");

        return mv;
    }
}
