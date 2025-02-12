package com.vfedotov.notification.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.HashMap;

public record UpdateUserDto (
    @NotNull(message = "Id must not be null!")
    Long id,

    @NotEmpty(message = "New name must not be empty or null!")
    @Size(max = 20, min = 3, message = "Invalid length of new name!")
    String newName,

    @NotEmpty(message = "New surname must not be empty or null!")
    @Size(max = 20, min = 3, message = "Invalid length of new surname!")
    String newSurname,

    @NotEmpty(message = "New login must not be empty or null!")
    @Size(max = 30, min = 3, message = "Invalid length of new login!")
    String newLogin,

    @NotEmpty(message = "New password must not be empty or null!")
    @Size(max = 30, min = 3, message = "Invalid length of new password!")
    String newPassword
) {
    public HashMap<String, String> getNonEmptyValues() {
        HashMap<String, String> map = new HashMap<>();
        if (!(newName == null || newName.isEmpty())) {
            map.put("name", newName);
        }
        if (!(newSurname == null || newSurname.isEmpty())) {
            map.put("surname", newSurname);
        }
        if (!(newLogin == null || newLogin.isEmpty())) {
            map.put("login", newLogin);
        }
        if (!(newPassword == null || newPassword.isEmpty())) {
            map.put("password", newPassword);
        }
        return map;
    }
}
