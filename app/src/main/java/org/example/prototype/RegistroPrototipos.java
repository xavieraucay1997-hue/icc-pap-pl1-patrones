package org.example.prototype;

import java.util.HashMap;

public class RegistroPrototipos {
    private HashMap<String, ProductoBase> prototipos = new HashMap<>();

    public void registrar(String clave, ProductoBase prototipo) {
         prototipos.put(clave.toLowerCase(), prototipo);
    }

    public boolean contiene (String clave) {
        return prototipos.containsKey(clave.toLowerCase());
    }

    public ProductoBase clonar(String clave, String nuevoId) {
        ProductoBase prototipo = prototipos.get(clave.toLowerCase());
        if(prototipo == null) {
            throw new IllegalArgumentException("No existe el producto : " + clave );
        }
        return prototipo.copiar(nuevoId);
    }


}
