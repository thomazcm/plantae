/**
 * 
 */
package com.thomazcm.plantae.generator;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
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

@Service
public class PdfGenerator {

	public ByteArrayOutputStream createPDF(BufferedImage qrCodeImage, String nome) 
			throws Exception{
		
		String titulo = "Brunch PLANTAE - 1ª Edição";
		String endereco = "Espaço Vitruvie - Rua Professor José Renault, 67 \n São Bento - Belo Horizonte";
		String data = "Domingo, 2 de julho de 2023 \n10:00-14:00";
		String ingresso = "Entrada - " + nome;
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.HALFLETTER);
		PdfWriter writer = PdfWriter.getInstance(document, baos);
		
		writer.setPageEvent(new BackgroundColorEvent(Color.decode("#FBF3EA")));
		
		document.open();
		
		Image logoImage = Image.getInstance("logo.png");
        logoImage.scaleAbsolute(390, 180);
        logoImage.setAbsolutePosition(0, PageSize.HALFLETTER.getHeight() - logoImage.getScaledHeight());
        document.add(logoImage);
		
        
        Font font = FontFactory.getFont("Montserrat-SemiBold.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 16);
        font.setColor(170, 78, 60);
        
        Paragraph paragraph1 = new Paragraph(titulo.toUpperCase(), font);
        paragraph1.setAlignment(Element.ALIGN_CENTER);
        paragraph1.setSpacingBefore(logoImage.getScaledHeight()-40);
        document.add(paragraph1);
        
        font = FontFactory.getFont("Montserrat-Medium.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12);
        font.setColor(170, 78, 60);
        
        Paragraph paragraph2 = new Paragraph(endereco, font);
        paragraph2.setAlignment(Element.ALIGN_CENTER);
        paragraph2.setSpacingBefore(3);
        document.add(paragraph2);
        
        font = FontFactory.getFont("Montserrat-Bold.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 14);
        font.setColor(170, 78, 60);
        Paragraph paragraph3 = new Paragraph(data, font);
        paragraph3.setAlignment(Element.ALIGN_CENTER);
        paragraph3.setExtraParagraphSpace(5);
        paragraph3.setSpacingBefore(3);
        document.add(paragraph3);
        
        
        font = FontFactory.getFont("Montserrat-Medium.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 11);
        font.setColor(170, 78, 60);
        Paragraph paragraph4 = new Paragraph(ingresso, font);
        paragraph4.setAlignment(Element.ALIGN_CENTER);
        paragraph4.setSpacingBefore(8);
        document.add(paragraph4);
        
        float qrCodeWidth = PageSize.HALFLETTER.getWidth() * 0.72f;
        float qrCodeHeight = qrCodeWidth; 

        Image qrCode = Image.getInstance(qrCodeImage, null);
        qrCode.scaleToFit(qrCodeWidth, qrCodeHeight);
        float qrCodeX = (PageSize.HALFLETTER.getWidth() - qrCode.getScaledWidth()) / 2; 
        float qrCodeY = 15; 
        qrCode.setAbsolutePosition(qrCodeX, qrCodeY);
        
		document.add(qrCode);
		document.close();
		
		return baos;
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