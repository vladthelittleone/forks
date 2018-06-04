package com.savik.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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
        assertTrue(BookmakerUtils.isForkAcceptableTypes(type1, type2));
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
        assertFalse(BookmakerUtils.isForkAcceptableTypes(type1, type2));
    }


}
