package com.desticube.placeholders;

import com.desticube.placeholders.api.Placeholders;
import com.desticube.placeholders.extensions.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import static com.desticube.placeholders.api.Placeholders.register;

public class PlaceholderMain extends JavaPlugin {

    @Override
    public void onEnable() {
        register(new PlayerExtension());
        register(new ServerExtension());
        if (getServer().getPluginManager().getPlugin("Vault") != null) register(new VaultExtension());
        if (getServer().getPluginManager().getPlugin("LuckPerms") != null) register(new LuckPermsExtension());
        if (getServer().getPluginManager().getPlugin("Lands") != null) register(new LandsExtension(this));
    }
}
