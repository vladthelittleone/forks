package com.savik.service.bookmaker.sbobet;

import com.savik.domain.BookmakerType;
import com.savik.domain.Match;
import com.savik.domain.SportType;
import com.savik.service.bookmaker.BookmakerCoeff;
import com.savik.service.bookmaker.BookmakerMatch;
import com.savik.service.bookmaker.BookmakerMatchResponse;
import com.savik.service.bookmaker.BookmakerService;
import com.savik.service.bookmaker.CoeffType;
import com.savik.service.bookmaker.Downloader;
import org.json.JSONArray;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SbobetBookmakerService extends BookmakerService {

    public static final int LIVE_AND_PREMATCH_INDEX = 2;
    public static final int LEAGUE_ID_INDEX = 1;
    public static final int MATCH_INFO_INDEX = 2;
    public static final int MATCH_COEFFS_INDEX = 4;
    public static final int MATCH_ID_INDEX = 0;
    public static final int HOME_NAME_INDEX = 1;
    public static final int GUEST_NAME_INDEX = 2;
    public static final int HOME_COEFF_INDEX = 0;
    public static final int GUEST_COEFF_INDEX = 1;
    public static final int MATCHES_IN_CONTAINER_INDEX = 1;
    public static final int LIVE_MATCHES_INDEX = 0;
    public static final int PREMATCH_MATCHES_WHILE_LIVE_EXISTS_INDEX = 1;
    public static final int PREMATCH_MATCHES_WHILE_LIVE_NOT_EXISTS_INDEX = 0;
    @Autowired
    Downloader downloader;

    @Override
    protected BookmakerType getBookmakerType() {
        return BookmakerType.SBOBET;
    }

    @Override
    public void handle(BookmakerMatch bookmakerMatch) {

        Match match = bookmakerMatch.getMatch();
        SportType sportType = match.getSportType();

        if (sportType == SportType.FOOTBALL) {
            Document download = downloader.download("https://www.sbobet.com/euro/football");
            List<BookmakerMatchResponse> matches = getMatches(download);

            String a = "";
        }

    }

    private List<BookmakerMatchResponse> getMatches(Document download) {
        Element script = download.getElementsByTag("script").last();
        String scriptText = script.childNodes().get(0).toString();

        String jsArray = scriptText.substring(scriptText.indexOf("onUpdate('od',") + 14, scriptText.indexOf("); }"));
        JSONArray jsonObject = new JSONArray(jsArray);

        List<BookmakerMatchResponse> bookmakerMatchResponses = new ArrayList<>();


        JSONArray liveAndPrematchArrays = jsonObject.getJSONArray(LIVE_AND_PREMATCH_INDEX);
        // live and prematch
        if (liveAndPrematchArrays.length() == 2) {
            getMatchesFromArray(liveAndPrematchArrays.getJSONArray(LIVE_MATCHES_INDEX)
                    .getJSONArray(MATCHES_IN_CONTAINER_INDEX), bookmakerMatchResponses);
            getMatchesFromArray(liveAndPrematchArrays.getJSONArray(PREMATCH_MATCHES_WHILE_LIVE_EXISTS_INDEX)
                    .getJSONArray(MATCHES_IN_CONTAINER_INDEX), bookmakerMatchResponses);
        } else if (liveAndPrematchArrays.length() == 1) {         // only prematch
            getMatchesFromArray(liveAndPrematchArrays.getJSONArray(PREMATCH_MATCHES_WHILE_LIVE_NOT_EXISTS_INDEX)
                    .getJSONArray(MATCHES_IN_CONTAINER_INDEX), bookmakerMatchResponses);
        }
        return bookmakerMatchResponses;

    }

    private void getMatchesFromArray(JSONArray matchesArray, List<BookmakerMatchResponse> bookmakerMatchResponses) {
        for (int i = 0; i < matchesArray.length(); i++) {
            JSONArray matchArray = matchesArray.getJSONArray(i);
            int sbobetLeagueId = matchArray.getInt(LEAGUE_ID_INDEX);
            JSONArray matchInfoArray = matchArray.getJSONArray(MATCH_INFO_INDEX);

            int matchId = matchInfoArray.getInt(MATCH_ID_INDEX);
            String homeTeamName = matchInfoArray.getString(HOME_NAME_INDEX);
            String guestTeamName = matchInfoArray.getString(GUEST_NAME_INDEX);

            JSONArray matchCoeffsArray = matchArray.getJSONArray(MATCH_COEFFS_INDEX);

            List<BookmakerCoeff> bookmakerCoeffs = new ArrayList<>();
            // j = 1, because, first element is info array
            for (int j = 1; j < matchCoeffsArray.length(); j++) {
                JSONArray coeffArrayContainer = matchCoeffsArray.getJSONArray(j);
                JSONArray handicapValueArray = coeffArrayContainer.getJSONArray(1);

                double handicapValue = handicapValueArray.getDouble(5);
                // 0 is 1x2 coeffs. useless for forks
                if (handicapValue != 0) {
                    JSONArray coeffValueArray = coeffArrayContainer.getJSONArray(2);
                    double homeCoeffValue = coeffValueArray.getDouble(HOME_COEFF_INDEX);
                    double guestCoeffValue = coeffValueArray.getDouble(GUEST_COEFF_INDEX);

                    BookmakerCoeff homeCoeff = new BookmakerCoeff(
                            CoeffType.HOME,
                            new BookmakerCoeff(
                                    CoeffType.MATCH,
                                    new BookmakerCoeff(CoeffType.HANDICAP, handicapValue, homeCoeffValue)
                            )
                    );

                    BookmakerCoeff guestCoeff = new BookmakerCoeff(
                            CoeffType.GUEST,
                            new BookmakerCoeff(
                                    CoeffType.MATCH,
                                    new BookmakerCoeff(CoeffType.HANDICAP, -handicapValue, guestCoeffValue)
                            )
                    );

                    bookmakerCoeffs.add(homeCoeff);
                    bookmakerCoeffs.add(guestCoeff);

                }
            }
            if (!bookmakerCoeffs.isEmpty()) {
                BookmakerMatchResponse bookmakerMatchResponse = BookmakerMatchResponse.builder()
                        .bookmakerCoeffs(bookmakerCoeffs)
                        .bookmakerGuestTeamName(guestTeamName)
                        .bookmakerHomeTeamName(homeTeamName)
                        .bookmakerLeagueId(String.valueOf(sbobetLeagueId))
                        .bookmakerMatchId(String.valueOf(matchId))
                        .build();

                bookmakerMatchResponses.add(bookmakerMatchResponse);


            }


        }
    }

    public static final String ARRAY_WITH_LIVE = "[4331,1,[[1,[[1,38809,[2381080,'Botosani','ACS Poli Timisoara','1.009-E030118052402',10,'05/25/2018 02:00','',0,0,1,13,{}],[1,1,11,45,0,0,{},,{1:45,2:45,3:15,4:15},0],[[6,6,[]],[33285244,[1,0,1,1,2000,0.50,2857383],[1.87,2.01]],[33285250,[5,0,5,1,500,0.00,2857383],[1.84,3.20,3.75]]],1],[1,25403,[2399369,'KFC Uerdingen 05 (n)','SV Waldhof Mannheim','1.010-E032418052403',10,'05/25/2018 01:30','',0,0,1,,{}],[1,1,44,45,0,0,{},,{1:45,2:45,3:15,4:15},0],[[4,4,[]],[33570958,[1,0,1,1,1000,0.25,2910796],[2.44,1.57]],[33570962,[5,0,5,1,500,0.00,2910796],[2.95,2.16,3.15]],[33570965,[1,0,1,1,2000,0.00,2910796],[1.94,1.94]]],3],[1,25403,[2399370,'SC Weiche Flensburg 08','Energie Cottbus','1.010-E032418052402',10,'05/25/2018 01:00','',0,3,1,19,{}],[1,2,11,45,0,0,{},,{1:45,2:45,3:15,4:15},0],[[3,3,[]],[33570968,[1,0,1,1,1000,0.00,2910797],[2.31,1.64]],[33570975,[1,0,1,1,2000,-0.25,2910797],[1.64,2.31]]],2],[1,185,[2397837,'AFC Eskilstuna','Gefle','1.005-E017618052401',10,'05/25/2018 01:00','',1,0,1,7,{126:[7,2]}],[2,2,12,45,0,0,{312:[0,2,12,45,0,0,,,{1:45,2:45,3:15,4:15},0],311:[0,2,12,45,0,0,,,{1:45,2:45,3:15,4:15},0],310:[0,2,12,45,0,0,,,{1:45,2:45,3:15,4:15},0],309:[0,2,12,45,0,0,,,{1:45,2:45,3:15,4:15},0],308:[0,2,12,45,0,0,,,{1:45,2:45,3:15,4:15},0],307:[0,2,12,45,0,0,,,{1:45,2:45,3:15,4:15},0],306:[0,2,12,45,0,0,,,{1:45,2:45,3:15,4:15},0],126:[1,2,12,45,0,0,,,{1:45,2:45,3:15,4:15},0],124:[0,5,27,45,0,0,,'HT',{1:45,2:45,3:15,4:15},0],123:[0,5,27,45,0,0,,'HT',{1:45,2:45,3:15,4:15},0]},,{1:45,2:45,3:15,4:15},0],[[1,1,[[1,0,1],[5,0,5]]]],3],[1,25,[2397641,'Trelleborgs FF','Brommapojkarna','1.003-E013118052401',10,'05/25/2018 01:00','',2,1,1,6,{126:[7,1]}],[3,2,11,45,0,0,{310:[0,2,11,45,0,0,,,{1:45,2:45,3:15,4:15},0],309:[0,2,11,45,0,0,,,{1:45,2:45,3:15,4:15},0],308:[0,2,11,45,0,0,,,{1:45,2:45,3:15,4:15},0],307:[0,2,11,45,0,0,,,{1:45,2:45,3:15,4:15},0],306:[0,2,11,45,0,0,,,{1:45,2:45,3:15,4:15},0],126:[1,2,11,45,0,0,,,{1:45,2:45,3:15,4:15},0],124:[0,5,26,45,0,0,,'HT',{1:45,2:45,3:15,4:15},0],123:[0,5,26,45,0,0,,'HT',{1:45,2:45,3:15,4:15},0]},,{1:45,2:45,3:15,4:15},0],[[11,11,[]],[33539609,[1,0,1,1,12000,0.25,2904896],[2.16,1.78]],[33539610,[1,0,1,1,3000,0.50,2904896],[2.58,1.55]],[33539615,[5,0,5,1,3000,0.00,2904896],[1.26,4.30,20.00]],[33539619,[1,0,1,1,5000,0.00,2904896],[1.60,2.47]]],1],[1,25,[2397642,'IFK Goteborg','Djurgardens','1.003-E013118052402',10,'05/25/2018 01:00','',0,2,1,2,{126:[5,2]}],[3,0,4,45,0,0,{312:[0,0,4,45,0,0,,,{1:45,2:45,3:15,4:15},0],311:[0,0,4,45,0,0,,,{1:45,2:45,3:15,4:15},0],310:[0,0,4,45,0,0,,,{1:45,2:45,3:15,4:15},0],309:[0,0,4,45,0,0,,,{1:45,2:45,3:15,4:15},0],308:[0,0,4,45,0,0,,,{1:45,2:45,3:15,4:15},0],307:[0,0,4,45,0,0,,,{1:45,2:45,3:15,4:15},0],306:[0,0,4,45,0,0,,,{1:45,2:45,3:15,4:15},0],126:[1,0,4,45,0,0,,,{1:45,2:45,3:15,4:15},0],124:[0,0,4,45,0,0,,,{1:45,2:45,3:15,4:15},0],123:[0,0,4,45,0,0,,,{1:45,2:45,3:15,4:15},0]},,{1:45,2:45,3:15,4:15},0],[[12,12,[]],[33539651,[1,0,1,1,12000,0.00,2904897],[1.98,1.94]],[33539652,[1,0,1,1,5000,-0.25,2904897],[1.62,2.42]],[33539657,[5,0,5,1,3000,0.00,2904897],[40.00,8.75,1.063]],[33539661,[1,0,1,1,3000,0.25,2904897],[2.58,1.55]]],3],[1,25,[2397643,'Dalkurd FF','Elfsborg','1.003-E013118052403',10,'05/25/2018 01:00','',0,2,1,4,{126:[0,2]}],[3,2,9,45,0,0,{311:[0,2,9,45,0,0,,,{1:45,2:45,3:15,4:15},0],310:[0,2,9,45,0,0,,,{1:45,2:45,3:15,4:15},0],309:[0,2,9,45,0,0,,,{1:45,2:45,3:15,4:15},0],308:[0,2,9,45,0,0,,,{1:45,2:45,3:15,4:15},0],307:[0,2,9,45,0,0,,,{1:45,2:45,3:15,4:15},0],306:[0,2,9,45,0,0,,,{1:45,2:45,3:15,4:15},0],126:[1,2,9,45,0,0,,,{1:45,2:45,3:15,4:15},0],124:[0,5,25,45,0,0,,'HT',{1:45,2:45,3:15,4:15},0],123:[0,5,25,45,0,0,,'HT',{1:45,2:45,3:15,4:15},0]},,{1:45,2:45,3:15,4:15},0],[[12,12,[]],[33539693,[1,0,1,1,12000,0.00,2904898],[1.96,1.96]],[33539694,[1,0,1,1,3000,0.25,2904898],[2.51,1.58]],[33539699,[5,0,5,1,3000,0.00,2904898],[40.00,8.00,1.076]],[33539703,[1,0,1,1,5000,-0.25,2904898],[1.60,2.47]]],3],[1,185,[2397836,'Norrby IF','Helsingborgs','1.005-E017618052402',10,'05/25/2018 01:00','',0,1,1,5,{126:[3,8]}],[2,2,12,45,0,0,{311:[0,2,12,45,0,0,,,{1:45,2:45,3:15,4:15},0],310:[0,2,12,45,0,0,,,{1:45,2:45,3:15,4:15},0],309:[0,2,12,45,0,0,,,{1:45,2:45,3:15,4:15},0],308:[0,2,12,45,0,0,,,{1:45,2:45,3:15,4:15},0],307:[0,2,12,45,0,0,,,{1:45,2:45,3:15,4:15},0],306:[0,2,12,45,0,0,,,{1:45,2:45,3:15,4:15},0],126:[1,2,12,45,0,0,,,{1:45,2:45,3:15,4:15},0],124:[0,5,27,45,0,0,,'HT',{1:45,2:45,3:15,4:15},0],123:[0,5,27,45,0,0,,'HT',{1:45,2:45,3:15,4:15},0]},,{1:45,2:45,3:15,4:15},0],[[9,9,[]],[33543669,[1,0,1,1,8000,-0.25,2905385],[1.79,2.14]],[33543670,[1,0,1,1,5000,0.00,2905385],[2.31,1.68]],[33543675,[5,0,5,1,2000,0.00,2905385],[15.00,3.95,1.32]]],2],[1,41184,[2401474,'Levski Sofia','Cherno More Varna','1.014-E039218052401',10,'05/25/2018 01:30','',2,0,1,0,{}],[1,1,40,45,0,0,{},,{1:45,2:45,3:15,4:15},0],[[6,6,[]],[33609782,[1,0,1,1,500,0.75,2917943],[1.91,1.93]],[33609788,[5,0,5,1,500,0.00,2917943],[1.007,9.00,65.00]]],1],[1,253,[2402749,'Valbo FF (n)','Bollnas GIF FF','1.013-E036818052403',10,'05/25/2018 01:30','',3,0,1,0,{}],[1,1,41,45,0,0,{},,{1:45,2:45,3:15,4:15},0],[[4,4,[]],[33628447,[1,0,1,1,500,0.75,2921473],[1.75,2.09]]],1],[1,253,[2402748,'Nosaby IF','Solvesborgs GoIF','1.013-E036818052402',10,'05/25/2018 01:00','',0,0,1,0,{}],[1,2,13,45,0,0,{},,{1:45,2:45,3:15,4:15},0],[[3,3,[]],[33628438,[1,0,1,1,500,0.25,2921472],[1.93,1.91]],[33628444,[5,0,5,1,500,0.00,2921472],[2.42,1.94,5.20]]],1],[1,1002,[2397838,'Hammarby (w)','Linkopings (w)','1.020-E046518052401',10,'05/25/2018 01:00','',1,1,1,0,{}],[1,2,11,45,0,0,{},,{1:45,2:45,3:15,4:15},0],[[3,3,[]],[33543699,[1,0,1,1,1000,-0.25,2905387],[1.92,1.92]],[33543703,[5,0,5,1,500,0.00,2905387],[3.95,2.28,2.33]]],2],[1,1002,[2397839,'Djurgardens (w)','Limhamn Bunkeflo (w)','1.020-E046518052402',10,'05/25/2018 01:00','',1,1,1,0,{}],[1,2,12,45,0,0,{},,{1:45,2:45,3:15,4:15},0],[[4,4,[]],[33543709,[1,0,1,1,500,0.25,2905388],[2.21,1.66]],[33543713,[5,0,5,1,500,0.00,2905388],[2.75,2.24,3.20]],[33543716,[1,0,1,1,1000,0.00,2905388],[1.77,2.07]]],3],[1,9423,[2402729,'Santos Tartu','Maardu Linnameeskond','1.018-E063518052405',10,'05/25/2018 01:30','',1,1,1,0,{}],[1,1,41,45,0,0,{},,{1:45,2:45,3:15,4:15},0],[[4,4,[]],[33628007,[1,0,1,1,500,-0.25,2921447],[2.21,1.66]]],2],[1,13344,[2403180,'VfL Wolfsburg (w) (PEN)','Lyon (w) (PEN)','1.019-E056918052401C',10,'05/24/2018 23:59','',0,0,0,0,{}],[0,0,39,45,0,0,{},,{1:45,2:45,3:15,4:15},0],[[1,1,[]]],3],[1,13344,[2403178,'VfL Wolfsburg (w) (ET)','Lyon (w) (ET)','1.019-E056918052401A',10,'05/24/2018 23:59','',1,3,0,0,{}],[1,3,12,15,1,0,{},,{1:45,2:45,3:15,4:15},0],[[0,0,[[1,0,1],[5,0,5]]]],3],[1,13344,[2403179,'VfL Wolfsburg (w) (PEN)','Lyon (w) (PEN)','1.019-E056918052401B',10,'05/24/2018 23:59','',0,0,0,0,{}],[0,0,39,45,0,0,{},,{1:45,2:45,3:15,4:15},0],[[1,1,[]],[33631825,[1,0,1,1,2000,0.00,2922238],[2.11,1.80]]],3]],[],[]],[2,[[1,23964,[2394606,'Vitoria BA','Sampaio Correa','1.027-E088618052401',10,'05/25/2018 06:00','',0,0,1,,{}],,[[8,8,[]],[33481408,[1,0,1,0,500,1.25,2891087],[1.77,2.07]],[33481413,[5,0,5,0,500,0.00,2891087],[1.31,4.50,7.20]]],1],[1,48952,[2397668,'Estudiantes La Plata','Nacional Montevideo','1.026-E083718052402',10,'05/25/2018 06:15','',0,0,1,12,{}],,[[15,15,[]],[33540336,[1,0,1,0,2000,0.75,2905056],[2.26,1.71]],[33540341,[5,0,5,0,1500,0.00,2905056],[1.94,3.35,3.80]],[33540349,[1,0,1,0,3000,0.50,2905056],[1.95,1.97]]],1],[1,48952,[2397669,'Club Bolivar','Delfin Manta','1.026-E083718052403',10,'05/25/2018 08:30','',0,0,1,17,{}],,[[15,15,[]],[33540351,[1,0,1,0,3000,1.25,2905057],[2.04,1.88]],[33540356,[5,0,5,0,1500,0.00,2905057],[1.43,4.40,6.60]],[33540364,[1,0,1,0,2000,1.00,2905057],[1.75,2.20]]],1],[1,48952,[2397670,'Atletico Nacional Medellin','Colo Colo','1.026-E083718052404',10,'05/25/2018 08:30','',0,0,1,11,{}],,[[15,15,[]],[33540366,[1,0,1,0,3000,0.75,2905058],[1.98,1.94]],[33540371,[5,0,5,0,1500,0.00,2905058],[1.75,3.15,5.20]],[33540379,[1,0,1,0,2000,0.50,2905058],[1.75,2.20]]],1],[1,48952,[2397671,'Corinthians','Millonarios Bogota','1.026-E083718052405',10,'05/25/2018 08:30','',0,0,1,10,{}],,[[15,15,[]],[33540381,[1,0,1,0,2000,1.00,2905059],[2.25,1.72]],[33540386,[5,0,5,0,1500,0.00,2905059],[1.65,3.60,5.20]],[33540394,[1,0,1,0,3000,0.75,2905059],[1.88,2.04]]],1],[1,48952,[2397672,'Independiente','Deportivo Lara','1.026-E083718052406',10,'05/25/2018 08:30','',0,0,1,14,{}],,[[14,14,[]],[33540396,[1,0,1,0,2000,1.75,2905060],[1.77,2.17]],[33540401,[5,0,5,0,1500,0.00,2905060],[1.18,6.20,14.50]],[33540409,[1,0,1,0,3000,2.00,2905060],[2.04,1.88]]],1],[1,48952,[2397667,'Santos','Real Garcilaso','1.026-E083718052401',10,'05/25/2018 06:15','',0,0,1,15,{}],,[[12,12,[]],[33540321,[1,0,1,0,2000,2.25,2905055],[2.04,1.88]],[33540326,[5,0,5,0,1000,0.00,2905055],[1.14,7.60,13.50]]],1],[1,45830,[2402341,'Silkeborg','Esbjerg','1.006-E025718052202',10,'05/25/2018 02:15','',0,0,1,8,{}],,[[15,15,[]],[33623128,[1,0,1,0,5000,0.25,2920737],[1.99,1.93]],[33623132,[5,0,5,0,2000,0.00,2920737],[2.25,3.25,3.05]],[33626433,[1,0,1,0,3000,0.50,2920737],[2.25,1.72]]],1],[1,20077,[2400439,'Virtus Entella','Ascoli Picchio','1.004-E016418052401',10,'05/25/2018 02:30','',0,0,1,3,{}],,[[22,22,[]],[33589633,[1,0,1,0,5000,0.25,2914234],[1.72,2.25]],[33589634,[1,0,1,0,8000,0.50,2914234],[2.03,1.89]],[33589640,[5,0,5,0,3000,0.00,2914234],[2.02,3.00,3.95]],[33642574,[1,0,1,0,3000,0.75,2914234],[2.35,1.66]]],1],[1,3883,[2395481,'Leiknir Reykjavik','IR Reykjavik','1.012-E035218052402',10,'05/25/2018 03:15','',0,0,1,0,{}],,[[8,8,[]],[33507200,[1,0,1,0,2000,0.50,2899248],[2.07,1.81]],[33507204,[5,0,5,0,1000,0.00,2899248],[2.06,3.50,3.25]]],1],[1,3883,[2395480,'Throttur Reykjavik','HK Kopavogur','1.012-E035218052401',10,'05/25/2018 03:15','',0,0,1,0,{}],,[[8,8,[]],[33507187,[1,0,1,0,2000,0.25,2899247],[2.12,1.77]],[33507191,[5,0,5,0,1000,0.00,2899247],[2.46,3.20,2.77]]],1],[1,51287,[2401439,'FUS Rabat (n)','Al Salmiya','1.023-E156418052401',10,'05/25/2018 03:00','',0,0,1,0,{}],,[[7,7,[]],[33607823,[1,0,1,0,500,2.00,2917716],[2.07,1.75]],[33607828,[5,0,5,0,500,0.00,2917716],[1.21,5.80,8.50]]],1]],[],[]]],{1:29,2:27,3:69,4:86,5:18,6:2,7:1,8:42,9:274},{3:41,1:44,7:18,9:17,5:22,8:13,2:16,6:12,4:12,14:8,15:9,99:6,17:0,16:4,13:8,10:8},0]";
}