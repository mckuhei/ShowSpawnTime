package com.seosean.showspawntime.config.hud;

import com.seosean.showspawntime.ShowSpawnTime;
import com.seosean.showspawntime.config.MainConfiguration;
import com.seosean.showspawntime.utils.DelayedTask;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigGui extends GuiScreen {
   private int selected = -1;
   private int scroll = 0;
   private boolean isDragging;
   private Point dragOffset;
   private List<HudCoordinate> boxes = new ArrayList<>();

   @Override
   public void handleMouseInput() throws IOException {
      super.handleMouseInput();

      int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
      int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

      int button = Mouse.getEventButton();
      boolean mouseButtonDown = Mouse.getEventButtonState();

      for (HudCoordinate box : boxes) {
         if (box.isMouseOver(mouseX, mouseY)) {
            if (button == 0) { // 左键
               if (mouseButtonDown) { // 鼠标按下
                  box.onMousePressed(mouseX, mouseY);
               } else { // 鼠标释放
                  box.onMouseReleased();
               }
            }
         }

         if (box.isDragging) { // 鼠标拖动
            box.onMouseDragged(mouseX, mouseY);
         }
      }
   }

   public void initGui() {
      MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.InitGuiEvent.Pre(this, buttonList));
      super.initGui();
      MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.InitGuiEvent.Post(this, buttonList));
      int widthTime = this.fontRendererObj.getStringWidth("➤ W2 00:00");
      int widthPowerup = this.fontRendererObj.getStringWidth("-BONUS GOLD - 00:00 ");
      int widthDPSCounter = this.fontRendererObj.getStringWidth("DPS: INSTA KILL");
      ScaledResolution sr = new ScaledResolution(this.mc);
      this.buttonList.add(new GuiButton(1, sr.getScaledWidth() / 2 - 82, sr.getScaledHeight() - 25, 80, 20, "Done"));
      this.buttonList.add(new GuiButton(2, sr.getScaledWidth() / 2 - 82 + 85, sr.getScaledHeight() - 25, 80, 20, "Reset"));
      boxes.clear();
      HudCoordinate boxSpawnTime = new HudCoordinate(0, MainConfiguration.getXSpawnTime(), MainConfiguration.getYSpawnTime(), widthTime, this.fontRendererObj.FONT_HEIGHT * 6, this.width, this.height);
      boxes.add(boxSpawnTime);
      HudCoordinate boxPowerup = new HudCoordinate(1, MainConfiguration.getXPowerup(), MainConfiguration.getYPowerup(), widthPowerup, this.fontRendererObj.FONT_HEIGHT * 6, this.width, this.height);
      boxes.add(boxPowerup);
      HudCoordinate boxDPSCounter = new HudCoordinate(2, MainConfiguration.getXDPSCounter(), MainConfiguration.getYDPSCounter(), widthDPSCounter, this.fontRendererObj.FONT_HEIGHT, this.width, this.height);
      boxes.add(boxDPSCounter);
   }

   protected void mouseClicked(int mouseX, int mouseY, int p_mouseClicked_3_) throws IOException {
      super.mouseClicked(mouseX, mouseY, p_mouseClicked_3_);
   }

   protected void mouseReleased(int p_mouseReleased_1_, int p_mouseReleased_2_, int p_mouseReleased_3_) {
      super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_2_, p_mouseReleased_3_);
   }

   protected void keyTyped(char p_keyTyped_1_, int p_keyTyped_2_) throws IOException {
      super.keyTyped(p_keyTyped_1_, p_keyTyped_2_);
   }

   public void drawScreen(int mouseX, int mouseY, float p_drawScreen_3_) {
      ScaledResolution sr = new ScaledResolution(this.mc);
      this.drawDefaultBackground();
      for (HudCoordinate box : boxes) {
         box.draw(this);
      }
      this.fontRendererObj.drawStringWithShadow("ShowSpawnTime", (float)sr.getScaledWidth() / 2.0F - (float)this.fontRendererObj.getStringWidth("ShowSpawnTime") / 2.0F, 10.0F, Color.WHITE.getRGB());
      this.fontRendererObj.drawStringWithShadow("Click \"Done\" to save your current HUD position settings.", (float)sr.getScaledWidth() / 2.0F - (float)this.fontRendererObj.getStringWidth("Click \"Done\" to save your current HUD position settings.") / 2.0F, this.height/2, Color.WHITE.getRGB());

      super.drawScreen(mouseX, mouseY, p_drawScreen_3_);
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      switch(button.id) {
         case 1: {
            for (HudCoordinate box : boxes) {
               if (box.getContents() == 0) {
                  MainConfiguration.XSpawnTime = box.x;
                  MainConfiguration.YSpawnTime = box.y;
                  ShowSpawnTime.getConfig().get(Configuration.CATEGORY_CLIENT, "XSpawnTime", -1).set(box.x);
                  ShowSpawnTime.getConfig().get(Configuration.CATEGORY_CLIENT, "YSpawnTime", -1).set(box.y);
               } else if (box.getContents() == 1) {
                  MainConfiguration.XPowerup = box.x;
                  MainConfiguration.YPowerup = box.y;
                  ShowSpawnTime.getConfig().get(Configuration.CATEGORY_CLIENT, "XPowerup", -1).set(box.x);
                  ShowSpawnTime.getConfig().get(Configuration.CATEGORY_CLIENT, "YPowerup", -1).set(box.y);
               } else if (box.getContents() == 2) {
                  MainConfiguration.XDPSCounter = box.x;
                  MainConfiguration.YDPSCounter = box.y;
                  ShowSpawnTime.getConfig().get(Configuration.CATEGORY_CLIENT, "XDPSCounter", -1).set(box.x);
                  ShowSpawnTime.getConfig().get(Configuration.CATEGORY_CLIENT, "YDPSCounter", -1).set(box.y);
               }
            }
            ShowSpawnTime.getConfig().save();
            this.mc.displayGuiScreen(null);
            break;
         }
         case 2: {
            for (HudCoordinate box : boxes) {
               if (box.getContents() == 0) {
                  ShowSpawnTime.getConfig().get(Configuration.CATEGORY_CLIENT, "XSpawnTime", -1).set(-1);
                  ShowSpawnTime.getConfig().get(Configuration.CATEGORY_CLIENT, "YSpawnTime", -1).set(-1);
                  MainConfiguration.XSpawnTime = -1;
                  MainConfiguration.YSpawnTime = -1;
                  new DelayedTask() {
                     @Override
                     public void run() {
                        box.x = MainConfiguration.getXSpawnTime();
                        box.absoluteX = (int)(MainConfiguration.getXSpawnTime() * ConfigGui.this.width);
                        box.y = MainConfiguration.getYSpawnTime();
                        box.absoluteY = (int)(MainConfiguration.getYSpawnTime() * ConfigGui.this.height);
                     }
                  }.runTaskLater(2);


               } else if (box.getContents() == 1) {
                  ShowSpawnTime.getConfig().get(Configuration.CATEGORY_CLIENT, "XPowerup", -1).set(-1);
                  ShowSpawnTime.getConfig().get(Configuration.CATEGORY_CLIENT, "YPowerup", -1).set(-1);
                  MainConfiguration.XPowerup = -1;
                  MainConfiguration.YPowerup = -1;
                  new DelayedTask() {
                     @Override
                     public void run() {
                        box.x = MainConfiguration.getXPowerup();
                        box.absoluteX = (int)(MainConfiguration.getXPowerup() * ConfigGui.this.width);
                        box.y = MainConfiguration.getYPowerup();
                        box.absoluteY = (int)(MainConfiguration.getYPowerup() * ConfigGui.this.height);
                     }
                  }.runTaskLater(2);


               } else if (box.getContents() == 2) {
                  ShowSpawnTime.getConfig().get(Configuration.CATEGORY_CLIENT, "XDPSCounter", -1).set(-1);
                  ShowSpawnTime.getConfig().get(Configuration.CATEGORY_CLIENT, "YDPSCounter", -1).set(-1);
                  MainConfiguration.XDPSCounter = -1;
                  MainConfiguration.YDPSCounter = -1;
                  new DelayedTask() {
                     @Override
                     public void run() {
                        box.x = MainConfiguration.getXDPSCounter();
                        box.absoluteX = (int)(MainConfiguration.getXDPSCounter() * ConfigGui.this.width);
                        box.y = MainConfiguration.getYDPSCounter();
                        box.absoluteY = (int)(MainConfiguration.getYDPSCounter() * ConfigGui.this.height);
                     }
                  }.runTaskLater(2);


               }
            }
            ShowSpawnTime.getConfig().save();
            break;
         }
         default: {
            ShowSpawnTime.getConfig().save();
            break;
         }
      }
   }

   public void onGuiClosed() {
      super.onGuiClosed();
   }
}
