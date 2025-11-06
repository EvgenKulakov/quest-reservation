package ru.questsfera.questreservation.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.questsfera.questreservation.mapper.SlotJsonMapper;
import ru.questsfera.questreservation.mapper.SlotListJsonMapper;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Bean
    public SlotJsonMapper slotJsonMapper() {
        return new SlotJsonMapper(objectMapper());
    }

    @Bean
    public SlotListJsonMapper slotListJsonMapper() {
        return new SlotListJsonMapper(objectMapper());
    }
}
