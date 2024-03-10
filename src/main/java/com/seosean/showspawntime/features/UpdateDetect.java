package com.seosean.showspawntime.features;


import com.seosean.showspawntime.ShowSpawnTime;
import com.seosean.showspawntime.utils.DelayedTask;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UpdateDetect {

    private boolean triggered = false;
    @SubscribeEvent
    public void playerConnectEvent(TickEvent.ClientTickEvent event) {
        if (triggered) {
            return;
        }
        Minecraft mc = Minecraft.getMinecraft();

        if (mc == null || mc.theWorld == null || mc.isSingleplayer() || mc.thePlayer == null) return;

        if (!mc.theWorld.isAreaLoaded(mc.thePlayer.getPosition(), 16)) {
            return;
        }

        triggered = true;
        new DelayedTask() {
            @Override
            public void run() {
                UpdateDetect.this.checkUpdates();
            }
        }.runTaskLater(3 * 20);

        MinecraftForge.EVENT_BUS.unregister(this);
    }

    public void checkUpdates() {
        new Thread(() -> {
            try {
                URL url = new URL("https://raw.githubusercontent.com/Seosean/ShowSpawnTime/2.0/build.gradle");
                URLConnection connection = url.openConnection();
                connection.setReadTimeout(5000);
                connection.addRequestProperty("User-Agent", "ShowSpawnTime update checker");
                connection.setDoOutput(true);
                final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String currentLine;
                String newestVersion = "";
                while ((currentLine = reader.readLine()) != null) {
                    if (currentLine.contains("version = \"")) {
                        String[] newestVersionSplit = currentLine.split(Pattern.quote("version = \""));
                        newestVersionSplit = newestVersionSplit[1].split(Pattern.quote("\""));
                        newestVersion = newestVersionSplit[0];
                        break;
                    }
                }
                reader.close();
                List<Integer> newestVersionNumbers = new ArrayList<>();
                List<Integer> thisVersionNumbers = new ArrayList<>();
                try {
                    for (String s : newestVersion.split(Pattern.quote("."))) {
                        newestVersionNumbers.add(Integer.parseInt(s));
                    }
                    for (String s : ShowSpawnTime.VERSION.split(Pattern.quote("."))) {
                        thisVersionNumbers.add(Integer.parseInt(s));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return;
                }
                for (int i = 0; i < 3; i++) {
                    if (i >= newestVersionNumbers.size() ) {
                        newestVersionNumbers.add(i, 0);
                    }
                    if (i >= thisVersionNumbers.size()) {
                        thisVersionNumbers.add(i, 0);
                    }
                    if (newestVersionNumbers.get(i) > thisVersionNumbers.get(i)) {
                        IChatComponent newVersion = new ChatComponentText(EnumChatFormatting.AQUA+ "ShowSpawnTime: " + EnumChatFormatting.GREEN + "A new version " + newestVersion + " is available. Download it by clicking here.");
                        IChatComponent downloadHover = new ChatComponentText(EnumChatFormatting.WHITE + "Click to Download");

                        newVersion.setChatStyle(newVersion.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, downloadHover)).setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Seosean/ShowSpawnTime/releases")));
                        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(newVersion);
                        status = Version.UPTODATE;
                        break;
                    } else if (newestVersionNumbers.get(i) < thisVersionNumbers.get(i)) {
                        IChatComponent newVersion = new ChatComponentText(EnumChatFormatting.AQUA+ "ShowSpawnTime: " + EnumChatFormatting.GREEN + "You are using an advanced version, it's probably unstable and uncompleted.");
                        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(newVersion);
                        status = Version.ADVANCED;
                    }

                    this.newestVersion = newestVersion;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    public static Version status = Version.LATEST;

    public static String newestVersion = "";


    public enum Version {
        UPTODATE,
        LATEST,
        ADVANCED
    }

}
