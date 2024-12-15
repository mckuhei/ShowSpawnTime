package com.seosean.showspawntime.handler;

import com.seosean.showspawntime.ShowSpawnTime;
import com.seosean.showspawntime.config.MainConfiguration;
import com.seosean.showspawntime.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Renderer {
    public boolean shouldRender;

    public Renderer() {
        this.configuration = ShowSpawnTime.getMainConfiguration();
        this.minecraft = Minecraft.getMinecraft();
        this.fontRenderer = minecraft.fontRenderer;
        this.shouldRender = false;
    }

    public static void setShouldRender(boolean flag) {
        ShowSpawnTime.getRendererList().forEach(r -> r.shouldRender = flag);
    }

    public Minecraft minecraft;
    public FontRenderer fontRenderer;
    public MainConfiguration configuration;

    @SubscribeEvent
    public void render(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }

        if (!PlayerUtils.isInZombies()) {
            return;
        }


        this.onRender(event);
    }

    public void onRender(RenderGameOverlayEvent.Post event) {
    }
}
