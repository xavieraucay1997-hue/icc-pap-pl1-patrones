package org.example.decorator;

import java.math.BigDecimal;

import org.example.modelo.Pedido;

public class Azucar extends DecoradorIngrediente{
    public Azucar(Pedido pedido) {
        super(pedido, "Azucar", new BigDecimal("0.50"));
    }

}
