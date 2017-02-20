package net.remotehost.domdetective.parser;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Christopher on 1/31/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class TemplateParserTest {

    private TemplateParser tested;

    @Before
    public void setUp() {
        tested = new TemplateParser();
    }

    @Test
    public void shouldGetTemplatesNames() {
        //given
        final Properties input = createDummy();

        //when
        final String[] actual = tested.getTemplatesNames(input);

        //then
        assertThat(actual).isNotEmpty();
        assertThat(actual).contains(new String[]{"something", "else", "misconfiguration"});
    }

    @Test
    public void shouldParseValidTemplate() throws MalformedURLException {
        //given
        final Properties input = createDummy();

        //when
        final Optional<Template> actual = tested.parseTemplate("something", input);

        //then
        assertThat(actual).isPresent();
        assertThat(actual).contains(new Template("something", "http://something.com",
                "div.bajo", Arrays.asList("div.class"), "button.bajo"));
    }

    @Test
    public void shouldNotParseInvalidTemplate() {
        //given
        final Properties input = createDummy();

        //when
        final Optional<Template> actual = tested.parseTemplate("misconfiguration", input);

        //then
        assertThat(actual).isEmpty();
    }

    @Test
    public void shouldParseMultipleValidTemplate() throws MalformedURLException {
        //given
        final Properties input = createDummy();

        //when
        final Optional<List<Template>> actual = tested.parseTemplates(input);

        //then
        assertThat(actual).isPresent();
        final List<Template> testTemplates = Lists.newArrayList(
                new Template("something", "http://something.com", "div.bajo",
                        Arrays.asList("div.class"), "button.bajo"),
                new Template("else", "http://else.com", "div.jajo",
                        Arrays.asList("div.class"), "button.jajo"));
        assertThat(actual).contains(testTemplates);
    }

    private Properties createDummy() {
        final Properties properties = new Properties();
        properties.setProperty("templates", "something;else;misconfiguration");
        properties.setProperty("something.url", "http://something.com");
        properties.setProperty("something.search.pattern", "div.bajo");
        properties.setProperty("something.output.pattern", "div.class");
        properties.setProperty("something.recurrence.pattern", "button.bajo");

        properties.setProperty("else.url", "http://else.com");
        properties.setProperty("else.search.pattern", "div.jajo");
        properties.setProperty("else.output.pattern", "div.class");
        properties.setProperty("else.recurrence.pattern", "button.jajo");
        return properties;
    }

}