package com.seosean.showspawntime.features.dpscounter;

import com.seosean.showspawntime.config.MainConfiguration;
import com.seosean.showspawntime.features.Renderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class DPSCounterRenderer extends Renderer {


    @Override
    public void onRender(RenderGameOverlayEvent.Post event) {
        ScaledResolution scaledResolution = new ScaledResolution(minecraft);

        float absoluteX = (float) MainConfiguration.getXDPSCounter() * (float)scaledResolution.getScaledWidth();
        float absoluteY = (float) MainConfiguration.getYDPSCounter() * (float)scaledResolution.getScaledHeight();

        fontRenderer.drawStringWithShadow("DPS: " + (DPSCounter.instaKillOn > 0 ? "INSTA KILL" : DPSCounter.DPS), absoluteX, absoluteY + this.fontRenderer.FONT_HEIGHT * (-1), 0xFFFFFF);

    }
}
