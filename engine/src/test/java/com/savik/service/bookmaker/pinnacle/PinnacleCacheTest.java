package com.savik.service.bookmaker.pinnacle;

import com.savik.domain.SportType;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class PinnacleCacheTest {

    PinnacleCache cache = new PinnacleCache();

    @DisplayName("Test cache update")
    @Test
    public void test() {
        OddsResponse expired = OddsResponse
                .builder()
                .leagues(Arrays.asList(
                        OddsLeague.builder()
                                .id(1)
                                .events(Arrays.asList(
                                        OddsEvent.builder()
                                                .id(11)
                                                .periods(Arrays.asList(
                                                        OddsPeriod.builder()
                                                                .number(0)
                                                                .totals(Arrays.asList(
                                                                        OddsTotal.builder().build()
                                                                ))
                                                                .build()

                                                ))
                                                .build()
                                ))
                                .build()
                ))
                .build();

        OddsResponse delta = OddsResponse
                .builder()
                .leagues(Arrays.asList(
                        OddsLeague.builder()
                                .id(1)
                                .events(Arrays.asList(
                                        OddsEvent.builder()
                                                .id(11)
                                                .periods(Arrays.asList(
                                                        OddsPeriod.builder()
                                                                .number(0)
                                                                .totals(Arrays.asList(
                                                                        OddsTotal.builder().build(),
                                                                        OddsTotal.builder().build(),
                                                                        OddsTotal.builder().build()
                                                                ))
                                                                .build(),
                                                        OddsPeriod.builder().number(1).build()

                                                ))
                                                .build(),
                                        OddsEvent.builder().id(12).build()
                                ))
                                .build(),
                        OddsLeague.builder().id(2).build()
                ))
                .build();

        cache.putOdds(SportType.FOOTBALL, expired);
        cache.updateOdds(SportType.FOOTBALL, delta);
        final OddsResponse updatedOdds = cache.getOdds(SportType.FOOTBALL);
        // стало 2 лиги с id = 1,2
        assertEquals(2, updatedOdds.getLeagues().size());
        assertEquals(Arrays.asList(1, 2), updatedOdds.getLeagues().stream().map(OddsLeague::getId).collect(Collectors.toList()));

        // у лиги с id=1, стало 2 events с id=11,12
        assertEquals(2, updatedOdds.findLeague(1).get().getEvents().size());
        assertEquals(Arrays.asList(11, 12), updatedOdds.findLeague(1).get().getEvents().stream().map(OddsEvent::getId).collect(Collectors.toList()));

        // у event с id=11, стало 2 periods с number=0,1
        assertEquals(2, updatedOdds.findLeague(1).get().findEvent(11).get().getPeriods().size());
        assertEquals(Arrays.asList(0, 1), updatedOdds.findLeague(1).get().findEvent(11).get().getPeriods().stream().map(OddsPeriod::getNumber).collect(Collectors.toList()));

        // у event с id=11,  period с number=0 стало 3 totals
        assertEquals(3, updatedOdds.findLeague(1).get().findEvent(11).get().findPeriod(0).get().getTotals().size());
    }
}
