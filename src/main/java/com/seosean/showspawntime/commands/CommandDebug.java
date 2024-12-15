package com.seosean.showspawntime.commands;

import com.seosean.showspawntime.ShowSpawnTime;
import com.seosean.showspawntime.config.LanguageConfiguration;
import com.seosean.showspawntime.features.UpdateDetect;
import com.seosean.showspawntime.handler.LanguageDetector;
import com.seosean.showspawntime.utils.DebugUtils;
import com.seosean.showspawntime.utils.GameUtils;
import com.seosean.showspawntime.utils.LanguageUtils;
import com.seosean.showspawntime.utils.PlayerUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandDebug extends CommandBase {
    public CommandDebug() {
    }

    public String getName() {
        return "sstdebug";
    }

    public String getUsage(ICommandSender sender) {
        return "sstdebug";
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        String arg0 = args[0].toLowerCase();
        switch (arg0) {
            case "lang": {
                LanguageConfiguration.translations.forEach((key, value) -> DebugUtils.sendMessage(key + ": " + value));
                LanguageDetector.detectLanguage();
                DebugUtils.sendMessage(LanguageDetector.language);

                break;
            }
            case "title": {
                DebugUtils.sendMessage(String.valueOf(PlayerUtils.isInZombiesTitle()));
                DebugUtils.sendMessage(ShowSpawnTime.getScoreboardManager().getTitle());
                break;
            }
            case "zbleft": {
                DebugUtils.sendMessage(String.valueOf(LanguageUtils.isZombiesLeft(ShowSpawnTime.getScoreboardManager().getContent(4))));
                DebugUtils.sendMessage(ShowSpawnTime.getScoreboardManager().getContent(4));
                break;
            }
            case "map": {
                LanguageUtils.ZombiesMap map = LanguageUtils.getMap();
                DebugUtils.sendMessage(String.valueOf(map));
                break;
            }
            case "round": {
                DebugUtils.sendMessage(String.valueOf(ShowSpawnTime.getSpawnTimes().currentRound));
                break;
            }
            case "tick": {
                DebugUtils.sendMessage(String.valueOf(ShowSpawnTime.getGameTickHandler().getGameTick()));
                break;
            }
            case "version": {
                DebugUtils.sendMessage(String.valueOf(UpdateDetect.status));
                DebugUtils.sendMessage(UpdateDetect.newestVersion);
                break;
            }
            case "players": {
                DebugUtils.sendMessage(GameUtils.playerList.toString());
                break;
            }
            case "type": {
                type = Integer.parseInt(args[1]);
                break;
            }
            case "block": {
                double x = Double.parseDouble(args[1]);
                double y = Double.parseDouble(args[2]);
                double z = Double.parseDouble(args[3]);
                BlockPos blockPos = new BlockPos(x, y, z);
                IBlockState blockState = sender.getEntityWorld().getBlockState(blockPos);
                DebugUtils.sendMessage(blockState.getBlock().getUnlocalizedName());
                break;
            }
        }
    }

    public static int type = 1;
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }
}
