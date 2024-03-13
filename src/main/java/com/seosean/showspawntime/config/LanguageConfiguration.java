package com.seosean.showspawntime.config;

import com.seosean.showspawntime.handler.LanguageDetector;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class LanguageConfiguration {
    public static Map<String, String> translations = new HashMap<>();
    public static void load() {
        ResourceLocation lang = new ResourceLocation("showspawntime", "lang/showspawntime.lang");
        String line;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(lang).getInputStream(), StandardCharsets.UTF_8));) {
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    String[] lines = line.split(" = ");
                    LanguageConfiguration.translations.put(lines[0], lines[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        key = key.concat(".").concat(LanguageDetector.language);
        return translations.getOrDefault(key, "");
    }

    public static String getOrigin(String key) {
        return translations.getOrDefault(key.concat(".EN_US"), "");
    }

    public static String getCache(String key) {
        return translations.getOrDefault(key.concat(".").concat(LanguageDetector.cacheLanguage), "");
    }
}
