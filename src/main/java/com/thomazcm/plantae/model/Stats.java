package com.thomazcm.plantae.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "stats")
public class Stats {

    
    @Id
    private String id;
    private Integer acessosPaginaDeCompraTotal = 0;
    private Integer acessosPaginaDeCompraEsgotados = 0;
    private List<LocalDateTime> datas = new ArrayList<>();
    
    
    public List<LocalDateTime> getDatas() {
        return this.datas;
    }
    public String getId() {
        return this.id;
    }
    public Integer getAcessosPaginaDeCompraTotal() {
        return this.acessosPaginaDeCompraTotal;
    }
    public Integer getAcessosPaginaDeCompraEsgotados() {
        return this.acessosPaginaDeCompraEsgotados;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setAcessosPaginaDeCompraTotal(Integer acessosPaginaDeCompraTotal) {
        this.acessosPaginaDeCompraTotal = acessosPaginaDeCompraTotal;
    }
    public void setAcessosPaginaDeCompraEsgotados(Integer acessosPaginaDeCompraEsgotados) {
        this.acessosPaginaDeCompraEsgotados = acessosPaginaDeCompraEsgotados;
    }
    
    
    
}
