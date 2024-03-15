package com.seosean.showspawntime.features.powerups;

import com.seosean.showspawntime.ShowSpawnTime;
import com.seosean.showspawntime.utils.PlayerUtils;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.List;

public class PowerupPredict {

    public static void detectNextPowerupRound() {

        int round = ShowSpawnTime.getSpawnTimes().currentRound;
        IChatComponent basic = new ChatComponentText(EnumChatFormatting.GOLD + "[" + EnumChatFormatting.WHITE + "ShowSpawnTime" + EnumChatFormatting.GOLD + "] ");
        List<IChatComponent> chatBox = new ArrayList<>();
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
                if (noticeRound > round) {
                    String powerup;
                    String color;
                    switch (i) {
                        case 0:
                            powerup = "Insta Kill";
                            color = String.valueOf(EnumChatFormatting.RED);
                            break;
                        case 1:
                            powerup = "Max Ammo";
                            color = String.valueOf(EnumChatFormatting.BLUE);
                            break;
                        case 2:
                            powerup = "Shopping Spree";
                            color = String.valueOf(EnumChatFormatting.DARK_PURPLE);
                            break;
                        default:
                            powerup = "";
                            color = "";
                    }
                    IChatComponent roundNotice = new ChatComponentText(color + powerup + EnumChatFormatting.WHITE + " in " + EnumChatFormatting.AQUA + noticeRound);
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
                basic.appendSibling(new ChatComponentText(EnumChatFormatting.WHITE + ", "));
            }else{
                basic.appendSibling(new ChatComponentText(EnumChatFormatting.WHITE + "."));
            }
        }

        if(!powerUpDetect.insRounds.isEmpty() || !powerUpDetect.maxRounds.isEmpty() || !powerUpDetect.ssRounds.isEmpty()) {
            PlayerUtils.sendMessage(basic);
        }
    }
}
