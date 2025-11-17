package com.ClubNostalgia.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "El título no puede estar vacío")
    @Column(nullable = false, unique = true)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String video;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VideoType videoType;

    @Column(columnDefinition = "TEXT")
    private String synopsis;

    @Column(columnDefinition = "TEXT")
    private String information;

    @Column(nullable = false)
    private String author;

    public enum VideoType {
        YOUTUBE,
        VIMEO,
        FILE
    }
}