package net.remotehost.domdetective.exceptions;

/**
 * Created by Christopher on 2/5/2017.
 */
public class ConfigurationException extends RuntimeException {

    public ConfigurationException() {
    }

    public ConfigurationException(String message) {
        super(message);
    }
}
