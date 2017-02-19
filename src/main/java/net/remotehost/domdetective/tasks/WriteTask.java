package net.remotehost.domdetective.tasks;

import net.remotehost.domdetective.parser.Template;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Created by Christopher on 2/19/2017.
 */
public class WriteTask implements Task {

    private final Properties properties;
    private final Template template;

    public WriteTask(Properties properties, Template template) {
        this.properties = properties;
        this.template = template;
    }

    @Override
    public void execute(TaskContext context) {
        final List<String> formattedRows = context.getFormattedRows();
        try (final FileWriter writer = new FileWriter(template.getName() + ".csv")) {
            for (String row : formattedRows) {
                writer.append(row).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
