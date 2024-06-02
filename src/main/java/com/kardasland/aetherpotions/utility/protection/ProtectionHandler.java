package com.kardasland.aetherpotions.utility.protection;

import com.kardasland.aetherpotions.utility.protection.hooks.ProtectionHook;
import com.kardasland.aetherpotions.utility.protection.hooks.WorldGuardHook;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getLogger;

public class ProtectionHandler {
    // In the future, we can add more protection hooks here
    // For now, we only have WorldGuard
    // You can add more protection plugins like GriefPrevention, etc.
    // Or you can ask me to add them for you

    List<ProtectionHook> protectionHandlers;
    public ProtectionHandler(){
        this.protectionHandlers = new ArrayList<>();
    }

    public void initProtectionHandlers() {
        // check for worldguard and other protection plugins, then add them to protectionHandlers list
        if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null){
            getLogger().info("WorldGuard found, adding to protection handlers.");
            protectionHandlers.add(new WorldGuardHook());
        }
    }

    public boolean isAllowed(Player player, Location location, boolean strict){
        for (ProtectionHook protectionHandler : protectionHandlers) {
            if (!protectionHandler.isAllowed(player, location, strict)){
                return false;
            }
        }
        return true;
    }
}
