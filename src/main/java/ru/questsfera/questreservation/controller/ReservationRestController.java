package ru.questsfera.questreservation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.questsfera.questreservation.dto.ResFormDTO;
import ru.questsfera.questreservation.dto.ReservationDTO;
import ru.questsfera.questreservation.processor.ReservationFactory;
import ru.questsfera.questreservation.service.reservation.ReservationService;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationRestController {

    private final ReservationService reservationService;
    private final ReservationFactory reservationFactory;

    @GetMapping("/{id}")
    public ResponseEntity<ResFormDTO> getReserveById(@PathVariable("id") Long id, Principal principal) {
        ReservationDTO reservationDTO = reservationService.findReservationDtoById(id);
        ResFormDTO resFormDTO = reservationFactory.createResFormDTO(reservationDTO);
        return ResponseEntity.ok(resFormDTO);
    }
}
