package com.example.uitests.swingdv.doc.image;

import de.ganzer.core.util.FileNames;
import de.ganzer.dv.DVLoadException;
import de.ganzer.dv.Document;
import de.ganzer.dv.DocumentCreationInfo;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageDocument extends Document {
    private BufferedImage image;

    public ImageDocument(DocumentCreationInfo<? extends Document> info) throws DVLoadException {
        super(info);
    }

    public Image getImage() {
        return image;
    }

    @Override
    protected void doCreateData() {
        throw new UnsupportedOperationException("New Images are not supported.");
    }

    @Override
    protected void doLoadData() throws IOException {
        image = ImageIO.read(new File(getName()));

        if (image == null)
            throw new IOException(String.format("Type %s is not supported.", FileNames.getExtension(getName()).toUpperCase()));
    }

    @Override
    protected void doSaveData() throws IOException {
        ImageIO.write(image, FileNames.getExtension(getName()).toUpperCase(), new File(getName()));
    }
}
