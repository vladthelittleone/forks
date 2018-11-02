package com.savik.client;

import com.savik.domain.Match;
import com.savik.domain.Team;
import com.savik.filter.MatchFilter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.savik.domain.MatchStatus.PREMATCH;
import static com.savik.domain.SportType.FOOTBALL;

@Component
@FeignClient(value = "parser", fallback = ParserClientFallback.class)
public interface ParserClient {

    @RequestMapping(method = RequestMethod.GET, value = "/matches")
    List<Match> getMatches(@RequestParam("matchFilter") MatchFilter matchFilter);
}

@Component
class ParserClientFallback implements ParserClient {

    @Override
    public List<Match> getMatches(MatchFilter matchFilter) {
        List<Match> matches = new ArrayList<>();

        /*  lAYxY2n8, LITHUANIA_A */
        matches.add(
                Match.builder()
                        .flashscoreId("jR0XIU59")
                        .flashscoreLeagueId("lAYxY2n8")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-10-31T16:00"))
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("Atlantas").flashscoreId("0tSFgCp6").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Kauno Zalgiris").flashscoreId("0p17kQla").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("UHayIlLF")
                        .flashscoreLeagueId("lAYxY2n8")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-10-31T16:00"))
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("Suduva").flashscoreId("zT1dAfJo").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Trakai").flashscoreId("Cpqd5uFj").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("OvauH8zM")
                        .flashscoreLeagueId("lAYxY2n8")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-10-31T18:00"))
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("Zalgiris").flashscoreId("fuLOijFI").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Stumbras").flashscoreId("rX0awNf8").sportType(FOOTBALL).build())
                        .build()
        );
        /*  6oug4RRc, ITALY_B */
        matches.add(
                Match.builder()
                        .flashscoreId("U7dmkcvD")
                        .flashscoreLeagueId("6oug4RRc")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-10-31T18:00"))
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("Salernitana").flashscoreId("Mq05g4BP").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Livorno").flashscoreId("0zxhbdWT").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("MsguiJ90")
                        .flashscoreLeagueId("6oug4RRc")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-10-31T20:00"))
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("Lecce").flashscoreId("G8lYsMgU").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Crotone").flashscoreId("d8H0SxkJ").sportType(FOOTBALL).build())
                        .build()
        );
        /*  nXxWpLmT, SWEDEN_ALLSVENSKAN */
        matches.add(
                Match.builder()
                        .flashscoreId("vXdYXAyo")
                        .flashscoreLeagueId("nXxWpLmT")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-10-31T18:00"))
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("Brommapojkarna").flashscoreId("ELVAW0WQ").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Norrkoping").flashscoreId("tpjDEBbi").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("KnG1wXDN")
                        .flashscoreLeagueId("nXxWpLmT")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-10-31T18:00"))
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("Djurgarden").flashscoreId("4Kh5hPE1").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Goteborg").flashscoreId("UovQtopk").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("j7OpsFEb")
                        .flashscoreLeagueId("nXxWpLmT")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-10-31T18:00"))
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("Hacken").flashscoreId("W6u0d7B3").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Sirius").flashscoreId("vXr8fotG").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("tdHcvDbH")
                        .flashscoreLeagueId("nXxWpLmT")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-10-31T18:00"))
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("Kalmar").flashscoreId("rkrUu5ae").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Trelleborgs").flashscoreId("KYqn82b7").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("hM5sreah")
                        .flashscoreLeagueId("nXxWpLmT")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-10-31T18:00"))
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("Sundsvall").flashscoreId("fmfHDVDc").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Hammarby").flashscoreId("SQsg3nME").sportType(FOOTBALL).build())
                        .build()
        );
        /*  nsGIGP38, GERMANY_WEST */
        matches.add(
                Match.builder()
                        .flashscoreId("Glxs135f")
                        .flashscoreLeagueId("nsGIGP38")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-10-31T18:30"))
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("Wattenscheid").flashscoreId("bLmQITjr").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Dortmund II").flashscoreId("vVcwNP6f").sportType(FOOTBALL).build())
                        .build()
        );
        /*  ObKhcPDs, BELARUS_PREMIER */
        matches.add(
                Match.builder()
                        .flashscoreId("r1ef33PG")
                        .flashscoreLeagueId("ObKhcPDs")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-10-31T13:45"))
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("Shakhtyor Soligorsk").flashscoreId("thsSAvVr").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Vitebsk").flashscoreId("xKzy9Iae").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("AwQE4h4g")
                        .flashscoreLeagueId("ObKhcPDs")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-10-31T15:45"))
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("Zhodino").flashscoreId("djQvVaME").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Din. Minsk").flashscoreId("Wtzu8xF1").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("AyFyGt9M")
                        .flashscoreLeagueId("ObKhcPDs")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-10-31T17:45"))
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("Brest").flashscoreId("IaRzWu78").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("BATE").flashscoreId("GnDSyToM").sportType(FOOTBALL).build())
                        .build()
        );
        /*  rmioSrer, INDIA_SUPERLIGA */
        matches.add(
                Match.builder()
                        .flashscoreId("p0viODcm")
                        .flashscoreLeagueId("rmioSrer")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-10-31T14:00"))
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("ATK").flashscoreId("2axO2Bap").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Bengaluru").flashscoreId("tCnsmWKr").sportType(FOOTBALL).build())
                        .build()
        );
        /*  KrrdAMyI, PERU_PREMIER */
        matches.add(
                Match.builder()
                        .flashscoreId("zBWqEkeL")
                        .flashscoreLeagueId("KrrdAMyI")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-10-31T20:30"))
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("Cajamarca").flashscoreId("6cko2SpQ").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Garcilaso").flashscoreId("zDuHd68m").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("0hvXGiB2")
                        .flashscoreLeagueId("KrrdAMyI")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-10-31T22:45"))
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("AD Cantolao").flashscoreId("fez8a7k5").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Dep. Municipal").flashscoreId("G4Fiytao").sportType(FOOTBALL).build())
                        .build()
        );
        /*  GKJrZE7I, ARGENTINA_B_METROPOLITANA */
        matches.add(
                Match.builder()
                        .flashscoreId("8p0tV5HG")
                        .flashscoreLeagueId("GKJrZE7I")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-10-31T18:05"))
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("Comunicaciones").flashscoreId("E7vZbFyA").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("All Boys").flashscoreId("M939rYCH").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("j9koUPWM")
                        .flashscoreLeagueId("GKJrZE7I")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-10-31T18:30"))
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("Colegiales").flashscoreId("zHuVaeM3").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Fenix").flashscoreId("bFx5u1LC").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("2mcYWRn4")
                        .flashscoreLeagueId("GKJrZE7I")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-10-31T18:30"))
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("Tristan Suarez").flashscoreId("ttebEY59").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Barracas Central").flashscoreId("hW08cP6J").sportType(FOOTBALL).build())
                        .build()
        );
        /*  COuk57Ci, ITALY_A */
        matches.add(
                Match.builder()
                        .flashscoreId("0p4s1xbH")
                        .flashscoreLeagueId("COuk57Ci")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-10-31T19:30"))
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("AC Milan").flashscoreId("8Sa8HInO").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Genoa").flashscoreId("d0PJxeie").sportType(FOOTBALL).build())
                        .build()
        );
        /*  dMwNgPSC, BELGIUM_B */
        matches.add(
                Match.builder()
                        .flashscoreId("UkPzPZHF")
                        .flashscoreLeagueId("dMwNgPSC")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-10-31T19:30"))
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("KSV Roeselare").flashscoreId("dIdZCbiA").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Westerlo").flashscoreId("hYaM5Gqp").sportType(FOOTBALL).build())
                        .build()
        );
        /*  tGwiyvJ1, SCOTLAND_PREMIER */
        matches.add(
                Match.builder()
                        .flashscoreId("YTjey7am")
                        .flashscoreLeagueId("tGwiyvJ1")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-10-31T19:45"))
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("Aberdeen").flashscoreId("AmdbMiWq").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Hamilton").flashscoreId("4QLusDEd").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("0jNizREg")
                        .flashscoreLeagueId("tGwiyvJ1")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-10-31T19:45"))
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("Dundee FC").flashscoreId("hjIyrgaj").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Celtic").flashscoreId("QFKRRD8M").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("vsMeZpUa")
                        .flashscoreLeagueId("tGwiyvJ1")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-10-31T19:45"))
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("Hearts").flashscoreId("0rGrwwNc").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Hibernian").flashscoreId("f5Fnxcx4").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("Y3LaY4q6")
                        .flashscoreLeagueId("tGwiyvJ1")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-10-31T19:45"))
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("Livingston").flashscoreId("MoYV5dic").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("St Johnstone").flashscoreId("ULK83EjM").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("n7P3XObC")
                        .flashscoreLeagueId("tGwiyvJ1")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-10-31T19:45"))
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("Rangers").flashscoreId("8vAWQXNS").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Kilmarnock").flashscoreId("jX8ezy7G").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("UVE8WrEI")
                        .flashscoreLeagueId("tGwiyvJ1")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-10-31T19:45"))
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("St. Mirren").flashscoreId("tAVXZEyT").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Motherwell").flashscoreId("YuW7ZfMM").sportType(FOOTBALL).build())
                        .build()
        );
        /*  WWZFEdUR, ECUADOR_A */
        matches.add(
                Match.builder()
                        .flashscoreId("MsPTSwgs")
                        .flashscoreLeagueId("WWZFEdUR")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-11-01T00:15"))
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("Ind. del Valle").flashscoreId("YFwXD2sh").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Guayaquil City").flashscoreId("pn42PQGI").sportType(FOOTBALL).build())
                        .build()
        );
        /*  dG2SqPrf, BELGIUM_A */
        matches.add(
                Match.builder()
                        .flashscoreId("AeBYlhkq")
                        .flashscoreLeagueId("dG2SqPrf")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-10-31T19:30"))
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("Antwerp").flashscoreId("rVpPxw8H").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Genk").flashscoreId("r3jWUIiN").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("bHVcefCF")
                        .flashscoreLeagueId("dG2SqPrf")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-10-31T19:30"))
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("Eupen").flashscoreId("roazgtic").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Cercle Brugge KSV").flashscoreId("IXnm9vwh").sportType(FOOTBALL).build())
                        .build()
        );
        matches.add(
                Match.builder()
                        .flashscoreId("jiSkcGs3")
                        .flashscoreLeagueId("dG2SqPrf")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-10-31T19:30"))
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("Kortrijk").flashscoreId("2o6njvyG").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Waasland-Beveren").flashscoreId("QaqfE8WE").sportType(FOOTBALL).build())
                        .build()
        );
        /*  CQv5qrFt, USA_MLS */
        matches.add(
                Match.builder()
                        .flashscoreId("6X96ffSi")
                        .flashscoreLeagueId("CQv5qrFt")
                        .sportType(FOOTBALL)
                        .date(LocalDateTime.parse("2018-10-31T23:00"))
                        .matchStatus(PREMATCH)
                        .homeTeam(Team.builder().name("New York City").flashscoreId("vZraQYnO").sportType(FOOTBALL).build())
                        .awayTeam(Team.builder().name("Philadelphia Union").flashscoreId("Szrwix67").sportType(FOOTBALL).build())
                        .build()
        );
        
        return matches;
    }
}

