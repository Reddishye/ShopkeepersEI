package es.redactado.shopkeepersEI.config;

import lombok.Getter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@Getter
@ConfigSerializable
public class Config {

    @Setting
    public Boolean isDebug = false;

    @Setting
    public MessagesConfig messages = new MessagesConfig();

    @Getter
    @ConfigSerializable
    public static class MessagesConfig {

        @Setting
        public String prefix = "<#eb64f8><bold>ShopkeepersEI<reset> <dark_gray>» ";

        @Setting
        public String pluginReloaded = "&aPlugin reloaded!";
    }
}