package com.caloteiros.user.application.mapper;

import com.caloteiros.user.application.dto.UpdateUserDTO;
import com.caloteiros.user.application.dto.UserDTO;
import com.caloteiros.user.domain.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toUserDTO(User user) {
        return new UserDTO(
                user.getUsername(),
                user.getEmail(),
                user.getPassword()
        );
    }

    public User fromUpdateToEntity(UpdateUserDTO dto, User user) {
        user.setUsername(dto.name());
        user.setEmail(dto.email());
        return user;
    }
}
