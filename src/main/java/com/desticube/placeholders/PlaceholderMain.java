package com.desticube.placeholders;

import com.desticube.placeholders.api.PAPIConverter;
import com.desticube.placeholders.api.Placeholders;
import com.desticube.placeholders.extensions.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;

import static com.desticube.placeholders.api.Placeholders.register;

public class PlaceholderMain extends JavaPlugin {

    @Override
    public void onEnable() {
        register(new PlayerExtension());
        register(new ServerExtension());
        try {
            PAPIConverter.registerAll(this);
            Placeholders.registerLocal(this);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException
                | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
