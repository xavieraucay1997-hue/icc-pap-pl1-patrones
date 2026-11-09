package org.example.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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

public class VentanaPedidos extends JFrame {

    // ---------- Letras ----------
    private static final String LETRA = "Segoe UI";
    private static final String LETRA_EMOJI = "Segoe UI Emoji";
    private static final String LETRA_TITULO = "Georgia";

    // ---------- Colores (Marfil, negro y dorado) ----------
    private static final Color CAFE_ARRIBA = new Color(238, 234, 225);
    private static final Color CAFE_ABAJO = new Color(228, 223, 212);

    private static final Color TARJETA = new Color(245, 242, 235);
    private static final Color BORDE = new Color(201, 162, 39);
    private static final Color TEXTO = new Color(25, 25, 25);
    private static final Color TEXTO_SUAVE = new Color(105, 99, 88);
    private static final Color AMBAR = new Color(175, 132, 30);

    private static final Color CARAMELO = new Color(25, 25, 25);
    private static final Color CARAMELO_HOVER = new Color(190, 145, 35);
    private static final Color CARAMELO_PRESIONADO = new Color(145, 105, 20);
    private static final Color LETRA_OSCURA = Color.WHITE;

    private static final Color VERDE = new Color(218, 180, 75);
    private static final Color LETRA_VERDE = new Color(25, 25, 25);
    private static final Color ROJO = new Color(25, 25, 25);

    private static final Color CREMA = new Color(218, 180, 75);
    private static final Color CREMA_HOVER = new Color(230, 195, 100);
    private static final Color CREMA_PRESIONADO = new Color(190, 150, 50);

    private static final Color SUAVE = new Color(25, 25, 25);
    private static final Color SUAVE_HOVER = new Color(65, 65, 65);
    private static final Color SUAVE_PRESIONADO = new Color(190, 145, 35);

    private static final Color FILA_PAR = new Color(245, 242, 235);
    private static final Color FILA_IMPAR = new Color(232, 227, 216);
    private static final Color FILA_SELECCION = new Color(225, 207, 160);
    private ServicioPedidos servicio;

    private String[] nombresProductos = {"Café", "Ensalada", "Pizza"};
    private String[] emojisProductos = {"☕", "🥗", "🍕"};
    private String[] clavesProductos = {"cafe", "ensalada", "pizza"};

    private String[] ingredientes = {"Azúcar", "Crema", "Tomate", "Pollo", "Queso", "Pepperoni"};
    private String[] emojisIngredientes = {"🍬", "🥛", "🍅", "🍗", "🧀", "🍄‍🟫"};

    // Componentes
    private JButton btnReiniciar = new BotonRedondo("Reiniciar pedidos", SUAVE, SUAVE_HOVER,
            SUAVE_PRESIONADO, Color.WHITE, 15);

    private JButton btnEfectivo = new BotonRedondo("Pago en efectivo", CREMA, CREMA_HOVER,
            CREMA_PRESIONADO, LETRA_OSCURA, 17);

    private JButton btnTarjeta = new BotonRedondo("Pago con tarjeta", AMBAR, new Color(225, 195, 110),
            new Color(160, 120, 30), TEXTO, 17);

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
            setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
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

    private void armarVentana() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 820);
        setMinimumSize(new Dimension(1100, 760));
        setLocationRelativeTo(null);

        JPanel principal = new FondoDegradado(new BorderLayout(0, 12));
        principal.setBorder(BorderFactory.createEmptyBorder(16, 20, 8, 20));
        setContentPane(principal);

        JLabel titulo = new JLabel("PedidoLab");
        titulo.setFont(new Font(LETRA_TITULO, Font.BOLD, 38));
        titulo.setForeground(TEXTO);
        principal.add(titulo, BorderLayout.NORTH);

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

    private PanelRedondo crearTarjeta(String titulo) {
        PanelRedondo tarjeta = new PanelRedondo(new BorderLayout(0, 12), TARJETA, true);
        tarjeta.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));

        JLabel etiqueta = new JLabel(titulo);
        etiqueta.setFont(new Font(LETRA, Font.BOLD, 16));
        etiqueta.setForeground(AMBAR);
        tarjeta.add(etiqueta, BorderLayout.NORTH);
        return tarjeta;
    }

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

    private JPanel crearPanelPedidos() {
        PanelRedondo tarjeta = crearTarjeta("3. Pedidos creados");

        PanelRedondo aviso = new PanelRedondo(new BorderLayout(), new Color(218, 180, 75, 45), false);
        aviso.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        JLabel textoAviso = new JLabel("Revisar pedido");
        textoAviso.setFont(new Font(LETRA, Font.BOLD, 15));
        textoAviso.setForeground(AMBAR);
        aviso.add(textoAviso, BorderLayout.CENTER);

        // Se agrega la columna "-" al final para quitar rápido
        String[] columnas = {"ID", "Producto", "Descripción", "Total", "-"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return columna == 4;
            }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(40);
        tabla.setShowGrid(false);
        tabla.setIntercellSpacing(new Dimension(0, 0));
        tabla.setBackground(FILA_PAR);
        tabla.setFillsViewportHeight(true);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(80);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(210);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(70);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(40);
        tabla.setDefaultRenderer(Object.class, crearRenderCeldas());

        // Evento de clic rápido en la tabla para la columna de eliminar ("-")
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int fila = tabla.rowAtPoint(e.getPoint());
                int columna = tabla.columnAtPoint(e.getPoint());
                if (fila != -1 && columna == 4) {
                    String id = (String) modeloTabla.getValueAt(fila, 0);
                    servicio.removerPedido(id);
                    actualizarTabla(null);
                    mostrarMensaje("Pedido " + id + " eliminado");
                }
            }
        });

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

                if (columna == 3) {
                    setForeground(AMBAR);
                    setFont(new Font(LETRA_TITULO, Font.BOLD, 17));
                    setHorizontalAlignment(RIGHT);
                } else if (columna == 4) {
                    setForeground(AMBAR);
                    setFont(new Font(LETRA, Font.BOLD, 18));
                    setHorizontalAlignment(CENTER);
                } else {
                    setForeground(TEXTO);
                    setFont(new Font(LETRA, Font.PLAIN, 15));
                    setHorizontalAlignment(LEFT);
                }
                return this;
            }
        };
    }

    private DefaultTableCellRenderer crearRenderEncabezado() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object valor,
                    boolean seleccionada, boolean foco, int fila, int columna) {
                super.getTableCellRendererComponent(t, valor, false, false, fila, columna);
                setBackground(new Color(25, 25, 25));
                setForeground(AMBAR);
                setFont(new Font(LETRA, Font.BOLD, 14));
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, AMBAR),
                        BorderFactory.createEmptyBorder(0, 12, 0, 12)));
                if (columna == 3) {
                    setHorizontalAlignment(RIGHT);
                } else if (columna == 4) {
                    setHorizontalAlignment(CENTER);
                } else {
                    setHorizontalAlignment(LEFT);
                }
                return this;
            }
        };
    }

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

        lblPrecioDetalle.setFont(new Font(LETRA, Font.BOLD, 36));
        lblPrecioDetalle.setForeground(AMBAR);
        lblPrecioDetalle.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelCampos.add(etiquetaPrecio);
        panelCampos.add(lblPrecioDetalle);
        panelCampos.setVisible(false);

        JScrollPane scrollCampos = new JScrollPane(panelCampos);
        scrollCampos.setBorder(BorderFactory.createEmptyBorder());
        scrollCampos.setOpaque(false);
        scrollCampos.getViewport().setOpaque(false);

        JPanel contenido = new JPanel(new BorderLayout());
        contenido.setOpaque(false);
        contenido.add(lblSeleccione, BorderLayout.NORTH);
        contenido.add(scrollCampos, BorderLayout.CENTER);
        tarjeta.add(contenido, BorderLayout.CENTER);
        return tarjeta;
    }

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

    private JPanel crearZonaPago() {
        JPanel zona = new JPanel(new BorderLayout(0, 12));
        zona.setOpaque(false);

        PanelRedondo tarjetaTotal = new PanelRedondo(new GridLayout(2, 1), Color.WHITE, false);
        tarjetaTotal.setBorder(BorderFactory.createLineBorder(BORDE, 1));
        tarjetaTotal.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE, 1),
                BorderFactory.createEmptyBorder(12, 22, 12, 22)));
        tarjetaTotal.setPreferredSize(new Dimension(100, 112));

        JLabel texto = new JLabel("Total de todos los pedidos");
        texto.setFont(new Font(LETRA, Font.BOLD, 16));
        texto.setForeground(new Color(80, 80, 80));

        lblTotal.setFont(new Font(LETRA, Font.BOLD, 42));
        lblTotal.setForeground(new Color(30, 30, 30));

        tarjetaTotal.add(texto);
        tarjetaTotal.add(lblTotal);
        zona.add(tarjetaTotal, BorderLayout.NORTH);

        JPanel pagos = new JPanel(new GridLayout(1, 2, 12, 0));
        pagos.setOpaque(false);
        pagos.add(btnEfectivo);
        pagos.add(btnTarjeta);
        zona.add(pagos, BorderLayout.SOUTH);
        return zona;
    }

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

    private void agregarEventos() {
        btnReiniciar.addActionListener(e -> reiniciar());

        btnEfectivo.addActionListener(e -> mostrarMensaje("Pago en efectivo seleccionado (demostración)"));
        btnTarjeta.addActionListener(e -> mostrarMensaje("Pago con tarjeta seleccionado (demostración)"));

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDetalle();
            }
        });
    }

    private void crearPedido(String clave) {
        try {
            Pedido nuevo = servicio.crearPedido(clave);
            actualizarTabla(nuevo.id());
            mostrarMensaje("Pedido " + nuevo.id() + " creado");
        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        }
    }

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

    private void reiniciar() {
        servicio.reiniciar();
        actualizarTabla(null);
        mostrarMensaje("Pedidos reiniciados");
    }

    private void actualizarTabla(String idASeleccionar) {
        modeloTabla.setRowCount(0);
        List<Pedido> pedidos = servicio.getPedidos();
        for (Pedido p : pedidos) {
            modeloTabla.addRow(new Object[]{
                p.id(), nombreProducto(p.tipo()), p.descripcion(), "$" + p.precio(), "-"
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

    private void mostrarMensaje(String texto) {
        panelNotificacion.setColorFondo(VERDE);
        lblNotificacion.setForeground(LETRA_VERDE);
        lblNotificacion.setText("✔  " + texto);
        panelNotificacion.setVisible(true);
        temporizador.restart();
    }

    private void mostrarError(String texto) {
        panelNotificacion.setColorFondo(ROJO);
        lblNotificacion.setForeground(Color.WHITE);
        lblNotificacion.setText("✖  Operación rechazada: " + texto);
        panelNotificacion.setVisible(true);
        temporizador.restart();
    }
}