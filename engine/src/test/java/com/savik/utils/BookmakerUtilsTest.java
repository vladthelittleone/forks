package com.savik.utils;

import com.savik.service.bookmaker.SideType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.savik.utils.BookmakerUtils.convertAsianBookmakerHandicap;
import static com.savik.utils.BookmakerUtils.isHandicapForkAcceptableTypes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BookmakerUtilsTest {


    @DisplayName(" It's acceptable type values for handicap, totals")
    @ParameterizedTest(name = "{index} => type1={0}, type2={1}")
    @CsvSource({
            "0, 0",
            "0, +0.25",
            "0, +0.5",
            "+0.25, 0",
            "+0.5, 0",
            "+0.5, +0.5",
            "-0.25, +0.25",
            "-0.25, +0.5",
            "+0.25, -0.25",
            "+0.5, -0.25",
    })
    public void test1(Double type1, Double type2) {
        assertTrue(isHandicapForkAcceptableTypes(type1, type2));
    }

    @DisplayName(" It's not acceptable type values for handicap, totals")
    @ParameterizedTest(name = "{index} => type1={0}, type2={1}")
    @CsvSource({
            "-0.25, -0.25",
            "-0.25, 0",
            "0, -0.25",
            "+0.5, -0.75",
            "-0.75, +0.5",
    })
    public void test2(Double type1, Double type2) {
        assertFalse(isHandicapForkAcceptableTypes(type1, type2));
    }

    @DisplayName(" Convert asian bookmaker type of handicap to general ( asian books in live don't look at match score for handicaps ) for HOME")
    @ParameterizedTest(name = "{index} => homeScore={0}, awayScore={1}, handicap={2}, result={3}")
    @CsvSource({
            "0, 0, 0, 0",
            "0, 0, -1, -1",
            "3, 1, -0.75, -2.75",
            "2, 0, 0, -2.",
            "2, 0, 0.25, -1.75",
            "3, 3, 0, 0",
            "3, 3, 0.25, 0.25",
            "1, 0, 0, 1",
            "1, 0, -0.25, -1.25",
    })
    public void test3(Integer homeScore, Integer awayScore, Double handicap, Double result) {
        assertEquals(convertAsianBookmakerHandicap(homeScore, awayScore, handicap, SideType.HOME), result);
    }

    @DisplayName(" Convert asian bookmaker type of handicap to general ( asian books in live don't look at match score for handicaps ) for AWAY")
    @ParameterizedTest(name = "{index} => homeScore={0}, awayScore={1}, handicap={2}, result={3}")
    @CsvSource({
            "0, 0, 0, 0",
            "0, 0, -1, -1",
            "3, 1, +0.75, 2.75",
            "2, 0, 0, 2",
            "2, 0, -0.25, 1.75",
            "3, 4, 0, -1",
            "3, 4, -0.25, -1.25",
    })
    public void test4(Integer homeScore, Integer awayScore, Double handicap, Double result) {
        assertEquals(convertAsianBookmakerHandicap(homeScore, awayScore, handicap, SideType.AWAY), result);
    }


}
