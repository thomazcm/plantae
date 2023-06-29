package com.thomazcm.plantae.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.thomazcm.plantae.api.service.PdfGenerator;
import com.thomazcm.plantae.api.service.PdfService;
import com.thomazcm.plantae.repository.IngressoRepository;

@RestController
@RequestMapping("/qr-code")
public class QrCodeApi {

    private final IngressoRepository repository;
    private final PdfGenerator pdfGenerator;
    private final PdfService ingressoService;

    public QrCodeApi(IngressoRepository repository, PdfGenerator pdfGenerator,
            PdfService ingressoService) {
        this.repository = repository;
        this.pdfGenerator = pdfGenerator;
        this.ingressoService = ingressoService;
    }

    @GetMapping("/pdf/{id}")
    public ResponseEntity<byte[]> baixarIngresso(@PathVariable String id) {

        var ingresso = repository.findById(id).get();
        String nomeIngresso =
                ingressoService.ajustarNomeArquivo(ingresso.getCliente(), ingresso.getId());
        var ingressoByte = pdfGenerator.createPDF(ingresso).toByteArray();
        HttpHeaders ingressoHeaders = ingressoService.pdfDownloadHeaders(nomeIngresso);

        return ResponseEntity.ok().headers(ingressoHeaders).body(ingressoByte);
    }



}
