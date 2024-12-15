package com.seosean.showspawntime.features.frcooldown;

import com.seosean.showspawntime.config.MainConfiguration;
import com.seosean.showspawntime.utils.LanguageUtils;
import com.seosean.showspawntime.utils.PlayerUtils;

import net.minecraft.util.text.ChatType;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.concurrent.ConcurrentHashMap;

public class FastReviveCoolDown {
    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        if (MainConfiguration.FastReviveCoolDown.equals(RenderType.OFF)) {
            return;
        }

        ChatType type = event.getType();
        
        if (type != ChatType.SYSTEM && type != ChatType.CHAT) {
            return;
        }

        if (!PlayerUtils.isInZombies()) {
            return;
        }

        String messsage = event.getMessage().getFormattedText();

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

    }

    public static ConcurrentHashMap<String, Integer> frcdMap = new ConcurrentHashMap<>();

    public enum RenderType {
        OFF,
        FRONT,
        MID,
        BEHIND
    }
}
