package com.kardasland.snowpotions;

import com.kardasland.snowpotions.commands.MainCommand;
import com.kardasland.snowpotions.events.DrinkEvent;
import com.kardasland.snowpotions.events.SplashEvent;
import com.kardasland.snowpotions.utility.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class AetherPotions extends JavaPlugin {

    public static AetherPotions instance;

    /***TODO
        Main hand - off hand detection
        then idk, go to the testing maybe?
     ***/


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
