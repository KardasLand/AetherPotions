package com.kardasland.snowpotions.commands;

import com.kardasland.snowpotions.potion.CustomPotionItem;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MainCommand implements CommandExecutor {

    //snowpotions give kardasland kumar 5
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            switch (args[0]){
                case "give":{
                    String targetName = args[1];
                    Player target = Bukkit.getPlayer(targetName);
                    if (target != null && target.isOnline()){
                        CustomPotionItem potionItem = new CustomPotionItem(args[2]);
                        if (potionItem.getCustomPotion().getId().isEmpty() || potionItem.getCustomPotion().getId().isBlank()){
                            // POTION ID NOT FOUND
                            return true;
                        }
                        ItemStack potion = potionItem.build();
                        int amount = Integer.parseInt(args[3]);
                        for (int i = 0; i < amount; i++){
                            if (target.getInventory().firstEmpty() == -1){
                                Objects.requireNonNull(target.getLocation().getWorld()).dropItemNaturally(target.getLocation(), potion);
                            }else {
                                target.getInventory().addItem(potion);
                            }
                        }
                        break;
                    }
                    break;
                }
                default:{
                    helpScreen();
                    break;
                }
            }
        }
        return true;
    }

    private void helpScreen() {

    }
}
