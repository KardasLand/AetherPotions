package com.kardasland.aetherpotions.commands;

import com.kardasland.aetherpotions.AetherPotions;
import com.kardasland.aetherpotions.potion.CustomPotion;
import com.kardasland.aetherpotions.potion.CustomPotionItem;
import com.kardasland.aetherpotions.potion.PotionValidation;
import com.kardasland.aetherpotions.utility.ConfigManager;
import com.kardasland.aetherpotions.utility.Misc;
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

public class AetherCommand implements CommandExecutor {



    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player player = sender instanceof Player ? (Player) sender : null;
        if (args.length == 0){
            if (Misc.checkPerm(player, "aetherpotions.help")){
                helpScreen(player);
            }
            return true;
        }
        switch (args[0]) {
            case "give" -> {
                if (Misc.checkPerm(player, "aetherpotions.give")) {
                    try {
                        // how did i forget this basic error checking
                        // /aetherpotions give <player> <potion> [amount]
                        if (args.length < 3) {
                            Misc.send(player, "Please enter a player name and potion id.", true);
                            return true;
                        }
                        int amount = args.length == 3 ? 1 : Integer.parseInt(args[3]);
                        givePotion(player, args[1], args[2], amount);
                    } catch (NumberFormatException exception) {
                        Misc.send(player, "Amount parameter is not a number.", true);
                    }
                }
            }
            case "list" -> {
                if (Misc.checkPerm(player, "aetherpotions.list")) {
                    list(player);
                }
            }
            case "migrate" -> {
                if (Misc.checkPerm(player, "aetherpotions.migrate")) {
                    // /aetherpotions migrate <id/all>
                    if (args.length == 1) {
                        Misc.send(player, "Please enter a potion id or all.", true);
                        return true;
                    }
                    migration(player, args);
                }
            }
            case "info" -> {
                if (Misc.checkPerm(player, "aetherpotions.info")) {
                    if (args.length <= 1) {
                        Misc.send(player, "Please enter a potion id.", true);
                        return true;
                    }
                    // /aetherpotions info <id> [detailed]
                    String id = args[1];
                    boolean detailed = args.length == 3 && Boolean.parseBoolean(args[2]);
                    info(player, id, detailed);
                }
            }
            case "reload" -> {
                if (Misc.checkPerm(player, "aetherpotions.reload")) {
                    Misc.send(player, AetherPotions.instance.reloadPlugin() ? "Reloaded successfully!" : "Some issue occured, please check the console.", true);
                }
            }
            default -> {
                if (Misc.checkPerm(player, "aetherpotions.help")) {
                    helpScreen(player);
                }
            }
        }
        return true;
    }





    private void migration(Player player, String[] args) {
        if (args[1].equalsIgnoreCase("all")){
            for (String id : Objects.requireNonNull(ConfigManager.get("potions.yml")).getConfigurationSection("potions.").getKeys(false)){
                CustomPotion customPotion = new CustomPotion(id, false, true);
                customPotion.migratePotion();
            }
            Misc.send(player, "All potions migrated successfully.", true);
        }else {
            CustomPotion customPotion = new CustomPotion(args[1], false, true);
            if (customPotion.migratePotion()){
                Misc.send(player, "Potion migrated successfully.", true);
            }else {
                Misc.send(player, "Potion could not be migrated. Manual fix may be required.", true);
            }
        }
    }


    // Make the following methods GUI based, getting too complicated now.
    private void info(Player player, String id, boolean detailed){
        PotionValidation potionValidation = new PotionValidation(id);
        if (potionValidation.isExists()){
            CustomPotion customPotion = new CustomPotion(id);
            Misc.send(player, "&bPotion Info &7- &b" + id, false);
            Misc.send(player, " ", false);
            if (!potionValidation.isValid()){
                Misc.send(player, "&cThere are errors while building the potion. Full List:", true);
                for (PotionValidation.Errors errors : potionValidation.getPotionErrors()){
                    Misc.send(player, "&b" + errors.toString() + ": &7" + errors.getMessage(), true);
                }
                return;
            }
            Misc.send(player, "&bDisplay name: &7" + customPotion.getDisplayName(), false);
            if (detailed)
                Misc.send(player, "&bLore: &7" + customPotion.getLore().toString(), false);
            Misc.send(player, "&bIs it splash?: " + (customPotion.isSplash() ? "&atrue": "&cfalse"), false);
            Misc.send(player, "&bDelete bottle? " + (customPotion.isDeleteBottle() ? "&atrue" : "&cfalse"), false);
            Misc.send(player, "&bPotion type &7: " + customPotion.getData().getType().getEffectType().getName(), false);
            Misc.send(player, "&bParticle enabled: " + (customPotion.getParticle().isEnabled() ? "&atrue" : "&cfalse"), false);
            if (customPotion.getParticle().isEnabled()){
                Misc.send(player, "&bParticle type: &7" + customPotion.getParticle().getType(), false);
                if (detailed){
                    Misc.send(player, "&bParticle time: &7" + customPotion.getParticle().getTime() + " second(s)", false);
                    Misc.send(player, "&bParticle amount: &7" + customPotion.getParticle().getAmount(), false);
                }
            }
            if (customPotion.isSplash() && detailed){
                Misc.send(player, "&bSplash command list: &7" + customPotion.getCommandList().getSplashCommandList().toString(), false);
            }
            if (!customPotion.isSplash() && detailed){
                Misc.send(player, "&bDrinking command list: &7" + customPotion.getCommandList().getDrinkingCommandList().toString(), false);
            }
            Misc.send(player, "&bAfterEffect enabled: " + (customPotion.getCommandList().getAfterEffect().isEnabled() ? "&atrue" : "&cfalse"), false);
            if (customPotion.getCommandList().getAfterEffect().isEnabled() && detailed){
                Misc.send(player, "&bAfterEffect commands list: &7" + customPotion.getCommandList().getAfterEffect().getCommandList().toString(), false);
            }

        }else {
            Misc.send(player, "&cThis id does not exist.", true);
        }
    }

    private void givePotion(Player player, String targetName, String id, int amount){
        Player target = Bukkit.getPlayer(targetName);
        if (target != null && target.isOnline()){
            CustomPotionItem potionItem = new CustomPotionItem(id, true);
            PotionValidation potionValidation = new PotionValidation(id);
            if (!(potionValidation.isExists() && potionValidation.isValid())){
                Misc.send(player, "&cThis potion does not exist.", true);
                return;
            }
            ItemStack potion = potionItem.build(player);
            potion = AetherPotions.instance.getNbtHandler().set(potion, id, "potionid");
            for (int i = 0; i < amount; i++){
                if (target.getInventory().firstEmpty() == -1){
                    Objects.requireNonNull(target.getLocation().getWorld()).dropItemNaturally(target.getLocation(), potion);
                }else {
                    target.getInventory().addItem(potion);
                }
            }
        }else {
            Misc.send(player, "&cThis player is not online.", true);
        }
    }

    private void list(Player player){
        StringBuilder sm = new StringBuilder();
        sm.append("&bAvailable potions: &7" );
        for (String id : Objects.requireNonNull(ConfigManager.get("potions.yml")).getConfigurationSection("potions.").getKeys(false)){
            PotionValidation potionValidation = new PotionValidation(id);
            sm.append(Misc.color(
                    (potionValidation.isValid() ? "&a" : "&c") +
                            id + "&7, "));
        }
        String finalPotionList = sm.toString();
        Misc.send(player, finalPotionList.substring(0, finalPotionList.length() - 2) + ".", false);
    }

    private void helpScreen(Player player){
        ArrayList<String> helpList = new ArrayList<>();
       helpList.add("&bAetherPotions &f(&7" + AetherPotions.instance.getDescription().getVersion() + "&f) &7- &bKardasLand");
       helpList.add(" ");
       if (Misc.checkPerm(player, "aetherpotions.give", true)) {
           helpList.add("&b/aetherpotions give <player> <potion> [amount]");
           helpList.add("&7> Gives a certain amount of potion to a player.");
       }
       if (Misc.checkPerm(player, "aetherpotions.info", true)) {
           helpList.add("&b/aetherpotions info <potion> [detailed]");
           helpList.add("&7> Gives brief info about the potion. You can add true after potion id if you want a detailed info.");
       }
       if (Misc.checkPerm(player, "aetherpotions.reload", true)) {
           helpList.add("&b/aetherpotions reload");
           helpList.add("&7> Reloads the whole plugin.");
       }
       if (Misc.checkPerm(player, "aetherpotions.list", true)) {
           helpList.add("&b/aetherpotions list");
           helpList.add("&7> Gives the list of potions.");
       }
       if (Misc.checkPerm(player, "aetherpotions.migrate", true)) {
           helpList.add("&b/aetherpotions migrate <id/all>");
           helpList.add("&7> Migrates the potion to the new system.");
       }
       for (String s : helpList) {
           Misc.send(player, s, false);
       }

    }
}
