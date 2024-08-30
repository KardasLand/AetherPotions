package com.kardasland.aetherpotions.utility.potions;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

public abstract class ItemWrapper {

	public abstract PotionMeta setBasePotionData(PotionMeta potionMeta, PotionType type);

	public PotionMeta addItemFlags(PotionMeta potionMeta, Object... flags) {
		for (Object flag : flags) {
			if (flag instanceof ItemFlag) {
				potionMeta.addItemFlags((ItemFlag) flag);
			}
		}
		return potionMeta;
	}

	public abstract PotionMeta addHideFlags(PotionMeta potionMeta);
}
