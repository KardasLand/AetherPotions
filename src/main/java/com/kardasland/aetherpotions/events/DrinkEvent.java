package com.kardasland.aetherpotions.events;

import com.kardasland.aetherpotions.potion.CustomPotion;
import com.kardasland.aetherpotions.potion.PotionValidation;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.meta.PotionMeta;

public class DrinkEvent implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void check(PlayerItemConsumeEvent event){
        if(event.getItem().hasItemMeta() && event.getItem().getItemMeta() instanceof PotionMeta){
            if (NBTEditor.contains(event.getItem(), "potionid")) {
                String id = NBTEditor.getString(event.getItem(), "potionid");
                PotionValidation potionValidation = new PotionValidation(id);
                if (potionValidation.isExists() && potionValidation.isValid() ){
                    CustomPotion customPotion = new CustomPotion(id);
                    event.setCancelled(!customPotion.isOriginalEffect());
                    customPotion.apply(event.getPlayer(), event);
                }
            }
        }
    }
}
