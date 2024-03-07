package com.seosean.showspawntime.modules.features.spawntimes;

import com.seosean.showspawntime.ShowSpawnTime;
import com.seosean.showspawntime.config.MainConfiguration;
import com.seosean.showspawntime.modules.features.Renderer;
import com.seosean.showspawntime.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SpawnTimeRenderer extends Renderer {

    private SpawnTimes spawnTimes = ShowSpawnTime.getSpawnTimes();

    @Override
    public void onRender(RenderGameOverlayEvent.Post event) {
        ScaledResolution scaledResolution = new ScaledResolution(minecraft);
        int widthW = fontRenderer.getStringWidth("➤ ");
        float absoluteX = (float) MainConfiguration.getXSpawnTime() * (float)scaledResolution.getScaledWidth();
        float absoluteY = (float)MainConfiguration.getYSpawnTime() * (float)scaledResolution.getScaledHeight();
        spawnTimes.getCurrentWave();

        int waveAmount = spawnTimes.roundTimes.length;

        fontRenderer.drawStringWithShadow("➤ ", absoluteX, absoluteY + this.fontRenderer.FONT_HEIGHT * (5 - waveAmount + spawnTimes.getNextWave()), 0xCC00CC);

        for (int i = 0; i < spawnTimes.roundTimes.length; i++) {
            int wave = i + 1;
            fontRenderer.drawStringWithShadow("W" + wave + " " + "00:" + spawnTimes.getWaveTime(wave), absoluteX + widthW, absoluteY + this.fontRenderer.FONT_HEIGHT * (5 - waveAmount + wave), spawnTimes.getColor(wave));
        }
    }
}
