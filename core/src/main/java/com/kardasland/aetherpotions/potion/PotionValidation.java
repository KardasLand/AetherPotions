package com.kardasland.aetherpotions.potion;

import com.kardasland.aetherpotions.utility.ConfigManager;
import lombok.Data;
import lombok.Getter;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.List;


/**
 * Need to add more error checking and validation.
 * Lacks update.
 */
@Data
public class PotionValidation {
    boolean isValid;
    boolean exists;
    List<Errors> potionErrors;
    String id;
    FileConfiguration cf;

    public PotionValidation(String id){
        this.cf = ConfigManager.get("potions.yml");
        assert cf != null;
        this.id = id;
        this.potionErrors = new ArrayList<>();
        setExists(cf.isConfigurationSection("potions."+id));
        if (!exists){
            return;
        }
        checkItems();
        checkPotionType();
        checkOldImplementation();
        checkCommandList();
        checkAfterEffect();
        checkParticle();
        //checkExtendAndUpgrade();
        if (potionErrors.isEmpty()){
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


    /**
     * Checking if the potion is extended and upgraded at the same time.
     * @deprecated Since 3.0.1
     */
    @Deprecated(since = "3.0.1", forRemoval = true)
    private void checkExtendAndUpgrade(){
        String shortcut = "potions."+id+".";
        boolean isExtended = cf.isSet(shortcut + "data.extended") && cf.getBoolean(shortcut + "data.extended");
        boolean isUpgraded = cf.isSet(shortcut + "data.upgraded") && cf.getBoolean(shortcut + "data.upgraded");
        if (isExtended && isUpgraded){
            potionErrors.add(Errors.BOTH_EXTENDED_AND_UPGRADED);
        }
    }

    // Checking old implementation.
    private void checkOldImplementation(){
        if (potionErrors.contains(Errors.IS_SPLASH)){
            return;
        }
        if (cf.isList("potions."+id+".commands.afterEffect.commands")){
            potionErrors.add(Errors.OLD_IMPLEMENTATION);
            return;
        }
        if (!cf.getBoolean("potions."+id+".isSplash") && cf.isList("potions."+id+".commands.drinkingCommands")){
            potionErrors.add(Errors.OLD_IMPLEMENTATION);
        }
        if (cf.getBoolean("potions."+id+".isSplash") && cf.isList("potions."+id+".commands.splashCommands")){
            potionErrors.add(Errors.OLD_IMPLEMENTATION);
        }
    }

    // Checking the splash & drink command list.
    private void checkCommandList() {
        String commands = "potions."+id+".commands.";
        String shortcut = "potions."+id+".";

        if (cf.isSet(shortcut + "isSplash") && cf.getBoolean(shortcut + "isSplash")){
            if (!cf.isSet(commands + "splashCommands")){
                potionErrors.add(Errors.SPLASH_COMMANDS);
            }
        }else {
            if (!cf.isSet(commands + "drinkingCommands")){
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


    @Getter
    public enum Errors{
        DISPLAY_NAME("Display name not found or not correct."),
        LORE("Lore not found or not correct."),
        IS_SPLASH ("Splash option is missing."),
        DELETE_BOTTLE("Delete bottle parameter is missing."),
        POTION_TYPE ("Potion type is missing or invalid."),
        PARTICLE ("Particle type is missing or invalid."),
        DRINKING_COMMANDS ("Drinking commands are missing."),
        SPLASH_COMMANDS ("Splash commands are missing."),
        AFTER_EFFECTS ("After effects are missing."),
        BOTH_EXTENDED_AND_UPGRADED ("Both extended and upgraded parameters are set to true."),
        OLD_IMPLEMENTATION("Old implementation of potion commands detected. Please migrate..");


        final String message;
        Errors(String message){
            this.message = message;
        }



    }
}
