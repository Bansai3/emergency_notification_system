package com.vfedotov.notification.service;

import com.vfedotov.notification.dao.entity.User;
import com.vfedotov.notification.dao.repository.UserRepository;
import com.vfedotov.notification.dto.RegisterUserDto;
import com.vfedotov.notification.dto.UpdateUserDto;
import com.vfedotov.notification.dto.UserDto;
import com.vfedotov.notification.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.*;

@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public void registerUser(RegisterUserDto registerUserDto) {
        checkUsersWithSameLogin(registerUserDto.login());
        User user = userMapper.fromRegisterUserDtoToUser(registerUserDto);
        userRepository.save(user);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(u -> userMapper.fromUserToUserDto(u)).toList();
    }

    public UserDto getUser(Long userId) {
        User user = getUserEntity(userId);
        return userMapper.fromUserToUserDto(user);
    }

    public void updateUser(UpdateUserDto updateUserDto) {
        User user = getUserEntity(updateUserDto.id());
        HashMap<String, String> map = updateUserDto.getNonEmptyValues();

        if (map.size() == 0) return;

        for (var field : map.entrySet()) {
            if (field.getKey().equals("name")) {
                user.setName(field.getValue());
            }
            if (field.getKey().equals("surname")) {
                user.setSurname(field.getValue());
            }
            if (field.getKey().equals("login")) {
                checkUsersWithSameLogin(field.getValue());
                user.setLogin(field.getValue());
            }
            if (field.getKey().equals("password")) {
                user.setPassword(field.getValue());
            }
        }
        userRepository.save(user);
    }

    public void removerUser(Long userId) {
        User user = getUserEntity(userId);
        userRepository.delete(user);
    }


    public void checkUsersWithSameLogin(String login) {
        Optional<User> user = userRepository.findUserByLogin(login);
        if (user.isPresent()) {
            throw new InvalidParameterException("User with login " + login + " already exists!");
        }
    }

    private User getUserEntity(String login) {
        return userRepository.findUserByLogin(login)
                .orElseThrow( () ->
                        new NoSuchElementException("User with login " + login + " does not exist!")
                );
    }

    private User getUserEntity(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow( () ->
                        new NoSuchElementException("User with id " + userId + " does not exist!")
                );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByLogin(username)
                .map(u -> new org.springframework.security.core.userdetails.User (
                        u.getLogin(),
                        u.getPassword(),
                        Collections.singleton(u.getRole())
                ))
                .orElseThrow(() ->
                        new UsernameNotFoundException("User with username " + username + " does not exist!"));
    }
}
