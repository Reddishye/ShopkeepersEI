package es.redactado.shopkeepersEI.utils;

import java.util.regex.Pattern;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class ColorTranslator {
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    private static final Pattern REMOVE_UNUSED_DECIMALS_PATTERN =
            Pattern.compile("%!REMOVE_UNUSED_DECIMALS:(\\d+\\.?\\d*)!%");
    private static final Pattern REMOVE_ALL_DECIMALS_PATTERN =
            Pattern.compile("%!REMOVE_ALL_DECIMALS:(\\d+\\.?\\d*)!%");
    private static final String[] COLOR_CODES = {
        "&0", "&1", "&2", "&3", "&4", "&5", "&6", "&7",
        "&8", "&9", "&a", "&b", "&c", "&d", "&e", "&f",
        "&k", "&l", "&m", "&n", "&o", "&r"
    };
    private static final String[] COLOR_REPLACEMENTS = {
        "<black>", "<dark_blue>", "<dark_green>", "<dark_aqua>", "<dark_red>", "<dark_purple>",
                "<gold>", "<grey>",
        "<dark_grey>", "<blue>", "<green>", "<aqua>", "<red>", "<light_purple>", "<yellow>",
                "<white>",
        "<obf>", "<b>", "<st>", "<u>", "<i>", "<reset>"
    };

    public static String toMM(String str) {
        str = str.replace("§", "&");
        for (int i = 0; i < COLOR_CODES.length; i++) {
            str = str.replace(COLOR_CODES[i], COLOR_REPLACEMENTS[i]);
        }
        return HEX_PATTERN.matcher(str).replaceAll("<#$1>");
    }

    public static String replaceStandardPlaceholders(String message) {
        message =
                REMOVE_UNUSED_DECIMALS_PATTERN
                        .matcher(message)
                        .replaceAll(mr -> mr.group(1).replaceAll("\\.0+$", ""));
        message =
                REMOVE_ALL_DECIMALS_PATTERN
                        .matcher(message)
                        .replaceAll(mr -> mr.group(1).split("\\.")[0]);
        return message;
    }

    public static Component translate(String message) {
        return MiniMessage.miniMessage().deserialize(toMM(replaceStandardPlaceholders(message)));
    }
}
