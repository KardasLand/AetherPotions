package com.kardasland.aetherpotions.potion;

import com.kardasland.aetherpotions.AetherPotions;
import com.kardasland.aetherpotions.runnables.ParticleRunnable;
import com.kardasland.aetherpotions.utility.ConfigManager;
import com.kardasland.aetherpotions.utility.CooldownHandler;
import com.kardasland.aetherpotions.utility.Misc;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Objects;



@SuppressWarnings({"checkstyle:Indentation", "checkstyle:MissingJavadocType"})
@Data
public class CustomPotion {
    private String displayName;
    private String id;
    private boolean instantDrink;
    private int time;
    private List<String> lore;
    private boolean deleteBottle;
    private boolean isSplash;
    private CustomParticle particle;
    private CustomCommandList commandList;
    private int customModelData;
    private PotionType type;

    private boolean isExtended = false;
    private boolean isUpgraded = false;
    private boolean originalEffect = false;

    private PotionData data;
    private PotionValidation validation;


    public CustomPotion(String id) {
        init(id, false);
    }

    public CustomPotion(String id, boolean reduced) {
        init(id, reduced);
    }

    /**
     * Initialize the potion.
     *
     *  @param id The id of the potion
     *  @param reduced If the potion is reduced
     */
    public void init(String id, boolean reduced) {
        this.id = id;
        PotionValidation potionValidation = new PotionValidation(id);
        if (!potionValidation.isValid()) {
            return;
        }
        FileConfiguration cf = ConfigManager.get("potions.yml");
        assert cf != null;
        String shortcut = "potions." + id + ".";
        this.displayName = cf.getString(shortcut + "displayName");
        this.lore = cf.getStringList(shortcut + "lore");
        this.isSplash = cf.getBoolean(shortcut + "isSplash");
        this.instantDrink = cf.isSet(shortcut + "instantDrink") && cf.getBoolean(shortcut + "instantDrink");
        final String potionType = cf.getString(shortcut + "data.potionType");
        if (cf.isSet(shortcut + "data.customModelData")) {
            this.customModelData = cf.getInt(shortcut + "data.customModelData");
        }
        if (cf.isSet(shortcut + "data.extended")) {
            this.isExtended = cf.getBoolean(shortcut + "data.extended");
        }
        if (cf.isSet(shortcut + "data.upgraded")) {
            this.isUpgraded = cf.getBoolean(shortcut + "data.upgraded");
        }
        if (cf.isSet(shortcut + "data.originalEffect")) {
            this.originalEffect = cf.getBoolean(shortcut + "data.originalEffect");
        }
        this.data = new PotionData(PotionType.valueOf(potionType), isExtended, isUpgraded);
        if (!reduced) {
            this.deleteBottle = !isSplash && cf.getBoolean(shortcut + "deleteBottle");
            this.particle = new CustomParticle(id);
            this.commandList = new CustomCommandList(id, isSplash);
        }
        // HOW IN THE ACTUAL FUCK THAT I FORGOT ABOUT IT?!?!?!?
        this.time = cf.isSet(shortcut + "cooldown") ? cf.getInt(shortcut + "cooldown") : 0;
    }

    /**
     * I wanted to do internally.
     * @param p Player
     * @param item The potion
     */
    private void applyEffect(Player p, ItemStack item) {
        if (CooldownHandler.isInCooldown(p.getUniqueId(), this.getId())) {
            Misc.send(p, Objects.requireNonNull(ConfigManager.get("messages.yml")).getString("StillInCooldown").replace("%time%", String.valueOf(CooldownHandler.getTimeLeft(p.getUniqueId(), getId()))), true);
            return;
        }
        // HOW THE FUCK I FORGOT THE COOLDOWNS?!?!?!?!
        // THE PROBLEM IS, HOW TF NO ONE TRIED COOLDOWN FOR **NEARLY A YEAR**?
        // I guess no one really uses it lmao
        if (getTime() != 0) {
            CooldownHandler c = new CooldownHandler(p.getUniqueId(), getId(), getTime());
            c.start();
        }

        for (String command : (isSplash ? getCommandList().getSplashCommandList() : getCommandList().getDrinkingCommandList())) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", p.getName()));
        }

        if (!isSplash) {
            if (p.getInventory().getItemInMainHand().equals(item)) {
                p.getInventory().setItemInMainHand(isDeleteBottle() ? new ItemStack(Material.AIR) : new ItemStack(Material.GLASS_BOTTLE));
            } else {
                p.getInventory().setItemInOffHand(isDeleteBottle() ? new ItemStack(Material.AIR) : new ItemStack(Material.GLASS_BOTTLE));
            }
        }

        if (getParticle().isEnabled()) {
            new ParticleRunnable(p, this).runTaskTimer(AetherPotions.instance, 20L, 20L);
        }

        if (getCommandList().getAfterEffect().isEnabled()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (String command : getCommandList().getAfterEffect().getCommandList()) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", p.getName()));
                    }
                }
            }.runTaskLater(AetherPotions.instance, 20L * this.getCommandList().getAfterEffect().getTime());
        }
    }

    // Decided to separate the apply method, because of the event handling.
    // I only need the item, but maybe in the future, I will need the event too.
    // I will probably change the methods, but for now, it's okay.
    public void apply(Player p, PlayerItemConsumeEvent event) {
        applyEffect(p, event!= null ? event.getItem() : null);
    }

    public void apply(Player p, PlayerInteractEvent event) {
        applyEffect(p, event.getItem());
    }
}


