package com.kardasland.aetherpotions;

import com.kardasland.aetherpotions.commands.AetherTabCompleter;
import com.kardasland.aetherpotions.commands.AetherCommand;
import com.kardasland.aetherpotions.events.DrinkEvent;
import com.kardasland.aetherpotions.events.ServerNotifier;
import com.kardasland.aetherpotions.events.SplashEvent;
import com.kardasland.aetherpotions.utility.ConfigManager;
import com.kardasland.aetherpotions.utility.Misc;
import com.kardasland.aetherpotions.utility.nbt.NBTEditorWrapper;
import com.kardasland.aetherpotions.utility.nbt.NBTHandler;
import com.kardasland.aetherpotions.utility.nbt.PSCEditorWrapper;
import com.kardasland.aetherpotions.utility.PlaceholderHook;
import com.kardasland.aetherpotions.utility.protection.ProtectionHandler;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

@Getter
public final class AetherPotions extends JavaPlugin {
    /* TODO
    there can be a bug, if player drinks a potion, for example fly command,
    when server shuts down, player will still have the effect, but the potion will be gone

    Solution: save the potion effect to player's data, and when player joins, give the effect back
    Alternative solution: when server shuts down, trigger after effect event.

    Also, custom events. idk why we need them, but we can add them. just for fun
     */
    public static AetherPotions instance;
    NBTHandler nbtHandler;
    ProtectionHandler protectionHandler;

    @Override
    public void onEnable() {
        instance = this;
        initConfigs();
        updateConfig();
        initEvents();
        initCommands();
        initNBTHandler();
        initHooks();
        this.protectionHandler = new ProtectionHandler();
        this.protectionHandler.initProtectionHandlers();
    }

    /**
     * Update the config file if needed
     */
    private void updateConfig() {
        // 3.1.0 -> 3.1.1
        ConfigManager.update("messages.yml", "StillInCooldown", "You are still in cooldown! Remaining seconds: &b%time%");
    }

    private void initHooks() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Misc.send(null, "PlaceholderAPI found, enabling placeholders.", false);
            new PlaceholderHook().register();
        }
    }


    private void initNBTHandler() {
        // this function is trash as fuck
        // but it gets the job done
        String a = getServer().getClass().getPackage().getName();
        String version = a.substring(a.lastIndexOf('.') + 1);
        String nbtHandlerName;
        if (version.equals("v1_19_R1") || version.equals("v1_18_R1") || version.equals("v1_17_R1") || version.equals("v1_16_R3") || version.equals("v1_16_R2") || version.equals("v1_16_R1") || version.equals("v1_15_R1") || version.equals("v1_14_R1") || version.equals("v1_13_R2") || version.equals("v1_13_R1") || version.equals("v1_12_R1") || version.equals("v1_11_R1") || version.equals("v1_10_R1") || version.equals("v1_9_R2") || version.equals("v1_9_R1") || version.equals("v1_8_R3") || version.equals("v1_8_R2") || version.equals("v1_8_R1")) {
            nbtHandler = new NBTEditorWrapper();
            nbtHandlerName = "NBTEditor";
        } else {
            nbtHandler = new PSCEditorWrapper();
            nbtHandlerName = "PSCEditor";
        }
        Misc.send(null, "Using " + nbtHandlerName +" as NBT Handler", false);
    }

    private void initCommands() {
        Objects.requireNonNull(getCommand("aetherpotions")).setExecutor(new AetherCommand());
        Objects.requireNonNull(getCommand("aetherpotions")).setTabCompleter(new AetherTabCompleter());
    }

    private void initEvents() {
        Bukkit.getPluginManager().registerEvents(new DrinkEvent(), this);
        Bukkit.getPluginManager().registerEvents(new SplashEvent(), this);
        Bukkit.getPluginManager().registerEvents(new ServerNotifier(), this);
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
