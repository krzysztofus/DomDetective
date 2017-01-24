package net.remotehost.domdetective.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Christopher on 1/24/2017.
 */
public class FileManager {

    private static final Logger

    public static Properties readProperties(String path) {
        final Properties properties = new Properties();
        final InputStream in = FileManager.class.getResourceAsStream(path);
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

}
