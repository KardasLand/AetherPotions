package com.kardasland.aetherpotions.utility;

import com.kardasland.aetherpotions.AetherPotions;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Misc {
    public static String color(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    public static List<String> color(List<String> messages){
        List<String> list = new ArrayList<>();
        for (String message : messages){
            list.add(ChatColor.translateAlternateColorCodes('&', message));
        }
        return list;
    }

    public static boolean checkPerm(Player player, String permission){
        if (player == null)
            return true;
        if (!player.hasPermission(permission))
            send(player, ConfigManager.get("messages.yml").getString("NoPermission").replace("%permission%", permission), true);
        return player.hasPermission(permission);
    }

    public static void send(Player p, String s, boolean prefix){
        try {
            String wholeMessage = ((prefix) ? ConfigManager.get("config.yml").getString("Prefix") : "") + color(s);

            if (p != null) {
                p.sendMessage(color(wholeMessage));
            } else {
                AetherPotions.instance.getLogger().info(color(wholeMessage));
            }
        }catch (NullPointerException exception){
            if (p != null){
                p.sendMessage(color("&b[AetherPotions] &cAn error occurred! Please contact administrators about the issue."));
                AetherPotions.instance.getLogger().info(color("&cAn error occurred! Likely prefix section on config is missing.."));
            }else {
                AetherPotions.instance.getLogger().info(color("&cAn error occurred! Likely prefix section on config is missing.."));
            }

        }
    }
}
