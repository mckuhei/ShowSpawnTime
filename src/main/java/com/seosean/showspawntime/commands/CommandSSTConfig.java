package com.seosean.showspawntime.commands;

import com.seosean.showspawntime.config.gui.ShowSpawnTimeGuiConfig;
import com.seosean.showspawntime.utils.DelayedTask;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class CommandSSTConfig extends CommandBase {
    public CommandSSTConfig() {
    }

    public String getCommandName() {
        return "sstconfig";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "sstconfig";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        GuiScreen parentScreen = Minecraft.getMinecraft().currentScreen;
        new DelayedTask(){
            @Override
            public void run() {
                Minecraft.getMinecraft().displayGuiScreen(new ShowSpawnTimeGuiConfig(parentScreen));
            }
        }.runTaskLater(2);
    }

    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    // $FF: synthetic method
    CommandSSTConfig(Object x1) {
        this();
    }
}