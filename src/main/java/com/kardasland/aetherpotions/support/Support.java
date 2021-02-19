package com.kardasland.aetherpotions.support;

import com.kardasland.aetherpotions.potion.Potion;
import com.kardasland.aetherpotions.potion.PotionItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface Support {
    public ItemStack castPotionToItemStack(Potion potion);
    public ItemStack potionAsItemStack(PotionItem potionItem, Potion potion);
    public ItemStack getItemHand(Player p);
    public void emptyHandSlot(Player p, ItemStack item, boolean deleted_bottle);
    public void PotionDrankRunnable(Player player, Potion potion);
}
