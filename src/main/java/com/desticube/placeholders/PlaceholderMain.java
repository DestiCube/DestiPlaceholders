package com.desticube.placeholders;

import com.desticube.placeholders.extensions.LuckPermsExtension;
import com.desticube.placeholders.extensions.PlayerExtension;
import com.desticube.placeholders.extensions.VaultExtension;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class PlaceholderMain extends JavaPlugin {

    @Override
    public void onEnable() {
        new PlayerExtension().register();
        if (getServer().getPluginManager().getPlugin("Vault") != null) new VaultExtension().register();
        if (getServer().getPluginManager().getPlugin("LuckPerms") != null) new LuckPermsExtension().register();
    }
}
