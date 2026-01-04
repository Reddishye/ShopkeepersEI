package es.redactado.shopkeepersEI;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import es.redactado.shopkeepersEI.config.Config;
import es.redactado.shopkeepersEI.config.ConfigContainer;
import es.redactado.shopkeepersEI.config.ConfigMapper;
import java.io.IOException;
import java.nio.file.Path;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

public class GuiceModule extends AbstractModule {
    private final JavaPlugin plugin;

    public GuiceModule(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        bind(JavaPlugin.class).toInstance(plugin);
        bind(Logger.class).asEagerSingleton();
        bind(String.class).annotatedWith(Named.class).toInstance("config.yml");
    }

    @Provides
    private Config provideConfig(ConfigContainer<Config> configurationContainer) {
        return configurationContainer.get();
    }

    @Provides
    private ConfigContainer<Config> provideConfigContainer(
            @Named("config.yml") String fileName, ConfigMapper configMapper) throws IOException {
        Path configPath = plugin.getDataFolder().toPath().resolve(fileName);
        YamlConfigurationLoader loader =
                YamlConfigurationLoader.builder()
                        .nodeStyle(NodeStyle.BLOCK)
                        .indent(2)
                        .defaultOptions(opts -> opts.shouldCopyDefaults(true))
                        .path(configPath)
                        .build();

        ConfigContainer<Config> configContainer =
                ConfigContainer.load(plugin.getDataFolder().toPath(), Config.class, fileName);
        configMapper.register(Config.class, configContainer);

        return configContainer;
    }

    @Provides
    private ConfigMapper provideConfigMapper() {
        return new ConfigMapper();
    }

    @Provides
    private BukkitAudiences provideBukkitAudiences() {
        return BukkitAudiences.create(plugin);
    }

    @Provides
    private YamlConfigurationLoader provideYamlConfigurationLoader(
            @Named("config.yml") String fileName) {
        Path configPath = plugin.getDataFolder().toPath().resolve(fileName);
        return YamlConfigurationLoader.builder()
                .nodeStyle(NodeStyle.BLOCK)
                .indent(2)
                .defaultOptions(opts -> opts.shouldCopyDefaults(true))
                .path(configPath)
                .build();
    }
}
