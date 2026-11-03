package org.example.prototype;

import java.math.BigDecimal;

import org.example.modelo.TipoProducto;

public class Pizza extends ProductoBase {
    public Pizza(String id){
        this(id, "Pizza Sencilla", new BigDecimal("5.00"));
    }

    public Pizza(String id, String descripcion, BigDecimal precioBase){
        super(id, TipoProducto.PIZZA, descripcion, precioBase);
    }

    @Override
    public ProductoBase copiar(String nuevoId) {
        return new Pizza(nuevoId, this.descripcion, this.precioBase);
    }

}
