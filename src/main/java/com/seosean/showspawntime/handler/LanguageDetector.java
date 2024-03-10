package com.seosean.showspawntime.handler;

import com.seosean.showspawntime.utils.LanguageUtils;

public class LanguageDetector {

    public static String cacheLanguage = "EN_US";
    public static String language = "EN_US";

    public static void detectLanguage() {
        String zombiesLeftText = LanguageUtils.getZombiesLeftText();
        String cacheLang = language;
        switch (zombiesLeftText) {
            case "Zombies Left": language = "EN_US"; break;
            case "剩余僵尸": language = "ZH_CN"; break;
            case "剩下殭屍數": language = "ZH_TW"; break;
            case "Zbývající zombie": language = "CS"; break;
            case "Zombier tilbage": language = "DA"; break;
            case "Zombies over": language = "NL"; break;
            case "Zombeja jäljellä": language = "FI"; break;
            case "Zombies restants": language = "FR"; break;
            case "Zombies übrig": language = "DE"; break;
            case "Ζόμπι που Απομένουν": language = "EL"; break;
            case "Hátralévő Zombik": language = "HU"; break;
            case "Zombi Rimanenti": language = "IT"; break;
            case "残りゾンビ": language = "JA"; break;
            case "남은 좀비": language = "KO"; break;
            case "Zombier igjen": language = "NO"; break;
            case "Pozostałe zombi": language = "PL"; break;
            case "Zombies restantes": language = "PT_PT"; break;
            case "Zumbis restantes": language = "PT_BR"; break;
            case "Zombi Rămași": language = "RO"; break;
            case "Осталось зомби": language = "RU"; break;
            case "Zombies Restantes": language = "ES_ES"; break;
            case "Zombier kvar": language = "SV_SE"; break;
            case "Kalan Zombi": language = "TR"; break;
            case "Залишилося зомбі": language = "UK"; break;
        }

        if (!language.equals(cacheLang)) {
            LanguageUtils.resetCache();
            cacheLanguage = language;
        }
    }
}
