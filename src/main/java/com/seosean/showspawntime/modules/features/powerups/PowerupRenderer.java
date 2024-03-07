package com.seosean.showspawntime.modules.features.powerups;

import com.seosean.showspawntime.ShowSpawnTime;
import com.seosean.showspawntime.config.MainConfiguration;
import com.seosean.showspawntime.modules.features.Renderer;
import com.seosean.showspawntime.utils.PlayerUtils;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Map;

public class PowerupRenderer extends Renderer {

    @Override
    public void onRender(RenderGameOverlayEvent.Post event) {
        if(!MainConfiguration.PowerupAlertToggle){
            return;
        }
        ScaledResolution scaledResolution = new ScaledResolution(minecraft);
        float absoluteX = (float) MainConfiguration.getXPowerup() * (float)scaledResolution.getScaledWidth();
        float absoluteY = (float) MainConfiguration.getYPowerup() * (float)scaledResolution.getScaledHeight();

        String textSpliterDummy = EnumChatFormatting.WHITE + " - ";
        String textIncoming = "Round";


        int widthBasic = fontRenderer.getStringWidth("-");
        int widthTitleDummy = fontRenderer.getStringWidth("BONUS GOLD");
        int widthSpliterDummy = fontRenderer.getStringWidth(" - ");
        int queue = 0;

        for (Powerup powerup : new ArrayList<>(Powerup.incPowerups)) {
            fontRenderer.drawStringWithShadow(powerup.getPowerupType().getDisplayName(), absoluteX + widthBasic, absoluteY + fontRenderer.FONT_HEIGHT * queue, 0xFFFFFF);
            fontRenderer.drawStringWithShadow(textSpliterDummy + textIncoming, absoluteX + widthBasic + widthTitleDummy, absoluteY + fontRenderer.FONT_HEIGHT * queue, 0xFFFFFF);
            fontRenderer.drawStringWithShadow(textIncoming, absoluteX + widthBasic + widthSpliterDummy + widthTitleDummy, absoluteY + fontRenderer.FONT_HEIGHT * queue, 0xFF6666);
            queue ++;
        }

        for (Powerup entry : new ArrayList<>(Powerup.powerups.values())) {
            fontRenderer.drawStringWithShadow(entry.getPowerupType().getDisplayName(), absoluteX + widthBasic, absoluteY + fontRenderer.FONT_HEIGHT * queue, 0xFFFFFF);
            fontRenderer.drawStringWithShadow(textSpliterDummy, absoluteX + widthBasic + widthTitleDummy, absoluteY + fontRenderer.FONT_HEIGHT * queue, 0xFFFFFF);
            fontRenderer.drawStringWithShadow("00:" + String.format("%02d", (entry.getOffsetTime() / 20)), absoluteX + widthBasic + widthSpliterDummy + widthTitleDummy, absoluteY + fontRenderer.FONT_HEIGHT * queue, 0x99CCFF);
            queue ++;
        }
    }
}
