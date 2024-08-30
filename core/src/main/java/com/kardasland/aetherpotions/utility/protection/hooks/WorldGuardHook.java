package com.kardasland.aetherpotions.utility.protection.hooks;

import com.kardasland.aetherpotions.utility.protection.hooks.ProtectionHook;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Objects;


public class WorldGuardHook implements ProtectionHook {
    @Override
    public boolean isAllowed(Player player, Location location, boolean strict){
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        if (strict){
            return query.testState(BukkitAdapter.adapt(location), localPlayer, Flags.POTION_SPLASH);
        }else {
            return !Objects.equals(query.queryState(BukkitAdapter.adapt(location), localPlayer, Flags.POTION_SPLASH), StateFlag.State.DENY);
        }
    }
}
