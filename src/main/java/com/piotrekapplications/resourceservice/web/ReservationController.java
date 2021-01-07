package com.piotrekapplications.resourceservice.web;

import com.piotrekapplications.resourceservice.entity.*;
import com.piotrekapplications.resourceservice.repositories.ReservationRepository;
import com.piotrekapplications.resourceservice.repositories.ResourceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/reservation")
@CrossOrigin(origins = "*")
public class ReservationController {
    private final ReservationRepository reservationRepository;
    private final ResourceRepository resourceRepository;

    public ReservationController(ReservationRepository reservationRepository, ResourceRepository resourceRepository) {
        this.reservationRepository = reservationRepository;
        this.resourceRepository = resourceRepository;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createReservation(@RequestBody ReservationDto reservationDto) {
        var reservation = new Reservation();
        reservation.setReservationUntil(Timestamp.valueOf(reservationDto.getReservationUntil()));
        reservation.setAssignTo(reservationDto.getAssignTo());
        if (reservationDto.getRemindMinutesBefore() != null) {
            reservation.setRemindMinutesBefore(Duration.parse("PT" + reservationDto.getRemindMinutesBefore()));
        }
        Resource resource = resourceRepository.getResourceByName(reservationDto.getResourceName());
        reservation.setResource(resource);
        reservationRepository.save(reservation);
    }

    @GetMapping
    public List<ReservationReturn> getAllReservation() {
        return reservationRepository.findAll().stream()
                .map(e -> {
                    var reservationReturn = new ReservationReturn();
                    reservationReturn.setReservationUntil(e.getReservationUntil().toLocalDateTime());
                    reservationReturn.setRemindMinutesBefore(e.getRemindMinutesBefore());
                    reservationReturn.setAssignTo(e.getAssignTo());
                    reservationReturn.setResourceName(e.getResource().getResourceName());
                    return reservationReturn;
                }).collect(Collectors.toList());
    }

    @PutMapping
    public ResponseEntity<String> notificationWasSent(@RequestParam String resourceName, @RequestParam String ownerEmail) {
        List<Reservation> reservations = reservationRepository.findAll();

        List<Reservation> lists = reservations.stream()
                .filter(e -> e.getAssignTo().equals(ownerEmail) && e.getResource().getResourceName()
                        .equals(resourceName))
                .collect(Collectors.toList());
        if (lists.size() != 1) {
            return new ResponseEntity<>("not found", HttpStatus.NOT_FOUND);
        } else {
            Reservation reservation = lists.get(0);
            reservation.setRemindMinutesBefore(null);
            reservationRepository.save(reservation);
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }

    @GetMapping("/resource")
    public ResponseEntity<List<ReservationReturn>> getReservationForResource(@RequestParam String resourceName) {
        List<ReservationReturn> reservations = reservationRepository.findAll().stream()
                .filter(e -> e.getResource().getResourceName().equals(resourceName))
                .map(e -> {
                    var reservationReturn = new ReservationReturn();
                    reservationReturn.setReservationUntil(e.getReservationUntil().toLocalDateTime());
                    reservationReturn.setRemindMinutesBefore(e.getRemindMinutesBefore());
                    reservationReturn.setAssignTo(e.getAssignTo());
                    reservationReturn.setResourceName(e.getResource().getResourceName());
                    return reservationReturn;
                }).collect(Collectors.toList());
        if (reservations.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(reservations, HttpStatus.OK);
        }
    }

    @GetMapping("/resource/limit")
    public ResponseEntity<Long> howManyPeopleUseResource(@RequestParam String resourceName) {
        long limit = reservationRepository.findAll().stream()
                .filter(e -> e.getResource().getResourceName().equals(resourceName))
                .count();
        return new ResponseEntity<>(limit, HttpStatus.OK);
    }
}
