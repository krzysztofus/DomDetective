package net.remotehost.domdetective.tasks;

import com.sun.rowset.internal.Row;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Christopher on 2/19/2017.
 */
public final class TaskContext {

    public static final String DELIMITER = ",";

    private final List<OutputRow> rows = new ArrayList<>();

    public void addRow(OutputRow row) {
        rows.add(row);
    }

    public List<OutputRow> getRows() {
        return rows;
    }

    public List<String> getFormattedRows() {
        return rows.stream().map(row -> String.join(DELIMITER, row)).collect(Collectors.toList());
    }

}
