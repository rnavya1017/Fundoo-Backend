package com.bridgelabz.fundoo.notes.controller;

import com.bridgelabz.fundoo.notes.dto.ReminderRequestDTO;
import com.bridgelabz.fundoo.notes.dto.ReminderResponseDTO;
import com.bridgelabz.fundoo.notes.service.ReminderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class                                                               ReminderController {

    private final ReminderService reminderService;

    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @PostMapping("/notes/{id}/reminder")
    public ResponseEntity<ReminderResponseDTO> createReminder(@PathVariable Long id, @Valid @RequestBody ReminderRequestDTO requestDTO, Authentication authentication) {
        ReminderResponseDTO response = reminderService.createReminder(id, requestDTO, authentication.getName());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/reminders")
    public ResponseEntity<List<ReminderResponseDTO>> getAllReminders(Authentication authentication) {
        return ResponseEntity.ok(
                reminderService.getAllReminders(
                        authentication.getName()
                )
        );
    }

    @DeleteMapping("/reminders/{id}")
    public ResponseEntity<Void> deleteReminder(@PathVariable Long id, Authentication authentication) {
        reminderService.deleteReminder(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}