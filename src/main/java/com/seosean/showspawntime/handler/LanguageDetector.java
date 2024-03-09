package com.seosean.showspawntime.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.seosean.showspawntime.utils.DebugUtils;
import com.seosean.showspawntime.utils.DelayedTask;
import com.seosean.showspawntime.utils.LanguageUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

public class LanguageDetector {

    public static String language = "EN_US";
    @SubscribeEvent
    public void onPlayerConnectHypixel(EntityJoinWorldEvent event) {
        if (!event.world.isRemote) {
            return;
        }

        if (!event.entity.equals(Minecraft.getMinecraft().thePlayer)) {
            return;
        }


    }

    public static void setLanguage() {
        String zombiesLeftText = LanguageUtils.getZombiesLeftText();

        switch (zombiesLeftText) {
            case "Zombies Left": language = "EN_US"; break;
            case "剩余僵尸": language = "ZH_CN"; break;
            case "剩下殭屍數": language = "ZH_TW"; break;
            case "Zbývající zombie": language = "CS"; break;
            case "Zombier tilbage": language = "DA"; break;
            case "Zombies over": language = "NL"; break;
            case "Zombeja jäljellä": language = "FI"; break;
            case "Zombies restants": language = "FR"; break;
            case "Zombies übrig": language = "DE"; break;
            case "Ζόμπι που Απομένουν": language = "EL"; break;
            case "Hátralévő Zombik": language = "HU"; break;
            case "Zombi Rimanenti": language = "IT"; break;
            case "残りゾンビ": language = "JA"; break;
            case "남은 좀비": language = "KO"; break;
            case "Zombier igjen": language = "NO"; break;
            case "Pozostałe zombi": language = "PL"; break;
            case "Zombies restantes": language = "PT_PT"; break;
            case "Zumbis restantes": language = "PT_BR"; break;
            case "Zombi Rămași": language = "RO"; break;
            case "Осталось зомби": language = "RU"; break;
            case "Zombies Restantes": language = "ES_ES"; break;
            case "Zombier kvar": language = "SV_SE"; break;
            case "Kalan Zombi": language = "TR"; break;
            case "Залишилося зомбі": language = "UK"; break;
        }
    }
    public enum Language {

    }
}
