package com.example.uitests.swingdv.doc.text;

import com.example.uitests.swingdv.doc.SwingDocumentUndoable;
import de.ganzer.core.io.BOMInputStreamReader;
import de.ganzer.dv.DVLoadException;
import de.ganzer.dv.Document;
import de.ganzer.dv.DocumentCreationInfo;

import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.io.*;
import java.nio.charset.Charset;

public class TextDocument extends Document {

    private PlainDocument document;
    private String encoding;

    public TextDocument(DocumentCreationInfo<TextDocument> info) throws DVLoadException {
        super(info);
    }

    public PlainDocument getDocument() {
        return document;
    }

    @Override
    protected void doCreateData() {
        encoding = Charset.defaultCharset().name();
        document = new PlainDocument();
        document.addUndoableEditListener(e -> {
            addUndoable(new SwingDocumentUndoable(e.getEdit()));
            setModified(true);
        });
    }

    @Override
    protected void doLoadData() throws IOException {
        var file = new File(getName());

        try (var fis = new FileInputStream(file)) {
            var bis = new BufferedInputStream(fis);
            var bir = new BOMInputStreamReader(bis);

            encoding = bir.getEncoding();
            String text = bir.readAll();

            document = new PlainDocument();

            try {
                document.insertString(0, text, null);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }

            document.addUndoableEditListener(e -> {
                addUndoable(new SwingDocumentUndoable(e.getEdit()));
                setModified(true);
            });
        }
    }

    @Override
    protected void doSaveData() throws IOException {
        var file = new File(getName());

        try (var fos = new FileOutputStream(file)) {
            var bos = new BufferedOutputStream(fos);

            try {
                String text = document.getText(0, document.getLength());
                bos.write(text.getBytes(encoding));
            } catch (BadLocationException e) {
                e.printStackTrace();
            }

            bos.flush();
        }
    }
}
