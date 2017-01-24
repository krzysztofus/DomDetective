package net.remotehost.domdetective.utils;

import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Christopher on 1/24/2017.
 */
public class FileManagerTest {

    @Test
    public void shouldReadProperties() {
        //given
        final String testFile = "validTest.properties";

        //when
        final Properties properties = FileManager.readProperties(testFile);

        //then
        final String actual = properties.getProperty("property.valid", null);
        assertNotNull("Should read properties file from: " + testFile, actual);
        assertEquals("property.valid should be read from: " + testFile, "works", actual);
    }

    @Test
    public void shouldNotProcessNullFileName() {
        //given
        final String testFile = null;

        //when
        final Properties properties = FileManager.readProperties(testFile);

        //then
        assertEquals("Properties should be empty.", 0, properties.size());
    }

    @Test
    public void shouldNotProcessEmptyFileName() {
        //given
        final String testFile = "";

        //when
        final Properties properties = FileManager.readProperties(testFile);

        //then
        assertEquals("Properties should be empty.", 0, properties.size());
    }

    @Test
    public void shouldNotProcessNonExistentFile() {
        //given
        final String testFile = "nonExistent";

        //when
        final Properties properties = FileManager.readProperties(testFile);

        //then
        assertEquals("Properties should be empty.", 0, properties.size());
    }

}