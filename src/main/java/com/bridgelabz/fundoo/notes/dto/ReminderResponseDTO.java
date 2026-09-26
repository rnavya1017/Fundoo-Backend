package com.bridgelabz.fundoo.notes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReminderResponseDTO {

    private Long id;

    private Long noteId;

    private String noteTitle;

    private LocalDateTime reminderTime;

    private String status;
}