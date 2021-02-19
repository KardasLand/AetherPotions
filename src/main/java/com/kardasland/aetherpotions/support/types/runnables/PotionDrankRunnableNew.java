package com.kardasland.aetherpotions.support.types.runnables;

import com.kardasland.aetherpotions.potion.Potion;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class PotionDrankRunnableNew extends BukkitRunnable {
    private Player player;
    private Particle type;
    private int amount;
    HashMap<UUID, Integer> cool = new HashMap<>();
    public PotionDrankRunnableNew(Player player, Potion potion){
        this.type = Particle.valueOf(potion.getParticle_type());
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
            player.spawnParticle(type, player.getLocation(), amount);
        }
    }
}
