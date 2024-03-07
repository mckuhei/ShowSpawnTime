package com.seosean.showspawntime.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.seosean.showspawntime.utils.DebugUtils;
import com.seosean.showspawntime.utils.DelayedTask;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class LanguageDetector {

    @SubscribeEvent
    public void onPlayerConnectHypixel(EntityJoinWorldEvent event) {
        if (!event.world.isRemote) {
            return;
        }

        if (!event.entity.equals(Minecraft.getMinecraft().thePlayer)) {
            return;
        }

        this.get();

        MinecraftForge.EVENT_BUS.unregister(this);

    }
    public void get() {
        String apiKey = "265215d0-23a5-4298-aed7-db075af88e66";

        try {
            URL url = new URL("https://api.hypixel.net/v2/player?name=" + Minecraft.getMinecraft().thePlayer.getName() + "&key=" + apiKey);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            connection.disconnect();

            String language = parseLanguageFromResponse(response.toString());
            new DelayedTask() {
                @Override
                public void run() {
                    DebugUtils.sendMessage("DEBUG1: " + language);
//                    DebugUtils.sendMessage("DEBUG2: " + response.toString());
                }
            }.runTaskLater(100);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String parseLanguageFromResponse(String response) {
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(response);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonObject playerObject = jsonObject.getAsJsonObject("player");
        return playerObject.get("userLanguage").getAsString();
    }
}
