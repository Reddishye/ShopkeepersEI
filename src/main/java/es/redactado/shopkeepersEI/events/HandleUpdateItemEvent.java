package es.redactado.shopkeepersEI.events;

import com.google.inject.Inject;
import com.nisovin.shopkeepers.api.events.UpdateItemEvent;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.ssomar.score.api.executableitems.ExecutableItemsAPI;
import com.ssomar.score.api.executableitems.config.ExecutableItemInterface;
import es.redactado.shopkeepersEI.Logger;
import es.redactado.shopkeepersEI.config.Config;
import es.redactado.shopkeepersEI.config.ConfigContainer;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class HandleUpdateItemEvent implements Listener {
    private final Logger logger;

    @Inject
    public HandleUpdateItemEvent(Logger logger, ConfigContainer<Config> configContainer) {
        this.logger = logger;
    }

    public void handleItemUpdateEvent(UpdateItemEvent event) {
        UnmodifiableItemStack item = event.getItem();
        ItemStack expectedItem = item.copy();

        try {
            Optional<ExecutableItemInterface> execItem = ExecutableItemsAPI.getExecutableItemsManager().getExecutableItem(expectedItem);
            if (execItem.isPresent()) {
                ExecutableItemInterface executableItemInterface = execItem.get();
                expectedItem = executableItemInterface.buildItem(1, Optional.empty(), Optional.empty());

                logger.debug("Item: " + item);
                logger.debug("Expected Item: " + expectedItem);
                logger.debug("Executable Item: " + execItem);
                logger.debug("Matching: " + expectedItem.equals(item.copy()));

                if (!expectedItem.equals(item.copy())) {
                    logger.debug("Item does not match the ExecutableItems configuration. Updating item...");
                    event.setItem(UnmodifiableItemStack.of(expectedItem));
                    logger.debug("Item updated to match the ExecutableItems configuration.");
                }
            }
        } catch (NoClassDefFoundError e) {
            logger.debug("NoClassDefFoundError -> Is ExecutableItems installed?");
        }
    }
}