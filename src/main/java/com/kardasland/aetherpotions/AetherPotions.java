package com.kardasland.aetherpotions;

import com.kardasland.aetherpotions.commands.MainCommand;
import com.kardasland.aetherpotions.events.DrinkEvent;
import com.kardasland.aetherpotions.events.SplashEvent;
import com.kardasland.aetherpotions.utility.ConfigManager;
import com.kardasland.aetherpotions.utility.Misc;
import com.kardasland.aetherpotions.utility.NBT.NBTEditorWrapper;
import com.kardasland.aetherpotions.utility.protection.ProtectionHandler;
import com.kardasland.aetherpotions.utility.NBT.NBTHandler;
import com.kardasland.aetherpotions.utility.NBT.PSCEditorWrapper;
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
        initEvents();
        initCommands();
        initNBTHandler();
        this.protectionHandler = new ProtectionHandler();
        this.protectionHandler.initProtectionHandlers();
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
