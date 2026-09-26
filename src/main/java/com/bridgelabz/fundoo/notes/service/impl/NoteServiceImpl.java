package com.bridgelabz.fundoo.notes.service.impl;

import com.bridgelabz.fundoo.notes.dto.LabelResponseDTO;
import com.bridgelabz.fundoo.notes.dto.NoteRequestDTO;
import com.bridgelabz.fundoo.notes.dto.NoteResponseDTO;
import com.bridgelabz.fundoo.notes.entity.Label;
import com.bridgelabz.fundoo.notes.entity.Note;
import com.bridgelabz.fundoo.notes.entity.User;
import com.bridgelabz.fundoo.notes.exception.LabelNotFoundException;
import com.bridgelabz.fundoo.notes.exception.NoteNotFoundException;
import com.bridgelabz.fundoo.notes.exception.UserNotFoundException;
import com.bridgelabz.fundoo.notes.repository.LabelRepository;
import com.bridgelabz.fundoo.notes.repository.NoteRepository;
import com.bridgelabz.fundoo.notes.repository.UserRepository;
import com.bridgelabz.fundoo.notes.service.NoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final LabelRepository labelRepository;

    public NoteServiceImpl(NoteRepository noteRepository, UserRepository userRepository, LabelRepository labelRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
        this.labelRepository = labelRepository;
    }

    @Override
    public NoteResponseDTO createNote(NoteRequestDTO requestDTO, String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Note note = new Note();

        note.setTitle(requestDTO.getTitle());
        note.setDescription(requestDTO.getDescription());
        note.setColor(requestDTO.getColor());

        note.setPinned(false);
        note.setArchived(false);
        note.setTrashed(false);

        note.setCreatedDate(LocalDateTime.now());
        note.setUpdatedDate(LocalDateTime.now());

        note.setUser(user);

        Note savedNote = noteRepository.save(note);

        log.info("Note created successfully for user: {}", email);

        return convertToResponse(savedNote);
    }

    @Override
    public List<NoteResponseDTO> getAllNotes(String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return noteRepository
                .findActiveNotesByUserIdOrderByPinnedDescCreatedDateDesc(user.getId())
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public NoteResponseDTO getNoteById(Long id, String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Note note = noteRepository
                .findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new NoteNotFoundException("Note not found"));

        return convertToResponse(note);
    }

    @Override
    public NoteResponseDTO updateNote(Long id, NoteRequestDTO requestDTO, String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Note note = noteRepository
                .findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new NoteNotFoundException("Note not found"));

        note.setTitle(requestDTO.getTitle());
        note.setDescription(requestDTO.getDescription());
        note.setColor(requestDTO.getColor());
        note.setUpdatedDate(LocalDateTime.now());

        Note updatedNote = noteRepository.save(note);

        return convertToResponse(updatedNote);
    }

    @Override
    public void deleteNote(Long id, String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Note note = noteRepository
                .findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new NoteNotFoundException("Note not found"));

        note.setTrashed(true);
        note.setUpdatedDate(LocalDateTime.now());

        noteRepository.save(note);
    }

    @Override
    public void pinNote(Long id, String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Note note = noteRepository
                .findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new NoteNotFoundException("Note not found"));

        note.setPinned(true);
        note.setUpdatedDate(LocalDateTime.now());

        noteRepository.save(note);
    }

    @Override
    public void unpinNote(Long id, String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Note note = noteRepository
                .findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new NoteNotFoundException("Note not found"));

        note.setPinned(false);
        note.setUpdatedDate(LocalDateTime.now());

        noteRepository.save(note);
    }

    @Override
    public void archiveNote(Long id, String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Note note = noteRepository
                .findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new NoteNotFoundException("Note not found"));

        note.setArchived(true);
        note.setUpdatedDate(LocalDateTime.now());

        noteRepository.save(note);

        log.info("Note archived successfully. Note ID: {}", id);
    }

    @Override
    public void unarchiveNote(Long id, String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Note note = noteRepository
                .findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new NoteNotFoundException("Note not found"));

        note.setArchived(false);
        note.setUpdatedDate(LocalDateTime.now());

        noteRepository.save(note);
    }

    @Override
    public void restoreNote(Long id, String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Note note = noteRepository
                .findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new NoteNotFoundException("Note not found"));

        note.setTrashed(false);
        note.setUpdatedDate(LocalDateTime.now());

        noteRepository.save(note);
    }

    @Override
    public void permanentDeleteNote(Long id, String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Note note = noteRepository
                .findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new NoteNotFoundException("Note not found"));

        if(!note.isTrashed()){
            throw new NoteNotFoundException(("Note is not in trash"));
        }

        noteRepository.delete(note);
    }

    @Override
    public List<NoteResponseDTO> searchNotes(String keyword, String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return noteRepository
                .searchNotes(user.getId(), keyword)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public List<NoteResponseDTO> getNotesByPinned(boolean pinned, String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return noteRepository
                .findByUserIdAndPinned(user.getId(), pinned)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public List<NoteResponseDTO> getNotesByArchived(boolean archived, String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return noteRepository
                .findByUserIdAndArchived(user.getId(), archived)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public List<NoteResponseDTO> getNotesByTrashed(boolean trashed, String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return noteRepository
                .findByUserIdAndTrashed(user.getId(), trashed)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public List<NoteResponseDTO> getNotesByColor(String color, String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return noteRepository
                .findByUserIdAndColor(user.getId(), color)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public Page<NoteResponseDTO> getNotesWithPagination(String email, int page, int size) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Pageable pageable = PageRequest.of(page, size);

        return noteRepository
                .findAllByUserId(user.getId(), pageable)
                .map(this::convertToResponse);
    }

    @Transactional
    @Override
    public void addLabelToNote(Long noteId, Long labelId, String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Note note = noteRepository
                .findByIdAndUserId(noteId, user.getId())
                .orElseThrow(() -> new NoteNotFoundException("Note not found"));

        Label label = labelRepository
                .findByIdAndUserId(labelId, user.getId())
                .orElseThrow(() -> new LabelNotFoundException("Label not found"));

        note.getLabels().add(label);
    }

    @Transactional
    @Override
    public void removeLabelFromNote(Long noteId, Long labelId, String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Note note = noteRepository
                .findByIdAndUserId(noteId, user.getId())
                .orElseThrow(() -> new NoteNotFoundException("Note not found"));

        Label label = labelRepository
                .findByIdAndUserId(labelId, user.getId())
                .orElseThrow(() -> new LabelNotFoundException("Label not found"));

        note.getLabels().remove(label);

        noteRepository.save(note);
    }

    @Override
    public List<NoteResponseDTO> getNotesByLabel(String labelName, String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Note> notes = noteRepository.findByUserIdAndLabels_NameIgnoreCase(user.getId(), labelName);

        return notes.stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public List<NoteResponseDTO> getNotesWithReminder(String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return noteRepository
                .findNotesWithReminders(user.getId())
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public List<NoteResponseDTO> getNotesByDate(LocalDate date, String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        return noteRepository
                .findByUserIdAndCreatedDateBetween(user.getId(), start, end)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    private NoteResponseDTO convertToResponse(Note note) {

        NoteResponseDTO responseDTO = new NoteResponseDTO();

        responseDTO.setId(note.getId());
        responseDTO.setTitle(note.getTitle());
        responseDTO.setDescription(note.getDescription());
        responseDTO.setColor(note.getColor());

        responseDTO.setPinned(note.isPinned());
        responseDTO.setArchived(note.isArchived());
        responseDTO.setTrashed(note.isTrashed());

        responseDTO.setCreatedDate(note.getCreatedDate());
        responseDTO.setUpdatedDate(note.getUpdatedDate());
        responseDTO.setReminderDate(note.getReminderDate());

        Set<LabelResponseDTO> labels = note.getLabels()
                .stream()
                .map(label -> new LabelResponseDTO(label.getId(), label.getName()))
                .collect(Collectors.toSet());

        responseDTO.setLabels(labels);

        return responseDTO;
    }
}
