package com.savik.service.bookmaker;

import com.savik.model.BookmakerCoeff;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.savik.service.bookmaker.CoeffType.AWAY;
import static com.savik.service.bookmaker.CoeffType.HANDICAP;
import static com.savik.service.bookmaker.CoeffType.HOME;
import static com.savik.service.bookmaker.CoeffType.MATCH;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BookmakerCoeffTest {


    /*
     * Handicap block
     * */

    @DisplayName(" It's a regular handicap fork (with same type value)")
    @ParameterizedTest(name = "{index} => coeff1={0}, coeff2={1}")
    @CsvSource({
            "2., 2.1",
            "1.8, 2.26"
    })
    public void test1(Double coeff1, Double coeff2) {
        BookmakerCoeff original = BookmakerCoeff.of(-0.25, coeff1, MATCH, HOME, HANDICAP);
        BookmakerCoeff target = BookmakerCoeff.of(0.25, coeff2, MATCH, AWAY, HANDICAP);
        assertTrue(target.isForkCompatibleTypeInTypes(original));
    }


    @DisplayName(" It's not a handicap forks (with same type value)")
    @ParameterizedTest(name = "{index} => coeff1={0}, coeff2={1}")
    @CsvSource({
            "2., 2.",
            "2., 1.9",
            "1.5, 1.5",
            "1.8, 2.25"
    })
    public void test2(Double coeff1, Double coeff2) {
        BookmakerCoeff original = BookmakerCoeff.of(-0.25, coeff1, MATCH, HOME, HANDICAP);
        BookmakerCoeff target = BookmakerCoeff.of(0.25, coeff2, MATCH, AWAY, HANDICAP);
        assertFalse(target.isForkCompatibleTypeInTypes(original));
    }


}
