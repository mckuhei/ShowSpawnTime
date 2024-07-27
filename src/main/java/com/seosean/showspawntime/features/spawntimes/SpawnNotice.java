package com.seosean.showspawntime.features.spawntimes;

import com.seosean.showspawntime.config.MainConfiguration;
import com.seosean.showspawntime.events.NoticeSoundEvent;
import com.seosean.showspawntime.utils.GameUtils;
import com.seosean.showspawntime.utils.LanguageUtils;
import com.seosean.showspawntime.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SpawnNotice {

    private static int currentRound;
    private static int[] currentRoundTimes = new int[0];
    private static final Minecraft minecraft = Minecraft.getMinecraft();

    public static void update(int round) {
        currentRound = round;
        currentRoundTimes = GameUtils.getRoundTimes(round);
    }

    public static void onSpawn(int tick) {
        if (currentRound == 0 || currentRoundTimes.length == 0) {
            if (currentRound != 0) {
                currentRoundTimes = GameUtils.getRoundTimes(currentRound);
            }
            if (currentRoundTimes.length == 0) {
                return;
            }
        }
        EntityPlayerSP player = minecraft.thePlayer;
        if (player == null) {
            return;
        }

        int finalWaveTime = currentRoundTimes[currentRoundTimes.length - 1];

        if ((MainConfiguration.PlayDEBBSound && (LanguageUtils.getMap().equals(LanguageUtils.ZombiesMap.DEAD_END) || LanguageUtils.getMap().equals(LanguageUtils.ZombiesMap.BAD_BLOOD) || LanguageUtils.getMap().equals(LanguageUtils.ZombiesMap.THE_LAB) || LanguageUtils.getMap().equals(LanguageUtils.ZombiesMap.PRISON))) || (MainConfiguration.PlayAASound && LanguageUtils.getMap().equals(LanguageUtils.ZombiesMap.ALIEN_ARCADIUM))) {
            for (int time : currentRoundTimes) {
                if (time * 1000 == tick) {
                    if (finalWaveTime * 1000 == tick) {
                        PlayerUtils.playSound(MainConfiguration.TheLastWaveSound, (float) MainConfiguration.TheLastWavePitch);
                    } else {
                        PlayerUtils.playSound(MainConfiguration.PrecededWaveSound, (float) MainConfiguration.PrecededWavePitch);
                    }
                    return;
                }
            }
        }

        if(MainConfiguration.DEBBCountDown) {
            if (tick == (finalWaveTime - 3) * 1000) {
                PlayerUtils.playSound(MainConfiguration.CountDownSound, (float) MainConfiguration.CountDownPitch);
            } else if (tick == (finalWaveTime - 2) * 1000) {
                PlayerUtils.playSound(MainConfiguration.CountDownSound, (float) MainConfiguration.CountDownPitch);
            } else if (tick == (finalWaveTime - 1) * 1000) {
                PlayerUtils.playSound(MainConfiguration.CountDownSound, (float) MainConfiguration.CountDownPitch);
            }
        }
    }

    @SubscribeEvent
    public void onSoundPlay(NoticeSoundEvent event) {
        Minecraft.getMinecraft().thePlayer.playSound(event.getSound(), 1.0F, event.getPitch());
    }
}
