package com.kardasland.aetherpotions.events;

import com.kardasland.aetherpotions.AetherPotions;
import com.kardasland.aetherpotions.api.events.AetherPotionDrinkEvent;
import com.kardasland.aetherpotions.potion.CustomPotion;
import com.kardasland.aetherpotions.potion.PotionValidation;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.Objects;

public class DrinkEvent implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void check(PlayerItemConsumeEvent event){
        if(event.getItem().hasItemMeta() && event.getItem().getItemMeta() instanceof PotionMeta){
            if (AetherPotions.instance.getNbtHandler().contains(event.getItem(), "potionid")) {
                String id = AetherPotions.instance.getNbtHandler().getString(event.getItem(), "potionid");
                PotionValidation potionValidation = new PotionValidation(id);
                if (potionValidation.isExists() && potionValidation.isValid() ){
                    CustomPotion customPotion = new CustomPotion(id);
                    event.setCancelled(true);
                    AetherPotionDrinkEvent drinkEvent = new AetherPotionDrinkEvent(customPotion, event.getPlayer());
                    Bukkit.getPluginManager().callEvent(drinkEvent);
                    if (drinkEvent.isCancelled()){
                        return;
                    }
                    customPotion.apply(event.getPlayer(), event);
                }
            }
        }
    }
    /**
     * This method is used to check if the player is right-clicking with a potion item.
     * Not sure if this is the best way to do this, maybe it can bypass some cheating checks idk.
     * @param event The PlayerInteractEvent
     */
    @EventHandler
    public void check(PlayerInteractEvent event){
        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)){
            return;
        }
        if (!(event.getItem() != null && event.getItem().hasItemMeta() && event.getItem().getItemMeta() instanceof PotionMeta)){
            return;
        }
        if (!AetherPotions.instance.getNbtHandler().contains(event.getItem(), "potionid")){
            return;
        }
        String id = AetherPotions.instance.getNbtHandler().getString(event.getItem(), "potionid");
        PotionValidation potionValidation = new PotionValidation(id);
        if (potionValidation.isExists() && potionValidation.isValid()){
            CustomPotion customPotion = new CustomPotion(id);
            if (customPotion.isSplash()){
                return;
            }
            if (customPotion.isInstantDrink()){
                AetherPotionDrinkEvent drinkEvent = new AetherPotionDrinkEvent(customPotion, event.getPlayer(), true);
                Bukkit.getPluginManager().callEvent(drinkEvent);
                if (drinkEvent.isCancelled()){
                    return;
                }
                customPotion.apply(event.getPlayer(), event);
            }
        }
    }
}
