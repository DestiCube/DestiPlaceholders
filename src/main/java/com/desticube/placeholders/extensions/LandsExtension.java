package com.desticube.placeholders.extensions;

import com.desticube.placeholders.api.Colors;
import com.desticube.placeholders.api.PlaceholderExtension;
import me.angeschossen.lands.api.integration.LandsIntegration;
import me.angeschossen.lands.api.player.LandPlayer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.desticube.placeholders.api.Colors.replace;
import static com.desticube.placeholders.api.Colors.replaceOldColors;
import static java.lang.String.valueOf;

public class LandsExtension extends PlaceholderExtension {

    private LandsIntegration lands = null;

    public LandsExtension(JavaPlugin plugin) {
        lands = new LandsIntegration(plugin);
    }

    @Override
    public @NotNull String getAuthor() {
        return "GamerDuck123";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "lands";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public @NotNull CompletableFuture<String> onRequest(Player p, String params) {
        return CompletableFuture.supplyAsync(() -> {
            if (p == null) return params;
            LandPlayer player = lands.getLandPlayer(p.getUniqueId());
            if (player.getLands() == null || player.getLands().isEmpty()) return "<red>None";
            else if (params.equalsIgnoreCase("land_name")) return replaceOldColors(player.getOwningLand() == null ? player.getLandNames()[0] : player.getOwningLand().getName());
            else if (params.equalsIgnoreCase("land_balance")) return replaceOldColors(valueOf(player.getOwningLand() == null ? player.getLand(player.getLandNames()[0]).getBalance() : player.getOwningLand().getBalance()));
            else if (params.equalsIgnoreCase("land_members")) return replaceOldColors(player.getOwningLand() == null ?  player.getLand(player.getLandNames()[0]).getOnlinePlayers().stream().map(pl -> pl.getName()).collect(Collectors.toList()).toString().replaceAll("\\[", "").replaceAll("\\]", "") :
                    player.getOwningLand().getOnlinePlayers().stream().map(pl -> pl.getName()).collect(Collectors.toList()).toString().replaceAll("\\[", "").replaceAll("\\]", ""));
            else return params;
        });
    }
}
