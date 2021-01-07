package com.piotrekapplications.resourceservice.scheduler;

import com.piotrekapplications.resourceservice.entity.Reservation;
import com.piotrekapplications.resourceservice.repositories.ReservationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReservationScheduler {
    private final ReservationRepository reservationRepository;

    public ReservationScheduler(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Scheduled(fixedRate = 30000)
    public void checkReservations() {
        List<Reservation> reservations = reservationRepository.findAll();

        LocalDateTime now = LocalDateTime.now();

        List<Reservation> reservationsNotActive = reservations.stream()
                .filter(e -> e.getReservationUntil().before(Timestamp.valueOf(now)))
                .collect(Collectors.toList());
        for (Reservation reservation : reservationsNotActive) {
            reservationRepository.delete(reservation);
        }
    }
}
