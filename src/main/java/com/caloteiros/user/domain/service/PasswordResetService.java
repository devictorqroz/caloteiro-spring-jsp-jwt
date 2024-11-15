package com.caloteiros.user.domain.service;

import com.caloteiros.shared.utils.EmailService;
import com.caloteiros.user.domain.entities.PasswordResetToken;
import com.caloteiros.user.domain.entities.User;
import com.caloteiros.user.domain.exceptions.PasswordResetTokenException;
import com.caloteiros.user.domain.exceptions.UserException;
import com.caloteiros.user.domain.repositories.PasswordResetTokenRepository;
import com.caloteiros.user.domain.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public PasswordResetService(UserRepository userRepository, PasswordResetTokenRepository tokenRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public String generateResetToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("Usuário não encontrado com o Email: " + email));

        PasswordResetToken token = new PasswordResetToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(LocalDateTime.now().plusHours(1));

        tokenRepository.save(token);
        return token.getToken();
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new PasswordResetTokenException("Token inválido"));

        if (resetToken.isExpired()) {
            throw new PasswordResetTokenException("Token expirado!");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        tokenRepository.delete(resetToken);
    }

    public void sendResetPasswordEmail(String email, String token) {
        emailService.sendPasswordResetEmail(email, token);
    }
}
