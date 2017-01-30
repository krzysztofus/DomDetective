package net.remotehost.domdetective.utils;

import org.junit.Test;

import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Christopher on 1/24/2017.
 */
public class FileManagerTest {

    @Test
    public void shouldReadProperties() {
        //given
        final String testFile = "validTest.properties";

        //when
        final Optional<Properties> properties = FileManager.readProperties(testFile);

        //then
        assertThat(properties).isNotEmpty();
        final String actual = properties.get().getProperty("property.valid", null);
        assertThat(actual).isNotNull().withFailMessage("Should read properties file from: " + testFile);
        assertThat(actual).isEqualTo("works").withFailMessage("property.valid should be read from: " + testFile);
    }

    @Test
    public void shouldNotProcessNullFileName() {
        //given
        final String testFile = null;

        //when
        final Optional<Properties> properties = FileManager.readProperties(testFile);

        //then
        assertThat(properties).isEmpty().withFailMessage("Should not process null pointed test file.");
    }

    @Test
    public void shouldNotProcessEmptyFileName() {
        //given
        final String testFile = "";

        //when
        final Optional<Properties> properties = FileManager.readProperties(testFile);

        //then
        assertThat(properties).isEmpty().withFailMessage("Should not process empty file name test file");
    }

    @Test
    public void shouldNotProcessNonExistentFile() {
        //given
        final String testFile = "nonExistent";

        //when
        final Optional<Properties> properties = FileManager.readProperties(testFile);

        //then
        assertThat(properties).isEmpty().withFailMessage("Should not process non existent file");
    }

}