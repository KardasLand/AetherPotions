package com.kardasland.aetherpotions.utility;

import com.kardasland.aetherpotions.AetherPotions;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderHook extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "aetherpotions";
    }

    @Override
    public @NotNull String getAuthor() {
        return "KardasLand";
    }

    @Override
    public @NotNull String getVersion() {
        return AetherPotions.instance.getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.matches("cooldown_[A-Za-z0-9]+")) {
            String[] split = params.split("_");
            if (split.length == 2) {
                Player p = player.getPlayer();
                if (p != null) {
                    boolean inCooldown = CooldownHandler.isInCooldown(p.getUniqueId(), split[1]);
                    if (!inCooldown) {
                        return "0";
                    }
                    return String.valueOf(CooldownHandler.getTimeLeft(p.getUniqueId(), split[1]));
                }
            }
        }

        return null;
    }
}
