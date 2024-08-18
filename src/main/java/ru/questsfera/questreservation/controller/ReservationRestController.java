package ru.questsfera.questreservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.questsfera.questreservation.dto.ResFormDTO;
import ru.questsfera.questreservation.entity.Reservation;
import ru.questsfera.questreservation.processor.ReservationFactory;
import ru.questsfera.questreservation.service.reservation.ReservationService;

import java.security.Principal;

@RestController()
@RequestMapping("/reservation")
public class ReservationRestController {
    @Autowired
    private ReservationService reservationService;

    @GetMapping("/{id}")
    public ResponseEntity<ResFormDTO> getReserveById(@PathVariable("id") Long id, Principal principal) {
        Reservation reservation = reservationService.getReserveById(id);
        ResFormDTO resFormDTO = ReservationFactory.createResFormDTO(reservation, reservation.getClient());
        return ResponseEntity.ok(resFormDTO);
    }
}
