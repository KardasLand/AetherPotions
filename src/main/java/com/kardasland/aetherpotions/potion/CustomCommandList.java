package com.kardasland.aetherpotions.potion;

import com.kardasland.aetherpotions.utility.ConfigManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class CustomCommandList {

    @Getter @Setter
    private List<String> drinkingCommandList;
    @Getter @Setter
    private List<String> splashCommandList;
    @Getter @Setter
    public boolean isSplash;
    @Getter @Setter AfterEffect afterEffect;

    public CustomCommandList(String id, boolean isSplash) {
        FileConfiguration cf = ConfigManager.get("potions.yml");
        this.isSplash = isSplash;
        String shortcut = "potions."+id+".commands.";
        if (isSplash){
            this.splashCommandList = cf.getStringList(shortcut + "splashCommands");
        }else {
            this.drinkingCommandList = cf.getStringList(shortcut + "drinkingCommands");
        }
        this.afterEffect = new AfterEffect(id);
    }

    public static class AfterEffect{
        @Getter @Setter
        private boolean enabled;
        @Getter @Setter
        private int time;
        @Getter @Setter
        private List<String> commands;

        public AfterEffect(String id) {
            FileConfiguration cf = ConfigManager.get("potions.yml");
            String shortcut = "potions."+id+".commands.afterEffect.";
            this.enabled = cf.getBoolean(shortcut + "enabled");
            this.time = cf.getInt(shortcut + "time");
            this.commands = cf.getStringList(shortcut + "commands");
        }
    }
}
