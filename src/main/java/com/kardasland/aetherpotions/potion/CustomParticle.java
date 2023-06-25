package com.kardasland.aetherpotions.potion;

import com.kardasland.aetherpotions.utility.ConfigManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;

public class CustomParticle {
    @Getter @Setter
    private boolean enabled;
    @Getter @Setter
    private String type;
    @Getter @Setter
    private int time;
    @Getter @Setter
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
