package com.kardasland.aetherpotions.utility;

import com.kardasland.aetherpotions.AetherPotions;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Misc {
    public static String color(String message){
        try {
            Pattern pattern = Pattern.compile("&#[a-fA-F0-9]{6}");
            Matcher matcher = pattern.matcher(message);
            while (matcher.find()) {
                String color = message.substring(matcher.start(), matcher.end());
                message = message.replace(color, "" + ChatColor.of(color.substring(1)));
                matcher = pattern.matcher(message);
            }

            // Exception is a bit of a hack, i still need to spigot version checker. But it works, so who cares lmao
        }catch (Exception ignored){}

        return ChatColor.translateAlternateColorCodes('&', message);
    }
    public static List<String> color(List<String> messages){
        List<String> list = new ArrayList<>();
        for (String message : messages){
            list.add(color(message));
        }
        return list;
    }

    public static boolean checkPerm(Player player, String permission){
        return checkPerm(player, permission, false);
    }
    public static boolean checkPerm(Player player, String permission, boolean silent){
        if (player == null)
            return true;
        if (!player.hasPermission(permission) && !silent)
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
