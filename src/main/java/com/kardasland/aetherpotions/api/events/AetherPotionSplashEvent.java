package com.kardasland.aetherpotions.api.events;

import com.kardasland.aetherpotions.potion.CustomPotion;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class AetherPotionSplashEvent extends Event implements Cancellable {
	@Getter
	private static final HandlerList HANDLERS = new HandlerList();
	@Getter @Setter
	private boolean isCancelled;
	@Getter @Setter
	private CustomPotion potion;
	@Getter @Setter
	private Player victim;
	@Getter @Setter
	private Player shooter;

	public AetherPotionSplashEvent(CustomPotion potion, Player victim) {
		this.potion = potion;
		this.victim = victim;
		this.shooter = null;
		this.isCancelled = false;
	}
	public AetherPotionSplashEvent(CustomPotion potion, Player victim, Player shooter) {
		this.potion = potion;
		this.victim = victim;
		this.shooter = shooter;
		this.isCancelled = false;
	}

	@NotNull
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
}
