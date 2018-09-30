package com.savik.utils;

import com.savik.model.BookmakerCoeff;
import com.savik.service.bookmaker.SideType;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import static java.math.BigDecimal.ONE;
import static java.math.MathContext.DECIMAL32;

@UtilityClass
public class BookmakerUtils {

    private final DecimalFormat f = new DecimalFormat("##.000");


    public boolean isFork(Double value1, Double value2) {
        return getForkPercentage(value1, value2).compareTo(ONE) < 0;
    }

    public BigDecimal getForkPercentage(Double value1, Double value2) {
        BigDecimal first = BigDecimal.valueOf(value1);
        BigDecimal second = BigDecimal.valueOf(value2);
        return (ONE.divide(first, DECIMAL32)).add((ONE.divide(second, DECIMAL32)));
    }

    public boolean isHandicapForkAcceptableTypes(Double type1, Double type2) {
        if (isPositive(type1) && isPositive(type2)) {
            return true;
        }
        if (isNegative(type1) && isNegative(type2)) {
            return false;
        }
        // type1 = -0.5 , type2 = +0.5, +0.75, +1.0, etc
        if (isNegative(type1)) {
            return type2 >= Math.abs(type1);
        }

        // type1 = +1.5, type2 = -1.5, -1.25, -1, etc.
        if (isPositive(type1)) {
            return type1 >= Math.abs(type2);
        }
        throw new IllegalArgumentException("values are strange:" + type1 + ", " + type2);

    }

    public boolean isTotalForkAcceptableTypes(Double totalOver, Double totalUnder) {
        if (isNegative(totalOver) || isNegative(totalUnder)) {
            throw new IllegalArgumentException("total should be positive. totalOver: " + totalOver + ", totalUnder:" + totalUnder);
        }
        if (totalOver > totalUnder) {
            return false;
        }
        return true;

    }

    public String formatFork(BookmakerCoeff first, BookmakerCoeff second) {
        return f.format(getForkPercentage(first.getCoeffValue(), second.getCoeffValue()));
    }

    public Double convertAsianBookmakerHandicap(Integer homeScore, Integer awayScore, Double asianHandicap, SideType sideType) {
        if (homeScore == null && awayScore == null) {
            return asianHandicap;
        }
        if (homeScore.equals(awayScore)) {
            return asianHandicap;
        }
        int delta = sideType == SideType.HOME ? homeScore - awayScore : awayScore - homeScore;
        if (delta > 0) {
            return BigDecimal.valueOf(asianHandicap).subtract(BigDecimal.valueOf(delta)).doubleValue();
        }
        return BigDecimal.valueOf(asianHandicap).add(BigDecimal.valueOf(-delta)).doubleValue();
    }

    public Double convertAsianBookmakerWinToHandicap(Integer homeScore, Integer awayScore, SideType sideType) {
        if (homeScore == null && awayScore == null) {
            return -0.5;
        }
        if (homeScore.equals(awayScore)) {
            return -0.5;
        }
        int delta = sideType == SideType.HOME ? awayScore - homeScore : homeScore - awayScore;
        return BigDecimal.valueOf(-0.5).subtract(BigDecimal.valueOf(delta)).doubleValue();
    }

    /*
    * like (-0.0/0.5)
    * */
    public Double parseSpecialHandicapString(String handicapTypeWithBraces, String delimeter) {
        String handicapType = handicapTypeWithBraces.replace("(", "").replace(")", "");
        final String[] values = handicapType.split(delimeter);
        final double value = new BigDecimal(values[0]).add(new BigDecimal(values[1])).divide(new BigDecimal(2)).doubleValue();
        return value;
    }

    public Double convertLayCoeff(Double layCoeff) {
        final Double value = new BigDecimal(layCoeff).divide(new BigDecimal(layCoeff).subtract(BigDecimal.ONE), DECIMAL32).doubleValue();
        return value;
    }

    private boolean isPositive(Double value) {
        return value >= 0;
    }

    private boolean isNegative(Double value) {
        return value < 0;
    }

}
