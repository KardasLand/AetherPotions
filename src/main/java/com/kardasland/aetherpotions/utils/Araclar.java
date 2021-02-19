package com.kardasland.aetherpotions.utils;

import com.kardasland.aetherpotions.AetherPotions;
import com.kardasland.aetherpotions.potion.Potion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Araclar {
    public static String color(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }
    public static List<String> color(List<String> list){
        List<String> temp = new ArrayList<>();
        for (String key : list){
            temp.add(color(key));
        }
        return temp;
    }
    public void noPerms(Player p, String placeholder){
        prefix(p, ConfigManager.get("messages.yml").getString("Not-Enough-Permission").replace("%permission%", placeholder));
    }
    public String getPrefix(){
        return ConfigManager.get("config.yml").getString("Prefix");
    }
    public void prefix(Player p, String message){
        p.sendMessage(color(getPrefix() + message));
    }
    public void prefix(String message){
        AetherPotions.instance.getLogger().info(color(getPrefix() + message));
    }
    public void nonPrefix(Player p, String message){
        p.sendMessage(color(message));
    }
    public void nonPrefix(String message){
        Bukkit.getLogger().info(color(message));
    }
    public int getCooldownSecond(){
        return ConfigManager.get("config.yml").getInt("Cooldown-Second");
    }
}
