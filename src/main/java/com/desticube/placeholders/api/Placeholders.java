package com.desticube.placeholders.api;

import com.desticube.placeholders.extensions.EmptyExtension;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Class.forName;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;
import static net.kyori.adventure.text.serializer.gson.GsonComponentSerializer.gson;

public class Placeholders {
    public static final ConcurrentHashMap<String, PlaceholderExtension> extensions = new ConcurrentHashMap<>();

    public static void register(PlaceholderExtension extension) {
        extensions.put(extension.getIdentifier(), extension);
    }

    private static final PlaceholderExtension emptyExtension = new EmptyExtension();
    private static final Pattern PATTERN = Pattern.compile("%(.*?)%");

    public static Component setPlaceholders(Player p, Component parse) {
        return parse.replaceText(b ->
                    b.match(PATTERN).replacement((matcher, builder) -> {
                        String matched = matcher.group(1);
                        String[] split = matched.split("_");
                        String toDeserialize = extensions.getOrDefault(split[0], emptyExtension).onRequest(p, matched.replaceFirst(split[0] + "_", "")).join();
                        return miniMessage().deserialize(toDeserialize == null ? "" : toDeserialize);
                    })
                );
    }

    public static String setPlaceholders(Player p, String parse) {
        Matcher matcher = PATTERN.matcher(parse);
        StringBuilder buffer = new StringBuilder();
        while(matcher.find()) {
            String matched = matcher.group(1);
            String[] split = matched.split("_");
            matcher.appendReplacement(buffer, extensions.getOrDefault(split[0], emptyExtension).onRequest(p, matched.replaceFirst(split[0] + "_", "")).join());
        }
        return matcher.appendTail(buffer).toString();
    }

    public static void registerLocal(JavaPlugin plugin) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        File folder = new File(plugin.getDataFolder() + File.separator + "local");
        if (!folder.exists()) folder.mkdirs();
        for (File file : folder.listFiles()) {
            PlaceholderExtension clazz = findClass(file, PlaceholderExtension.class).getDeclaredConstructor().newInstance();
            register(clazz);
        }
    }

    private static <T> Class<? extends T> findClass(@NotNull final File file,
                                            @NotNull final Class<T> clazz)  {
        try {
            if (!file.exists()) {
                return null;
            }

            final URL jar = file.toURI().toURL();
            final URLClassLoader loader = new URLClassLoader(new URL[]{jar}, clazz.getClassLoader());
            final List<String> matches = new ArrayList<>();
            final List<Class<? extends T>> classes = new ArrayList<>();

            try (final JarInputStream stream = new JarInputStream(jar.openStream())) {
                JarEntry entry;
                while ((entry = stream.getNextJarEntry()) != null) {
                    final String name = entry.getName();
                    if (name.isEmpty() || !name.endsWith(".class")) {
                        continue;
                    }

                    matches.add(name.substring(0, name.lastIndexOf('.')).replace('/', '.'));
                }

                for (final String match : matches) {
                    try {
                        final Class<?> loaded = loader.loadClass(match);
                        if (clazz.isAssignableFrom(loaded)) {
                            classes.add(loaded.asSubclass(clazz));
                        }
                    } catch (final NoClassDefFoundError ignored) {
                    }
                }
            }
            if (classes.isEmpty()) {
                loader.close();
                return null;
            }
            return classes.get(0);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}