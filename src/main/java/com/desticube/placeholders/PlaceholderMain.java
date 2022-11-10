package com.desticube.placeholders;

import com.desticube.placeholders.api.Placeholders;
import com.desticube.placeholders.extensions.LuckPermsExtension;
import com.desticube.placeholders.extensions.PlayerExtension;
import com.desticube.placeholders.extensions.VaultExtension;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import static com.desticube.placeholders.api.Placeholders.register;

public class PlaceholderMain extends JavaPlugin {

    @Override
    public void onEnable() {
        register(new PlayerExtension());
        if (getServer().getPluginManager().getPlugin("Vault") != null) register(new VaultExtension());
        if (getServer().getPluginManager().getPlugin("LuckPerms") != null) register(new LuckPermsExtension());
    }
}
