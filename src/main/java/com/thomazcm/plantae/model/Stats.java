package com.thomazcm.plantae.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "stats")
public class Stats {

    
    @Id
    private String id;
    private Integer acessosPaginaDeCompraTotal = 0;
    private Integer acessosPaginaDeCompraEsgotados = 0;
    private List<LocalDateTime> datas = new ArrayList<>();
    private List<String> requestIps = new ArrayList<>();
    
    public String getId() {
        return this.id;
    }
    
    public List<LocalDateTime> getDatas() {
        return Collections.unmodifiableList(this.datas);
    }
    
    public List<String> getRequestIps() {
        return Collections.unmodifiableList(this.requestIps);
    }
    
    public Integer getAcessosPaginaDeCompraTotal() {
        return this.acessosPaginaDeCompraTotal;
    }
    public Integer getAcessosPaginaDeCompraEsgotados() {
        return this.acessosPaginaDeCompraEsgotados;
    }
  
    public void novoAcesso(HttpServletRequest request) {
        acessosPaginaDeCompraTotal++;
        datas.add(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
        requestIps.add(getRequestIp(request));
            
    }
    public void novoAcessoEsgotado() {
        acessosPaginaDeCompraEsgotados++;
    }

    private String getRequestIp(HttpServletRequest request) {
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null || clientIp.isEmpty()) {
            clientIp = request.getRemoteAddr();
        }
        return clientIp;
    }
    
    
    
}
