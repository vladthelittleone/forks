package com.savik.service.bookmaker.marathon;

import com.savik.domain.BookmakerType;
import com.savik.model.BookmakerCoeff;
import com.savik.service.bookmaker.BookmakerMatch;
import com.savik.service.bookmaker.BookmakerMatchResponse;
import com.savik.service.bookmaker.BookmakerService;
import com.savik.service.bookmaker.CoeffType;
import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.savik.domain.BookmakerType.MARATHON;

@Component
public class MarathonBookmakerService extends BookmakerService {

    final String ADDITIONAL_MARKETS = "\"ADDITIONAL_MARKETS\":\"";


    @Autowired
    MarathonDownloader marathonDownloader;

    @Override
    protected BookmakerType getBookmakerType() {
        return MARATHON;
    }

    @Override
    protected Optional<BookmakerMatchResponse> handle(BookmakerMatch match) {
        Document download = marathonDownloader.download("7083671");
        String body = download.body().outerHtml();
        body = StringEscapeUtils.unescapeHtml(body);
        body = body.replaceAll("\\\\\"", "").replaceAll("\\\\n", "").replaceAll("\n", "");
        int i = body.indexOf(ADDITIONAL_MARKETS);
        final String html = body.substring(i + ADDITIONAL_MARKETS.length(), body.indexOf("} </body>"));
        final Document document = Jsoup.parse(html);


        List<BookmakerCoeff> bookmakerCoeffs = new ArrayList<>();
        fillHandicap(document, bookmakerCoeffs);
        fillAsianHandicap(document, bookmakerCoeffs);
        fillTotal(document, bookmakerCoeffs, match);
        fillAsianTotal(document, bookmakerCoeffs, match);

        final BookmakerMatchResponse build = BookmakerMatchResponse.builder()
                .bookmakerType(MARATHON)
                .bookmakerMatchId("!!!!")
                .bookmakerLeagueId(match.getBookmakerLeague().getBookmakerId())
                .bookmakerHomeTeamName(match.getHomeTeam().getName())
                .bookmakerAwayTeamName(match.getAwayTeam().getName())
                .bookmakerCoeffs(bookmakerCoeffs)
                .build();

        return Optional.of(build);
    }

    private void fillHandicap(Document document, List<BookmakerCoeff> bookmakerCoeffs) {
        final Elements handicaps = document.select("div.name-field:containsOwn(With Handicap)");
        for (Element handicap : handicaps) {
            final Element marketTableName = handicap.parent().parent().parent().parent();
            final Element coeffsTable = marketTableName.nextElementSibling();
            final Elements handicapBlock = coeffsTable.select("> tbody > tr[data-header-highlighted-bounded]");
            final Elements prices = handicapBlock.select("td.price");
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
            if (handicap.text().equalsIgnoreCase("To Win 2nd Half With Handicap")) {
                final BookmakerCoeff homeCoeff = createFromHandicapBlock(homePrice, CoeffType.SECOND_HALF, CoeffType.HOME, CoeffType.HANDICAP);
                final BookmakerCoeff awayCoeff = createFromHandicapBlock(awayPrice, CoeffType.SECOND_HALF, CoeffType.AWAY, CoeffType.HANDICAP);
                bookmakerCoeffs.add(homeCoeff);
                bookmakerCoeffs.add(awayCoeff);
            }
        }
    }

    private void fillTotal(Document document, List<BookmakerCoeff> bookmakerCoeffs, BookmakerMatch match) {
        final Elements handicaps = document.select("div.name-field:containsOwn(Total Goals)");
        for (Element handicap : handicaps) {
            final Element marketTableName = handicap.parent().parent().parent().parent();
            final Element coeffsTable = marketTableName.nextElementSibling();
            final Elements handicapBlock = coeffsTable.select("> tbody > tr[data-header-highlighted-bounded]");
            final Elements prices = handicapBlock.select("td.price");
            final Element homePrice = prices.get(0);
            final Element awayPrice = prices.get(1);
            if (handicap.text().equalsIgnoreCase("Total Goals")) {
                final BookmakerCoeff overCoeff = createFromHandicapBlock(homePrice, CoeffType.MATCH, CoeffType.COMMON, CoeffType.TOTAL, CoeffType.OVER);
                final BookmakerCoeff underCoeff = createFromHandicapBlock(awayPrice, CoeffType.MATCH, CoeffType.COMMON, CoeffType.TOTAL, CoeffType.UNDER);
                bookmakerCoeffs.add(overCoeff);
                bookmakerCoeffs.add(underCoeff);
            }
            if (handicap.text().equalsIgnoreCase("Total Goals (" + match.getHomeTeam().getName() + ")")) {
                final BookmakerCoeff overCoeff = createFromHandicapBlock(homePrice, CoeffType.MATCH, CoeffType.HOME, CoeffType.TOTAL, CoeffType.OVER);
                final BookmakerCoeff underCoeff = createFromHandicapBlock(awayPrice, CoeffType.MATCH, CoeffType.HOME, CoeffType.TOTAL, CoeffType.UNDER);
                bookmakerCoeffs.add(overCoeff);
                bookmakerCoeffs.add(underCoeff);
            }
            if (handicap.text().equalsIgnoreCase("Total Goals (" + match.getAwayTeam().getName() + ")")) {
                final BookmakerCoeff overCoeff = createFromHandicapBlock(homePrice, CoeffType.MATCH, CoeffType.AWAY, CoeffType.TOTAL, CoeffType.OVER);
                final BookmakerCoeff underCoeff = createFromHandicapBlock(awayPrice, CoeffType.MATCH, CoeffType.AWAY, CoeffType.TOTAL, CoeffType.UNDER);
                bookmakerCoeffs.add(overCoeff);
                bookmakerCoeffs.add(underCoeff);
            }
            if (handicap.text().equalsIgnoreCase("Total Goals - 1st Half")) {
                final BookmakerCoeff overCoeff = createFromHandicapBlock(homePrice, CoeffType.FIRST_HALF, CoeffType.COMMON, CoeffType.TOTAL, CoeffType.OVER);
                final BookmakerCoeff underCoeff = createFromHandicapBlock(awayPrice, CoeffType.FIRST_HALF, CoeffType.COMMON, CoeffType.TOTAL, CoeffType.UNDER);
                bookmakerCoeffs.add(overCoeff);
                bookmakerCoeffs.add(underCoeff);
            }
            if (handicap.text().equalsIgnoreCase("Total Goals (" + match.getHomeTeam().getName() + ") - 1st Half")) {
                final BookmakerCoeff overCoeff = createFromHandicapBlock(homePrice, CoeffType.FIRST_HALF, CoeffType.HOME, CoeffType.TOTAL, CoeffType.OVER);
                final BookmakerCoeff underCoeff = createFromHandicapBlock(awayPrice, CoeffType.FIRST_HALF, CoeffType.HOME, CoeffType.TOTAL, CoeffType.UNDER);
                bookmakerCoeffs.add(overCoeff);
                bookmakerCoeffs.add(underCoeff);
            }
            if (handicap.text().equalsIgnoreCase("Total Goals (" + match.getAwayTeam().getName() + ") - 1st Half")) {
                final BookmakerCoeff overCoeff = createFromHandicapBlock(homePrice, CoeffType.FIRST_HALF, CoeffType.AWAY, CoeffType.TOTAL, CoeffType.OVER);
                final BookmakerCoeff underCoeff = createFromHandicapBlock(awayPrice, CoeffType.FIRST_HALF, CoeffType.AWAY, CoeffType.TOTAL, CoeffType.UNDER);
                bookmakerCoeffs.add(overCoeff);
                bookmakerCoeffs.add(underCoeff);
            }
            if (handicap.text().equalsIgnoreCase("Total Goals - 2nd Half")) {
                final BookmakerCoeff overCoeff = createFromHandicapBlock(homePrice, CoeffType.SECOND_HALF, CoeffType.COMMON, CoeffType.TOTAL, CoeffType.OVER);
                final BookmakerCoeff underCoeff = createFromHandicapBlock(awayPrice, CoeffType.SECOND_HALF, CoeffType.COMMON, CoeffType.TOTAL, CoeffType.UNDER);
                bookmakerCoeffs.add(overCoeff);
                bookmakerCoeffs.add(underCoeff);
            }
            if (handicap.text().equalsIgnoreCase("Total Goals (" + match.getHomeTeam().getName() + ") - 2st Half")) {
                final BookmakerCoeff overCoeff = createFromHandicapBlock(homePrice, CoeffType.SECOND_HALF, CoeffType.HOME, CoeffType.TOTAL, CoeffType.OVER);
                final BookmakerCoeff underCoeff = createFromHandicapBlock(awayPrice, CoeffType.SECOND_HALF, CoeffType.HOME, CoeffType.TOTAL, CoeffType.UNDER);
                bookmakerCoeffs.add(overCoeff);
                bookmakerCoeffs.add(underCoeff);
            }
            if (handicap.text().equalsIgnoreCase("Total Goals (" + match.getAwayTeam().getName() + ") - 2st Half")) {
                final BookmakerCoeff overCoeff = createFromHandicapBlock(homePrice, CoeffType.SECOND_HALF, CoeffType.AWAY, CoeffType.TOTAL, CoeffType.OVER);
                final BookmakerCoeff underCoeff = createFromHandicapBlock(awayPrice, CoeffType.SECOND_HALF, CoeffType.AWAY, CoeffType.TOTAL, CoeffType.UNDER);
                bookmakerCoeffs.add(overCoeff);
                bookmakerCoeffs.add(underCoeff);
            }
        }
    }

    private void fillAsianTotal(Document document, List<BookmakerCoeff> bookmakerCoeffs, BookmakerMatch match) {
        final Elements handicaps = document.select("div.name-field:containsOwn(Asian Total Goals)");
        for (Element handicap : handicaps) {
            final Element marketTableName = handicap.parent().parent().parent().parent();
            final Element coeffsTable = marketTableName.nextElementSibling();
            final Elements handicapBlock = coeffsTable.select("> tbody > tr[data-header-highlighted-bounded]");
            final Elements prices = handicapBlock.select("td.price");
            final Element homePrice = prices.get(0);
            final Element awayPrice = prices.get(1);
            if (handicap.text().equalsIgnoreCase("Asian Total Goals")) {
                final BookmakerCoeff overCoeff = createFromAsianHandicapBlock(homePrice, CoeffType.MATCH, CoeffType.COMMON, CoeffType.TOTAL, CoeffType.OVER);
                final BookmakerCoeff underCoeff = createFromAsianHandicapBlock(awayPrice, CoeffType.MATCH, CoeffType.COMMON, CoeffType.TOTAL, CoeffType.UNDER);
                bookmakerCoeffs.add(overCoeff);
                bookmakerCoeffs.add(underCoeff);
            }
        }
    }

    private void fillAsianHandicap(Document document, List<BookmakerCoeff> bookmakerCoeffs) {
        final Elements handicaps = document.select("div.name-field:containsOwn(To Win Match With Asian Handicap)");
        for (Element handicap : handicaps) {
            final Element marketTableName = handicap.parent().parent().parent().parent();
            final Element coeffsTable = marketTableName.nextElementSibling();
            final Elements handicapBlock = coeffsTable.select("> tbody > tr[data-header-highlighted-bounded]");
            final Elements prices = handicapBlock.select("td.price");
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

    BookmakerCoeff createFromHandicapBlock(Element block, CoeffType... types) {
        final String handicapTypeWithBraces = block.select("div.coeff-handicap").text();
        final String handicapType = handicapTypeWithBraces.substring(1, handicapTypeWithBraces.length() - 1);
        final String handicapValue = block.select("div.coeff-price > span").text();
        return BookmakerCoeff.of(Double.valueOf(handicapType), Double.valueOf(handicapValue), types);
    }

    BookmakerCoeff createFromAsianHandicapBlock(Element block, CoeffType... types) {
        final String handicapTypeWithBraces = block.select("div.coeff-handicap").text();
        final String handicapType = handicapTypeWithBraces.substring(1, handicapTypeWithBraces.length() - 1);
        final String[] values = handicapType.split(",");
        final double value = new BigDecimal(values[0]).add(new BigDecimal(values[1])).divide(new BigDecimal(2)).doubleValue();
        final String handicapValue = block.select("div.coeff-price > span").text();
        return BookmakerCoeff.of(value, Double.valueOf(handicapValue), types);
    }
}
