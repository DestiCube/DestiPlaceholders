package com.desticube.placeholders.api;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public

enum Colors {
    black("&0","<black>"),

    dark_blue("&1","<dark_blue>"),

    dark_green("&2","<dark_green>"),

    dark_aqua("&3","<dark_aqua>"),

    dark_red("&4","<dark_red>"),

    dark_purple("&5","<dark_purple>"),

    gold("&6","<gold>"),

    gray("&7","<gray>"),

    dark_gray("&8","<dark_gray>"),

    blue("&9","<blue>"),

    green("&a","<green>"),

    aqua("&b","<aqua>"),

    red("&c","<red>"),

    light_purple("&d","<light_purple>"),

    yellow("&e","<yellow>"),

    white("&f","<white>"),

    bold("&l","");

    public String code;
    public String mm;

    Colors(String code, String mm) {
        this.code = code;
        this.mm = mm;
    }

    private static final Pattern OLD_COLORS = Pattern.compile("&([A-Na-n\\d])");
    private final static Pattern BRACKET_HEX_COLORS = Pattern.compile("[{]#([^{}]+)[}]");

    public static String replace(String str) {
        return replaceOldColors(replaceOldHexColors(str));
    }

    public static String replaceOldHexColors(String str) {
        Matcher matcher = BRACKET_HEX_COLORS.matcher(str);
        StringBuilder buffer = new StringBuilder();
        while(matcher.find()) matcher.appendReplacement(buffer,"<#" + matcher.group(1) + ">");
        return matcher.appendTail(buffer).toString();
    }

    public static String replaceOldColors(String str) {
        Matcher matcher = OLD_COLORS.matcher(str);
        StringBuilder buffer = new StringBuilder();
        while(matcher.find()) matcher.appendReplacement(buffer, of("&" + matcher.group(1)).mm);
        return matcher.appendTail(buffer).toString();
    }

    public static Colors of(String s) {
        Colors returnColor = white;
        for (Colors color : Colors.values()) {
            if (s.equalsIgnoreCase(color.code)) {
                returnColor = color;
                break;
            }
        }
        return returnColor;
    }
}