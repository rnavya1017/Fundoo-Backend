package com.bridgelabz.fundoo.notes.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationConsumer {

    @JmsListener(destination = NotificationProducer.QUEUE_NAME)
    public void processNotification(String message) {

        try {
            log.info("Reminder notification processed: {}", message);
        }
        catch (Exception e) {
                log.error("JMS notification processing failed: {}", e.getMessage());

                throw e;
        }
    }
}