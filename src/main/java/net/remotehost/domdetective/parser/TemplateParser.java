package net.remotehost.domdetective.parser;

import net.remotehost.domdetective.utils.PropertiesParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static net.remotehost.domdetective.utils.Strings.isAnyBlank;

/**
 * Created by Christopher on 1/30/2017.
 */
public class TemplateParser {
    private static final Logger logger = LogManager.getLogger();

    public static final String TEMPLATE_LIST_NAME = "templates";
    public static final String TEMPLATE_URL = "url";
    public static final String TEMPLATE_SEARCH_PATTERN = "search.pattern";
    public static final String TEMPLATE_NESTING_PATTERN = "nesting.pattern";

    private final PropertiesParser parser;

    public TemplateParser(PropertiesParser parser) {
        this.parser = parser;
    }

    public TemplateParser() {
        this.parser = new PropertiesParser();
    }

    public Optional<List<Template>> parseTemplates(Properties properties) {
        if (properties == null) {
            throw new IllegalArgumentException("Properties are required!");
        }

        final Optional<String[]> cases = parser.getArray(properties, TEMPLATE_LIST_NAME, ",");
        if (!cases.isPresent()) {
            logger.warn("Could not find any templates");
            return Optional.empty();
        }

        List<Template> templates = new ArrayList<>();
        for (String name : cases.get()) {
            final Optional<Template> template = parseTemplate(name, properties);
            template.ifPresent(templates::add);
        }

        return (templates.size() == 0) ? Optional.empty() : Optional.of(templates);
    }

    public Optional<String[]> getTemplatesNames(Properties properties) {
        if (properties == null) {
            throw new IllegalArgumentException("Properties are required!");
        }

        return parser.getArray(properties, TEMPLATE_LIST_NAME, ",");
    }

    public Optional<Template> parseTemplate(String name, Properties properties) {
        if (properties == null) {
            throw new IllegalArgumentException("Properties are required!");
        }

        final String url = properties.getProperty(name + TEMPLATE_URL);
        final String searchPattern = properties.getProperty(name + TEMPLATE_SEARCH_PATTERN);
        final String nestingPattern = properties.getProperty(name + TEMPLATE_NESTING_PATTERN);

        if (isAnyBlank(url, searchPattern, nestingPattern)) {
            logger.error("Malformed template for case: " + name);
            return Optional.empty();
        }

        return Optional.of(new Template(name, url, searchPattern, nestingPattern));
    }
}
