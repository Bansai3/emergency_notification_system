package com.vfedotov.notification.mapper;

import com.vfedotov.notification.dao.entity.Role;
import com.vfedotov.notification.dao.entity.User;
import com.vfedotov.notification.dao.repository.UserRepository;
import com.vfedotov.notification.dto.RegisterUserDto;
import com.vfedotov.notification.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class UserMapper {

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public User fromRegisterUserDtoToUser(RegisterUserDto registerUserDto) {
        return new User(
                null,
                registerUserDto.name(),
                registerUserDto.surname(),
                registerUserDto.login(),
                passwordEncoder.encode(registerUserDto.password()),
                Role.USER,
                new HashSet<>()
        );
    }

    public UserDto fromUserToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getLogin(),
                user.getPassword(),
                user.getRole()
        );
    }
}
