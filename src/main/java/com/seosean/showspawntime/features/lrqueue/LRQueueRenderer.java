package com.seosean.showspawntime.features.lrqueue;

import com.seosean.showspawntime.config.MainConfiguration;
import com.seosean.showspawntime.handler.Renderer;
import com.seosean.showspawntime.utils.LanguageUtils;
import com.seosean.showspawntime.utils.PlayerUtils;
import com.seosean.showspawntime.utils.StringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;

public class LRQueueRenderer extends Renderer {

    @SubscribeEvent
    public void lrCountDown(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.world == null || mc.isSingleplayer()) {
            return;
        }
        if (event.phase!= TickEvent.Phase.START) {
            return;
        }

        EntityPlayerSP p = mc.player;
        if (p == null) {
            return;
        }

        if (displayTime > 0) {
            displayTime --;
        }

        if (lrOffsetCountDown > 0) {
            lrOffsetCountDown -= 1 ;
        } else {
            lrUsings = 0;
        }

    }
    public static int lrUsings = 0;
    public static int lrOffsetCountDown = 0;
    public static int displayTime = 0;
    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        if (!MainConfiguration.LightningRodQueue) {
            return;
        }

        if (!PlayerUtils.isInZombies()) {
            return;
        }
        String message = StringUtils.trim(event.getMessage().getUnformattedText());

        if (message.contains(":")) {
            return;
        }

        if (!message.contains("!") && !message.contains("！")) {
            return;
        }

        if (LanguageUtils.contains(message, "zombies.game.hasspawned")) {
            lrUsings --;
        }
    }

    @Override
    public void onRender(RenderGameOverlayEvent.Post event){
        Minecraft minecraft = Minecraft.getMinecraft();
        if(!MainConfiguration.LightningRodQueue){
            return;
        }

        if (displayTime > 0) {
            float f2 = (float) displayTime - event.getPartialTicks();
            int l1 = (int) (f2 * 255.0F / 20.0F);

            if (l1 > 255) {
                l1 = 255;
            }

            if (l1 > 8) {
                ScaledResolution scaledResolution = new ScaledResolution(minecraft);
                int screenWidth = scaledResolution.getScaledWidth();
                int screenHeight = scaledResolution.getScaledHeight();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);


                String LRQueue1 = "❶";
                String LRQueue2 = "❷";
                String LRQueue3 = "❸";
                String LRQueue4 = "❹";
                String LRQueueCrossBar =  "▬";
                String LRQueueDummy1 = "❶ ▬ ❷ ▬ ❸ ▬ ❹";
                String LRQueueDummy2 = "❶ ▬ ❷";
                String LRQueueDummy3 = "▬ ❷ ▬ ❸ ▬";

                int triggeredColor = 0xFFFF00;
                int disabledColor = 0x808080;
                int crossbarColor = 0x00FF00;
                int firstLight;
                int secondLight;
                int thirthLight;
                int forthLight;

                if(lrUsings == 1){
                    firstLight = triggeredColor;
                    secondLight = disabledColor;
                    thirthLight = disabledColor;
                    forthLight = disabledColor;
                }else if(lrUsings == 2){
                    firstLight = triggeredColor;
                    secondLight = triggeredColor;
                    thirthLight = disabledColor;
                    forthLight = disabledColor;
                }else if(lrUsings == 3){
                    firstLight = triggeredColor;
                    secondLight = triggeredColor;
                    thirthLight = triggeredColor;
                    forthLight = disabledColor;
                }else if(lrUsings == 4){
                    firstLight = triggeredColor;
                    secondLight = triggeredColor;
                    thirthLight = triggeredColor;
                    forthLight = triggeredColor;
                }else{
                    firstLight = disabledColor;
                    secondLight = disabledColor;
                    thirthLight = disabledColor;
                    forthLight = disabledColor;
                }
                minecraft.fontRenderer.drawStringWithShadow(LRQueue1,
                        screenWidth / 2.0F - minecraft.fontRenderer.getStringWidth(LRQueueDummy1) / 2.0F,
                        screenHeight / 1.2F,
                        firstLight + (l1 << 24 & -firstLight));
                minecraft.fontRenderer.drawStringWithShadow(LRQueue2,
                        screenWidth / 2.0F - minecraft.fontRenderer.getStringWidth(LRQueueDummy2) / 2.0F,
                        screenHeight / 1.2F,
                        secondLight + (l1 << 24 & -secondLight));
                minecraft.fontRenderer.drawStringWithShadow(LRQueue3,
                        screenWidth / 2.0F + minecraft.fontRenderer.getStringWidth(LRQueueDummy2) / 2.0F - minecraft.fontRenderer.getStringWidth(LRQueue1),
                        screenHeight / 1.2F,
                        thirthLight + (l1 << 24 & -thirthLight));
                minecraft.fontRenderer.drawStringWithShadow(LRQueue4,
                        screenWidth / 2.0F + minecraft.fontRenderer.getStringWidth(LRQueueDummy1) / 2.0F - minecraft.fontRenderer.getStringWidth(LRQueue1),
                        screenHeight / 1.2F,
                        forthLight + (l1 << 24 & -forthLight));

                minecraft.fontRenderer.drawStringWithShadow(LRQueueCrossBar,
                        screenWidth / 2.0F - minecraft.fontRenderer.getStringWidth(LRQueueDummy3) / 2.0F,
                        screenHeight / 1.2F,
                        crossbarColor + (l1 << 24 & -crossbarColor));
                minecraft.fontRenderer.drawStringWithShadow(LRQueueCrossBar,
                        screenWidth / 2.0F - minecraft.fontRenderer.getStringWidth(LRQueueCrossBar) / 2.0F,
                        screenHeight / 1.2F,
                        crossbarColor + (l1 << 24 & -crossbarColor));
                minecraft.fontRenderer.drawStringWithShadow(LRQueueCrossBar,
                        screenWidth / 2.0F + minecraft.fontRenderer.getStringWidth(LRQueueDummy3) / 2.0F - minecraft.fontRenderer.getStringWidth(LRQueueCrossBar),
                        screenHeight / 1.2F,
                        crossbarColor + (l1 << 24 & -crossbarColor));
                GlStateManager.disableBlend();
            }
        }
    }
}
