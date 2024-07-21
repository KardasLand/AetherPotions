package com.kardasland.aetherpotions.potion;

import com.kardasland.aetherpotions.utility.ConfigManager;
import com.kardasland.aetherpotions.utility.Probability;
import lombok.Data;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

// TODO
// This class is not complete.
// I need to work on more robust solution for this class.
// its 2 am and i am tired.
// For now, i will just add the required methods and fields to make the code compile.
// I will come back to this class later.

@Data
public class CustomCommandList {

    private List<String> drinkingCommandList;
    private List<String> splashCommandList;

    public boolean includeChance;
    public boolean isSplash;
    Probability beforeProbability;
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
        validateChance();
    }



    /**
     * From this point, the code is not complete.
     */


    private void validateChance(){
        // split the commands by "::" and add the number between that 2 to the chance list
        // "command::chance"
        this.beforeProbability = parseChances(isSplash ? splashCommandList : drinkingCommandList);
        if (afterEffect.isEnabled()){
            this.afterEffect.setProbability(parseChances(afterEffect.getCommandList()));
        }
    }

    private Probability parseChances(List<String> commandList) {
        Probability probability = new Probability();
        for (String command : commandList) {
            String[] split = command.split("::");
            if (split.length == 2) {
                if (!includeChance) {
                    includeChance = true;
                }
                probability.addChance(split[0], Integer.parseInt(split[1]));
            }
        }
        return probability;
    }

    public List<String> drawCommandList(){
        if (includeChance){
            List<String> commands = new ArrayList<>();
            String chance = (String) beforeProbability.getRandomElement();
            commands.add(chance);
            return commands;
        }else {
            return isSplash ? splashCommandList : drinkingCommandList;
        }
    }

    @Data
    public static class AfterEffect{
        private boolean enabled;
        private int time;
        private List<String> commandList;
        Probability probability;


        public AfterEffect(String id) {
            FileConfiguration cf = ConfigManager.get("potions.yml");
            String shortcut = "potions."+id+".commands.afterEffect.";
            this.enabled = cf.getBoolean(shortcut + "enabled");
            this.time = cf.getInt(shortcut + "time");
            this.commandList = cf.getStringList(shortcut + "commands");
        }


        // From this point, the code is not complete.
        public String drawCommandList(){
            if (enabled){
                String command = (String) probability.getRandomElement();
                return command;
            }else {
                return null;
            }
        }
    }
}
