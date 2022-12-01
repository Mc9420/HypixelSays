package me.misoryan.hypixelsays.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import me.misoryan.hypixelsays.util.configuration.Configuration;
import me.misoryan.hypixelsays.util.configuration.annotations.ConfigData;
import me.misoryan.hypixelsays.util.configuration.annotations.ConfigSerializer;
import me.misoryan.hypixelsays.util.configuration.serializer.LocationSerializer;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;


@Getter
@Setter
public class PluginConfig extends Configuration {
    public PluginConfig(JavaPlugin plugin) {
        super(plugin);
    }

    private int preStartPlayers = 2;

    private int preStartSeconds = 10;

    private int startPlayers = 8;

    private int startSeconds = 5;

    private int maxPlayers = 10;

    private int maxRound = 15;

    @ConfigData(
            path = "location.hubSpawnPoint"
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location hubSpawnPoint;

    @ConfigData(
            path = "location.gameSpawnPoint"
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location gameSpawnPoint;

    /* Example

    @ConfigData(
            path = "testConfig.testBoolean"
    )
    private boolean testBoolean = false;

    @ConfigData(
            path = "testConfig.testString"
    )
    private boolean testString;

    @ConfigData(
            path = "testConfig.testInt"
    )
    private int testInt;

    @ConfigData(
            path = "testConfig.testLocation"
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location testLocation;

    @ConfigData(
            path = "testConfig.testListInteger"
    )
    private List<Integer> testListInteger;

    */

}
