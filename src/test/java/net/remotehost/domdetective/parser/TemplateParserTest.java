package net.remotehost.domdetective.parser;

import com.google.common.collect.Lists;
import net.remotehost.domdetective.utils.PropertiesParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

/**
 * Created by Christopher on 1/31/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class TemplateParserTest {

    private TemplateParser tested;

    @Mock
    private PropertiesParser propertiesParser;

    @Before
    public void setUp() {
        tested = new TemplateParser();
        given(propertiesParser.getArray(any(), anyString(), anyString()))
                .willReturn(Optional.of(new String[]{"something", "else", "misconfiguration"}));
    }

    @Test
    public void shouldGetTemplatesNames() {
        //given
        final Properties input = createDummy();

        //when
        final Optional<String[]> actual = tested.getTemplatesNames(input);

        //then
        assertThat(actual).isPresent();
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
                "<div id=\"bajo\">*</div>", "<button id=\"bajo\">*</button>"));
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
                new Template("something", "http://something.com",
                        "<div id=\"bajo\">*</div>", "<button id=\"bajo\">*</button>"),
                new Template("else", "http://else.com",
                        "<div id=\"jajo\">*</div>", "<button id=\"jajo\">*</button>"));
        assertThat(actual).contains(testTemplates);
    }

    private Properties createDummy() {
        final Properties properties = new Properties();
        properties.setProperty("cases", "something,else,misconfiguration");
        properties.setProperty("something.url", "http://something.com");
        properties.setProperty("something.search.pattern", "<div id=\"bajo\">*</div>");
        properties.setProperty("something.nesting.pattern", "<button id=\"bajo\">*</button>");

        properties.setProperty("else.url", "http://else.com");
        properties.setProperty("else.search.pattern", "<div id=\"jajo\">*</div>");
        properties.setProperty("else.nesting.pattern", "<button id=\"jajo\">*</button>");
        return properties;
    }

}