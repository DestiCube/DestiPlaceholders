package com.desticube.placeholders.extensions;

import com.desticube.placeholders.api.PlaceholderExtension;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.String.valueOf;
import static net.kyori.adventure.text.serializer.gson.GsonComponentSerializer.gson;

public class PlayerExtension extends PlaceholderExtension {
    @Override
    public @NotNull String getAuthor() {
        return "GamerDuck123";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "player";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public @NotNull CompletableFuture<String> onRequestRelational(Player player, String params) {
        AtomicReference<Player> play = new AtomicReference<>(player);
        return CompletableFuture.supplyAsync(() -> {
            Player p = play.get();
            if (params.equalsIgnoreCase("name")) return p.getName();
            else if (params.equalsIgnoreCase("exp_level")) return valueOf(p.getExpToLevel());
            else if (params.equalsIgnoreCase("exp_amount")) return valueOf(p.getTotalExperience());
            else if (params.equalsIgnoreCase("displayname")) return gson().serialize(p.displayName());
            else if (params.equalsIgnoreCase("gamemode")) return p.getGameMode().toString();
            else if (params.equalsIgnoreCase("is_whitelisted")) return valueOf(p.isWhitelisted());
            else if (params.equalsIgnoreCase("ping")) return valueOf(p.getPing());
            else if (params.equalsIgnoreCase("world")) return p.getWorld().getName();
            return params;
        });
    }
}
