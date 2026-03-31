package com.cnbsoft.plugin.generator.util;

import java.util.Arrays;
import java.util.List;

public class StringUtil {

    public static String convertColumnIntoProp(String source) {
        if (source == null || source.indexOf("_") == -1) {
            if (source.equals(source.toUpperCase())) {
                return source.toLowerCase();
            } else {
                return source;
            }
        }
        StringBuilder buffer = new StringBuilder();
        source = source.toLowerCase();
        String[] tempArr = source.split("_", -1);
        for (int i = 0; i < tempArr.length; i++) {
            if (i == 0) {
                buffer.append(tempArr[i]);
            } else {
                buffer.append(capitalize(tempArr[i]));
            }
        }
        return buffer.toString();
    }

    public static String capitalize(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        return new StringBuilder(str.length())
                .append(Character.toTitleCase(str.charAt(0)))
                .append(str.substring(1))
                .toString();
    }

    public static String toTitleCase(String str) {
        List<String> strs = Arrays.asList(str.replaceAll("_", " ").split(" "));
        StringBuilder buffer = new StringBuilder();
        for (String s : strs) {
            buffer.append(Character.toTitleCase(s.charAt(0)))
                    .append(s.substring(1).toLowerCase());
        }
        return buffer.toString();
    }

    public static String tableNameToJavaName(String source) {
        return capitalize(toTitleCase(source));
    }

    public static String toFirstLower(String str) {
        if (str == null || str.length() == 0) return str;
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }
}
