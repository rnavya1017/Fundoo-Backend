package com.bridgelabz.fundoo.notes.service;

import com.bridgelabz.fundoo.notes.dto.AttachmentResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AttachmentService {

    AttachmentResponseDTO uploadAttachment(Long noteId, MultipartFile file, String email);

    List<AttachmentResponseDTO> getAttachments(Long noteId, String email);

    void deleteAttachment(Long attachmentId, String email);
}