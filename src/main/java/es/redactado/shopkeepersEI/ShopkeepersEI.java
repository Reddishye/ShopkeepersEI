package es.redactado.shopkeepersEI;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.ssomar.score.api.executableitems.config.ExecutableItemsManagerInterface;
import es.redactado.shopkeepersEI.config.Config;
import es.redactado.shopkeepersEI.config.ConfigContainer;
import es.redactado.shopkeepersEI.config.ConfigMapper;
import es.redactado.shopkeepersEI.events.HandleUpdateItemEvent;
import es.redactado.shopkeepersEI.events.ShopkeeperItemUpdateListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import com.ssomar.score.api.executableitems.ExecutableItemsAPI;

import java.util.List;

public final class ShopkeepersEI extends JavaPlugin {
    private Logger logger;
    private ConfigMapper configMapper;
    private Injector injector;
    private Plugin executableItems;

    @Override
    public void onEnable() {
        injector = Guice.createInjector(new GuiceModule(this));
        logger = injector.getInstance(Logger.class);
        configMapper = injector.getInstance(ConfigMapper.class);

        ConfigContainer<Config> configContainer = injector.getInstance(new Key<ConfigContainer<Config>>() {});
        configMapper.register(Config.class, configContainer);

        Config config = configContainer.get();

        if (config.isDebug) {
            logger.info("Debug mode enabled, unless you want a bunch of messages, you should disable it.");
        }

        logger.info("Plugin enabled.");

        // Check if ExecutableItems is installed
        executableItems = Bukkit.getPluginManager().getPlugin("ExecutableItems");
        if (executableItems == null || !executableItems.isEnabled()) {
            logger.info("ExecutableItems -> NOT FOUND");
        } else {
            logger.info("ExecutableItems -> FOUND");

            logger.debug("Showing EI Item Ids to prove manager is available: " + ExecutableItemsAPI.getExecutableItemsManager().getExecutableItemIdsList().toString());
        }

        // register event
        getServer().getPluginManager().registerEvents(new ShopkeeperItemUpdateListener(injector.getInstance(HandleUpdateItemEvent.class)), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void registerCommands(List<Command> commands) {
        commands.forEach(command -> {
            PluginCommand bukkitCommand = getCommand(command.getName());
            if (bukkitCommand != null) {
                bukkitCommand.setExecutor((CommandExecutor) injector.getInstance(command.getClass()));
                logger.debug("Registered command: " + command.getName());
            } else {
                logger.error("Command not found: " + command.getName() + ". Did you register it in plugin.yml?");
            }
        });
    }
}
