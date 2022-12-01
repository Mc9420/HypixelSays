package me.misoryan.hypixelsays.game;

import lombok.Getter;
import lombok.Setter;
import me.misoryan.hypixelsays.HypixelSays;
import me.misoryan.hypixelsays.task.AbstractTask;
import me.misoryan.hypixelsays.task.type.NodHeadTask;
import me.misoryan.hypixelsays.task.type.SpawnGolemTask;
import me.misoryan.hypixelsays.util.PlayerUtil;
import me.misoryan.hypixelsays.util.RandomUtil;
import me.misoryan.hypixelsays.util.RankUtil;
import me.misoryan.hypixelsays.util.chat.CC;
import me.misoryan.hypixelsays.util.cooldown.Cooldown;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author Misoryan
 * @Date 2022/11/30 10:26
 */
@Getter
@Setter
public class Game {

    private static Random random = new Random();

    private Cooldown timer;

    private GameState gameState = GameState.LOADING;

    private RoundState roundState = RoundState.IDLE;

    private AbstractTask task;

    private int round = 0;

    private int finishedPlayers = 0;

    private List<GamePlayer> gamePlayers = new ArrayList<>();

    private List<Location> placedBlocks = new ArrayList<>();

    public void addGamePlayer(Player player) {
        gamePlayers.removeIf(gamePlayer -> gamePlayer.getUuid().equals(player.getUniqueId()));
        gamePlayers.add(new GamePlayer(player));
    }

    public List<Player> getPlayers() {
        List<Player> list = new ArrayList<>();
        for (GamePlayer gamePlayer : gamePlayers) {
            if (Bukkit.getPlayer(gamePlayer.getUuid()) != null) {
                list.add(Bukkit.getPlayer(gamePlayer.getUuid()));
            }
        }
        return list;
    }

    public GamePlayer getGamePlayer(UUID uuid) {
        for (GamePlayer player : gamePlayers) {
            if (player.getUuid().equals(uuid)) {
                return player;
            }
        }
        return null;
    }

    public void removeGamePlayer(Player player) {
        gamePlayers.removeIf(gamePlayer -> gamePlayer.getUuid().equals(player.getUniqueId()));
    }

    public void removeGamePlayer(UUID uuid) {
        gamePlayers.removeIf(gamePlayer -> gamePlayer.getUuid().equals(uuid));
    }

    public void removeGamePlayer(String name) {
        gamePlayers.removeIf(gamePlayer -> gamePlayer.getName().equals(name));
    }

    public void startGame() {
        this.gameState = GameState.GAMING;
        this.timer = new Cooldown(5, TimeUnit.SECONDS);
        Bukkit.getOnlinePlayers().forEach(player -> player.teleport(HypixelSays.getInstance().getPluginConfig().getGameSpawnPoint()));
    }

    public void startRound() {
        this.roundState = RoundState.PLAYING;
        this.task = HypixelSays.getInstance().getTaskFactory().getTasks().get(random.nextInt(HypixelSays.getInstance().getTaskFactory().getTasks().size()));
        this.timer = new Cooldown(task.getTimer(),TimeUnit.SECONDS);
        this.round++;
        this.finishedPlayers = 0;
        List<ItemStack> itemStacks = task.getItemStacks();
        getPlayers().forEach(player -> {
            player.getInventory().clear();
            player.setItemOnCursor(null);
            player.setHealth(task.getHealth());
            player.setFoodLevel(task.getFoodLevel());
            if (itemStacks != null) {
                itemStacks.forEach(item -> player.getInventory().addItem(item));
            }
            PlayerUtil.sendActionBar(player,"&6" + task.getTaskInfo(),task.getTimer() * 20);
            PlayerUtil.sendTitle(player,"","&e" + task.getTaskInfo(),0,task.getTimer() * 20,0);
                }
        );
        task.onStart();
    }

    public void endRound() {
        this.roundState = RoundState.IDLE;
        this.timer = new Cooldown(3,TimeUnit.SECONDS);
        task.onEnd();
        getPlacedBlocks().forEach(location -> location.getBlock().setType(Material.AIR));
        getPlacedBlocks().clear();
        getPlayers().forEach(player -> {
                    player.teleport(HypixelSays.getInstance().getPluginConfig().getGameSpawnPoint());
                    player.closeInventory();
                    player.getInventory().clear();
                    player.setItemOnCursor(null);
                    player.setGameMode(GameMode.SURVIVAL);
                    player.setHealth(20);
                    player.setFoodLevel(20);
                    GamePlayer gamePlayer = getGamePlayer(player.getUniqueId());
                    int rank = gamePlayer.getRank().get(round) == null ? -1 : gamePlayer.getRank().get(round);
                    if (task.isRace()) {
                        if (rank == -1) {
                            PlayerUtil.sendTitle(player,"","&c你在此回合失败了!", 0, 3 * 20, 0);
                            player.sendMessage(CC.translate("&c你在此回合失败了! &6" + finishedPlayers + "&e/" + getPlayers().size() + "名玩家完成了此回合."));
                        } else {
                            PlayerUtil.sendTitle(player,"","&f你获得了第 " + gamePlayer.getRank().get(round) + " 名!", 0, 3 * 20, 0);
                            player.sendMessage(CC.translate("&a你获得了第" + gamePlayer.getRank().get(round) + "名! " + (finishedPlayers != getPlayers().size() ? "&6" + finishedPlayers + "&e/" + getPlayers().size() + "名玩家完成了此回合." : "")));
                            if (rank >= 1 && rank <= 3) {
                                gamePlayer.setStars(gamePlayer.getStars() + (4 - rank));
                            }
                        }
                    } else {
                        if (rank == -1) {
                            PlayerUtil.sendTitle(player,"","&c你在此回合失败了!", 0, 3 * 20, 0);
                            player.sendMessage(CC.translate("&c你在此回合失败了! &6" + finishedPlayers + "&e/" + getPlayers().size() + "名玩家完成了此回合."));
                        } else {
                            PlayerUtil.sendTitle(player,"","&a你成功完成了此回合游戏!", 0, 3 * 20, 0);
                            player.sendMessage(CC.translate("&a你成功完成了此回合游戏! " + (finishedPlayers != getPlayers().size() ? "&6" + finishedPlayers + "&e/" + getPlayers().size() + "名玩家完成了此回合." : "")));
                            gamePlayer.setStars(gamePlayer.getStars() + 1);
                        }
                    }
                });

        if (round < HypixelSays.getInstance().getPluginConfig().getMaxRound()) {
            CC.broadcast("&a回合结束! 下一回合将在 3 秒后开始.");
        } else {
            end();
        }
    }

    public void end() {
        this.gameState = GameState.ENDING;
        this.timer = new Cooldown(10,TimeUnit.SECONDS);
        Collections.sort(gamePlayers);
        for (int i = 0; i < getGamePlayers().size(); i++) {
            getGamePlayers().get(getGamePlayers().size() - i - 1).setFinalRank(i + 1);
        }
        CC.broadcast(CC.SB_BAR + CC.SB_BAR);
        CC.broadcast("                          &f&lMISORYAN SAYS");
        CC.broadcast(" ");
        if (gamePlayers.size() > 0) {
            CC.broadcast("                      &e&l第一名 &7- " + RankUtil.getColoredNameWithPrefix(getGamePlayers().get(getGamePlayers().size() - 1)));
        }
        if (gamePlayers.size() > 1) {
            CC.broadcast("                      &6&l第二名 &7- " + RankUtil.getColoredNameWithPrefix(getGamePlayers().get(getGamePlayers().size() - 2)));
        }
        if (gamePlayers.size() > 2) {
            CC.broadcast("                      &c&l第三名 &7- " + RankUtil.getColoredNameWithPrefix(getGamePlayers().get(getGamePlayers().size() - 3)));
        }
        CC.broadcast(" ");
        CC.broadcast(CC.SB_BAR + CC.SB_BAR);
        getGamePlayers().forEach(player -> {
            PlayerUtil.sendTitle(Bukkit.getPlayer(player.getUuid()),"&6游戏结束!","&a你赢得了第" + player.getFinalRank() + "名!",0,60,0);
            Bukkit.getPlayer(player.getUuid()).teleport(HypixelSays.getInstance().getPluginConfig().getHubSpawnPoint());
        });
    }



}
