package com.savik.utils;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;
import static java.math.MathContext.DECIMAL32;

@UtilityClass
public class BookmakerUtils {

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

    private boolean isPositive(Double value) {
        return value >= 0;
    }

    private boolean isNegative(Double value) {
        return value < 0;
    }

}
