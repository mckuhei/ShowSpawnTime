package com.seosean.showspawntime.features.powerups;

import com.seosean.showspawntime.config.MainConfiguration;
import com.seosean.showspawntime.handler.Renderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.util.ArrayList;

public class PowerupRenderer extends Renderer {

    @Override
    public void onRender(RenderGameOverlayEvent.Post event) {
        if(!MainConfiguration.PowerupAlertToggle){
            return;
        }
        ScaledResolution scaledResolution = new ScaledResolution(minecraft);
        float absoluteX = (float) MainConfiguration.getXPowerup() * (float)scaledResolution.getScaledWidth();
        float absoluteY = (float) MainConfiguration.getYPowerup() * (float)scaledResolution.getScaledHeight();

        String textSpliterDummy = TextFormatting.WHITE + " - ";
        String textIncoming = "Round";


        int widthBasic = fontRenderer.getStringWidth("-");
        int widthTitleDummy = fontRenderer.getStringWidth("BONUS GOLD");
        int widthSpliterDummy = fontRenderer.getStringWidth(" - ");
        int queue = 0;

        for (Powerup powerup : new ArrayList<>(Powerup.incPowerups)) {
            fontRenderer.drawStringWithShadow(powerup.getPowerupType().getTextFormatting() + powerup.getPowerupType().getDisplayName(), absoluteX + widthBasic, absoluteY + fontRenderer.FONT_HEIGHT * queue, 0xFFFFFF);
            fontRenderer.drawStringWithShadow(textSpliterDummy + textIncoming, absoluteX + widthBasic + widthTitleDummy, absoluteY + fontRenderer.FONT_HEIGHT * queue, 0xFFFFFF);
            fontRenderer.drawStringWithShadow(textIncoming, absoluteX + widthBasic + widthSpliterDummy + widthTitleDummy, absoluteY + fontRenderer.FONT_HEIGHT * queue, 0xFF6666);
            queue ++;
        }

        for (Powerup entry : new ArrayList<>(Powerup.powerups.values())) {
            fontRenderer.drawStringWithShadow(((entry.getOffsetTime() / 20 <= 10 && (entry.getOffsetTime() / 20) % 2 == 0) ? TextFormatting.WHITE : entry.getPowerupType().getTextFormatting()) + entry.getPowerupType().getDisplayName(), absoluteX + widthBasic, absoluteY + fontRenderer.FONT_HEIGHT * queue, 0xFFFFFF);
            fontRenderer.drawStringWithShadow(textSpliterDummy, absoluteX + widthBasic + widthTitleDummy, absoluteY + fontRenderer.FONT_HEIGHT * queue, 0xFFFFFF);
            fontRenderer.drawStringWithShadow("00:" + String.format("%02d", (entry.getOffsetTime() / 20)), absoluteX + widthBasic + widthSpliterDummy + widthTitleDummy, absoluteY + fontRenderer.FONT_HEIGHT * queue, 0x99CCFF);
            queue ++;
        }
    }
}
