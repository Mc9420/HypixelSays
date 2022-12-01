package me.misoryan.hypixelsays.game.runnable;

import me.misoryan.hypixelsays.HypixelSays;
import me.misoryan.hypixelsays.config.PluginConfig;
import me.misoryan.hypixelsays.game.Game;
import me.misoryan.hypixelsays.game.GameState;
import me.misoryan.hypixelsays.game.RoundState;
import me.misoryan.hypixelsays.util.chat.CC;
import me.misoryan.hypixelsays.util.cooldown.Cooldown;
import me.misoryan.hypixelsays.util.time.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

/**
 * @Author Misoryan
 * @Date 2022/11/30 10:32
 */
public class GameRunnable extends BukkitRunnable {
    @Override
    public void run() {
        PluginConfig config = HypixelSays.getInstance().getPluginConfig();
        Game game = HypixelSays.getInstance().getGame();
        if (HypixelSays.getInstance().getGame().getGameState() == GameState.WAITING) {
            if (Bukkit.getServer().getOnlinePlayers().size() < config.getPreStartPlayers()) {
                if (game.getTimer() != null) {
                    CC.broadcast("&c游戏人数不足! 倒计时被取消.");
                }
                game.setTimer(null);
            } else if (Bukkit.getServer().getOnlinePlayers().size() >= config.getPreStartPlayers()) {
                if (game.getTimer() == null) {
                    game.setTimer(new Cooldown(config.getPreStartSeconds(), TimeUnit.SECONDS));
                    CC.broadcast("&e游戏将在 &a" + config.getPreStartSeconds() + " &e秒后开始!");
                } else if (game.getTimer().hasExpired()) {
                    game.startGame();
                } else {
                    if (Bukkit.getServer().getOnlinePlayers().size() >= config.getStartPlayers()) {
                        game.setTimer(new Cooldown(Math.min(game.getTimer().getRemaining(),config.getStartSeconds() * 1000L)));
                    }
                    if (game.getTimer().getRemaining() / 1000 % 10 == 0 || game.getTimer().getRemaining() <= 5000) {
                        CC.broadcast("&e游戏将在 &a" + game.getTimer().getRemaining() / 1000 + " &e秒后开始!");
                    }
                }
            }
        } else if (game.getGameState() == GameState.GAMING) {
            if (game.getPlayers().size() <= 1) {
                CC.broadcast("&c参与游戏玩家不足,游戏已强制结束.");
                game.end();
            }
            if (game.getGameState() == GameState.GAMING && game.getRoundState() == RoundState.IDLE && game.getTimer().hasExpired()) {
                if (game.getRound() < config.getMaxRound()) {
                    game.startRound();
                } else {
                    game.end();
                }
            }
            if (game.getRoundState() == RoundState.PLAYING && game.getTimer().hasExpired()) {
                game.endRound();
            }
        } else if (game.getGameState() == GameState.ENDING && game.getTimer() != null && game.getTimer().hasExpired()) {
            Bukkit.shutdown();
        }
    }
}
