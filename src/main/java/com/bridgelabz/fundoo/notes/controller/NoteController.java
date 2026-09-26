package com.bridgelabz.fundoo.notes.controller;

import com.bridgelabz.fundoo.notes.dto.NoteRequestDTO;
import com.bridgelabz.fundoo.notes.dto.NoteResponseDTO;
import com.bridgelabz.fundoo.notes.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity<NoteResponseDTO> createNote(@Valid @RequestBody NoteRequestDTO requestDTO, Authentication authentication) {
        String email = authentication.getName();
        NoteResponseDTO responseDTO = noteService.createNote(requestDTO, email);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<NoteResponseDTO>> getAllNotes(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(noteService.getAllNotes(email));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteResponseDTO> getNoteById(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(noteService.getNoteById(id, email));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteResponseDTO> updateNote(@PathVariable Long id, @RequestBody @Valid NoteRequestDTO requestDTO, Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(noteService.updateNote(id, requestDTO, email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        noteService.deleteNote(id, email);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/trash")
    public ResponseEntity<List<NoteResponseDTO>> getTrash(Authentication authentication) {
        String email = authentication.getName();

        return ResponseEntity.ok(noteService.getNotesByTrashed(true, email));
    }

    @PatchMapping("/{id}/pin")
    public ResponseEntity<Void> pinNote(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        noteService.pinNote(id, email);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/unpin")
    public ResponseEntity<Void> unpinNote(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        noteService.unpinNote(id, email);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<Void> archiveNote(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        noteService.archiveNote(id, email);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/unarchive")
    public ResponseEntity<Void> unarchiveNote(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        noteService.unarchiveNote(id, email);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<Void> restoreNote(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        noteService.restoreNote(id, email);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<Void> permanentDeleteNote(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        noteService.permanentDeleteNote(id, email);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<NoteResponseDTO>> searchNotes(@RequestParam String keyword, Authentication authentication) {
        String email = authentication.getName();

        return ResponseEntity.ok(noteService.searchNotes(keyword, email));
    }

    @GetMapping("/filter/pinned")
    public ResponseEntity<List<NoteResponseDTO>> getNotesByPinned(@RequestParam boolean pinned, Authentication authentication) {
        String email = authentication.getName();

        return ResponseEntity.ok(noteService.getNotesByPinned(pinned, email));
    }

    @GetMapping("/filter/archived")
    public ResponseEntity<List<NoteResponseDTO>> getNotesByArchived(@RequestParam boolean archived, Authentication authentication) {
        String email = authentication.getName();

        return ResponseEntity.ok(noteService.getNotesByArchived(archived, email));
    }

    @GetMapping("/filter/trashed")
    public ResponseEntity<List<NoteResponseDTO>> getNotesByTrashed(@RequestParam boolean trashed, Authentication authentication) {
        String email = authentication.getName();

        return ResponseEntity.ok(noteService.getNotesByTrashed(trashed, email));
    }

    @GetMapping("/filter/reminder")
    public ResponseEntity<List<NoteResponseDTO>> getNotesWithReminder(Authentication authentication) {
        return ResponseEntity.ok(
                noteService.getNotesWithReminder(authentication.getName())
        );
    }

    @GetMapping("/filter/date")
    public ResponseEntity<List<NoteResponseDTO>> getNotesByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, Authentication authentication) {
        return ResponseEntity.ok(
                noteService.getNotesByDate(date, authentication.getName())
        );
    }

    @GetMapping("/filter/color")
    public ResponseEntity<List<NoteResponseDTO>> getNotesByColor(@RequestParam String color, Authentication authentication) {
        String email = authentication.getName();

        return ResponseEntity.ok(noteService.getNotesByColor(color, email));
    }

    @GetMapping("/page")
    public ResponseEntity<Page<NoteResponseDTO>> getNotesWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Authentication authentication
    ) {
        String email = authentication.getName();

        return ResponseEntity.ok(noteService.getNotesWithPagination(email, page, size));
    }

    @PostMapping("{id}/labels")
    public ResponseEntity<String> addLabelToNotes(@PathVariable Long id, @RequestParam Long labelId, Authentication authentication){
        noteService.addLabelToNote(id, labelId, authentication.getName());

        return ResponseEntity.ok("Label added to note successfully");
    }

    @DeleteMapping("/{id}/labels/{labelId}")
    public ResponseEntity<Void> removeLabelFromNote(@PathVariable Long id, @PathVariable Long labelId, Authentication authentication) {
        noteService.removeLabelFromNote(id, labelId, authentication.getName());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter/label")
    public ResponseEntity<List<NoteResponseDTO>> getNotesByLabel(@RequestParam String label, Authentication authentication) {
        return ResponseEntity.ok(noteService.getNotesByLabel(label, authentication.getName()));
    }
}
