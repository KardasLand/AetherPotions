package com.kardasland.snowpotions.events;

import com.kardasland.snowpotions.AetherPotions;
import com.kardasland.snowpotions.potion.CustomPotion;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Objects;

public class SplashEvent implements Listener {
    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (event.getEntity().getShooter() instanceof Player) {
            if (event.getEntity() instanceof ThrownPotion) {
                Player player = (Player) event.getEntity().getShooter();
                ItemStack item = player.getInventory().getItemInMainHand();
                if (item.getItemMeta() != null && NBTEditor.contains(item, "potionid")){
                    event.getEntity().setMetadata("aetherpotion", new FixedMetadataValue(AetherPotions.instance, NBTEditor.getString(item, "potionid")));
                }
            }
        }
    }

    @EventHandler
    public void splash(PotionSplashEvent event) {
        if (event.getPotion().hasMetadata("aetherpotion")) {
            String id = String.valueOf(event.getPotion().getMetadata("aetherpotion").get(0).value());
            CustomPotion potion = new CustomPotion(id);
            for (Entity e : event.getAffectedEntities()){
                if (e instanceof Player){
                    // we can safely do null because it cannot and will not use the event cause of the splash feature
                    potion.apply(Objects.requireNonNull(((Player) e).getPlayer()), null);
                }
            }
        }
    }
}
