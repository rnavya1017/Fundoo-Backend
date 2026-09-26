package com.bridgelabz.fundoo.notes.service;

import com.bridgelabz.fundoo.notes.dto.NoteRequestDTO;
import com.bridgelabz.fundoo.notes.dto.NoteResponseDTO;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface NoteService {

    NoteResponseDTO createNote(NoteRequestDTO requestDTO, String email);

    List<NoteResponseDTO> getAllNotes(String email);

    NoteResponseDTO getNoteById(Long id, String email);

    NoteResponseDTO updateNote(
            Long id,
            NoteRequestDTO requestDTO,
            String email
    );

    void deleteNote(Long id, String email);

    void pinNote(Long id, String email);

    void unpinNote(Long id, String email);

    void archiveNote(Long id, String email);

    void unarchiveNote(Long id, String email);

    void restoreNote(Long id, String email);

    void permanentDeleteNote(Long id, String email);

    List<NoteResponseDTO> searchNotes(String keyword, String email);

    List<NoteResponseDTO> getNotesByPinned(boolean pinned, String email);

    List<NoteResponseDTO> getNotesByArchived(boolean archived, String email);

    List<NoteResponseDTO> getNotesByTrashed(boolean trashed, String email);

    List<NoteResponseDTO> getNotesByColor(String color, String email);

    Page<NoteResponseDTO> getNotesWithPagination(String email, int page, int size);

    List<NoteResponseDTO> getNotesWithReminder(String email);

    void addLabelToNote(Long noteId, Long labelId, String email);

    void removeLabelFromNote(Long noteId, Long labelId, String email);

    List<NoteResponseDTO> getNotesByLabel(String labelName, String email);

    List<NoteResponseDTO> getNotesByDate(LocalDate date, String email);
}