package com.example.uitests.swingdv.doc.text;

import de.ganzer.core.io.CsvInputStreamReader;
import de.ganzer.core.io.CsvOutputStreamWriter;
import de.ganzer.core.util.Strings;
import de.ganzer.dv.*;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CSVDocument extends Document {
    public static class ChangeContext {
        public final int row;
        public final int column;

        public ChangeContext(int row, int column) {
            this.row = row;
            this.column = column;
        }
    }

    private List<List<String>> data;
    private String encoding;

    public CSVDocument(DocumentCreationInfo<? extends Document> info) throws DVLoadException {
        super(info);
    }

    public int getRowCount() {
        return data.size();
    }

    public int getColumnCount() {
        return data.isEmpty() ? 0 : data.get(0).size();
    }

    public String getValue(int row, int column) {
        if (row < 0 || row >= data.size())
            return null;

        var line = data.get(row);

        if (column < 0 || column >= line.size())
            return null;

        return line.get(column);
    }

    public void setValue(String value, int row, int column, View<?> originator) {
        if (row < 0 || column < 0)
            return;

        if (Strings.isNullOrEmpty(value) && (row >= data.size() || column >= data.get(row).size()))
            return;

        if (row < data.size() && column < data.get(row).size() && Objects.equals(value, data.get(row).get(column)))
            return;

        addUndoable(new UndoableCSV(row, column, value, originator));
    }

    @Override
    protected void doCreateData() {
        encoding = Charset.defaultCharset().name();
        data = new ArrayList<>();
    }

    @Override
    protected void doLoadData() throws IOException {
        var file = new File(getName());

        try (var fis = new FileInputStream(file)) {
            var bis = new BufferedInputStream(fis);
            var csv = new CsvInputStreamReader(bis);

//            csv.setValueSeparator(Locale.getDefault().getLanguage().equals("de") ? ';' : ',');
            csv.setValueSeparator(';');

            encoding = csv.getEncoding();
            data = new ArrayList<>();

            while (true) {
                var line = csv.readLine();

                if (line.isEmpty())
                    break;

                data.add(line);
            }
        }
    }

    @Override
    protected void doSaveData() throws IOException {
        var file = new File(getName());

        try (var fos = new FileOutputStream(file)) {
            var bos = new BufferedOutputStream(fos);
            var csv = new CsvOutputStreamWriter(bos, encoding);

//            csv.setValueSeparator(Locale.getDefault().getLanguage().equals("de") ? ';' : ',');
            csv.setValueSeparator(';');

            for (var line : data)
                csv.writeLine(line);

            csv.flush();
        }
    }

    private class UndoableCSV implements Undoable {
        private final int row;
        private final int column;
        private final String value;
        private final int[] orgRowSizes;
        private final int newRowSize;
        private final String title;

        private String orgValue;

        public UndoableCSV(int row, int column, String value, View<?> originator) {
            this.row = row;
            this.column = column;
            this.value = value != null ? value : "";

            orgRowSizes = new int[data.size()];

            int max = 0;

            for (int i = 0; i < data.size(); i++) {
                orgRowSizes[i] = data.get(i).size();
                max = Math.max(orgRowSizes[i], max);
            }

            newRowSize = Math.max(max, column + 1);

            title = row >= orgRowSizes.length || column >= orgRowSizes[row] ? "Add Value" : "Change Value";

            execute(originator);
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public void execute() {
            execute(null);
        }

        @Override
        public void undo() {
            data.get(row).set(column, orgValue);

            while (data.size() > orgRowSizes.length)
                data.remove(data.size() - 1);

            for (int i = 0; i < orgRowSizes.length; i++) {
                while (data.get(i).size() > orgRowSizes[i])
                    data.get(i).remove(data.get(i).size() - 1);
            }

            setModified(true);
            notifyDataChange(null, new ChangeContext(row, column));
        }

        private void execute(View<?> originator) {
            while (row >= data.size())
                data.add(new ArrayList<>());

            for (var line : data) {
                while (newRowSize > line.size())
                    line.add("");
            }

            orgValue = data.get(row).get(column);
            data.get(row).set(column, value != null ? value : "");

            setModified(true);
            notifyDataChange(originator, new ChangeContext(row, column));
        }
    }
}
