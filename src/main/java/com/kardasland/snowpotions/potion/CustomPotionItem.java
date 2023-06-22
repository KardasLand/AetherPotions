package com.kardasland.snowpotions.potion;

import com.kardasland.snowpotions.utility.Misc;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

public class CustomPotionItem {
    @Getter @Setter
    private String id;
    @Getter @Setter
    public CustomPotion customPotion;
    public CustomPotionItem(String id){
        this.customPotion = new CustomPotion(id, true);
    }

    public ItemStack build(){
        ItemStack itemStack = new ItemStack(customPotion.isSplash() ? Material.SPLASH_POTION : Material.POTION);

        //Potion Meta
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        potionMeta.setBasePotionData(customPotion.getData());
        potionMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        itemStack.setItemMeta(potionMeta);

        //Visual Meta
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Misc.color(customPotion.getDisplayName()));
        itemMeta.setLore(Misc.color(customPotion.getLore()));
        itemStack.setItemMeta(itemMeta);
        itemStack = NBTEditor.set(itemStack, customPotion.getId(), "potionid");
        return itemStack;
    }
}
