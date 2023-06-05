package com.thomazcm.plantae.config.properties;

import java.util.ResourceBundle;
import org.springframework.stereotype.Component;

@Component
public class PdfProperties {

    private ResourceBundle resourceBundle;

    public PdfProperties(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    private String getResource(String resource) {
        return this.resourceBundle.getString("plantae.pdf." + resource);
    }

    public String getTitulo() {
        return getResource("titulo");
    }

    public String getPrefixoNomeNoIngresso() {
        return getResource("prefixoNomeNoIngresso");
    }

    public String getEndereco() {
        return getResource("endereco");
    }

    public String getData() {
        return getResource("data");
    }

    public String getRodape() {
        return getResource("rodape");
    }

    public String getBackgroundColor() {
        return getResource("backgroundColor");
    }

}
