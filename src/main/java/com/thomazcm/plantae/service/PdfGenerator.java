/**
 * 
 */
package com.thomazcm.plantae.service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import org.springframework.stereotype.Service;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.thomazcm.plantae.config.properties.PdfProperties;
import com.thomazcm.plantae.model.Ingresso;

@Service
public class PdfGenerator {

    private final QRCodeGenerator qrCodeGenerator;
    private final PdfProperties properties;

    public PdfGenerator(QRCodeGenerator qrCodeGenerator, PdfProperties properties) {
        this.qrCodeGenerator = qrCodeGenerator;
        this.properties = properties;
    }

    public ByteArrayOutputStream createPDF(Ingresso ingresso) {
        return createPDF(List.of(ingresso));
    }

    public ByteArrayOutputStream createPDF(List<Ingresso> ingressos) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.HALFLETTER);
            PdfWriter writer = PdfWriter.getInstance(document, baos);

            String backgroundColor = properties.getBackgroundColor();
            writer.setPageEvent(new BackgroundColorEvent(Color.decode(backgroundColor)));

            document.open();

            ingressos.forEach(ingresso -> {
                try {
                    var qrCodeImage = qrCodeGenerator.generateQRCodeImage(ingresso.getQrCodeUrl());
                    String nomeCliente = ingresso.getCliente();
                    writePage(document, qrCodeImage, nomeCliente);
                    document.newPage();

                } catch (Exception e) {
                    System.out
                            .println("Error generating PDF contents for " + ingresso.getCliente());
                    e.printStackTrace();
                }
            });
            document.close();

            return baos;
        } catch (Exception e) {
            System.out.println("Failed to create PDF file");
            e.printStackTrace();
            return null;
        }
    }

    private void writePage(Document document, BufferedImage qrCodeImage, String nomeCliente)
            throws DocumentException, MalformedURLException, IOException {

        Image logoImage = Image.getInstance("classpath:static/png/logo.png");
        logoImage.scaleAbsolute(390, 180);
        logoImage.setAbsolutePosition(0,
                PageSize.HALFLETTER.getHeight() - logoImage.getScaledHeight());
        document.add(logoImage);

        document.add(
                createParagraph(properties.getTitulo().toUpperCase(), 
                    "static/fonts/Montserrat-SemiBold.ttf", 16,
                    Element.ALIGN_CENTER, logoImage.getScaledHeight() - 50, 0 
                ));
        document.add(
                createParagraph(properties.getEndereco(), 
                    "static/fonts/Montserrat-Medium.ttf", 12,
                    Element.ALIGN_CENTER, 
                    3, 0
                ));
        document.add(
                createParagraph(properties.getData(), 
                    "static/fonts/Montserrat-Bold.ttf", 14,
                    Element.ALIGN_CENTER, 3, 5));
        document.add(
                createParagraph(properties.getPrefixoNomeNoIngresso() + nomeCliente,
                    "static/fonts/Montserrat-Medium.ttf", 11, 
                    Element.ALIGN_CENTER, 5, 0));

        float qrCodeWidth = PageSize.HALFLETTER.getWidth() * 0.72f;
        float qrCodeHeight = qrCodeWidth;

        Image qrCode = Image.getInstance(qrCodeImage, null);
        qrCode.scaleToFit(qrCodeWidth, qrCodeHeight);
        float qrCodeX = (PageSize.HALFLETTER.getWidth() - qrCode.getScaledWidth()) / 2;
        float qrCodeY = 42;
        qrCode.setAbsolutePosition(qrCodeX, qrCodeY);

        document.add(qrCode);
        document.add(
                createParagraph(properties.getRodape(), 
                    "static/fonts/Montserrat-Bold.ttf", 8,
                    Element.ALIGN_CENTER, 260, 5));
    }

    private Paragraph createParagraph(String content, String fontName, int fontSize, int alignment,
            float spacingBefore, float extraSpace) {
        Font font = FontFactory.getFont(fontName, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, fontSize);
        font.setColor(170, 78, 60);

        Paragraph paragraph = new Paragraph(content, font);
        paragraph.setAlignment(alignment);
        paragraph.setSpacingBefore(spacingBefore);
        paragraph.setExtraParagraphSpace(extraSpace);

        return paragraph;
    }

    private static class BackgroundColorEvent extends PdfPageEventHelper {
        private final BaseColor backgroundColor;

        public BackgroundColorEvent(Color backgroundColor) {
            this.backgroundColor = new BaseColor(backgroundColor.getRGB());
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte canvas = writer.getDirectContentUnder();
            Rectangle rect = document.getPageSize();
            canvas.saveState();
            canvas.setColorFill(backgroundColor);
            canvas.rectangle(rect.getLeft(), rect.getBottom(), rect.getWidth(), rect.getHeight());
            canvas.fill();
            canvas.restoreState();
        }
    }
}
