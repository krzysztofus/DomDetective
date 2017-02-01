package net.remotehost.domdetective.utils;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by Christopher on 2/1/2017.
 */
public abstract class Strings {

    public static boolean isAnyBlank(String... strings) {
        for (String string : strings) {
            if (isBlank(string)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isNoneBlank(String... strings) {
        return !isAnyBlank(strings);
    }
}
