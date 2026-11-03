package org.example.modelo;

import java.math.BigDecimal;

public interface Pedido {
    String id();

    TipoProducto tipo();
    String descripcion();

    BigDecimal precio();
    


}
