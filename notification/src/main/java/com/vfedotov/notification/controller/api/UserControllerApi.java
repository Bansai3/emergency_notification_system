package com.vfedotov.notification.controller.api;

import com.vfedotov.notification.dto.RegisterUserDto;
import com.vfedotov.notification.dto.UpdateUserDto;
import com.vfedotov.notification.dto.UserDto;
import com.vfedotov.notification.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class UserControllerApi {

    @Autowired
    private UserService userService;

    @PostMapping("register")
    public ResponseEntity<String> registerUser(
            @ModelAttribute @Validated RegisterUserDto dto) {

        userService.registerUser(dto);
        return ResponseEntity.ok("User successfully registered!");
    }

    @GetMapping("user/{user_id}")
    public UserDto getUser(@PathVariable("user_id") Long userId) {
        return userService.getUser(userId);
    }

    @DeleteMapping("user/{user_id}")
    public void deleteUser(@PathVariable("user_id") Long userId) {
        userService.removerUser(userId);
    }

    @GetMapping("all_users")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PatchMapping("user")
    public void updateUser(@RequestBody @Validated UpdateUserDto updateUserDto) {
        userService.updateUser(updateUserDto);
    }
}
