package com.seosean.showspawntime.config;

import com.seosean.showspawntime.handler.LanguageDetector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LanguageConfiguration {
    public LanguageConfiguration(File file) {
        this.file = file;
    }

    private File file;

    public static Map<String, String> translations = new HashMap<>();
    public void load() {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
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
}
