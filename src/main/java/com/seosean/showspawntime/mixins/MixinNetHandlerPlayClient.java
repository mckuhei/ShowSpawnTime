package com.seosean.showspawntime.mixins;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.seosean.showspawntime.ShowSpawnTime;
import com.seosean.showspawntime.config.MainConfiguration;
import com.seosean.showspawntime.features.dpscounter.DPSCounter;
import com.seosean.showspawntime.features.lrqueue.LRQueueRenderer;
import com.seosean.showspawntime.utils.LanguageUtils;
import com.seosean.showspawntime.utils.PlayerUtils;
import com.seosean.showspawntime.utils.StringUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.EntityDataManager.DataEntry;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

@Mixin(NetHandlerPlayClient.class)
public class MixinNetHandlerPlayClient {

    @Inject(method = "handleEntityMetadata", at = @At(value = "HEAD"))
    public void handleEntityMetadata(SPacketEntityMetadata packetIn, CallbackInfo callbackInfo)
    {
        if (packetIn == null) {
            return;
        }
        List<EntityDataManager.DataEntry<?>> list = packetIn.getDataManagerEntries();
        if (list == null) {
            return;
        }
        if (list.isEmpty()) {
            return;
        }

        if (Minecraft.getMinecraft() != null) {
            if (Minecraft.getMinecraft().world != null) {
                Entity entity = Minecraft.getMinecraft().world.getEntityByID(packetIn.getEntityId());
                if (entity instanceof EntityArmorStand) {
                    for (DataEntry<?> watchableObject : new ArrayList<>(list)) {
                        if (watchableObject != null) {
                        	DataParameter<?> key = watchableObject.getKey();
                            if (key.getSerializer() == DataSerializers.STRING && key.getId() == 2) {
                                if (watchableObject.getValue() instanceof String) {
                                    String armorstandName = StringUtils.trim((String) watchableObject.getValue());
                                    ShowSpawnTime.getPowerupDetect().detectArmorstand(armorstandName, packetIn.getEntityId());
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    @Unique
    private boolean showSpawnTime$AAr10 = false;

    @Inject(method = "handleCustomSound", at = @At(value = "RETURN"))
    public void handleCustomSound(SPacketCustomSound packetIn, CallbackInfo ci) {
    	String soundName = packetIn.getSoundName();
    	SoundEvent soundEvent = SoundEvent.REGISTRY.getObject(new ResourceLocation(soundName));
    	if (soundEvent != null)
    		this.showSpawnTime$detectSound(soundEvent, packetIn.getPitch());
    }

    @Unique
    private void showSpawnTime$detectSound(SoundEvent soundIn, float pitch){
        if (soundIn == SoundEvents.ENTITY_WITHER_SPAWN || soundIn == SoundEvents.ENTITY_ENDERDRAGON_DEATH || (soundIn == SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE && !showSpawnTime$AAr10)) {
            showSpawnTime$AAr10 = soundIn == SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE;

            LRQueueRenderer.lrUsings = 0;
            ShowSpawnTime.getGameTickHandler().setGameStarted(soundIn != SoundEvents.ENTITY_ENDERDRAGON_DEATH);
            if (LRQueueRenderer.displayTime > 100) {
                LRQueueRenderer.displayTime = 100;
            }
        }

        if (MainConfiguration.LightningRodQueue) {
            if (PlayerUtils.isInZombies()){
                if (LanguageUtils.getMap().equals(LanguageUtils.ZombiesMap.ALIEN_ARCADIUM)) {
                    if (soundIn == SoundEvents.ENTITY_LIGHTNING_THUNDER) {
                        if (pitch != 2.0) {
                            LRQueueRenderer.lrUsings ++;
                            LRQueueRenderer.lrOffsetCountDown = 100;
                            LRQueueRenderer.displayTime = 160;
                        }
                    }
                }
            }
        }

        if (MainConfiguration.DPSCounterToggle) {
            DPSCounter.detectWeaponBehavior(soundIn, pitch);
        }
    }
}
