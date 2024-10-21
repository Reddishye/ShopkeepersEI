package es.redactado.shopkeepersEI.events;

import es.redactado.shopkeepersEI.ShopkeepersEI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;

import com.nisovin.shopkeepers.api.events.UpdateItemEvent;

public class ShopkeeperItemUpdateListener implements Listener {

    private final HandleUpdateItemEvent handleItemUpdateEvent;

    public ShopkeeperItemUpdateListener(HandleUpdateItemEvent handleItemUpdateEvent) {
        this.handleItemUpdateEvent = handleItemUpdateEvent;
    }

    @EventHandler
    public void onUpdateItem(UpdateItemEvent event) {
        handleItemUpdateEvent.handleItemUpdateEvent(event);
    }
}
