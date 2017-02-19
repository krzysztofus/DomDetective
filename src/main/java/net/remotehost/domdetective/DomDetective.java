package net.remotehost.domdetective;

import net.remotehost.domdetective.exceptions.ConfigurationException;
import net.remotehost.domdetective.parser.Template;
import net.remotehost.domdetective.parser.TemplateParser;
import net.remotehost.domdetective.tasks.SearchTask;
import net.remotehost.domdetective.tasks.TaskContext;
import net.remotehost.domdetective.tasks.WriteTask;
import net.remotehost.domdetective.utils.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;

import static net.remotehost.domdetective.config.ConfigProperties.*;
import static net.remotehost.domdetective.parser.PropertiesUtil.printProperties;
import static org.apache.commons.lang3.StringUtils.isNoneBlank;

/**
 * Created by Christopher on 1/24/2017.
 */
public class DomDetective {
    private static final Logger logger = LogManager.getLogger();

    private static final TemplateParser templateParser = new TemplateParser();

    private Properties appProperties;
    private String baseDir;
    private String configDir;
    private String configFile;
    private String templateFile;

    public void execute() {
        logger.info("Verifying config integrity...");
        if (!isConfigured()) {
            throw new IllegalArgumentException(String.format("Require: %s, %s, %s, %s to be set before execution",
                    BASE_DIR_KEY, CONFIG_DIR_KEY, CONFIG_FILE_KEY, TEMPLATE_FILE_KEY));
        }
        readConfig();

        logger.info("Reading appProperties file...");
        final Optional<Properties> propertiesOrNot = FileUtils.readProperties(Paths.get(configDir, configFile).toString());
        if (!propertiesOrNot.isPresent()) {
            throw new ConfigurationException("Properties file is missing");
        }
        appProperties = propertiesOrNot.get();
        printProperties(appProperties);

        logger.info("Reading templates file...");
        final Optional<Properties> templateOrNot = FileUtils.readProperties(Paths.get(baseDir, templateFile).toString());
        if (!templateOrNot.isPresent()) {
            throw new ConfigurationException("Template file is missing!");
        }
        final Properties templates = templateOrNot.get();
        printProperties(templates);

        logger.debug("Parsing templates...");
        final Optional<Template> northOrNot = templateParser.parseTemplate("north", templates);
        if (!northOrNot.isPresent()) {
            throw new RuntimeException("Ups! Something went wrong");
        }
        final Template north = northOrNot.get();

        logger.info("Processing template...");
        processTemplate(north);
        logger.info("Execution successful!");
    }

    public void processTemplate(Template template) {
        final TaskContext context = new TaskContext();

        final SearchTask searchTask = new SearchTask(appProperties, template);
        final WriteTask writeTask = new WriteTask(appProperties, template);

        logger.info("Draining template data...");
        searchTask.execute(context);
        logger.info("Saving output...");
        writeTask.execute(context);
    }

    public static boolean isConfigured() {
        final String baseDir = System.getProperty(BASE_DIR_KEY);
        final String configDir = System.getProperty(CONFIG_DIR_KEY);
        final String configFile = System.getProperty(CONFIG_FILE_KEY);
        final String templateFile = System.getProperty(TEMPLATE_FILE_KEY);

        return isNoneBlank(baseDir, configDir, configFile, templateFile);
    }
    private void readConfig() {
        baseDir = System.getProperty(BASE_DIR_KEY);
        configDir = System.getProperty(CONFIG_DIR_KEY);
        configFile = System.getProperty(CONFIG_FILE_KEY);
        templateFile = System.getProperty(TEMPLATE_FILE_KEY);
    }
}
