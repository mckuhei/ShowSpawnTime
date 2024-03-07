package com.seosean.showspawntime.mixins;

import com.seosean.showspawntime.ShowSpawnTime;
import com.seosean.showspawntime.config.MainConfiguration;
import com.seosean.showspawntime.modules.features.Renderer;
import com.seosean.showspawntime.modules.features.leftnotice.LeftNotice;
import com.seosean.showspawntime.modules.features.powerups.Powerup;
import com.seosean.showspawntime.modules.features.timerecorder.TimeRecorder;
import com.seosean.showspawntime.utils.DebugUtils;
import com.seosean.showspawntime.utils.GameUtils;
import com.seosean.showspawntime.utils.LanguageUtils;
import com.seosean.showspawntime.utils.PlayerUtils;
import com.seosean.showspawntime.utils.StringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Mixin(GuiIngame.class)
public abstract class MixinGuiIngame {


    @Inject(method = "displayTitle", at = @At(value = "RETURN"))
    private void displayTitle(String p_175178_1_, String p_175178_2_, int p_175178_3_, int p_175178_4_, int p_175178_5_, CallbackInfo callbackInfo){
        String roundTitle = p_175178_1_ == null ? "" : StringUtils.trim(p_175178_1_);
        boolean flag = roundTitle.contains("Win!") || roundTitle.contains("赢") || roundTitle.contains("贏");

        if (LanguageUtils.isRoundTitle(roundTitle) || flag) {
            ShowSpawnTime.getScoreboardManager().updateScoreboardContent();
            if (!PlayerUtils.isInZombiesTitle()) {
                return;
            }

            ShowSpawnTime.getScoreboardManager().setKeepUpdate(PlayerUtils.isInZombiesTitle());

            int round = LanguageUtils.getRoundNumber(roundTitle);

//            DebugUtils.sendMessage("Debug 1: " + round);
//            DebugUtils.sendMessage("Debug 2: " + LanguageUtils.getMap());

            ShowSpawnTime.getSpawnTimes().setCurrentRound(round);

            Renderer.setShouldRender(true);

            ShowSpawnTime.getSpawnNotice().update(round);

            Powerup.incPowerups.clear();

            if (ShowSpawnTime.getInstance().getPowerupDetect().isInsRound(round)) {
                Powerup.deserialize(Powerup.PowerupType.INSTA_KILL);
            }

            if (ShowSpawnTime.getInstance().getPowerupDetect().isMaxRound(round)) {
                Powerup.deserialize(Powerup.PowerupType.MAX_AMMO);
            }

            if (ShowSpawnTime.getInstance().getPowerupDetect().isSSRound(round)) {
                Powerup.deserialize(Powerup.PowerupType.SHOPPING_SPREE);
            }

            TimeRecorder.recordGameTime(roundTitle);
            TimeRecorder.recordRedundantTime(flag);
            ShowSpawnTime.getInstance().getGameTickHandler().setGameStarted(true);
            ShowSpawnTime.getInstance().getGameTickHandler().reset();
        }
    }

    @ModifyArg(method = "renderScoreboard", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawString(Ljava/lang/String;III)I", ordinal = 0), index = 0)
    private String modifyArgumentText(String text) {
        if(MainConfiguration.Wave3LeftNotice) {
            if (PlayerUtils.isInZombies()) {
                if (text.contains("Zombies Left") || text.contains("剩余僵尸") || text.contains("剩下殭屍數")) {
                    String amount = "";
                    if (text.contains(":")) {
                        amount = StringUtils.trim(text.split(":")[1]);
                    } else if(text.contains("：")) {
                        amount = StringUtils.trim(text.split("：")[1]);
                    }
                    int left = LeftNotice.getLeft(ShowSpawnTime.getSpawnTimes().currentRound);
                    boolean isCleared = Integer.parseInt(amount) <= left;
                    if (LanguageUtils.getMap().equals(LanguageUtils.ZombiesMap.DEAD_END) || LanguageUtils.getMap().equals(LanguageUtils.ZombiesMap.BAD_BLOOD)) {
                        return text.concat(EnumChatFormatting.WHITE + " | " + (isCleared ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + ((left == 0) ? "" : left));
                    }
                }
            }
        }
        if (MainConfiguration.PlayerHealthNotice) {
            if (PlayerUtils.isInZombies()) {
                if (text.contains("§") && (text.contains(":") || text.contains("："))) {
                    String playerName = "";
                    if (text.contains(":")) {
                        playerName = StringUtils.trim(text.split(":")[0]);
                    } else if (text.contains("：")){
                        playerName = StringUtils.trim(text.split("：")[0]);
                    }
                    if (playerName.length() >= 2) {
                        EntityPlayer entityPlayer = getPlayerEntity(playerName);
                        if (entityPlayer != null) {
                            String trippedText = StringUtils.trim(text);
                            if (trippedText.contains(": REVIVE") || trippedText.contains(": QUIT") || trippedText.contains(": DEAD") || trippedText.contains(": 等待救援") || trippedText.contains("： 已退出") || trippedText.contains(": 已死亡") || trippedText.contains(": 等待復活") || trippedText.contains(": 已退出")) {
                                return text;
                            }
                            float health = entityPlayer.getHealth();
                            return EnumChatFormatting.WHITE + "(" + getColor(entityPlayer) + (int) health + EnumChatFormatting.WHITE + ")" + " " + text;
                        }
                    }
                }
            }
        }
        return text;
    }

    @Shadow
    public abstract FontRenderer getFontRenderer();

    //Contents
    @ModifyArg(method = "renderScoreboard", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawString(Ljava/lang/String;III)I", ordinal = 0), index = 1)
    private int modifyArgumentWidth0(int l1){
        if(MainConfiguration.PlayerHealthNotice || MainConfiguration.Wave3LeftNotice) {
            if (PlayerUtils.isInZombies()) {
                String text = "(00)";
                return l1 - getFontRenderer().getStringWidth(text);
            }
        }
        return l1;
    }

    //Title
    @ModifyArg(method = "renderScoreboard", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawString(Ljava/lang/String;III)I", ordinal = 2), index = 1)
    private int modifyArgumentWidth1(int l1){
        if (MainConfiguration.PlayerHealthNotice || MainConfiguration.Wave3LeftNotice) {
            if (PlayerUtils.isInZombies()) {
                String text = "(00)";
                return l1 - getFontRenderer().getStringWidth(text) / 2;
            }
        }
        return l1;
    }

    //Contents GUI
    @ModifyArg(method = "renderScoreboard", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiIngame;drawRect(IIIII)V", ordinal = 0), index = 0)
    private int modifyArgumentLeft0(int l1){
        if (MainConfiguration.PlayerHealthNotice || MainConfiguration.Wave3LeftNotice) {
            if (PlayerUtils.isInZombies()) {
                String text = "(00)";
                return l1 - getFontRenderer().getStringWidth(text);
            }
        }
        return l1;
    }

    //Title GUI
    @ModifyArg(method = "renderScoreboard", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiIngame;drawRect(IIIII)V", ordinal = 1), index = 0)
    private int modifyArgumentLeft1(int l1){
        if (MainConfiguration.PlayerHealthNotice || MainConfiguration.Wave3LeftNotice) {
            if (PlayerUtils.isInZombies()) {
                String text = "(00)";
                return l1 - getFontRenderer().getStringWidth(text);
            }
        }
        return l1;
    }

    //Bottom GUI
    @ModifyArg(method = "renderScoreboard", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiIngame;drawRect(IIIII)V", ordinal = 2), index = 0)
    private int modifyArgumentLeft2(int l1){
        if(MainConfiguration.PlayerHealthNotice || MainConfiguration.Wave3LeftNotice) {
            if (PlayerUtils.isInZombies()) {
                String text = "(00)";
                return l1 - getFontRenderer().getStringWidth(text);
            }
        }
        return l1;
    }


    @Unique
    private List<EntityPlayer> getPlayerList() {
        return new CopyOnWriteArrayList<>(Minecraft.getMinecraft().theWorld.playerEntities);
    }


    @Unique
    private EntityPlayer getPlayerEntity(String name) {
        List<EntityPlayer> playerList = this.getPlayerList();
        for (EntityPlayer entity: playerList){
            String entityName = entity.getDisplayNameString().trim();
            if (name.equals(entityName.trim())) {
                return entity;
            }
        }
        return null;
    }

    @Unique
    private EnumChatFormatting getColor(EntityPlayer entityPlayer) {
        int currentHealth = (int)entityPlayer.getHealth();
        if (currentHealth > entityPlayer.getMaxHealth() / 2) {
            return EnumChatFormatting.GREEN;
        } else if (currentHealth > entityPlayer.getMaxHealth() / 4) {
            return EnumChatFormatting.YELLOW;
        } else return EnumChatFormatting.RED;
    }

}
