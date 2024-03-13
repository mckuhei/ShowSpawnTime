package com.seosean.showspawntime.features.frcooldown;

import com.seosean.showspawntime.ShowSpawnTime;
import com.seosean.showspawntime.config.LanguageConfiguration;
import com.seosean.showspawntime.handler.LanguageDetector;
import com.seosean.showspawntime.utils.DebugUtils;
import com.seosean.showspawntime.utils.LanguageUtils;
import com.seosean.showspawntime.utils.PlayerUtils;
import com.seosean.showspawntime.utils.StringUtils;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FastReviveCoolDown {
    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        if (event.type != 1) {
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
