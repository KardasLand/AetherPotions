package com.kardasland.aetherpotions.commands;

import com.kardasland.aetherpotions.AetherPotions;
import com.kardasland.aetherpotions.potion.Potion;
import com.kardasland.aetherpotions.potion.PotionItem;
import com.kardasland.aetherpotions.utils.Araclar;
import com.kardasland.aetherpotions.utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MainCommand implements CommandExecutor {
    Araclar araclar = new Araclar();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player p = ((Player) sender);
            if (args.length == 1){
                if (args[0].equals("reload")){
                    if (p.hasPermission("aetherpotions.reload")){
                        a();
                        araclar.prefix(p, "Reloaded successfully!");
                    }else {
                        araclar.noPerms(p, "aetherpotions.reload");
                    }
                }else if (args[0].equals("list")){
                    if (p.hasPermission("aetherpotions.list")){
                        StringBuilder sm = new StringBuilder();
                        for (String key : ConfigManager.get("config.yml").getConfigurationSection("Potions.").getKeys(false)){
                            sm.append(key).append(", ");
                        }
                        String list = sm.substring(0, sm.length() - 2);
                        araclar.prefix(p, "PotionID list: " + list + ".");
                    }else {
                        araclar.noPerms(p, "aetherpotions.reload");
                    }
                }else {
                    helpScreen(p);
                }
            }else if (args.length == 3){
                if (args[0].equals("give")){
                    if (p.hasPermission("aetherpotions.give")){
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target != null && target.isOnline()){
                            Potion potion = new Potion(args[2]);
                            if (potion.getPotionType() != null){
                                PotionItem potionItem = new PotionItem(potion.getPotionid());
                                ItemStack s = AetherPotions.instance.getSupport().potionAsItemStack(potionItem, potion);
                                if (target.getInventory().firstEmpty() == -1){
                                    target.getWorld().dropItem(target.getLocation(), s);
                                    araclar.prefix(p, "Successfully gave " + target.getName() + " " + potion.getPotionid() + " potion!");
                                }else {
                                    target.getInventory().addItem(s);
                                    araclar.prefix(p, "Successfully gave " + target.getName() + " " + potion.getPotionid() + " potion!");
                                }
                            }
                        }
                    }else {
                        araclar.noPerms(p, "aetherpotions.give");
                    }
                }else {
                    helpScreen(p);
                }
            }else {
                defaultScreen(p);
            }
        }else {
            if (args.length == 1){
                if (args[0].equals("reload")){
                   a();
                        araclar.prefix("Reloaded successfully!");
                }else if (args[0].equals("list")){
                   StringBuilder sm = new StringBuilder();
                   for (String key : ConfigManager.get("config.yml").getConfigurationSection("Potions.").getKeys(false)){
                       sm.append(key).append(", ");
                   }
                   String list = sm.substring(0, sm.length() - 2);
                   araclar.prefix("PotionID list: " + list + ".");
                }else {
                    helpScreen();
                }
            }else if (args.length == 3){
                if (args[0].equals("give")){
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null && target.isOnline()){
                        Potion potion = new Potion(args[2]);
                        if (potion.getPotionType() != null){
                            PotionItem potionItem = new PotionItem(potion.getPotionid());
                            ItemStack s = AetherPotions.instance.getSupport().potionAsItemStack(potionItem, potion);
                            if (target.getInventory().firstEmpty() == -1){
                                target.getWorld().dropItem(target.getLocation(), s);
                                araclar.prefix("Successfully gave " + target.getName() + " " + potion.getPotionid() + " potion!");
                            }else {
                                target.getInventory().addItem(s);
                                araclar.prefix("Successfully gave " + target.getName() + " " + potion.getPotionid() + " potion!");
                            }
                        }
                    }
                }else {
                    helpScreen();
                }
            } else {
                defaultScreen();
            }
        }
        return true;
    }
    public void a(){
        ConfigManager.reload("config.yml");
        ConfigManager.reload("messages.yml");
    }
    public void defaultScreen(){
        araclar.nonPrefix("&bAetherPotions &7(&f"+ AetherPotions.instance.getDescription().getVersion()+"&7)");
        araclar.nonPrefix("&7Author: &bKardasLand");
        araclar.nonPrefix("&7Help: &b/aetherpotions &fhelp");
    }
    public void defaultScreen(Player p){
        araclar.nonPrefix(p,"&bAetherPotions &7(&f"+ AetherPotions.instance.getDescription().getVersion()+"&7)");
        araclar.nonPrefix(p,"&7Author: &bKardasLand");
        araclar.nonPrefix(p,"&7Help: &b/aetherpotions &fhelp");
    }

    public void helpScreen(Player p){
        if (!p.hasPermission("aetherpotions.admin")){
            defaultScreen(p);
        }else{
            araclar.nonPrefix(p,"&bAetherPotions &7(&f"+ AetherPotions.instance.getDescription().getVersion()+"&7)");
            araclar.nonPrefix(p," ");
            araclar.nonPrefix(p,"&b/aetherpotions reload");
            araclar.nonPrefix(p,"&7> Reloads plugin.");
            araclar.nonPrefix(p,"&b/aetherpotions list");
            araclar.nonPrefix(p,"&7> Lists the potions.");
            araclar.nonPrefix(p,"&b/aetherpotions give <player> <potionid>");
            araclar.nonPrefix(p,"&7> Gives player a potion.");
        }
    }

    public void helpScreen(){
        araclar.nonPrefix("&bAetherPotions &7(&f"+ AetherPotions.instance.getDescription().getVersion()+"&7)");
        araclar.nonPrefix(" ");
        araclar.nonPrefix("&b/aetherpotions reload");
        araclar.nonPrefix("&7> Reloads plugin.");
        araclar.nonPrefix("&b/aetherpotions list");
        araclar.nonPrefix("&7> Lists the potions.");
        araclar.nonPrefix("&b/aetherpotions give <player> <potionid>");
        araclar.nonPrefix("&7> Gives player a potion.");

    }
}
