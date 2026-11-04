package org.example.decorator;

import java.math.BigDecimal;

import org.example.modelo.Pedido;
import org.example.modelo.TipoProducto;

public abstract class DecoradorIngrediente implements Pedido{
    
    protected Pedido pedidoEnvuelto;
    protected String nombreIngrediente;
    protected BigDecimal costoIngrediente;

    public DecoradorIngrediente(Pedido pedidoEnuelto, String nombreIngrediente, BigDecimal costoIngrediente) {
        this.pedidoEnvuelto = pedidoEnuelto;
        this.nombreIngrediente = nombreIngrediente;
        this.costoIngrediente = costoIngrediente;
    }


    @Override
    public String id() {
        return pedidoEnvuelto.id();

    }

    @Override
    public TipoProducto tipo() {
        return pedidoEnvuelto.tipo();

    }

    @Override
    public String descripcion() {
        return pedidoEnvuelto.descripcion() + " + " + nombreIngrediente;
    }

    @Override
    public BigDecimal precio() {
        return pedidoEnvuelto.precio().add(costoIngrediente);
    }
}