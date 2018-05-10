package com.savik.flashscore;

import com.savik.SportType;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class SportConfig {

    SportType sportType;

    String sportPrefix;
}
