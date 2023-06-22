package com.kardasland.snowpotions.runnables;

import com.kardasland.snowpotions.potion.CustomPotion;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.Potion;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class ParticleRunnable extends BukkitRunnable {
    private Player player;
    private Particle type;
    private int amount;
    HashMap<UUID, Integer> cool = new HashMap<>();
    public ParticleRunnable(Player player, CustomPotion potion){
        this.type = Particle.valueOf(potion.getParticle().getType());
        this.amount = potion.getParticle().getAmount();
        this.player = player;
        cool.put(player.getUniqueId(), potion.getParticle().getTime());
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
