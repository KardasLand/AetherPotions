package com.kardasland.aetherpotions.support.types.runnables;

import com.kardasland.aetherpotions.potion.Potion;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class PotionDrankRunnableLegacy extends BukkitRunnable {
    private Player player;
    private Effect type;
    private int amount;
    HashMap<UUID, Integer> cool = new HashMap<>();
    public PotionDrankRunnableLegacy(Player player, Potion potion){
        this.type = Effect.valueOf(potion.getParticle_type());
        this.amount = potion.getParticle_amount();
        this.player = player;
        cool.put(player.getUniqueId(), potion.getParticle_time());
    }
    @Override
    public void run() {
        if (cool.get(player.getUniqueId()) == 1){
            cancel();
        }else {
            cool.put(player.getUniqueId(), cool.get(player.getUniqueId()) - 1);
            for (int i = 0; i <= amount; i ++){
                player.getWorld().playEffect(player.getLocation(), type, 19);
            }
        }
    }
}
