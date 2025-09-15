package com.caloteiros.shared.auth.controller;

import com.caloteiros.shared.auth.dto.LoginRequestDTO;
import com.caloteiros.shared.auth.dto.RegisterRequestDTO;
import com.caloteiros.shared.security.jwt.TokenService;
import com.caloteiros.user.domain.entities.User;
import com.caloteiros.user.domain.exceptions.UserException;
import com.caloteiros.user.domain.repositories.UserRepository;
import com.caloteiros.user.domain.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

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

        logger.info("Tentativa de login para o email: {}", loginRequest.email());

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());

            logger.warn("Falha na validação dos dados de login para o email '{}': {}", loginRequest.email(), errors);
            model.addAttribute("errors", errors);
            return "auth/login";
        }

        User user = repository.findByEmail(loginRequest.email()).orElse(null);

        if (user == null || !passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            logger.warn("Falha na autenticação para o email '{}': Credenciais inválidas.", loginRequest.email());
            model.addAttribute("errors", Collections.singletonList("Credenciais inválidas"));
            return "auth/login";
        }

        String token = this.tokenService.generateToken(user);

        session.setAttribute("JWT_TOKEN", token);
        session.setAttribute("AUTHENTICATED_USER", user);
        session.setAttribute("loggedUserId", user.getId());
        session.setAttribute("loggedUserName", user.getUsername());

        logger.info("Usuário com email '{}' (ID: {}) autenticado com sucesso.", user.getEmail(), user.getId());

        return "home";
    }

    @PostMapping("/register")
    public String processRegister(
            @Valid @ModelAttribute RegisterRequestDTO registerRequest,
            BindingResult bindingResult,
            Model model,
            HttpSession session) {

        logger.info("Tentativa de registro para o email: {}", registerRequest.email());

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());

            logger.warn("Falha na validação dos dados de registro para o email '{}': {}", registerRequest.email(), errors);
            model.addAttribute("errors", errors);
            return "auth/register";
        }

        User user = registerRequest.toUser();

        try {
            userService.createUser(user);
        } catch (UserException e) {
            logger.warn("Falha ao registrar usuário com email '{}': {}", user.getEmail(), e.getMessage());
            model.addAttribute("errors", Collections.singletonList(e.getMessage()));
            return "auth/register";
        }

        String token = this.tokenService.generateToken(user);
        session.setAttribute("JWT_TOKEN", token);
        session.setAttribute("AUTHENTICATED_USER", user);
        return "redirect:/auth/login";
    }
}
