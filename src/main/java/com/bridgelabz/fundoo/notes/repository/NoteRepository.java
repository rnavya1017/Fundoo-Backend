package com.bridgelabz.fundoo.notes.repository;

import com.bridgelabz.fundoo.notes.entity.Note;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findAllByUserId(Long userId);

    List<Note> findActiveNotesByUserIdOrderByPinnedDescCreatedDateDesc(Long userId);

    Page<Note> findAllByUserId(Long userId, Pageable pageable);

    Optional<Note> findByIdAndUserId(Long noteId, Long userId);

    @Query("""
            SELECT n FROM Note n
            WHERE n.user.id = :userId
            AND (n.title ILIKE %:keyword% OR n.description ILIKE %:keyword%)
    """)
    // @Param("userId") -- Take the value from the Java variable userId and put it into :userId in the query.
    List<Note> searchNotes(@Param("userId") Long userId, @Param("keyword") String keyword);

    List<Note> findByUserIdAndPinned(Long userId, boolean pinned);

    List<Note> findByUserIdAndArchived(Long userId, boolean archive);

    List<Note> findByUserIdAndTrashed(Long userId, boolean trashed);

    List<Note> findByUserIdAndColor(Long userId, String color);

    List<Note> findByUserIdAndLabels_NameIgnoreCase(Long userId, String name);

    @Query("SELECT DISTINCT n FROM Note n JOIN n.reminders r WHERE n.user.id = :userId")
    List<Note> findNotesWithReminders(@Param("userId") Long userId);

    List<Note> findByUserIdAndCreatedDateBetween(Long userId, LocalDateTime start, LocalDateTime end);
}
