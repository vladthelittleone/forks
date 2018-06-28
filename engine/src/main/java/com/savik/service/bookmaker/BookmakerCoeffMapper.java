package com.savik.service.bookmaker;

import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.savik.service.bookmaker.CoeffType.AWAY;
import static com.savik.service.bookmaker.CoeffType.HANDICAP;
import static com.savik.service.bookmaker.CoeffType.HOME;
import static com.savik.service.bookmaker.CoeffType.OVER;
import static com.savik.service.bookmaker.CoeffType.TOTAL;
import static com.savik.service.bookmaker.CoeffType.UNDER;

public class BookmakerCoeffMapper {


    private static Map<CoeffType, List<CoeffType>> acceptableTypes = new HashMap<>();

    static {
        acceptableTypes.put(HOME, Arrays.asList(AWAY));
        acceptableTypes.put(AWAY, Arrays.asList(HOME));
        acceptableTypes.put(HANDICAP, Arrays.asList(HANDICAP));
        acceptableTypes.put(TOTAL, Arrays.asList(TOTAL));
        acceptableTypes.put(OVER, Arrays.asList(UNDER));
        acceptableTypes.put(UNDER, Arrays.asList(OVER));
    }

    public static boolean isAcceptable(CoeffType origin, CoeffType target) {
        List<CoeffType> types = acceptableTypes.get(origin);
        if (CollectionUtils.isEmpty(types)) {
            throw new IllegalArgumentException("Origin acceptable types not found: " + origin);
        }
        return types.contains(target);
    }

}
