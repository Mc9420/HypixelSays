package me.misoryan.hypixelsays.game;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author Misoryan
 * @Date 2022/11/30 10:50
 */
@Getter
@Setter
public class GamePlayer implements Comparable<GamePlayer> {

    public GamePlayer(Player player) {
        this.name = player.getName();
        this.uuid = player.getUniqueId();
    }
    private String name;
    private UUID uuid;

    private int stars;

    private int finalRank;

    //<Round,Rank>
    private Map<Integer,Integer> rank = new HashMap<>();

    @Override
    public int compareTo(GamePlayer o) {
        return Integer.compare(this.stars, o.stars);
    }
}
