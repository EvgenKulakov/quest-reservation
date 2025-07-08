package ru.questsfera.questreservation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.questsfera.questreservation.model.dto.ReservationWithClient;
import ru.questsfera.questreservation.model.entity.Reservation;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(source = "client.id", target = "clientId")
    Reservation toEntity(ReservationWithClient reservationWIthClient);
}
