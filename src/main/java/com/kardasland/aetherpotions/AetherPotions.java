package com.kardasland.aetherpotions;

import com.kardasland.aetherpotions.commands.MainCommand;
import com.kardasland.aetherpotions.events.DrinkEvent;
import com.kardasland.aetherpotions.events.SplashEvent;
import com.kardasland.aetherpotions.utility.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class AetherPotions extends JavaPlugin {

    public static AetherPotions instance;

    @Override
    public void onEnable() {
        instance = this;
        initConfigs();
        initEvents();
        initCommands();
    }

    private void initCommands() {
        Objects.requireNonNull(getCommand("aetherpotions")).setExecutor(new MainCommand());
    }

    private void initEvents() {
        Bukkit.getPluginManager().registerEvents(new DrinkEvent(), this);
        Bukkit.getPluginManager().registerEvents(new SplashEvent(), this);
    }

    private void initConfigs() {
        ConfigManager.load("potions.yml");
        ConfigManager.load("config.yml");
        ConfigManager.load("messages.yml");
    }

    @Override
    public void onDisable() {
    }

    public boolean reloadPlugin() {
        ConfigManager.reload("config.yml");
        ConfigManager.reload("potions.yml");
        ConfigManager.reload("messages.yml");
        return true;
    }
}
