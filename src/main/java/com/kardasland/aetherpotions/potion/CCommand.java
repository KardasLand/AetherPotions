package com.kardasland.aetherpotions.potion;

import lombok.Data;

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
