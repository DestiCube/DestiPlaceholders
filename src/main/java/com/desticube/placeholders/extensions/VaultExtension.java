package com.desticube.placeholders.extensions;

import com.desticube.placeholders.api.PlaceholderExtension;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

import java.security.Permissions;
import java.util.concurrent.CompletableFuture;

import static java.lang.String.valueOf;
import static net.kyori.adventure.text.serializer.gson.GsonComponentSerializer.gson;

public class VaultExtension extends PlaceholderExtension {

    private Economy econ = null;
    private Chat chat = null;
    private Permission perms = null;

    public VaultExtension() {
        RegisteredServiceProvider<Economy> econRSP = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        RegisteredServiceProvider<Chat> chatRSP = Bukkit.getServer().getServicesManager().getRegistration(Chat.class);
        RegisteredServiceProvider<Permission> permsRSP = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
        if (econRSP == null || chatRSP == null || permsRSP == null) {return;}
        econ = econRSP.getProvider();
        chat = chatRSP.getProvider();
        perms = permsRSP.getProvider();
    }

    @Override
    public @NotNull String getAuthor() {
        return "GamerDuck123";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "vault";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public @NotNull CompletableFuture<String> onRequestRelational(Player p, String params) {
        return CompletableFuture.supplyAsync(() -> {
            if (params.equalsIgnoreCase("primary_group")) return perms.getPrimaryGroup(p);
            else if (params.equalsIgnoreCase("balance")) return valueOf(econ.getBalance(p));
            else return params;
        });
    }
}
