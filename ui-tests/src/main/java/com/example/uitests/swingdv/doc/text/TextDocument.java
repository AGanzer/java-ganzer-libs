package com.example.uitests.swingdv.doc.text;

import de.ganzer.core.io.BOMInputStreamReader;
import de.ganzer.dv.DVLoadException;
import de.ganzer.dv.Document;
import de.ganzer.dv.DocumentCreationInfo;
import de.ganzer.dv.View;

import java.io.*;
import java.nio.charset.Charset;

public class TextDocument extends Document {
    private String text;
    private String encoding;

    public TextDocument(DocumentCreationInfo<TextDocument> info) throws DVLoadException {
        super(info);
    }

    public String getText() {
        return text;
    }

    public void setText(String text, View<?> originator) {
        var old = this.text;
        this.text = text;

        setModified(true);
        notifyDataChange(originator, old);
    }

    @Override
    protected void doCreateData() {
        encoding = Charset.defaultCharset().name();
        text = "";
    }

    @Override
    protected void doLoadData() throws IOException {
        var file = new File(getName());

        try (var fis = new FileInputStream(file);
             var bis = new BufferedInputStream(fis);
             var bir = new BOMInputStreamReader(fis)) {
            encoding = bir.getEncoding();
            text = new String(bis.readAllBytes(), encoding);
        }
    }

    @Override
    protected void doSaveData() throws IOException {
        var file = new File(getName());

        try (var fos = new FileOutputStream(file);
             var bos = new BufferedOutputStream(fos)) {
            bos.write(text.getBytes(encoding));
        }
    }
}
