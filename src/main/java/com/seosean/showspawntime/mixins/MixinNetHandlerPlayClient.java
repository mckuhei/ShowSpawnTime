package com.seosean.showspawntime.mixins;

import com.seosean.showspawntime.ShowSpawnTime;
import com.seosean.showspawntime.utils.StringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(NetHandlerPlayClient.class)
public class MixinNetHandlerPlayClient {

    @Inject(method = "handleEntityMetadata", at = @At(value = "HEAD"))
    public void handleEntityMetadata(S1CPacketEntityMetadata packetIn, CallbackInfo callbackInfo)
    {
        if (packetIn == null) {
            return;
        }
        List<DataWatcher.WatchableObject> list = packetIn.func_149376_c();
        if (list == null) {
            return;
        }
        if (list.isEmpty()) {
            return;
        }
        
        if (Minecraft.getMinecraft() != null) {
            if (Minecraft.getMinecraft().theWorld != null) {
                Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(packetIn.getEntityId());
                if (entity instanceof EntityArmorStand) {
                    for (DataWatcher.WatchableObject watchableObject : new ArrayList<>(packetIn.func_149376_c())) {
                        if (watchableObject != null) {
                            if (watchableObject.getObjectType() == 4 && watchableObject.getDataValueId() == 2) {
                                if (watchableObject.getObject() instanceof String) {
                                    String armorstandName = StringUtils.trim((String) watchableObject.getObject());
                                    ShowSpawnTime.getPowerupDetect().detectArmorstand(armorstandName, packetIn.getEntityId());
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
