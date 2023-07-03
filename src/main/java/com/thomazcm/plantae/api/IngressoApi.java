package com.thomazcm.plantae.api;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.thomazcm.plantae.api.dto.IngressoDto;
import com.thomazcm.plantae.api.dto.PedidoDto;
import com.thomazcm.plantae.api.form.IngressoUpdateForm;
import com.thomazcm.plantae.api.form.integration.RequestPayload;
import com.thomazcm.plantae.api.service.IngressoGenerator;
import com.thomazcm.plantae.api.service.PdfGenerator;
import com.thomazcm.plantae.api.service.PdfService;
import com.thomazcm.plantae.api.service.PlantaeEmailSender;
import com.thomazcm.plantae.model.Ingresso;
import com.thomazcm.plantae.model.integration.Cliente;
import com.thomazcm.plantae.repository.IngressoRepository;

@RestController
@RequestMapping("/ingressos")
public class IngressoApi {

    private final IngressoRepository repository;
    private final IngressoGenerator ingressoGenerator;
    private final PlantaeEmailSender emailSender;
    private final PdfGenerator pdfGenerator;
    private final PdfService service;

    public IngressoApi(IngressoRepository repository, IngressoGenerator ingressoGenerator,
            PlantaeEmailSender emailSender, PdfService service, PdfGenerator pdfGenerator) {
        this.repository = repository;
        this.ingressoGenerator = ingressoGenerator;
        this.emailSender = emailSender;
        this.pdfGenerator = pdfGenerator;
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<IngressoDto>> listarIngressos() {
        var ingressos =
                repository.findAll().stream().map(IngressoDto::new).collect(Collectors.toList());
        return ResponseEntity.ok(sortByValidoENumero(ingressos));
    }
    
    @GetMapping("/info")
    public ResponseEntity<String> customerInfo() {
        List<Ingresso> allIngressos = repository.findAll();
        String ingressoInfo = buildInfo(allIngressos);
        
        return ResponseEntity.ok(ingressoInfo);
        
    }
    
    private String buildInfo(List<Ingresso> allIngressos) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        StringBuilder builder = new StringBuilder("=== Ingressos Vendidos ====");
        builder.append("\n1º Lote: 43\n2ºLote: 11 \nCortesias: 4");
        builder.append("\n===============");
        
        
        for (int i = 0; i < allIngressos.size(); i++) {
            Ingresso ingresso = allIngressos.get(i);
            
            builder.append(System.lineSeparator());
            builder.append(String.join(": ", Integer.valueOf(i+1).toString(), ingresso.getCliente()));
            builder.append(System.lineSeparator());
            builder.append("Lote: " + ingresso.getLote().toString().toLowerCase());
            builder.append(System.lineSeparator());
            builder.append(String.join(": ", "Data da compra", ingresso.getData().format(formatter)));
            
        }
        
        return builder.toString();
    }

    @PostMapping(value = "/novo", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> novoIngresso(@RequestBody RequestPayload payload,
            @RequestHeader("Enviar-Email") String enviarEmail) throws Exception {

        PedidoDto pedidtoDto = payload.getPedidoDto();
        String email = pedidtoDto.getEmail();
        Boolean cortesia = pedidtoDto.getCortesia();
        List<String> nomes = pedidtoDto.getClientes().stream().map(Cliente::getNome)
                .collect(Collectors.toList());


        List<Ingresso> ingressos = new ArrayList<Ingresso>();
        nomes.forEach(nome -> {
            Ingresso novoIngresso = ingressoGenerator.novoIngresso(nome, email, cortesia);
            ingressos.add(novoIngresso);
        });

        String nomePdf = service.ajustarNomeArquivo(nomes, ingressos.get(0).getId());
        ByteArrayOutputStream ingressoPdf = pdfGenerator.createPDF(ingressos);

        if (Boolean.parseBoolean(enviarEmail) && email != null && !email.isEmpty()) {
            emailSender.sendPdfEmail(email, ingressoPdf, nomePdf, ingressos.get(0).getCliente());
        }
        return ResponseEntity.ok().headers(service.pdfDownloadHeaders(nomePdf))
                .body(ingressoPdf.toByteArray());
    }

    @GetMapping("/validar/{id}")
    public ResponseEntity<IngressoDto> validarPorNome(@PathVariable String id) {
        try {
            Ingresso ingresso = repository.findById(id).get();
            ingresso.validar(ingresso.getSenha());
            repository.save(ingresso);
            return ResponseEntity.ok(new IngressoDto(ingresso));
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngressoDto> atualizar(@RequestBody IngressoUpdateForm form,
            @PathVariable String id) {
        try {
            Ingresso ingresso = repository.findById(id).get();
            ingresso.setCliente(form.getCliente());
            repository.save(ingresso);
            return ResponseEntity.ok(new IngressoDto(ingresso));
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<IngressoDto> excluir(@PathVariable String id) {
        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    private List<IngressoDto> sortByValidoENumero(List<IngressoDto> ingressos) {

        List<IngressoDto> validos =
                ingressos.stream().filter(i -> i.getValid()).collect(Collectors.toList());
        List<IngressoDto> invalidos =
                ingressos.stream().filter(i -> !i.getValid()).collect(Collectors.toList());

        validos.sort((i1, i2) -> {
            return i1.getCliente().compareTo(i2.getCliente());
        });
        invalidos.sort((i1, i2) -> {
            return i1.getCliente().compareTo(i2.getCliente());
        });
        validos.addAll(invalidos);
        return validos;
    }

}
