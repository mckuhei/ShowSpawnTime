package com.seosean.showspawntime;

import com.seosean.showspawntime.commands.CommandCommon;
import com.seosean.showspawntime.commands.CommandDebug;
import com.seosean.showspawntime.commands.CommandSSTConfig;
import com.seosean.showspawntime.commands.CommandSSTHUD;
import com.seosean.showspawntime.config.ConfigTip;
import com.seosean.showspawntime.config.LanguageConfiguration;
import com.seosean.showspawntime.config.MainConfiguration;
import com.seosean.showspawntime.handler.GameTickHandler;
import com.seosean.showspawntime.handler.ScoreboardManager;
import com.seosean.showspawntime.features.Renderer;
import com.seosean.showspawntime.features.dpscounter.DPSCounter;
import com.seosean.showspawntime.features.dpscounter.DPSCounterRenderer;
import com.seosean.showspawntime.features.lrqueue.LRQueueRenderer;
import com.seosean.showspawntime.features.powerups.PowerupDetect;
import com.seosean.showspawntime.features.powerups.PowerupRenderer;
import com.seosean.showspawntime.features.spawntimes.SpawnNotice;
import com.seosean.showspawntime.features.spawntimes.SpawnTimeRenderer;
import com.seosean.showspawntime.features.spawntimes.SpawnTimes;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Mod(modid = ShowSpawnTime.MODID, version = ShowSpawnTime.VERSION,
        guiFactory = "com.seosean.showspawntime.config.gui.ShowSpawnTimeGuiFactory"

)
public class ShowSpawnTime
{
    public static final String MODID = "showspawntime";
    public static final String VERSION = "2.0";
    private static Logger LOGGER;
    private static Configuration CONFIGURATION;
    public static final String EMOJI_REGEX = "(?:[\uD83C\uDF00-\uD83D\uDDFF]|[\uD83E\uDD00-\uD83E\uDDFF]|[\uD83D\uDE00-\uD83D\uDE4F]|[\uD83D\uDE80-\uD83D\uDEFF]|[\u2600-\u26FF]\uFE0F?|[\u2700-\u27BF]\uFE0F?|\u24C2\uFE0F?|[\uD83C\uDDE6-\uD83C\uDDFF]{1,2}|[\uD83C\uDD70\uD83C\uDD71\uD83C\uDD7E\uD83C\uDD7F\uD83C\uDD8E\uD83C\uDD91-\uD83C\uDD9A]\uFE0F?|[\u0023\u002A\u0030-\u0039]\uFE0F?\u20E3|[\u2194-\u2199\u21A9-\u21AA]\uFE0F?|[\u2B05-\u2B07\u2B1B\u2B1C\u2B50\u2B55]\uFE0F?|[\u2934\u2935]\uFE0F?|[\u3030\u303D]\uFE0F?|[\u3297\u3299]\uFE0F?|[\uD83C\uDE01\uD83C\uDE02\uD83C\uDE1A\uD83C\uDE2F\uD83C\uDE32-\uD83C\uDE3A\uD83C\uDE50\uD83C\uDE51]\uFE0F?|[\u203C\u2049]\uFE0F?|[\u25AA\u25AB\u25B6\u25C0\u25FB-\u25FE]\uFE0F?|[\u00A9\u00AE]\uFE0F?|[\u2122\u2139]\uFE0F?|\uD83C\uDC04\uFE0F?|\uD83C\uDCCF\uFE0F?|[\u231A\u231B\u2328\u23CF\u23E9-\u23F3\u23F8-\u23FA]\uFE0F?)";
    public static final String COLOR_REGEX = "§[a-zA-Z0-9]";


    private static ShowSpawnTime INSTANCE;
    private static MainConfiguration MAIN_CONFIGURATION;
    private static LanguageConfiguration LANGUAGE_CONFIGURATION;

    private static final ScoreboardManager SCOREBOARD_MANAGER = new ScoreboardManager();
    private static final List<Renderer> RENDERER_LIST = new ArrayList<>();

    private static GameTickHandler gameTickHandler;
    private static PowerupDetect powerupDetect;
    private static PowerupRenderer powerupRenderer;
    private static SpawnTimeRenderer spawnTimeRenderer;
    private static DPSCounterRenderer dpsCounterRenderer;
    private static LRQueueRenderer lrQueueRenderer;
    private static SpawnTimes spawnTimes;
    private static SpawnNotice spawnNotice;
    private static DPSCounter dpsCounter;
    @EventHandler
    public void preinit(FMLPreInitializationEvent event){

        LOGGER = event.getModLog();

        File modConfigFolder = new File(event.getModConfigurationDirectory(), MODID);
        if (!modConfigFolder.exists()) {
            modConfigFolder.mkdirs();
        }

        CONFIGURATION = new Configuration(new File(modConfigFolder, MODID + ".cfg"));
        LANGUAGE_CONFIGURATION = createLangConfig();

        MAIN_CONFIGURATION = new MainConfiguration(CONFIGURATION, LOGGER);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        INSTANCE = this;
        MinecraftForge.EVENT_BUS.register(SCOREBOARD_MANAGER);
        MinecraftForge.EVENT_BUS.register(MAIN_CONFIGURATION);

        MinecraftForge.EVENT_BUS.register(new ConfigTip());

        MinecraftForge.EVENT_BUS.register(gameTickHandler = new GameTickHandler());
        MinecraftForge.EVENT_BUS.register(powerupDetect = new PowerupDetect());
        MinecraftForge.EVENT_BUS.register(spawnTimes = new SpawnTimes());
        MinecraftForge.EVENT_BUS.register(spawnNotice = new SpawnNotice());
        MinecraftForge.EVENT_BUS.register(dpsCounter = new DPSCounter());

        MinecraftForge.EVENT_BUS.register(lrQueueRenderer = new LRQueueRenderer());
        MinecraftForge.EVENT_BUS.register(powerupRenderer = new PowerupRenderer());
        MinecraftForge.EVENT_BUS.register(spawnTimeRenderer = new SpawnTimeRenderer());
        MinecraftForge.EVENT_BUS.register(dpsCounterRenderer = new DPSCounterRenderer());

        RENDERER_LIST.add(lrQueueRenderer);
        RENDERER_LIST.add(powerupRenderer);
        RENDERER_LIST.add(spawnTimeRenderer);
        RENDERER_LIST.add(dpsCounterRenderer);

        ClientCommandHandler.instance.registerCommand(new CommandSSTHUD());
        ClientCommandHandler.instance.registerCommand(new CommandSSTConfig());
        ClientCommandHandler.instance.registerCommand(new CommandCommon());
        ClientCommandHandler.instance.registerCommand(new CommandDebug());

        ClientRegistry.registerKeyBinding(MainConfiguration.keyToggleCountDown);
        ClientRegistry.registerKeyBinding(MainConfiguration.keyTogglePlayerInvisible);
        ClientRegistry.registerKeyBinding(MainConfiguration.keyOpenConfig);
    }

    public static ScoreboardManager getScoreboardManager() {
        return SCOREBOARD_MANAGER;
    }

    private static LanguageConfiguration createLangConfig() {
        File langConfig = new File(CONFIGURATION.getConfigFile().getParentFile(), MODID + ".lang");
        langConfig.deleteOnExit();
        try {
            langConfig.createNewFile();
            ResourceLocation lang = new ResourceLocation("showspawntime", "lang/showspawntime.lang");
            BufferedReader reader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(lang).getInputStream(), StandardCharsets.UTF_8));
            String line;

            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(langConfig.toPath()), StandardCharsets.UTF_8))) {
                while ((line = reader.readLine()) != null) {
                    writer.write(line);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        LanguageConfiguration config = new LanguageConfiguration(langConfig);
        config.load();
        return config;
    }

    public static LanguageConfiguration getLanguageConfiguration() {
        return LANGUAGE_CONFIGURATION;
    }

    public static ShowSpawnTime getInstance() {
        return INSTANCE;
    }

    public static GameTickHandler getGameTickHandler() {
        return gameTickHandler;
    }

    public static PowerupDetect getPowerupDetect() {
        return powerupDetect;
    }

    public static SpawnTimes getSpawnTimes() {
        return spawnTimes;
    }

    public static PowerupRenderer getPowerupRenderer() {
        return powerupRenderer;
    }

    public static LRQueueRenderer getLRQueueRenderer() {
        return lrQueueRenderer;
    }

    public static List<Renderer> getRendererList() {
        return RENDERER_LIST;
    }

    public static SpawnTimeRenderer getSpawnTimeRenderer() {
        return spawnTimeRenderer;
    }

    public static SpawnNotice getSpawnNotice() {
        return spawnNotice;
    }

    public static DPSCounter getDpsCounter() {
        return dpsCounter;
    }

    public static DPSCounterRenderer getDPSCounterRenderer() {
        return dpsCounterRenderer;
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    public static Configuration getConfiguration() {
        return CONFIGURATION;
    }

    public static MainConfiguration getMainConfiguration() {
        return MAIN_CONFIGURATION;
    }
}
