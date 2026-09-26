package com.bridgelabz.fundoo.notes.service;

import com.bridgelabz.fundoo.notes.dto.ReminderRequestDTO;
import com.bridgelabz.fundoo.notes.dto.ReminderResponseDTO;

import java.util.List;

public interface ReminderService {

    ReminderResponseDTO createReminder(
            Long noteId,
            ReminderRequestDTO requestDTO,
            String email
    );

    List<ReminderResponseDTO> getAllReminders(String email);

    void deleteReminder(Long reminderId, String email);
}