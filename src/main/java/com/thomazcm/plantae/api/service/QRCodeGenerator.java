package com.thomazcm.plantae.api.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import net.glxn.qrgen.javase.QRCode;

@Service
public class QRCodeGenerator {

    public BufferedImage generateQRCodeImage(String qrCodeUrl) throws Exception {

        int color = 0xFFAA4E3B;
        int darkerColor = (color & 0xFF000000) | ((color & 0x00FFFFFF) - 0x00101010);

        ByteArrayOutputStream stream = QRCode.from(qrCodeUrl).withSize(250, 250)
                .withColor(darkerColor, 0xFFFBF3EA).stream();
        ByteArrayInputStream bis = new ByteArrayInputStream(stream.toByteArray());

        return ImageIO.read(bis);
    }
}
