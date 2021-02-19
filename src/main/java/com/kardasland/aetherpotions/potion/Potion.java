package com.kardasland.aetherpotions.potion;

import com.kardasland.aetherpotions.utils.ConfigManager;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.potion.PotionType;

import java.util.List;

public class Potion {
    private String potionid;
    private PotionType potionType;
    private boolean particle_enabled;
    private String particle_type;
    private int particle_time;
    private int particle_amount;
    private boolean delete_bottle;
    private List<String> commandlist;
    private boolean after_effect_enabled;
    private int after_effect_time;
    private List<String> after_effect_commands;

    public Potion(String potionid){
        this.potionid = potionid;
        FileConfiguration cf = ConfigManager.get("config.yml");
        this.potionType = PotionType.valueOf(cf.getString("Potions."+potionid+".type"));
        this.delete_bottle = cf.getBoolean("Potions."+potionid+".delete-bottle-after-drink");
        this.commandlist = cf.getStringList("Potions."+potionid+".commands-on-drink");
        this.particle_enabled = cf.getBoolean("Potions."+potionid+".particle.enabled");
        if (this.particle_enabled){
            this.particle_time = cf.getInt("Potions."+potionid+".particle.time");
            this.particle_amount = cf.getInt("Potions."+potionid+".particle.amount");
            this.particle_type = cf.getString("Potions."+potionid+".particle.type");
        }
        try {
            this.after_effect_enabled = cf.getBoolean("Potions."+potionid+".after-effect.enabled");
        }catch (NullPointerException ex){
            this.after_effect_enabled = false;
        }
        if (this.after_effect_enabled){
            this.after_effect_time = cf.getInt("Potions."+potionid+".after-effect.time");
            this.after_effect_commands = cf.getStringList("Potions."+potionid+".after-effect.commands");
        }
    }

    public String getParticle_type() {
        return particle_type;
    }
    public boolean getParticle_enabled(){
        return particle_enabled;
    }
    public PotionType getPotionType() {
        return potionType;
    }

    public String getPotionid() {
        return potionid;
    }

    public int getParticle_amount() {
        return particle_amount;
    }

    public int getParticle_time() {
        return particle_time;
    }

    public boolean getDeleteBottle() {
        return delete_bottle;
    }

    public List<String> getCommandlist() {
        return commandlist;
    }

    public int getAfter_effect_time() {
        return after_effect_time;
    }

    public List<String> getAfter_effect_commands() {
        return after_effect_commands;
    }
    public boolean getAfter_effect_enabled(){
        return after_effect_enabled;
    }
}
