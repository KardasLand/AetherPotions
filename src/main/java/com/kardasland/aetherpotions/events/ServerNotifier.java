package com.kardasland.aetherpotions.events;

import com.kardasland.aetherpotions.AetherPotions;
import com.kardasland.aetherpotions.utility.Misc;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ServerNotifier implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (event.getPlayer().hasPermission("aetherpotions.notify") || event.getPlayer().isOp()) {
            String version = AetherPotions.instance.getDescription().getVersion();
            if (version.contains("dev") || version.contains("beta")) {
                Misc.send(event.getPlayer(), "&7You are using a &cdevelopment/beta &7version of AetherPotions.", true);
                Misc.send(event.getPlayer(), "&7Development builds can contain some bugs/mistakes.", true);
                Misc.send(event.getPlayer(), "&7It is advised to report any error/bugs in our support channel.", true);
                Misc.send(event.getPlayer(), "&aIf you don't need any new features/can wait, please use the &llast stable &aversion.", true);
            }
        }
    }
}
