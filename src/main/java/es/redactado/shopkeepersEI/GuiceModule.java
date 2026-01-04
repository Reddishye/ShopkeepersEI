package es.redactado.shopkeepersEI;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import es.redactado.shopkeepersEI.config.Config;
import es.redactado.shopkeepersEI.config.ConfigContainer;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

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
    private BukkitAudiences provideBukkitAudiences() {
        return BukkitAudiences.create(plugin);
    }

    @Provides
    @Singleton
    private ConfigContainer<Config> provideConfigContainer() {
        return ConfigContainer.load(
                plugin.getDataFolder().toPath(), "config.yml", Config.class, logger);
    }
}
