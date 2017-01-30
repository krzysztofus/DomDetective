package net.remotehost.domdetective.parser;

import net.remotehost.domdetective.utils.PropertiesParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by Christopher on 1/30/2017.
 */
public class TemplateParser {
    private static final Logger logger = LogManager.getLogger();

    public Optional<List<Case>> parseCases(Properties properties) {
        if (properties == null) {
            throw new IllegalArgumentException("Properties are required!");
        }

        final Optional<String[]> cases = PropertiesParser.getArray(properties,"cases", ",");
        if (cases.isPresent()) {
            
        }
        return null;
    }

    public Case parseCase(String caseName, Properties properties) {
        return null;
    }
}
