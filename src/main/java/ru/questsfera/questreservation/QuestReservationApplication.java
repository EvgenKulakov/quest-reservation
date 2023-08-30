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
import ru.questsfera.questreservation.converters.StatusTypeConverter;
import ru.questsfera.questreservation.converters.UserConverter;

@SpringBootApplication
public class QuestReservationApplication implements WebMvcConfigurer {
    private final StatusTypeConverter statusTypeConverter;
    private final QuestConverter questConverter;
    private final AdminConverter adminConverter;
    private final UserConverter userConverter;

    @Autowired
    public QuestReservationApplication(StatusTypeConverter statusTypeConverter,
                                       QuestConverter questConverter, AdminConverter adminConverter,
                                       UserConverter userConverter) {
        this.statusTypeConverter = statusTypeConverter;
        this.questConverter = questConverter;
        this.adminConverter = adminConverter;
        this.userConverter = userConverter;
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
        registry.addConverter(questConverter);
        registry.addConverter(adminConverter);
        registry.addConverter(userConverter);

        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.registerFormatters(registry);
    }
}
