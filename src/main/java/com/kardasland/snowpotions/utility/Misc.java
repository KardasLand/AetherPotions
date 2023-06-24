package com.kardasland.snowpotions.utility;

import com.kardasland.snowpotions.AetherPotions;
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

    public static void send(Player p, String s, boolean prefix){
        try {
            String wholeMessage = ((prefix) ? ConfigManager.get("config.yml").getString("Prefix") : "") + color(s);

            if (p != null) {
                p.sendMessage(wholeMessage);
            } else {
                AetherPotions.instance.getLogger().info(wholeMessage);
            }
        }catch (NullPointerException exception){
            if (p != null){
                p.sendMessage(Misc.color("&b[AetherPotions] &cAn error occurred! Please contact administrators about the issue."));
                AetherPotions.instance.getLogger().info(Misc.color("&cAn error occurred! Likely prefix section on config is missing.."));
            }else {
                AetherPotions.instance.getLogger().info(Misc.color("&cAn error occurred! Likely prefix section on config is missing.."));
            }

        }
    }
}
