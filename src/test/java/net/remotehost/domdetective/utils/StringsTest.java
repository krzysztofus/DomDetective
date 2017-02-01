package net.remotehost.domdetective.utils;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Christopher on 2/1/2017.
 */
public class StringsTest {

    @Test
    public void shouldTellNoStringIsBlank() {
        //when
        final boolean actual = Strings.isNoneBlank("no", "string", "is", "blank");

        //then
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldTellThatSomeStringsAreNull() {
        //when
        final boolean actual = Strings.isNoneBlank("this", "string", "is", null);

        //then
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldTellThatSomeStringsAreEmpty() {
        //when
        final boolean actual = Strings.isNoneBlank("this", "string", "is", "");

        //then
        assertThat(actual).isFalse();
    }

}