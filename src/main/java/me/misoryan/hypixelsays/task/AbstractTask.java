package me.misoryan.hypixelsays.task;

import me.misoryan.hypixelsays.HypixelSays;
import me.misoryan.hypixelsays.game.Game;
import me.misoryan.hypixelsays.game.GamePlayer;
import me.misoryan.hypixelsays.game.RoundState;
import me.misoryan.hypixelsays.util.RankUtil;
import me.misoryan.hypixelsays.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @Author Misoryan
 * @Date 2022/11/30 16:32
 */
public abstract class AbstractTask {

    public abstract String getInternalName();

    public abstract String getTaskInfo();

    public abstract boolean isRace();

    public abstract int getTimer();

    public int getHealth() {
        return 20;
    }

    public int getFoodLevel() {
        return 20;
    }


    public abstract List<ItemStack> getItemStacks();

    public abstract void onStart();

    public abstract void onEnd();

    public boolean isTaskPlaying(AbstractTask task) {
        return HypixelSays.getInstance().getGame().getTask() != null && HypixelSays.getInstance().getGame().getTask().getInternalName().equals(task.getInternalName()) && HypixelSays.getInstance().getGame().getRoundState() == RoundState.PLAYING;
    }

    public void complete(GamePlayer player) {
        Game game = HypixelSays.getInstance().getGame();
        if (player.getRank().get(game.getRound()) != null) return;
        game.setFinishedPlayers(game.getFinishedPlayers() + 1);
        player.getRank().put(game.getRound(),game.getFinishedPlayers());
        CC.broadcast(RankUtil.getColoredName(player) + " &a完成了此回合.");
    }

    public void complete(Player player) {
        Game game = HypixelSays.getInstance().getGame();
        if (game.getGamePlayer(player.getUniqueId()) == null) return;
        complete(game.getGamePlayer(player.getUniqueId()));
    }

    public void fail(GamePlayer player) {
        Game game = HypixelSays.getInstance().getGame();
        if (player.getRank().get(game.getRound()) != null) return;
        player.getRank().put(game.getRound(),-1);
        CC.broadcast(RankUtil.getColoredName(player) + " &c在此回合中失败了.");
    }

    public void fail(Player player) {
        Game game = HypixelSays.getInstance().getGame();
        if (game.getGamePlayer(player.getUniqueId()) == null) return;
        fail(game.getGamePlayer(player.getUniqueId()));
    }


}
