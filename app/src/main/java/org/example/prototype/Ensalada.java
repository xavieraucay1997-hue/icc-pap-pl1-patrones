package org.example.prototype;

import java.math.BigDecimal;

import org.example.modelo.TipoProducto;

public class Ensalada extends ProductoBase {
    public Ensalada(String id){
        super(id, TipoProducto.ENSALADA, "Ensalada Sencilla", new BigDecimal("3.00"));
    }

    private Ensalada(Ensalada otro, String nuevoId){
        super(otro, nuevoId);
    }
    @Override 
    public ProductoBase copiar(String nuevoId) { 
        return new Ensalada(this, nuevoId);
    }

}
