package com.seosean.showspawntime.utils;

import com.seosean.showspawntime.ShowSpawnTime;
import com.seosean.showspawntime.events.PowerupSpawnEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LanguageUtils {
    private static List<String> ZOMBIES_TITLE = Arrays.asList("ZOMBIES", "僵尸末日", "殭屍末日");
    private static List<String> ZOMBIES_LEFT = Arrays.asList("Zombies Left", "剩下殭屍數", "剩下殭屍數", "Zbývající zombie", "Zombier tilbage", "Zombies over", "Zombeja jäljellä", "Zombies restants", "Zombies übrig", "Ζόμπι που Απομένουν", "Hátralévő Zombik", "Zombi Rimanenti", "残りゾンビ", "남은 좀비", "Zombier igjen", "Zombies Left", "Pozostałe zombi", "Zombies restantes", "Zumbis restantes", "Zombi Rămași", "Осталось зомби", "Zombies Restantes", "Zombier kvar", "Kalan Zombi", "Залишилося зомбі");
    private static List<String> ROUND = Arrays.asList("回合", ". kolo", "Runde", "Ronde", "Kierros", "Manche", "Runde", "Γύρος", ". Kör", "Round", "ラウンド", "라운드", "Runde", "Runda", "Horda", "Rodada", "Runda", "Раунд", "Ronda", "Runda", ". Tur", "Раунд");
    private static List<String> MAP_THE_LAB = Arrays.asList("The Lab");
    private static List<String> MAP_DEAD_END = Arrays.asList("穷途末路", "窮途末路", "Dead End");
    private static List<String> MAP_BAD_BLOOD = Arrays.asList("坏血之宫", "壞血宮殿", "Bad Blood");
    private static List<String> MAP_ALIEN_ARCADIUM = Arrays.asList("外星游乐园", "外星遊樂園", "Alien Arcadium");
    private static List<String> MAPS = Arrays.asList("穷途末路", "窮途末路", "Dead End", "坏血之宫", "壞血宮殿", "Bad Blood", "外星游乐园", "外星遊樂園", "Alien Arcadium", "The Lab");
    private static Map<String, List<String>> POWERUPS_PATTERN_UPPER;
    private static Map<String, List<String>> POWERUPS_PATTERN_LOWER;

    static {
        POWERUPS_PATTERN_UPPER = new HashMap<>();
        POWERUPS_PATTERN_UPPER.put("INSTA_KILL", Arrays.asList("INSTA KILL", "瞬间击杀", "一擊必殺", "OKAMŽITÉ ZABITÍ", "ØJEBLIKKELIGT DRAB", "ONMIDDELLIJKE DOOD", "VÄLITÖN TAPPO", "KILL INSTANTANE", "SOFORTIGER KILL", "ΣΤΙΓΜΙΑΙΑ ΕΞΟΝΤΩΣΗ", "ΣΤΙΓΜΙΑΙΑ ΕΞΟΝΤΩΣΗ", "UCCISIONE ISTANTANEA", "インスタキル", "즉시 처치", "UMIDDELBART DRAP", "INSTA SCUTTLE", "NATYCHMIASTOWE ZABÓJSTWO", "KILL IMEDIATA", "ABATE INSTANTÂNEO", "INSTA-CIDERE", "МОМЕНТ. УБИЙСТВО", "MUERTE INSTANTÁNEA", "DIREKT KILL", "ANİ ÖLDÜRME", "МИТТЄВЕ ВБИВСТВО"));
        POWERUPS_PATTERN_UPPER.put("MAX_AMMO", Arrays.asList("MAX AMMO", "弹药满载", "填滿彈藥", "MAXIMÁLNÍ MUNICE", "MAKS AMMUNITION", "MAXIMALE MUNITIE", "TÄYSI LIPAS", "MUNITIONS MAX", "MAXIMALE MUNITION", "ΜΕΓΙΣΤΑ ΠΥΡΟΜΑΧΙΚΑ", "MAX LŐSZER", "MUNIZIONI AL MASSIMO", "マックスアモ", "탄약 충전", "MAKS. AMMUNISJON", "Maksymalna ilość amunicji", "MUNIÇÃO MÁXIMA", "MUNIÇÃO MÁXIMA", "MUNIȚIE MAXIMĂ", "МАКС. БОЕПРИПАСОВ", "MUNICION MAXIMA", "MAX. AMMONUTION", "MAKSİMUM CEPHANE", "МАКС. НАБОЇВ"));
        POWERUPS_PATTERN_UPPER.put("DOUBLE_GOLD", Arrays.asList("DOUBLE GOLD", "双倍金钱", "雙倍金幣", "DVOJNÁSOBNÉ ZLATO", "DOBBELT GULD", "DUBBEL GOLD", "TUPLAKULTA", "DOUBLE OR", "DOPPELTES GOLD", "ΔΙΠΛΑΣΙΟΣ ΧΡΥΣΟΣ", "DUPLA ARANY", "ORO DOPPIO", "ダブルゴールド", "더블 골드", "DOBBELGULL", "DOUBLE DOUBLOONS", "PODWOJONE ZŁOTO", "OURO A DOBRAR", "OURO EM DOBRO", "AUR DUBLU", "УДВОЕННОЕ ЗОЛОТО", "ORO DOBLE", "DUBBELGULD", "ÇİFTE ALTIN", "ПОДВІЙНЕ ЗОЛОТО"));
    }
    public static boolean isZombiesLeft(String string) {
        string = StringUtils.trim(string);
        if (string.contains(":")) {
            string = string.split(":")[0];
        } else if (string.contains("：")) {
            string = string.split("：")[0];
        }
        return ZOMBIES_LEFT.contains(string.trim());
    }

    public static boolean isZombiesTitle(String string) {
        string = StringUtils.trim(string);
        return ZOMBIES_TITLE.contains(string.trim());
    }

    public static boolean isRoundTitle(String string) {
        string = StringUtils.trim(string);
        for (String s : ROUND) {
            if (string.trim().contains(s)) {
                return true;
            }
        }
        return false;
    }

    public static int getRoundNumber(String string) {
        string = StringUtils.trim(string).trim();
        for (String s : ROUND) {
            if (string.contains(s)) {
                return StringUtils.getNumberInString(string);
            }
        }
        return 0;
    }

    public static String getMapString() {
        String mapString = ShowSpawnTime.getScoreboardManager().getContent(ShowSpawnTime.getScoreboardManager().getSize() - 2);

        for (String regex : MAPS) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(mapString);

            if (matcher.find()) {
                return matcher.group();
            }
        }
        return "";
    }

    public static ZombiesMap getMap() {
        HashMap<String, List<String>> mapLists = new HashMap<>();
        mapLists.put("MAP_THE_LAB", MAP_THE_LAB);
        mapLists.put("MAP_DEAD_END", MAP_DEAD_END);
        mapLists.put("MAP_BAD_BLOOD", MAP_BAD_BLOOD);
        mapLists.put("MAP_ALIEN_ARCADIUM", MAP_ALIEN_ARCADIUM);
        String mapName = LanguageUtils.getMapString();
        if (mapName.contains(":")) {
            mapName = mapName.split(":")[1].trim();
        } else if (mapName.contains("：")) {
            mapName = mapName.split("：")[1].trim();
        }
        String listName;

        for (Map.Entry<String, List<String>> entry : mapLists.entrySet()) {
            if (entry.getValue().contains(mapName)) {
                listName = entry.getKey();
                switch (listName) {
                    case "MAP_THE_LAB": return ZombiesMap.THE_LAB;
                    case "MAP_DEAD_END": return ZombiesMap.DEAD_END;
                    case "MAP_BAD_BLOOD": return ZombiesMap.BAD_BLOOD;
                    case "MAP_ALIEN_ARCADIUM": return ZombiesMap.ALIEN_ARCADIUM;
                }
                break;
            }
        }
        return ZombiesMap.NULL;
    }


    public enum ZombiesMap {
        NULL(new int[][]{}, 0),
        THE_LAB(LanguageUtils.THE_LAB_WAVE_TIME, 40),
        DEAD_END(LanguageUtils.DEAD_END_WAVE_TIME, 30),
        BAD_BLOOD(LanguageUtils.BAD_BLOOD_WAVE_TIME, 30),
        ALIEN_ARCADIUM(LanguageUtils.ALIEN_ARCADIUM_WAVE_TIME, 105);

        private final int[][] timer;
        private final int maxRound;
        ZombiesMap(int[][] timer, int maxRound) {
            this.timer = timer;
            this.maxRound = maxRound;
        }
        
        public int[][] getTimer() {
            return timer.clone();
        }

        public int getMaxRound() {
            return maxRound;
        }
    }

    public static final int[][] THE_LAB_WAVE_TIME = {{10,22},{10,22},{10,22},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34}};
    public static final int[][] DEAD_END_WAVE_TIME = {{10,20},{10,20},{10,20,35},{10,20,35},{10,22,37},{10,22,44},{10,25,47},{10,25,50},{10,22,38},{10,24,45},{10,25,48},{10,25,50},{10,25,50},{10,25,45},{10,25,46},{10,24,47},{10,24,47},{10,24,47},{10,24,47},{10,24,49},{10,23,44},{10,23,45},{10,23,42},{10,23,43},{10,23,43},{10,23,36},{10,24,44},{10,24,42},{10,24,42},{10,24,45}};
    public static final int[][] BAD_BLOOD_WAVE_TIME = {{10,22},{10,22},{10,22},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,22,34},{10,24,38},{10,24,38},{10,22,34},{10,24,38},{10,22,34}};
    public static final int[][] ALIEN_ARCADIUM_WAVE_TIME = {{10,13,16,19},{10,14,18,22},{10,13,16,19},{10,14,17,21,25,28},{10,14,18,22,26,30},{10,14,19,23,28,32},{10,15,19,23,27,31},{10,15,20,25,30,35},{10,14,19,23,28,32},{10,16,22,27,33,38},{10,16,21,27,32,38},{10,16,22,28,34,40},{10,16,22,28,34,40},{10,16,21,26,31,36},{10,17,24,31,38,46},{10,16,22,27,33,38},{10,14,19,23,28,32},{10,14,19,23,28,32},{10,14,18,22,26,30},{10,15,21,26,31,36},{10,14,19,23,28,32},{10,14,19,23,28,34},{10,14,18,22,26,30},{10,14,19,23,28,32},{10},{10,23,36},{10,22,34},{10,20,30},{10,24,38},{10,22,34},{10,22,34},{10,21,32},{10,22,34},{10,22,34},{10},{10,22,34},{10,20,31},{10,22,34},{10,22,34},{10,22,34,37,45},{10,21,32},{10,22,34},{10,13,22,25,34,37},{10,22,34},{10,22,34,35},{10,21,32,35},{10,20,30},{10,20,30,33},{10,21,32},{10,22,34,37},{10,20,30,33},{10,22,34,37},{10,22,34,37},{10,20,32,35,39},{10,16,22,28,34,40},{10,14,18},{10,14,18},{10,22,34,37,38},{10,14,18,22,26,30},{10,20,30,33},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,27,32},{10,14,18,22,27,32},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{10,14,18,22,26,30},{5},{5},{5},{5},{5}};

}
