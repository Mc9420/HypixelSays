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

import java.util.List;

/**
 * @Author Misoryan
 * @Date 2022/11/30 20:40
 */
@AutoRegister
public class StandStillTask extends AbstractTask implements Listener {
    @Override
    public String getInternalName() {
        return "stand_still";
    }

    @Override
    public String getTaskInfo() {
        return "站立不动";
    }

    @Override
    public boolean isRace() {
        return false;
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

    }

    @Override
    public void onEnd() {
        Game game = HypixelSays.getInstance().getGame();
        game.getGamePlayers().forEach(this::complete);
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Game game = HypixelSays.getInstance().getGame();
        if (isTaskPlaying(this) && game.getGamePlayer(player.getUniqueId()) != null && game.getTimer().getRemaining() < 6000) {
            if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getY() != event.getTo().getY() || event.getFrom().getZ() != event.getTo().getZ()) {
                fail(player);
            }
        }
    }
}
