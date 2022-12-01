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
 * @Date 2022/11/30 19:15
 */
@AutoRegister
public class JumpTask extends AbstractTask implements Listener {
    @Override
    public String getInternalName() {
        return "jump";
    }

    @Override
    public String getTaskInfo() {
        return "跳向空中";
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
        return null;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onEnd() {

    }

    @EventHandler
    public void onJump(PlayerMoveEvent event) {
        Game game = HypixelSays.getInstance().getGame();
        Player player = event.getPlayer();
        if (isTaskPlaying(this) && game.getGamePlayer(player.getUniqueId()) != null && event.getFrom().getY() < event.getTo().getY()) {
            complete(player);
        }
    }
}
