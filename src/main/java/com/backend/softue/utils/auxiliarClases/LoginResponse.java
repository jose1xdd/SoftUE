package com.backend.softue.utils.auxiliarClases;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LoginResponse {
    @NotBlank(message = "")
    @NotNull(message = "")
    @Email(message = "")
    private String email;
    @NotNull(message = "")
    @NotBlank(message = "")
    private String password;
}
