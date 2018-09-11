package com.savik.service.bookmaker.pinnacle;

import com.savik.model.BookmakerCoeff;
import com.savik.service.bookmaker.CoeffType;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.savik.model.BookmakerCoeff.of;
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

@Component
public class PinnacleParser {
    
    public Set<BookmakerCoeff> parse(OddsEvent odds) {
        Set<BookmakerCoeff> bookmakerCoeffs = new HashSet<>();
        List<OddsPeriod> periods = odds.getPeriods();
        for (OddsPeriod period : periods) {
            CoeffType partType = getPartByStatusCode(period.getNumber());
            final OddsMoneyline moneyline = period.getMoneyline();
           /* if (moneyline != null) {
                if (moneyline.getHome() != null) {
                    bookmakerCoeffs.add(of(-0.5, moneyline.getHome(), partType, HOME, HANDICAP));
                }
                if (moneyline.getAway() != null) {
                    bookmakerCoeffs.add(of(-0.5, moneyline.getAway(), partType, AWAY, HANDICAP));
                }
            }*/
            List<OddsSpread> spreads = period.getSpreads();
            if (spreads != null) {
                for (OddsSpread spread : spreads) {
                    bookmakerCoeffs.add(of(spread.getHdp(), spread.getHome(), partType, HOME, HANDICAP));
                    bookmakerCoeffs.add(of(-spread.getHdp(), spread.getAway(), partType, AWAY, HANDICAP));
                }
            }
            List<OddsTotal> totals = period.getTotals();
            if (totals != null) {
                for (OddsTotal total : totals) {
                    bookmakerCoeffs.add(of(total.getPoints(), total.getOver(), partType, COMMON, TOTAL, OVER));
                    bookmakerCoeffs.add(of(total.getPoints(), total.getUnder(), partType, COMMON, TOTAL, UNDER));
                }
            }
            OddsTeamTotalBlock teamTotal = period.getTeamTotal();
            if (teamTotal != null) {
                OddsTeamTotal home = teamTotal.getHome();
                if (home != null) {
                    bookmakerCoeffs.add(of(home.getPoints(), home.getOver(), partType, HOME, TOTAL, OVER));
                    bookmakerCoeffs.add(of(home.getPoints(), home.getUnder(), partType, HOME, TOTAL, UNDER));
                }
                OddsTeamTotal away = teamTotal.getAway();
                if (home != null) {
                    bookmakerCoeffs.add(of(away.getPoints(), away.getOver(), partType, AWAY, TOTAL, OVER));
                    bookmakerCoeffs.add(of(away.getPoints(), away.getUnder(), partType, AWAY, TOTAL, UNDER));
                }
            }
        }
        return bookmakerCoeffs;
    }

    private CoeffType getPartByStatusCode(int number) {
        switch (number) {
            case 0:
                return MATCH;
            case 1:
                return FIRST_HALF;
            case 2:
                return SECOND_HALF;
        }
        throw new PinnacleException("number is invalid, should be 1,2,3. number: " + number);
    }
}
