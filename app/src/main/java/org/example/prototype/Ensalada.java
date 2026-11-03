package org.example.prototype;

import java.math.BigDecimal;

import org.example.modelo.TipoProducto;

public class Ensalada extends ProductoBase {

    public Ensalada(String id) { 
        this(id, "Ensalada sencilla", new BigDecimal("3.00"));

    }

    public Ensalada(String id, String descripcion, BigDecimal precioBase){
        super(id, TipoProducto.ENSALADA, descripcion, precioBase);

    }


    @Override 
    public ProductoBase copiar(String nuevoId) { 
        return new Ensalada(nuevoId, this.descripcion, this.precioBase);
    }

}
