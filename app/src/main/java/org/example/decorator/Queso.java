package org.example.decorator;

import java.math.BigDecimal;

import org.example.modelo.Pedido;

public class Queso extends DecoradorIngrediente{
    public Queso(Pedido pedido){
        super(pedido, "Queso", new BigDecimal("1.00"));
    }

}
