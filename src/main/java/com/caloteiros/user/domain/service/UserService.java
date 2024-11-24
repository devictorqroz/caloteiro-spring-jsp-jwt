package com.caloteiros.user.domain.service;

import com.caloteiros.user.application.dto.UpdateUserDTO;
import com.caloteiros.user.application.dto.UserDTO;
import com.caloteiros.user.application.mapper.UserMapper;
import com.caloteiros.user.domain.entities.User;
import com.caloteiros.user.domain.exceptions.PasswordException;
import com.caloteiros.user.domain.exceptions.UserException;
import com.caloteiros.user.domain.repositories.UserRepository;
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

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public void createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserException("Email já registrado");
        }

        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        userRepository.save(user);
    }

    @Cacheable(value = "users", key = "#id")
    public UserDTO findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toUserDTO)
                .orElseThrow(() -> new UserException("Usuário não encontrado com o ID " + id));
    }

    @CacheEvict(value = "users", key = "#id")
    @Transactional
    public void update(Long id, UpdateUserDTO updateUserDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserException("Usuário não encontrado com o ID: " + id));
        user = userMapper.fromUpdateToEntity(updateUserDTO, user);

        if (updateUserDTO.newPassword() != null && !updateUserDTO.newPassword().isBlank()) {
            if (!updateUserDTO.newPassword().equals(updateUserDTO.confirmPassword())) {
                throw new PasswordException("Passwords não coincidem");
            }

            String hash = passwordEncoder.encode(updateUserDTO.newPassword());
            user.setPassword(hash);
            userRepository.save(user);
        }
    }
}
