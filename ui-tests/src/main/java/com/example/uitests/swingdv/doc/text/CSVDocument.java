package com.example.uitests.swingdv.doc.text;

import de.ganzer.core.io.CsvInputStreamReader;
import de.ganzer.core.io.CsvOutputStreamWriter;
import de.ganzer.core.util.Strings;
import de.ganzer.dv.DVLoadException;
import de.ganzer.dv.Document;
import de.ganzer.dv.DocumentCreationInfo;
import de.ganzer.dv.View;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

        while (row >= data.size())
            data.add(new ArrayList<>());

        for (var line : data) {
            while (column >= line.size())
                line.add("");
        }

        data.get(row).set(column, value != null ? value : "");

        setModified(true);
        notifyDataChange(originator, new ChangeContext(row, column));
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

            csv.setValueSeparator(Locale.getDefault().getLanguage().equals("de") ? ';' : ',');

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

            csv.setValueSeparator(Locale.getDefault().getLanguage().equals("de") ? ';' : ',');

            for (var line : data)
                csv.writeLine(line);
        }
    }
}
