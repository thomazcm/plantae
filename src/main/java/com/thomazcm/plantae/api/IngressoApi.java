package com.thomazcm.plantae.api;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.thomazcm.plantae.dto.IngressoDto;
import com.thomazcm.plantae.dto.IngressoUpdateForm;
import com.thomazcm.plantae.model.Ingresso;
import com.thomazcm.plantae.model.LoteIngresso;
import com.thomazcm.plantae.repository.IngressoRepository;

@RestController
@RequestMapping("/ingressos")
public class IngressoApi {

    @Autowired
    private IngressoRepository repository;

    @GetMapping
    public ResponseEntity<List<IngressoDto>> listarIngressos() {
        
        var ingressosLista = repository.findAll();
        ingressosLista.forEach(ingresso -> update(ingresso));
        
        var ingressos =
                repository.findAll().stream().map(IngressoDto::new).collect(Collectors.toList());
        return ResponseEntity.ok(sortByValidoENumero(ingressos));
    }

    private void update(Ingresso ingresso) {
        if (ingresso.getLote() == null) {
            if (ingresso.getId().compareTo("6488d54b92ce80139a912b5c") == 0 
                    || ingresso.getId().compareTo("64865ec8c3e2e63b054945e0") == 0
                    || ingresso.getId().compareTo("6488c71c5fff23529cbe6ced") == 0) {
                ingresso.setLote(LoteIngresso.CORTESIA);
            } else if (ingresso.getData().compareTo(LocalDate.of(2023, 06, 13)) < 0) {
                ingresso.setLote(LoteIngresso.PRIMEIRO);
            } else {
                ingresso.setLote(LoteIngresso.SEGUNDO);
            }
            repository.save(ingresso);
        }
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
