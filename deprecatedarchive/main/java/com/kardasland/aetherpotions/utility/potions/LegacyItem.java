package com.kardasland.aetherpotions.utility.potions;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public class LegacyItem extends ItemWrapper {

	@Override
	public PotionMeta setBasePotionData(PotionMeta potionMeta, PotionType type) {
		potionMeta.setBasePotionData(new PotionData(type));
		return potionMeta;
	}

	@Override
	public PotionMeta addItemFlags(PotionMeta potionMeta, Object... flags) {
		for (Object flag : flags) {
			if (flag instanceof ItemFlag) {
				potionMeta.addItemFlags((ItemFlag) flag);
			}
		}
		return null;
	}

	@Override
	public PotionMeta addHideFlags(PotionMeta potionMeta) {
		return addItemFlags(potionMeta, ItemFlag.HIDE_POTION_EFFECTS);
	}
}
