package es.redactado.shopkeepersEI.command;

import com.google.inject.Inject;
import es.redactado.shopkeepersEI.ShopkeepersEI;
import es.redactado.shopkeepersEI.config.Config;
import es.redactado.shopkeepersEI.config.ConfigContainer;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.CommandPlaceholder;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import static es.redactado.shopkeepersEI.utils.ColorTranslator.translate;

@Command({"shopkeepersei", "sei"})
public class MainCommand {
    private final ConfigContainer<Config> configContainer;
    private final ShopkeepersEI plugin;

    @Inject
    public MainCommand(ConfigContainer<Config> configContainer, ShopkeepersEI plugin) {
        this.configContainer = configContainer;
        this.plugin = plugin;
    }

    @CommandPlaceholder
    @CommandPermission("shopkeepersei.about")
    public void mainCommand(BukkitCommandActor actor) {
            actor.audience().get().sendMessage(translate(""));
            actor.audience().get().sendMessage(translate("&d╔═════════════════════════════════╗"));
            actor.audience().get().sendMessage(translate("&d    &6&lShopkeepersEI &r&eHelp"));
            actor.audience().get().sendMessage(translate("&d╚═════════════════════════════════╝"));
            actor.audience().get().sendMessage(translate(""));

            if (actor.sender().hasPermission("shopkeepersei.reload")) {
                actor.audience().get().sendMessage(translate("&5&l▸&r &b/skei reload"));
                actor.audience().get().sendMessage(translate("  &5└─ &7&oReload plugin configuration"));
                actor.audience().get().sendMessage(translate(""));
            }

            actor.audience().get().sendMessage(translate("&8━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"));
            actor.audience().get().sendMessage(translate("  &7Version: &e" + plugin.getDescription().getVersion()));
            actor.audience().get().sendMessage(translate("&8━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"));
            actor.audience().get().sendMessage(translate(""));
    }

    @Subcommand("reload")
    @CommandPermission("shopkeepersei.reload")
    public void reload(BukkitCommandActor actor) {
        configContainer.reload();
        actor.reply(translate("&aConfiguration reloaded successfully!"));
    }
}
