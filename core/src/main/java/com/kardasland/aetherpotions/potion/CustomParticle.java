package com.kardasland.aetherpotions.potion;

import com.kardasland.aetherpotions.utility.ConfigManager;
import lombok.Data;
import org.bukkit.configuration.file.FileConfiguration;

@Data
public class CustomParticle {
    private boolean enabled;
    private String type;
    private int time;
    private int amount;

    public CustomParticle(String id) {
        FileConfiguration cf = ConfigManager.get("potions.yml");
        String shortcut = "potions."+id+".particle.";
        this.enabled = cf.getBoolean(shortcut + "enabled");
        this.type = cf.getString(shortcut + "type");
        this.time = cf.getInt(shortcut + "time");
        this.amount = cf.getInt(shortcut + "amount");
    }
}
