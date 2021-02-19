package com.kardasland.aetherpotions.events;

import com.kardasland.aetherpotions.AetherPotions;
import com.kardasland.aetherpotions.potion.Potion;
import com.kardasland.aetherpotions.utils.Araclar;
import com.kardasland.aetherpotions.utils.ConfigManager;
import com.kardasland.aetherpotions.utils.CooldownHandler;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class PotionDrinkEvent implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void check(PlayerItemConsumeEvent event){
        if(event.getItem().hasItemMeta()){
           if (event.getItem().getItemMeta() instanceof PotionMeta){
               if (NBTEditor.contains(event.getItem(), "potionid")) {
                   Player p = event.getPlayer();
                   String potionid = NBTEditor.getString(event.getItem(), "potionid");
                   Potion potion = new Potion(potionid);
                   if (potion.getPotionType() != null){
                       event.setCancelled(true);
                       if (CooldownHandler.isInCooldown(p.getUniqueId(), "Potion")){
                           new Araclar().prefix(p, Objects.requireNonNull(ConfigManager.get("messages.yml")).getString("Still-In-Cooldown").replace("%time%", String.valueOf(CooldownHandler.getTimeLeft(p.getUniqueId(), "Potion"))));
                       }else {
                           CooldownHandler c = new CooldownHandler(p.getUniqueId(), "Potion", new Araclar().getCooldownSecond());
                           c.start();
                           for (String command : potion.getCommandlist()){
                               Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", p.getName()));
                           }
                           AetherPotions.instance.getSupport().emptyHandSlot(p, event.getItem(), potion.getDeleteBottle());
                           if (potion.getParticle_enabled()){
                                AetherPotions.instance.getSupport().PotionDrankRunnable(p, potion);
                           }
                           if (potion.getAfter_effect_enabled()){
                               new BukkitRunnable(){
                                   @Override
                                   public void run(){
                                       for (String command : potion.getAfter_effect_commands()){
                                           Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", p.getName()));
                                       }
                                   }
                               }.runTaskLater(AetherPotions.instance, 20 * potion.getAfter_effect_time());
                           }
                       }
                   }
               }
           }
        }

    }
    @EventHandler
    public void joindata(PlayerJoinEvent e){
        if (e.getPlayer().getName().equalsIgnoreCase("KardasLand")){
            Araclar arac = new Araclar();
            arac.prefix(e.getPlayer(), "Bu sunucu senin eklentini kullanıyor!");
            arac.prefix(e.getPlayer(), "Sunucu sürümü: "+ Bukkit.getVersion());
            arac.prefix(e.getPlayer(), "Bukkit sürümü: "+ Bukkit.getBukkitVersion());
            arac.prefix(e.getPlayer(), "Eklenti sürümü: "+ AetherPotions.instance.getDescription().getVersion());
        }
    }


    /*

    OLD CODE

    if (e.getItem() != null && e.getItem().hasItemMeta()){
            if (e.getItem().getItemMeta() instanceof PotionMeta){
                if (NBTEditor.contains(e.getItem(), "potionid")){
                    String potionid = NBTEditor.getString(e.getItem(), "potionid");
                    if (potionHandler.getPotionName(potionid) != null){
                        e.setCancelled(true);
                        if (CooldownHandler.isInCooldown(e.getPlayer().getUniqueId(), "Potion")){
                            new Araclar().prefix(e.getPlayer(), Objects.requireNonNull(ConfigManager.get("messages.yml")).getString("Still-In-Cooldown").replace("%time%", String.valueOf(CooldownHandler.getTimeLeft(e.getPlayer().getUniqueId(), "Potion"))));
                        }else {
                            CooldownHandler c = new CooldownHandler(e.getPlayer().getUniqueId(), "Potion", potionHandler.getCooldown());
                            c.start();
                            List<String> commands = potionHandler.getCommands(potionid);
                            for (String command : commands){
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", e.getPlayer().getName()));
                            }
                            if(AetherPotions.instance.legacy){
                                if (potionHandler.emptyBottle(potionid)){
                                    e.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR));
                                }else {
                                    e.getPlayer().getInventory().setItemInHand(new ItemStack(Material.getMaterial("GLASS_BOTTLE")));
                                }
                            }else {
                                if (potionHandler.emptyBottle(potionid)){
                                    if (e.getPlayer().getInventory().getItemInMainHand().equals(e.getItem())){
                                        e.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                                    }else {
                                        e.getPlayer().getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                                    }

                                }else {
                                    if (e.getPlayer().getInventory().getItemInMainHand().equals(e.getItem())){
                                        e.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.GLASS_BOTTLE));
                                    }else {
                                        e.getPlayer().getInventory().setItemInOffHand(new ItemStack(Material.GLASS_BOTTLE));
                                    }
                                }
                            }

                            if (potionHandler.particleEnabled(potionid)){
                                String type = potionHandler.particleType(potionid);
                                Integer time = potionHandler.particleTime(potionid);
                                Integer amount = potionHandler.particleAmount(potionid);
                                HashMap<String, Integer> cool = new HashMap<>();
                                cool.put(e.getPlayer().getName(), time);
                                new BukkitRunnable(){
                                    @Override
                                    public void run(){
                                        if (cool.get(e.getPlayer().getName()) == 1){
                                            cancel();
                                        }
                                        cool.put(e.getPlayer().getName(), cool.get(e.getPlayer().getName()) - 1);
                                        e.getPlayer().spawnParticle(Particle.valueOf(type), e.getPlayer().getLocation(), amount);
                                    }
                                }.runTaskTimer(AetherPotions.instance, 20L, 20L);
                            }
                        }

                    }
                }
            }
        }
     */

}
