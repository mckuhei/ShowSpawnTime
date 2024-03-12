package com.seosean.showspawntime.utils;

import com.seosean.showspawntime.ShowSpawnTime;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public static String trim(String s) {
        return s.replaceAll(ShowSpawnTime.EMOJI_REGEX, "").replaceAll(ShowSpawnTime.COLOR_REGEX, "").trim();
    }



    public static int getNumberInString(String s) {
        String regex = "\\d+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }
        return 0;
    }

    public static boolean contains(List<String> strings, String string) {
        for (String s : strings) {
            if (string.contains(s)) {
                return true;
            }
        }
        return false;
    }
}
