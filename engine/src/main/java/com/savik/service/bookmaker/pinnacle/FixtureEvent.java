package com.savik.service.bookmaker.pinnacle;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FixtureEvent {
    Integer id;
    Integer parentId;
    String home;
    String away;
    Integer liveStatus;
    String status;
    LocalDateTime starts;
}
