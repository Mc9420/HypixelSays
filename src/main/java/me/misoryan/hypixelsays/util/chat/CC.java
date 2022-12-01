package me.misoryan.hypixelsays.util.chat;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @Author: EmptyIrony
 * @Date: 2020/12/30 0:00
 */
public class CC {
    private static final Gson gson = new Gson();
    private static final transient String SECTOR_SYMBOL = "§";
    private static final transient String ALL_PATTERN = "[0-9A-FK-ORa-fk-or]";
    private static final transient Pattern VANILLA_PATTERN = Pattern.compile(SECTOR_SYMBOL + "+(" + ALL_PATTERN + ")");


    public static final String BLUE;
    public static final String AQUA;
    public static final String YELLOW;
    public static final String RED;
    public static final String GRAY;
    public static final String GOLD;
    public static final String GREEN;
    public static final String WHITE;
    public static final String BLACK;
    public static final String BOLD;
    public static final String ITALIC;
    public static final String UNDER_LINE;
    public static final String STRIKE_THROUGH;
    public static final String RESET;
    public static final String MAGIC;
    public static final String DARK_BLUE;
    public static final String DARK_AQUA;
    public static final String DARK_GRAY;
    public static final String DARK_GREEN;
    public static final String DARK_PURPLE;
    public static final String DARK_RED;
    public static final String PINK;
    public static final String MENU_BAR;
    public static final String CHAT_BAR;
    public static final String SB_BAR;
    private static final Map<String, ChatColor> MAP = new HashMap();

    static {
        MAP.put("pink", ChatColor.LIGHT_PURPLE);
        MAP.put("orange", ChatColor.GOLD);
        MAP.put("purple", ChatColor.DARK_PURPLE);
        ChatColor[] var0 = ChatColor.values();
        int var1 = var0.length;

        for (int var2 = 0; var2 < var1; ++var2) {
            ChatColor chatColor = var0[var2];
            MAP.put(chatColor.name().toLowerCase().replace("_", ""), chatColor);
        }

        BLUE = ChatColor.BLUE.toString();
        AQUA = ChatColor.AQUA.toString();
        YELLOW = ChatColor.YELLOW.toString();
        RED = ChatColor.RED.toString();
        GRAY = ChatColor.GRAY.toString();
        GOLD = ChatColor.GOLD.toString();
        GREEN = ChatColor.GREEN.toString();
        WHITE = ChatColor.WHITE.toString();
        BLACK = ChatColor.BLACK.toString();
        BOLD = ChatColor.BOLD.toString();
        ITALIC = ChatColor.ITALIC.toString();
        UNDER_LINE = ChatColor.UNDERLINE.toString();
        STRIKE_THROUGH = ChatColor.STRIKETHROUGH.toString();
        RESET = ChatColor.RESET.toString();
        MAGIC = ChatColor.MAGIC.toString();
        DARK_BLUE = ChatColor.DARK_BLUE.toString();
        DARK_AQUA = ChatColor.DARK_AQUA.toString();
        DARK_GRAY = ChatColor.DARK_GRAY.toString();
        DARK_GREEN = ChatColor.DARK_GREEN.toString();
        DARK_PURPLE = ChatColor.DARK_PURPLE.toString();
        DARK_RED = ChatColor.DARK_RED.toString();
        PINK = ChatColor.LIGHT_PURPLE.toString();
        MENU_BAR = ChatColor.GOLD.toString() + ChatColor.STRIKETHROUGH + "------------------------";
        CHAT_BAR = ChatColor.GOLD.toString() + ChatColor.STRIKETHROUGH + "------------------------------------------------";
        SB_BAR = ChatColor.GOLD.toString() + ChatColor.STRIKETHROUGH + "----------------------";
    }

    public CC() {
    }

    public static String stripColor(String input) {
        return VANILLA_PATTERN.matcher(input).replaceAll("");
    }

    public static Set<String> getColorNames() {
        return MAP.keySet();
    }

    public static void debug(String string) {
        CC.boardCastWithPermission(string, "thepit.admin");
    }

    public static ChatColor getColorFromName(String name) {
        if (MAP.containsKey(name.trim().toLowerCase())) {
            return MAP.get(name.trim().toLowerCase());
        } else {
            try {
                return ChatColor.valueOf(name.toUpperCase().replace(" ", "_"));
            } catch (Exception var3) {
                return null;
            }
        }
    }

    public static void printError(Player sender, Exception e) {
        sender.sendMessage(CC.translate("&c" + e.toString()));
        for (StackTraceElement element : e.getStackTrace()) {
            sender.sendMessage(CC.translate("&cAt " + element.toString()));
        }
        sender.sendMessage(translate("&c执行操作时发生了一个错误.请完整截图此信息并反馈至管理员!"));
    }

    public static String translate(String in) {
        return ChatColor.translateAlternateColorCodes('&', in);
    }

    public static List<String> translate(List<String> lines) {
        List<String> toReturn = new ArrayList<>();

        for (String line : lines) {
            toReturn.add(ChatColor.translateAlternateColorCodes('&', line));
        }

        return toReturn;
    }

    public static List<String> translate(String[] lines) {
        List<String> toReturn = new ArrayList<>();

        for (String line : lines) {
            if (line != null) {
                toReturn.add(ChatColor.translateAlternateColorCodes('&', line));
            }
        }

        return toReturn;
    }

    public static void broadcast(String text) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(CC.translate(text));
        }
    }

    public static void boardCastWithPermission(String text, String permission) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission(permission)) {
                player.sendMessage(CC.translate(text));
            }
        }
    }
}
