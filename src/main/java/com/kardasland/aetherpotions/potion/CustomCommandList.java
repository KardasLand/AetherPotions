package com.kardasland.aetherpotions.potion;

import com.kardasland.aetherpotions.utility.ConfigManager;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;


@Data
public class CustomCommandList {

    private List<String> drinkingCommandList;
    private List<String> splashCommandList;
    public boolean isSplash;
    AfterEffect afterEffect;

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


    @Data
    public static class AfterEffect{
        private boolean enabled;
        private int time;
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
