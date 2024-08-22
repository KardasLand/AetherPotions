package com.kardasland.aetherpotions.potion;

import com.kardasland.aetherpotions.AetherPotions;
import lombok.Data;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

@Data
public class CCommand {
    int order;
    List<String> command;
    int chance;
    int weight;
    int delay;
    Executor executor;

    public CCommand(int order, List<String> command, int chance, int weight, Executor executor, int delay) {
        this.order = order;
        this.command = command;
        this.chance = chance;
        this.weight = weight;
        this.executor = executor;
        this.delay = delay;
    }
    public CCommand() {
    }

    public boolean isChance() {
        return this.chance > 0;
    }

    public boolean isWeight() {
        return this.weight > 0;
    }

    public boolean isFixed() {
        return !isChance() && !isWeight();
    }
    public boolean isDelayed() {
        return delay > 0;
    }


    // alright fuck it i cant find a way to effectively chain delays.
    // RECURSIVE TIME MOTHERFUCKER
    public void execute(Player target){
        for (String s : command) {
            final String command = s.replace("%player%", target.getName()).replace("%target%", target.getName());
            if (delay > 0) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        AetherPotions.instance.getServer().dispatchCommand(executor.equals(Executor.CONSOLE) ? AetherPotions.instance.getServer().getConsoleSender() : target, command);
                    }
                }.runTaskLater(AetherPotions.instance, delay * 20L);
                return;
            }
            AetherPotions.instance.getServer().dispatchCommand(executor.equals(Executor.CONSOLE) ? AetherPotions.instance.getServer().getConsoleSender() : target, command);
        }
    }


    public void execute(Player target, List<CCommand> e){
        if (e.isEmpty()){
            return;
        }
        CCommand command1 = e.get(0);
        command1.execute(target);
        e.remove(command1);
        execute(target, e);
    }


    public enum Executor {
        CONSOLE,
        PLAYER
    }

    @Override
    public String toString() {
        return "CCommand{" +
                "order=" + order +
                ", commands='" + command + '\'' +
                ", chance=" + chance +
                ", weight=" + weight +
                ", executor=" + executor +
                ", delay=" + delay +
                '}';
    }
}
