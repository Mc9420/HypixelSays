package me.misoryan.hypixelsays.task.type;

import me.misoryan.hypixelsays.HypixelSays;
import me.misoryan.hypixelsays.game.Game;
import me.misoryan.hypixelsays.parm.AutoRegister;
import me.misoryan.hypixelsays.task.AbstractTask;
import me.misoryan.hypixelsays.util.chat.CC;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @Author Misoryan
 * @Date 2022/11/30 21:47
 */
@AutoRegister
public class SpawnGolemTask extends AbstractTask implements Listener {

    public HashMap<Location, UUID> placedPumpkins = new HashMap<>();
    @Override
    public String getInternalName() {
        return "build_golem";
    }

    @Override
    public String getTaskInfo() {
        return "建造一个铁傀儡";
    }

    @Override
    public boolean isRace() {
        return true;
    }

    @Override
    public int getTimer() {
        return 15;
    }

    @Override
    public List<ItemStack> getItemStacks() {
        List<ItemStack> itemStacks = new ArrayList<>();
        ItemStack itemStack = new ItemStack(Material.IRON_BLOCK);
        itemStack.setAmount(64);
        itemStacks.add(itemStack);
        itemStacks.add(new ItemStack(Material.PUMPKIN));
        return itemStacks;
    }

    @Override
    public void onStart() {
        placedPumpkins.clear();
    }

    @Override
    public void onEnd() {

    }

    @EventHandler
    public void onBuild(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Game game = HypixelSays.getInstance().getGame();
        if (isTaskPlaying(this) && game.getGamePlayer(player.getUniqueId()) != null) {
            event.setCancelled(false);
            game.getPlacedBlocks().add(event.getBlock().getLocation());
            if (event.getBlock().getType() == Material.PUMPKIN) {
                placedPumpkins.put(event.getBlock().getLocation(),player.getUniqueId());
            }
        }
    }

    @EventHandler
    public void onGolemSpawn(CreatureSpawnEvent event) {
       if (isTaskPlaying(this) && event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.BUILD_IRONGOLEM)) {
           event.setCancelled(true);
           for (Location location : placedPumpkins.keySet()) {
               if (location.getX() - event.getLocation().getX() == -0.5 && location.getY() - event.getLocation().getY() < 1.96 && location.getY() - event.getLocation().getY() > 1.95 && location.getZ() - event.getLocation().getZ() == -0.5) {
                   Game game = HypixelSays.getInstance().getGame();
                   if (game.getGamePlayer(placedPumpkins.get(location)) != null) {
                       complete(game.getGamePlayer(placedPumpkins.get(location)));
                   }
               }
           }
        }
    }
}
