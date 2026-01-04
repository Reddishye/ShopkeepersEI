package es.redactado.shopkeepersEI.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import es.redactado.shopkeepersEI.Logger;
import es.redactado.shopkeepersEI.config.partial.MessagesPartial;

@Configuration
public class Config {

    @Comment("Available values: TRACE, DEBUG, INFO, SUCCESS, WARN, ERROR, FATAL")
    public Logger.LogLevel logLevel = Logger.LogLevel.INFO;

    public MessagesPartial messages = new MessagesPartial();
}
