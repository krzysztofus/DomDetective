package net.remotehost.domdetective.tasks;

import com.sun.org.apache.xpath.internal.XPath;
import net.remotehost.domdetective.parser.PropertiesUtil;
import net.remotehost.domdetective.parser.Template;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.xsoup.Xsoup;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by Christopher on 2/11/2017.
 */
public class SearchTask implements Task {
    private static final Logger logger = LogManager.getLogger();

    public static final String JSOUP_LANGUAGE_KEY = "jsuop.language";
    public static final String JSOUP_USER_AGENT_KEY = "jsoup.userAgent";
    public static final String JSOUP_CONNECTION_TIMEOUT = "jsoup.timeout";
    public static final String JSOUP_MAX_RECCURENCE = "jsoup.maxRecurrenceCount";

    private final Properties properties;
    private final Template template;

    public SearchTask(Properties properties, Template template) {
        this.properties = properties;
        this.template = template;
    }

    @Override
    public void execute(TaskContext context) {
        if (template == null) {
            throw new IllegalArgumentException("Valid template is required!");
        }

        final int maxRecurrenceCount = PropertiesUtil.getIntegerProperty(properties, JSOUP_MAX_RECCURENCE, 100);
        try {
            final Document document = getDocument(template.getUrl());
            recursiveSearch(context, document, maxRecurrenceCount);
        } catch (IOException e) {
            logger.error("Failed to connect!\n" + template.toString());
        }
    }

    public void recursiveSearch(TaskContext context, Document document, int recurrenceCounter) throws IOException {
        final Elements elements = document.select(template.getSearchPattern());
        logger.debug(String.format("Found %s elements matching pattern: %s", elements.size(), template.getSearchPattern()));
        for (Element element : elements) {
            context.addRow(parseElement(element, template.getOutputPattern()));
        }

        final Optional<Document> next = getNext(document, template.getRecurrencePattern());
        if (next.isPresent() && recurrenceCounter > 0) {
            recursiveSearch(context, next.get(), --recurrenceCounter);
        }
    }

    private Document getDocument(String url) throws IOException {
        final int timeout = PropertiesUtil.getIntegerProperty(properties, JSOUP_CONNECTION_TIMEOUT, 3000);
        final Document document = Jsoup.connect(url)
                .data("language", properties.getProperty(JSOUP_LANGUAGE_KEY))
                .userAgent(properties.getProperty(JSOUP_USER_AGENT_KEY))
                .timeout(timeout)
                .get();
        logger.debug("Acquired document:\n" + document.title());
        return document;
    }

    private OutputRow parseElement(Element element, List<String> xPathQueries) {
        return xPathQueries.stream().map(query -> Xsoup.compile(query).evaluate(element).get()).collect(Collectors.toCollection(OutputRow::new));
    }

 /*   private OutputRow parseElement(Element element, List<String> xPathQueries) throws XPathExpressionException {
        final XPath xPath = XPathFactory.newInstance().newXPath();
        return xPathQueries.stream().map(query -> {
            try {
                return xPath.compile(query).evaluate(element);
            } catch (XPathExpressionException e) {
                return "XPATH_QUERY_FAILED";
            }
        }).collect(Collectors.toCollection(OutputRow::new));
    }*/

    private Optional<Document> getNext(Document document, String nestingPattern) throws IOException {
        final String nestedUrl = document.select(nestingPattern).attr("href");

        if (isBlank(nestedUrl)) {
            logger.debug("Did not find a valid pointer to next document. It usually means search ended on last element.");
            return Optional.empty();
        }

        final boolean isUrl = UrlValidator.getInstance().isValid(nestedUrl);
        if (!isUrl) {
            logger.warn("Configured css query does not point to a valid next page pointer!");
            return Optional.empty();
        }

        return Optional.of(getDocument(nestedUrl));
    }
}