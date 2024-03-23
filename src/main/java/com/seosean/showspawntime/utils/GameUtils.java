package com.seosean.showspawntime.utils;

import com.seosean.showspawntime.ShowSpawnTime;
import com.seosean.showspawntime.handler.ScoreboardManager;
import net.minecraft.entity.monster.EntityZombie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameUtils {

    private static final Map<Integer, List<Integer>> o1RoundsToWaves = new HashMap<>();
    private static final Map<Integer, List<Integer>> giantRoundsToWaves = new HashMap<>();
    private static final Map<Integer, List<Integer>> o1AndGiantRoundsToWaves = new HashMap<>();

    static {
        o1RoundsToWaves.put(40, Arrays.asList(5));
        o1RoundsToWaves.put(45, Arrays.asList(3, 4));
        o1RoundsToWaves.put(46, Arrays.asList(4));
        o1RoundsToWaves.put(48, Arrays.asList(4));
        o1RoundsToWaves.put(54, Arrays.asList(5));
        o1RoundsToWaves.put(55, Arrays.asList(6));
        o1RoundsToWaves.put(58, Arrays.asList(5));
        o1RoundsToWaves.put(59, Arrays.asList(1, 2, 3, 4, 5, 6));
        o1RoundsToWaves.put(60, Arrays.asList(3, 4));
        o1RoundsToWaves.put(64, Arrays.asList(5, 6));
        o1RoundsToWaves.put(67, Arrays.asList(6));
        o1RoundsToWaves.put(68, Arrays.asList(5, 6));
        o1RoundsToWaves.put(69, Arrays.asList(5, 6));
        o1RoundsToWaves.put(70, Arrays.asList(2, 3));
        o1RoundsToWaves.put(74, Arrays.asList(4, 5, 6));
        o1RoundsToWaves.put(77, Arrays.asList(6));
        o1RoundsToWaves.put(78, Arrays.asList(5, 6));
        o1RoundsToWaves.put(79, Arrays.asList(5, 6));
        o1RoundsToWaves.put(80, Arrays.asList(2, 3));
        o1RoundsToWaves.put(84, Arrays.asList(4, 5, 6));
        o1RoundsToWaves.put(87, Arrays.asList(6));
        o1RoundsToWaves.put(88, Arrays.asList(5,6));
        o1RoundsToWaves.put(89, Arrays.asList(5,6));
        o1RoundsToWaves.put(90, Arrays.asList(2,3));
        o1RoundsToWaves.put(94, Arrays.asList(4,5,6));
        o1RoundsToWaves.put(97, Arrays.asList(6));
        o1RoundsToWaves.put(98, Arrays.asList(5,6));
        o1RoundsToWaves.put(99, Arrays.asList(5,6));
        o1RoundsToWaves.put(100, Arrays.asList(2,3));

        giantRoundsToWaves.put(15, Arrays.asList(6));
        giantRoundsToWaves.put(20, Arrays.asList(3, 5));
        giantRoundsToWaves.put(22, Arrays.asList(4, 6));
        giantRoundsToWaves.put(24, Arrays.asList(2, 4, 6));
        giantRoundsToWaves.put(30, Arrays.asList(1, 2, 3));
        giantRoundsToWaves.put(36, Arrays.asList(2, 3));
        giantRoundsToWaves.put(37, Arrays.asList(2, 3));
        giantRoundsToWaves.put(38, Arrays.asList(2, 3));
        giantRoundsToWaves.put(39, Arrays.asList(2 ,3));
        giantRoundsToWaves.put(40, Arrays.asList(2, 3));
        giantRoundsToWaves.put(41, Arrays.asList(2, 3));
        giantRoundsToWaves.put(42, Arrays.asList(1, 2, 3));
        giantRoundsToWaves.put(43, Arrays.asList(2, 4, 6));
        giantRoundsToWaves.put(44, Arrays.asList(1, 2, 3));
        giantRoundsToWaves.put(45, Arrays.asList(2));
        giantRoundsToWaves.put(47, Arrays.asList(3));
        giantRoundsToWaves.put(50, Arrays.asList(2, 4));
        giantRoundsToWaves.put(51, Arrays.asList(2, 4));
        giantRoundsToWaves.put(52, Arrays.asList(2, 4));
        giantRoundsToWaves.put(53, Arrays.asList(2, 4));
        giantRoundsToWaves.put(54, Arrays.asList(4));
        giantRoundsToWaves.put(55, Arrays.asList(1, 2, 3, 4));
        giantRoundsToWaves.put(58, Arrays.asList(4));
        giantRoundsToWaves.put(65, Arrays.asList(4, 5, 6));
        giantRoundsToWaves.put(75, Arrays.asList(4, 5, 6));
        giantRoundsToWaves.put(85, Arrays.asList(4, 5, 6));
        giantRoundsToWaves.put(95, Arrays.asList(4, 5, 6));

        o1AndGiantRoundsToWaves.put(54,Arrays.asList(2));
        o1AndGiantRoundsToWaves.put(55, Arrays.asList(5));
        o1AndGiantRoundsToWaves.put(58, Arrays.asList(2));
        o1AndGiantRoundsToWaves.put(70, Arrays.asList(4, 5, 6));
        o1AndGiantRoundsToWaves.put(80, Arrays.asList(4, 5, 6));
        o1AndGiantRoundsToWaves.put(90, Arrays.asList(4, 5, 6));
        o1AndGiantRoundsToWaves.put(100, Arrays.asList(4, 5, 6));
    }

    public static int[] getBossRounds() {
        switch (LanguageUtils.getMap()) {
            case ALIEN_ARCADIUM: return new int[]{25, 35, 56, 57, 101};
            case DEAD_END: return new int[]{5, 10, 15, 20, 25, 30};
            case BAD_BLOOD: return new int[]{10, 15, 20, 25, 30};
            case THE_LAB: return new int[]{5, 10, 15, 20, 25, 30, 35, 40};
        }
        return new int[]{};
    }

    public static int[][] getGlobalTimers() {
        LanguageUtils.ZombiesMap map = LanguageUtils.getMap();
        if (map != null) {
            return map.getTimer().clone();
        }
        return new int[0][];
    }

    public static int getWaveTime(int round, int wave) {
        LanguageUtils.ZombiesMap map = LanguageUtils.getMap();
        if (map == null || !JavaUtils.isValidIndex(map.getTimer(), round - 1, wave - 1)) {
            return 0;
        }
        return map.getTimer()[round - 1] [wave - 1];
    }

    public static int[] getRoundTimes(int round) {
        LanguageUtils.ZombiesMap map = LanguageUtils.getMap();
        if (map.equals(LanguageUtils.ZombiesMap.NULL) || !JavaUtils.isValidIndex(map.getTimer(), round - 1, 0)) {
            return new int[0];
        }
        return map.getTimer()[round - 1].clone();
    }

    public static int getCurrentZombiesTimeTick() {
        return ShowSpawnTime.getGameTickHandler().getGameTick();
    }

    public static int getCurrentWave(int currentRound) {
        int[] roundTicks = getRoundTimes(currentRound);
        for (int i = 0; i < roundTicks.length; i++) {
            roundTicks[i] *= 1000;
        }
        return JavaUtils.findInsertPosition(roundTicks, ShowSpawnTime.getGameTickHandler().getGameTick());
    }

    public static List<Integer> getGiantOnlyWaves(int round) {
        List<Integer> waveList = new ArrayList<>();
        if (giantRoundsToWaves.get(round) != null) {
            waveList = giantRoundsToWaves.get(round);
        }
        return waveList;
    }

    public static List<Integer> getO1OnlyWaves(int round) {
        List<Integer> waveList = new ArrayList<>();
        if (o1RoundsToWaves.get(round) != null) {
            waveList = o1RoundsToWaves.get(round);
        }
        return waveList;
    }

    public static List<Integer> getO1AndGiantWaves(int round) {
        List<Integer> waveList = new ArrayList<>();
        if (o1AndGiantRoundsToWaves.get(round) != null) {
            waveList = o1AndGiantRoundsToWaves.get(round);
        }
        return waveList;
    }

    public static boolean isGiantOnlyWave(int round, int wave) {
        return getGiantOnlyWaves(round).contains(wave);
    }

    public static boolean isTo1OnlyWave(int round, int wave) {
        return getO1OnlyWaves(round).contains(wave);
    }

    public static boolean isTo1GiantWave(int round, int wave) {
        return getO1AndGiantWaves(round).contains(wave);
    }

    public static List<String> playerList = new ArrayList<>();
    public static void initializePlayerList() {
        ScoreboardManager scoreboardManager = ShowSpawnTime.getScoreboardManager();
        playerList = new ArrayList<>();
        for (int i = 6; i <= scoreboardManager.getSize(); i ++) {
            String content = scoreboardManager.getContent(i);
            if (content.startsWith(" ") || content.isEmpty()) {
                break;
            }
            String colon = content.contains(":") ? ":" : (scoreboardManager.getContent(i).contains("：") ? "：" : "");

            if (colon.isEmpty()) {
                break;
            }

            playerList.add(content.split(colon)[0]);
        }
    }
}
