package com.caloteiros.shared.auth.controller;

import com.caloteiros.shared.auth.dto.LoginRequestDTO;
import com.caloteiros.shared.auth.dto.RegisterRequestDTO;
import com.caloteiros.shared.security.jwt.TokenService;
import com.caloteiros.user.domain.entities.User;
import com.caloteiros.user.domain.exceptions.UserAlreadyExistsException;
import com.caloteiros.user.domain.repositories.UserRepository;
import com.caloteiros.user.domain.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private final UserRepository repository;

    @Autowired
    private final UserService userService;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final TokenService tokenService;

    public AuthController(UserRepository repository, UserService userService, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.repository = repository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequestDTO("", ""));
        model.addAttribute("errors", new ArrayList<String>());
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequestDTO("", "", ""));
        model.addAttribute("errors", new ArrayList<String>());
        return "auth/register";
    }

    @PostMapping("/login")
    public String processLogin(
            @Valid @ModelAttribute LoginRequestDTO loginRequest,
            BindingResult bindingResult,
            Model model,
            HttpSession session) {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            model.addAttribute("errors", errors);
            return "auth/login";
        }

        User user = repository.findByEmail(loginRequest.email()).orElse(null);

        if (user == null || !passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            model.addAttribute("errors", Collections.singletonList("Credenciais inválidas"));
            return "auth/login";
        }

        String token = this.tokenService.generateToken(user);

        session.setAttribute("JWT_TOKEN", token);
        session.setAttribute("loggedUserName", user.getName());

        return "menu";
    }

    @PostMapping("/register")
    public String processRegister(
            @Valid @ModelAttribute RegisterRequestDTO registerRequest,
            BindingResult bindingResult,
            Model model,
            HttpSession session) {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            model.addAttribute("errors", errors);
            return "auth/register";
        }

        User user = registerRequest.toUser();

        try {
            userService.createUser(user);
        } catch (UserAlreadyExistsException e) {
            model.addAttribute("errors", Collections.singletonList(e.getMessage()));
            return "auth/register";
        }

        String token = this.tokenService.generateToken(user);
        session.setAttribute("JWT_TOKEN", token);
        return "redirect:/auth/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }
}
