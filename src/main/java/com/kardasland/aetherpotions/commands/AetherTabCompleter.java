package com.kardasland.aetherpotions.commands;

import com.kardasland.aetherpotions.utility.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AetherTabCompleter implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        final List<String> oneArgList = new ArrayList<>();
        if (strings.length == 1) {
            oneArgList.add("reload");
            oneArgList.add("give");
            oneArgList.add("list");
            oneArgList.add("info");
            oneArgList.add("help");
            oneArgList.add("migrate");
            return oneArgList;
        }
        List<String> potionList = new ArrayList<>(ConfigManager.get("potions.yml").getConfigurationSection("potions").getKeys(false));
        switch (strings.length) {
            case 2 -> {
                return switch (strings[0]) {
                    case "info" -> potionList;
                    case "migrate" -> {
                        List<String> completions = new ArrayList<>(potionList);
                        completions.add("all");
                        yield potionList;
                    }
                    case "give" -> Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).toList();
                    default -> new ArrayList<>();
                };
            }
            case 3 -> {
                return switch (strings[0]) {
                    case "info" -> List.of("true", "false");
                    case "give" -> potionList;
                    default -> new ArrayList<>();
                };
            }
            case 4 -> {
                if (strings[0].equals("give")) {
                    return List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
                } else {
                    return new ArrayList<>();
                }
            }
        }
        return Collections.emptyList();

    }
}
