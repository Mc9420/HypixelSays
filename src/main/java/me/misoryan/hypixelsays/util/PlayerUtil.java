package me.misoryan.hypixelsays.util;

import me.misoryan.hypixelsays.HypixelSays;
import me.misoryan.hypixelsays.util.chat.CC;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @Author Misoryan
 * @Date 2022/11/30 17:46
 */
public class PlayerUtil {

    public static void sendActionBar(Player player, String message) {
        ChatComponentText components = new ChatComponentText(CC.translate(message));
        PacketPlayOutChat packet = new PacketPlayOutChat(components, (byte) 2);

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public static void sendActionBar(Player player, String message, int duration) {
        sendActionBar(player, message);

        if (duration >= 0) {
            // Sends empty message at the end of the duration. Allows messages shorter than 3 seconds, ensures precision.
            new BukkitRunnable() {
                @Override
                public void run() {
                    sendActionBar(player, "");
                }
            }.runTaskLater(HypixelSays.getInstance(), duration + 1);
        }

        // Re-sends the messages every 3 seconds so it doesn't go away from the player's screen.
        while (duration > 40) {
            duration -= 40;
            new BukkitRunnable() {
                @Override
                public void run() {
                    sendActionBar(player, message);
                }
            }.runTaskLater(HypixelSays.getInstance(), duration);
        }
    }

    public static void sendTitle(Player player, String title, String sub, int fadeIn, int fadeOut, int duration) {
        if (title != null) {
            PacketPlayOutTitle packet = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, new ChatComponentText(CC.translate(title)), fadeIn, fadeOut, duration);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
        if (sub != null) {
            PacketPlayOutTitle packet = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, new ChatComponentText(CC.translate(sub)), fadeIn, fadeOut, duration);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
    }
}
