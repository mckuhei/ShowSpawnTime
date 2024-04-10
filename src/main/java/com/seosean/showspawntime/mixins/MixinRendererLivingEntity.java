package com.seosean.showspawntime.mixins;

import com.seosean.showspawntime.ShowSpawnTime;
import com.seosean.showspawntime.commands.CommandDebug;
import com.seosean.showspawntime.config.MainConfiguration;
import com.seosean.showspawntime.features.powerups.Powerup;
import com.seosean.showspawntime.utils.DebugUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(RendererLivingEntity.class)
public abstract class MixinRendererLivingEntity<T extends EntityLivingBase> extends Render<T> {

    protected MixinRendererLivingEntity(RenderManager renderManager) {
        super(renderManager);
    }

    @ModifyArgs(method = "renderName*", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RendererLivingEntity;renderOffsetLivingLabel(Lnet/minecraft/entity/Entity;DDDLjava/lang/String;FD)V"))
    private void modifiedArgs(Args args) {
        if (MainConfiguration.PowerupCountDown) {
            Entity entity = args.get(0);
            String str = args.get(4);

            if (entity instanceof EntityArmorStand && Powerup.powerups.containsKey(entity)) {
                int offsetTime = Powerup.powerups.get(entity).getOffsetTime() / 2;
                args.set(4, str + EnumChatFormatting.WHITE + (" ✨" + (offsetTime / 10.0) + "s"));
            }
        }
    }



    @Redirect(method = "renderName*", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RendererLivingEntity;renderOffsetLivingLabel(Lnet/minecraft/entity/Entity;DDDLjava/lang/String;FD)V"))
    private void onRedirecting(RendererLivingEntity<?> instance, Entity entityIn, double x, double y, double z, String str, float p_177069_9_, double p_177069_10_) {
        if (entityIn instanceof EntityArmorStand && Powerup.powerups.containsKey(entityIn)) {
            this.overrideRenderLivingLabel((T) entityIn, str, x, y, z, 128);
        } else {
            this.renderOffsetLivingLabel((T) entityIn, x, y - (((T) entityIn).isChild() ? (double)(entityIn.height / 2.0F) : 0.0D), z, str, 0.02666667F, p_177069_10_);
        }
    }



    protected void overrideRenderLivingLabel(T entityIn, String str, double x, double y, double z, int maxDistance)
    {
        double d0 = entityIn.getDistanceSqToEntity(this.renderManager.livingPlayer);
        if (d0 <= (double)(maxDistance * maxDistance))
        {
            FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
            float f = 1.6F;
            float f1 = 0.016666668F * f;
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)x + 0.0F, (float)y + entityIn.height + 0.5F, (float)z);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(-f1, -f1, f1);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            int i = 0;

            if (str.equals("deadmau5"))
            {
                i = -10;
            }

            int j = fontrenderer.getStringWidth(str) / 2;
            GlStateManager.disableTexture2D();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            worldrenderer.pos((double)(-j - 1), (double)(-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldrenderer.pos((double)(-j - 1), (double)(8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldrenderer.pos((double)(j + 1), (double)(8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldrenderer.pos((double)(j + 1), (double)(-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();

            boolean flag = false;
            if (str.contains("✨")) {
                String[] strings = str.split("✨");
                if (strings.length == 2) {
                    fontrenderer.drawString(strings[0], (float) (-fontrenderer.getStringWidth(str) / 2.0), (float) i, MainConfiguration.powerupNameTagRenderColor, MainConfiguration.PowerupNameTagShadow);
                    fontrenderer.drawString(strings[1].replace("§f", ""), (float) (-fontrenderer.getStringWidth(str) / 2.0 + fontrenderer.getStringWidth("A" + strings[0])), (float) i, MainConfiguration.powerupCountDownRenderColor, true);

                    GlStateManager.enableDepth();
                    GlStateManager.depthMask(true);

                    fontrenderer.drawString(strings[0], (float) (-fontrenderer.getStringWidth(str) / 2.0), (float) i, -1, MainConfiguration.PowerupNameTagShadow);
                    fontrenderer.drawString(strings[1].replace("§f", ""), (float) (-fontrenderer.getStringWidth(str) / 2.0 + fontrenderer.getStringWidth("A" + strings[0])), (float) i, 0x99CCFF, true);
                    flag = true;
                }
            }

            if (!flag) {
                GlStateManager.enableDepth();
                GlStateManager.depthMask(true);
                fontrenderer.drawString(str, (float) (-fontrenderer.getStringWidth(str) / 2.0), i, MainConfiguration.powerupNameTagRenderColor, MainConfiguration.PowerupNameTagShadow);
            }
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
        }
    }
}
