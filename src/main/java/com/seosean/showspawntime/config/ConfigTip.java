package com.seosean.showspawntime.config;

import com.seosean.showspawntime.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ConfigTip {

    private boolean ShowConfigTip;

    @SubscribeEvent
    public void playerConnectEvent(EntityJoinWorldEvent event) {
        if (!(event.entity instanceof EntityPlayer)) return;
        if (!event.entity.worldObj.isRemote) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.theWorld == null || mc.isSingleplayer()) return;
        EntityPlayerSP p = mc.thePlayer;
        if (p == null) return;

        if (ShowConfigTip || !PlayerUtils.isInZombiesTitle()) {
            return;
        }

        IChatComponent globalTips = new ChatComponentText(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "ShowSpawnTime: " +
                EnumChatFormatting.WHITE + "Some configs can be edited at ");
        IChatComponent configTips = new ChatComponentText(EnumChatFormatting.GOLD.toString() + EnumChatFormatting.BOLD + "CONFIG.");
        IChatComponent configHover = new ChatComponentText(EnumChatFormatting.WHITE.toString() + "Click to edit configs.");

        IChatComponent hudTips = new ChatComponentText(EnumChatFormatting.GOLD.toString() + EnumChatFormatting.BOLD + "  [HUD]");
        IChatComponent hudHover = new ChatComponentText(EnumChatFormatting.WHITE.toString() + "Click to edit HUDs");

        IChatComponent featuresTips = new ChatComponentText(EnumChatFormatting.GREEN + "  [Features]");
        IChatComponent featuresHover = new ChatComponentText(EnumChatFormatting.GREEN + "Click for more information about commands of this mod." +
                EnumChatFormatting.WHITE + "\n· Show all wave intervals" +
                EnumChatFormatting.WHITE + "\n· Make a sound when waves spawn" +
                EnumChatFormatting.WHITE + "\n· Note current game time when rounds ends" +
                EnumChatFormatting.WHITE + "\n· Tell you the time you take to clean up" +
                EnumChatFormatting.WHITE + "\n· Show the queue of lightning rod" +
                EnumChatFormatting.WHITE + "\n· Count down with sounds before 3rd wave" +
                EnumChatFormatting.WHITE + "\n· Alert to AA \"boss\" with color" +
                EnumChatFormatting.WHITE + "\n· Show teammates' health in sidebar + " +
                EnumChatFormatting.WHITE + "\n· Show the amount of wave 3 zombies in sidebar" +
                EnumChatFormatting.WHITE + "\n· Show the left time of powerups over your screen");

        IChatComponent glitchTips = new ChatComponentText(EnumChatFormatting.RED + "  [Click when config GUI glitches]");
        IChatComponent glitchHover = new ChatComponentText(EnumChatFormatting.WHITE + "Glitched?");

        ChatStyle configs = new ChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, configHover)).setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sstconfig"));
        ChatStyle huds = new ChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hudHover)).setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ssthud"));

        ChatStyle features = new ChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, featuresHover)).setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sst feature"));
        ChatStyle buttom = new ChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, glitchHover)).setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sst feature glitch"));

        configTips.setChatStyle(configs);
        hudTips.setChatStyle(huds);

        featuresTips.setChatStyle(features);
        glitchTips.setChatStyle(buttom);

        IChatComponent commandsTips = new ChatComponentText("\n" + EnumChatFormatting.GRAY + "/sstconfig and /ssthud are ways to open config GUIs.");

        globalTips.appendSibling(configTips).appendSibling(hudTips).appendSibling(featuresTips).appendSibling(glitchTips).appendSibling(commandsTips);
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(globalTips);

        ShowConfigTip = true;
        MinecraftForge.EVENT_BUS.unregister(ConfigTip.this);
    }
}
