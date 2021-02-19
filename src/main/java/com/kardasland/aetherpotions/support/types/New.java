package com.kardasland.aetherpotions.support.types;

import com.kardasland.aetherpotions.AetherPotions;
import com.kardasland.aetherpotions.potion.Potion;
import com.kardasland.aetherpotions.potion.PotionItem;
import com.kardasland.aetherpotions.support.Support;
import com.kardasland.aetherpotions.support.types.runnables.PotionDrankRunnableNew;
import com.kardasland.aetherpotions.utils.Araclar;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;

public class New implements Support {
    @Override
    public ItemStack castPotionToItemStack(Potion potion) {
        ItemStack pot = new ItemStack(Material.POTION, 1);
        PotionMeta meta = (PotionMeta) pot.getItemMeta();
        meta.setBasePotionData(new PotionData(potion.getPotionType()));
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        pot.setItemMeta(meta);
        return pot;
    }
    public ItemStack potionAsItemStack(PotionItem potionItem, Potion potion){
        ItemStack pot = castPotionToItemStack(potion);
        ItemMeta itemMeta = pot.getItemMeta();
        itemMeta.setDisplayName(Araclar.color(potionItem.getTitle()));
        itemMeta.setLore(Araclar.color(potionItem.getLore()));
        pot.setItemMeta(itemMeta);
        pot = NBTEditor.set(pot, potion.getPotionid(), "potionid");
        return pot;
    }

    @Override
    public ItemStack getItemHand(Player p) {
        if (p.getInventory().getItemInMainHand().getType().equals(Material.getMaterial("POTION"))){
            return p.getInventory().getItemInMainHand();
        }else if (p.getInventory().getItemInOffHand().getType().equals(Material.getMaterial("POTION"))){
            return p.getInventory().getItemInOffHand();
        }else {
            return null;
        }
    }

    @Override
    public void emptyHandSlot(Player p, ItemStack item, boolean delete_bottle) {
        if (p.getInventory().getItemInMainHand().equals(item)){
            if (delete_bottle){
                p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            }else {
                p.getInventory().setItemInMainHand(new ItemStack(Material.GLASS_BOTTLE));
            }
        }else {
            if (delete_bottle){
                p.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
            }else {
                p.getInventory().setItemInOffHand(new ItemStack(Material.GLASS_BOTTLE));
            }
        }
    }

    @Override
    public void PotionDrankRunnable(Player player, Potion potion) {
        new PotionDrankRunnableNew(player, potion).runTaskTimer(AetherPotions.instance, 20L, 20L);
    }
}
