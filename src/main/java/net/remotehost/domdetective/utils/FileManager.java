package net.remotehost.domdetective.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by Christopher on 1/24/2017.
 */
public class FileManager {
    private static final Logger logger = LogManager.getLogger();

    public static Properties readProperties(String fileName) {
        final Properties properties = new Properties();
        if (isBlank(fileName)) {
            logger.error("Invalid properties fileName: " + fileName);
            return properties;
        }

        logger.trace("Properties fileName: " + fileName);
        final InputStream in = FileManager.class.getClassLoader().getResourceAsStream(fileName);
        if (in == null) {
            logger.error("Failed to get the resource: " + fileName);
            return properties;
        }

        try {
            properties.load(in);
        } catch (IOException e) {
            logger.error("Could not load properties!", e);
        }
        return properties;
    }

}
