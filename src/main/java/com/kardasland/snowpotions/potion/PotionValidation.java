package com.kardasland.snowpotions.potion;

import com.kardasland.snowpotions.utility.ConfigManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.potion.PotionType;

import java.util.List;

public class PotionValidation {
    @Getter @Setter
    CustomPotion customPotion;
    @Getter @Setter
    boolean isValid;
    @Getter @Setter
    boolean exists;
    @Getter @Setter
    List<Errors> potionErrors;
    String id;
    FileConfiguration cf;


    public PotionValidation(String id){
        this.cf = ConfigManager.get("potions.yml");
        assert cf != null;
        this.id = id;
        if (!cf.isConfigurationSection("potions.id")){
            setExists(false);
            return;
        }
        checkItems();
        checkPotionType();
        checkCommandList();
        checkAfterEffect();
        checkParticle();
        if (potionErrors.size() == 0){
            setValid(true);
        }
    }

    // Checking the particle list.
    private void checkParticle() {
        String particleKey = "potions."+id+".particle.";
        if (cf.isSet(particleKey + "enabled") && cf.getBoolean(particleKey + "enabled")){
            boolean particleError = true;
            String targetParticle = cf.getString(particleKey + "type");
            for (Particle particle : Particle.values()){
                if (particle.toString().equalsIgnoreCase(targetParticle)){
                    particleError = false;
                }
            }
            if (particleError) {
                potionErrors.add(Errors.PARTICLE);
            }
        }
    }

    // Checking the after effect list.
    private void checkAfterEffect() {
        String commands = "potions."+id+".commands.";
        String aftereffect = commands + "afterEffect.";
        if (cf.isSet(aftereffect + "enabled") && cf.getBoolean(aftereffect + "enabled")) {
            if (!cf.isSet(aftereffect + "commands")){
                potionErrors.add(Errors.AFTER_EFFECTS);
            }
        }
    }

    // Checking the splash & drink command list.
    private void checkCommandList() {
        String commands = "potions."+id+".commands.";
        String shortcut = "potions."+id+".";
        if (cf.isSet(shortcut + "isSplash") && cf.getBoolean(shortcut + "isSplash")){
            if (!cf.isSet(commands + "splashCommandList")){
                potionErrors.add(Errors.SPLASH_COMMANDS);
            }
        }else {
            if (!cf.isSet(commands + "drinkingCommandList")){
                potionErrors.add(Errors.DRINKING_COMMANDS);
            }
        }
    }

    // Checking potion type.
    private void checkPotionType() {
        String shortcut = "potions."+id+".";
        String targetType = cf.getString(shortcut + "data.potionType");
        boolean potionError = true;
        for (PotionType everyPotion : PotionType.values()){
            if (everyPotion.toString().equalsIgnoreCase(targetType)){
                potionError = false;
            }
        }
        if (potionError){
            potionErrors.add(Errors.POTION_TYPE);
        }
    }

    // Item things.
    private void checkItems() {
        String shortcut = "potions."+id+".";
        if (!cf.isSet(shortcut + "displayName"))
            potionErrors.add(Errors.DISPLAY_NAME);
        if (!cf.isSet(shortcut + "lore"))
            potionErrors.add(Errors.LORE);
        if (!cf.isSet(shortcut + "isSplash"))
            potionErrors.add(Errors.IS_SPLASH);
        if (!cf.isSet(shortcut + "deleteBottle") && !cf.getBoolean(shortcut + "isSplash"))
            potionErrors.add(Errors.DELETE_BOTTLE);
    }


    public enum Errors{
        DISPLAY_NAME,
        LORE,
        IS_SPLASH,
        DELETE_BOTTLE,
        POTION_TYPE,
        PARTICLE,
        DRINKING_COMMANDS,
        SPLASH_COMMANDS,
        AFTER_EFFECTS

    }
}