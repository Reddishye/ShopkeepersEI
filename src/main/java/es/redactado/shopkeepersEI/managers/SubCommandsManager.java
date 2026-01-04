package es.redactado.shopkeepersEI.managers;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public abstract class SubCommandsManager {
    public abstract void execute(
            @NotNull CommandSender sender, @NotNull String cmd, @NotNull String[] args);
}
