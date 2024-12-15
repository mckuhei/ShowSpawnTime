package com.seosean.showspawntime.features.powerups;

import com.seosean.showspawntime.ShowSpawnTime;
import com.seosean.showspawntime.utils.PlayerUtils;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class PowerupPredict {

    public static void detectNextPowerupRound() {

        int round = ShowSpawnTime.getSpawnTimes().currentRound;
        if (round <= 0) {
            return;
        }
        ITextComponent basic = new TextComponentString(TextFormatting.GOLD + "[" + TextFormatting.WHITE + "ShowSpawnTime" + TextFormatting.GOLD + "] ");
        List<ITextComponent> chatBox = new ArrayList<>();
        Integer[][] rounds = {{}, {}, {}}; // insRounds, maxRounds, ssRounds

        PowerupDetect powerUpDetect = ShowSpawnTime.getPowerupDetect();
        List<Integer> insRounds = powerUpDetect.insRounds;
        List<Integer> maxRounds = powerUpDetect.maxRounds;
        List<Integer> ssRounds = powerUpDetect.ssRounds;

        rounds[0] = insRounds.toArray(new Integer[0]);
        rounds[1] = maxRounds.toArray(new Integer[0]);
        rounds[2] = ssRounds.toArray(new Integer[0]);

        for (int i = 0; i < rounds.length; i++) {
            Integer[] roundArray = rounds[i];
            for (int noticeRound : roundArray) {
                if (noticeRound >= round) {
                    String powerup;
                    String color;
                    switch (i) {
                        case 0:
                            powerup = "Insta Kill";
                            color = String.valueOf(TextFormatting.RED);
                            break;
                        case 1:
                            powerup = "Max Ammo";
                            color = String.valueOf(TextFormatting.BLUE);
                            break;
                        case 2:
                            powerup = "Shopping Spree";
                            color = String.valueOf(TextFormatting.DARK_PURPLE);
                            break;
                        default:
                            powerup = "";
                            color = "";
                    }
                    String furtherPredict = "";
                    String notice = TextFormatting.WHITE + " in " + TextFormatting.AQUA + noticeRound;

                    if (noticeRound == round) {
                                notice = " " + TextFormatting.GREEN + TextFormatting.BOLD + "NOW";
                        for (int furtherRound : roundArray) {
                            if (furtherRound > round) {
                                furtherPredict =  TextFormatting.GRAY + "(" + furtherRound + ")";
                                break;
                            }
                        }

                    }
                    ITextComponent roundNotice = new TextComponentString(color + powerup + notice + furtherPredict);
                    chatBox.add(roundNotice);
                    break;
                }
            }
        }

        if(chatBox.isEmpty()) {
            return;
        }

        for(int i = 0; i < chatBox.size() ; i++){
            basic.appendSibling(chatBox.get(i));
            if(i != chatBox.size() - 1){
                basic.appendSibling(new TextComponentString(TextFormatting.WHITE + ", "));
            }else{
                basic.appendSibling(new TextComponentString(TextFormatting.WHITE + "."));
            }
        }

        if(!powerUpDetect.insRounds.isEmpty() || !powerUpDetect.maxRounds.isEmpty() || !powerUpDetect.ssRounds.isEmpty()) {
            PlayerUtils.sendMessage(basic);
        }
    }
}
