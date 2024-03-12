package com.seosean.showspawntime.handler;

import com.seosean.showspawntime.ShowSpawnTime;
import com.seosean.showspawntime.features.Renderer;
import com.seosean.showspawntime.features.frcooldown.FastReviveCoolDown;
import com.seosean.showspawntime.utils.DebugUtils;
import com.seosean.showspawntime.utils.DelayedTask;
import com.seosean.showspawntime.utils.LanguageUtils;
import com.seosean.showspawntime.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;


public class GameTickHandler {

    public GameTickHandler() {
        minecraft = Minecraft.getMinecraft();
    }
    private final Minecraft minecraft;
    private boolean zGameStarted;
    private int zGameTick = 0;

    @SubscribeEvent
    public void onPlayerChangeWorld(EntityJoinWorldEvent event) {
        if (!event.entity.worldObj.isRemote) {
            return;
        }
        if (minecraft == null || minecraft.theWorld == null || minecraft.isSingleplayer()) {
            return;
        }
        EntityPlayerSP p = minecraft.thePlayer;
        if (p == null) {
            return;
        }
        Entity entity = event.entity;

        if (!entity.equals(p)){
            return;
        }

        if (ShowSpawnTime.VERSION.contains("Pre")) {
            DebugUtils.sendMessage(EnumChatFormatting.GOLD.toString() + EnumChatFormatting.BOLD + "ShowSpawnTime" + EnumChatFormatting.WHITE + ": " + EnumChatFormatting.RED + "You are now using an unstable pre-version build of ShowSpawnTime! Please update your version as fast as possible if there is a latest version released.");
        }

        Renderer.setShouldRender(false);
        ShowSpawnTime.getScoreboardManager().clear();

        zGameStarted = false;
        zGameTick = 0;
        ShowSpawnTime.getSpawnTimes().setCurrentRound(0);

        FastReviveCoolDown.frcdMap.clear();

        new DelayedTask() {
            @Override
            public void run() {
                if (PlayerUtils.isInZombies()) {
                    int round = LanguageUtils.getRoundNumber(ShowSpawnTime.getScoreboardManager().getContent(3));
                    ShowSpawnTime.getSpawnTimes().setCurrentRound(round);
                    Renderer.setShouldRender(true);
                }
            }
        }.runTaskLater(100);
    }


    private final ReentrantLock lock = new ReentrantLock();

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private Future<?> future = null;

    public void startOrSplit() {
        lock.lock();
        try {
            if (future == null) {
                future = executor.scheduleAtFixedRate(() -> {
                    lock.lock();
                    try {
                        zGameTick += 10;
                        if (zGameTick % 1000 == 0) {
                            if (zGameStarted && PlayerUtils.isInZombies()) {
                                ShowSpawnTime.getSpawnNotice().onSpawn(zGameTick);
                            }
                        }

                        for (Map.Entry<String, Integer> entry : new ArrayList<>(FastReviveCoolDown.frcdMap.entrySet())) {
                            int newValue = entry.getValue() - 10;
                            FastReviveCoolDown.frcdMap.computeIfPresent(entry.getKey(), (k, v) -> newValue);

                            if (newValue <= 0) {
                                FastReviveCoolDown.frcdMap.remove(entry.getKey());
                            }
                        }
                    }
                    finally {
                        lock.unlock();
                    }
                }, 0, 10, TimeUnit.MILLISECONDS);
            }
            else {
                zGameTick = 0;
            }
        } catch (Exception e) {
            System.out.println("SST Splits Exception!");
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }

    public int getGameTick() {
        return zGameTick;
    }

    public void setGameStarted(boolean flag) {
        this.zGameStarted = flag;
    }
}
