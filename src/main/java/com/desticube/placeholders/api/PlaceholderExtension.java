package com.desticube.placeholders.api;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class PlaceholderExtension {

    @NotNull
    public abstract String getAuthor();

    @NotNull
    public abstract String getIdentifier();

    @NotNull
    public abstract String getVersion();

    public CompletableFuture<String> onRequest(Player player, String params) {
        return completedFuture(params);
    }

}
