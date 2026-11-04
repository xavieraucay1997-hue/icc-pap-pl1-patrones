package org.example.decorator;

import java.math.BigDecimal;

import org.example.modelo.Pedido;

public class Pollo extends DecoradorIngrediente {
    public Pollo(Pedido pedido){
        super(pedido, "Pollo", new BigDecimal("1.50"));
    }

}
