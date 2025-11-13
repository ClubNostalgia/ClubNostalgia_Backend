package com.ClubNostalgia.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    @NotBlank(message = "Nombre no puede estar vacío")
    private String name;

    @NotBlank(message = "Email no puede estar vacio")
    @Email(message = "Email debe ser válido")
    private String email;

    private String role;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, max = 30, message = "La contraseña debe tener entre 8 y 30 carácteres")
    private String password;
    
}
