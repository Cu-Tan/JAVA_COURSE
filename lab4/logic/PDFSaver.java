package com.example.student_registry.logic;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.WritableImage;

import java.awt.image.BufferedImage;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;

public class PDFSaver {

  public static void export(ScrollPane _scrollPane, String filePath) throws Exception {

    Node content = _scrollPane.getContent();

    double originalWidth = content.prefWidth(-1);
    double originalHeight = content.prefHeight(-1);

    WritableImage snapshot = new WritableImage((int) originalWidth, (int) originalHeight);
    content.snapshot(new SnapshotParameters(), snapshot);

    BufferedImage bufferedImage = SwingFXUtils.fromFXImage(snapshot, null);

    try (PDDocument document = new PDDocument()) {
      PDPage page = new PDPage();
      document.addPage(page);

      var pdImage = LosslessFactory.createFromImage(document, bufferedImage);
      try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

        PDRectangle mediaBox = page.getMediaBox();
        float pageWidth = mediaBox.getWidth() - 10;
        float pageHeight = mediaBox.getHeight() - 10;

        float imageWidth = bufferedImage.getWidth();
        float imageHeight = bufferedImage.getHeight();

        float scale = Math.min(pageWidth / imageWidth, pageHeight / imageHeight);

        float drawWidth = imageWidth * scale;
        float drawHeight = imageHeight * scale;

        float x = (mediaBox.getWidth() - drawWidth) / 2;
        float y = (mediaBox.getHeight() - drawHeight) / 2;

        contentStream.drawImage(pdImage, x, y, drawWidth, drawHeight);
      }

      document.save(filePath);
    }

  }

}
