package com.seosean.showspawntime.mixins;

import com.seosean.showspawntime.config.MainConfiguration;
import com.seosean.showspawntime.features.powerups.Powerup;
import com.seosean.showspawntime.utils.DebugUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Render.class)
public abstract class MixinRender<T extends Entity> {
    @Inject(method = "renderEntityOnFire", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;color(FFFF)V", shift = At.Shift.AFTER))
    private void renderEntityOnFire(Entity entity, double x, double y, double z, float partialTicks, CallbackInfo c) {
        if(MainConfiguration.PlayerInvisible && entity instanceof EntityPlayer && entity != Minecraft.getMinecraft().thePlayer) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity) < 1.4 ? 0 : 1.0F);
        }
    }

//    @Inject(method = "renderLivingLabel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawString(Ljava/lang/String;III)I", ordinal = 1, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILSOFT)
//    private void onInjecting(T entityIn, String str, double x, double y, double z, int maxDistance, CallbackInfo ci, double d0, FontRenderer fontrenderer, float f, float f1, Tessellator tessellator, WorldRenderer worldrenderer, int i, int j) {
//        if (entityIn instanceof EntityArmorStand && Powerup.powerups.containsKey(entityIn)) {
//            DebugUtils.sendMessage("Debug 1");
//            if (str.contains("§f")) {
//                String[] strings = str.split("§f");
//                DebugUtils.sendMessage("Debug 2:" + strings.length);
//                if (strings.length == 2) {
//                    DebugUtils.sendMessage("Debug 3");
//                    fontrenderer.drawString(strings[1], -fontrenderer.getStringWidth(str) / 2 + fontrenderer.getStringWidth(strings[0]), i, 0x99CCFFFF);
//                }
//            }
//        }
//    }

}
