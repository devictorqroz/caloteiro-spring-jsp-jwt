package com.caloteiros.shared.config;

import com.caloteiros.user.domain.entities.User;
import com.caloteiros.user.domain.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        String demoEmail = "demo@caloteiros.com";

        userRepository.findByEmail(demoEmail).ifPresent(user -> {
            logger.warn("Deletando usuário de demonstração antigo");
            userRepository.delete(user);
        });

        logger.info("Criando usuário de demonstração para a API.");

        User demoUser = new User();
        demoUser.setUsername("Usuário Demo");
        demoUser.setEmail(demoEmail);
        demoUser.setPassword(passwordEncoder.encode("password"));

        userRepository.save(demoUser);
        logger.info(">>> Usuário de demonstração da API criado com sucesso! <<<");
        logger.info(">>> Email: {}", demoEmail);
        logger.info(">>> Senha: password");

    }
}