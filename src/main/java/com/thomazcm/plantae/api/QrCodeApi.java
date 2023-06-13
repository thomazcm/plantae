package com.thomazcm.plantae.api;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.thomazcm.plantae.dto.ClienteDto;
import com.thomazcm.plantae.dto.RequestPayload;
import com.thomazcm.plantae.model.Cliente;
import com.thomazcm.plantae.model.Ingresso;
import com.thomazcm.plantae.repository.IngressoRepository;
import com.thomazcm.plantae.service.EmailService;
import com.thomazcm.plantae.service.IngressoGenerator;
import com.thomazcm.plantae.service.PdfGenerator;
import com.thomazcm.plantae.service.QRCodeGenerator;

@RestController
@RequestMapping("/qr-code")
public class QrCodeApi {

    IngressoGenerator ingressoGenerator;
    QRCodeGenerator qrCodeGenerator;
    IngressoRepository repository;
    PdfGenerator pdfGenerator;
    EmailService emailSender;

    public QrCodeApi(IngressoGenerator ingressoGenerator, QRCodeGenerator qrCodeGenerator,
            IngressoRepository repository, PdfGenerator pdfGenerator, EmailService emailSender) {
        this.ingressoGenerator = ingressoGenerator;
        this.qrCodeGenerator = qrCodeGenerator;
        this.repository = repository;
        this.pdfGenerator = pdfGenerator;
        this.emailSender = emailSender;
    }

    @PostMapping(value = "/novo", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> novoIngresso(@RequestBody RequestPayload payload,
            @RequestHeader("Enviar-Email") String enviarEmail) throws Exception {

        ClienteDto clienteDto = payload.getClienteDto();
        String email = clienteDto.getEmail();
        List<String> nomes = clienteDto.getClientes().stream().map(Cliente::getNome)
                .collect(Collectors.toList());


        List<Ingresso> ingressos = new ArrayList<Ingresso>();
        nomes.forEach(nome -> {
            Ingresso novoIngresso = ingressoGenerator.novoIngresso(nome, email);
            ingressos.add(novoIngresso);
        });

        String nomePdf = ajustarNomeArquivo(nomes, ingressos.get(0).getId());
        ByteArrayOutputStream ingressoPdf = pdfGenerator.createPDF(ingressos);

        if (Boolean.parseBoolean(enviarEmail) && email != null && !email.isEmpty()) {
            emailSender.sendPdfEmail(email, ingressoPdf, nomePdf, ingressos.get(0).getCliente());
        }
        return ResponseEntity.ok().headers(pdfDownloadHeaders(nomePdf))
                .body(ingressoPdf.toByteArray());
    }

    @GetMapping("/pdf/{id}")
    public ResponseEntity<byte[]> baixarIngresso(@PathVariable String id) throws Exception {

        var ingresso = repository.findById(id).get();
        String nomeIngresso = ajustarNomeArquivo(List.of(ingresso.getCliente()), ingresso.getId());
        ByteArrayOutputStream baos = pdfGenerator.createPDF(ingresso);

        return ResponseEntity.ok().headers(pdfDownloadHeaders(nomeIngresso))
                .body(baos.toByteArray());
    }

    private HttpHeaders pdfDownloadHeaders(String nomeIngresso) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.attachment().filename(nomeIngresso).build());
        headers.set("file-pdf-name", nomeIngresso);
        return headers;
    }

    private String ajustarNomeArquivo(List<String> nomes, String id) {
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
}