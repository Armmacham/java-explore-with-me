package ru.practicum.ewmservice.user.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewUserRequest {
    @NotNull
    @NotBlank
    @Size(min = 2, max = 250)
    String name;
    @NotNull
    @NotBlank
    @Email
    @Size(min = 6, max = 254)
    String email;
}
