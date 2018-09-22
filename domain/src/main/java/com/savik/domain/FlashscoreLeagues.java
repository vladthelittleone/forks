package com.savik.domain;

import java.util.Arrays;

public class FlashscoreLeagues {
    public enum FOOTBALL {
        ARGENTINA_SUPERLIGA("naYhNOaA"),
        ARGENTINA_B_METROPOLITANA("GKJrZE7I"),
        ARGENTINA_B_NACIONAL("QTNHA4p4"),
        SPAIN_LA("QVmLl54o"),
        SPAIN_SEGUNDA("vZiPmPJi"),
        BRASIL_A("Yq4hUnzQ"),
        BRASIL_B("vRtLP6rs"),
        ITALY_A("COuk57Ci"),
        ITALY_B("6oug4RRc"),
        AUSTRALIA_A("OjWW1naM"),
        AUSTRALIA_NPL("lfZXonNq"),
        AUSTRIA_BUNDESLIGA("rJg7S7Me"),
        PORTUGAL_PREMIER("UmMRoGzp"),
        BUNDESLIGA("W6BOzpK2"),
        INDIA_SUPERLIGA("rmioSrer"),
        HOLLAND_1("6Nl8nagD"),
        FRANCE_1("KIShoMk3"),
        FRANCE_2("Y35Jer59"),
        FRANCE_NATIONAL("pv9Nf2KF"),
        HOLLAND_EREDIVISIE("Or1bBrWD"),
        ENGLAND_PREMIER("dYlOSQOD"),
        GREECE_SUPERLIGA("d2pwJFHI"),
        CYPRUS_1("EmZxlqvh"),
        //COSTA_RICA("tpC1pUBH"), live ?
        BELGIUM_A("dG2SqPrf"),
        BELGIUM_B("dMwNgPSC"),
        POLAND_EKSTRAKLASA("lrMHUHDc"),
        AUSTRIA_ERSTE("EPQbW9EF"),
        AUSTRALIA_BRISBEN("EBPEFrRQ"),
        ENGLAND_CHAMPIONSHIP("2DSCa5fE"),
        RUSSIA_PREMIER("YacqHHdS"),
        RUSSIA_2("fJMY4hHj"),
        DENMARK_SUPERLIGA("O6W7GIaF"),
        FINLAND_VEIKKAUSLIIGA("lpTTNvTq"),
        GERMANY_BUNDESLIGA_2("tKH71vSe"),
        GERMANY_BUNDESLIGA_3("fNLB0bs2"),
        GERMANY_NORTH("jV4OhMB1"),
        GERMANY_WEST("nsGIGP38"),
        CHECH_LIGA("hleea1wH"),
        SLOVENIA_PRVA("dQIMIN5n"),
        LITHUANIA_A("lAYxY2n8"),
        ESTONIA_MEISTR("hvTioR1s"),
        HUNGARY_NB2("KIZK2ns8"),
        SOUTH_AFRICA_PREMIER("WYFXQ1KH"),
        POLAND_1("Qo6off6p"),
        USA_MLS("CQv5qrFt"),
        ECUADOR_A("WWZFEdUR"),
        SWITZERLAND_CHALLENGE("jVMQJOHF"),
        CROATIA_1("nqMxclRN"),
        BULGARIA_A("r9Y9rlDt"),
        SWEDEN_SUPERETTAN("bBIP6pm3"),
        //EGYPT_PREMIER("xbpjAGxq"), // for live
        IRAN_PRO("2HzmcynI"),
        PERU_PREMIER("KrrdAMyI"),
        JAPAN_J2("vXr8fotG"),
        JAPAN_J1("pAq4eRQ9"),
        KOREA_K("0IDCJLlH"),
        KOREA_NATIONAL("xfzNMq1a"),
        NORWAY_ELIT("GOvB22xg"),
        SWEDEN_ALLSVENSKAN("nXxWpLmT"),
        SWEDEN_1("UZCe41Bj"),
        SCOTLAND_1("l4P6dwRR"),
        SCOTLAND_PREMIER("tGwiyvJ1"),
        BELARUS_PREMIER("ObKhcPDs"),
        TURKEY_SUPERLIGA("Opdcd08Q"),
        TURKEY_1("Spw6Az1D"),
        ICELAND_PREMIER("GdxJccH5"),
        FINLAND_YKKONEN("W8IYMbrj"),
        ROMANIA_1("GILi6JC9"),
        //THAI_PREMIER("hOlyBdW0"),  live ?
        ISRAEL_PREMIER("rHJ2vy1B"),
        RUSSIA_FNL("jizXHcsM"),
        IRELAND_PREMIER("naHiWdnt"),
        ENGLAND_1("rJSMG3H0"),
        ENGLAND_NATIONAL("4CWHXGM1"),
        ENGLAND_NATIONAL_NORTH("rsMtLd7e"),
        ENGLAND_NATIONAL_SOUTH("QPXiMdx8"),
        ENGLAND_2("0MwU4NW6"),
        //PARAGUAY_PREMIER("S66R0t75"),
        //COLOMBIA_A("t0fpnjs5"),
        CHILE_PRIMIER("0KangpCU"),
        MEXICO_PREMIER_A("bm2Vlsfl"),
        SAUDI_PRO("tUxUbLR2"),
        UEFA_CHAMPIONS_GROUP_STAGE("xGrwqq16"),
        UEFA_EUROPE_GROUP_STAGE("ClDjv3V5");

        String id;

        public String getId() {
            return id;
        }

        public static FOOTBALL getById(String id) {
            return Arrays.asList(values()).stream().filter(l -> id.equalsIgnoreCase(l.getId())).findFirst().get();
        } 

        FOOTBALL(String id) {
            this.id = id;
        }
    }
}
