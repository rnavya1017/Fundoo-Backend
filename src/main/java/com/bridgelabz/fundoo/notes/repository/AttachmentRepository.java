package com.bridgelabz.fundoo.notes.repository;

import com.bridgelabz.fundoo.notes.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    List<Attachment> findAllByNoteIdAndNoteUserId(Long noteId, Long userId);

    Optional<Attachment> findByIdAndNoteUserId(Long id, Long userId);
}