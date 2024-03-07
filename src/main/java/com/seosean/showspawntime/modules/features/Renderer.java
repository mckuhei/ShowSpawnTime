package com.seosean.showspawntime.modules.features;

import com.seosean.showspawntime.ShowSpawnTime;
import com.seosean.showspawntime.config.MainConfiguration;
import com.seosean.showspawntime.utils.DebugUtils;
import com.seosean.showspawntime.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public abstract class Renderer {
    public boolean shouldRender;

    public Renderer() {
        this.configuration = ShowSpawnTime.getInstance().getMainConfiguration();
        this.minecraft = Minecraft.getMinecraft();
        this.fontRenderer = minecraft.fontRendererObj;
        this.shouldRender = false;
    }

    public static void setShouldRender(boolean flag) {
        ShowSpawnTime.getPowerupRenderer().shouldRender = flag;
        ShowSpawnTime.getSpawnTimeRenderer().shouldRender = flag;
    }

    public Minecraft minecraft;
    public FontRenderer fontRenderer;
    public MainConfiguration configuration;

    @SubscribeEvent
    public void render(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }

        if (!PlayerUtils.isInZombies()) {
            return;
        }

        if (!this.shouldRender) {
            return;
        }

        this.onRender(event);
    }

    public void onRender(RenderGameOverlayEvent.Post event) {
    }
}
