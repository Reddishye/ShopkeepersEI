package es.redactado.shopkeepersEI.config;

import com.google.inject.Singleton;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class ConfigMapper {
    private final Map<Class<?>, ConfigContainer<?>> configs = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public <C> Optional<ConfigContainer<C>> get(Class<C> clazz) {
        return Optional.ofNullable((ConfigContainer<C>) configs.get(clazz));
    }

    public <C> void register(Class<C> clazz, ConfigContainer<C> config) {
        configs.put(clazz, config);
    }

    public void reload() {
        configs.values().parallelStream().forEach(ConfigContainer::reload);
    }
}