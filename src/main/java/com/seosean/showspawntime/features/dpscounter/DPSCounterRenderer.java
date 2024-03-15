package com.seosean.showspawntime.features.dpscounter;

import com.seosean.showspawntime.config.MainConfiguration;
import com.seosean.showspawntime.handler.Renderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class DPSCounterRenderer extends Renderer {


    @Override
    public void onRender(RenderGameOverlayEvent.Post event) {
        if (!MainConfiguration.DPSCounterToggle) {
            return;
        }

        ScaledResolution scaledResolution = new ScaledResolution(minecraft);

        float absoluteX = (float) MainConfiguration.getXDPSCounter() * (float)scaledResolution.getScaledWidth();
        float absoluteY = (float) MainConfiguration.getYDPSCounter() * (float)scaledResolution.getScaledHeight();

        fontRenderer.drawStringWithShadow(EnumChatFormatting.GOLD + "DPS" + EnumChatFormatting.WHITE + ": " + (DPSCounter.instaKillOn > 0 ? EnumChatFormatting.RED +  "INSTA KILL" : EnumChatFormatting.AQUA.toString() + DPSCounter.DPS), absoluteX, absoluteY, 0xFFFFFF);

    }
}
