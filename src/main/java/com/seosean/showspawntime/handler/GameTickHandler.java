package com.seosean.showspawntime.handler;

import com.seosean.showspawntime.ShowSpawnTime;
import com.seosean.showspawntime.features.Renderer;
import com.seosean.showspawntime.utils.DelayedTask;
import com.seosean.showspawntime.utils.LanguageUtils;
import com.seosean.showspawntime.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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

        Renderer.setShouldRender(false);
        ShowSpawnTime.getScoreboardManager().clear();

        zGameStarted = false;
        zGameTick = 0;

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
                    }
                    finally {
                        lock.unlock();
                    }
                }, 0, 10, TimeUnit.MILLISECONDS);
            }
            else {
                zGameTick = 0;
            }
        }
        finally {
            lock.unlock();
        }
    }

    public int getGameTick() {
        return zGameTick;
    }

    public void reset() {
        startOrSplit();
    }

    public void setGameStarted(boolean flag) {
        this.zGameStarted = flag;
    }
}
