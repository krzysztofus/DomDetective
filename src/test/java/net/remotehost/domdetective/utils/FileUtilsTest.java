package net.remotehost.domdetective.utils;

import net.remotehost.domdetective.TestUtils;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Christopher on 1/24/2017.
 */
public class FileUtilsTest extends TestUtils {

    @Test
    public void shouldReadProperties() throws FileNotFoundException {
        //given
        final String testFile = Paths.get(baseDir, "src", "test", "resources", "validTest.properties").toString();

        //when
        final Optional<Properties> properties = FileUtils.readProperties(testFile);

        //then
        assertThat(properties).isNotEmpty();
        final String actual = properties.get().getProperty("property.valid", null);
        assertThat(actual).isNotNull().withFailMessage("Should read properties file from: " + testFile);
        assertThat(actual).isEqualTo("works").withFailMessage("property.valid should be read from: " + testFile);
    }

    @Test
    public void shouldNotProcessNullFileName() throws FileNotFoundException {
        //given
        final String testFile = null;

        //when
        final Optional<Properties> properties = FileUtils.readProperties(testFile);

        //then
        assertThat(properties).isEmpty().withFailMessage("Should not process null pointed test file.");
    }

    @Test
    public void shouldNotProcessEmptyFileName() throws FileNotFoundException {
        //given
        final String testFile = "";

        //when
        final Optional<Properties> properties = FileUtils.readProperties(testFile);

        //then
        assertThat(properties).isEmpty().withFailMessage("Should not process empty file name test file");
    }

    @Test
    public void shouldNotProcessNonExistentFile() throws FileNotFoundException {
        //given
        final String testFile = Paths.get(baseDir, "src", "test", "resources", "nonExistent").toString();

        //when
        final Optional<Properties> properties = FileUtils.readProperties(testFile);

        //then
        assertThat(properties).isEmpty().withFailMessage("Should not process non existent file");
    }

}