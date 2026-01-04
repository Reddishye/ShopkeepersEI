package es.redactado.shopkeepersEI.commands;

import static es.redactado.shopkeepersEI.utils.ColorTranslator.translate;

import com.google.inject.Inject;
import com.google.inject.Injector;
import es.redactado.shopkeepersEI.Logger;
import es.redactado.shopkeepersEI.config.Config;
import es.redactado.shopkeepersEI.config.ConfigContainer;
import es.redactado.shopkeepersEI.managers.SubCommandDataManager;
import es.redactado.shopkeepersEI.managers.SubCommandsManager;
import es.redactado.shopkeepersEI.subcommands.*;
import java.util.*;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ShopkeepersEICommand extends Command implements CommandExecutor {
    private final Injector injector;
    private final ConfigContainer<Config> configContainer;
    private final BukkitAudiences audiences;
    private final LinkedHashMap<String, SubCommandDataManager> subCommandDataMap =
            new LinkedHashMap<>();
    private final HashMap<SubCommandDataManager, SubCommandsManager> subCommandMap =
            new HashMap<>();
    private final Logger logger;

    @Inject
    protected ShopkeepersEICommand(
            Injector injector,
            ConfigContainer<Config> configContainer,
            JavaPlugin plugin,
            BukkitAudiences audiences,
            Logger logger) {
        super("shopkeepersEI");

        this.injector = injector;
        this.audiences = audiences;
        this.logger = logger;
        this.configContainer = configContainer;

        setName("shopkeepersei");
        setAliases(List.of("sei"));
        setPermission("shopkeepersEI.commands");
        setDescription("Main command of the ShopkeepersEI plugin");

        registerSubCommand(reloadSubCommand.class);
    }

    @Override
    public boolean execute(
            @NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        Audience audience = audiences.sender(sender);

        if (args.length == 0) {
            audience.sendMessage(translate("&cUsage: /shopkeepersEI <subcommand>"));
            return true;
        }

        String subCommandName = args[0].toLowerCase();
        SubCommandsManager subCommand = subCommandMap.get(subCommandDataMap.get(subCommandName));

        if (subCommand == null) {
            audience.sendMessage(translate("&cUnknown subcommand: " + subCommandName));
            return true;
        }

        String[] subCommandArgs = Arrays.copyOfRange(args, 1, args.length);
        subCommand.execute(sender, commandLabel, subCommandArgs);

        return true;
    }

    private void registerSubCommand(Class<? extends SubCommandsManager> subCommandClass) {
        SubCommandsManager subCommand = injector.getInstance(subCommandClass);
        SubCommandDataManager subCommandData =
                subCommand.getClass().getAnnotation(SubCommandDataManager.class);
        subCommandDataMap.put(subCommandData.name(), subCommandData);
        subCommandMap.put(subCommandData, subCommand);
        logger.debug("Registered subcommand: " + subCommandData.name());
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args) {
        return execute(sender, label, args);
    }
}
