package me.misoryan.hypixelsays.task.type;

import me.misoryan.hypixelsays.HypixelSays;
import me.misoryan.hypixelsays.game.Game;
import me.misoryan.hypixelsays.parm.AutoRegister;
import me.misoryan.hypixelsays.task.AbstractTask;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Author Misoryan
 * @Date 2022/11/30 22:58
 */
@AutoRegister
public class NodHeadTask extends AbstractTask implements Listener {

    public static List<UUID> upHead = new ArrayList<>();
    public static List<UUID> downHead = new ArrayList<>();

    @Override
    public String getInternalName() {
        return "nod_head";
    }

    @Override
    public String getTaskInfo() {
        return "上下点头";
    }

    @Override
    public boolean isRace() {
        return true;
    }

    @Override
    public int getTimer() {
        return 8;
    }

    @Override
    public List<ItemStack> getItemStacks() {
        return null;
    }

    @Override
    public void onStart() {
        upHead.clear();
        downHead.clear();
    }

    @Override
    public void onEnd() {

    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Game game = HypixelSays.getInstance().getGame();
        if (isTaskPlaying(this) && game.getGamePlayer(player.getUniqueId()) != null) {
            if (event.getTo().getPitch() > 60 && !upHead.contains(player.getUniqueId())) {
                upHead.add(player.getUniqueId());
            }
            if (event.getTo().getPitch() < -60 && !downHead.contains(player.getUniqueId())) {
                downHead.add(player.getUniqueId());
            }
            if (upHead.contains(player.getUniqueId()) && downHead.contains(player.getUniqueId())) {
                complete(player);
            }
        }
    }
}
