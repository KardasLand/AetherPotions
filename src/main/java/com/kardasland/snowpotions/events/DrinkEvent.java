package com.kardasland.snowpotions.events;

import com.kardasland.snowpotions.potion.CustomPotion;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.meta.PotionMeta;

public class DrinkEvent implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void check(PlayerItemConsumeEvent event){
        if(event.getItem().hasItemMeta() && event.getItem().getItemMeta() instanceof PotionMeta){
                if (NBTEditor.contains(event.getItem(), "potionid")) {
                    Player p = event.getPlayer();
                    String id = NBTEditor.getString(event.getItem(), "potionid");
                    CustomPotion potion = new CustomPotion(id);
                    if (potion.getData() != null){
                        event.setCancelled(true);
                        potion.apply(p, event);
                    }
                }
        }
    }
}
