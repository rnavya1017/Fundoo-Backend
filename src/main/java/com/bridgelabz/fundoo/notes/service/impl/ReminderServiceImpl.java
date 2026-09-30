package com.bridgelabz.fundoo.notes.service.impl;

import com.bridgelabz.fundoo.notes.dto.ReminderRequestDTO;
import com.bridgelabz.fundoo.notes.dto.ReminderResponseDTO;
import com.bridgelabz.fundoo.notes.entity.Note;
import com.bridgelabz.fundoo.notes.entity.Reminder;
import com.bridgelabz.fundoo.notes.entity.User;
import com.bridgelabz.fundoo.notes.exception.NoteNotFoundException;
import com.bridgelabz.fundoo.notes.exception.ReminderNotFoundException;
import com.bridgelabz.fundoo.notes.exception.UserNotFoundException;
import com.bridgelabz.fundoo.notes.jms.NotificationProducer;
import com.bridgelabz.fundoo.notes.repository.NoteRepository;
import com.bridgelabz.fundoo.notes.repository.ReminderRepository;
import com.bridgelabz.fundoo.notes.repository.UserRepository;
import com.bridgelabz.fundoo.notes.service.ReminderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ReminderServiceImpl implements ReminderService {

    private final ReminderRepository reminderRepository;
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final NotificationProducer notificationProducer;

    public ReminderServiceImpl(ReminderRepository reminderRepository, NoteRepository noteRepository, UserRepository userRepository, NotificationProducer notificationProducer) {
        this.reminderRepository = reminderRepository;
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
        this.notificationProducer = notificationProducer;
    }

    @Override
    @Transactional
    public ReminderResponseDTO createReminder(Long noteId, ReminderRequestDTO requestDTO, String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Note note = noteRepository
                .findByIdAndUserId(noteId, user.getId())
                .orElseThrow(() -> new NoteNotFoundException("Note not found"));

        Reminder reminder = new Reminder();

        reminder.setNote(note);
        reminder.setReminderTime(requestDTO.getReminderTime());
        reminder.setStatus("PENDING");
        note.setReminderDate(requestDTO.getReminderTime());
        noteRepository.save(note);

        Reminder savedReminder = reminderRepository.save(reminder);

        log.info("Reminder created successfully for Note ID: " + noteId);

        notificationProducer.sendReminder(savedReminder);

        return convertToResponse(savedReminder);
    }

    @Override
    public List<ReminderResponseDTO> getAllReminders(String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return reminderRepository
                .findAllByNoteUserId(user.getId())
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteReminder(Long reminderId, String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Reminder reminder = reminderRepository
                .findByIdAndNoteUserId(reminderId, user.getId())
                .orElseThrow(() -> new ReminderNotFoundException("Reminder not found"));

        Note note=reminder.getNote();
        note.setReminderDate(null);
        noteRepository.save(note);
        reminderRepository.delete(reminder);
        log.info("Remainder deleted successfully for Note ID: {}",note.getId());
    }

    private ReminderResponseDTO convertToResponse(Reminder reminder) {
        return new ReminderResponseDTO(
                reminder.getId(),
                reminder.getNote().getId(),
                reminder.getNote().getTitle(),
                reminder.getReminderTime(),
                reminder.getStatus()
        );
    }
}
