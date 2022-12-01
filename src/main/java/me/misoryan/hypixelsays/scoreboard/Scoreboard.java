package me.misoryan.hypixelsays.scoreboard;

import io.github.thatkawaiisam.assemble.AssembleAdapter;
import me.misoryan.hypixelsays.HypixelSays;
import me.misoryan.hypixelsays.config.PluginConfig;
import me.misoryan.hypixelsays.game.Game;
import me.misoryan.hypixelsays.game.GameState;
import me.misoryan.hypixelsays.util.RankUtil;
import me.misoryan.hypixelsays.util.time.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @Author Misoryan
 * @Date 2022/11/30 09:37
 */
public class Scoreboard implements AssembleAdapter {
    @Override
    public String getTitle(Player player) {
        return "&e&lMISORYAN SAYS";
    }

    @Override
    public List<String> getLines(Player player) {
        List<String> lines = new ArrayList<>();
        try {
            Game game = HypixelSays.getInstance().getGame();
            PluginConfig config = HypixelSays.getInstance().getPluginConfig();
            int onlinePlayers = Bukkit.getServer().getOnlinePlayers().size();

            lines.add("&7");
            switch (game.getGameState()) {
                case LOADING:
                    lines.add("&c游戏正在加载中...");
                    break;
                case WAITING:
                    lines.add("&f玩家: &a" + onlinePlayers + "/" + config.getMaxPlayers());
                    lines.add("&7");
                    if (onlinePlayers < config.getPreStartPlayers()) {
                        lines.add("&f需要 &a" + (config.getPreStartPlayers() - onlinePlayers) + " &f名玩家");
                        lines.add("&f加入以开始游戏...");
                    } else if (game.getTimer() != null) {
                        lines.add("&f游戏将在 &a" + TimeUtil.millisToTimer(game.getTimer().getRemaining()) + " &f后");
                        lines.add("&f开始以容纳更多玩家进入");
                    }
                    lines.add("&7");
                    lines.add("&f服务器: &a" + HypixelSays.getInstance().getServerName());
                    break;
                default:
                    boolean included = false;
                    Collections.sort(game.getGamePlayers());
                    for (int i = 0; i < Math.min(game.getGamePlayers().size(),3); i++) {
                        if (game.getGamePlayers().get(game.getGamePlayers().size() - i - 1) != null && Bukkit.getPlayer(game.getGamePlayers().get(game.getGamePlayers().size() - i - 1).getUuid()) != null) {
                            lines.add(RankUtil.getColoredName(Bukkit.getPlayer(game.getGamePlayers().get(game.getGamePlayers().size() - i - 1).getUuid())) + "&f: &a" + game.getGamePlayers().get(game.getGamePlayers().size() - i - 1).getStars());
                            if (game.getGamePlayers().get(game.getGamePlayers().size() - i - 1).getUuid().equals(player.getUniqueId())) {
                                included = true;
                            }
                        }
                    }
                    if (!included && game.getGamePlayer(player.getUniqueId()) != null) {
                        lines.add("&7");
                        lines.add(RankUtil.getColoredName(player) + "&f: &a" + game.getGamePlayer(player.getUniqueId()).getStars());
                    }
                    if (game.getGamePlayer(player.getUniqueId()) == null) {
                        lines.add("&7");
                        lines.add("&c你正在观战中!");
                    }
                    lines.add("&7");
                    if (game.getGameState() == GameState.GAMING) {
                        lines.add("&f回合: &a" + game.getRound() + "/" + config.getMaxRound());
                    } else if (game.getGameState() == GameState.ENDING) {
                        lines.add("&a游戏结束!");
                    }
            }
            lines.add("&7");
            lines.add("&eme.misoryan.hypixelsays");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lines;
    }
}
