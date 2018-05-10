package com.savik.flashscore;

import com.savik.SportType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class SportConfig {

    SportType sportType;

    String flashscoreSportId;
}
