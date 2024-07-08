package com.kardasland.aetherpotions.commands;

import com.kardasland.aetherpotions.AetherPotions;
import com.kardasland.aetherpotions.potion.CustomPotion;
import com.kardasland.aetherpotions.potion.CustomPotionItem;
import com.kardasland.aetherpotions.potion.PotionValidation;
import com.kardasland.aetherpotions.utility.ConfigManager;
import com.kardasland.aetherpotions.utility.Misc;
import io.github.bananapuncher714.nbteditor.NBTEditor;
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

    public MainCommand() {
        initHelpList();
    }

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
            case "give": {
                if (Misc.checkPerm(player, "aetherpotions.give")){
                    try {

                        // how did i forget this basic error checking
                        // /aetherpotions give <player> <potion> [amount]
                        if (args.length < 3){
                            Misc.send(player, "Please enter a player name and potion id.", true);
                            return true;
                        }
                        int amount = args.length == 3 ? 1 : Integer.parseInt(args[3]);
                        givePotion(player, args[1], args[2], amount);
                    }catch (NumberFormatException exception){
                        Misc.send(player, "Amount parameter is not a number.", true);
                    }
                }
                break;
            }
            case "list": {
                if (Misc.checkPerm(player, "aetherpotions.list")){
                    list(player);
                }
                break;
            }
            case "info": {
                if (Misc.checkPerm(player, "aetherpotions.info")){
                    if (args.length <= 1){
                        Misc.send(player, "Please enter a potion id.", true);
                        return true;
                    }
                    // /aetherpotions info <id> [detailed]
                    String id = args[1];
                    boolean detailed = args.length == 3 && Boolean.parseBoolean(args[2]);
                    info(player, id, detailed);
                }
                break;
            }
            case "reload":{
                if (Misc.checkPerm(player, "aetherpotions.reload")){
                    Misc.send(player, AetherPotions.instance.reloadPlugin() ? "Reloaded successfully!" : "Some issue occured, please check the console.", true);
                }
                break;
            }
            default: {
                if (Misc.checkPerm(player, "aetherpotions.help")){
                    helpScreen(player);
                }
                break;
            }
        }
        return true;
    }

    private void info(Player player, String id, boolean detailed){
        PotionValidation potionValidation = new PotionValidation(id);
        if (potionValidation.isExists()){
            CustomPotion customPotion = new CustomPotion(id);
            Misc.send(player, "&bPotion Info &7- &b" + id, false);
            Misc.send(player, " ", false);
            if (!potionValidation.isValid()){
                StringBuilder sm = new StringBuilder();
                for (PotionValidation.Errors errors : potionValidation.getPotionErrors()){
                    sm.append(errors.toString()).append(", ");
                }
                Misc.send(player, "&bThere are &cerrors! &bThere are some missing/invalid configurations: &c" + sm.substring(0, sm.length() - 2) + "&7.", false);
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
                Misc.send(player, "&bAfterEffect commands list: &7" + customPotion.getCommandList().getAfterEffect().getCommands().toString(), false);
            }

        }else {
            Misc.send(player, "&cThis id does not exist.", true);
        }
    }

    private boolean givePotion(Player player, String targetName, String id, int amount){
        Player target = Bukkit.getPlayer(targetName);
        if (target != null && target.isOnline()){
            CustomPotionItem potionItem = new CustomPotionItem(id, true);
            PotionValidation potionValidation = new PotionValidation(id);
            if (!(potionValidation.isExists() && potionValidation.isValid())){
                Misc.send(player, "&cThis potion does not exist.", true);
                return false;
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
            return true;
        }else {
            Misc.send(player, "&cThis player is not online.", true);
            return false;
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
        for (String message : helpList){
            Misc.send(player, message, false);
        }
    }

    private void initHelpList() {
        this.helpList = new ArrayList<>();
        this.helpList.add("&bAetherPotions &f(&7" + AetherPotions.instance.getDescription().getVersion() + "&f) &7- &bKardasLand");
        this.helpList.add(" ");
        this.helpList.add("&b/aetherpotions give <player> <potion> [amount]");
        this.helpList.add("&7> Gives a certain amount of potion to a player.");
        this.helpList.add("&b/aetherpotions info <potion> [detailed]");
        this.helpList.add("&7> Gives brief info about the potion. You can add true after potion id if you want a detailed info.");
        this.helpList.add("&b/aetherpotions reload");
        this.helpList.add("&7> Reloads the whole plugin.");
        this.helpList.add("&b/aetherpotions list");
        this.helpList.add("&7> Gives the list of potions.");
    }
}
