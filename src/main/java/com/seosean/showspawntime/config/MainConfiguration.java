package com.seosean.showspawntime.config;

import com.seosean.showspawntime.ShowSpawnTime;
import com.seosean.showspawntime.config.gui.ShowSpawnTimeGuiConfig;
import com.seosean.showspawntime.features.frcooldown.FastReviveCoolDown;
import com.seosean.showspawntime.utils.DebugUtils;
import com.seosean.showspawntime.utils.DelayedTask;
import com.seosean.showspawntime.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;

public class MainConfiguration {

    public MainConfiguration(Configuration config, Logger logger) {
        MainConfiguration.config = config;
        MainConfiguration.logger = logger;
        MainConfiguration.minecraft = Minecraft.getMinecraft();
        this.check();
        this.ConfigLoad();
    }

    public static Minecraft minecraft;
    public static String[] AARoundsRecord = new String[]{"OFF", "Quintuple", "Tenfold","ALL"};
    public static String[] DEBBRoundsRecord = new String[]{"OFF", "Quintuple", "Tenfold","ALL"};
    public static KeyBinding keyTogglePlayerInvisible = new KeyBinding("SST Player Invisible", Keyboard.KEY_NONE, "Show Spawn Time");
    public static KeyBinding keyOpenConfig = new KeyBinding("SST Config", Keyboard.KEY_NONE, "Show Spawn Time");

    public static Configuration config;
    public static Logger logger;
    public static double HighlightDelay;
    public static boolean PlayAASound;
    public static boolean PlayDEBBSound;
    public static SoundEvent PrecededWaveSound;
    public static SoundEvent TheLastWaveSound;
    public static double PrecededWavePitch;
    public static double TheLastWavePitch;
    public static boolean ColorAlert;
    public static String AARoundsRecordToggle;
    public static String DEBBRoundsRecordToggle;
    public static boolean CleanUpTimeToggle;
    public static boolean PowerupAlertToggle;
    public static boolean PowerupPredictToggle;
    public static boolean PowerupCountDown;
    public static boolean PowerupNameTagShadow;
    public static boolean LightningRodQueue;
    public static boolean Wave3LeftNotice;
    public static boolean PlayerHealthNotice;
    public static boolean DPSCounterToggle;
    public static FastReviveCoolDown.RenderType FastReviveCoolDown;
    public static String[] FastReviveCoolDownRenderType = new String[]{"OFF", "FRONT", "MID","BEHIND"};

    public static boolean DEBBCountDown;
    public static SoundEvent CountDownSound;
    public static double CountDownPitch;
    public static boolean PlayerInvisible;


    public static LinkedHashMap<String, IConfigElement> sstRelated = new LinkedHashMap<>();
    public static LinkedHashMap<String, IConfigElement> recordRelated = new LinkedHashMap<>();
    public static LinkedHashMap<String, IConfigElement> powerupRelated = new LinkedHashMap<>();
    public static LinkedHashMap<String, IConfigElement> qolRelated = new LinkedHashMap<>();

    public static LinkedHashMap<String, IConfigElement> guiRelated = new LinkedHashMap<>();

    public void check() {
        File file = config.getConfigFile();
        boolean changed = false;
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("I:X")) {
                    line = line.replace("I:X", "D:X");
                    changed = true;
                } else if (line.contains("I:Y")) {
                    line = line.replace("I:Y", "D:Y");
                    changed = true;
                }
                content.append(line).append(System.lineSeparator());
            }

            if (changed) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write(content.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ConfigLoad(){
        config.load();
        logger.info("Started loading config. ");

        Property propertyXSpawnTime = config.get(Configuration.CATEGORY_CLIENT, "XSpawnTime", -1.0, "X");
        Property propertyYSpawnTime = config.get(Configuration.CATEGORY_CLIENT, "YSpawnTime", -1.0, "Y");
        XSpawnTime = propertyXSpawnTime.getDouble();
        YSpawnTime = propertyYSpawnTime.getDouble();
        guiRelated.put("XSpawnTime", new ConfigElement(propertyXSpawnTime));
        guiRelated.put("YSpawnTime", new ConfigElement(propertyYSpawnTime));

        Property propertyXPowerup = config.get(Configuration.CATEGORY_CLIENT, "XPowerup", -1.0, "X");
        Property propertyYPowerup = config.get(Configuration.CATEGORY_CLIENT, "YPowerup", -1.0, "Y");
        XPowerup = propertyXPowerup.getDouble();
        YPowerup = propertyYPowerup.getDouble();
        guiRelated.put("XPowerup", new ConfigElement(propertyXPowerup));
        guiRelated.put("YPowerup", new ConfigElement(propertyYPowerup));

        Property propertyXDPSCounter = config.get(Configuration.CATEGORY_CLIENT, "XDPSCounter", -1.0, "X");
        Property propertyYDPSCounter = config.get(Configuration.CATEGORY_CLIENT, "YDPSCounter", -1.0, "Y");
        XDPSCounter = propertyXDPSCounter.getDouble();
        YDPSCounter = propertyYDPSCounter.getDouble();
        guiRelated.put("XDPSCounter", new ConfigElement(propertyXDPSCounter));
        guiRelated.put("YDPSCounter", new ConfigElement(propertyYDPSCounter));

        String comment;
        String commentPlaySound;
        String commentDEBBPlaySound;
        String commentPlaySoundPrecededWave;
        String commentPlaySoundLastWave;
        String commentPlaySoundPrecededWavePitch;
        String commentPlaySoundLastWavePitch;
        String commentDEBBCountDown;
        String commentDEBBCountDownSound;
        String commentDEBBCountDownPitch;
        String commentDangerAlert;
        String commentAAAllRoundsRecord;
        String commentDEBBAllRoundsRecord;
        String commentCleanUpTimeTook;

        String commentPowerupAlert;
        String commentPowerupPredict;
        String commentPowerupCountDown;
        String commentPowerupNameTagShadow;

        String commentLightningRodHelper;
        String commentWave3LeftNotice;
        String commentPlayerHealthNotice;
        String commentCriticalTextFix;
        String commentDPSCounterToggle;
        String commentFastReviveCoolDown;

//        comment = "How long will the highlight delayed after a wave spawns in **SECOND**. \nNotice it only works in Dead End and Bad Blood.";
//        Property propertyHighlightDelay = config.get(Configuration.CATEGORY_GENERAL, "Highlight Delay", 2.0, comment, -10 , 10);
//        HighlightDelay = propertyHighlightDelay.getDouble();
//        sstRelated.put(propertyHighlightDelay.getName(), new ConfigElement(propertyHighlightDelay));


        /**
         *  SST Related Elements
         */

        commentPlaySound = "Turn on/off the sound of wave spawning in AA.";
        Property propertyPlaySound =  config.get(Configuration.CATEGORY_GENERAL, "Toggle AA Sound", true, commentPlaySound);
        PlayAASound = propertyPlaySound.getBoolean();
        sstRelated.put(propertyPlaySound.getName(), new ConfigElement(propertyPlaySound));

        commentDEBBPlaySound = "Turn on/off the sound of wave spawning in DE and BB.";
        Property propertyPlayDEBBSound = config.get(Configuration.CATEGORY_GENERAL, "Toggle DE/BB Sound", true, commentDEBBPlaySound);
        PlayDEBBSound = propertyPlayDEBBSound.getBoolean();
        sstRelated.put(propertyPlayDEBBSound.getName(), new ConfigElement(propertyPlayDEBBSound));

        commentPlaySoundPrecededWave = "The sound will be played when a wave spawns except the last wave. \nYou can search the sounds you want at https://minecraft.fandom.com/wiki/Sounds.json/Java_Edition_values_before_1.9 \nChinese wiki: https://minecraft.fandom.com/zh/wiki/Sounds.json/Java%E7%89%881.9%E5%89%8D";
        Property propertyPrecededWave = config.get(Configuration.CATEGORY_GENERAL, "Preceded Wave Sound", "block.note.pling", commentPlaySoundPrecededWave);
        PrecededWaveSound = SoundEvent.REGISTRY.getObject(new ResourceLocation(propertyPrecededWave.getString()));
        sstRelated.put(propertyPrecededWave.getName(), new ConfigElement(propertyPrecededWave));

        commentPlaySoundPrecededWavePitch = "The pitch setting of PrecededWave.";
        Property propertyPrecededWavePitch = config.get(Configuration.CATEGORY_GENERAL, "Preceded Wave Pitch", 2.0, commentPlaySoundPrecededWavePitch, 0, 2);
        PrecededWavePitch = propertyPrecededWavePitch.getDouble();
        sstRelated.put(propertyPrecededWavePitch.getName(), new ConfigElement(propertyPrecededWavePitch));

        commentPlaySoundLastWave = "The sound will be played when the last wave spawns.";
        Property propertyTheLastWave = config.get(Configuration.CATEGORY_GENERAL, "Final Wave Sound", "entity.experience_orb.pickup", commentPlaySoundLastWave);
        TheLastWaveSound = SoundEvent.REGISTRY.getObject(new ResourceLocation(propertyTheLastWave.getString()));
        sstRelated.put(propertyTheLastWave.getName(), new ConfigElement(propertyTheLastWave));

        commentPlaySoundLastWavePitch = "The pitch setting of TheLastWave.";
        Property propertyTheLastWavePitch = config.get(Configuration.CATEGORY_GENERAL, "Final Wave Pitch", 0.5, commentPlaySoundLastWavePitch, 0, 2);
        TheLastWavePitch = propertyTheLastWavePitch.getDouble();
        sstRelated.put(propertyTheLastWavePitch.getName(), new ConfigElement(propertyTheLastWavePitch));

        commentDEBBCountDown = "Turn on/off the count-down sound of seconds before final wave spawns in DE and BB.";
        Property propertyDEBBCountDown = config.get(Configuration.CATEGORY_GENERAL, "Toggle DE/BB W3 Count Down Sound", false, commentDEBBCountDown);
        DEBBCountDown = propertyDEBBCountDown.getBoolean();
        sstRelated.put(propertyDEBBCountDown.getName(), new ConfigElement(propertyDEBBCountDown));

        commentDEBBCountDownSound = "The sound of W3 Count Down";
        Property propertyCountDownSound = config.get(Configuration.CATEGORY_GENERAL, "Count Down Sound", "block.note.pling", commentDEBBCountDownSound);
        CountDownSound = SoundEvent.REGISTRY.getObject(new ResourceLocation(propertyCountDownSound.getString()));
        sstRelated.put(propertyCountDownSound.getName(), new ConfigElement(propertyCountDownSound));

        commentDEBBCountDownPitch = "The sound pitch of W3 Count Down";
        Property propertyCountDownPitch = config.get(Configuration.CATEGORY_GENERAL, "Count Down Pitch", 1.5, commentDEBBCountDownPitch, 0, 2);
        CountDownPitch = propertyCountDownPitch.getDouble();
        sstRelated.put(propertyCountDownPitch.getName(), new ConfigElement(propertyCountDownPitch));

        commentDangerAlert = "Turn on/off the color alert to The Old One and Giants. \nOnly works in AA.";
        Property propertyColorAlert = config.get(Configuration.CATEGORY_GENERAL, "AA Boss Color Alert", true, commentDangerAlert);
        ColorAlert = propertyColorAlert.getBoolean();
        sstRelated.put(propertyColorAlert.getName(), new ConfigElement(propertyColorAlert));

        /**
         *  Record Related Elements
         */
        commentAAAllRoundsRecord  = "Turn on/off the round timing similar to round 10/20/105 in AA.";
        Property propertyAARoundsRecordToggle = config.get(Configuration.CATEGORY_GENERAL, "AA Rounds Record Timing", "ALL", commentAAAllRoundsRecord, AARoundsRecord);
        AARoundsRecordToggle = propertyAARoundsRecordToggle.getString();
        recordRelated.put(propertyAARoundsRecordToggle.getName(), new ConfigElement(propertyAARoundsRecordToggle));

        commentDEBBAllRoundsRecord = "Turn on/off the round timing similar to round 10/20/30 in DE/BB.";
        Property propertyDEBBRoundsRecordToggle = config.get(Configuration.CATEGORY_GENERAL, "DE/BB Rounds Record Timing", "ALL", commentDEBBAllRoundsRecord, DEBBRoundsRecord);
        DEBBRoundsRecordToggle = propertyDEBBRoundsRecordToggle.getString();
        recordRelated.put(propertyDEBBRoundsRecordToggle.getName(), new ConfigElement(propertyDEBBRoundsRecordToggle));

        commentCleanUpTimeTook = "Turn on/off the tip of time took to clean up.";
        Property propertyCleanUpTimeToggle = config.get(Configuration.CATEGORY_GENERAL, "Clean Up Time Tips", true, commentCleanUpTimeTook);
        CleanUpTimeToggle = propertyCleanUpTimeToggle.getBoolean();
        recordRelated.put(propertyCleanUpTimeToggle.getName(), new ConfigElement(propertyCleanUpTimeToggle));

        /**
         *  Powerup Related Elements
         */

        commentPowerupAlert = "Remind you when this is a powerup-round. Start counting down when a powerup spawns";
        Property propertyPowerupAlertToggle = config.get(Configuration.CATEGORY_GENERAL, "Powerup Alert", true, commentPowerupAlert);
        PowerupAlertToggle = propertyPowerupAlertToggle.getBoolean();
        powerupRelated.put(propertyPowerupAlertToggle.getName(), new ConfigElement(propertyPowerupAlertToggle));

        commentPowerupPredict = "Notice you when the next powerup is at the beginning of round";
        Property propertyPowerupPredictToggle = config.get(Configuration.CATEGORY_GENERAL, "Powerup Predict", true, commentPowerupPredict);
        PowerupPredictToggle = propertyPowerupPredictToggle.getBoolean();
        powerupRelated.put(propertyPowerupPredictToggle.getName(), new ConfigElement(propertyPowerupPredictToggle));

        commentPowerupCountDown = "Show the remaining time of powerups on expiration.";
        Property propertyPowerupCountDown = config.get(Configuration.CATEGORY_GENERAL, "Powerup Count Down", true, commentPowerupCountDown);
        PowerupCountDown = propertyPowerupCountDown.getBoolean();
        powerupRelated.put(propertyPowerupCountDown.getName(), new ConfigElement(propertyPowerupCountDown));

        commentPowerupNameTagShadow = "Render shadow for nametags of powerups.";
        Property propertyPowerupNameTagShadow = config.get(Configuration.CATEGORY_GENERAL, "Powerup NameTag Shadow", false, commentPowerupNameTagShadow);
        PowerupNameTagShadow = propertyPowerupNameTagShadow.getBoolean();
        powerupRelated.put(propertyPowerupNameTagShadow.getName(), new ConfigElement(propertyPowerupNameTagShadow));

        /**
         *  QoL Related Elements
         */
        commentLightningRodHelper = "Turn on/off the helper of lightning rod queue in AA.";
        Property propertyLightningRodQueue = config.get(Configuration.CATEGORY_GENERAL, "LR Queue Helper", true, commentLightningRodHelper);
        LightningRodQueue = propertyLightningRodQueue.getBoolean();
        qolRelated.put(propertyLightningRodQueue.getName(), new ConfigElement(propertyLightningRodQueue));

        commentWave3LeftNotice = "Enhance the Sidebar which shows you the amount of zombies in wave 3rd in DE/BB.";
        Property propertyWave3LeftNotice = config.get(Configuration.CATEGORY_GENERAL, "Wave 3rd Left Notice", true, commentWave3LeftNotice);
        Wave3LeftNotice = propertyWave3LeftNotice.getBoolean();
        qolRelated.put(propertyWave3LeftNotice.getName(), new ConfigElement(propertyWave3LeftNotice));

        commentPlayerHealthNotice = "Enhance the Sidebar which shows the health of players.";
        Property propertyPlayerHealthNotice = config.get(Configuration.CATEGORY_GENERAL, "Player Health Notice", true, commentPlayerHealthNotice);
        PlayerHealthNotice = propertyPlayerHealthNotice.getBoolean();
        qolRelated.put(propertyPlayerHealthNotice.getName(), new ConfigElement(propertyPlayerHealthNotice));

        commentFastReviveCoolDown = "Enhance the Sidebar which shows the cool down of fast revive for each player.";
        Property propertyFastReviveCoolDown = config.get(Configuration.CATEGORY_GENERAL, "Fast Revive Cool Down", "BEHIND", commentFastReviveCoolDown, FastReviveCoolDownRenderType);
        FastReviveCoolDown = com.seosean.showspawntime.features.frcooldown.FastReviveCoolDown.RenderType.valueOf(propertyFastReviveCoolDown.getString());
        qolRelated.put(propertyFastReviveCoolDown.getName(), new ConfigElement(propertyFastReviveCoolDown));

        commentDPSCounterToggle = "Display your own damage per second on your screen.";
        Property propertyDPSCounterToggle = config.get(Configuration.CATEGORY_GENERAL, "Individual DPS Counter", true, commentDPSCounterToggle);
        DPSCounterToggle = propertyDPSCounterToggle.getBoolean();
        qolRelated.put(propertyDPSCounterToggle.getName(), new ConfigElement(propertyDPSCounterToggle));

        config.save();
        logger.info("Finished loading config. ");
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(ShowSpawnTime.MODID) && (event.getConfigID() == null || !event.getConfigID().equals("dummyID"))) {
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
                    minecraft.fontRenderer
                            .getStringWidth("➤ W2 00:00")
                    / (double)screenWidth;
        }
        return XSpawnTime;
    }

    public static double getYSpawnTime(){
        int screenHeight = new ScaledResolution(minecraft).getScaledHeight();
        if(YSpawnTime < 0){
            return 1 - (double)minecraft.fontRenderer.FONT_HEIGHT * 8 / (double)screenHeight;
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
            return 0.5 - (float)minecraft.fontRenderer.FONT_HEIGHT * 4 / (double)screenHeight;
        }
        return YPowerup;
    }

    public static double getXDPSCounter(){
        int screenWidth = new ScaledResolution(minecraft).getScaledWidth();
        if(XDPSCounter < 0){
            return 0.75 - (double)minecraft.fontRenderer.getStringWidth("DPS: INSTA KILL") / (double)screenWidth;
        }
        return XDPSCounter;
    }

    public static double getYDPSCounter(){
        int screenHeight = new ScaledResolution(minecraft).getScaledHeight();
        if(YDPSCounter < 0){
            return 1 - minecraft.fontRenderer.FONT_HEIGHT * 3 / (double)screenHeight;
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
            ITextComponent text;
            if (PlayerInvisible) {
                text = new TextComponentString(TextFormatting.YELLOW + "Toggled Player Invisible " + TextFormatting.GREEN + "ON");
            } else {
                text = new TextComponentString(TextFormatting.YELLOW + "Toggled Player Invisible " + TextFormatting.RED + "OFF");
            }
            PlayerUtils.sendMessage(text);
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

    public static int powerupNameTagRenderColor = 0x20FFFFFF;
    public static int powerupCountDownRenderColor = 0x2099CCFF;

}
