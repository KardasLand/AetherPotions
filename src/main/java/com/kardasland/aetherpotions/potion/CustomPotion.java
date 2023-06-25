package com.kardasland.aetherpotions.potion;

import com.kardasland.aetherpotions.AetherPotions;
import com.kardasland.aetherpotions.runnables.ParticleRunnable;
import com.kardasland.aetherpotions.utility.ConfigManager;
import com.kardasland.aetherpotions.utility.CooldownHandler;
import com.kardasland.aetherpotions.utility.Misc;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Objects;

public class CustomPotion {
    @Getter @Setter
    private String displayName;
    @Getter @Setter
    private String id;
    @Getter @Setter
    private int time;
    @Getter @Setter
    private List<String> lore;
    @Getter @Setter
    private boolean deleteBottle;
    @Getter @Setter
    private boolean isSplash;
    @Getter @Setter
    private CustomParticle particle;
    @Getter @Setter
    private CustomCommandList commandList;
    @Getter @Setter
    private PotionType type;

    @Getter @Setter
    private PotionData data;

    @Getter @Setter
    private PotionValidation validation;


    public CustomPotion(String id){
        init(id, false);
    }
    public CustomPotion(String id, boolean reduced){
        init(id, reduced);
    }

    public void init(String id, boolean reduced){
        this.id = id;
        PotionValidation potionValidation = new PotionValidation(id);
        if (!potionValidation.isValid()){
            return;
        }
        FileConfiguration cf = ConfigManager.get("potions.yml");
        assert cf != null;
        String shortcut = "potions."+id+".";
        this.displayName = cf.getString(shortcut + "displayName");
        this.lore = cf.getStringList(shortcut + "lore");
        this.isSplash = cf.getBoolean(shortcut + "isSplash");
        this.data = new PotionData(PotionType.valueOf(cf.getString(shortcut + "data.potionType")), false, false);
        if (!reduced){
            this.deleteBottle = !isSplash && cf.getBoolean(shortcut + "deleteBottle");
            this.particle = new CustomParticle(id);
            this.commandList = new CustomCommandList(id, isSplash);
        }
    }

    public void apply(Player p, PlayerItemConsumeEvent event){
        if (CooldownHandler.isInCooldown(p.getUniqueId(), this.getId())){
            Misc.send(p,
                    Objects.requireNonNull(ConfigManager.get("messages.yml")).getString("StillInCooldown")
                            .replace("%time%", String.valueOf(CooldownHandler.getTimeLeft(p.getUniqueId(), getId()))), true);
        }else {

            if (getTime() != 0){
                CooldownHandler c = new CooldownHandler(p.getUniqueId(), getId(), getTime());
                c.start();
            }


            for (String command : (isSplash ? getCommandList().getSplashCommandList() : getCommandList().getDrinkingCommandList())){
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", p.getName()));
            }

            if (!isSplash){
                if (p.getInventory().getItemInMainHand().equals(event.getItem())) {
                    p.getInventory().setItemInMainHand(isDeleteBottle() ? new ItemStack(Material.AIR) : new ItemStack(Material.GLASS_BOTTLE));
                }else {
                    p.getInventory().setItemInOffHand(isDeleteBottle() ? new ItemStack(Material.AIR) : new ItemStack(Material.GLASS_BOTTLE));
                }
            }

            if (getParticle().isEnabled()){
                new ParticleRunnable(p, this).runTaskTimer(AetherPotions.instance, 20L, 20L);
            }

            if (getCommandList().getAfterEffect().isEnabled()){
                new BukkitRunnable(){
                    @Override
                    public void run(){
                        for (String command : getCommandList().getAfterEffect().getCommands()){
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", p.getName()));
                        }
                    }
                }.runTaskLater(AetherPotions.instance, 20L * this.getCommandList().getAfterEffect().getTime());
            }
        }
    }
}


