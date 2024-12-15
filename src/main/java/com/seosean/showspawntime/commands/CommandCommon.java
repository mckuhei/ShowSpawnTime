package com.seosean.showspawntime.commands;

import com.seosean.showspawntime.ShowSpawnTime;
import com.seosean.showspawntime.config.LanguageConfiguration;
import com.seosean.showspawntime.features.leftnotice.LeftNotice;
import com.seosean.showspawntime.utils.DebugUtils;
import com.seosean.showspawntime.utils.PlayerUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.fml.common.FMLLog;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CommandCommon extends CommandBase{
    public CommandCommon() {
    }

    public String getName() {
        return "sst";
    }

    public String getUsage(ICommandSender sender) {
        return "sst";
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args[0].equals("debug")) {
            DebugUtils.sendMessage(LanguageConfiguration.translations.toString());
        }
        if (args[0].equals("feature")) {
            if (args.length == 2 && args[1].equals("glitch")) {
                ITextComponent configHover = new TextComponentString(TextFormatting.WHITE.toString() + "Click to reset the config.");
                ITextComponent configfolder = new TextComponentString(TextFormatting.GOLD.toString() + " [RESET MOD CONFIG]");
                ITextComponent glitchTips = new TextComponentString(TextFormatting.AQUA + ">> " + TextFormatting.WHITE + "If you find out certain glitches in the config screen, you reset the config though clicking: ");
                Style configs = new Style().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, configHover)).setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sst feature glitch reset"));
                configfolder.setStyle(configs);
                PlayerUtils.sendMessage(glitchTips.appendSibling(configfolder));
            } else if(args.length == 3 && args[1].equals("glitch") && args[2].equals("reset")){

                ITextComponent configfolder = new TextComponentString(TextFormatting.RED.toString() + "The config file has been reset, you can now reedit with /sstconfig or /ssthud");
                PlayerUtils.sendMessage(configfolder);

                File fileBak = new File(ShowSpawnTime.getConfiguration().getConfigFile().getAbsolutePath() + "_" +
                        new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".errored");
                FMLLog.severe("An exception occurred while loading config file %s. This file will be renamed to %s " +
                        "and a new config file will be generated.", ShowSpawnTime.getConfiguration().getConfigFile().getName(), fileBak.getName());
                ShowSpawnTime.getConfiguration().getConfigFile().renameTo(fileBak);
                ShowSpawnTime.getMainConfiguration().ConfigLoad();
                ShowSpawnTime.getConfiguration().load();
            } else if(args.length == 1){
                ITextComponent commands = new TextComponentString(TextFormatting.YELLOW + "· " + TextFormatting.GREEN + "/sst mode normal/hard/rip " + TextFormatting.GRAY + "to correct the mode detection if you disconnect." + TextFormatting.GRAY  + " (The feature about wave 3rd zombies display in sidebar.)" +
                TextFormatting.YELLOW + "\n· " + TextFormatting.GREEN + "/sst ins/max/ss 2/3/5/6/7 " + TextFormatting.YELLOW + "to correct the powerups pattern detection if you disconnect." + TextFormatting.GRAY  + " (The feature about showing countdown of powerups.)" +
                TextFormatting.YELLOW + "\n· " + TextFormatting.GREEN + "/sstconfig " + TextFormatting.YELLOW + "to open config GUI." +
                TextFormatting.YELLOW + "\n· " + TextFormatting.GREEN + "/ssthud " + TextFormatting.YELLOW + "to open HUD GUI. "  + TextFormatting.GRAY + "(You can edit the HUD of ZombiesAutoSplits by Seosean with this command as well if you installed it, and it works the same.)" +
                TextFormatting.YELLOW + "\n· " + TextFormatting.GREEN + "/sst checkupdate " + TextFormatting.YELLOW + "to check the latest version.");
                PlayerUtils.sendMessage(commands);
            }
        } else if (args.length > 1 && args[0].equals("copy")) {
            StringBuilder text = new StringBuilder();
            int i;
            if(args.length > 2){
                for (i = 1; i < args.length; ++i) {
                    text.append(args[i]).append(args.length > i + 1 ? " " : "");
                }
            }else{
                text.append(args[1]);
            }
            String copyingText = String.valueOf(text);
            StringSelection selection = new StringSelection(copyingText);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, null);
            String copiedText = "Copied Successfully!";
            ITextComponent copied = new TextComponentString(TextFormatting.GREEN + copiedText);
            PlayerUtils.sendMessage(copied);
        }else if(args.length > 1 && args[0].equals("ins")){
            if(Integer.parseInt(args[1]) == 2 || Integer.parseInt(args[1]) == 3) {
                ShowSpawnTime.getPowerupDetect().setInstaRound(Integer.parseInt(args[1]));
                ITextComponent pattern = new TextComponentString(TextFormatting.GREEN + "Insta Kill Pattern has been set to " + TextFormatting.RED + args[1]);
                PlayerUtils.sendMessage(pattern);
                ITextComponent alert = new TextComponentString(TextFormatting.GRAY + "The pattern will still be reset by mod's detection in r2/r3/r5/... .");
                PlayerUtils.sendMessage(alert);
            }
        }else if(args.length > 1 && args[0].equals("max")){
            if(Integer.parseInt(args[1]) == 2 || Integer.parseInt(args[1]) == 3) {
                ShowSpawnTime.getPowerupDetect().setMaxRound(Integer.parseInt(args[1]));
                ITextComponent pattern = new TextComponentString(TextFormatting.GREEN + "Max Ammo Pattern has been set to " + TextFormatting.RED + args[1]);
                PlayerUtils.sendMessage(pattern);
                ITextComponent alert = new TextComponentString(TextFormatting.GRAY + "The pattern will still be reset by mod's detection in r2/r3/r5/... .");
                PlayerUtils.sendMessage(alert);
            }
        }else if(args.length > 1 && args[0].equals("ss")){
            if(Integer.parseInt(args[1]) == 5 || Integer.parseInt(args[1]) == 6 || Integer.parseInt(args[1]) == 7) {
                ShowSpawnTime.getPowerupDetect().setSSRound(Integer.parseInt(args[1]));
                ITextComponent pattern = new TextComponentString(TextFormatting.GREEN + "Shopping Spree Pattern has been set to " + TextFormatting.RED + args[1]);
                PlayerUtils.sendMessage(pattern);
                ITextComponent alert = new TextComponentString(TextFormatting.GRAY + "The pattern will still be reset by mod's detection in r2/r3/r5/... .");
                PlayerUtils.sendMessage(alert);
            }
        }else if(args.length > 1 && args[0].equals("mode")){
            switch (args[1]) {
                case "normal": {
                    LeftNotice.diff = LeftNotice.Difficulty.NORMAL;
                    ITextComponent pattern = new TextComponentString(TextFormatting.GREEN + "Game mode for wave 3rd counter has been set to " + TextFormatting.RED + "Normal");
                    PlayerUtils.sendMessage(pattern);
                    break;
                }
                case "hard": {
                    LeftNotice.diff = LeftNotice.Difficulty.HARD;
                    ITextComponent pattern = new TextComponentString(TextFormatting.GREEN + "Game mode for wave 3rd counter has been set to " + TextFormatting.RED + "Hard");
                    PlayerUtils.sendMessage(pattern);
                    break;
                }
                case "rip": {
                    LeftNotice.diff = LeftNotice.Difficulty.RIP;
                    ITextComponent pattern = new TextComponentString(TextFormatting.GREEN + "Game mode for wave 3rd counter has been set to " + TextFormatting.RED + "RIP");
                    PlayerUtils.sendMessage(pattern);
                    break;
                }
            }
        }
    }

    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public java.util.List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        java.util.List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("mode");
            completions.add("max");
            completions.add("ins");
            completions.add("ss");
            completions.add("checkupdate");
        } else if (args.length == 2 && !args[0].equals("mode")) {
            if(args[0].equals("ins") || args[0].equals("max")) {
                completions.add("2");
                completions.add("3");
            }else if(args[0].equals("ss")){
                completions.add("5");
                completions.add("6");
                completions.add("7");
            }
        } else if (args.length == 2) {
            completions.add("normal");
            completions.add("hard");
            completions.add("rip");
        }
        return completions;
    }

    // 为什么有反编译的代码
    // $FF: synthetic method
    CommandCommon(Object x1) {
        this();
    }
}