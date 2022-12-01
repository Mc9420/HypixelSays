package me.misoryan.hypixelsays.util;

import me.misoryan.hypixelsays.game.GamePlayer;
import org.bukkit.entity.Player;

/**
 * @Author Misoryan
 * @Date 2022/11/30 10:17
 */
public class RankUtil {

    public static String getColoredName(Player player) {
        return "&7" + player.getName();
    }

    public static String getColoredName(GamePlayer player) {
        return "&7" + player.getName();
    }

    public static String getColoredNameWithPrefix(Player player) {
        return "&7" + player.getName();
    }

    public static String getColoredNameWithPrefix(GamePlayer player) {
        return "&7" + player.getName();
    }
}
