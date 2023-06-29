package com.thomazcm.plantae.api.form.integration;

import com.thomazcm.plantae.api.dto.PedidoDto;

public class RequestPayload {

    private PedidoDto pedidoDto;

    public PedidoDto getPedidoDto() {
        return pedidoDto;
    }

    public void setClienteDto(PedidoDto pedidoDto) {
        this.pedidoDto = pedidoDto;
    }

}
