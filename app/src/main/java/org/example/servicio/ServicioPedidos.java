package org.example.servicio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.example.decorator.Azucar;
import org.example.decorator.Crema;
import org.example.decorator.Pepperoni;
import org.example.decorator.Pollo;
import org.example.decorator.Queso;
import org.example.decorator.Tomate;
import org.example.modelo.Pedido;
import org.example.modelo.TipoProducto;
import org.example.prototype.Cafe;
import org.example.prototype.Ensalada;
import org.example.prototype.Pizza;
import org.example.prototype.RegistroPrototipos;

public class ServicioPedidos {
    private RegistroPrototipos registro = new RegistroPrototipos();
    private ArrayList<Pedido> pedidos = new ArrayList<>();
    private int contador = 0; 


    private HashMap<TipoProducto, List<String>> permitidos = new HashMap<>();

    public ServicioPedidos() {
        registro.registrar("cafe", new Cafe("PROTO-CAFE"));
        registro.registrar("ensalada", new Ensalada("PROTO-ENSALADA"));
        registro.registrar("pizza", new Pizza("PROTO-PIZZA"));

        permitidos.put(TipoProducto.CAFE, List.of("Azúcar", "Crema"));
        permitidos.put(TipoProducto.ENSALADA, List.of("Tomate", "Pollo"));
        permitidos.put(TipoProducto.PIZZA, List.of("Queso", "Pepperoni"));
    }

    public Pedido crearPedido(String clave) {
        if (!registro.contiene(clave)) {

            throw new IllegalArgumentException("No existe el producto: " + clave);
        }
        contador++;
        String nuevoId = String.format("P-%03d", contador);
        Pedido nuevo = registro.clonar(clave, nuevoId);
        pedidos.add(nuevo);
        return nuevo;
    }

    public void validarCompatibilidad(Pedido pedido, String ingrediente) {
        if (pedido == null) {
            throw new IllegalArgumentException("Debe seleccionar un pedido primero.");
        }
        if (!permitidos.get(pedido.tipo()).contains(ingrediente)) {
            throw new IllegalArgumentException(
                    "No se puede agregar " + ingrediente + " a " + pedido.tipo());
        }
    }

    public Pedido agregarIngrediente(String idPedido, String ingrediente) {
        Pedido actual = buscarPedido(idPedido);
        validarCompatibilidad(actual, ingrediente);

        Pedido decorado = crearDecorador(ingrediente, actual);
        pedidos.set(buscarPosicion(idPedido), decorado);
        return decorado;
    }

    
    private Pedido crearDecorador(String ingrediente, Pedido pedido) {
        switch (ingrediente) {
            case "Azúcar":
                return new Azucar(pedido);
            case "Crema":
                return new Crema(pedido);
            case "Tomate":
                return new Tomate(pedido);
            case "Pollo":
                return new Pollo(pedido);
            case "Queso":
                return new Queso(pedido);
            case "Pepperoni":
                return new Pepperoni(pedido);
            default:
                throw new IllegalArgumentException("Ingrediente desconocido: " + ingrediente);
        }
    }

    private int buscarPosicion(String id) {
        for (int i = 0; i < pedidos.size(); i++) {
            if (pedidos.get(i).id().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    public Pedido buscarPedido(String id) {
        int posicion = buscarPosicion(id);
        if (posicion == -1) {
            return null;
        }
        return pedidos.get(posicion);
    }

    public List<String> ingredientesPermitidos(TipoProducto tipo) {
        return permitidos.get(tipo);
    }

    public List<String> ingredientesAgregados(Pedido pedido) {
        ArrayList<String> lista = new ArrayList<>();
        String[] partes = pedido.descripcion().split(" \\+ ");
        for (int i = 1; i < partes.length; i++) {
            lista.add(partes[i]);
        }
        return lista;
    }

    public List<Pedido> getPedidos() {
        return new ArrayList<>(pedidos);
    }

    public void reiniciar() {
        pedidos.clear();
        contador = 0;
    }

    public BigDecimal totalGeneral() {
        BigDecimal suma = BigDecimal.ZERO;
        for (Pedido p : pedidos) {
            suma = suma.add(p.precio());
        }
        return suma;
    }
    public boolean removerPedido(String id) {
        int pos = buscarPosicion(id);
        if (pos != -1) {
            pedidos.remove(pos);
            return true;
        }
        return false;
    }

}
