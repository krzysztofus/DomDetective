package net.remotehost.domdetective.utils;

import com.google.common.collect.Sets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.Properties;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by Christopher on 1/30/2017.
 */
public class PropertiesParser {
    private static final Logger logger = LogManager.getLogger();

    public static final String DELIMITER = ",";

    public String[] getArray(String name, Properties properties) {
        if (properties == null) {
            throw new IllegalArgumentException("Properties are required!");
        }

        if (isBlank(name)) {
            throw new IllegalArgumentException("Property name is required!");
        }

        final String property = properties.getProperty(name);
        if (isBlank(property)) {
            logger.warn("Property: " + name + " not found!");
            return new String[0];
        }

        if (property.contains(DELIMITER)) {
            final String[] split = property.split(DELIMITER);
            return split;
        } else {
            logger.warn("Property key: " + name + "contains just one value");
            return new String[]{property};
        }
    }

    public Optional<Set<String>> getSet(Properties properties, String name) {
        final String[] values = getArray(name, properties);
        return Optional.ofNullable(Sets.newHashSet(values));
    }
}
