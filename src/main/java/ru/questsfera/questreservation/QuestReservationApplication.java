package ru.questsfera.questreservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.questsfera.questreservation.converters.AdminConverter;
import ru.questsfera.questreservation.converters.QuestConverter;
import ru.questsfera.questreservation.converters.StatusConverter;
import ru.questsfera.questreservation.converters.StatusTypeConverter;

@SpringBootApplication
public class QuestReservationApplication implements WebMvcConfigurer {
    private final StatusTypeConverter statusTypeConverter;
    private final StatusConverter statusConverter;
    private final QuestConverter questConverter;
    private final AdminConverter adminConverter;

    @Autowired
    public QuestReservationApplication(StatusTypeConverter statusTypeConverter, StatusConverter statusConverter,
                                       QuestConverter questConverter, AdminConverter adminConverter) {
        this.statusTypeConverter = statusTypeConverter;
        this.statusConverter = statusConverter;
        this.questConverter = questConverter;
        this.adminConverter = adminConverter;

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
        registry.addConverter(questConverter);
        registry.addConverter(adminConverter);

        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.registerFormatters(registry);
    }
}
