package me.misoryan.hypixelsays.task.type;

import me.misoryan.hypixelsays.HypixelSays;
import me.misoryan.hypixelsays.game.Game;
import me.misoryan.hypixelsays.parm.AutoRegister;
import me.misoryan.hypixelsays.task.AbstractTask;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Misoryan
 * @Date 2022/11/30 21:04
 */
@AutoRegister
public class ShootYourselfTask extends AbstractTask implements Listener {
    @Override
    public String getInternalName() {
        return "shoot_yourself";
    }

    @Override
    public String getTaskInfo() {
        return "用弓箭射你自己";
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
        itemStacks.add(new ItemStack(Material.BOW));
        ItemStack itemStack = new ItemStack(Material.ARROW);
        itemStack.setAmount(64);
        itemStacks.add(itemStack);
        return itemStacks;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onEnd() {
        Bukkit.getWorlds().forEach(world -> {
            world.getEntities().forEach(entity -> {
                if (entity instanceof Arrow) {
                    entity.remove();
                }
            });
        });
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Projectile && ((Projectile) event.getDamager()).getShooter() instanceof Player) {
            Player player = (Player) ((Projectile) event.getDamager()).getShooter();
            Game game = HypixelSays.getInstance().getGame();
            if (isTaskPlaying(this) && game.getGamePlayer(player.getUniqueId()) != null && player.getUniqueId().equals(event.getEntity().getUniqueId())) {
                event.setCancelled(false);
                event.setDamage(0);
                complete(player);
            }
        }
    }
}
