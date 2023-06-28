package com.kardasland.aetherpotions.events;

import com.kardasland.aetherpotions.AetherPotions;
import com.kardasland.aetherpotions.potion.CustomPotion;
import com.kardasland.aetherpotions.potion.CustomPotionItem;
import com.kardasland.aetherpotions.utility.ConfigManager;
import com.kardasland.aetherpotions.utility.Misc;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Objects;

public class SplashEvent implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (event.getEntity().getShooter() instanceof Player) {
            if (event.getEntity() instanceof ThrownPotion) {
                Player player = (Player) event.getEntity().getShooter();
                ItemStack item = player.getInventory().getItemInMainHand();
                if (item.getItemMeta() != null && NBTEditor.contains(item, "potionid")){
                    if (isAllowed((Player) event.getEntity().getShooter(), event.getLocation(), true)){
                        event.getEntity().setMetadata("aetherpotion", new FixedMetadataValue(AetherPotions.instance, NBTEditor.getString(item, "potionid")));
                    }else {
                        Misc.send((Player) event.getEntity().getShooter(), ConfigManager.get("messages.yml").getString("NotAllowedToThrow"), true);
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void splash(PotionSplashEvent event) {
        if (event.getPotion().hasMetadata("aetherpotion") && event.getEntity().getShooter() instanceof Player) {
            String id = String.valueOf(event.getPotion().getMetadata("aetherpotion").get(0).value());
            event.setCancelled(true);
            CustomPotion customPotion = new CustomPotion(id);
            boolean oneTime = true;
            for (Entity e : event.getAffectedEntities()){
                if (e instanceof Player){
                    if (isAllowed(((Player) e).getPlayer(), event.getEntity().getLocation(), true)){
                        // we can safely do null because it cannot and will not use the event cause of the splash feature
                        customPotion.apply(Objects.requireNonNull(((Player) e).getPlayer()), null);
                    }else if (oneTime){
                        // This is a joke
                        oneTime = false;
                        Player shooter = (Player) event.getEntity().getShooter();
                        Misc.send(shooter, ConfigManager.get("messages.yml").getString("NotAllowedToThrow"), true);

                        CustomPotionItem potionItem = new CustomPotionItem(customPotion);
                        ItemStack potion = potionItem.build();
                        potion = NBTEditor.set(potion, id, "potionid");
                        if (shooter.getInventory().firstEmpty() == -1){
                            Objects.requireNonNull(shooter.getLocation().getWorld()).dropItemNaturally(shooter.getLocation(), potion);
                        }else {
                            shooter.getInventory().addItem(potion);
                        }
                    }
                }
            }
        }
    }

    public boolean isAllowed(Player player, Location location, boolean strict){
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        if (strict){
            return query.testState(BukkitAdapter.adapt(location), localPlayer, Flags.POTION_SPLASH);
        }else {
            return !Objects.equals(query.queryState(BukkitAdapter.adapt(location), localPlayer, Flags.POTION_SPLASH), StateFlag.State.DENY);
        }
    }
}
