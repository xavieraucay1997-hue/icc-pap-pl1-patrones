package org.example.decorator;

import java.math.BigDecimal;

import org.example.modelo.Pedido;

public class Tomate extends DecoradorIngrediente{
    public Tomate (Pedido pedido){
        super(pedido, "Tomate", new BigDecimal("1.10"));
    }

}
