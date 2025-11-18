package com.ClubNostalgia.backend.dto.request;

import com.ClubNostalgia.backend.entity.Project.VideoType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectRequest {

    @NotBlank(message = "El título no puede estar vacío")
    @Size(max = 100, message = "El título no puede tener más de 100 caracteres")
    private String title;

    @Size(max = 500, message = "La URL del video no puede tener más de 500 caracteres")
    private String video;

    @NotNull(message = "El tipo de video no puede estar vacío")
    private VideoType videoType;

    @NotBlank(message = "La sinopsis no puede estar vacía")
    private String synopsis;

    @NotBlank(message = "La información no puede estar vacía")
    private String information;

    @NotBlank(message = "El autor no puede estar vacío")
    @Size(max = 50, message = "El autor no puede tener más de 50 caracteres")
    private String author;

    private UUID categoryId;
}