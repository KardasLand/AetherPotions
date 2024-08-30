package com.kardasland.aetherpotions.utility.potions.versions;

import com.kardasland.aetherpotions.utility.potions.PotionMetaWrapper;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public class Wrapper1_20_R1 extends PotionMetaWrapper {
	@Override
	public PotionMeta setBasePotionData(PotionMeta potionMeta, PotionType type) {
		potionMeta.setBasePotionData(new PotionData(type));
		return potionMeta;
	}

	@Override
	public PotionMeta addHideFlags(PotionMeta potionMeta) {
		return addItemFlags(potionMeta, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ATTRIBUTES);
	}
}
