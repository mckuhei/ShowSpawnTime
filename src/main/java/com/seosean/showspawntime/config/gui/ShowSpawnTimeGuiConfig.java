package com.seosean.showspawntime.config.gui;

import com.seosean.showspawntime.ShowSpawnTime;
import com.seosean.showspawntime.config.MainConfiguration;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.ArrayList;
import java.util.List;

public class ShowSpawnTimeGuiConfig extends GuiConfig {
    public ShowSpawnTimeGuiConfig(GuiScreen parent) {
        super(parent, getElements(),
                ShowSpawnTime.MODID,
                false,
                false,
                "ShowSpawnTime Configuration"
        );
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        super.actionPerformed(button);
        try
        {
            if ((configID != null || this.parentScreen == null || !(this.parentScreen instanceof GuiConfig))
                    && (this.entryList.hasChangedEntry(true)))
            {
                boolean requiresMcRestart = this.entryList.saveConfigElements();

                if (Loader.isModLoaded(modID))
                {
                    ConfigChangedEvent event = new ConfigChangedEvent.OnConfigChangedEvent(modID, configID, isWorldRunning, requiresMcRestart);
                    MinecraftForge.EVENT_BUS.post(event);

                    if (!event.getResult().equals(Event.Result.DENY)) {
                        MinecraftForge.EVENT_BUS.post(new ConfigChangedEvent.PostConfigChangedEvent(modID, configID, isWorldRunning, requiresMcRestart));
                    }

                    if (this.parentScreen instanceof GuiConfig) {
                        ((GuiConfig) this.parentScreen).needsRefresh = true;
                    }
                }
            }
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
    }

    private static List<IConfigElement> getElements() {
        List<IConfigElement> list = new ArrayList<>();

        list.add(new DummyConfigElement.DummyCategoryElement("SST Related", "", new ArrayList<>(MainConfiguration.sstRelated.values())));
        list.add(new DummyConfigElement.DummyCategoryElement("ADDONS Related", "", new ArrayList<>(MainConfiguration.powerupRelated.values())));
        list.add(new DummyConfigElement.DummyCategoryElement("RECORD Related", "", new ArrayList<>(MainConfiguration.recordRelated.values())));
        return list;
    }
}
