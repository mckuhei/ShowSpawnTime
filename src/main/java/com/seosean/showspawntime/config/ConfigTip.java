package com.seosean.showspawntime.config;

import com.seosean.showspawntime.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ConfigTip {

    private boolean ShowConfigTip;

    @SubscribeEvent
    public void playerConnectEvent(EntityJoinWorldEvent event) {
        if (!(event.getEntity() instanceof EntityPlayer)) return;
        if (!event.getEntity().world.isRemote) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.world == null || mc.isSingleplayer()) return;
        EntityPlayerSP p = mc.player;
        if (p == null) return;

        if (ShowConfigTip || !PlayerUtils.isInZombiesTitle()) {
            return;
        }

        ITextComponent globalTips = new TextComponentString(TextFormatting.AQUA.toString() + TextFormatting.BOLD + "ShowSpawnTime: " +
                TextFormatting.WHITE + "Some configs can be edited at ");
        ITextComponent configTips = new TextComponentString(TextFormatting.GOLD.toString() + TextFormatting.BOLD + "CONFIG.");
        ITextComponent configHover = new TextComponentString(TextFormatting.WHITE.toString() + "Click to edit configs.");

        ITextComponent hudTips = new TextComponentString(TextFormatting.GOLD.toString() + TextFormatting.BOLD + "  [HUD]");
        ITextComponent hudHover = new TextComponentString(TextFormatting.WHITE.toString() + "Click to edit HUDs");

        ITextComponent featuresTips = new TextComponentString(TextFormatting.GREEN + "  [Features]");
        ITextComponent featuresHover = new TextComponentString(TextFormatting.GREEN + "Click for more information about commands of this mod." +
                TextFormatting.WHITE + "\n· Show all wave intervals" +
                TextFormatting.WHITE + "\n· Make a sound when waves spawn" +
                TextFormatting.WHITE + "\n· Note current game time when rounds ends" +
                TextFormatting.WHITE + "\n· Tell you the time you take to clean up" +
                TextFormatting.WHITE + "\n· Show the queue of lightning rod" +
                TextFormatting.WHITE + "\n· Count down with sounds before 3rd wave" +
                TextFormatting.WHITE + "\n· Alert to AA \"boss\" with color" +
                TextFormatting.WHITE + "\n· Show teammates' health in sidebar + " +
                TextFormatting.WHITE + "\n· Show the amount of wave 3 zombies in sidebar" +
                TextFormatting.WHITE + "\n· Show the left time of powerups over your screen");

        ITextComponent glitchTips = new TextComponentString(TextFormatting.RED + "  [Click when config GUI glitches]");
        ITextComponent glitchHover = new TextComponentString(TextFormatting.WHITE + "Glitched?");

        Style configs = new Style().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, configHover)).setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sstconfig"));
        Style huds = new Style().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hudHover)).setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ssthud"));

        Style features = new Style().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, featuresHover)).setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sst feature"));
        Style buttom = new Style().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, glitchHover)).setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sst feature glitch"));

        configTips.setStyle(configs);
        hudTips.setStyle(huds);

        featuresTips.setStyle(features);
        glitchTips.setStyle(buttom);

        ITextComponent commandsTips = new TextComponentString("\n" + TextFormatting.GRAY + "/sstconfig and /ssthud are ways to open config GUIs.");

        globalTips.appendSibling(configTips).appendSibling(hudTips).appendSibling(featuresTips).appendSibling(glitchTips).appendSibling(commandsTips);
        PlayerUtils.sendMessage(globalTips);

        ShowConfigTip = true;
        MinecraftForge.EVENT_BUS.unregister(ConfigTip.this);
    }
}
