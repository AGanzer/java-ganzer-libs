package com.example.uitests.swingdv.doc.text;

import de.ganzer.core.io.BOMInputStreamReader;
import de.ganzer.dv.DVLoadException;
import de.ganzer.dv.Document;
import de.ganzer.dv.DocumentCreationInfo;

import java.io.*;
import java.nio.charset.Charset;

public class TextDocument extends Document {
    public static final String TEXT_PROPERTY = "text";

    private String text;
    private String encoding;

    public TextDocument(DocumentCreationInfo<TextDocument> info) throws DVLoadException {
        super(info);
    }

    public String getText() {
        return text;
    }

    public void setText(String text, Object originator) {
        var old = this.text;
        this.text = text;

        setModified(true);
        fireChange(TEXT_PROPERTY, originator, old, text);
    }

    @Override
    protected void doCreateData() {
        encoding = Charset.defaultCharset().name();
        text = "";
    }

    @Override
    protected void doLoadData() throws RuntimeException {
        var file = new File(getName());

        try (var fis = new FileInputStream(file);
             var bis = new BufferedInputStream(fis);
             var bir = new BOMInputStreamReader(fis)) {
            encoding = bir.getEncoding();
            text = new String(bis.readAllBytes(), encoding);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doSaveData() throws RuntimeException {
        var file = new File(getName());

        try (var fos = new FileOutputStream(file);
             var bos = new BufferedOutputStream(fos)) {
            bos.write(text.getBytes(encoding));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
