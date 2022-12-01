package me.misoryan.hypixelsays.task.type;

import me.misoryan.hypixelsays.HypixelSays;
import me.misoryan.hypixelsays.game.Game;
import me.misoryan.hypixelsays.parm.AutoRegister;
import me.misoryan.hypixelsays.task.AbstractTask;
import me.misoryan.hypixelsays.util.chat.CC;
import net.minecraft.server.v1_8_R3.EntityEgg;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Misoryan
 * @Date 2022/11/30 19:09
 */
@AutoRegister
public class ThrowEggsTask extends AbstractTask implements Listener {
    @Override
    public String getInternalName() {
        return "throw_eggs";
    }

    @Override
    public String getTaskInfo() {
        return "向其他玩家扔鸡蛋";
    }

    @Override
    public boolean isRace() {
        return true;
    }

    @Override
    public int getTimer() {
        return 10;
    }

    @Override
    public List<ItemStack> getItemStacks() {
        List<ItemStack> itemStacks = new ArrayList<>();
        ItemStack itemStack = new ItemStack(Material.EGG);
        itemStack.setAmount(64);
        itemStacks.add(itemStack);
        return itemStacks;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onEnd() {

    }

    @EventHandler
    public void onPlayerDamagedByEggs(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Projectile && ((Projectile) event.getDamager()).getShooter() instanceof Player) {
            Player player = (Player) ((Projectile) event.getDamager()).getShooter();
            Game game = HypixelSays.getInstance().getGame();
            if (isTaskPlaying(this) && game.getGamePlayer(player.getUniqueId()) != null) {
                event.setCancelled(false);
                complete(player);
            }
        }
    }

    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent event) {
        if (isTaskPlaying(this) && event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.EGG)) {
            event.setCancelled(true);
        }
    }
}
