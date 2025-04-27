package ru.questsfera.questreservation.service.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.questsfera.questreservation.entity.Client;
import ru.questsfera.questreservation.repository.jpa.ClientRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock ClientRepository clientRepository;
    @InjectMocks ClientService clientService;

    @Test
    void saveClient() {
        Client client = getClient();
        clientService.saveClient(client);
        verify(clientRepository).save(client);
    }

    @Test
    void findById_success() {
        Client exceptedClient = getClient();
        when(clientRepository.findById(1)).thenReturn(Optional.of(exceptedClient));
        Client actualClient = clientService.findById(1);

        assertThat(actualClient).isSameAs(exceptedClient);

        verify(clientRepository).findById(1);
    }

    @Test
    void findById_returnNull() {
        when(clientRepository.findById(100)).thenReturn(Optional.empty());
        Client client = clientService.findById(100);

        assertThat(client).isNull();

        verify(clientRepository).findById(100);
    }

    private Client getClient() {
        return new Client(
                1,
                "TestName_client",
                "TestSurname_client",
                "+79995201511", "ee@email.com",
                null,
                null,
                1
        );
    }
}