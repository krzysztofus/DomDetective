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

    public Optional<String[]> getArray(Properties properties, String name, String delimiter) {
        if (properties == null) {
            throw new IllegalArgumentException("Properties are required!");
        }

        if (isBlank(name)) {
            throw new IllegalArgumentException("Property name is required!");
        }

        if (isBlank(delimiter)) {
            logger.debug("Blank delimiter. Defaulting to ','");
            delimiter = ",";
        }

        final String property = properties.getProperty(name);
        if (isBlank(property)) {
            logger.warn("Property: " + name + " not found!");
            return Optional.empty();
        }

        if (property.contains(delimiter)) {
            return Optional.of(property.split(delimiter));
        } else {
            logger.warn("Property key: " + name + "contains just one value");
            return Optional.of(new String[]{property});
        }
    }

    public Optional<Set<String>> getSet(Properties properties, String name, String delimiter) {
        final Optional<String[]> values = getArray(properties, name, delimiter);
        return Optional.ofNullable(Sets.newHashSet(values.get()));
    }
}
