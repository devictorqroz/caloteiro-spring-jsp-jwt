package com.caloteiros.user.domain.service;

import com.caloteiros.user.application.dto.DeleteUserDTO;
import com.caloteiros.user.application.dto.UpdateUserDTO;
import com.caloteiros.user.application.dto.UserDTO;
import com.caloteiros.user.application.mapper.UserMapper;
import com.caloteiros.user.domain.entities.User;
import com.caloteiros.user.domain.exceptions.PasswordException;
import com.caloteiros.user.domain.exceptions.UserException;
import com.caloteiros.user.domain.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Cacheable(value = "users", key = "#id")
    public UserDTO findById(Long id) {
        logger.info("Buscando usuário pelo ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Usuário com ID {} não encontrado.", id);
                    return new UserException("Usuário não encontrado com o ID " + id);
                });

        logger.info("Usuário com ID {} encontrado com sucesso.", id);
        return userMapper.toUserDTO(user);
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public void createUser(User user) {
        logger.info("Iniciando processo de criação para o usuário com email: {}", user.getEmail());
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            logger.warn("Tentativa de criar usuário com email já existente: {}", user.getEmail());
            throw new UserException("Email já registrado");
        }

        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        userRepository.save(user);

        logger.info("Usuário com email {} criado com sucesso. ID: {}", user.getEmail(), user.getId());
    }

    @CacheEvict(value = "users", key = "#id")
    @Transactional
    public void update(Long id, UpdateUserDTO updateUserDTO) {
        logger.info("Iniciando processo de atualização para o usuário ID: {}", id);

        logger.debug("Dados recebidos para atualização do usuário ID {}: {}", id, updateUserDTO);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Tentativa de atualizar usuário inexistente com ID: {}", id);
                    return new UserException("Usuário não encontrado com o ID: " + id);
                });

        user = userMapper.fromUpdateToEntity(updateUserDTO, user);

        if (updateUserDTO.newPassword() != null && !updateUserDTO.newPassword().isBlank()) {
            logger.info("Tentativa de atualização de senha para o usuário ID: {}", id);
            if (!updateUserDTO.newPassword().equals(updateUserDTO.confirmPassword())) {
                logger.warn("Falha na atualização do usuário ID {}: senhas não coincidem.", id);
                throw new PasswordException("Passwords não coincidem");
            }

            String hash = passwordEncoder.encode(updateUserDTO.newPassword());
            user.setPassword(hash);

            logger.info("Senha do usuário ID {} será atualizada.", id);
        }

        userRepository.save(user);

        logger.info("Usuário ID {} atualizado com sucesso.", id);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void delete(Long userId, DeleteUserDTO deleteUserDTO) {

        logger.info("Iniciando processo de exclusão para usuário ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("Tentativa de excluir usuário inexistente com ID: {}", userId);
                    return new UserException("Usuário não encontrado com o ID: " + userId);
                });

        if (!passwordEncoder.matches(deleteUserDTO.password(), user.getPassword())) {
            logger.warn("Falha na exclusão do usuário ID {}: senha inválida fornecida.", userId);
            throw new UserException("Password inválido!");
        }

        userRepository.delete(user);

        logger.info("Usuário ID {} excluído com sucesso.", userId);
    }
}
