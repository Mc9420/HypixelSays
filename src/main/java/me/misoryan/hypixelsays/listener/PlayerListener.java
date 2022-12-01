package me.misoryan.hypixelsays.listener;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.misoryan.hypixelsays.HypixelSays;
import me.misoryan.hypixelsays.config.PluginConfig;
import me.misoryan.hypixelsays.game.Game;
import me.misoryan.hypixelsays.game.GamePlayer;
import me.misoryan.hypixelsays.game.GameState;
import me.misoryan.hypixelsays.parm.AutoRegister;
import me.misoryan.hypixelsays.util.PlayerUtil;
import me.misoryan.hypixelsays.util.RankUtil;
import me.misoryan.hypixelsays.util.chat.CC;
import me.misoryan.hypixelsays.util.cooldown.Cooldown;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.concurrent.TimeUnit;

/**
 * @Author Misoryan
 * @Date 2022/11/20 16:54
 */
@AutoRegister
public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage("");
        Player player = event.getPlayer();
        GameState gameState = HypixelSays.getInstance().getGame().getGameState();
        player.getInventory().clear();
        player.setItemOnCursor(null);
        player.setHealth(20);
        player.setFoodLevel(20);
        if (gameState == GameState.WAITING) {
            event.setJoinMessage(CC.translate(RankUtil.getColoredName(player) + " &e进入了游戏! (&b" + Bukkit.getServer().getOnlinePlayers().size() + "&e/&b" + HypixelSays.getInstance().getPluginConfig().getMaxPlayers() + "&e)"));
            player.teleport(HypixelSays.getInstance().getPluginConfig().getHubSpawnPoint());
            HypixelSays.getInstance().getGame().addGamePlayer(player);
        } else if (gameState == GameState.GAMING) {
            player.setGameMode(GameMode.SPECTATOR);
            player.teleport(HypixelSays.getInstance().getPluginConfig().getGameSpawnPoint());
            player.sendMessage(CC.translate("&c游戏已开始,你已被加入观察者中!"));
            HypixelSays.getInstance().getGame().getPlayers().forEach(p -> {
                p.hidePlayer(player);
            });
        } else if (gameState == GameState.ENDING) {
            player.setGameMode(GameMode.SPECTATOR);
            player.teleport(HypixelSays.getInstance().getPluginConfig().getHubSpawnPoint());
        }
        playerJoinLeaveCheck();
        sendServerNamePackets(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage("");
        Player player = event.getPlayer();
        GameState gameState = HypixelSays.getInstance().getGame().getGameState();
        if (gameState == GameState.WAITING) {
            event.setQuitMessage(CC.translate(RankUtil.getColoredName(player) + " &e已退出!"));
        }
        HypixelSays.getInstance().getGame().removeGamePlayer(player);
        playerJoinLeaveCheck();
    }

    @EventHandler
    public void onPlayerPing(ServerListPingEvent event) {
        event.setMotd(HypixelSays.getInstance().getGame().getGameState().name());
    }

    private static void playerJoinLeaveCheck() {
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
        }
    }

    private static void sendServerNamePackets(Player player) {
        if (!HypixelSays.getInstance().getServerName().equals("Unknown")) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("GetServer");
            try {
                player.sendPluginMessage(HypixelSays.getInstance(), "BungeeCord", out.toByteArray());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
