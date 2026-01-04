package es.redactado.shopkeepersEI.events;

import com.nisovin.shopkeepers.api.events.UpdateItemEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

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
