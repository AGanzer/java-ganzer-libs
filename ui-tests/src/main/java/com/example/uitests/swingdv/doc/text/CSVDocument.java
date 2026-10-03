package com.example.uitests.swingdv.doc.text;

import de.ganzer.core.io.CsvInputStreamReader;
import de.ganzer.core.io.CsvOutputStreamWriter;
import de.ganzer.dv.DVLoadException;
import de.ganzer.dv.Document;
import de.ganzer.dv.DocumentCreationInfo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVDocument extends Document {
    private List<List<String>> data;
    private String encoding;

    public CSVDocument(DocumentCreationInfo<? extends Document> info) throws DVLoadException {
        super(info);
    }

    public List<List<String>> getData() {
        return data;
    }

    @Override
    protected void doCreateData() {
        data = new ArrayList<>();
    }

    @Override
    protected void doLoadData(InputStream is) throws IOException {
        var file = new File(getName());
        var fis = new FileInputStream(file);
        var bis = new BufferedInputStream(fis);

        try (var csv = new CsvInputStreamReader(bis)) {
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
    protected void doSaveData(OutputStream os) throws IOException {
        var file = new File(getName());
        var fos = new FileOutputStream(file);
        var bos = new BufferedOutputStream(fos);

        try (var csv = new CsvOutputStreamWriter(bos, encoding)) {
            for (var line : data)
                csv.writeLine(line);
        }
    }
}
