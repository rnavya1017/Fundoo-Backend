package com.bridgelabz.fundoo.notes.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "notes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String color;

    private boolean pinned;

    private boolean archived;

    private boolean trashed;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private LocalDateTime reminderDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany
    @JoinTable(
            name = "note_label",
            joinColumns = @JoinColumn(name = "note_id"),
            inverseJoinColumns = @JoinColumn(name = "label_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Label> labels = new HashSet<>();

    @OneToMany(mappedBy = "note", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reminder> reminders = new ArrayList<>();

    @OneToMany(
            mappedBy = "note",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Attachment> attachments = new ArrayList<>();
}