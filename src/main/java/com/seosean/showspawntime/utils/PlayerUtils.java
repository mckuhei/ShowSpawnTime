package com.seosean.showspawntime.utils;

import com.seosean.showspawntime.ShowSpawnTime;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class PlayerUtils {
    public static boolean isInZombiesTitle() {
        return LanguageUtils.isZombiesTitle(ShowSpawnTime.getScoreboardManager().getTitle());
    }
    public static boolean isInZombies() {
        return LanguageUtils.isZombiesTitle(ShowSpawnTime.getScoreboardManager().getTitle()) && LanguageUtils.isZombiesLeft(ShowSpawnTime.getScoreboardManager().getContent(4));
    }

    public static void sendMessage(String string) {
        sendMessage(new TextComponentString(string));
    }

    public static void sendMessage(ITextComponent string) {
        if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().player != null) {
            Minecraft.getMinecraft().player.sendMessage(string);;
        }
    }

    public static void playSound(SoundEvent sound, float pitch) {
        if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().player != null) {
            EntityPlayer entityPlayer = Minecraft.getMinecraft().player;
            World world = Minecraft.getMinecraft().world;
            if (!world.isRemote) {
                return;
            }
            Minecraft.getMinecraft().addScheduledTask(() -> {
                SPacketSoundEffect s29PacketSoundEffect = new SPacketSoundEffect(sound, SoundCategory.PLAYERS, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, 1.0F, pitch);
                Minecraft.getMinecraft().player.connection.handleSoundEffect(s29PacketSoundEffect);
            });
        }
    }
}
