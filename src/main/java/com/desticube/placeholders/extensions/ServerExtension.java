package com.desticube.placeholders.extensions;

import com.desticube.placeholders.api.PlaceholderExtension;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

import static com.desticube.placeholders.api.Colors.replace;
import static java.lang.String.valueOf;

public class ServerExtension extends PlaceholderExtension {

    @Override
    public @NotNull String getAuthor() {
        return "GamerDuck123";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "server";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public @NotNull CompletableFuture<String> onRequest(Player p, String params) {
        return CompletableFuture.supplyAsync(() -> {
            if (params.equalsIgnoreCase("playercount")) return valueOf(Bukkit.getOnlinePlayers().size());
            else return params;
        });
    }
}
