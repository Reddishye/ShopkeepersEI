package es.redactado.shopkeepersEI.config;

import de.exlll.configlib.Configuration;
import es.redactado.shopkeepersEI.config.partial.MessagesPartial;

@Configuration
public class Config {

    public Boolean isDebug = false;

    public MessagesPartial messages = new MessagesPartial();

}
