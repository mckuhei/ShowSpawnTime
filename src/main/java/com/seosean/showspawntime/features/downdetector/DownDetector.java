package com.seosean.showspawntime.features.downdetector;

import com.seosean.showspawntime.config.LanguageConfiguration;
import com.seosean.showspawntime.config.MainConfiguration;
import com.seosean.showspawntime.features.frcooldown.FastReviveCoolDown;
import com.seosean.showspawntime.handler.LanguageDetector;
import com.seosean.showspawntime.utils.DebugUtils;
import com.seosean.showspawntime.utils.GameUtils;
import com.seosean.showspawntime.utils.LanguageUtils;
import com.seosean.showspawntime.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DownDetector {

    public static ConcurrentHashMap<String, Integer> downCDMap = new ConcurrentHashMap<>();

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        if (!MainConfiguration.DownTimeCountDown) {
            return;
        }


        if (event.type != 1 && event.type != 0) {
            return;
        }

        if (!PlayerUtils.isInZombies()) {
            return;
        }

        String text = event.message.getFormattedText();
        if (!text.contains("§e")) {
            return;
        }

        if (!LanguageUtils.contains(text, "zombies.game.knockdown.1") && !LanguageUtils.contains(text, "zombies.game.down.2") && !LanguageUtils.contains(text, "zombies.game.down.3") && !LanguageUtils.contains(text, "zombies.game.down.4")) {
            return;
        }

        String[] strings = text.split("§");

        List<String> stringList = new ArrayList<>();

        for (String s : strings) {
            if (!s.isEmpty() && s.length() != 1 && !s.startsWith("l") && !s.startsWith("e")) {
                stringList.add(s.substring(1));
            }
        }

        GameUtils.initializePlayerList();
        List<String> playerNames = new ArrayList<>(GameUtils.playerList);
        for (String s : stringList) {
            if (playerNames.contains(s)) {
                downCDMap.put(s, 25 * 1000);
                return;
            }
        }
    }


}
