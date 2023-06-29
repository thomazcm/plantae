package com.thomazcm.plantae.api.service;

import java.util.List;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class PdfService {

    public String ajustarNomeArquivo(String cliente, String id) {
        return ajustarNomeArquivo(List.of(cliente), id);
    }

    public String ajustarNomeArquivo(List<String> nomes, String id) {
        StringBuilder builder = new StringBuilder("brunch-plantae-");
        nomes.forEach(nome -> {
            int spaceIndex = nome.indexOf(" ");
            if (spaceIndex != -1) {
                String primeiroNome = nome.substring(0, spaceIndex);
                builder.append(primeiroNome);
            } else {
                builder.append(nome);
            }
            builder.append(".");
        });
        return builder + id.substring(id.length() - 10) + ".pdf";
    }

    public HttpHeaders pdfDownloadHeaders(String nomePdf) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.attachment().filename(nomePdf).build());
        headers.set("file-pdf-name", nomePdf);
        return headers;
    }

}
