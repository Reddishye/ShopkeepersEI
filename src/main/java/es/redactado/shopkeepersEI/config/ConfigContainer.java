package es.redactado.shopkeepersEI.config;

import de.exlll.configlib.ConfigLib;
import de.exlll.configlib.NameFormatters;
import de.exlll.configlib.YamlConfigurationProperties;
import de.exlll.configlib.YamlConfigurations;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import es.redactado.shopkeepersEI.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * @author Angelillo15
 * (Some modifications have been made by redactado)
 */
public class ConfigContainer<C> {
    private final AtomicReference<C> configuration = new AtomicReference<>();
    private final Class<C> clazz;
    private final String fileName;
    private final Path path;
    private final Logger logger;

    private final YamlConfigurationProperties properties =
            ConfigLib.BUKKIT_DEFAULT_PROPERTIES.toBuilder()
                    .setNameFormatter(NameFormatters.IDENTITY)
                    .setFieldFilter(field -> !field.getName().startsWith("$$"))
                    .header(
                            """
                            Shopkeepers ExecutableItems Integration (Unofficial)
                            https://github.com/Reddishye/ShopkeepersEI
                            
                            Feel free to create an issue on GitHub if you find any bugs or have any suggestions.
                            """)
                    .build();

    // yep, redactado was also here

    private ConfigContainer(Class<C> clazz, String fileName, Path path, Logger logger) {
        this.clazz = clazz;
        this.fileName = fileName;
        this.path = path;
        this.logger = logger;

        logger.debug("ConfigContainer initialization started");

        try {
            // Check if target directory exists
            if (!Files.exists(path)) {
                logger.debug("Target directory does not exist, creating: " + path);
                try {
                    Files.createDirectories(path);
                    logger.info("Created configuration directory: " + path);
                } catch (Exception dirEx) {
                    logger.error("Failed to create configuration directory: " + path, dirEx);
                    throw dirEx;
                }
            }

            // Check if config file exists
            Path configFile = path.resolve(fileName);
            boolean fileExists = Files.exists(configFile);
            logger.debug("Configuration file exists: " + fileExists + " - " + configFile);

            if (!fileExists) {
                logger.info("Configuration file does not exist, will be created: " + configFile);
            }

            logger.debug("Loading configuration using YamlConfigurations.update()");
            C config = YamlConfigurations.update(configFile, clazz, properties);

            if (config == null) {
                logger.error("Configuration loading returned null - this is critical");
                throw new IllegalStateException("Failed to load configuration - got null result");
            }

            configuration.set(config);
            logger.info("ConfigContainer initialized successfully for " + clazz.getSimpleName());
        } catch (Exception e) {
            logger.error("Failed to initialize ConfigContainer for " + clazz.getSimpleName(), e);
            throw new RuntimeException("ConfigContainer initialization failed", e);
        }
    }

    public CompletableFuture<Void> reload() {
        logger.debug("Configuration reload requested for " + clazz.getSimpleName());
        return CompletableFuture.runAsync(
                        () -> {
                            try {
                                Path configFile = path.resolve(fileName);

                                // Check if file still exists
                                if (!Files.exists(configFile)) {
                                    logger.warn(
                                            "Configuration file no longer exists during reload: "
                                                    + configFile);
                                    logger.debug(
                                            "File may have been deleted - will attempt to"
                                                    + " recreate");
                                }

                                logger.debug("Executing YamlConfigurations.update() for reload");
                                C config = YamlConfigurations.update(configFile, clazz, properties);

                                if (config == null) {
                                    logger.error(
                                            "Configuration reload returned null for "
                                                    + clazz.getSimpleName());
                                    throw new IllegalStateException(
                                            "Configuration reload failed - got null result");
                                }

                                configuration.set(config);

                                logger.info(
                                        "Configuration successfully reloaded for "
                                                + clazz.getSimpleName());
                                logger.debug("Reload operation completed successfully");

                            } catch (Exception e) {
                                logger.error(
                                        "Failed to reload configuration for "
                                                + clazz.getSimpleName(),
                                        e);
                                throw new RuntimeException("Configuration reload failed", e);
                            }
                        })
                .whenComplete(
                        (result, throwable) -> {
                            if (throwable != null) {
                                logger.error(
                                        "Async configuration reload failed for "
                                                + clazz.getSimpleName(),
                                        throwable);
                            }
                        });
    }

    public CompletableFuture<Void> save() {
        logger.debug("Configuration save requested for " + clazz.getSimpleName());
        return CompletableFuture.runAsync(
                        () -> {
                            try {
                                C config = configuration.get();
                                if (config == null) {
                                    logger.error(
                                            "Cannot save null configuration for "
                                                    + clazz.getSimpleName());
                                    throw new IllegalStateException(
                                            "Configuration is null - cannot save");
                                }

                                Path configFile = path.resolve(fileName);
                                // Check file status before save
                                boolean fileExisted = Files.exists(configFile);
                                long oldSize = fileExisted ? Files.size(configFile) : 0;

                                logger.debug("Executing YamlConfigurations.save()");
                                YamlConfigurations.save(configFile, clazz, config);

                                // Check file status after save
                                if (Files.exists(configFile)) {
                                    long newSize = Files.size(configFile);
                                    logger.info(
                                            "Configuration successfully saved for "
                                                    + clazz.getSimpleName()
                                                    + " ("
                                                    + newSize
                                                    + " bytes)");
                                } else {
                                    logger.error(
                                            "Configuration file was not created during save: "
                                                    + configFile);
                                    throw new IllegalStateException(
                                            "Save operation failed - file not created");
                                }

                                logger.debug("Save operation completed successfully");

                            } catch (Exception e) {
                                logger.error(
                                        "Failed to save configuration for " + clazz.getSimpleName(),
                                        e);
                                throw new RuntimeException("Configuration save failed", e);
                            }
                        })
                .whenComplete(
                        (result, throwable) -> {
                            if (throwable != null) {
                                logger.error(
                                        "Async configuration save failed for "
                                                + clazz.getSimpleName(),
                                        throwable);
                            }
                        });
    }

    public C get() {
        C config = configuration.get();
        if (config == null) {
            logger.warn("Configuration is null when accessed for " + clazz.getSimpleName());
        }
        return config;
    }

    public static <C> ConfigContainer<C> load(
            @NotNull final Path path,
            @NotNull final String fileName,
            @NotNull final Class<C> clazz,
            @NotNull final Logger logger) {
        logger.debug(
                "Static load method called for " + clazz.getSimpleName() + " from " + fileName);
        try {
            ConfigContainer<C> container = new ConfigContainer<>(clazz, fileName, path, logger);
            logger.debug("ConfigContainer successfully created via static load method");
            return container;
        } catch (Exception e) {
            logger.error("Failed to create ConfigContainer via static load method", e);
            throw e;
        }
    }

    /**
     * Gets information about this configuration container for debugging
     */
    public String getInfo() {
        Path configFile = path.resolve(fileName);
        boolean fileExists = Files.exists(configFile);
        long fileSize = 0;
        try {
            fileSize = fileExists ? Files.size(configFile) : 0;
        } catch (Exception e) {
        }

        return String.format(
                "ConfigContainer[class=%s, file=%s, path=%s, exists=%s, size=%d bytes]",
                clazz.getSimpleName(), fileName, path, fileExists, fileSize);
    }

    @Override
    public String toString() {
        return getInfo();
    }
}
