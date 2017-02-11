package net.remotehost.domdetective.tasks;

import net.remotehost.domdetective.parser.Template;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Christopher on 2/11/2017.
 */
public class SearchTask implements Task {
    private static final Logger logger = LogManager.getLogger();

    private final Template template;

    public SearchTask(Template template) {
        this.template = template;
    }

    @Override
    public void execute() {
        if (template == null) {
            throw new IllegalArgumentException("Valid template is required!");
        }

        try {
            final Document document = Jsoup.connect(template.getUrl())
                    .data("language", "English")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36")
                    .timeout(3000)
                    .get();
            logger.debug("Acquired document:\n" + document.title());

            final Elements elements = document.select(template.getSearchPattern());
            logger.debug(String.format("Found %s elements matching pattern: %s", elements.size(), template.getSearchPattern()));
            for (Element element : elements) {
                parseElement(element, template.getOutputPattern());
            }
        } catch (IOException e) {
            logger.error("Failed to connect!\n" + template.toString());
        }
    }

    private void parseElement(Element element, String[] cssQueries) {
        for (String query : cssQueries) {
            System.out.println(element.select(query).text());
        }
    }
}
