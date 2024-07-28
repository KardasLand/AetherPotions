package com.kardasland.aetherpotions.potion;

import com.kardasland.aetherpotions.AetherPotions;
import lombok.Data;
import org.bukkit.entity.Player;

import java.util.List;

@Data
public class CCommand {
    int order;
    List<String> command;
    int chance;
    int weight;
    Executor executor;

    public CCommand(int order, List<String> command, int chance, int weight, Executor executor) {
        this.order = order;
        this.command = command;
        this.chance = chance;
        this.weight = weight;
        this.executor = executor;
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

    public void execute(Player target){
        for (String s : command) {
            AetherPotions.instance.getServer().dispatchCommand(executor.equals(Executor.CONSOLE) ? AetherPotions.instance.getServer().getConsoleSender() : target, s.replace("%player%", target.getName()));
        }
    }

    public enum Executor {
        CONSOLE,
        PLAYER
    }

    @Override
    public String toString() {
        return "CCommand{" +
                "order=" + order +
                ", command='" + command + '\'' +
                ", chance=" + chance +
                ", weight=" + weight +
                ", executor=" + executor +
                '}';
    }
}
