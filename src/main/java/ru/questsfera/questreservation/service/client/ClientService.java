package ru.questsfera.questreservation.service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.questsfera.questreservation.entity.Client;
import ru.questsfera.questreservation.repository.ClientRepository;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Transactional()
    public void saveClient(Client client) {
        clientRepository.save(client);
    }
}
