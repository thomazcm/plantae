package com.thomazcm.plantae.dto.integration;

import com.thomazcm.plantae.dto.PedidoDto;

public class RequestPayload {

    private PedidoDto pedidoDto;

    public PedidoDto getPedidoDto() {
        return pedidoDto;
    }

    public void setClienteDto(PedidoDto pedidoDto) {
        this.pedidoDto = pedidoDto;
    }

}
