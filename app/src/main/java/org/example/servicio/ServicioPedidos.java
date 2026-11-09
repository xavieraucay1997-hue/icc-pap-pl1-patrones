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
    private int contador = 0; // para generar P-001, P-002...

    // Ingredientes permitidos para cada tipo de producto
    private HashMap<TipoProducto, List<String>> permitidos = new HashMap<>();

    public ServicioPedidos() {
        // Se registra un prototipo de cada producto
        registro.registrar("cafe", new Cafe("PROTO-CAFE"));
        registro.registrar("ensalada", new Ensalada("PROTO-ENSALADA"));
        registro.registrar("pizza", new Pizza("PROTO-PIZZA"));

        // Reglas de compatibilidad (segun el catalogo)
        permitidos.put(TipoProducto.CAFE, List.of("Azúcar", "Crema"));
        permitidos.put(TipoProducto.ENSALADA, List.of("Tomate", "Pollo"));
        permitidos.put(TipoProducto.PIZZA, List.of("Queso", "Pepperoni"));
    }

    // Crea un pedido nuevo clonando el prototipo del registro
    public Pedido crearPedido(String clave) {
        if (!registro.contiene(clave)) {
            // no se aumenta el contador ni se agrega nada a la lista
            throw new IllegalArgumentException("No existe el producto: " + clave);
        }
        contador++;
        String nuevoId = String.format("P-%03d", contador);
        Pedido nuevo = registro.clonar(clave, nuevoId);
        pedidos.add(nuevo);
        return nuevo;
    }

    // Revisa que el ingrediente sea compatible. Si no, lanza un error.
    public void validarCompatibilidad(Pedido pedido, String ingrediente) {
        if (pedido == null) {
            throw new IllegalArgumentException("Debe seleccionar un pedido primero.");
        }
        if (!permitidos.get(pedido.tipo()).contains(ingrediente)) {
            throw new IllegalArgumentException(
                    "No se puede agregar " + ingrediente + " a " + pedido.tipo());
        }
    }

    // Envuelve el pedido con el decorador y lo reemplaza en la lista.
    // Si algo falla se lanza un error y el pedido queda igual.
    public Pedido agregarIngrediente(String idPedido, String ingrediente) {
        Pedido actual = buscarPedido(idPedido);
        validarCompatibilidad(actual, ingrediente);

        // El decorador solo se crea si paso la validacion
        Pedido decorado = crearDecorador(ingrediente, actual);
        pedidos.set(buscarPosicion(idPedido), decorado);
        return decorado;
    }

    // Crea el decorador segun el nombre del ingrediente
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

    // Devuelve el pedido con ese ID, o null si no existe
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

    // Devuelve la lista de ingredientes que tiene un pedido.
    // La descripcion tiene la forma "Café + Azúcar + Crema", se separa por " + "
    // y se descarta el primer texto (que es el producto base).
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

    // Borra los pedidos creados. Los prototipos del registro no se tocan.
    public void reiniciar() {
        pedidos.clear();
        contador = 0;
    }
    // Calcula la suma total del precio de todos los pedidos registrados
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
