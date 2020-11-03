package de.fyreum.dreships.data;

import de.gleyder.umbrella.core.storage.config.ConfigurationValue;
import de.gleyder.umbrella.core.storage.config.StaticValueClass;

@StaticValueClass(rootName = "settings")
public class ConfigLocalization {

    public static ConfigurationValue AIRSHIP_PRICE_DISTANCE_MULTIPLIER;

    public static ConfigurationValue LAND_PRICE_DISTANCE_MULTIPLIER;

    public static ConfigurationValue SHIP_PRICE_DISTANCE_MULTIPLIER;

    public static ConfigurationValue START_PRICE;

    public static class permissions {

        public static ConfigurationValue ALL;

        public static ConfigurationValue DS_CREATE;

        public static ConfigurationValue DS_DELETE;

        public static ConfigurationValue DS_HELP;

        public static ConfigurationValue DS_INFO;

        public static ConfigurationValue DS_SAVE;

        public static ConfigurationValue DS_TELEPORT;

    }
}
