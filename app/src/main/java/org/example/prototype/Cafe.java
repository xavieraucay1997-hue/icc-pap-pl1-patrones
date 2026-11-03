package org.example.prototype;

import java.math.BigDecimal;

import org.example.modelo.TipoProducto;

public class Cafe extends ProductoBase {

    public Cafe(String id){
        this(id, "Cafe", new BigDecimal("2.20"));
    }

    public Cafe(String id, String descripcion, BigDecimal precioBase){
        super(id, TipoProducto.CAFE, descripcion, precioBase);
    }
    
    @Override
    public ProductoBase copiar(String nuevoId) {
        return new Cafe(nuevoId, this.descripcion, this.precioBase);
    }


    

}
