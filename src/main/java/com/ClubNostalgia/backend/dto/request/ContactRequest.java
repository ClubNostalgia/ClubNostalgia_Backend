package com.ClubNostalgia.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ContactRequest(
    @NotBlank(message = "El nombre es obligatorio")
    String nombre,
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no es válido")
    String email,
    
    @NotBlank(message = "El proyecto es obligatorio")
    String proyecto
) {}
