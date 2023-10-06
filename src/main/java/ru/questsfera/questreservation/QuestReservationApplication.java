package ru.questsfera.questreservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.questsfera.questreservation.converter.*;

import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class QuestReservationApplication implements WebMvcConfigurer {
    private final StatusTypeConverter statusTypeConverter;

    @Autowired
    public QuestReservationApplication(StatusTypeConverter statusTypeConverter) {
        this.statusTypeConverter = statusTypeConverter;
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

        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setDateFormatter(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        registrar.registerFormatters(registry);
    }
}
