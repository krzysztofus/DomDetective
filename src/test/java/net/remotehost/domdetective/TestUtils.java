package net.remotehost.domdetective;

/**
 * Created by Christopher on 2/5/2017.
 */
public class TestUtils {

    protected final String baseDir;

    public TestUtils() {
        this.baseDir = System.getProperty("baseDir", null);
        if (baseDir == null) {
            throw new IllegalArgumentException("-DbaseDir JVM variable is not set!");
        }
    }
}
