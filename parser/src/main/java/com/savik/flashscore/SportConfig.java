package com.savik.flashscore;

import com.savik.SportType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@ToString
@Getter
@FieldDefaults(makeFinal=true, level=AccessLevel.PRIVATE)
public class SportConfig {

    SportType sportType;

    String flashscoreSportId;
}
