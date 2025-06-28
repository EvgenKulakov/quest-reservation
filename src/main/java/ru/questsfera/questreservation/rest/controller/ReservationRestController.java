package ru.questsfera.questreservation.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.questsfera.questreservation.model.dto.ReservationForm;
import ru.questsfera.questreservation.model.dto.ReservationWIthClient;
import ru.questsfera.questreservation.service.reservation.ReservationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationRestController {

    private final ReservationService reservationService;

    @GetMapping("/{id}")
    public ResponseEntity<ReservationForm> getReserveById(@PathVariable("id") Long id) {
        ReservationWIthClient reservationWIthClient = reservationService.findReservationWIthClientById(id);
        ReservationForm reservationForm = ReservationForm.fromReservationWithClient(reservationWIthClient);
        return ResponseEntity.ok(reservationForm);
    }
}
