package com.caloteiros.shared.auth.controller;

import com.caloteiros.shared.auth.dto.LoginRequestDTO;
import com.caloteiros.shared.auth.dto.LoginResponseDTO;
import com.caloteiros.shared.security.jwt.TokenService;
import com.caloteiros.user.domain.entities.User;
import com.caloteiros.user.domain.repositories.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Operações de autenticação da API")
public class AuthRestController {

    private static final Logger logger = LoggerFactory.getLogger(AuthRestController.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthRestController(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequest) {
        logger.info("Tentativa de login via API para o email: {}", loginRequest.email());

        User user = userRepository.findByEmail(loginRequest.email())
                .orElse(null);

        if (user == null || !passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            logger.warn("Falha na autenticação via API para '{}': Credenciais inválidas.", loginRequest.email());
            return ResponseEntity.status(401).build();
        }

        String token = tokenService.generateToken(user);
        logger.info("Token gerado com sucesso para '{}' via API.", loginRequest.email());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}
