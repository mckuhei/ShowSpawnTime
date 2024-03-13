package com.seosean.showspawntime.utils;

import com.seosean.showspawntime.ShowSpawnTime;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class PlayerUtils {
    public static boolean isInZombiesTitle() {
        return LanguageUtils.isZombiesTitle(ShowSpawnTime.getScoreboardManager().getTitle());
    }
    public static boolean isInZombies() {
        return LanguageUtils.isZombiesTitle(ShowSpawnTime.getScoreboardManager().getTitle()) && LanguageUtils.isZombiesLeft(ShowSpawnTime.getScoreboardManager().getContent(4));
    }

    public static void sendMessage(String string) {
        sendMessage(new ChatComponentText(string));
    }

    public static void sendMessage(IChatComponent string) {
        if (Minecraft.getMinecraft().thePlayer != null) {
            PlayerUtils.sendMessage(string);
        }
    }
}
