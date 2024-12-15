package com.seosean.showspawntime.features.spawntimes;

import com.seosean.showspawntime.ShowSpawnTime;
import com.seosean.showspawntime.config.MainConfiguration;
import com.seosean.showspawntime.utils.GameUtils;
import com.seosean.showspawntime.utils.JavaUtils;
import com.seosean.showspawntime.utils.LanguageUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.util.Objects;

public class SpawnTimes {

    public SpawnTimes() {
        this.minecraft = Minecraft.getMinecraft();
        this.fontRenderer = minecraft.fontRenderer;
    }

    public Minecraft minecraft;
    public FontRenderer fontRenderer;
    public int currentRound = 1;
    public int currentWave;
    public int[] roundTimes = new int[0];

    public void setCurrentRound(int round) {
        this.currentRound = round;
        this.roundTimes = GameUtils.getRoundTimes(currentRound);
    }

    public int getCurrentWave() {
        if (roundTimes.length == 0) {
            this.roundTimes = GameUtils.getRoundTimes(currentRound);
        }
        int[] roundTicks = roundTimes.clone();
        for (int i = 0; i < roundTicks.length; i++) {
            roundTicks[i] *= 1000;
        }
        return currentWave = JavaUtils.findInsertPosition(roundTicks, ShowSpawnTime
                .getGameTickHandler()
                .getGameTick());
    }

    public int getWaveTime(int wave) {
        LanguageUtils.ZombiesMap map = LanguageUtils.getMap();
        if (map == null || !JavaUtils.isValidIndex(roundTimes, wave - 1)) {
            return 0;
        }
        int time = roundTimes [wave - 1];
        return time;
    }

    public int getNextWave() {
        return roundTimes.length == this.currentWave ? currentWave : currentWave + 1;
    }

    public int getColor(int wave) {
        if (wave == getNextWave()) {
            if(MainConfiguration.ColorAlert && Objects.equals(LanguageUtils.getMap(), LanguageUtils.ZombiesMap.ALIEN_ARCADIUM)) {
                if (GameUtils.isGiantOnlyWave(currentRound, wave)) {
                    return 0x0099FF;
                } else if (GameUtils.isTo1OnlyWave(currentRound, wave)) {
                    return 0x00FF00;
                } else if (GameUtils.isTo1GiantWave(currentRound, wave)) {
                    return 0xFF0000;
                }
            }
            return 0xFFFF00;
        } else if (wave < getNextWave()) {
            return 0x5A5A5A;
        } else {
            if(MainConfiguration.ColorAlert && Objects.equals(LanguageUtils.getMap(), LanguageUtils.ZombiesMap.ALIEN_ARCADIUM)) {
                if (GameUtils.isGiantOnlyWave(currentRound, wave)) {
                    return 0x663399;
                } else if (GameUtils.isTo1OnlyWave(currentRound, wave)) {
                    return 0x006666;
                } else if (GameUtils.isTo1GiantWave(currentRound, wave)) {
                    return 0x783300;
                }
            }
            return 0x808080;
        }
    }
}
