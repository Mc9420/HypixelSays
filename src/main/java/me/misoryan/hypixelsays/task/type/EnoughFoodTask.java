package me.misoryan.hypixelsays.task.type;

import me.misoryan.hypixelsays.HypixelSays;
import me.misoryan.hypixelsays.game.Game;
import me.misoryan.hypixelsays.parm.AutoRegister;
import me.misoryan.hypixelsays.task.AbstractTask;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author Misoryan
 * @Date 2022/11/30 19:01
 */
@AutoRegister
public class EnoughFoodTask extends AbstractTask implements Listener {
    @Override
    public String getInternalName() {
        return "eat_enough_food";
    }

    @Override
    public String getTaskInfo() {
        return "将你的饱食度恢复至满";
    }

    @Override
    public boolean isRace() {
        return true;
    }

    @Override
    public int getTimer() {
        return 20;
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
    public int getFoodLevel() {
        return 1;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onEnd() {

    }

    @EventHandler
    public void onPlayerEat(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Game game = HypixelSays.getInstance().getGame();
            Player player = (Player) event.getEntity();
            if (isTaskPlaying(this) && game.getGamePlayer(player.getUniqueId()) != null) {
                event.setCancelled(false);
                if (event.getFoodLevel() < 1) {
                    event.setFoodLevel(1);
                }
                if (event.getFoodLevel() >= 20) {
                    complete(player);
                }
            }
        }
    }
}
