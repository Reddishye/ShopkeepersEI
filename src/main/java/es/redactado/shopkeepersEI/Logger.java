package es.redactado.shopkeepersEI;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import es.redactado.shopkeepersEI.config.Config;
import es.redactado.shopkeepersEI.config.ConfigContainer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;

@Singleton
public class Logger {
    private final MiniMessage miniMessage;
    private final String prefix;
    private final Audience console;
    private final ConfigContainer<Config> configContainer;
    private final Config config;

    @Inject
    public Logger(JavaPlugin plugin, ConfigContainer<Config> configContainer) {
        this.miniMessage = MiniMessage.miniMessage();
        this.console = BukkitAudiences.create(plugin).console();
        this.configContainer = configContainer;
        this.config = configContainer.get();
        this.prefix =
                (config.messages.prefix != null && !config.messages.prefix.isEmpty())
                        ? config.messages.prefix
                        : "<#eb64f8><bold>ShopkeepersEI<reset> <dark_gray>» ";
    }

    public void info(String message) {
        log(LogLevel.INFO, message);
    }

    public void warn(String message) {
        log(LogLevel.WARN, message);
    }

    public void error(String message) {
        log(LogLevel.ERROR, message);
    }

    public void debug(String message) {
        if (config.isDebug) {
            log(LogLevel.DEBUG, message);
        }
    }

    private void log(LogLevel level, String message) {
        String formattedMessage = prefix + level.getColor() + message;
        console.sendMessage(miniMessage.deserialize(formattedMessage));
    }

    private enum LogLevel {
        INFO("<#f9b4eb>"),
        WARN("<#f9e5b4>"),
        ERROR("<#f9beb4>"),
        DEBUG("<#b4c8f9>");

        private final String color;

        LogLevel(String color) {
            this.color = color;
        }

        public String getColor() {
            return color;
        }
    }
}
