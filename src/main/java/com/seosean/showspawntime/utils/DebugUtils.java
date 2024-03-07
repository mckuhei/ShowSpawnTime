package com.seosean.showspawntime.utils;

import com.seosean.showspawntime.ShowSpawnTime;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class DebugUtils {
    public static void sendMessage(String message) {
        IChatComponent debug = new ChatComponentText(EnumChatFormatting.DARK_GREEN + message);
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(debug);
    }
}
