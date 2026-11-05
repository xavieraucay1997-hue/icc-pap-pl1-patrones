package org.example.servicio;

import java.util.HashMap;
import java.util.List;

import org.example.modelo.Pedido;
import org.example.modelo.TipoProducto;

public class ServicioPedidos {
    private HashMap<TipoProducto, List<String>> permitidos = new HashMap<>();
    
    public ServicioPedidos(){
        permitidos.put(TipoProducto.CAFE, List.of("Azucar", "Crema"));
        permitidos.put(TipoProducto.ENSALADA, List.of("Tomate" , "Pollo"));
        permitidos.put(TipoProducto.PIZZA, List.of("Queso", "Pepperoni"));


    }
    
    public void validarCompatibilidad(Pedido pedido, String ingrediente){
        if(pedido == null) { 
            throw new IllegalArgumentException("Debe seleccionar un pedido primero. ");

        }
        if(!permitidos.get(pedido.tipo()).contains(ingrediente)){
            throw new IllegalArgumentException("No se puede agregar " + ingrediente + " a " + pedido.tipo());
            
        }
    }


}
