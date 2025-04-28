package ru.questsfera.questreservation.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.questsfera.questreservation.dto.ReservationDTO;
import ru.questsfera.questreservation.entity.Reservation;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    @Mapping(source = "client.id", target = "clientId")
    Reservation toEntity(ReservationDTO reservationDTO);
}
