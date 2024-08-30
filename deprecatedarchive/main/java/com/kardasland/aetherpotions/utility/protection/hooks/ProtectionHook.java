package com.kardasland.aetherpotions.utility.protection.hooks;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface ProtectionHook {
    boolean isAllowed(Player player, Location location, boolean strict);
}
