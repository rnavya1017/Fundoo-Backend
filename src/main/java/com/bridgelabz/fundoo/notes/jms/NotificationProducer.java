package com.bridgelabz.fundoo.notes.jms;

import com.bridgelabz.fundoo.notes.entity.Reminder;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.core.JacksonException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationProducer {

    public static final String QUEUE_NAME = "fundoo.reminder.queue";

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    public NotificationProducer(JmsTemplate jmsTemplate, ObjectMapper objectMapper) {
        this.jmsTemplate = jmsTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendReminder(Reminder reminder) {

        ReminderMessage message = new ReminderMessage(
                reminder.getNote().getUser().getId(),
                reminder.getNote().getId(),
                reminder.getNote().getTitle(),
                reminder.getReminderTime()
        );

        try {
            String jsonMessage = objectMapper.writeValueAsString(message);

            jmsTemplate.convertAndSend(
                    QUEUE_NAME,
                    jsonMessage
            );

        }
        catch (JacksonException exception) {
            throw new RuntimeException("Failed to create JMS message", exception);
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReminderMessage {

        private Long userId;

        private Long noteId;

        private String title;

        private java.time.LocalDateTime reminderTime;
    }
}