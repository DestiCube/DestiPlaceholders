package com.desticube.placeholders.api;

import com.google.common.io.Resources;
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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import static java.lang.Class.forName;

public class PAPIConverter {

    public static void registerAll(JavaPlugin plugin) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        File folder = new File(plugin.getDataFolder() + File.separator + "papi");
        if (!folder.exists()) folder.mkdirs();
        for (File file : folder.listFiles()) {
            Class<?> clazz = findClass(file, forName("me.clip.placeholderapi.expansion.PlaceholderExpansion"));
            Object obj = clazz.getDeclaredConstructor().newInstance();
            Method[] methods = clazz.getDeclaredMethods();
            AtomicReference<Method> onRequest = new AtomicReference<Method>();
            AtomicReference<Method> getAuthor = new AtomicReference<Method>();
            AtomicReference<Method> getIdentifier = new AtomicReference<Method>();
            AtomicReference<Method> getVersion = new AtomicReference<Method>();
            Arrays.stream(methods).forEach(method -> {
                if (method.getName().equalsIgnoreCase("onRequest")) onRequest.set(method);
                else if (method.getName().equalsIgnoreCase("getAuthor")) getAuthor.set(method);
                else if (method.getName().equalsIgnoreCase("getIdentifier")) getIdentifier.set(method);
                else if (method.getName().equalsIgnoreCase("getVersion")) getVersion.set(method);
            });
            Placeholders.register(new PlaceholderExtension() {
                @Override
                public @NotNull String getAuthor() {
                    getAuthor.get().setAccessible(true);
                    Object o = null;
                    try {
                        o = getAuthor.get().invoke(obj);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                    getAuthor.get().setAccessible(false);
                    Bukkit.getLogger().info(o instanceof String ? (String) o : "NULL");
                    return o instanceof String ? (String) o : "NULL";

                }

                @Override
                public @NotNull String getIdentifier() {
                    getIdentifier.get().setAccessible(true);
                    Object o = null;
                    try {
                        o = getIdentifier.get().invoke(obj);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                    getIdentifier.get().setAccessible(true);
                    Bukkit.getLogger().info(o instanceof String ? (String) o : "NULL");
                    return o instanceof String ? (String) o : "NULL";
                }

                @Override
                public @NotNull String getVersion() {
                    getVersion.get().setAccessible(true);
                    Object o = null;
                    try {
                        o = getVersion.get().invoke(obj);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                    getVersion.get().setAccessible(true);
                    Bukkit.getLogger().info(o instanceof String ? (String) o : "NULL");
                    return o instanceof String ? (String) o : "NULL";
                }

                @Override
                public CompletableFuture<String> onRequest(Player player, String params) {
                    return CompletableFuture.supplyAsync(() -> {
                        onRequest.get().setAccessible(true);
                        Object o = null;
                        try {
                            o = onRequest.get().invoke(obj, player, params);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                        onRequest.get().setAccessible(true);
                        Bukkit.getLogger().info(o instanceof String ? (String) o : "NULL");
                        return o instanceof String ? (String) o : "NULL";
                    });
                }
            });
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
