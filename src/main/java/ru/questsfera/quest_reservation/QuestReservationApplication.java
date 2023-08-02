package ru.questsfera.quest_reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.questsfera.quest_reservation.processor.StatusConverter;
import ru.questsfera.quest_reservation.processor.StatusTypeConverter;

@SpringBootApplication
public class QuestReservationApplication implements WebMvcConfigurer {
    private final StatusTypeConverter statusTypeConverter;
    private final StatusConverter statusConverter;

    @Autowired
    public QuestReservationApplication(StatusTypeConverter statusTypeConverter, StatusConverter statusConverter) {
        this.statusTypeConverter = statusTypeConverter;
        this.statusConverter = statusConverter;
    }

    public static void main(String[] args) {
        SpringApplication.run(QuestReservationApplication.class, args);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(statusTypeConverter);
        registry.addConverter(statusConverter);
    }
}
