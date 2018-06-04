package com.savik.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BookmakerUtils {

    public boolean isFork(Double value1, Double value2) {
        return (1 / value1 + 1 / value2) < 1;
    }
}
