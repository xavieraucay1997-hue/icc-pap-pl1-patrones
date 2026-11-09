package org.example.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import org.example.modelo.Pedido;
import org.example.modelo.TipoProducto;
import org.example.servicio.ServicioPedidos;

// Ventana unica de la aplicacion.
// Solo captura eventos y muestra datos: toda la logica esta en ServicioPedidos.
public class VentanaPedidos extends JFrame {

    // ---------- Letras ----------
    private static final String LETRA = "Segoe UI";
    private static final String LETRA_EMOJI = "Segoe UI Emoji";
    private static final String LETRA_TITULO = "Georgia";

    // ---------- Colores (fondo cafe en degradado, acentos vivos) ----------
    private static final Color CAFE_ARRIBA = new Color(104, 66, 42);
    private static final Color CAFE_ABAJO = new Color(20, 13, 10);
    private static final Color TARJETA = new Color(30, 21, 16, 215);
    private static final Color BORDE = new Color(255, 255, 255, 35);
    private static final Color TEXTO = new Color(245, 239, 230);
    private static final Color TEXTO_SUAVE = new Color(190, 172, 155);
    private static final Color AMBAR = new Color(240, 184, 96);

    private static final Color CARAMELO = new Color(201, 132, 68);
    private static final Color CARAMELO_HOVER = new Color(228, 160, 92);
    private static final Color CARAMELO_PRESIONADO = new Color(166, 104, 48);
    private static final Color LETRA_OSCURA = new Color(36, 22, 14);

    private static final Color VERDE = new Color(30, 215, 96);
    private static final Color LETRA_VERDE = new Color(8, 40, 20);
    private static final Color ROJO = new Color(214, 69, 65);

    private static final Color CREMA = new Color(245, 239, 230);
    private static final Color CREMA_HOVER = Color.WHITE;
    private static final Color CREMA_PRESIONADO = new Color(214, 204, 190);

    private static final Color SUAVE = new Color(255, 255, 255, 40);
    private static final Color SUAVE_HOVER = new Color(255, 255, 255, 70);
    private static final Color SUAVE_PRESIONADO = new Color(255, 255, 255, 95);

    private static final Color FILA_PAR = new Color(38, 28, 22);
    private static final Color FILA_IMPAR = new Color(46, 34, 27);
    private static final Color FILA_SELECCION = new Color(125, 80, 44);

    private ServicioPedidos servicio;

    // Productos (la clave es la misma que usa el registro de prototipos)
    private String[] nombresProductos = {"Café", "Ensalada", "Pizza"};
    private String[] emojisProductos = {"☕", "🥗", "🍕"};
    private String[] clavesProductos = {"cafe", "ensalada", "pizza"};

    // Ingredientes (el nombre debe ser igual al de ServicioPedidos)
    private String[] ingredientes = {"Azúcar", "Crema", "Tomate", "Pollo", "Queso", "Pepperoni"};
    private String[] emojisIngredientes = {"🍬", "🥛", "🍅", "🍗", "🧀", "🌶️"};

    // Componentes
    private JButton btnReiniciar = new BotonRedondo("Reiniciar pedidos", SUAVE, SUAVE_HOVER,
            SUAVE_PRESIONADO, TEXTO, 15);
    private JButton btnEfectivo = new BotonRedondo("💵  Pago en efectivo", CREMA, CREMA_HOVER,
            CREMA_PRESIONADO, LETRA_OSCURA, 17);
    private JButton btnTarjeta = new BotonRedondo("💳  Pago con tarjeta", AMBAR, new Color(255, 205, 130),
            new Color(205, 150, 70), LETRA_OSCURA, 17);

    private DefaultTableModel modeloTabla;
    private JTable tabla;

    private JLabel lblSeleccione = new JLabel("Seleccione un pedido de la lista.");
    private JPanel panelCampos = new JPanel();
    private JLabel lblId = new JLabel();
    private JLabel lblProducto = new JLabel();
    private JLabel lblDescripcion = new JLabel();
    private JLabel lblIngredientes = new JLabel();
    private JLabel lblPrecioDetalle = new JLabel();
    private JLabel lblTotal = new JLabel("$0.00");

    // Notificacion que aparece unos segundos (pedido creado, ingrediente agregado, errores)
    private PanelRedondo panelNotificacion = new PanelRedondo(new BorderLayout(), VERDE, false);
    private JLabel lblNotificacion = new JLabel();
    private Timer temporizador = new Timer(3000, e -> panelNotificacion.setVisible(false));

    public VentanaPedidos(ServicioPedidos servicio) {
        super("Pedido");
        this.servicio = servicio;
        armarVentana();
        agregarEventos();
        mostrarDetalle();
    }

    // ---------- Clases para el diseño ----------

    // Fondo con degradado cafe (de arriba hacia abajo)
    private static class FondoDegradado extends JPanel {
        public FondoDegradado(LayoutManager layout) {
            super(layout);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setPaint(new GradientPaint(0, 0, CAFE_ARRIBA, 0, getHeight(), CAFE_ABAJO));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }

    // Panel con fondo de esquinas redondeadas
    private static class PanelRedondo extends JPanel {
        private Color colorFondo;
        private boolean conBorde;

        public PanelRedondo(LayoutManager layout, Color colorFondo, boolean conBorde) {
            super(layout);
            this.colorFondo = colorFondo;
            this.conBorde = conBorde;
            setOpaque(false);
        }

        public void setColorFondo(Color color) {
            this.colorFondo = color;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(colorFondo);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 24);

            if (conBorde) {
                g2.setColor(BORDE);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 24);
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }

    // Boton con esquinas redondeadas que cambia de color al pasar el mouse
    private static class BotonRedondo extends JButton {
        private Color base;
        private Color hover;
        private Color presionado;

        public BotonRedondo(String texto, Color base, Color hover, Color presionado,
                            Color letra, int tamano) {
            super(texto);
            this.base = base;
            this.hover = hover;
            this.presionado = presionado;

            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setRolloverEnabled(true);
            setForeground(letra);
            setFont(new Font(LETRA_EMOJI, Font.BOLD, tamano));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (getModel().isPressed()) {
                g2.setColor(presionado);
            } else if (getModel().isRollover()) {
                g2.setColor(hover);
            } else {
                g2.setColor(base);
            }

            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ---------- Construccion de la ventana ----------

    private void armarVentana() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 820);
        setMinimumSize(new Dimension(1100, 760));
        setLocationRelativeTo(null);

        JPanel principal = new FondoDegradado(new BorderLayout(0, 12));
        principal.setBorder(BorderFactory.createEmptyBorder(16, 20, 8, 20));
        setContentPane(principal);

        // Titulo
        JLabel titulo = new JLabel("Pedido");
        titulo.setFont(new Font(LETRA_TITULO, Font.BOLD, 38));
        titulo.setForeground(TEXTO);
        principal.add(titulo, BorderLayout.NORTH);

        // Dos columnas: izquierda (producto y pedidos), derecha (ingredientes, detalle, total y pago)
        JPanel centro = new JPanel(new GridLayout(1, 2, 16, 0));
        centro.setOpaque(false);

        JPanel izquierda = new JPanel(new BorderLayout(0, 14));
        izquierda.setOpaque(false);
        izquierda.add(crearPanelProducto(), BorderLayout.NORTH);
        izquierda.add(crearPanelPedidos(), BorderLayout.CENTER);

        JPanel derecha = new JPanel(new BorderLayout(0, 14));
        derecha.setOpaque(false);
        derecha.add(crearPanelIngredientes(), BorderLayout.NORTH);
        derecha.add(crearPanelDetalle(), BorderLayout.CENTER);
        derecha.add(crearZonaPago(), BorderLayout.SOUTH);

        centro.add(izquierda);
        centro.add(derecha);
        principal.add(centro, BorderLayout.CENTER);

        principal.add(crearZonaNotificacion(), BorderLayout.SOUTH);
    }

    // Crea una tarjeta oscura redondeada con un titulo arriba
    private PanelRedondo crearTarjeta(String titulo) {
        PanelRedondo tarjeta = new PanelRedondo(new BorderLayout(0, 12), TARJETA, true);
        tarjeta.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));

        JLabel etiqueta = new JLabel(titulo);
        etiqueta.setFont(new Font(LETRA, Font.BOLD, 16));
        etiqueta.setForeground(AMBAR);
        tarjeta.add(etiqueta, BorderLayout.NORTH);

        return tarjeta;
    }

    // Panel 1: un boton por producto. Un clic crea el pedido.
    private JPanel crearPanelProducto() {
        PanelRedondo tarjeta = crearTarjeta("1. Elegir producto base");

        JPanel fila = new JPanel(new GridLayout(1, 3, 12, 0));
        fila.setOpaque(false);

        for (int i = 0; i < nombresProductos.length; i++) {
            final int posicion = i;

            JButton boton = new BotonRedondo(emojisProductos[i] + "  " + nombresProductos[i],
                    CARAMELO, CARAMELO_HOVER, CARAMELO_PRESIONADO, LETRA_OSCURA, 18);

            boton.addActionListener(e -> crearPedido(clavesProductos[posicion]));
            fila.add(boton);
        }

        tarjeta.add(fila, BorderLayout.CENTER);
        return tarjeta;
    }

    // Panel 2: un boton por ingrediente. Un clic lo agrega al pedido seleccionado.
    private JPanel crearPanelIngredientes() {
        PanelRedondo tarjeta = crearTarjeta("2. Agregar ingredientes al pedido seleccionado");

        JPanel cuadricula = new JPanel(new GridLayout(2, 3, 12, 12));
        cuadricula.setOpaque(false);

        for (int i = 0; i < ingredientes.length; i++) {
            final String nombre = ingredientes[i];

            JButton boton = new BotonRedondo(emojisIngredientes[i] + "  " + nombre,
                    CARAMELO, CARAMELO_HOVER, CARAMELO_PRESIONADO, LETRA_OSCURA, 17);

            boton.addActionListener(e -> agregarIngrediente(nombre));
            cuadricula.add(boton);
        }

        tarjeta.add(cuadricula, BorderLayout.CENTER);
        return tarjeta;
    }

    // Panel 3: tabla de pedidos creados
    private JPanel crearPanelPedidos() {
        PanelRedondo tarjeta = crearTarjeta("3. Pedidos creados");

        // Aviso para que el usuario sepa donde seleccionar
        PanelRedondo aviso = new PanelRedondo(new BorderLayout(), new Color(240, 184, 96, 45), false);
        aviso.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));

        JLabel textoAviso = new JLabel("AQUÍ SELECCIONA TU PEDIDO");
        textoAviso.setFont(new Font(LETRA, Font.BOLD, 15));
        textoAviso.setForeground(AMBAR);
        aviso.add(textoAviso, BorderLayout.CENTER);

        String[] columnas = {"ID", "Producto", "Descripción", "Total", "Eliminar"};

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(40);
        tabla.setShowGrid(false);
        tabla.setIntercellSpacing(new Dimension(0, 0));
        tabla.setBackground(FILA_PAR);
        tabla.setFillsViewportHeight(true);

        tabla.getColumnModel().getColumn(0).setPreferredWidth(60);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(90);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(230);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(80);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(65);

        tabla.setDefaultRenderer(Object.class, crearRenderCeldas());

        JTableHeader encabezado = tabla.getTableHeader();
        encabezado.setReorderingAllowed(false);
        encabezado.setPreferredSize(new Dimension(100, 38));
        encabezado.setDefaultRenderer(crearRenderEncabezado());

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(FILA_PAR);

        JPanel contenido = new JPanel(new BorderLayout(0, 10));
        contenido.setOpaque(false);
        contenido.add(aviso, BorderLayout.NORTH);
        contenido.add(scroll, BorderLayout.CENTER);

        tarjeta.add(contenido, BorderLayout.CENTER);

        JPanel abajo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        abajo.setOpaque(false);
        abajo.add(btnReiniciar);
        tarjeta.add(abajo, BorderLayout.SOUTH);

        return tarjeta;
    }

    // Como se dibuja cada celda de la tabla (filas alternadas, total resaltado)
    private DefaultTableCellRenderer crearRenderCeldas() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object valor,
                    boolean seleccionada, boolean foco, int fila, int columna) {

                super.getTableCellRendererComponent(t, valor, false, false, fila, columna);

                setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));

                if (seleccionada) {
                    setBackground(FILA_SELECCION);
                } else if (fila % 2 == 0) {
                    setBackground(FILA_PAR);
                } else {
                    setBackground(FILA_IMPAR);
                }

                if (columna == 4) {
                    setForeground(ROJO);
                    setFont(new Font(LETRA, Font.BOLD, 20));
                    setHorizontalAlignment(CENTER);
                } else if (columna == 3) {
                    setForeground(AMBAR);
                    setFont(new Font(LETRA_TITULO, Font.BOLD, 17));
                    setHorizontalAlignment(RIGHT);
                } else {
                    setForeground(TEXTO);
                    setFont(new Font(LETRA, Font.PLAIN, 15));
                    setHorizontalAlignment(LEFT);
                }

                return this;
            }
        };
    }

    // Como se dibuja el encabezado de la tabla
    private DefaultTableCellRenderer crearRenderEncabezado() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object valor,
                    boolean seleccionada, boolean foco, int fila, int columna) {

                super.getTableCellRendererComponent(t, valor, false, false, fila, columna);

                setBackground(new Color(30, 21, 16));
                setForeground(AMBAR);
                setFont(new Font(LETRA, Font.BOLD, 14));

                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, AMBAR),
                        BorderFactory.createEmptyBorder(0, 12, 0, 12)));

                if (columna == 4) {
                    setHorizontalAlignment(CENTER);
                } else if (columna == 3) {
                    setHorizontalAlignment(RIGHT);
                } else {
                    setHorizontalAlignment(LEFT);
                }

                return this;
            }
        };
    }

    // Panel 4: detalle del pedido seleccionado
    private JPanel crearPanelDetalle() {
        PanelRedondo tarjeta = crearTarjeta("4. Detalle del pedido seleccionado");

        lblSeleccione.setFont(new Font(LETRA, Font.PLAIN, 17));
        lblSeleccione.setForeground(TEXTO_SUAVE);

        panelCampos.setLayout(new BoxLayout(panelCampos, BoxLayout.Y_AXIS));
        panelCampos.setOpaque(false);

        agregarCampo("ID", lblId);
        agregarCampo("Producto", lblProducto);
        agregarCampo("Descripción", lblDescripcion);
        agregarCampo("Ingredientes agregados", lblIngredientes);

        JLabel etiquetaPrecio = new JLabel("Total del pedido");
        etiquetaPrecio.setFont(new Font(LETRA, Font.PLAIN, 14));
        etiquetaPrecio.setForeground(TEXTO_SUAVE);
        etiquetaPrecio.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblPrecioDetalle.setFont(new Font(LETRA_TITULO, Font.BOLD, 40));
        lblPrecioDetalle.setForeground(AMBAR);
        lblPrecioDetalle.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelCampos.add(etiquetaPrecio);
        panelCampos.add(lblPrecioDetalle);
        panelCampos.setVisible(false);

        JPanel contenido = new JPanel(new BorderLayout());
        contenido.setOpaque(false);
        contenido.add(lblSeleccione, BorderLayout.NORTH);
        contenido.add(panelCampos, BorderLayout.CENTER);

        tarjeta.add(contenido, BorderLayout.CENTER);
        return tarjeta;
    }

    // Agrega un campo al detalle
    private void agregarCampo(String titulo, JLabel valor) {
        JLabel etiqueta = new JLabel(titulo);
        etiqueta.setFont(new Font(LETRA, Font.PLAIN, 14));
        etiqueta.setForeground(TEXTO_SUAVE);
        etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);

        valor.setFont(new Font(LETRA, Font.BOLD, 19));
        valor.setForeground(TEXTO);
        valor.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelCampos.add(etiqueta);
        panelCampos.add(valor);
        panelCampos.add(Box.createVerticalStrut(8));
    }

    // Zona de abajo a la derecha: total de todos los pedidos y botones de pago
    private JPanel crearZonaPago() {
        JPanel zona = new JPanel(new BorderLayout(0, 12));
        zona.setOpaque(false);

        // Tarjeta verde con el total de todos los pedidos
        PanelRedondo tarjetaTotal = new PanelRedondo(new GridLayout(2, 1), VERDE, false);
        tarjetaTotal.setBorder(BorderFactory.createEmptyBorder(12, 22, 12, 22));
        tarjetaTotal.setPreferredSize(new Dimension(100, 112));

        JLabel texto = new JLabel("Total de todos los pedidos");
        texto.setFont(new Font(LETRA, Font.BOLD, 16));
        texto.setForeground(LETRA_VERDE);

        lblTotal.setFont(new Font(LETRA_TITULO, Font.BOLD, 44));
        lblTotal.setForeground(LETRA_VERDE);

        tarjetaTotal.add(texto);
        tarjetaTotal.add(lblTotal);
        zona.add(tarjetaTotal, BorderLayout.NORTH);

        // Botones de pago (solo de muestra: no cambian ningun pedido)
        JPanel pagos = new JPanel(new GridLayout(1, 2, 12, 0));
        pagos.setOpaque(false);
        pagos.add(btnEfectivo);
        pagos.add(btnTarjeta);
        zona.add(pagos, BorderLayout.SOUTH);

        return zona;
    }

    // Espacio fijo abajo donde aparece la notificacion
    private JPanel crearZonaNotificacion() {
        JPanel zona = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 6));
        zona.setOpaque(false);
        zona.setPreferredSize(new Dimension(100, 60));

        panelNotificacion.setBorder(BorderFactory.createEmptyBorder(8, 26, 8, 26));
        lblNotificacion.setFont(new Font(LETRA_EMOJI, Font.BOLD, 18));
        panelNotificacion.add(lblNotificacion, BorderLayout.CENTER);
        panelNotificacion.setVisible(false);

        temporizador.setRepeats(false);
        zona.add(panelNotificacion);

        return zona;
    }

    // ---------- Eventos ----------

    private void agregarEventos() {
        btnReiniciar.addActionListener(e -> reiniciar());

        // Los botones de pago solo muestran un aviso
        btnEfectivo.addActionListener(e -> mostrarMensaje("Pago en efectivo seleccionado (demostración)"));
        btnTarjeta.addActionListener(e -> mostrarMensaje("Pago con tarjeta seleccionado (demostración)"));

        // Al elegir una fila se muestra su detalle
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDetalle();
            }
        });

        // Al hacer clic en la X se elimina solamente ese pedido
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tabla.rowAtPoint(e.getPoint());
                int columna = tabla.columnAtPoint(e.getPoint());

                if (fila != -1 && columna == 4) {
                    String id = (String) modeloTabla.getValueAt(fila, 0);
                    eliminarPedido(id);
                }
            }
        });
    }

    // Boton de producto: el servicio clona el prototipo del registro
    private void crearPedido(String clave) {
        try {
            Pedido nuevo = servicio.crearPedido(clave);
            actualizarTabla(nuevo.id());
            mostrarMensaje("Pedido " + nuevo.id() + " creado");
        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        }
    }

    // Boton de ingrediente: el servicio valida y decora el pedido seleccionado
    private void agregarIngrediente(String ingrediente) {
        String id = idSeleccionado();

        try {
            servicio.agregarIngrediente(id, ingrediente);
            actualizarTabla(id);
            mostrarMensaje(ingrediente + " agregado al pedido " + id);
        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        }
    }

    // Elimina un unico pedido usando su ID
    private void eliminarPedido(String id) {
        if (servicio.removerPedido(id)) {
            actualizarTabla(null);
            mostrarMensaje("Pedido " + id + " eliminado");
        }
    }

    // Boton "Reiniciar pedidos": borra los pedidos, los prototipos no se tocan
    private void reiniciar() {
        servicio.reiniciar();
        actualizarTabla(null);
        mostrarMensaje("Pedidos reiniciados");
    }

    // ---------- Actualizacion de la vista ----------

    // Vuelve a llenar la tabla con los pedidos del servicio.
    // Si se indica un ID, deja esa fila seleccionada.
    private void actualizarTabla(String idASeleccionar) {
        modeloTabla.setRowCount(0);

        List<Pedido> pedidos = servicio.getPedidos();

        for (Pedido p : pedidos) {
            modeloTabla.addRow(new Object[]{
                p.id(),
                nombreProducto(p.tipo()),
                p.descripcion(),
                "$" + p.precio(),
                "X"
            });
        }

        lblTotal.setText("$" + servicio.totalGeneral());

        if (idASeleccionar != null) {
            for (int fila = 0; fila < modeloTabla.getRowCount(); fila++) {
                if (modeloTabla.getValueAt(fila, 0).equals(idASeleccionar)) {
                    tabla.setRowSelectionInterval(fila, fila);
                }
            }
        }

        mostrarDetalle();
    }

    // Llena el panel de detalle con el pedido seleccionado
    private void mostrarDetalle() {
        String id = idSeleccionado();
        Pedido pedido = null;

        if (id != null) {
            pedido = servicio.buscarPedido(id);
        }

        if (pedido == null) {
            lblSeleccione.setVisible(true);
            panelCampos.setVisible(false);
            return;
        }

        List<String> lista = servicio.ingredientesAgregados(pedido);
        String ingredientesTexto = "Ninguno";

        if (!lista.isEmpty()) {
            ingredientesTexto = String.join(", ", lista);
        }

        lblId.setText(pedido.id());
        lblProducto.setText(nombreProducto(pedido.tipo()));
        lblDescripcion.setText(pedido.descripcion());
        lblIngredientes.setText(ingredientesTexto);
        lblPrecioDetalle.setText("$" + pedido.precio());

        lblSeleccione.setVisible(false);
        panelCampos.setVisible(true);
    }

    // ID de la fila seleccionada (null si no hay ninguna)
    private String idSeleccionado() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            return null;
        }

        return (String) modeloTabla.getValueAt(fila, 0);
    }

    private String nombreProducto(TipoProducto tipo) {
        switch (tipo) {
            case CAFE:
                return "Café";
            case ENSALADA:
                return "Ensalada";
            default:
                return "Pizza";
        }
    }

    // Notificacion verde (todo salio bien)
    private void mostrarMensaje(String texto) {
        panelNotificacion.setColorFondo(VERDE);
        lblNotificacion.setForeground(LETRA_VERDE);
        lblNotificacion.setText("✔  " + texto);
        panelNotificacion.setVisible(true);
        temporizador.restart();
    }

    // Notificacion roja (operacion rechazada) y ventana de aviso
    private void mostrarError(String texto) {
        panelNotificacion.setColorFondo(ROJO);
        lblNotificacion.setForeground(Color.WHITE);
        lblNotificacion.setText("✖  Operación rechazada: " + texto);
        panelNotificacion.setVisible(true);
        temporizador.restart();

        JOptionPane.showMessageDialog(this, texto, "Operación rechazada", JOptionPane.WARNING_MESSAGE);
    }
}