package com.seosean.showspawntime.modules.features.timerecorder;

import com.seosean.showspawntime.ShowSpawnTime;
import com.seosean.showspawntime.config.MainConfiguration;
import com.seosean.showspawntime.utils.GameUtils;
import com.seosean.showspawntime.utils.JavaUtils;
import com.seosean.showspawntime.utils.LanguageUtils;
import com.seosean.showspawntime.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.lang3.ArrayUtils;

import java.text.DecimalFormat;

public class TimeRecorder {

    public static int recordedRound = 0;
    public static void recordGameTime(String titleText) {
        boolean record = false;
        int[] skipRound;

        LanguageUtils.ZombiesMap zombiesMap = LanguageUtils.getMap();

        if (zombiesMap.equals(LanguageUtils.ZombiesMap.NULL)) {
            return;
        }

        boolean isAAMap = LanguageUtils.getMap().equals(LanguageUtils.ZombiesMap.ALIEN_ARCADIUM);

        String roundsRecordToggle = isAAMap ? MainConfiguration.AARoundsRecordToggle : MainConfiguration.DEBBRoundsRecordToggle;

        if (roundsRecordToggle.contains("OFF")) {
            return;
        }

        int maxRound = isAAMap ? 105 : (zombiesMap.equals(LanguageUtils.ZombiesMap.THE_LAB) ? 40 : 30);

        for (int i = maxRound; i > 0; i--) {
            if (titleText.contains(String.valueOf(i))) {
                recordedRound = i;

                int increment = 0;
                if (isAAMap ? MainConfiguration.AARoundsRecordToggle.contains("Quintuple") : MainConfiguration.DEBBRoundsRecordToggle.contains("Quintuple")) {
                    increment = 5;
                } else if (isAAMap ? MainConfiguration.AARoundsRecordToggle.contains("Tenfold") : MainConfiguration.DEBBRoundsRecordToggle.contains("Tenfold")) {
                    increment = 10;
                }

                if (increment != 0 && i % increment != 1) {
                    return;
                }

                record = true;
                break;
            }
        }

        skipRound = LanguageUtils.getMap().equals(LanguageUtils.ZombiesMap.ALIEN_ARCADIUM) ?
                new int[]{0, 1, 11, 21, 106} : new int[]{0, 1, 11, 21, 31, 41};

        if (record && !ArrayUtils.contains(skipRound, recordedRound)) {
            try {
                String time = ShowSpawnTime.getScoreboardManager().getContent(ShowSpawnTime.getScoreboardManager().getSize() - 3);
                String[] split;
                if(time.contains("：")){
                    split = time.split("：");
                }else{
                    split = time.split(" ");
                }

                IChatComponent crossBar = new ChatComponentText(EnumChatFormatting.GREEN.toString() + EnumChatFormatting.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬ ");
                IChatComponent timing = new ChatComponentText(EnumChatFormatting.YELLOW + "                     You completed " + EnumChatFormatting.RED + "Round " + (recordedRound - 1) + EnumChatFormatting.YELLOW + " in " + EnumChatFormatting.GREEN + split[1] + EnumChatFormatting.YELLOW + "!");
                IChatComponent copy = new ChatComponentText(EnumChatFormatting.GREEN + "Copy");
                timing.setChatStyle(new ChatStyle().setChatHoverEvent(new HoverEvent(net.minecraft.event.HoverEvent.Action.SHOW_TEXT, copy)).setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sst copy " + "You completed Round " + (recordedRound - 1) + " in " + split[1].replace("§a", "").replace("\uD83D\uDC79", "") + "!")));
                Minecraft.getMinecraft().thePlayer.addChatComponentMessage(crossBar);
                Minecraft.getMinecraft().thePlayer.addChatComponentMessage(timing);
                Minecraft.getMinecraft().thePlayer.addChatComponentMessage(crossBar);
            } catch (Exception e) {
                IChatComponent warning = new ChatComponentText(EnumChatFormatting.RED + "CANNOT RECORD THE TIME, PLEASE REPORT THIS TO Seosean");
                Minecraft.getMinecraft().thePlayer.addChatComponentMessage(warning);
            }
        }
    }

    public static void recordRedundantTime(boolean isGameOver) {
        int lastRoundTime = ShowSpawnTime.getInstance().getGameTickHandler().getGameTick();
        double redundantLastRoundTime;
        if (MainConfiguration.CleanUpTimeToggle) {
            if (PlayerUtils.isInZombies()) {
                LanguageUtils.ZombiesMap zombiesMap = LanguageUtils.getMap();
                if (zombiesMap.equals(LanguageUtils.ZombiesMap.NULL)) {
                    return;
                }

                int[][] timer = zombiesMap.getTimer();
                int maxRound = zombiesMap.getMaxRound();
                int currentRound = isGameOver ? maxRound : ShowSpawnTime.getSpawnTimes().currentRound - 1;

                if (currentRound == 0) {
                    return;
                }

                if (!JavaUtils.isValidIndex(timer, currentRound - 1, timer[currentRound - 1].length - 1)) {
                    return;
                }

                redundantLastRoundTime = lastRoundTime / 1000.0 - timer[currentRound - 1][timer[currentRound - 1].length - 1];

                IChatComponent redundantTimeTips = new ChatComponentText(EnumChatFormatting.YELLOW + "You took " + EnumChatFormatting.RED.toString() + EnumChatFormatting.BOLD + ((redundantLastRoundTime < 0) ? "--" : new DecimalFormat("#.##").format(redundantLastRoundTime)) + EnumChatFormatting.YELLOW + " seconds to clean up after the last wave.");
                IChatComponent copy = new ChatComponentText(EnumChatFormatting.GREEN + "Copy");
                redundantTimeTips.setChatStyle(new ChatStyle().setChatHoverEvent(new HoverEvent(net.minecraft.event.HoverEvent.Action.SHOW_TEXT, copy)).setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sst copy " + "You took " + ((redundantLastRoundTime < 0) ? "--" : new DecimalFormat("#.##").format(redundantLastRoundTime)) + " seconds to clean up after the last wave.")));
                Minecraft.getMinecraft().thePlayer.addChatComponentMessage(redundantTimeTips);

            }
        }
    }
}
