package com.seosean.showspawntime.mixins;

import com.seosean.showspawntime.config.MainConfiguration;
import com.seosean.showspawntime.ShowSpawnTime;
import com.seosean.showspawntime.modules.features.lrqueue.LRQueue;
import com.seosean.showspawntime.utils.LanguageUtils;
import com.seosean.showspawntime.utils.PlayerUtils;
import net.minecraft.client.multiplayer.WorldClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(WorldClient.class)
public class MixinWorldClient {

    @Unique
    private boolean AAr10 = false;

    @Inject(method = "playSound", at = @At(value = "RETURN"))
    private void playSound(double x, double y, double z, String soundName, float volume, float pitch, boolean distanceDelay, CallbackInfo callbackInfo){
        this.detectSound(soundName, pitch);
    }

    @Unique
    private void detectSound(String soundEffect, float pitch){
        if (soundEffect.equals("mob.wither.spawn") || soundEffect.equals("mob.enderdragon.end") || (soundEffect.equals("mob.guardian.curse") && !AAr10)) {
            AAr10 = soundEffect.equals("mob.guardian.curse");
            LRQueue.lrUsings = 0;
            ShowSpawnTime.getInstance().getGameTickHandler().setGameStarted(!soundEffect.equals("mob.enderdragon.end"));
            if (LRQueue.displayTime > 100) {
                LRQueue.displayTime = 100;
            }
        }
        if (MainConfiguration.LightningRodQueue) {
            if (PlayerUtils.isInZombies()){
                if (LanguageUtils.getMap().equals(LanguageUtils.ZombiesMap.ALIEN_ARCADIUM)) {
                    if (soundEffect.equals("ambient.weather.thunder")) {
                        if (pitch != 2.0) {
                            LRQueue.lrUsings ++;
                            LRQueue.lrOffsetCountDown = 100;
                            LRQueue.displayTime = 160;
                        }
                    }
                }
            }
        }
    }
}
