package com.bridgelabz.fundoo.notes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteResponseDTO {

    private Long id;

    private String title;

    private String description;

    private String color;

    private boolean pinned;

    private boolean archived;

    private boolean trashed;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private LocalDateTime reminderDate;

    private Set<LabelResponseDTO> labels;
}