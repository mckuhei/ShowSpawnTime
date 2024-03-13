package com.seosean.showspawntime.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class DebugUtils {
    public static void sendMessage(String message) {
        if (Minecraft.getMinecraft().thePlayer != null) {
            IChatComponent debug = new ChatComponentText(EnumChatFormatting.DARK_GREEN + message);
            PlayerUtils.sendMessage(debug);
        }
    }
}
