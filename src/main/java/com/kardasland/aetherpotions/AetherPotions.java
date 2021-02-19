package com.kardasland.aetherpotions;

import com.kardasland.aetherpotions.commands.MainCommand;
import com.kardasland.aetherpotions.events.PotionDrinkEvent;
import com.kardasland.aetherpotions.support.Support;
import com.kardasland.aetherpotions.support.types.Legacy;
import com.kardasland.aetherpotions.support.types.New;
import com.kardasland.aetherpotions.utils.ConfigManager;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class AetherPotions extends JavaPlugin {
    private Support support;
    public static AetherPotions instance;
    @Override
    public void onEnable() {
        instance = this;
        loadConfigs();
        loadSupport();
        loadCommands();
        loadEvents();
    }
    private void loadConfigs(){
        ConfigManager.load("config.yml");
        ConfigManager.load("messages.yml");
    }
    private void loadSupport(){
        if (ConfigManager.get("config.yml").getBoolean("force-new-support")){
            this.support = new New();
            getLogger().info("Added 1.12+ support forcefully.");
        }else {
            if (NBTEditor.getMinecraftVersion().greaterThanOrEqualTo(NBTEditor.MinecraftVersion.v1_12)){
                this.support = new New();
                getLogger().info("1.12+ support added.");
            }else {
                this.support = new Legacy();
                getLogger().info("Legacy(1.11-) support added.");
            }
        }

    }
    private void loadCommands(){
        getCommand("aetherpotions").setExecutor(new MainCommand());
    }
    private void loadEvents(){
        Bukkit.getPluginManager().registerEvents(new PotionDrinkEvent(), this);
    }
    @Override
    public void onDisable() {
    }

    public Support getSupport() {
        return support;
    }
}
