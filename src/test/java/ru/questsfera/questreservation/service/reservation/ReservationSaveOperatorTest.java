package ru.questsfera.questreservation.service.reservation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.questsfera.questreservation.service.client.ClientService;

@ExtendWith(MockitoExtension.class)
class ReservationSaveOperatorTest {

    @Mock ReservationService reservationService;
    @Mock ClientService clientService;
    @InjectMocks ReservationSaveOperator reservationSaveOperator;

    @Test
    void saveReservation() {
    }

    @Test
    void saveBlockReservation() {
    }

    private String getSLotJson() {
        return """
                {
                  "companyId" : 1,
                  "questId" : 1,
                  "questName" : "Quest One",
                  "reservationId" : 3,
                  "date" : "22-04-2025 (вторник)",
                  "time" : "17:00",
                  "price" : 3000,
                  "statuses" : [ {
                    "id" : 1,
                    "type" : "NEW_RESERVE",
                    "text" : "Новый"
                  }, {
                    "id" : 2,
                    "type" : "CANCEL",
                    "text" : "Отменён"
                  }, {
                    "id" : 3,
                    "type" : "CONFIRMED",
                    "text" : "Подтверждён"
                  }, {
                    "id" : 4,
                    "type" : "NOT_COME",
                    "text" : "Не пришёл"
                  }, {
                    "id" : 5,
                    "type" : "COMPLETED",
                    "text" : "Завершён"
                  } ],
                  "statusType" : "NOT_COME",
                  "minPersons" : 1,
                  "maxPersons" : 6
                }""";
    }
}