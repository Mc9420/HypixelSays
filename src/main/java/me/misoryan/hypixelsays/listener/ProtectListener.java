package me.misoryan.hypixelsays.listener;

import me.misoryan.hypixelsays.HypixelSays;
import me.misoryan.hypixelsays.game.GameState;
import me.misoryan.hypixelsays.parm.AutoRegister;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

/**
 * @Author Misoryan
 * @Date 2022/11/30 17:16
 */
@AutoRegister
public class ProtectListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerBuild(BlockPlaceEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerBreak(BlockBreakEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerPickUpItem(PlayerPickupItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }
}
