package com.savik.service.bookmaker;

import com.savik.model.BookmakerCoeff;
import com.savik.model.BookmakerCoeff.CoeffTypeChain;
import com.savik.utils.BookmakerUtils;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.savik.service.bookmaker.CoeffType.AWAY;
import static com.savik.service.bookmaker.CoeffType.COMMON;
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
    private static Map<CoeffTypeChain, List<CoeffValueChecker>> acceptableCheckers = new HashMap<>();

    final static CoeffTypeChain MATCH_AWAY_HANDICAP = new CoeffTypeChain(MATCH, AWAY, HANDICAP);
    final static CoeffTypeChain MATCH_HOME_HANDICAP = new CoeffTypeChain(MATCH, HOME, HANDICAP);
    final static CoeffTypeChain FIRST_HALF_AWAY_HANDICAP = new CoeffTypeChain(FIRST_HALF, AWAY, HANDICAP);
    final static CoeffTypeChain FIRST_HALF_HOME_HANDICAP = new CoeffTypeChain(FIRST_HALF, HOME, HANDICAP);
    final static CoeffTypeChain SECOND_HALF_AWAY_HANDICAP = new CoeffTypeChain(SECOND_HALF, AWAY, HANDICAP);
    final static CoeffTypeChain SECOND_HALF_HOME_HANDICAP = new CoeffTypeChain(SECOND_HALF, HOME, HANDICAP);
    final static CoeffTypeChain MATCH_COMMON_TOTAL_OVER = new CoeffTypeChain(MATCH, COMMON, TOTAL, OVER);
    final static CoeffTypeChain MATCH_COMMON_TOTAL_UNDER = new CoeffTypeChain(MATCH, COMMON, TOTAL, UNDER);
    final static CoeffTypeChain MATCH_HOME_TOTAL_OVER = new CoeffTypeChain(MATCH, HOME, TOTAL, OVER);
    final static CoeffTypeChain MATCH_HOME_TOTAL_UNDER = new CoeffTypeChain(MATCH, HOME, TOTAL, UNDER);
    final static CoeffTypeChain MATCH_AWAY_TOTAL_OVER = new CoeffTypeChain(MATCH, AWAY, TOTAL, OVER);
    final static CoeffTypeChain MATCH_AWAY_TOTAL_UNDER = new CoeffTypeChain(MATCH, AWAY, TOTAL, UNDER);
    final static CoeffTypeChain FIRST_HALF_COMMON_TOTAL_OVER = new CoeffTypeChain(FIRST_HALF, COMMON, TOTAL, OVER);
    final static CoeffTypeChain FIRST_HALF_COMMON_TOTAL_UNDER = new CoeffTypeChain(FIRST_HALF, COMMON, TOTAL, UNDER);
    final static CoeffTypeChain FIRST_HALF_HOME_TOTAL_OVER = new CoeffTypeChain(FIRST_HALF, HOME, TOTAL, OVER);
    final static CoeffTypeChain FIRST_HALF_HOME_TOTAL_UNDER = new CoeffTypeChain(FIRST_HALF, HOME, TOTAL, UNDER);
    final static CoeffTypeChain FIRST_HALF_AWAY_TOTAL_OVER = new CoeffTypeChain(FIRST_HALF, AWAY, TOTAL, OVER);
    final static CoeffTypeChain FIRST_HALF_AWAY_TOTAL_UNDER = new CoeffTypeChain(FIRST_HALF, AWAY, TOTAL, UNDER);


    final static HandicapCoeffValueChecker HANDICAP_COEFF_VALUE_CHECKER = new HandicapCoeffValueChecker();
    final static TotalOverCoeffValueChecker TOTAL_OVER_COEFF_VALUE_CHECKER = new TotalOverCoeffValueChecker();
    final static TotalUnderCoeffValueChecker TOTAL_UNDER_COEFF_VALUE_CHECKER = new TotalUnderCoeffValueChecker();
    final static LayCoeffValueChecker LAY_COEFF_VALUE_CHECKER = createLayChecker();

    static LayCoeffValueChecker createLayChecker() {
        final LayCoeffValueChecker checker = new LayCoeffValueChecker();
        checker.setCheckers(
                Arrays.asList(HANDICAP_COEFF_VALUE_CHECKER, TOTAL_OVER_COEFF_VALUE_CHECKER, TOTAL_UNDER_COEFF_VALUE_CHECKER)
        );
        return checker;
    }

    static List<CoeffValueChecker> wrapCommonCheckers(CoeffValueChecker checker) {
        return Arrays.asList(LAY_COEFF_VALUE_CHECKER, checker);
    }

    static {

        acceptableCheckers.put(MATCH_AWAY_HANDICAP, wrapCommonCheckers(HANDICAP_COEFF_VALUE_CHECKER));
        acceptableCheckers.put(MATCH_HOME_HANDICAP, wrapCommonCheckers(HANDICAP_COEFF_VALUE_CHECKER));
        acceptableCheckers.put(FIRST_HALF_AWAY_HANDICAP, wrapCommonCheckers(HANDICAP_COEFF_VALUE_CHECKER));
        acceptableCheckers.put(FIRST_HALF_HOME_HANDICAP, wrapCommonCheckers(HANDICAP_COEFF_VALUE_CHECKER));
        acceptableCheckers.put(SECOND_HALF_AWAY_HANDICAP, wrapCommonCheckers(HANDICAP_COEFF_VALUE_CHECKER));
        acceptableCheckers.put(SECOND_HALF_HOME_HANDICAP, wrapCommonCheckers(HANDICAP_COEFF_VALUE_CHECKER));
        acceptableCheckers.put(MATCH_COMMON_TOTAL_OVER, wrapCommonCheckers(TOTAL_UNDER_COEFF_VALUE_CHECKER));
        acceptableCheckers.put(MATCH_COMMON_TOTAL_UNDER, wrapCommonCheckers(TOTAL_OVER_COEFF_VALUE_CHECKER));
        acceptableCheckers.put(MATCH_HOME_TOTAL_OVER, wrapCommonCheckers(TOTAL_UNDER_COEFF_VALUE_CHECKER));
        acceptableCheckers.put(MATCH_HOME_TOTAL_UNDER, wrapCommonCheckers(TOTAL_OVER_COEFF_VALUE_CHECKER));
        acceptableCheckers.put(MATCH_AWAY_TOTAL_OVER, wrapCommonCheckers(TOTAL_UNDER_COEFF_VALUE_CHECKER));
        acceptableCheckers.put(MATCH_AWAY_TOTAL_UNDER, wrapCommonCheckers(TOTAL_OVER_COEFF_VALUE_CHECKER));
        acceptableCheckers.put(FIRST_HALF_COMMON_TOTAL_OVER, wrapCommonCheckers(TOTAL_UNDER_COEFF_VALUE_CHECKER));
        acceptableCheckers.put(FIRST_HALF_COMMON_TOTAL_UNDER, wrapCommonCheckers(TOTAL_OVER_COEFF_VALUE_CHECKER));
        acceptableCheckers.put(FIRST_HALF_HOME_TOTAL_OVER, wrapCommonCheckers(TOTAL_UNDER_COEFF_VALUE_CHECKER));
        acceptableCheckers.put(FIRST_HALF_HOME_TOTAL_UNDER, wrapCommonCheckers(TOTAL_OVER_COEFF_VALUE_CHECKER));
        acceptableCheckers.put(FIRST_HALF_AWAY_TOTAL_OVER, wrapCommonCheckers(TOTAL_UNDER_COEFF_VALUE_CHECKER));
        acceptableCheckers.put(FIRST_HALF_AWAY_TOTAL_UNDER, wrapCommonCheckers(TOTAL_OVER_COEFF_VALUE_CHECKER));


        createMatching(
                Arrays.asList(
                        MATCH_AWAY_HANDICAP,
                        MATCH_HOME_HANDICAP
                )
        );

        createMatching(
                Arrays.asList(
                        FIRST_HALF_AWAY_HANDICAP,
                        FIRST_HALF_HOME_HANDICAP
                )
        );

        createMatching(
                Arrays.asList(
                        SECOND_HALF_AWAY_HANDICAP,
                        SECOND_HALF_HOME_HANDICAP
                )
        );

        createMatching(
                Arrays.asList(
                        MATCH_COMMON_TOTAL_OVER,
                        MATCH_COMMON_TOTAL_UNDER
                )
        );

        createMatching(
                Arrays.asList(
                        MATCH_COMMON_TOTAL_OVER,
                        FIRST_HALF_COMMON_TOTAL_UNDER
                )
        );

        createMatching(
                Arrays.asList(
                        MATCH_HOME_TOTAL_OVER,
                        MATCH_HOME_TOTAL_UNDER
                )
        );

        createMatching(
                Arrays.asList(
                        MATCH_HOME_TOTAL_OVER,
                        FIRST_HALF_HOME_TOTAL_UNDER
                )
        );

        createMatching(
                Arrays.asList(
                        MATCH_AWAY_TOTAL_OVER,
                        MATCH_AWAY_TOTAL_UNDER
                )
        );

        createMatching(
                Arrays.asList(
                        MATCH_AWAY_TOTAL_OVER,
                        FIRST_HALF_AWAY_TOTAL_UNDER
                )
        );

        createMatching(
                Arrays.asList(
                        FIRST_HALF_COMMON_TOTAL_OVER,
                        FIRST_HALF_COMMON_TOTAL_UNDER
                )
        );

        createMatching(
                Arrays.asList(
                        FIRST_HALF_HOME_TOTAL_OVER,
                        FIRST_HALF_HOME_TOTAL_UNDER
                )
        );

        createMatching(
                Arrays.asList(
                        FIRST_HALF_AWAY_TOTAL_OVER,
                        FIRST_HALF_AWAY_TOTAL_UNDER
                )
        );

    }

    static void createMatching(List<CoeffTypeChain> types) {
        for (CoeffTypeChain type : types) {
            ArrayList<CoeffTypeChain> typesCopy = new ArrayList<>(types);
            typesCopy.remove(type);
            final List<CoeffTypeChain> coeffTypeChains = acceptableTypes.getOrDefault(type, new ArrayList<>());
            coeffTypeChains.addAll(typesCopy);
            acceptableTypes.put(type, coeffTypeChains);
        }
    }

    public static boolean isAcceptable(BookmakerCoeff origin, BookmakerCoeff target) {
        if(origin.isSame(target) && BookmakerUtils.isBackLay(origin, target)) {
            return true;
        }
        List<CoeffTypeChain> types = acceptableTypes.get(origin.getTypeChain());
        if (CollectionUtils.isEmpty(types)) {
            throw new IllegalArgumentException("Origin acceptable types not found: " + origin);
        }
        return types.contains(target.getTypeChain());
    }

    public static boolean isBetCompatibleByValue(BookmakerCoeff origin, BookmakerCoeff target) {
        final List<CoeffValueChecker> coeffValueCheckers = acceptableCheckers.get(origin.getTypeChain());
        if (CollectionUtils.isEmpty(coeffValueCheckers)) {
            throw new IllegalArgumentException("Checkers for chain not found: " + origin.getTypeChain());
        }
        for (CoeffValueChecker coeffValueChecker : coeffValueCheckers) {
            if (coeffValueChecker.canCheck(target.getTypeChain()) && coeffValueChecker.isCompatible(origin, target)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isFork(BookmakerCoeff origin, BookmakerCoeff target) {
        final List<CoeffValueChecker> coeffValueCheckers = acceptableCheckers.get(origin.getTypeChain());
        if (CollectionUtils.isEmpty(coeffValueCheckers)) {
            throw new IllegalArgumentException("Checkers for chain not found: " + origin.getTypeChain());
        }
        for (CoeffValueChecker coeffValueChecker : coeffValueCheckers) {
            if (coeffValueChecker.canCheck(target.getTypeChain()) && coeffValueChecker.isFork(origin, target)) {
                return true;
            }
        }
        return false;
    }

}

interface CoeffValueChecker {
    boolean canCheck(CoeffTypeChain targetChain);

    boolean isCompatible(BookmakerCoeff original, BookmakerCoeff target);

    boolean isFork(BookmakerCoeff original, BookmakerCoeff target);
}

class LayCoeffValueChecker implements CoeffValueChecker {

    @Setter
    List<CoeffValueChecker> checkers;

    @Override
    public boolean canCheck(CoeffTypeChain targetChain) {
        return true;
    }

    @Override
    public boolean isCompatible(BookmakerCoeff original, BookmakerCoeff target) {
        return original.isSame(target) && BookmakerUtils.isBackLay(original, target);
    }

    @Override
    public boolean isFork(BookmakerCoeff original, BookmakerCoeff target) {
        for (CoeffValueChecker checker : checkers) {
            if (checker.canCheck(target.getTypeChain()) && checker.isFork(original, target)) {
                return true;
            }
        }
        return false;
    }


}

class HandicapCoeffValueChecker implements CoeffValueChecker {

    @Override
    public boolean canCheck(CoeffTypeChain targetChain) {
        return targetChain.getLastChild() == HANDICAP;
    }

    @Override
    public boolean isCompatible(BookmakerCoeff original, BookmakerCoeff target) {
        return !BookmakerUtils.isBackLay(original, target) && BookmakerUtils.isHandicapForkAcceptableTypes(original.getTypeValue(), target.getTypeValue());
    }

    @Override
    public boolean isFork(BookmakerCoeff original, BookmakerCoeff target) {
        return BookmakerUtils.isFork(original.getCoeffValue(), target.getCoeffValue());
    }
}

class TotalOverCoeffValueChecker implements CoeffValueChecker {

    @Override
    public boolean canCheck(CoeffTypeChain targetChain) {
        return targetChain.getLastChild() == OVER;
    }

    @Override
    public boolean isCompatible(BookmakerCoeff original, BookmakerCoeff target) {
        return !BookmakerUtils.isBackLay(original, target) && BookmakerUtils.isTotalForkAcceptableTypes(target.getTypeValue(), original.getTypeValue());
    }

    @Override
    public boolean isFork(BookmakerCoeff original, BookmakerCoeff target) {
        return BookmakerUtils.isFork(original.getCoeffValue(), target.getCoeffValue());
    }
}

class TotalUnderCoeffValueChecker implements CoeffValueChecker {

    @Override
    public boolean canCheck(CoeffTypeChain targetChain) {
        return targetChain.getLastChild() == UNDER;
    }

    @Override
    public boolean isCompatible(BookmakerCoeff original, BookmakerCoeff target) {
        return !BookmakerUtils.isBackLay(original, target) && BookmakerUtils.isTotalForkAcceptableTypes(original.getTypeValue(), target.getTypeValue());
    }

    @Override
    public boolean isFork(BookmakerCoeff original, BookmakerCoeff target) {
        return BookmakerUtils.isFork(original.getCoeffValue(), target.getCoeffValue());
    }
}
