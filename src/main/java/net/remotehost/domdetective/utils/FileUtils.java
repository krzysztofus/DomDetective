package net.remotehost.domdetective.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by Christopher on 1/24/2017.
 */
public class FileUtils {
    private static final Logger logger = LogManager.getLogger();

    public static Optional<Properties> readProperties(String path) {
        final Properties properties = new Properties();
        if (isBlank(path)) {
            logger.error("Invalid properties path: " + path);
            return Optional.empty();
        }

        logger.trace("Properties path: " + path);
        try (final InputStream in = new FileInputStream(path)){
            if (in == null) {
                logger.error("Failed to get the resource: " + path);
                return Optional.empty();
            }

            properties.load(in);
        } catch (IOException e) {
            logger.error("Could not load properties!", e);
            return Optional.empty();
        }
        return Optional.of(properties);
    }

}
