package com.savik.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BookmakerUtils {

    public boolean isFork(Double value1, Double value2) {
        return (1 / value1 + 1 / value2) < 1;
    }

    public boolean isForkAcceptableTypes(Double type1, Double type2) {
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

    private boolean isPositive(Double value) {
        return value >= 0;
    }

    private boolean isNegative(Double value) {
        return value < 0;
    }

}
