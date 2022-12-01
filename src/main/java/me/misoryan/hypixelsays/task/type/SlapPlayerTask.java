package me.misoryan.hypixelsays.task.type;

import me.misoryan.hypixelsays.HypixelSays;
import me.misoryan.hypixelsays.parm.AutoRegister;
import me.misoryan.hypixelsays.task.AbstractTask;
import me.misoryan.hypixelsays.util.chat.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author Misoryan
 * @Date 2022/11/30 17:00
 */
@AutoRegister
public class SlapPlayerTask extends AbstractTask implements Listener {

    @Override
    public String getInternalName() {
        return "slap_player";
    }

    @Override
    public String getTaskInfo() {
        return "使用鱼攻击其他玩家";
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
        itemStacks.add(new ItemStack(Material.RAW_BEEF));
        itemStacks.add(new ItemStack(Material.POTATO_ITEM));
        itemStacks.add(new ItemStack(Material.BREAD));
        itemStacks.add(new ItemStack(Material.MELON));
        itemStacks.add(new ItemStack(Material.COOKIE));
        itemStacks.add(new ItemStack(Material.PORK));
        itemStacks.add(new ItemStack(Material.APPLE));
        itemStacks.add(new ItemStack(Material.CAKE));
        itemStacks.add(new ItemStack(Material.RAW_FISH));
        Collections.shuffle(itemStacks);
        return itemStacks;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onEnd() {

    }

    @EventHandler
    public void onPlayerDamagePlayer(EntityDamageByEntityEvent event) {
        if (isTaskPlaying(this)) {
            if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
                event.setCancelled(false);
                event.setDamage(0);
                Player player = (Player) event.getDamager();
                if (HypixelSays.getInstance().getGame().getGamePlayer(player.getUniqueId()) == null) return;
                if (player.getItemInHand() != null && player.getItemInHand().getType() == Material.RAW_FISH) {
                    complete(HypixelSays.getInstance().getGame().getGamePlayer(player.getUniqueId()));
                } else {
                    fail(HypixelSays.getInstance().getGame().getGamePlayer(player.getUniqueId()));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (isTaskPlaying(this)) {
            event.setCancelled(false);
            event.setDamage(0);
        }
    }

}
