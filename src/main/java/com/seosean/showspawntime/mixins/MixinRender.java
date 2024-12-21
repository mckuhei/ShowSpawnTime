package com.seosean.showspawntime.mixins;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.seosean.showspawntime.config.MainConfiguration;
import com.seosean.showspawntime.features.powerups.Powerup;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.text.TextFormatting;

@Mixin(Render.class)
public abstract class MixinRender{

	@Shadow@Final
	protected RenderManager renderManager;
	
	@Shadow
	public abstract FontRenderer getFontRendererFromRenderManager();
	
    @Shadow
    protected abstract void renderLivingLabel(Entity entityIn, String str, double x, double y, double z, int maxDistance);

    @Redirect(method = "renderEntityName(Lnet/minecraft/entity/Entity;DDDLjava/lang/String;D)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/Render;renderLivingLabel(Lnet/minecraft/entity/Entity;Ljava/lang/String;DDDI)V"))
    private void onRedirecting(Render<?> instance, Entity entityIn, String str, double x, double y, double z, int maxDistance) {
        if (MainConfiguration.PowerupCountDown && entityIn instanceof EntityArmorStand && Powerup.powerups.containsKey(entityIn)) {
            this.showSpawnTime$renderLivingLabel(entityIn, str, x, y, z, 128);
        } else {
            this.renderLivingLabel(entityIn, str, x, y, z, maxDistance);
        }
    }

    
    @Unique
    protected void showSpawnTime$renderLivingLabel(Entity entityIn, String str, double x, double y, double z, int maxDistance) {
        int offsetTime = Powerup.powerups.get((EntityArmorStand) entityIn).getOffsetTime() / 2;
        String nameTagStr = str;
        String countdownStr = TextFormatting.WHITE + (" " + (offsetTime / 10.0) + "s");
        str = str + countdownStr;

        double d0 = entityIn.getDistanceSq(this.renderManager.renderViewEntity);

        if (d0 <= (double)(maxDistance * maxDistance))
        {
            boolean isSneaking = entityIn.isSneaking();
            float viewerYaw = this.renderManager.playerViewY;
            float viewerPitch = this.renderManager.playerViewX;
            boolean isThirdPersonFrontal = this.renderManager.options.thirdPersonView == 2;
            float f2 = entityIn.height + 0.5F - (isSneaking ? 0.25F : 0.0F);
            int verticalShift = "deadmau5".equals(str) ? -10 : 0;
            FontRenderer fontRendererIn = this.getFontRendererFromRenderManager();
            y = y + f2;
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-viewerYaw, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate((float)(isThirdPersonFrontal ? -1 : 1) * viewerPitch, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(-0.025F, -0.025F, 0.025F);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);

            if (!isSneaking)
            {
                GlStateManager.disableDepth();
            }

            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            int i = fontRendererIn.getStringWidth(str) / 2;
            GlStateManager.disableTexture2D();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
            bufferbuilder.pos((double)(-i - 1), (double)(-1 + verticalShift), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            bufferbuilder.pos((double)(-i - 1), (double)(8 + verticalShift), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            bufferbuilder.pos((double)(i + 1), (double)(8 + verticalShift), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            bufferbuilder.pos((double)(i + 1), (double)(-1 + verticalShift), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();

            fontRendererIn.drawString(nameTagStr, -fontRendererIn.getStringWidth(str) / 2, verticalShift, MainConfiguration.powerupNameTagRenderColor, MainConfiguration.PowerupNameTagShadow);
            fontRendererIn.drawString(countdownStr.replace("§f", ""), (float) (-fontRendererIn.getStringWidth(str) / 2.0 + fontRendererIn.getStringWidth("A" + nameTagStr)), (float) verticalShift, MainConfiguration.powerupCountDownRenderColor, true);
            GlStateManager.enableDepth();

            GlStateManager.depthMask(true);
            fontRendererIn.drawString(nameTagStr, -fontRendererIn.getStringWidth(str) / 2, verticalShift, -1, MainConfiguration.PowerupNameTagShadow);
            fontRendererIn.drawString(countdownStr.replace("§f", ""), (float) (-fontRendererIn.getStringWidth(str) / 2.0 + fontRendererIn.getStringWidth("A" + nameTagStr)), (float) verticalShift, 0x99CCFF, true);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
        }
    }
}
