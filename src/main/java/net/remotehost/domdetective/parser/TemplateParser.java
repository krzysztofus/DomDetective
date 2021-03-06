package net.remotehost.domdetective.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.isAnyBlank;

/**
 * Created by Christopher on 1/30/2017.
 */
public class TemplateParser {
    private static final Logger logger = LogManager.getLogger();

    public static final String TEMPLATE_LIST_NAME = "templates";
    public static final String TEMPLATE_URL = ".url";
    public static final String TEMPLATE_SEARCH_PATTERN = ".search.pattern";
    public static final String TEMPLATE_OUTPUT_PATTERN = ".output.pattern";
    public static final String TEMPLATE_RECURRENCE_PATTERN = ".recurrence.pattern";

    private final PropertiesUtil parser;

    public TemplateParser(PropertiesUtil parser) {
        this.parser = parser;
    }

    public TemplateParser() {
        this.parser = new PropertiesUtil();
    }

    public Optional<List<Template>> parseTemplates(Properties rawTemplates) {
        if (rawTemplates == null) {
            throw new IllegalArgumentException("Properties are required!");
        }

        final String[] cases = parser.getArray(TEMPLATE_LIST_NAME, rawTemplates);
        if (cases.length == 0) {
            logger.warn("Could not find any templates");
            return Optional.empty();
        }

        List<Template> templates = new ArrayList<>();
        for (String name : cases) {
            final Optional<Template> template = parseTemplate(name, rawTemplates);
            template.ifPresent(templates::add);
        }

        return (templates.size() == 0) ? Optional.empty() : Optional.of(templates);
    }

    public String[] getTemplatesNames(Properties properties) {
        if (properties == null) {
            throw new IllegalArgumentException("Properties are required!");
        }

        return parser.getArray(TEMPLATE_LIST_NAME, properties);
    }

    public Optional<Template> parseTemplate(String name, Properties properties) {
        if (properties == null) {
            throw new IllegalArgumentException("Properties are required!");
        }

        final String url = properties.getProperty(name + TEMPLATE_URL);
        final String searchPattern = properties.getProperty(name + TEMPLATE_SEARCH_PATTERN);
        final List<String> outputPattern = parser.getList(name + TEMPLATE_OUTPUT_PATTERN, properties);
        final String recurrencePattern = properties.getProperty(name + TEMPLATE_RECURRENCE_PATTERN);

        if (isAnyBlank(url, searchPattern, recurrencePattern)) {
            logger.error("Malformed template for case: " + name);
            return Optional.empty();
        }

        final Template template = new Template(name, url, searchPattern, outputPattern, recurrencePattern);
        logger.debug("Parsed template:\n" + template.toString());
        return Optional.of(template);
    }
}
