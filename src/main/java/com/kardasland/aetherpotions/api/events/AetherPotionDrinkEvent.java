package com.kardasland.aetherpotions.api.events;

import com.kardasland.aetherpotions.potion.CustomPotion;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;



public class AetherPotionDrinkEvent extends Event implements Cancellable {
	@Getter
	private static final HandlerList HANDLERS = new HandlerList();
	@Getter @Setter
	private boolean isCancelled;
	@Getter @Setter
	private CustomPotion potion;
	@Getter @Setter
	private Player player;
	@Getter @Setter
	private boolean isInstant;

	public AetherPotionDrinkEvent(CustomPotion potion, Player player) {
		this.potion = potion;
		this.player = player;
		this.isInstant = false;
		this.isCancelled = false;
	}
	public AetherPotionDrinkEvent(CustomPotion potion, Player player, boolean isInstant) {
		this.potion = potion;
		this.player = player;
		this.isInstant = isInstant;
		this.isCancelled = false;
	}

	@NotNull
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}


}
