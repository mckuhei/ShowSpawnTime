package com.seosean.showspawntime.features.frcooldown;

import com.seosean.showspawntime.config.LanguageConfiguration;
import com.seosean.showspawntime.config.MainConfiguration;
import com.seosean.showspawntime.handler.LanguageDetector;
import com.seosean.showspawntime.utils.DebugUtils;
import com.seosean.showspawntime.utils.PlayerUtils;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.concurrent.ConcurrentHashMap;

public class FastReviveCoolDown {
    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        if (MainConfiguration.FastReviveCoolDown.equals(RenderType.OFF)) {
            return;
        }
        String messsage = event.message.getFormattedText();

        if (event.type != 1 && event.type != 0) {
            return;
        }

        if (!PlayerUtils.isInZombies()) {
            return;
        }


        if (messsage.contains(":")) {
            return;
        }
        if (!messsage.contains("§e")) {
            return;
        }

        String revivePatternEN = "";
        String revivePattern = LanguageConfiguration.get("zombies.game.revive");
        if (!LanguageDetector.language.equalsIgnoreCase("EN_US")) {
            revivePatternEN = LanguageConfiguration.getOrigin("zombies.game.revive");
        }

        if (!messsage.contains(revivePattern)) {
            if (!messsage.contains(revivePatternEN)) {
                return;
            } else {
                revivePattern = revivePatternEN;
            }
        }


        if (revivePattern.isEmpty()) {
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
        frcdMap.put(name, 5000);
    }

    public static ConcurrentHashMap<String, Integer> frcdMap = new ConcurrentHashMap<>();

    public enum RenderType {
        OFF,
        FRONT,
        MID,
        BEHIND
    }
}
