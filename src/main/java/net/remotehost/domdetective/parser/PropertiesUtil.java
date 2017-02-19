package net.remotehost.domdetective.parser;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by Christopher on 1/30/2017.
 */
public class PropertiesUtil {
    private static final Logger logger = LogManager.getLogger();

    public static final String DELIMITER = ",";

    public static int getIntegerProperty(Properties properties, String key, int defaultValue) {
        final Optional<Integer> integerProperty = getIntegerProperty(properties, key);
        if (integerProperty.isPresent()) {
            return integerProperty.get();
        } else {
            return defaultValue;
        }
    }

    public static Optional<Integer> getIntegerProperty(Properties properties, String key) {
        basicInputCheck(key, properties);

        final String property = properties.getProperty(key);
        if (isBlank(property)) {
            logger.warn("Property: " + key + "not found!");
            return Optional.empty();
        }

        try {
            return Optional.of(Integer.valueOf(property));
        } catch (NumberFormatException e) {
            logger.warn("Cannot parse property as Integer!", e);
            return Optional.empty();
        }
    }

    public static String[] getArray(String key, Properties properties) {
        basicInputCheck(key, properties);

        final String property = properties.getProperty(key);
        if (isBlank(property)) {
            logger.warn("Property: " + key + " not found!");
            return new String[0];
        }

        if (property.contains(DELIMITER)) {
            final String[] split = property.split(DELIMITER);
            return split;
        } else {
            logger.warn("Property key: " + key + "contains just one value");
            return new String[]{property};
        }
    }

    private static void basicInputCheck(String key, Properties properties) {
        if (properties == null) {
            throw new IllegalArgumentException("Properties are required!");
        }

        if (isBlank(key)) {
            throw new IllegalArgumentException("Property key is required!");
        }
    }

    public static Optional<Set<String>> getSet(String key, Properties properties) {
        final String[] values = getArray(key, properties);
        return Optional.ofNullable(Sets.newHashSet(values));
    }

    public static List<String> getList(String key, Properties properties) {
        final String[] values = getArray(key, properties);
        return Lists.newArrayList(values);
    }

    public static void printProperties(Properties properties) {
        final Set<Map.Entry<Object, Object>> entries = properties.entrySet();
        logger.debug("Printing properties entries:");
        for (Map.Entry entry : entries) {
            logger.debug(entry.getKey() + "=" + entry.getValue());
        }
        logger.debug("Printing done. No more entries");
    }
}
