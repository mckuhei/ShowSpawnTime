package com.seosean.showspawntime.config.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HudCoordinate {
    public double x, y;
    public int absoluteX, absoluteY;
    private int width, height;
    public boolean isDragging;
    private int dragOffsetX, dragOffsetY;
    private int screenWidth, screenHeight;
    private int contents;
    private FontRenderer fontRenderer;
    private boolean resizable;
    private int resizeCorner;

    public HudCoordinate(int contents, double x, double y, int width, int height, int screenWidth, int screenHeight) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isDragging = false;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.contents = contents;
        this.fontRenderer = Minecraft.getMinecraft().fontRenderer;
        this.resizable = true;
        this.resizeCorner = 0;
    }

    public int getContents(){
        return contents;
    }

    public void draw(GuiScreen gui) {
        this.absoluteX = (int) (x * screenWidth);
        this.absoluteY = (int) (y * screenHeight);
        int color = isDragging ? 0x80FFFFFF : 0x00000000;
        if (contents == 0) {
            Gui.drawRect(absoluteX, absoluteY, absoluteX + width, absoluteY + height, color);
            int widthDirector = this.fontRenderer.getStringWidth("➤ ");
            this.fontRenderer.drawStringWithShadow("➤ ", absoluteX, absoluteY + this.fontRenderer.FONT_HEIGHT * 3, 0xCC00CC);
            this.fontRenderer.drawStringWithShadow("W1 00:10", absoluteX + widthDirector, absoluteY + this.fontRenderer.FONT_HEIGHT * 3, 0xFFFF00);
            this.fontRenderer.drawStringWithShadow("W2 00:20", absoluteX + widthDirector, absoluteY + this.fontRenderer.FONT_HEIGHT * 4, 0x808080);
            this.fontRenderer.drawStringWithShadow("W3 00:30", absoluteX + widthDirector, absoluteY + this.fontRenderer.FONT_HEIGHT * 5, 0x808080);
            this.fontRenderer.drawStringWithShadow("00:00.0", absoluteX + widthDirector + (this.fontRenderer.getStringWidth("W1 00:10") - this.fontRenderer.getStringWidth("00:00.0")), absoluteY + this.fontRenderer.FONT_HEIGHT * 6, 0xFFFFFF);
        } else if (contents == 1) {
            Gui.drawRect(absoluteX, absoluteY, absoluteX + width  + this.fontRenderer.getStringWidth(" "), absoluteY + height, color);
            this.fontRenderer.drawStringWithShadow(" BONUS GOLD - 00:00", absoluteX, absoluteY, 0xFFFF00);
            this.fontRenderer.drawStringWithShadow(" BONUS GOLD - 00:00", absoluteX, absoluteY + this.fontRenderer.FONT_HEIGHT, 0xFFFF55);
            this.fontRenderer.drawStringWithShadow(" BONUS GOLD - 00:00", absoluteX, absoluteY + this.fontRenderer.FONT_HEIGHT * 2, 0xFFFF55);
            this.fontRenderer.drawStringWithShadow(" BONUS GOLD - 00:00", absoluteX, absoluteY + this.fontRenderer.FONT_HEIGHT * 3, 0xFFFF55);
            this.fontRenderer.drawStringWithShadow(" BONUS GOLD - 00:00", absoluteX, absoluteY + this.fontRenderer.FONT_HEIGHT * 4, 0xFFFF55);
            this.fontRenderer.drawStringWithShadow(" BONUS GOLD - 00:00", absoluteX, absoluteY + this.fontRenderer.FONT_HEIGHT * 5, 0xFFFF55);
        } else if (contents == 2) {
            Gui.drawRect(absoluteX, absoluteY, absoluteX + width, absoluteY + height, color);
            this.fontRenderer.drawStringWithShadow(TextFormatting.GOLD + "DPS" + TextFormatting.WHITE + ":" + TextFormatting.RED + " INSTA KILL", absoluteX, absoluteY, 0xFFFFFF);
        } else if(contents == 3) {
            Gui.drawRect(absoluteX, absoluteY, absoluteX + width, absoluteY + height, color);
            this.fontRenderer.drawStringWithShadow("0:00:0", absoluteX, absoluteY, 0xFFFFFF);
        }
    }

    @SubscribeEvent
    public static void onInitGui(GuiScreenEvent.InitGuiEvent event) {
        if (event.getGui() instanceof ConfigGui) {
            // 进行相应的操作
        }
    }

    public boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= absoluteX && mouseX <= absoluteX + width && mouseY >= absoluteY && mouseY <= absoluteY + height;
    }

    public void onMousePressed(int mouseX, int mouseY) {
        if (isMouseOver(mouseX, mouseY)) {
            isDragging = true;
            dragOffsetX = absoluteX - mouseX;
            dragOffsetY = absoluteY - mouseY;
        }
    }

    public void onMouseReleased() {
        isDragging = false;
        resizable = false;
        resizeCorner = 0;
    }

    public void onMouseDragged(int mouseX, int mouseY) {
        if (isDragging) {
            absoluteX = mouseX + dragOffsetX;
            absoluteY = mouseY + dragOffsetY;
            x = (double)absoluteX / (double)screenWidth;
            y = (double)absoluteY / (double)screenHeight;

            // 检查是否超出屏幕范围
            if (x < 0) {
                absoluteX = 0;
                x = 0;
            } else if (absoluteX + width > screenWidth) {
                absoluteX = screenWidth - width;
                x = (double)absoluteX / (double)screenWidth;
            }
            if (y < 0) {
                absoluteY = 0;
                y = 0;
            } else if (absoluteY + height > screenHeight) {
                absoluteY = screenHeight - height;
                y = (double)absoluteY / (double)screenHeight;
            }
        }
    }
}