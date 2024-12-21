package com.seosean.showspawntime.features.spawntimes;

import com.seosean.showspawntime.ShowSpawnTime;
import com.seosean.showspawntime.config.MainConfiguration;
import com.seosean.showspawntime.handler.GameTickHandler;
import com.seosean.showspawntime.handler.Renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class SpawnTimeRenderer extends Renderer {

    private SpawnTimes spawnTimes = ShowSpawnTime.getSpawnTimes();

    @Override
    public void onRender(RenderGameOverlayEvent.Post event) {
        ScaledResolution scaledResolution = new ScaledResolution(minecraft);
        int widthW = fontRenderer.getStringWidth("➤ ");
        float absoluteX = (float) MainConfiguration.getXSpawnTime() * (float)scaledResolution.getScaledWidth();
        float absoluteY = (float) MainConfiguration.getYSpawnTime() * (float)scaledResolution.getScaledHeight();
        spawnTimes.getCurrentWave();
        int waveAmount = spawnTimes.roundTimes.length;

        if (spawnTimes.currentRound != 0 && waveAmount == 0) {
            System.out.println("ShowSpawnTime: " + "There is an exception happened to map detector!");
            return;
        }
        
        if (waveAmount != 0) {
            fontRenderer.drawStringWithShadow("➤ ", absoluteX, absoluteY + this.fontRenderer.FONT_HEIGHT * (5 - waveAmount + spawnTimes.getNextWave()), 0xCC00CC);
        }

        for (int i = 0; i < waveAmount; i++) {
            int wave = i + 1;
            fontRenderer.drawStringWithShadow("W" + wave + " " + this.getTime(spawnTimes.getWaveTime(wave)), absoluteX + widthW, absoluteY + this.fontRenderer.FONT_HEIGHT * (5 - waveAmount + wave), spawnTimes.getColor(wave));
        }
        GameTickHandler gameTickHandler = ShowSpawnTime.getGameTickHandler();
        long millis = gameTickHandler.getGameTick() == 0 ? 0L : Minecraft.getSystemTime() - gameTickHandler.getStartTime();
        long minutesPart = millis / 60000;
        long secondsPart = (millis % 60000) / 1000;
        long tenthSecondsPart = millis % 1000;
        String time = String.format("%02d:%02d.%d", minutesPart, secondsPart, tenthSecondsPart / 100);
        fontRenderer.drawStringWithShadow(time, absoluteX + widthW + (this.fontRenderer.getStringWidth("W1 00:10") - this.fontRenderer.getStringWidth("00:00.0")), absoluteY + this.fontRenderer.FONT_HEIGHT * 6, 0xFFFFFF);
    }

    private String getTime(int time) {
        if (time <= 0) {
            return "00:00";
        }

        int seconds = time % 60;
        int minutes = time / 60;

        return String.format("%02d:%02d", minutes, seconds);
    }
}
