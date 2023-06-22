package com.kardasland.snowpotions.utility;

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
        p.sendMessage(((prefix) ? ConfigManager.get("config.yml").getString("Prefix") : "") + color(s));
    }
}
