package com.seosean.showspawntime.config.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

import java.util.Set;

public class ShowSpawnTimeGuiFactory implements IModGuiFactory {
    @Override
    public void initialize(Minecraft minecraft) {

    }

    public boolean hasConfigGui() {
        return true;
    }
    
    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen) {
		return new ShowSpawnTimeGuiConfig(parentScreen);
	}
}
