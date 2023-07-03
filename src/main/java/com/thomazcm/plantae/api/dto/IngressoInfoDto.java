package com.thomazcm.plantae.api.dto;

import java.time.format.DateTimeFormatter;
import java.util.List;
import com.thomazcm.plantae.model.Ingresso;

public class IngressoInfoDto {

    private String nome;
    private String dataDaCompra;
    private String lote;


    public IngressoInfoDto(Ingresso ingresso) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");

        this.nome = ingresso.getCliente();
        this.dataDaCompra = ingresso.getData().format(formatter);
        this.lote = ingresso.getLote().toString().toLowerCase();
    }


    public String getNome() {
        return this.nome;
    }


    public String getDataDaCompra() {
        return this.dataDaCompra;
    }


    public String getLote() {
        return this.lote;
    }

    public static List<IngressoInfoDto> toList(List<Ingresso> ingressos) {
        return ingressos.stream().map(IngressoInfoDto::new).toList();
    }


}
