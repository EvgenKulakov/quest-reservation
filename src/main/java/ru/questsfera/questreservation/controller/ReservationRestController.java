package ru.questsfera.questreservation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.questsfera.questreservation.model.dto.ResFormDTO;
import ru.questsfera.questreservation.model.dto.ReservationWIthClient;
import ru.questsfera.questreservation.service.reservation.ReservationService;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationRestController {

    private final ReservationService reservationService;

    @GetMapping("/{id}")
    public ResponseEntity<ResFormDTO> getReserveById(@PathVariable("id") Long id, Principal principal) {
        ReservationWIthClient reservationWIthClient = reservationService.findReservationDtoById(id);
        ResFormDTO resFormDTO = ResFormDTO.fromReservationDto(reservationWIthClient);
        return ResponseEntity.ok(resFormDTO);
    }
}
