package com.kardasland.aetherpotions.api;

import com.kardasland.aetherpotions.potion.CustomPotion;
import com.kardasland.aetherpotions.potion.CustomPotionItem;
import com.kardasland.aetherpotions.potion.PotionValidation;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * AetherPotions API
 */
public class PotionManager {

	/**
	 * Get custom potion by ID.
	 * Does not check if the potion is valid.
	 * Check if the potion is valid by using {@link PotionManager#isValid} method.
	 * @param id Potion ID
	 * @return {@link CustomPotion} object
	 */
	public CustomPotion getPotion(String id) {
		return new CustomPotion(id);
	}

	/**
	 * Get custom potion validation by ID.
	 * Useful for manual validation.
	 * @param id Potion ID
	 * @return {@link PotionValidation} object
	 */
	public PotionValidation getValidation(String id) {
		return new PotionValidation(id);
	}

	/**
	 * Get custom potion item by ID.
	 * Does not check if the potion is valid.
	 * Check if the potion is valid by using {@link PotionManager#isValid} method.
	 * @param id Potion ID
	 * @return {@link CustomPotionItem} object
	 */
	public CustomPotionItem getPotionItem(String id) {
		return new CustomPotionItem(new CustomPotion(id));
	}

	/**
	 * Get custom potion item by CustomPotion object.
	 * @param id Potion ID
	 * @return {@link CustomPotionItem} object
	 * @deprecated Use {@link PotionManager#getPotionItemStack(String, Player)} instead.
	 */
	@Deprecated
	public ItemStack getPotionItemStack(String id) {
		return new CustomPotionItem(new CustomPotion(id)).build();
	}

	/**
	 * Get potion item stack by ID.
	 * @param id Potion ID
	 * @param player Player
	 * @return {@link ItemStack} potion item stack
	 */
	public ItemStack getPotionItemStack(String id, Player player) {
		return new CustomPotionItem(new CustomPotion(id)).build(player);
	}

	/**
	 * Get potion item stack by CustomPotion object.
	 * @param potion CustomPotion object
	 * @return {@link ItemStack} potion item stack
	 */
	public ItemStack getPotionItemStack(CustomPotion potion, Player player) {
		return new CustomPotionItem(potion).build(player);
	}

	/**
	 * Give custom potion to player.
	 * @param id Potion ID
	 * @param player Player
	 */
	public void givePotion(String id, Player player) {
		givePotion(id, player, 1);
	}

	/**
	 * Give custom potion with certain amount to player.
	 * Does check if the potion is valid.
	 * @param id Potion ID
	 * @param player Player
	 * @param amount Amount of potion
	 */
	public void givePotion(String id, Player player, int amount) {
		PotionValidation validation = new PotionValidation(id);
		if (!validation.isExists() || !validation.isValid()) {
			return;
		}
		CustomPotion customPotion = new CustomPotion(id);
		CustomPotionItem item = new CustomPotionItem(customPotion);
		item.givePotion(player, amount);
	}

	/**
	 * Check if the potion is valid.
	 * @param id Potion ID
	 * @return true if the potion is valid.
	 */
	public boolean isValid(String id) {
		PotionValidation validation = new PotionValidation(id);
		return validation.isExists() && validation.isValid();
	}
}
