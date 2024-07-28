package com.kardasland.aetherpotions.potion;

import com.kardasland.aetherpotions.AetherPotions;
import com.kardasland.aetherpotions.utility.ConfigManager;
import com.kardasland.aetherpotions.utility.Probability;
import lombok.Data;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class CCommandList {
    List<CCommand> drinkingCommandList = new ArrayList<>();
    List<CCommand> splashCommandList = new ArrayList<>();
    boolean isSplash;
    AfterEffect afterEffect;
    String id;

    public CCommandList(String id, boolean isSplash) {
        this.id = id;
        FileConfiguration cf = ConfigManager.get("potions.yml");
        this.isSplash = isSplash;
        String shortcut = "potions."+id+".commands." + (isSplash ? "splashCommands" : "drinkingCommands");
        this.afterEffect = new AfterEffect(id);
        if (cf.isList(shortcut)){
            return;
        }
        for (String order : cf.getConfigurationSection(shortcut).getKeys(false)){
            ConfigurationSection section = cf.getConfigurationSection(shortcut+"."+order);
            assert section != null;
            int chance = section.isSet("chance") ? section.getInt("chance") : 0;
            int weight = section.isSet("weight") ? section.getInt("weight") : 0;
            CCommand.Executor executor = CCommand.Executor.valueOf(section.getString("executor"));
            if (isSplash){
                this.splashCommandList.add(new CCommand(Integer.parseInt(order), section.getStringList("commands"), chance, weight, executor));
            }else {
                this.drinkingCommandList.add(new CCommand(Integer.parseInt(order), section.getStringList("commands"), chance, weight, executor));
            }
        }
    }

    public MigrationStatus migrateOverhaul() {
        FileConfiguration cf = ConfigManager.get("potions.yml");
        String shortcut = "potions."+id+".commands." + (isSplash ? "splashCommands" : "drinkingCommands");
        // does it need migration?
        if (!cf.isList(shortcut)){
            return MigrationStatus.NOT_NEEDED;
        }
        List<String> commandList = cf.getStringList(shortcut);
        // remove old list
        ConfigManager.remove("potions.yml", shortcut);
        int order = 1;
        for (String command : commandList){
            AetherPotions.instance.getLogger().info("Migrating command: "+command);
            CCommand cCommand = new CCommand();
            cCommand.setCommand(Collections.singletonList(command));
            cCommand.setOrder(order++);
            cCommand.setChance(0);
            cCommand.setWeight(0);
            if (isSplash){
                this.splashCommandList.add(cCommand);
            }else {
                this.drinkingCommandList.add(cCommand);
            }
            ConfigManager.set("potions.yml", shortcut+"."+cCommand.getOrder()+".commands", List.of(command));
            ConfigManager.set("potions.yml", shortcut+"."+cCommand.getOrder()+".chance", 0);
            ConfigManager.set("potions.yml", shortcut+"."+cCommand.getOrder()+".weight", 0);
            ConfigManager.set("potions.yml", shortcut+"."+cCommand.getOrder()+".executor", CCommand.Executor.CONSOLE.toString());
        }
        ConfigManager.save("potions.yml");
        ConfigManager.reload("potions.yml");
        return MigrationStatus.MIGRATED;
    }


    /**
     * Draw a command list from the raw list
     * Fixed commands first, then chance, then weight
     * Chance is calculated by the chance value of the command. List can be empty.
     * Weight is calculated by the weight value of the command. At LEAST ONE will be selected.
     * @param rawList Raw list of commands. Splash, Drinking, AfterEffect
     * @return Calculated list of commands to be executed.
     */
    public List<CCommand> drawCommandList(List<CCommand> rawList) {
        Probability probability = new Probability();
        List<CCommand> drawList = new ArrayList<>();
        List<CCommand> chanceList = new ArrayList<>();
        List<CCommand> weightList = new ArrayList<>();

        for (CCommand command : rawList) {
            if (command.isFixed()){
                drawList.add(command);
            }else if (command.isChance()) {
                chanceList.add(command);
            }else if (command.isWeight()) {
                weightList.add(command);
            }
        }

        //chance calculation
        for (CCommand command : chanceList) {
            probability.addChance(command, command.getChance());
        }
        CCommand picked = (CCommand) probability.getRandomElement(true);
        if (picked != null) {
            drawList.add(picked);
        }
        probability = new Probability();

        //weight calculation
        if (weightList.isEmpty()){
            return drawList;
        }
        for (CCommand command : weightList) {
            probability.addChance(command, command.getWeight());
        }

        picked = (CCommand) probability.getRandomElement(false);
        drawList.add(picked);
        return drawList;
    }

    public enum MigrationStatus {
        MIGRATED,
        NOT_MIGRATED,
        NOT_NEEDED,
        FAILED,
    }
    @Data
    public static class AfterEffect{
        private boolean enabled;
        private int time;
        private List<CCommand> commandList = new ArrayList<>();
        String id;
        public AfterEffect(String id)  {
            this.id = id;
            FileConfiguration cf = ConfigManager.get("potions.yml");
            String shortcut = "potions."+id+".commands.afterEffect.";
            this.enabled = cf.getBoolean(shortcut + "enabled");
            this.time = cf.getInt(shortcut + "time");
            // check migration
            if (cf.isList(shortcut+"commands")){
                return;
            }
            for (String order : cf.getConfigurationSection(shortcut+"commands").getKeys(false)){
                ConfigurationSection section = cf.getConfigurationSection(shortcut+"commands."+order);
                assert section != null;
                int chance = section.isSet("chance") ? section.getInt("chance") : 0;
                int weight = section.isSet("weight") ? section.getInt("weight") : 0;
                CCommand.Executor executor = CCommand.Executor.valueOf(section.getString("executor", CCommand.Executor.CONSOLE.toString()));
                this.commandList.add(new CCommand(Integer.parseInt(order), section.getStringList("commands"), chance, weight, executor));
            }
        }

        public MigrationStatus migrateOverhaul(){
            FileConfiguration cf = ConfigManager.get("potions.yml");
            String shortcut = "potions."+id+".commands.afterEffect.commands";
            if (!cf.isList(shortcut)){
                return MigrationStatus.NOT_NEEDED;
            }
            List<String> commandList = cf.getStringList(shortcut);
            ConfigManager.remove("potions.yml", shortcut);
            int order = 1;
            for (String command : commandList){
                CCommand cCommand = new CCommand();
                cCommand.setCommand(Collections.singletonList(command));
                cCommand.setOrder(order++);
                cCommand.setChance(0);
                cCommand.setWeight(0);
                this.commandList.add(cCommand);
                ConfigManager.set("potions.yml", shortcut+"."+cCommand.getOrder()+".commands", List.of(command));
                ConfigManager.set("potions.yml", shortcut+"."+cCommand.getOrder()+".chance", 0);
                ConfigManager.set("potions.yml", shortcut+"."+cCommand.getOrder()+".weight", 0);
                ConfigManager.set("potions.yml", shortcut+"."+cCommand.getOrder()+".executor", CCommand.Executor.CONSOLE.toString());
            }
            ConfigManager.save("potions.yml");
            ConfigManager.reload("potions.yml");
            return MigrationStatus.MIGRATED;
        }
    }
}
