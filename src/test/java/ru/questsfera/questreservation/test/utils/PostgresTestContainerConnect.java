package ru.questsfera.questreservation.test.utils;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.questsfera.questreservation.config.Profile;

/**
 * Using with docker-engine
 */
@Testcontainers
@ActiveProfiles(Profile.POSTGRES_TESTCONTAINER)
public interface PostgresTestContainerConnect {

    @Container
    PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17.4");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
}
