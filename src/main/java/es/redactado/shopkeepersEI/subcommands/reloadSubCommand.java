package es.redactado.shopkeepersEI.subcommands;

import com.google.inject.Inject;
import es.redactado.shopkeepersEI.commands.ShopkeepersEICommand;
import es.redactado.shopkeepersEI.config.Config;
import es.redactado.shopkeepersEI.config.ConfigContainer;
import es.redactado.shopkeepersEI.managers.SubCommandDataManager;
import es.redactado.shopkeepersEI.managers.SubCommandsManager;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static es.redactado.shopkeepersEI.utils.ColorTranslator.translate;

@SubCommandDataManager(
        name = "reload",
        description = "Reloads the config files",
        permission = "mysthicCommandManager.admin"
)

public class reloadSubCommand extends SubCommandsManager {
    private final ConfigContainer<Config> configContainer;
    private final BukkitAudiences bukkitAudiences;

    @Inject
    public reloadSubCommand(ConfigContainer<Config> configContainer, BukkitAudiences bukkitAudiences) {
        super();

        this.configContainer = configContainer;
        this.bukkitAudiences = bukkitAudiences;
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String args, @NotNull String[] strings) {
        configContainer.reload();
        bukkitAudiences.sender(sender).sendMessage(translate(configContainer.get().messages.pluginReloaded));
    }
}
