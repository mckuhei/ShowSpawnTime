package com.seosean.showspawntime.features.frcooldown;

import com.seosean.showspawntime.config.LanguageConfiguration;
import com.seosean.showspawntime.config.MainConfiguration;
import com.seosean.showspawntime.features.downdetector.DownDetector;
import com.seosean.showspawntime.handler.LanguageDetector;
import com.seosean.showspawntime.utils.DebugUtils;
import com.seosean.showspawntime.utils.LanguageUtils;
import com.seosean.showspawntime.utils.PlayerUtils;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.concurrent.ConcurrentHashMap;

public class FastReviveCoolDown {
    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        if (MainConfiguration.FastReviveCoolDown.equals(RenderType.OFF) && !MainConfiguration.DownTimeCountDown) {
            return;
        }

        if (event.type != 1 && event.type != 0) {
            return;
        }

        if (!PlayerUtils.isInZombies()) {
            return;
        }

        String messsage = event.message.getFormattedText();

        if (messsage.contains(":")) {
            return;
        }

        if (!messsage.contains("§e")) {
            return;
        }

        if (!LanguageUtils.contains(messsage, "zombies.game.revive")) {
            return;
        }

        String[] strings = messsage.split("§");
        String name = "";
        for (String s : strings) {
            if (!s.isEmpty() && !s.startsWith("e") && s.length() != 1) {
                name = s;
            }
        }

        if (name.isEmpty()) {
            return;
        }

        name = name.substring(1);
        if (!MainConfiguration.FastReviveCoolDown.equals(RenderType.OFF)) {
            frcdMap.put(name, 5000);
        }

        if (MainConfiguration.DownTimeCountDown) {
            DownDetector.downCDMap.remove(name);
        }

    }

    public static ConcurrentHashMap<String, Integer> frcdMap = new ConcurrentHashMap<>();

    public enum RenderType {
        OFF,
        FRONT,
        MID,
        BEHIND
    }
}
