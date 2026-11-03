package org.example.prototype;

import java.math.BigDecimal;

import org.example.modelo.Pedido;
import org.example.modelo.TipoProducto;

public abstract class ProductoBase implements Pedido, Prototipo<ProductoBase> {
    protected String id;
    protected  TipoProducto tipo;
    protected  String descripcion; 
    protected BigDecimal precioBase;
    
    public ProductoBase(String id, TipoProducto tipo, String descripcion, BigDecimal precioBase) {
        this.id = id;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.precioBase = precioBase;
    }

    @Override
    public String descripcion() {
        return descripcion;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public BigDecimal precio() {

        return precioBase;
    }

    @Override
    public TipoProducto tipo() {
        return tipo;
    }

    

    


}
