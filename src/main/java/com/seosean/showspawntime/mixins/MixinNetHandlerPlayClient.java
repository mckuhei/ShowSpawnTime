package com.seosean.showspawntime.mixins;

import com.seosean.showspawntime.ShowSpawnTime;
import com.seosean.showspawntime.utils.StringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class MixinNetHandlerPlayClient {

    @Inject(method = "handleEntityMetadata", at = @At(value = "HEAD"))
    public void handleEntityMetadata(S1CPacketEntityMetadata packetIn, CallbackInfo callbackInfo)
    {
        if (Minecraft.getMinecraft().theWorld.getEntityByID(packetIn.getEntityId()) instanceof EntityArmorStand) {
            for (DataWatcher.WatchableObject watchableObject : packetIn.func_149376_c()) {
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
