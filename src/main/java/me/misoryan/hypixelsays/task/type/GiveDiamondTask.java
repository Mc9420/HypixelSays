package me.misoryan.hypixelsays.task.type;

import me.misoryan.hypixelsays.HypixelSays;
import me.misoryan.hypixelsays.game.Game;
import me.misoryan.hypixelsays.parm.AutoRegister;
import me.misoryan.hypixelsays.task.AbstractTask;
import me.misoryan.hypixelsays.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Author Misoryan
 * @Date 2022/11/30 20:03
 */
@AutoRegister
public class GiveDiamondTask extends AbstractTask implements Listener {
    @Override
    public String getInternalName() {
        return "give_diamond";
    }

    @Override
    public String getTaskInfo() {
        return "给予其他玩家钻石";
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
        return null;
    }

    @Override
    public void onStart() {
        Game game = HypixelSays.getInstance().getGame();
        game.getPlayers().forEach(player -> {
        ItemStack itemStack = new ItemStack(Material.DIAMOND);
        itemStack.setAmount(64);
        player.getInventory().addItem(ItemUtil.changeNbt(itemStack,"owner",player.getUniqueId().toString()));
    });
    }

    @Override
    public void onEnd() {
        Bukkit.getWorlds().forEach(world -> {
            world.getEntities().forEach(entity -> {
                if (entity instanceof Item) {
                    entity.remove();
                }
            });
        });
    }

    @EventHandler
    public void onPlayerPickItem(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        Game game = HypixelSays.getInstance().getGame();
        if (isTaskPlaying(this) && game.getGamePlayer(player.getUniqueId()) != null && event.getItem().getItemStack().getType() == Material.DIAMOND) {
            event.setCancelled(true);
            if (ItemUtil.getItemStringData(event.getItem().getItemStack(), "owner") != null) {
                String uuid = ItemUtil.getItemStringData(event.getItem().getItemStack(), "owner");
                if (game.getGamePlayer(UUID.fromString(uuid)) != null && !player.getUniqueId().equals(UUID.fromString(uuid))) {
                    complete(game.getGamePlayer(UUID.fromString(uuid)));
                }
            }
            event.getItem().remove();
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Game game = HypixelSays.getInstance().getGame();
        if (isTaskPlaying(this) && game.getGamePlayer(player.getUniqueId()) != null) {
            event.setCancelled(false);
        }
    }
}
