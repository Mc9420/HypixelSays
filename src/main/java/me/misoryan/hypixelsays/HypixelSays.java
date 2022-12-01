package me.misoryan.hypixelsays;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import io.github.thatkawaiisam.assemble.Assemble;
import io.github.thatkawaiisam.assemble.AssembleStyle;
import lombok.Getter;
import me.misoryan.hypixelsays.config.PluginConfig;
import me.misoryan.hypixelsays.game.Game;
import me.misoryan.hypixelsays.game.GameState;
import me.misoryan.hypixelsays.game.runnable.GameRunnable;
import me.misoryan.hypixelsays.parm.AutoRegister;
import me.misoryan.hypixelsays.scoreboard.Scoreboard;
import me.misoryan.hypixelsays.task.TaskFactory;
import me.misoryan.hypixelsays.util.ClassUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.Collection;

public final class HypixelSays extends JavaPlugin implements PluginMessageListener {

    public static String MAIN_PACKAGE;

    @Getter
    private String serverName = "Unknown";

    @Getter
    private Game game;

    @Getter
    private static HypixelSays instance;

    private GameRunnable gameRunnable;

    @Getter
    private TaskFactory taskFactory;
    @Override
    public void onEnable() {
        // Plugin startup logic

        this.game = new Game();
        getGame().setGameState(GameState.WAITING);

        instance = this;
        MAIN_PACKAGE = instance.getClass().getPackage().getName();

        loadListener();
        loadConfig();
        loadAssemble();
        loadRunnable();
        loadTasks();



        Bukkit.getLogger().info("HypixelSays Enabled");
        getGame().setGameState(GameState.WAITING);

    }

    //module: config
    private PluginConfig pluginConfig;

    private void loadConfig() {
        Bukkit.getLogger().info("Start loading configuration....");
        this.pluginConfig = new PluginConfig(this);
        this.pluginConfig.load();
        this.pluginConfig.save();
        Bukkit.getLogger().info("Configuration is now loaded.");
    }

    public PluginConfig getPluginConfig() {
        return this.pluginConfig;
    }

    private void loadRunnable() {
        this.gameRunnable = new GameRunnable();
        gameRunnable.runTaskTimer(this,20,20);
    }

    private void loadTasks() {
        this.taskFactory = new TaskFactory();
        taskFactory.init();
        Bukkit.getLogger().info(taskFactory.getTasks().size() + " Tasks loaded.");
    }


    private void loadAssemble() {
        // Start Instance.
        Assemble assemble = new Assemble(this, new Scoreboard());

        // Set Interval (Tip: 20 ticks = 1 second).
        assemble.setTicks(2);

        // Set Style (Tip: Viper Style starts at -1 and goes down).
        assemble.setAssembleStyle(AssembleStyle.KOHI);
    }



    private void loadListener() {
        Bukkit.getLogger().info("Start loading listeners now...");

        Collection<Class<?>> classes = ClassUtil.getClassesInPackage(this, MAIN_PACKAGE);

        for (Class<?> clazz : classes) {
            try {
                if (clazz.isAnnotationPresent(AutoRegister.class)) {
                    if (Listener.class.isAssignableFrom(clazz)) {
                        try {
                            Bukkit.getPluginManager()
                                    .registerEvents((Listener) clazz.newInstance(), this);
                        } catch (Exception e) {
                            Bukkit.getLogger().info("Error occupied in loading class " + clazz.getName());
                        }
                        Bukkit.getLogger().info("Listener in class " + clazz.getName() + " loaded.");
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        Bukkit.getLogger().info("All listeners finished loading.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();


        if (subchannel.equals("GetServer")) {
            serverName = in.readUTF();
        }
    }
}
