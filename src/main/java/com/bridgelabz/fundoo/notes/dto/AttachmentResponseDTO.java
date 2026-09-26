package com.bridgelabz.fundoo.notes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentResponseDTO {
    private Long id;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private String filePath;
    private Long noteId;
    private LocalDateTime uploadedDate;
}