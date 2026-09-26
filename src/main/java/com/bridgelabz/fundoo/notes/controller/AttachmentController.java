package com.bridgelabz.fundoo.notes.controller;

import com.bridgelabz.fundoo.notes.dto.AttachmentResponseDTO;
import com.bridgelabz.fundoo.notes.service.AttachmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AttachmentController {

    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PostMapping("/notes/{id}/attachments")
    public ResponseEntity<AttachmentResponseDTO> uploadAttachment(@PathVariable Long id, @RequestParam("file") MultipartFile file, Authentication authentication) {
        return ResponseEntity.status(201)
                .body(attachmentService.uploadAttachment(id, file, authentication.getName())
        );
    }

    @GetMapping("/notes/{id}/attachments")
    public ResponseEntity<List<AttachmentResponseDTO>> getAttachments(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(
                attachmentService.getAttachments(id, authentication.getName())
        );
    }

    @DeleteMapping("/attachments/{id}")
    public ResponseEntity<Void> deleteAttachment(@PathVariable Long id, Authentication authentication) {
        attachmentService.deleteAttachment(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}