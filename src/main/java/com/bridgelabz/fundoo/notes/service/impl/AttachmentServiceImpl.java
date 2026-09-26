package com.bridgelabz.fundoo.notes.service.impl;

import com.bridgelabz.fundoo.notes.dto.AttachmentResponseDTO;
import com.bridgelabz.fundoo.notes.entity.Attachment;
import com.bridgelabz.fundoo.notes.entity.Note;
import com.bridgelabz.fundoo.notes.entity.User;
import com.bridgelabz.fundoo.notes.exception.AttachmentNotFoundException;
import com.bridgelabz.fundoo.notes.exception.NoteNotFoundException;
import com.bridgelabz.fundoo.notes.exception.UserNotFoundException;
import com.bridgelabz.fundoo.notes.repository.AttachmentRepository;
import com.bridgelabz.fundoo.notes.repository.NoteRepository;
import com.bridgelabz.fundoo.notes.repository.UserRepository;
import com.bridgelabz.fundoo.notes.service.AttachmentService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    private static final String UPLOAD_DIR = "uploads";

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    public AttachmentServiceImpl(AttachmentRepository attachmentRepository, NoteRepository noteRepository, UserRepository userRepository) {
        this.attachmentRepository = attachmentRepository;
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AttachmentResponseDTO uploadAttachment(Long noteId, MultipartFile file, String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Note note = noteRepository
                .findByIdAndUserId(noteId, user.getId())
                .orElseThrow(() -> new NoteNotFoundException("Note not found"));

        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size cannot exceed 5 MB");
        }

        String fileName = file.getOriginalFilename();

        String contentType;

        if (fileName.toLowerCase().endsWith(".pdf")) {
            contentType = "application/pdf";
        } else if (fileName.toLowerCase().endsWith(".jpg")
                || fileName.toLowerCase().endsWith(".jpeg")) {
            contentType = "image/jpeg";
        } else if (fileName.toLowerCase().endsWith(".png")) {
            contentType = "image/png";
        } else if (fileName.toLowerCase().endsWith(".doc")) {
            contentType = "application/msword";
        } else if (fileName.toLowerCase().endsWith(".docx")) {
            contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        } else {
            throw new IllegalArgumentException("File type is not allowed");
        }

        try {

            Path uploadPath = Paths.get(UPLOAD_DIR);

            Files.createDirectories(uploadPath);

            fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            Path filePath = uploadPath.resolve(fileName);

            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            Attachment attachment = new Attachment();

            attachment.setFileName(file.getOriginalFilename());
            attachment.setFileType(contentType);
            attachment.setFileSize(file.getSize());
            attachment.setFilePath(filePath.toString());
            attachment.setNote(note);
            attachment.setUploadedDate(LocalDateTime.now());

            Attachment saved = attachmentRepository.save(attachment);

            return mapToResponse(saved);

        }
        catch (IOException e) {
            throw new RuntimeException("Failed to store attachment", e);
        }
    }

    @Override
    public List<AttachmentResponseDTO> getAttachments(Long noteId, String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        noteRepository.findByIdAndUserId(
                noteId,
                user.getId()
        ).orElseThrow(() ->
                new NoteNotFoundException("Note not found"));

        return attachmentRepository
                .findAllByNoteIdAndNoteUserId(noteId, user.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void deleteAttachment(Long attachmentId, String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Attachment attachment = attachmentRepository
                .findByIdAndNoteUserId(attachmentId, user.getId())
                .orElseThrow(() -> new AttachmentNotFoundException("Attachment not found"));

        try {
            Files.deleteIfExists(Paths.get(attachment.getFilePath()));
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to delete attachment file", e);
        }

        attachmentRepository.delete(attachment);
    }

    private AttachmentResponseDTO mapToResponse(Attachment attachment) {

        return new AttachmentResponseDTO(
                attachment.getId(),
                attachment.getFileName(),
                attachment.getFileType(),
                attachment.getFileSize(),
                attachment.getFilePath(),
                attachment.getNote().getId(),
                attachment.getUploadedDate()
        );
    }
}