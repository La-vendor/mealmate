package com.lavendor.mealmate.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserDTO(
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be between {min} and {max} characters")
        String username,

        @NotBlank(message = "Password is required")
        @Size(min = 3, message = "Password must be at least {min} characters")
        String password,

        @NotBlank(message = "Confirm password is required")
        String confirmPassword) {
}
