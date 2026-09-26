package com.bridgelabz.fundoo.notes.repository;

import com.bridgelabz.fundoo.notes.entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    List<Reminder> findAllByNoteUserId(Long userId);

    Optional<Reminder> findByIdAndNoteUserId(Long id, Long userId);
}