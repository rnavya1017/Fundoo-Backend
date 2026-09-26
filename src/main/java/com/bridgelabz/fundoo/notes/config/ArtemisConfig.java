package com.bridgelabz.fundoo.notes.config;

import org.apache.activemq.artemis.api.core.SimpleString;
import org.apache.activemq.artemis.core.settings.impl.AddressSettings;
import org.springframework.boot.artemis.autoconfigure.ArtemisConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ArtemisConfig {

    @Bean
    public ArtemisConfigurationCustomizer reminderArtemisCustomizer() {

        return configuration -> {

            AddressSettings settings = new AddressSettings();
            settings.setMaxDeliveryAttempts(3);
            settings.setRedeliveryDelay(2000);
            settings.setDeadLetterAddress(SimpleString.of("fundoo.reminder.dlq"));

            configuration.addAddressSetting("fundoo.reminder.queue", settings);
        };
    }
}