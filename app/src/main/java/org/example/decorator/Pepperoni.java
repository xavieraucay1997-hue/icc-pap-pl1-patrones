package org.example.decorator;

import java.math.BigDecimal;

import org.example.modelo.Pedido;

public class Pepperoni extends DecoradorIngrediente{
    public Pepperoni(Pedido pedido){
        super(pedido, "Pepperoni", new BigDecimal("1.10"));
    }

}
