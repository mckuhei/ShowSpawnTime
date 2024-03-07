package com.seosean.showspawntime.config;

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

    private Map<String, String> translations = new HashMap<>();
    public void load() {
//        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                String[] lines = line.split("=");
//                translations.put(lines[0], lines[1]);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
