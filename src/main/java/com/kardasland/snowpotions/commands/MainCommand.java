package com.kardasland.snowpotions.commands;

import com.kardasland.snowpotions.AetherPotions;
import com.kardasland.snowpotions.potion.CustomPotion;
import com.kardasland.snowpotions.potion.CustomPotionItem;
import com.kardasland.snowpotions.potion.PotionValidation;
import com.kardasland.snowpotions.utility.ConfigManager;
import com.kardasland.snowpotions.utility.Misc;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainCommand implements CommandExecutor {

    List<String> helpList;

     /*

    AetherPotions (v0.123) - KardasLand

    &b/aetherpotions give <player> <potion> <number>
    &7> Gives a certain amount of potion to a player.
    &b/aetherpotions reload
    &7> Reloads the whole plugin.
    &b/aetherpotions list
    &7 Gives the list of potions.
     */


    public MainCommand() {
        this.helpList = new ArrayList<>();
        this.helpList.add("&bAetherPotions &f(&7" + AetherPotions.instance.getDescription().getVersion() + "&b) - KardasLand");
        this.helpList.add("&b/aetherpotions give <player> <potion> <amount>");
        this.helpList.add("&7> Gives a certain amount of potion to a player.");
        this.helpList.add("&b/aetherpotions reload");
        this.helpList.add("&7> Reloads the whole plugin.");
        this.helpList.add("&b/aetherpotions list");
        this.helpList.add("&7> Gives the list of potions.");
    }


    //snowpotions give kardasland kumar 5
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player player = sender instanceof Player ? (Player) sender : null;
        switch (args[0]) {
            case "give": {
                int amount = Integer.parseInt(args[3]);
                Misc.send(player, givePotion(args[1], args[2], amount) ? "Success." : "Failed, check the console.", true);
                break;
            }
            case "list": {
                list(player);
                break;
            }
            case "info": {
                String id = args[1];
                boolean detailed = args[2] != null && Boolean.parseBoolean(args[2]);
                info(player, id, detailed);
                break;
            }
            case "reload":{

                Misc.send(player, AetherPotions.instance.reloadPlugin() ? "Reloaded successfully!" : "Some issue occured, please check the console.", true);
                break;
            }
            default: {
                helpScreen(player);
                break;
            }
        }
        return true;
    }

    private void info(Player player, String id, boolean detailed){
        PotionValidation potionValidation = new PotionValidation(id);
        if (potionValidation.isExists()){
            CustomPotion customPotion = new CustomPotion(id);
            Misc.send(player, "Potion Info - " + id, false);
            Misc.send(player, " ", false);
            if (!potionValidation.isValid()){
                StringBuilder sm = new StringBuilder();
                for (PotionValidation.Errors errors : potionValidation.getPotionErrors()){
                    sm.append(errors).append(", ");
                }
                String finalString = sm.substring(0, 2)  + ".";
                Misc.send(player, "&bThere are &cerrors! &bThere are some missing/invalid configurations: &c" + finalString, false);
            }
            Misc.send(player, "&bDisplay name: " + customPotion.getDisplayName(), false);
            if (detailed)
                Misc.send(player, "&bLore: " + customPotion.getLore().toString(), false);
            Misc.send(player, "&bIs it splash?: " + customPotion.isSplash(), false);
            Misc.send(player, "&bDelete bottle? " + customPotion.isDeleteBottle(), false);
            Misc.send(player, "&bPotion type: " + customPotion.getType(), false);
            Misc.send(player, "&bParticle enabled: " + customPotion.getParticle().isEnabled(), false);
            if (customPotion.getParticle().isEnabled()){
                Misc.send(player, "&bParticle type: " + customPotion.getParticle().getType(), false);
                if (detailed){
                    Misc.send(player, "&bParticle time: " + customPotion.getParticle().getTime(), false);
                    Misc.send(player, "&bParticle amount: " + customPotion.getParticle().getAmount(), false);
                }
            }
            if (customPotion.isSplash() && detailed){
                Misc.send(player, "&bSplash command list: " + customPotion.getCommandList().getSplashCommandList().toString(), false);
            }
            if (!customPotion.isSplash() && detailed){
                Misc.send(player, "&b: " + customPotion.getCommandList().toString(), false);
            }
            Misc.send(player, "&bAfterEffect enabled: " + customPotion.getCommandList().getAfterEffect().isEnabled(), false);
            if (detailed){
                Misc.send(player, "&bAfterEffect commands list: " + customPotion.getCommandList().getAfterEffect().getCommands().toString(), false);
            }

        }else {
            Misc.send(player, "This id does not exist.", true);
        }
    }

    private boolean givePotion(String targetName, String id, int amount){
        Player target = Bukkit.getPlayer(targetName);
        if (target != null && target.isOnline()){
            CustomPotionItem potionItem = new CustomPotionItem(id);
            if (potionItem.getCustomPotion().getId().isEmpty() || potionItem.getCustomPotion().getId().isBlank()){
                // POTION ID NOT FOUND
                return false;
            }
            ItemStack potion = potionItem.build();
            for (int i = 0; i < amount; i++){
                if (target.getInventory().firstEmpty() == -1){
                    Objects.requireNonNull(target.getLocation().getWorld()).dropItemNaturally(target.getLocation(), potion);
                }else {
                    target.getInventory().addItem(potion);
                }
            }
            return true;
        }else {
            return false;
        }
    }

    private void list(Player player){
        StringBuilder sm = new StringBuilder();
        sm.append("&bAvailable potions: " );
        for (String id : Objects.requireNonNull(ConfigManager.get("potions.yml")).getConfigurationSection("potions.").getKeys(false)){
            PotionValidation potionValidation = new PotionValidation(id);
            sm.append(Misc.color(
                    (potionValidation.isValid() ? "&a" : "&c") +
                            id + "&7, "));
        }
        String finalPotionList = sm.substring(0, 2) + ".";
        Misc.send(player, finalPotionList, false);
    }

    private void helpScreen(Player player){
        for (String message : helpList){
            Misc.send(player, message, false);
        }
    }
}
