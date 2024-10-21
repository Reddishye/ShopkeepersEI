package es.redactado.shopkeepersEI.config;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.atomic.AtomicReference;

@Singleton
public class ConfigContainer<C> {
    private final AtomicReference<C> config;
    private final YamlConfigurationLoader loader;
    private final Class<C> clazz;
    private final String fileName;

    @Inject
    private ConfigContainer(C config, Class<C> clazz, YamlConfigurationLoader loader, String fileName) {
        this.config = new AtomicReference<>(config);
        this.loader = loader;
        this.clazz = clazz;
        this.fileName = fileName;
    }

    public C get() {
        return config.get();
    }

    public CompletableFuture<Void> reload() {
        return CompletableFuture.runAsync(() -> {
            try {
                config.set(loader.load().get(clazz));
            } catch (ConfigurateException e) {
                throw new CompletionException("Could not load " + fileName + " file", e);
            }
        });
    }

    public CompletableFuture<Void> save() {
        return CompletableFuture.runAsync(() -> {
            try {
                CommentedConfigurationNode node = loader.load();
                node.set(clazz, config.get());
                loader.save(node);
            } catch (ConfigurateException e) {
                throw new CompletionException("Could not save " + fileName + " file", e);
            }
        });
    }

    public static <C> ConfigContainer<C> load(Path path, Class<C> clazz) throws IOException {
        return load(path, clazz, "config.yml");
    }

    public static <C> ConfigContainer<C> load(Path path, Class<C> clazz, String fileName) throws IOException {
        path = path.resolve(fileName);
        boolean firstCreation = Files.notExists(path);
        YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .nodeStyle(NodeStyle.BLOCK)
                .indent(2)
                .defaultOptions(opts -> opts.shouldCopyDefaults(true).header("""
                        This is the main configuration file for the plugin, please report any issues to the GitHub repository. (https://github.com/Reddishye/ShopkeepersEI)
                        """))
                .path(path)
                .build();

        CommentedConfigurationNode node = loader.load();
        C config = node.get(clazz);

        if (firstCreation) {
            node.set(clazz, config);
            loader.save(node);
        }

        return new ConfigContainer<>(config, clazz, loader, fileName);
    }
}