package com.seosean.showspawntime.modules.features.spawntimes;

import com.seosean.showspawntime.events.ZombiesTickEvent;
import com.seosean.showspawntime.config.MainConfiguration;
import com.seosean.showspawntime.utils.DebugUtils;
import com.seosean.showspawntime.utils.GameUtils;
import com.seosean.showspawntime.utils.LanguageUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SpawnNotice {

    private int currentRound;
    private int[] currentRoundTimes = new int[0];
    private final Minecraft minecraft = Minecraft.getMinecraft();

    public void update(int round) {
        this.currentRound = round;
        this.currentRoundTimes = GameUtils.getRoundTimes(round);
    }

    @SubscribeEvent
    public void onSpawn(ZombiesTickEvent event) {
        if (currentRound == 0 || currentRoundTimes.length == 0) {
            return;
        }

        int tick = event.getTick();

        int finalWaveTime = currentRoundTimes[currentRoundTimes.length - 1];

        if ((MainConfiguration.PlayDEBBSound && (LanguageUtils.getMap().equals(LanguageUtils.ZombiesMap.DEAD_END) || LanguageUtils.getMap().equals(LanguageUtils.ZombiesMap.BAD_BLOOD) || LanguageUtils.getMap().equals(LanguageUtils.ZombiesMap.THE_LAB))) || (MainConfiguration.PlayAASound && LanguageUtils.getMap().equals(LanguageUtils.ZombiesMap.ALIEN_ARCADIUM))) {
            for (int time : currentRoundTimes) {
                if (time * 1000 == tick) {
                    if (finalWaveTime * 1000 == tick) {

                        minecraft.thePlayer.playSound(MainConfiguration.TheLastWave, 2, (float) MainConfiguration.TheLastWavePitch);
                    } else {
//                        DebugUtils.sendMessage(String.valueOf(tick));
                        minecraft.thePlayer.playSound(MainConfiguration.PrecededWave, 2, (float) MainConfiguration.PrecededWavePitch);
                    }
                    return;
                }
            }
        }
        if(MainConfiguration.DEBBCountDown) {
            if (tick == (finalWaveTime - 3) * 1000) {
                Minecraft.getMinecraft().thePlayer.playSound(MainConfiguration.TheLastWave, 2, (float) MainConfiguration.PrecededWavePitch);
            } else if (tick == (finalWaveTime - 2) * 1000) {
                Minecraft.getMinecraft().thePlayer.playSound(MainConfiguration.TheLastWave, 2, (float) MainConfiguration.PrecededWavePitch);
            } else if (tick == (finalWaveTime - 1) * 1000) {
                Minecraft.getMinecraft().thePlayer.playSound(MainConfiguration.TheLastWave, 2, (float) MainConfiguration.PrecededWavePitch);
            }
        }
    }
}
