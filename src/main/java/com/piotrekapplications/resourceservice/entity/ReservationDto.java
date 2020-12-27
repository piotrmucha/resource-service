package com.piotrekapplications.resourceservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class ReservationDto {
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime reservationUntil;

    private String resourceName;

    private String assignTo;

    private String remindMinutesBefore;
}
