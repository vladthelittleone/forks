package com.savik.service.bookmaker;

import com.savik.model.BookmakerCoeff.CoeffTypeChain;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.savik.service.bookmaker.CoeffType.AWAY;
import static com.savik.service.bookmaker.CoeffType.FIRST_HALF;
import static com.savik.service.bookmaker.CoeffType.HANDICAP;
import static com.savik.service.bookmaker.CoeffType.HOME;
import static com.savik.service.bookmaker.CoeffType.MATCH;
import static com.savik.service.bookmaker.CoeffType.OVER;
import static com.savik.service.bookmaker.CoeffType.SECOND_HALF;
import static com.savik.service.bookmaker.CoeffType.TOTAL;
import static com.savik.service.bookmaker.CoeffType.UNDER;

public class BookmakerCoeffMapper {


    private static Map<CoeffTypeChain, List<CoeffTypeChain>> acceptableTypes = new HashMap<>();

    static {
/*        acceptableTypes.put(HOME, Arrays.asList(AWAY));
        acceptableTypes.put(AWAY, Arrays.asList(HOME));
        acceptableTypes.put(HANDICAP, Arrays.asList(HANDICAP));
        acceptableTypes.put(TOTAL, Arrays.asList(TOTAL));
        acceptableTypes.put(OVER, Arrays.asList(UNDER));
        acceptableTypes.put(UNDER, Arrays.asList(OVER));
        
        
       */

        createMatching(
                Arrays.asList(
                        new CoeffTypeChain(MATCH, AWAY, HANDICAP),
                        new CoeffTypeChain(MATCH, HOME, HANDICAP)
                )
        );

        createMatching(
                Arrays.asList(
                        new CoeffTypeChain(FIRST_HALF, AWAY, HANDICAP),
                        new CoeffTypeChain(FIRST_HALF, HOME, HANDICAP)
                )
        );

        createMatching(
                Arrays.asList(
                        new CoeffTypeChain(SECOND_HALF, AWAY, HANDICAP),
                        new CoeffTypeChain(SECOND_HALF, HOME, HANDICAP)
                )
        );

        createMatching(
                Arrays.asList(
                        new CoeffTypeChain(MATCH, TOTAL, OVER),
                        new CoeffTypeChain(MATCH, TOTAL, UNDER)
                )
        );

        createMatching(
                Arrays.asList(
                        new CoeffTypeChain(FIRST_HALF, TOTAL, OVER),
                        new CoeffTypeChain(FIRST_HALF, TOTAL, UNDER)
                )
        );
        
    }
    
    static void createMatching(List<CoeffTypeChain> types) {
        for (CoeffTypeChain type : types) {
            ArrayList<CoeffTypeChain> copy = new ArrayList<>(types);
            copy.remove(type);
            acceptableTypes.put(type, copy);
        }
    }

    public static boolean isAcceptable(CoeffTypeChain origin, CoeffTypeChain target) {
        List<CoeffTypeChain> types = acceptableTypes.get(origin);
        if (CollectionUtils.isEmpty(types)) {
            throw new IllegalArgumentException("Origin acceptable types not found: " + origin);
        }
        return types.contains(target);
    }

}
