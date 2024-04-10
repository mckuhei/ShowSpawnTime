package com.seosean.showspawntime.utils;

import com.seosean.showspawntime.ShowSpawnTime;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

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
        if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().thePlayer != null) {
            Minecraft.getMinecraft().thePlayer.addChatComponentMessage(string);
        }
    }

    public static void playSound(String sound, float pitch) {
        if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().thePlayer != null) {
            EntityPlayer entityPlayer = Minecraft.getMinecraft().thePlayer;
            World world = Minecraft.getMinecraft().theWorld;
            if (!world.isRemote) {
                return;
            }
            Minecraft.getMinecraft().addScheduledTask(() -> {
                S29PacketSoundEffect s29PacketSoundEffect = new S29PacketSoundEffect(sound, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, 1.0F, pitch);
                Minecraft.getMinecraft().thePlayer.sendQueue.handleSoundEffect(s29PacketSoundEffect);
            });
        }
    }
}
