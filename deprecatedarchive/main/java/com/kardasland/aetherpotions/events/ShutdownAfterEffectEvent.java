package com.kardasland.aetherpotions.events;

import com.kardasland.aetherpotions.AetherPotions;
import com.kardasland.aetherpotions.potion.CustomPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
	/* TODO OLD
    there can be a bug, if player drinks a potion, for example fly command,
    when server shuts down, player will still have the effect, but the potion will be gone
    Solution: save the potion effect to player's data, and when player joins, give the effect back
    Alternative solution: when server shuts down, trigger after effect event.

    Update: Alternative solution is better. It is not worth to save every player's potion effect to the temporary database etc...
     */
public class ShutdownAfterEffectEvent implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if (AetherPotions.instance.getPotionCache().contains(event.getPlayer().getUniqueId())) {
			AetherPotions.instance.getPotionCache().remove(event.getPlayer().getUniqueId());
			CustomPotion customPotion = new CustomPotion(AetherPotions.instance.getPotionCache().get(event.getPlayer().getUniqueId()));
			customPotion.execute(event.getPlayer(), customPotion.getCommandList().getAfterEffect().getCommandList());
		}
	}
}
