package com.savik.domain;

public class FlashscoreLeagues {
    public static enum FOOTBALL {
        LA("QVmLl54o"),
        SERIE_A("COuk57Ci"),
        AUSTRALIA_A("OjWW1naM"),
        AUSTRALIA_NPL("lfZXonNq"),
        PORTUGAL_PREMIER("UmMRoGzp"),
        BUNDESLIGA("W6BOzpK2"),
        INDIA_SUPERLIGA("rmioSrer"),
        HOLLAND_1("6Nl8nagD"),
        FRANCE_1("KIShoMk3"),
        FRANCE_2("Y35Jer59"),
        HOLLAND_EREDIVISIE("Or1bBrWD"),
        ENGLAND_PREMIER("dYlOSQOD"),
        GREECE_SUPERLIGA("d2pwJFHI"),
        CYPRUS_1("EmZxlqvh"),
        COSTA_RICA("tpC1pUBH"),
        BELGIUM_A("dG2SqPrf"),
        BELGIUM_B("dMwNgPSC"),
        POLAND_EKSTRAKLASA("lrMHUHDc"),
        AUSTRIA_ERSTE("EPQbW9EF"),
        AUSTRIA_BRISBEN("EBPEFrRQ"),
        ENGLAND_CHAMPIONSHIP("2DSCa5fE"),
        RUSSIA_PREMIER("YacqHHdS"),
        DENMARK_SUPERLIGA("O6W7GIaF"),
        FINLAND_VEIKKAUSLIIGA("lpTTNvTq"),
        GERMANY_BUNDESLIGA_2("tKH71vSe"),
        GERMANY_BUNDESLIGA_3("fNLB0bs2"),
        CHECH_LIGA("hleea1wH"),
        POLAND_1("Qo6off6p"),
        SWITZERLAND_1("jVMQJOHF"),
        CROATIA_1("nqMxclRN"),
        BULGARIA_A("r9Y9rlDt"),
        SWEDEN_SUPERETTAN("bBIP6pm3"),
        EGYPT_PREMIER("xbpjAGxq"),
        IRAN_PRO("2HzmcynI"),
        PERU_PREMIER("KrrdAMyI"),
        JAPAN_J2("vXr8fotG"),
        KOREA_K("0IDCJLlH"),
        NORWAY_ELIT("GOvB22xg"),
        SWEDEN_ALLSVENSKAN("nXxWpLmT"),
        ENGLAND_1("rJSMG3H0"),
        ENGLAND_2("0MwU4NW6");

        String id;

        public String getId() {
            return id;
        }

        FOOTBALL(String id) {
            this.id = id;
        }
    }
}