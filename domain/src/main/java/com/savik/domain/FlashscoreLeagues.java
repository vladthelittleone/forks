package com.savik.domain;

import java.util.Arrays;

public class FlashscoreLeagues {
    public static enum FOOTBALL {
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
        //CYPRUS_1("EmZxlqvh"),
        //COSTA_RICA("tpC1pUBH"),
        BELGIUM_A("dG2SqPrf"),
        BELGIUM_B("dMwNgPSC"),
        POLAND_EKSTRAKLASA("lrMHUHDc"),
        AUSTRIA_ERSTE("EPQbW9EF"),
        //AUSTRALIA_BRISBEN("EBPEFrRQ"),
        ENGLAND_CHAMPIONSHIP("2DSCa5fE"),
        RUSSIA_PREMIER("YacqHHdS"),
        DENMARK_SUPERLIGA("O6W7GIaF"),
        FINLAND_VEIKKAUSLIIGA("lpTTNvTq"),
        GERMANY_BUNDESLIGA_2("tKH71vSe"),
        GERMANY_BUNDESLIGA_3("fNLB0bs2"),
        CHECH_LIGA("hleea1wH"),
        POLAND_1("Qo6off6p"),
        SWITZERLAND_CHALLENGE("jVMQJOHF"),
        CROATIA_1("nqMxclRN"),
        //BULGARIA_A("r9Y9rlDt"),
        SWEDEN_SUPERETTAN("bBIP6pm3"),
        //EGYPT_PREMIER("xbpjAGxq"),
        IRAN_PRO("2HzmcynI"),
        //PERU_PREMIER("KrrdAMyI"),
        JAPAN_J2("vXr8fotG"),
        JAPAN_J1("pAq4eRQ9"),
        KOREA_K("0IDCJLlH"),
        //KOREA_NATIONAL("xfzNMq1a"),
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
        ISRAEL_PREMIER("rHJ2vy1B"),
        //RUSSIA_FNL("jizXHcsM"),
        ENGLAND_1("rJSMG3H0"),
        ENGLAND_NATIONAL("4CWHXGM1"),
        ENGLAND_NATIONAL_NORTH("rsMtLd7e"),
        ENGLAND_NATIONAL_SOUTH("QPXiMdx8"),
        ENGLAND_2("0MwU4NW6");

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
