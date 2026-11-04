package org.example.decorator;

import java.math.BigDecimal;

import org.example.modelo.Pedido;

public class Crema extends DecoradorIngrediente {
    public Crema(Pedido pedido){
        super(pedido, "Crema",  new BigDecimal("1.20"));
    }

}
