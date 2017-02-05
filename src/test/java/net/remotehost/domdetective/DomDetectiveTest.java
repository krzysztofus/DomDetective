package net.remotehost.domdetective;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static net.remotehost.domdetective.config.ConfigProperties.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Christopher on 2/5/2017.
 */
public class DomDetectiveTest {

    private Memory memory = new Memory();

    @Before
    public void persistOriginalConfigVarsValues() {
        memory.store(BASE_DIR_KEY);
        memory.store(CONFIG_DIR_KEY);
        memory.store(CONFIG_FILE_KEY);
        memory.store(TEMPLATE_FILE_KEY);
    }

    @Test
    public void shouldMatchAllRequiredJvmVars() {
        //given
        System.setProperty("baseDir", "baseDir");
        System.setProperty("configDir", "configDir");
        System.setProperty("configFile", "configFile");
        System.setProperty("templateFile", "templateFile");

        //when
        final boolean actual = DomDetective.isConfigured();

        //then
        assertThat(actual).isTrue();
    }

    @After
    public void clearSystemProperties() {
        memory.reset();
    }

    private class Memory {
        private Map<String, String> configProps = new HashMap<>();

        void store(String key) {
            final String property = System.getProperty(key);
            if (property == null) return;

            configProps.put(key, property);
        }

        void reset() {
            for (Map.Entry<String, String> entry : configProps.entrySet()) {
                System.setProperty(entry.getKey(), entry.getValue());
            }
        }
    }

}