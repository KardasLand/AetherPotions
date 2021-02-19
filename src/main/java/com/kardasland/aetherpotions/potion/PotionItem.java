package com.kardasland.aetherpotions.potion;

import com.kardasland.aetherpotions.utils.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class PotionItem {
    private String title;
    private List<String> lore;
    private Potion potion;
    public PotionItem(String potionid){
        this.potion = new Potion(potionid);
        FileConfiguration cf = ConfigManager.get("config.yml");
        this.title = cf.getString("Potions."+potionid+".name");
        this.lore = cf.getStringList("Potions."+potionid+".lore");
    }

    public String getTitle() {
        return title;
    }

    public List<String> getLore() {
        return lore;
    }
}
