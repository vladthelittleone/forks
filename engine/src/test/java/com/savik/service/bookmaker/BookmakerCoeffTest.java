package com.savik.service.bookmaker;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static com.savik.service.bookmaker.CoeffType.GUEST;
import static com.savik.service.bookmaker.CoeffType.HANDICAP;
import static com.savik.service.bookmaker.CoeffType.HOME;
import static com.savik.service.bookmaker.CoeffType.MATCH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class BookmakerCoeffTest {


    /*
    * Handicap block
    * */

    @Test
    public void testIsRegularHandicapFork() {
        BookmakerCoeff original = BookmakerCoeff.of(-0.25, 2., HANDICAP, HOME, MATCH);
        BookmakerCoeff target = BookmakerCoeff.of(0.25, 2.1, HANDICAP, GUEST, MATCH);
        assertTrue(target.isFork(original));
    }

    @Test
    public void testIsNotHandicapFork() {
        BookmakerCoeff original = BookmakerCoeff.of(-0.25, 2., HANDICAP, HOME, MATCH);
        BookmakerCoeff target = BookmakerCoeff.of(0.25, 2., HANDICAP, GUEST, MATCH);
        assertFalse(target.isFork(original));
    }
}
