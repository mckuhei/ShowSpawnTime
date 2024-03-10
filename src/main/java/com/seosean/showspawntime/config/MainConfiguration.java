package com.seosean.showspawntime.config;

import com.seosean.showspawntime.ShowSpawnTime;
import com.seosean.showspawntime.config.gui.ShowSpawnTimeGuiConfig;
import com.seosean.showspawntime.utils.DelayedTask;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;
import java.util.Map;

public class MainConfiguration {

    public MainConfiguration(Configuration config, Logger logger) {
        MainConfiguration.config = config;
        MainConfiguration.logger = logger;
        MainConfiguration.minecraft = Minecraft.getMinecraft();
        this.ConfigLoad();
    }

    public static Minecraft minecraft;
    public static String[] AARoundsRecord = new String[]{"OFF", "Quintuple", "Tenfold","ALL"};
    public static String[] DEBBRoundsRecord = new String[]{"OFF", "Quintuple", "Tenfold","ALL"};
    public static KeyBinding keyToggleCountDown = new KeyBinding("Wave 3 Count Down", Keyboard.KEY_NONE, "Show Spawn Time");
    public static KeyBinding keyTogglePlayerInvisible = new KeyBinding("Player Invisible", Keyboard.KEY_NONE, "Show Spawn Time");
    public static KeyBinding keyOpenConfig = new KeyBinding("Config", Keyboard.KEY_NONE, "Show Spawn Time");

    public static Configuration config;
    public static Logger logger;
    public static double HighlightDelay;
    public static boolean PlayAASound;
    public static boolean PlayDEBBSound;
    public static String PrecededWave;
    public static String TheLastWave;
    public static double PrecededWavePitch;
    public static double TheLastWavePitch;
    public static boolean ShowConfigTip;
    public static boolean ColorAlert;
    public static String AARoundsRecordToggle;
    public static String DEBBRoundsRecordToggle;
    public static boolean CleanUpTimeToggle;
    public static boolean LightningRodQueue;
    public static boolean PowerupAlertToggle;
    public static boolean Wave3LeftNotice;
    public static boolean PlayerHealthNotice;
    public static boolean DEBBCountDown;
    public static boolean PlayerInvisible;


    public static Map<String, IConfigElement> sstRelated = new HashMap<>();
    public static Map<String, IConfigElement> recordRelated = new HashMap<>();
    public static Map<String, IConfigElement> powerupRelated = new HashMap<>();


    public void ConfigLoad(){
        config.load();
        logger.info("Started loading config. ");

        XSpawnTime = config.get(Configuration.CATEGORY_CLIENT, "XSpawnTime", -1, "X").getDouble();
        YSpawnTime = config.get(Configuration.CATEGORY_CLIENT, "YSpawnTime", -1, "Y").getDouble();

        XPowerup = config.get(Configuration.CATEGORY_CLIENT, "XPowerup", -1, "X").getDouble();
        YPowerup = config.get(Configuration.CATEGORY_CLIENT, "YPowerup", -1, "Y").getDouble();

        XDPSCounter = config.get(Configuration.CATEGORY_CLIENT, "XDPSCounter", -1, "X").getDouble();
        YDPSCounter = config.get(Configuration.CATEGORY_CLIENT, "YDPSCounter", -1, "Y").getDouble();

        String comment;
        String commentPlaySound;
        String commentDEBBPlaySound;
        String commentPlaySoundPrecededWave;
        String commentPlaySoundLastWave;
        String commentPlaySoundPrecededWavePitch;
        String commentPlaySoundLastWavePitch;
        String commentDangerAlert;
        String commentAAAllRoundsRecord;
        String commentDEBBAllRoundsRecord;
        String commentCleanUpTimeTook;
        String commentLightningRodHelper;
        String commentPowerupAlert;
        String commentWave3LeftNotice;
        String commentPlayerHealthNotice;

        comment = "How long will the highlight delayed after a wave spawns in **SECOND**. \nNotice it only works in Dead End and Bad Blood.";
        Property propertyHighlightDelay = config.get(Configuration.CATEGORY_GENERAL, "Highlight Delay", 2.0, comment, -10 , 10);
        HighlightDelay = propertyHighlightDelay.getDouble();
        sstRelated.put(propertyHighlightDelay.getName(), new ConfigElement(propertyHighlightDelay));


        commentPlaySound = "Turn on/off the sound of wave spawning in AA.";
        Property propertyPlaySound =  config.get(Configuration.CATEGORY_GENERAL, "Toggle AA Sound", true, commentPlaySound);
        PlayAASound = propertyPlaySound.getBoolean();
        sstRelated.put(propertyPlaySound.getName(), new ConfigElement(propertyPlaySound));

        commentDEBBPlaySound = "Turn on/off the sound of wave spawning in DE and BB.";
        Property propertyPlayDEBBSound = config.get(Configuration.CATEGORY_GENERAL, "Toggle DE/BB Sound", true, commentDEBBPlaySound);
        PlayDEBBSound = propertyPlayDEBBSound.getBoolean();
        sstRelated.put(propertyPlayDEBBSound.getName(), new ConfigElement(propertyPlayDEBBSound));

        commentPlaySoundPrecededWave = "What sound will be played when a wave spawns except the last wave. \nYou can search the sounds you want at https://minecraft.fandom.com/wiki/Sounds.json/Java_Edition_values_before_1.9 \nChinese wiki: https://minecraft.fandom.com/zh/wiki/Sounds.json/Java%E7%89%881.9%E5%89%8D";
        Property propertyPrecededWave = config.get(Configuration.CATEGORY_GENERAL, "Preceded Wave Sound", "note.pling", commentPlaySoundPrecededWave);
        PrecededWave = propertyPrecededWave.getString();
        sstRelated.put(propertyPrecededWave.getName(), new ConfigElement(propertyPrecededWave));

        commentPlaySoundPrecededWavePitch = "The pitch setting of PrecededWave.";
        Property propertyPrecededWavePitch = config.get(Configuration.CATEGORY_GENERAL, "Preceded Wave Pitch", 2.0, commentPlaySoundPrecededWavePitch, 0, 2);
        PrecededWavePitch = config.get(Configuration.CATEGORY_GENERAL, "Preceded Wave Pitch", 2.0, commentPlaySoundPrecededWavePitch, 0, 2).getDouble();
        sstRelated.put(propertyPrecededWavePitch.getName(), new ConfigElement(propertyPrecededWavePitch));

        commentPlaySoundLastWave = "What sound will be played when the last wave spawns.";
        Property propertyTheLastWave = config.get(Configuration.CATEGORY_GENERAL, "Last Wave Sound", "random.orb", commentPlaySoundLastWave);
        TheLastWave = config.get(Configuration.CATEGORY_GENERAL, "Last Wave Sound", "random.orb", commentPlaySoundLastWave).getString();
        sstRelated.put(propertyTheLastWave.getName(), new ConfigElement(propertyTheLastWave));

        commentPlaySoundLastWavePitch = "The pitch setting of TheLastWave.";
        Property propertyTheLastWavePitch = config.get(Configuration.CATEGORY_GENERAL, "Last Wave Pitch", 0.5, commentPlaySoundLastWavePitch, 0, 2);
        TheLastWavePitch = config.get(Configuration.CATEGORY_GENERAL, "Last Wave Pitch", 0.5, commentPlaySoundLastWavePitch, 0, 2).getDouble();
        sstRelated.put(propertyTheLastWavePitch.getName(), new ConfigElement(propertyTheLastWavePitch));

        commentDangerAlert = "Turn on/off the color alert to The Old One and Giants. \nOnly works in AA.";
        Property propertyColorAlert = config.get(Configuration.CATEGORY_GENERAL, "AA Boss Alert", true, commentDangerAlert);
        ColorAlert = config.get(Configuration.CATEGORY_GENERAL, "AA Boss Alert", true, commentDangerAlert).getBoolean();
        sstRelated.put(propertyColorAlert.getName(), new ConfigElement(propertyColorAlert));

        commentAAAllRoundsRecord  = "Turn on/off the round timing similar to round 10/20/105 in AA.";
        Property propertyAARoundsRecordToggle = config.get(Configuration.CATEGORY_GENERAL, "AA Rounds Record Timing", "ALL", commentAAAllRoundsRecord, AARoundsRecord);
        AARoundsRecordToggle = config.getString("AA Rounds Record Timing", Configuration.CATEGORY_GENERAL, "ALL", commentAAAllRoundsRecord, AARoundsRecord);
        recordRelated.put(propertyAARoundsRecordToggle.getName(), new ConfigElement(propertyAARoundsRecordToggle));

        commentDEBBAllRoundsRecord = "Turn on/off the round timing similar to round 10/20/30 in DE/BB.";
        Property propertyDEBBRoundsRecordToggle = config.get(Configuration.CATEGORY_GENERAL, "DE/BB Rounds Record Timing", "ALL", commentDEBBAllRoundsRecord, DEBBRoundsRecord);
        DEBBRoundsRecordToggle = config.getString("DE/BB Rounds Record Timing", Configuration.CATEGORY_GENERAL, "ALL", commentDEBBAllRoundsRecord, DEBBRoundsRecord);
        recordRelated.put(propertyDEBBRoundsRecordToggle.getName(), new ConfigElement(propertyDEBBRoundsRecordToggle));

        commentCleanUpTimeTook = "Turn on/off the tip of time took to clean up.";
        Property propertyCleanUpTimeToggle = config.get(Configuration.CATEGORY_GENERAL, "Clean Up Time Tips", true, commentCleanUpTimeTook);
        CleanUpTimeToggle = config.get(Configuration.CATEGORY_GENERAL, "Clean Up Time Tips", true, commentCleanUpTimeTook).getBoolean();
        recordRelated.put(propertyCleanUpTimeToggle.getName(), new ConfigElement(propertyCleanUpTimeToggle));

        commentLightningRodHelper = "Turn on/off the helper of lightning rod queue in AA.";
        Property propertyLightningRodQueue = config.get(Configuration.CATEGORY_GENERAL, "LR Queue Helper", true, commentLightningRodHelper);
        LightningRodQueue = config.get(Configuration.CATEGORY_GENERAL, "LR Queue Helper", true, commentLightningRodHelper).getBoolean();
        powerupRelated.put(propertyLightningRodQueue.getName(), new ConfigElement(propertyLightningRodQueue));

        commentPowerupAlert = "Remind you when this is a powerup-round. Start counting down when a powerup spawns";
        Property propertyPowerupAlertToggle = config.get(Configuration.CATEGORY_GENERAL, "Powerup Alert", true, commentPowerupAlert);
        PowerupAlertToggle = config.get(Configuration.CATEGORY_GENERAL, "Powerup Alert", true, commentPowerupAlert).getBoolean();
        powerupRelated.put(propertyPowerupAlertToggle.getName(), new ConfigElement(propertyPowerupAlertToggle));

        commentWave3LeftNotice = "Enhance the ScoreBoard which shows you the amount of zombies in wave 3rd in DE/BB.";
        Property propertyWave3LeftNotice = config.get(Configuration.CATEGORY_GENERAL, "Wave 3rd Left Notice", true, commentWave3LeftNotice);
        Wave3LeftNotice = config.get(Configuration.CATEGORY_GENERAL, "Wave 3rd Left Notice", true, commentWave3LeftNotice).getBoolean();
        recordRelated.put(propertyWave3LeftNotice.getName(), new ConfigElement(propertyWave3LeftNotice));

        commentPlayerHealthNotice = "Enhance the ScoreBoard which shows the health of players.";
        Property propertyPlayerHealthNotice = config.get(Configuration.CATEGORY_GENERAL, "Player Health Notice", true, commentPlayerHealthNotice);
        PlayerHealthNotice = config.get(Configuration.CATEGORY_GENERAL, "Player Health Notice", true, commentPlayerHealthNotice).getBoolean();
        powerupRelated.put(propertyPlayerHealthNotice.getName(), new ConfigElement(propertyPlayerHealthNotice));

        config.save();
        logger.info("Finished loading config. ");
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equals(ShowSpawnTime.MODID) && (event.configID == null || !event.configID.equals("dummyID"))) {
            config.save();
            this.ConfigLoad();
        }
    }

    public static double XSpawnTime;
    public static double YSpawnTime;
    public static double XPowerup;
    public static double YPowerup;
    public static double XDPSCounter;
    public static double YDPSCounter;

    public static double getXSpawnTime(){
        int screenWidth = new ScaledResolution(minecraft).getScaledWidth();
        if(XSpawnTime < 0){
            return 1 - (double)
                    minecraft.fontRendererObj
                            .getStringWidth("➤ W2 00:00")
                    / (double)screenWidth;
        }
        return XSpawnTime;
    }

    public static double getYSpawnTime(){
        int screenHeight = new ScaledResolution(minecraft).getScaledHeight();
        if(YSpawnTime < 0){
            return 1 - (double)minecraft.fontRendererObj.FONT_HEIGHT * 7 / (double)screenHeight;
        }
        return YSpawnTime;
    }

    public static double getXPowerup(){
        if(XPowerup < 0){
            return 0;
        }
        return XPowerup;
    }

    public static double getYPowerup(){
        int screenHeight = new ScaledResolution(minecraft).getScaledHeight();
        if(YPowerup < 0){
            return 0.5 - (float)minecraft.fontRendererObj.FONT_HEIGHT * 4 / (double)screenHeight;
        }
        return YPowerup;
    }

    public static double getXDPSCounter(){
        int screenWidth = new ScaledResolution(minecraft).getScaledWidth();
        if(XDPSCounter < 0){
            return 0.75 - (double)minecraft.fontRendererObj.getStringWidth("DPS: INSTA KILL") / (double)screenWidth;
        }
        return XDPSCounter;
    }

    public static double getYDPSCounter(){
        int screenHeight = new ScaledResolution(minecraft).getScaledHeight();
        if(YDPSCounter < 0){
            return 1 - minecraft.fontRendererObj.FONT_HEIGHT * 2 / (double)screenHeight;
        }
        return YDPSCounter;
    }

    @SubscribeEvent
    public void keyHandler(InputEvent.KeyInputEvent event) {
        if (Minecraft.getMinecraft().currentScreen != null) {
            return;
        }

        if (keyTogglePlayerInvisible.isPressed()) {
            PlayerInvisible  = !PlayerInvisible;
            IChatComponent text;
            if (PlayerInvisible) {
                text = new ChatComponentText(EnumChatFormatting.YELLOW + "Toggled Player Invisible " + EnumChatFormatting.GREEN + "ON");
            } else {
                text = new ChatComponentText(EnumChatFormatting.YELLOW + "Toggled Player Invisible " + EnumChatFormatting.RED + "OFF");
            }
            Minecraft.getMinecraft().thePlayer.addChatComponentMessage(text);
        }

        else if (keyToggleCountDown.isPressed()) {
            DEBBCountDown  = !DEBBCountDown;
            IChatComponent text;
            if (DEBBCountDown) {
                text = new ChatComponentText(EnumChatFormatting.YELLOW + "Toggled Count Down " + EnumChatFormatting.GREEN + "ON");
            } else {
                text = new ChatComponentText(EnumChatFormatting.YELLOW + "Toggled Count Down " + EnumChatFormatting.RED + "OFF");
            }
            Minecraft.getMinecraft().thePlayer.addChatComponentMessage(text);
        }

        else if (keyOpenConfig.isPressed()) {
            new DelayedTask() {
                @Override
                public void run() {
                    Minecraft.getMinecraft().displayGuiScreen(new ShowSpawnTimeGuiConfig(null));
                }
            }.runTaskLater(2);
        }
    }
}
