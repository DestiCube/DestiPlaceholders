package com.desticube.placeholders.api;

import com.desticube.placeholders.extensions.EmptyExtension;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;
import static net.kyori.adventure.text.serializer.gson.GsonComponentSerializer.gson;

public class Placeholders {
    public static final ConcurrentHashMap<String, PlaceholderExtension> extensions = new ConcurrentHashMap<>();

    public static void register(PlaceholderExtension extension) {
        extensions.put(extension.getIdentifier(), extension);
    }

    private static final PlaceholderExtension emptyExtension = new EmptyExtension();
    private static final Pattern PATTERN = Pattern.compile("%(.*?)%");
    public static Component setPlaceholders(Component parse) {
        return parse.replaceText(b -> {
                   b.match(PATTERN).replacement((matcher, builder) -> {
                       String matched = matcher.group(1);
                       String[] split = matched.split("_");
                       String toDeserialize = extensions.getOrDefault(split[0], emptyExtension).onRequest(matched.replaceFirst(split[0] + "_", "")).join();
                       return miniMessage().deserialize(toDeserialize == null ? "" : toDeserialize);
                   });
                });
    }

    public static Component setPlaceholders(Player p, Component parse) {
        return parse.replaceText(b ->
                    b.match(PATTERN).replacement((matcher, builder) -> {
                        String matched = matcher.group(1);
                        String[] split = matched.split("_");
                        String toDeserialize = extensions.getOrDefault(split[0], emptyExtension).onRequestRelational(p, matched.replaceFirst(split[0] + "_", "")).join();
                        return miniMessage().deserialize(toDeserialize == null ? "" : toDeserialize);
                    })
                );
    }

    public static String setPlaceholders(String parse) {
        Matcher matcher = PATTERN.matcher(parse);
        StringBuilder buffer = new StringBuilder();
        while(matcher.find()) {
            String matched = matcher.group(1);
            String[] split = matched.split("_");
            matcher.appendReplacement(buffer, extensions.getOrDefault(split[0], emptyExtension).onRequest(matched.replaceFirst(split[0] + "_", "")).join());
        }
        return matcher.appendTail(buffer).toString();
    }

    public static String setPlaceholders(Player p, String parse) {
        Matcher matcher = PATTERN.matcher(parse);
        StringBuilder buffer = new StringBuilder();
        while(matcher.find()) {
            String matched = matcher.group(1);
            String[] split = matched.split("_");
            matcher.appendReplacement(buffer, extensions.getOrDefault(split[0], emptyExtension).onRequestRelational(p, matched.replaceFirst(split[0] + "_", "")).join());
        }
        return matcher.appendTail(buffer).toString();
    }
}