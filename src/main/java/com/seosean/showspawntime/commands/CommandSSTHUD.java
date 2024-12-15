package com.seosean.showspawntime.commands;

import com.seosean.showspawntime.config.hud.ConfigGui;
import com.seosean.showspawntime.utils.DelayedTask;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandSSTHUD extends CommandBase {
    public CommandSSTHUD() {
    }

    public String getName() {
        return "ssthud";
    }

    public String getUsage(ICommandSender sender) {
        return "ssthud";
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        GuiScreen parentScreen = Minecraft.getMinecraft().currentScreen;
        new DelayedTask(){
            @Override
            public void run() {
                Minecraft.getMinecraft().displayGuiScreen(new ConfigGui());
            }
        }.runTaskLater(2);
    }

    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    // $FF: synthetic method
    CommandSSTHUD(Object x1) {
        this();
    }
}