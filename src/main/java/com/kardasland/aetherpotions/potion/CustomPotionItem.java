package com.kardasland.aetherpotions.potion;

import com.kardasland.aetherpotions.utility.Misc;
import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.List;

public class CustomPotionItem {
    @Getter @Setter
    private String id;
    @Getter @Setter
    public CustomPotion customPotion;
    public CustomPotionItem(String id){
        this.customPotion = new CustomPotion(id, true);
    }
    public CustomPotionItem(String id, boolean reduced){
        this.customPotion = new CustomPotion(id, reduced);
    }
    public CustomPotionItem(CustomPotion customPotion){
        this.customPotion = customPotion;
    }

    public ItemStack build(){
        ItemStack itemStack = new ItemStack(customPotion.isSplash() ? Material.SPLASH_POTION : Material.POTION);
        //Potion Meta
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        if (potionMeta != null) {
            potionMeta.setDisplayName(Misc.color(customPotion.getDisplayName()));
            potionMeta.setLore(Misc.color(customPotion.getLore()));
            potionMeta.setBasePotionData(customPotion.getData());
            // This breaks when 1.20 and 1.21 idk other versions
            // TODO Need to fix this
            potionMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            itemStack.setItemMeta(potionMeta);
        }
        return itemStack;
    }

    /**
     * Placeholder integration
     * @param player Player
     * @return Potion ItemStack
     */
    public ItemStack build(Player player){
        ItemStack itemStack = new ItemStack(customPotion.isSplash() ? Material.SPLASH_POTION : Material.POTION);
        //Potion Meta
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        if (potionMeta != null) {
            String displayName = customPotion.getDisplayName();
            displayName = PlaceholderAPI.setPlaceholders(player, displayName);
            potionMeta.setDisplayName(Misc.color(displayName));
            List<String> lore = customPotion.getLore();
            lore = PlaceholderAPI.setPlaceholders(player, lore);
            potionMeta.setLore(Misc.color(lore));
            potionMeta.setBasePotionData(customPotion.getData());
            potionMeta.setCustomModelData(customPotion.getCustomModelData());
            // This breaks when 1.20 and 1.21 idk other versions
            // TODO Need to fix this
            potionMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            itemStack.setItemMeta(potionMeta);
        }
        return itemStack;
    }
}
