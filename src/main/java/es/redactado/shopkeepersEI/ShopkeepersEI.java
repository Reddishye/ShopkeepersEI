package es.redactado.shopkeepersEI;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.ssomar.score.api.executableitems.ExecutableItemsAPI;
import es.redactado.shopkeepersEI.command.MainCommand;
import es.redactado.shopkeepersEI.config.Config;
import es.redactado.shopkeepersEI.config.ConfigContainer;
import es.redactado.shopkeepersEI.events.HandleUpdateItemEvent;
import es.redactado.shopkeepersEI.events.ShopkeeperItemUpdateListener;

import java.lang.reflect.Type;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.Lamp;
import revxrsal.commands.annotation.SuggestWith;
import revxrsal.commands.annotation.list.AnnotationList;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.bukkit.BukkitLamp;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;

public final class ShopkeepersEI extends JavaPlugin {
    private Logger logger;
    private Injector injector;
    private Plugin executableItems;
    private ConfigContainer<Config> configContainer;

    @Override
    public void onEnable() {
        logger = Logger.create("ShopkeepersEI");
        injector = Guice.createInjector(new GuiceModule(this, logger));
        logger = injector.getInstance(Logger.class);
        configContainer = injector.getInstance(new Key<>() {});

        logger.setMinLevel(configContainer.get().logLevel);

        logger.debug("Debug mode enabled, unless you want a bunch of messages, you should disable it.");

        logger.info("Plugin enabled.");

        // Check if ExecutableItems is installed
        executableItems = Bukkit.getPluginManager().getPlugin("ExecutableItems");
        if (executableItems == null || !executableItems.isEnabled()) {
            logger.info("ExecutableItems -> NOT FOUND");
        } else {
            logger.info("ExecutableItems -> FOUND");

            logger.debug(
                    "Showing EI Item Ids to prove manager is available: "
                            + ExecutableItemsAPI.getExecutableItemsManager()
                                    .getExecutableItemIdsList()
                                    .toString());
        }

        // this code is so fucking ugly, sorryyyy
        Lamp<BukkitCommandActor> lamp = BukkitLamp.builder(this)
                .suggestionProviders(providers -> {
                    providers.addProviderFactory(new SuggestionProvider.Factory<BukkitCommandActor>() {
                        @Override
                        public SuggestionProvider<BukkitCommandActor> create(
                                @NotNull Type type,
                                @NotNull AnnotationList annotations,
                                @NotNull Lamp<BukkitCommandActor> lampInstance
                        ) {
                            SuggestWith suggestWith = annotations.get(SuggestWith.class);

                            if (suggestWith != null) {
                                Class<?> providerClass = suggestWith.value();
                                try {
                                    return (SuggestionProvider<BukkitCommandActor>) injector.getInstance(providerClass);
                                } catch (Exception e) {
                                    logger.error("Failed to create SuggestionProvider for " + providerClass.getName() + ": " + e.getMessage());
                                }
                            }
                            return null;
                        }
                    });
                })
                .build();
        // here the ugly code ends (hopefully)

        // register command
        lamp.register(injector.getInstance(MainCommand.class));

        // register event
        getServer()
                .getPluginManager()
                .registerEvents(
                        new ShopkeeperItemUpdateListener(
                                injector.getInstance(HandleUpdateItemEvent.class)),
                        this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
