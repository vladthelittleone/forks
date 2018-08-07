package com.savik.service.bookmaker.marathon;

import com.savik.domain.BookmakerType;
import com.savik.domain.SportType;
import com.savik.model.BookmakerCoeff;
import com.savik.service.bookmaker.BookmakerMatch;
import com.savik.service.bookmaker.BookmakerMatchResponse;
import com.savik.service.bookmaker.CoeffType;
import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class MarathonParser {

    final String ADDITIONAL_MARKETS = "\"ADDITIONAL_MARKETS\":\"";

    @Autowired
    MarathonDownloader marathonDownloader;

    public List<BookmakerCoeff> downloadAndParseMatch(String marathonMatchId, BookmakerMatch match) {
        Document download = marathonDownloader.download(marathonMatchId);
        String body = download.body().outerHtml();
        body = StringEscapeUtils.unescapeHtml(body);
        body = body.replaceAll("\\\\\"", "").replaceAll("\\\\n", "").replaceAll("\n", "");
        int i = body.indexOf(ADDITIONAL_MARKETS);
        final String html = body.indexOf("} </body>") != -1 ? 
                body.substring(i + ADDITIONAL_MARKETS.length(), body.indexOf("} </body>")) :
                body.substring(i + ADDITIONAL_MARKETS.length(), body.indexOf("</body>"));
        final Document document = Jsoup.parse(html);


        List<BookmakerCoeff> bookmakerCoeffs = new ArrayList<>();
        fillHandicap(document, bookmakerCoeffs);
        fillAsianHandicap(document, bookmakerCoeffs);
        fillTotal(document, bookmakerCoeffs, match);
        fillAsianTotal(document, bookmakerCoeffs, match);

        bookmakerCoeffs.removeAll(Collections.singleton(null));
        return bookmakerCoeffs;
    }
    
    public List<BookmakerMatchResponse> getMatchesBySport(SportType sportType) {
        final Document download = marathonDownloader.download(sportType);

        List<BookmakerMatchResponse> responses = new ArrayList<>();
        final Elements leagues = download.select("div.category-container");
        for (Element league : leagues) {
            final Optional<String> leagueName = league.select(".category-label").first().select("span").stream().map(e -> e.text()).reduce((a, b) -> a + b);
            final Elements matches = league.select("table.foot-market > tbody[data-event-treeid]");
            for (Element match : matches) {
                final String bookmakerMatchId = match.attr("data-event-treeid");
                final Element table = match.select("table.member-area-content-table").first();
                final Elements names = table.select("td.name");
                String homeTeam = null, awayTeam = null;
                for (Element name : names) {
                    final String teamNumber = name.select(".member-number").text();
                    final String nameValue = name.select("div.member-name > span").text();
                    if("1.".equalsIgnoreCase(teamNumber)) {
                        homeTeam = nameValue;
                    }if("2.".equalsIgnoreCase(teamNumber)) {
                        awayTeam = nameValue;
                    }
                }
                BookmakerMatchResponse bookmakerMatchResponse = BookmakerMatchResponse.builder()
                        .bookmakerAwayTeamName(awayTeam)
                        .bookmakerHomeTeamName(homeTeam)
                        .bookmakerMatchId(bookmakerMatchId)
                        .bookmakerType(BookmakerType.MARATHON)
                        .bookmakerLeagueId(leagueName.orElse("DEFAULT"))
                        .build();
                responses.add(bookmakerMatchResponse);
            }
        }
        return responses;
    }

    private void fillHandicap(Document document, List<BookmakerCoeff> bookmakerCoeffs) {
        final Elements handicaps = document.select("div.name-field:containsOwn(With Handicap)");
        for (Element handicap : handicaps) {
            final Element marketTableName = handicap.parent().parent().parent().parent();
            final Element coeffsTable = marketTableName.nextElementSibling();
            final Elements handicapBlocks = coeffsTable.select("> tbody > tr[data-header-highlighted-bounded]");
            for (Element handicapBlock : handicapBlocks) {
                final Elements prices = handicapBlock.select("td.price");
                if(prices.size() != 2) {
                    continue;
                }
                final Element homePrice = prices.get(0);
                final Element awayPrice = prices.get(1);
                if (handicap.text().equalsIgnoreCase("To Win Match With Handicap")) {
                    final BookmakerCoeff homeCoeff = createFromHandicapBlock(homePrice, CoeffType.MATCH, CoeffType.HOME, CoeffType.HANDICAP);
                    final BookmakerCoeff awayCoeff = createFromHandicapBlock(awayPrice, CoeffType.MATCH, CoeffType.AWAY, CoeffType.HANDICAP);
                    bookmakerCoeffs.add(homeCoeff);
                    bookmakerCoeffs.add(awayCoeff);
                }
                if (handicap.text().equalsIgnoreCase("To Win 1st Half With Handicap")) {
                    final BookmakerCoeff homeCoeff = createFromHandicapBlock(homePrice, CoeffType.FIRST_HALF, CoeffType.HOME, CoeffType.HANDICAP);
                    final BookmakerCoeff awayCoeff = createFromHandicapBlock(awayPrice, CoeffType.FIRST_HALF, CoeffType.AWAY, CoeffType.HANDICAP);
                    bookmakerCoeffs.add(homeCoeff);
                    bookmakerCoeffs.add(awayCoeff);
                }
                /*if (handicap.text().equalsIgnoreCase("To Win 2nd Half With Handicap")) {
                    final BookmakerCoeff homeCoeff = createFromHandicapBlock(homePrice, CoeffType.SECOND_HALF, CoeffType.HOME, CoeffType.HANDICAP);
                    final BookmakerCoeff awayCoeff = createFromHandicapBlock(awayPrice, CoeffType.SECOND_HALF, CoeffType.AWAY, CoeffType.HANDICAP);
                    bookmakerCoeffs.add(homeCoeff);
                    bookmakerCoeffs.add(awayCoeff);
                }*/
            }
        }
    }

    private void fillTotal(Document document, List<BookmakerCoeff> bookmakerCoeffs, BookmakerMatch match) {
        final Elements handicaps = document.select("div.name-field:containsOwn(Total Goals)");
        for (Element handicap : handicaps) {
            final Element marketTableName = handicap.parent().parent().parent().parent();
            final Element coeffsTable = marketTableName.nextElementSibling();
            final Elements totalBlocks = coeffsTable.select("> tbody > tr[data-header-highlighted-bounded]");
            for (Element totalBlock : totalBlocks) {
                final Elements prices = totalBlock.select("td.price");
                if(prices.size() != 2) {
                    continue;
                }
                final Element underPrice = prices.get(0);
                final Element overPrice = prices.get(1);
                if (handicap.text().equalsIgnoreCase("Total Goals")) {
                    final BookmakerCoeff overCoeff = createFromHandicapBlock(overPrice, CoeffType.MATCH, CoeffType.COMMON, CoeffType.TOTAL, CoeffType.OVER);
                    final BookmakerCoeff underCoeff = createFromHandicapBlock(underPrice, CoeffType.MATCH, CoeffType.COMMON, CoeffType.TOTAL, CoeffType.UNDER);
                    bookmakerCoeffs.add(overCoeff);
                    bookmakerCoeffs.add(underCoeff);
                }
                if (handicap.text().equalsIgnoreCase("Total Goals (" + match.getHomeTeam().getName() + ")")) {
                    final BookmakerCoeff overCoeff = createFromHandicapBlock(overPrice, CoeffType.MATCH, CoeffType.HOME, CoeffType.TOTAL, CoeffType.OVER);
                    final BookmakerCoeff underCoeff = createFromHandicapBlock(underPrice, CoeffType.MATCH, CoeffType.HOME, CoeffType.TOTAL, CoeffType.UNDER);
                    bookmakerCoeffs.add(overCoeff);
                    bookmakerCoeffs.add(underCoeff);
                }
                if (handicap.text().equalsIgnoreCase("Total Goals (" + match.getAwayTeam().getName() + ")")) {
                    final BookmakerCoeff overCoeff = createFromHandicapBlock(overPrice, CoeffType.MATCH, CoeffType.AWAY, CoeffType.TOTAL, CoeffType.OVER);
                    final BookmakerCoeff underCoeff = createFromHandicapBlock(underPrice, CoeffType.MATCH, CoeffType.AWAY, CoeffType.TOTAL, CoeffType.UNDER);
                    bookmakerCoeffs.add(overCoeff);
                    bookmakerCoeffs.add(underCoeff);
                }
                if (handicap.text().equalsIgnoreCase("Total Goals - 1st Half")) {
                    final BookmakerCoeff overCoeff = createFromHandicapBlock(overPrice, CoeffType.FIRST_HALF, CoeffType.COMMON, CoeffType.TOTAL, CoeffType.OVER);
                    final BookmakerCoeff underCoeff = createFromHandicapBlock(underPrice, CoeffType.FIRST_HALF, CoeffType.COMMON, CoeffType.TOTAL, CoeffType.UNDER);
                    bookmakerCoeffs.add(overCoeff);
                    bookmakerCoeffs.add(underCoeff);
                }
                if (handicap.text().equalsIgnoreCase("Total Goals (" + match.getHomeTeam().getName() + ") - 1st Half")) {
                    final BookmakerCoeff overCoeff = createFromHandicapBlock(overPrice, CoeffType.FIRST_HALF, CoeffType.HOME, CoeffType.TOTAL, CoeffType.OVER);
                    final BookmakerCoeff underCoeff = createFromHandicapBlock(underPrice, CoeffType.FIRST_HALF, CoeffType.HOME, CoeffType.TOTAL, CoeffType.UNDER);
                    bookmakerCoeffs.add(overCoeff);
                    bookmakerCoeffs.add(underCoeff);
                }
                if (handicap.text().equalsIgnoreCase("Total Goals (" + match.getAwayTeam().getName() + ") - 1st Half")) {
                    final BookmakerCoeff overCoeff = createFromHandicapBlock(overPrice, CoeffType.FIRST_HALF, CoeffType.AWAY, CoeffType.TOTAL, CoeffType.OVER);
                    final BookmakerCoeff underCoeff = createFromHandicapBlock(underPrice, CoeffType.FIRST_HALF, CoeffType.AWAY, CoeffType.TOTAL, CoeffType.UNDER);
                    bookmakerCoeffs.add(overCoeff);
                    bookmakerCoeffs.add(underCoeff);
                }
                /*if (handicap.text().equalsIgnoreCase("Total Goals - 2nd Half")) {
                    final BookmakerCoeff overCoeff = createFromHandicapBlock(overPrice, CoeffType.SECOND_HALF, CoeffType.COMMON, CoeffType.TOTAL, CoeffType.OVER);
                    final BookmakerCoeff underCoeff = createFromHandicapBlock(underPrice, CoeffType.SECOND_HALF, CoeffType.COMMON, CoeffType.TOTAL, CoeffType.UNDER);
                    bookmakerCoeffs.add(overCoeff);
                    bookmakerCoeffs.add(underCoeff);
                }
                if (handicap.text().equalsIgnoreCase("Total Goals (" + match.getHomeTeam().getName() + ") - 2st Half")) {
                    final BookmakerCoeff overCoeff = createFromHandicapBlock(overPrice, CoeffType.SECOND_HALF, CoeffType.HOME, CoeffType.TOTAL, CoeffType.OVER);
                    final BookmakerCoeff underCoeff = createFromHandicapBlock(underPrice, CoeffType.SECOND_HALF, CoeffType.HOME, CoeffType.TOTAL, CoeffType.UNDER);
                    bookmakerCoeffs.add(overCoeff);
                    bookmakerCoeffs.add(underCoeff);
                }
                if (handicap.text().equalsIgnoreCase("Total Goals (" + match.getAwayTeam().getName() + ") - 2st Half")) {
                    final BookmakerCoeff overCoeff = createFromHandicapBlock(overPrice, CoeffType.SECOND_HALF, CoeffType.AWAY, CoeffType.TOTAL, CoeffType.OVER);
                    final BookmakerCoeff underCoeff = createFromHandicapBlock(underPrice, CoeffType.SECOND_HALF, CoeffType.AWAY, CoeffType.TOTAL, CoeffType.UNDER);
                    bookmakerCoeffs.add(overCoeff);
                    bookmakerCoeffs.add(underCoeff);
                }*/
            }

        }
    }

    private void fillAsianTotal(Document document, List<BookmakerCoeff> bookmakerCoeffs, BookmakerMatch match) {
        final Elements handicaps = document.select("div.name-field:containsOwn(Asian Total Goals)");
        for (Element handicap : handicaps) {
            final Element marketTableName = handicap.parent().parent().parent().parent();
            final Element coeffsTable = marketTableName.nextElementSibling();
            final Elements totalBlocks = coeffsTable.select("> tbody > tr[data-header-highlighted-bounded]");
            for (Element totalBlock : totalBlocks) {
                final Elements prices = totalBlock.select("td.price");
                if(prices.size() != 2) {
                    continue;
                }
                final Element underPrice = prices.get(0);
                final Element overPrice = prices.get(1);
                if (handicap.text().equalsIgnoreCase("Asian Total Goals")) {
                    final BookmakerCoeff overCoeff = createFromAsianHandicapBlock(overPrice, CoeffType.MATCH, CoeffType.COMMON, CoeffType.TOTAL, CoeffType.OVER);
                    final BookmakerCoeff underCoeff = createFromAsianHandicapBlock(underPrice, CoeffType.MATCH, CoeffType.COMMON, CoeffType.TOTAL, CoeffType.UNDER);
                    bookmakerCoeffs.add(overCoeff);
                    bookmakerCoeffs.add(underCoeff);
                }
            }
        }
    }

    private void fillAsianHandicap(Document document, List<BookmakerCoeff> bookmakerCoeffs) {
        final Elements handicaps = document.select("div.name-field:containsOwn(To Win Match With Asian Handicap)");
        for (Element handicap : handicaps) {
            final Element marketTableName = handicap.parent().parent().parent().parent();
            final Element coeffsTable = marketTableName.nextElementSibling();
            final Elements handicapBlocks = coeffsTable.select("> tbody > tr[data-header-highlighted-bounded]");
            for (Element handicapBlock : handicapBlocks) {
                final Elements prices = handicapBlock.select("td.price");
                if(prices.size() != 2) {
                    continue;
                }
                final Element homePrice = prices.get(0);
                final Element awayPrice = prices.get(1);
                if (handicap.text().equalsIgnoreCase("To Win Match With Asian Handicap")) {
                    final BookmakerCoeff homeCoeff = createFromAsianHandicapBlock(homePrice, CoeffType.MATCH, CoeffType.HOME, CoeffType.HANDICAP);
                    final BookmakerCoeff awayCoeff = createFromAsianHandicapBlock(awayPrice, CoeffType.MATCH, CoeffType.AWAY, CoeffType.HANDICAP);
                    bookmakerCoeffs.add(homeCoeff);
                    bookmakerCoeffs.add(awayCoeff);
                }
            }

        }
    }

    BookmakerCoeff createFromHandicapBlock(Element block, CoeffType... types) {
        final String handicapTypeWithBraces = block.select("div.coeff-handicap").text();
        if(StringUtils.isEmpty(handicapTypeWithBraces)){
            return null;
        }
        final String handicapType = handicapTypeWithBraces.substring(1, handicapTypeWithBraces.length() - 1);
        final String handicapValue = block.select("div.coeff-price > span").text();
        return BookmakerCoeff.of(Double.valueOf(handicapType), Double.valueOf(handicapValue), types);
    }

    BookmakerCoeff createFromAsianHandicapBlock(Element block, CoeffType... types) {
        final String handicapTypeWithBraces = block.select("div.coeff-handicap").text();
        if(StringUtils.isEmpty(handicapTypeWithBraces)){
            return null;
        }
        final String handicapType = handicapTypeWithBraces.substring(1, handicapTypeWithBraces.length() - 1);
        final String[] values = handicapType.split(",");
        final double value = new BigDecimal(values[0]).add(new BigDecimal(values[1])).divide(new BigDecimal(2)).doubleValue();
        final String handicapValue = block.select("div.coeff-price > span").text();
        return BookmakerCoeff.of(value, Double.valueOf(handicapValue), types);
    }
}
