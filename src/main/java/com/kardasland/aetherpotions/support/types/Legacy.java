package com.kardasland.aetherpotions.support.types;

import com.kardasland.aetherpotions.AetherPotions;
import com.kardasland.aetherpotions.potion.Potion;
import com.kardasland.aetherpotions.potion.PotionItem;
import com.kardasland.aetherpotions.support.Support;
import com.kardasland.aetherpotions.support.types.runnables.PotionDrankRunnableLegacy;
import com.kardasland.aetherpotions.utils.Araclar;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Legacy implements Support {
    @Override
    public ItemStack castPotionToItemStack(Potion potion) {
        org.bukkit.potion.Potion pot = new org.bukkit.potion.Potion(potion.getPotionType());
        ItemStack itemstack = pot.toItemStack(1);
        ItemMeta meta = itemstack.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        itemstack.setItemMeta(meta);
        return itemstack;
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
        if (delete_bottle){
            p.getInventory().setItemInHand(new ItemStack(Material.AIR));
        }else {
            p.getInventory().setItemInHand(new ItemStack(Material.GLASS_BOTTLE));
        }
    }

    @Override
    public void PotionDrankRunnable(Player player, Potion potion) {
        new PotionDrankRunnableLegacy(player, potion).runTaskTimer(AetherPotions.instance, 20L, 20L);
    }
}
