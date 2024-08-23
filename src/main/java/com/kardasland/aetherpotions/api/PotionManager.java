package com.kardasland.aetherpotions.api;

import com.kardasland.aetherpotions.potion.CustomPotion;
import com.kardasland.aetherpotions.potion.CustomPotionItem;
import com.kardasland.aetherpotions.potion.PotionValidation;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


// This class will be used on other plugins as API.
public class PotionManager {
	public CustomPotion getPotion(String id) {
		return new CustomPotion(id);
	}

	public PotionValidation getValidation(String id) {
		return new PotionValidation(id);
	}

	public CustomPotionItem getPotionItem(String id) {
		return new CustomPotionItem(new CustomPotion(id));
	}

	@Deprecated
	public ItemStack getPotionItemStack(String id) {
		return new CustomPotionItem(new CustomPotion(id)).build();
	}

	public ItemStack getPotionItemStack(String id, Player player) {
		return new CustomPotionItem(new CustomPotion(id)).build(player);
	}

	public ItemStack getPotionItemStack(CustomPotion potion, Player player) {
		return new CustomPotionItem(potion).build(player);
	}

	public void givePotion(String id, Player player) {
		CustomPotion potion = new CustomPotion(id);
		CustomPotionItem item = new CustomPotionItem(potion);
		player.getInventory().addItem(item.build());
	}

	public void givePotion(String id, Player player, int amount) {
		CustomPotion potion = new CustomPotion(id);
		CustomPotionItem item = new CustomPotionItem(potion);
		item.build().setAmount(amount);
		player.getInventory().addItem(item.build());
	}

	public boolean isPotion(String id) {
		return new CustomPotion(id).getValidation().isValid();
	}

	public boolean isValid(String id) {
		return new PotionValidation(id).isValid();
	}
}
