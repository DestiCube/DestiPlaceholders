package com.desticube.placeholders.extensions;

import com.desticube.placeholders.api.Colors;
import com.desticube.placeholders.api.PlaceholderExtension;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

import static com.desticube.placeholders.api.Colors.replace;
import static java.lang.String.valueOf;

public class LuckPermsExtension extends PlaceholderExtension {

    private LuckPerms perms = null;

    public LuckPermsExtension() {
        perms = LuckPermsProvider.get();
    }

    @Override
    public @NotNull String getAuthor() {
        return "GamerDuck123";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "luckperms";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public @NotNull CompletableFuture<String> onRequest(Player p, String params) {
        return CompletableFuture.supplyAsync(() -> {
            if (p == null) return params;
            User user = perms.getUserManager().loadUser(p.getUniqueId()).join();
            if (params.equalsIgnoreCase("primary_group")) return user.getPrimaryGroup();
            else if (params.equalsIgnoreCase("prefix")) return replace(user.getCachedData().getMetaData().getPrefix());
            else if (params.equalsIgnoreCase("suffix")) return replace(user.getCachedData().getMetaData().getSuffix());
            else return params;
        });
    }
}
