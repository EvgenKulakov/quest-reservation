package ru.questsfera.questreservation.service.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.questsfera.questreservation.model.entity.Client;
import ru.questsfera.questreservation.repository.jpa.ClientRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock ClientRepository clientRepository;
    @InjectMocks ClientService clientService;

    @Test
    void saveClient() {
        Client client = Mockito.mock(Client.class);
        clientService.saveClient(client);
        verify(clientRepository).save(client);
    }

    @Test
    void findById_success() {
        Client exceptedClient = Mockito.mock(Client.class);
        when(clientRepository.findById(anyInt())).thenReturn(Optional.of(exceptedClient));
        Client actualClient = clientService.findById(anyInt());

        assertThat(actualClient).isSameAs(exceptedClient);

        verify(clientRepository).findById(anyInt());
    }

    @Test
    void findById_returnNull() {
        when(clientRepository.findById(anyInt())).thenReturn(Optional.empty());
        Client client = clientService.findById(anyInt());

        assertThat(client).isNull();

        verify(clientRepository).findById(anyInt());
    }
}