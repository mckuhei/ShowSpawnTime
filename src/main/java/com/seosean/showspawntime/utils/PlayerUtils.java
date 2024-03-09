package com.seosean.showspawntime.utils;

import com.seosean.showspawntime.ShowSpawnTime;

public class PlayerUtils {
    public static boolean isInZombiesTitle() {
        return LanguageUtils.isZombiesTitle(ShowSpawnTime.getScoreboardManager().getTitle());
    }
    public static boolean isInZombies() {
        return LanguageUtils.isZombiesTitle(ShowSpawnTime.getScoreboardManager().getTitle()) && LanguageUtils.isZombiesLeft(ShowSpawnTime.getScoreboardManager().getContent(4));
    }
}
