package net.remotehost.domdetective.tasks;

import net.remotehost.domdetective.parser.PropertiesUtil;
import net.remotehost.domdetective.parser.Template;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

/**
 * Created by Christopher on 2/11/2017.
 */
public class SearchTask implements Task {
    private static final Logger logger = LogManager.getLogger();

    public static final String JSOUP_LANGUAGE_KEY = "jsuop.language";
    public static final String JSOUP_USER_AGENT_KEY = "jsoup.userAgent";
    public static final String JSOUP_CONNECTION_TIMEOUT = "jsoup.timeout";

    private final Properties properties;
    private final Template template;

    public SearchTask(Properties properties, Template template) {
        this.properties = properties;
        this.template = template;
    }

    @Override
    public void execute() {
        if (template == null) {
            throw new IllegalArgumentException("Valid template is required!");
        }

        try {
            final Document document = getDocument(template.getUrl());
            recursiveSearch(document);
        } catch (IOException e) {
            logger.error("Failed to connect!\n" + template.toString());
        }
    }

    private void recursiveSearch(Document document) throws IOException {
        final Elements elements = document.select(template.getSearchPattern());
        logger.debug(String.format("Found %s elements matching pattern: %s", elements.size(), template.getSearchPattern()));
        for (Element element : elements) {
            parseElement(element, template.getOutputPattern());
        }

        final Optional<Document> next = getNext(document, template.getRecurrencePattern());
        if (next.isPresent()) {
            recursiveSearch(next.get());
        }
    }

    private Document getDocument(String url) throws IOException {
        final Optional<Integer> timeoutOrNot = PropertiesUtil.getIntegerProperty(JSOUP_CONNECTION_TIMEOUT, properties);
        final Document document = Jsoup.connect(url)
                .data("language", properties.getProperty(JSOUP_LANGUAGE_KEY))
                .userAgent(properties.getProperty(JSOUP_USER_AGENT_KEY))
                .timeout(timeoutOrNot.isPresent() ? timeoutOrNot.get() : 3000)
                .get();
        logger.debug("Acquired document:\n" + document.title());
        return document;
    }

    private void parseElement(Element element, String[] cssQueries) {
        for (String query : cssQueries) {
            System.out.println(element.select(query).text());
        }
    }

    private Optional<Document> getNext(Document document, String nestingPattern) throws IOException {
        final Elements nested = document.select(nestingPattern);

        switch (nested.size()) {
            case 0:
                return Optional.empty();
            case 1:
                final String nestedUrl = nested.get(0).text();
                final boolean isUrl = UrlValidator.getInstance().isValid(nestedUrl);
                if (!isUrl) {
                logger.warn("Configured css query does not point to a valid next page pointer!");
                    return Optional.empty();
                }

                return Optional.of(getDocument(nestedUrl));
            default:
                logger.warn("Expecting only 1 pointer to next page. Instead received: " + nested.size());
                return Optional.empty();
        }
    }
}