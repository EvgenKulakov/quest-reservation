package ru.questsfera.questreservation.service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.entity.Client;
import ru.questsfera.questreservation.repository.jpa.ClientRepository;

import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Transactional
    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    @Transactional(readOnly = true)
    public Client findById(Integer clientId) {
        Optional<Client> optionalClient = clientRepository.findById(clientId);
        return optionalClient.orElse(null);
    }
}
