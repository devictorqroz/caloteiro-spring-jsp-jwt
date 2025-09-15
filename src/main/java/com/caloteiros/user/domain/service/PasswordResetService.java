package com.caloteiros.user.domain.service;

import com.caloteiros.shared.utils.EmailService;
import com.caloteiros.user.domain.entities.PasswordResetToken;
import com.caloteiros.user.domain.entities.User;
import com.caloteiros.user.domain.exceptions.PasswordResetTokenException;
import com.caloteiros.user.domain.exceptions.UserException;
import com.caloteiros.user.domain.repositories.PasswordResetTokenRepository;
import com.caloteiros.user.domain.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetService.class);

    public PasswordResetService(UserRepository userRepository, PasswordResetTokenRepository tokenRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional
    public String generateResetToken(String email) {

        logger.info("Iniciando geração de token de redefinição de senha para o email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.warn("Tentativa de gerar token para email não cadastrado: {}", email);
                    return new UserException("Usuário não encontrado com o Email: " + email);
                });

        PasswordResetToken token = new PasswordResetToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(LocalDateTime.now().plusHours(1));

        tokenRepository.save(token);

        logger.info("Token de redefinição gerado com sucesso para o usuário ID: {}", user.getId());
        return token.getToken();
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {

        logger.info("Iniciando processo de redefinição de senha via token.");

        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> {
                    logger.warn("Tentativa de redefinição de senha com token inválido");
                    return new PasswordResetTokenException("Token inválido");
                });

        if (resetToken.isExpired()) {
            logger.warn("Tentativa de redefinição de senha com token expirado para o usuário ID: {}", resetToken.getUser().getId());
            throw new PasswordResetTokenException("Token expirado!");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        logger.info("Senha redefinida com sucesso para o usuário ID: {}", user.getId());

        tokenRepository.delete(resetToken);

        logger.debug("Token de redefinição de senha para o usuário ID {} foi utilizado e excluído.", user.getId());
    }

    public void sendResetPasswordEmail(String email, String token) {
        logger.info("Preparando para enviar email de redefinição de senha para: {}", email);

        try {
            emailService.sendPasswordResetEmail(email, token);
            logger.info("Email de redefinição enviado com sucesso para: {}", email);
        } catch (Exception e) {
            logger.error("Falha ao enviar email de redefinição de senha para: {}", email, e);
            throw new PasswordResetTokenException("Falha ao enviar email de redefinição de senha para o email");
        }
    }
}
