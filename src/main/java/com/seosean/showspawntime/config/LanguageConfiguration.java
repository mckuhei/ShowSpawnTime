package com.seosean.showspawntime.config;

import com.seosean.showspawntime.handler.LanguageDetector;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class LanguageConfiguration {
    public LanguageConfiguration(File file) {
        this.file = file;
    }

    private final File file;

    public static Map<String, String> translations = new HashMap<>();
    public void load() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    String[] lines = line.split(" = ");
                    translations.put(lines[0], lines[1]);
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
