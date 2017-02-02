package net.remotehost.domdetective.utils;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.Properties;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Christopher on 1/30/2017.
 */
public class PropertiesParserTest {
    
    private PropertiesParser tested;
    
    @Before
    public void setUpTested(){
        tested = new PropertiesParser();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotParserNullProperties() {
        //given
        final Properties input = null;

        //when
        tested.getArray("name", input);

        //then should throw IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotParserBlankPropertyName() {
        //given
        final Properties input = new Properties();

        //when
        tested.getArray("", input);

        //then should throw IllegalArgumentException
    }

    @Test
    public void shouldParseValidPropertyOnDefaultDelimiter() {
        //given
        final Properties input = createDummyProperties("key", "test,values");

        //when
        final String[] actual = tested.getArray("key", input);

        //then
        assertThat(actual).isNotEmpty();
        assertThat(actual).contains(new String[]{"test", "values"});
    }

    @Test
    public void shouldReturnEmptyOptionalWhenNoPropertyWasFound() {
        //given
        final Properties input = createDummyProperties("Will", "not,look,for,that");

        //when
        final String[] actual = tested.getArray("test", input);

        //then
        assertThat(actual).isEmpty();
    }

    @Test
    public void shouldReturnSingleElementWhenPropertyIsNotAnArray() {
        //given
        final Properties input = createDummyProperties("key", "singleValue");

        //when
        final String[] actual = tested.getArray("key", input);

        //then
        assertThat(actual).isNotEmpty();
        assertThat(actual).contains(new String[]{"singleValue"});
    }

    @Test
    public void shouldGetSet() {
        //given
        final Properties input = createDummyProperties("key", "test,values");

        //when
        final Optional<Set<String>> actual = tested.getSet(input, "key");

        //then
        assertThat(actual).isNotEmpty();
        assertThat(actual).contains(Sets.newHashSet("test", "values"));
    }

    private Properties createDummyProperties(String key, String value) {
        final Properties properties = new Properties();
        properties.setProperty(key, value);
        return properties;
    }

}