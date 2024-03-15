package com.seosean.showspawntime.handler;

import com.seosean.showspawntime.config.MainConfiguration;
import com.seosean.showspawntime.features.frcooldown.FastReviveCoolDown;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Map;

public class CountDownTimer {
    @SubscribeEvent
    public void countdown(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.theWorld == null || mc.isSingleplayer()) {
            return;
        }
        if (event.phase!= TickEvent.Phase.START) {
            return;
        }
        EntityPlayerSP p = mc.thePlayer;
        if (p == null) {
            return;
        }

        if (!MainConfiguration.FastReviveCoolDown.equals(FastReviveCoolDown.RenderType.OFF)) {
            for (Map.Entry<String, Integer> entry : new ArrayList<>(FastReviveCoolDown.frcdMap.entrySet())) {
                int newValue = entry.getValue() - 50;
                FastReviveCoolDown.frcdMap.computeIfPresent(entry.getKey(), (k, v) -> newValue);

                if (newValue <= 0) {
                    FastReviveCoolDown.frcdMap.remove(entry.getKey());
                }
            }
        }
    }
}
