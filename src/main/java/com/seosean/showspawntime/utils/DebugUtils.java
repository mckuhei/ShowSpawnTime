package com.seosean.showspawntime.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class DebugUtils {
    public static void sendMessage(String message) {
        if (Minecraft.getMinecraft().player != null) {
            ITextComponent debug = new TextComponentString(TextFormatting.DARK_GREEN + message);
            PlayerUtils.sendMessage(debug);
        }
    }
}
