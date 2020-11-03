package de.fyreum.dreships.data;

import de.gleyder.umbrella.core.storage.config.StaticValueClass;
import de.gleyder.umbrella.core.storage.localization.Localization;

@StaticValueClass(valueName = "translation", rootName = "lang")
public class LangLocalization {

    public static class descriptions {

        public static Localization DS;

    }

    public static class warns {

        public static Localization POSSIBLE_SUFFOCATION;

        public static Localization VAULT_IS_MISSING;

    }

    public static class commands {

        public static Localization CACHE_IS_EMPTY;

        public static Localization DS_CREATE_START;

        public static Localization DS_CREATE_SUCCESS;

        public static Localization DS_DELETE_SUCCESS;

        public static Localization DS_HELP;

        public static Localization DS_INFO_IS_TRAVEL_SIGN;

        public static Localization DS_INFO_NO_TRAVEL_SIGN;

        public static Localization DS_SAVE_ALREADY_TRAVEL_SIGN;

        public static Localization DS_SAVE_SUCCESS;

        public static Localization DS_TELEPORT_SUGGESTION;

        public static Localization PRICE_NULL_OR_NEGATIVE;

        public static Localization TARGET_BLOCK_IS_NO_SIGN;

        public static Localization TARGET_BLOCK_IS_NO_TRAVEL_SIGN;

    }

    public static class info {

        public static Localization BREAK_DENIED;

        public static Localization TP_SUCCESS;

    }

    public static class sign {

        public static Localization NOT_ENOUGH_MONEY;

        public static Localization SIGN_LINE_ONE;

        public static Localization SIGN_LINE_TWO;

        public static Localization SIGN_LINE_THREE;

        public static Localization SIGN_LINE_FOUR;

    }
}
