package com.ClubNostalgia.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

import com.ClubNostalgia.backend.entity.Project.VideoType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectResponse {
    private UUID id;
    private String title;
    private String video;
    private VideoType videoType;
    private String synopsis;
    private String information;
    private String author;
}