package taller;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import static taller.catalogo.*;

public class pedido extends JFrame {

    private static final long serialVersionUID = 1L;

    // Estado del pedido 
    private int[] cantidades  = {0, 0, 0};          // azul, rojo, negro
    private String[] acabados = {"CROMADO", "CROMADO", "CROMADO"};
    private static final int[] PRECIOS = {200, 100}; // CROMADO, PINTADO

    // Labels de cantidad y subtotal por color
    private JLabel[] qtyLabels = new JLabel[3];
    private JLabel[] subLabels = new JLabel[3];

    // Botones de acabado para poder cambiar su estado visual
    private JButton[][] btnAcabado = new JButton[3][2]; // [color][0=CROMADO,1=PINTADO]

    private static final String[] COLORES_NOMBRES = {"AZUL", "ROJO", "NEGRO"};
    private static final String[] REFS = {"REF-AS-001", "REF-AS-002", "REF-AS-003"};
    private static final Color[] BARRAS = {C_AZUL_AS, C_ROJO_AS, C_PLATA};

    public pedido() {
        setTitle("TALLERASFIG — Orden de Trabajo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 680);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(C_NEGRO);
        setContentPane(root);

        root.add(buildCabecera(), BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(C_NEGRO);
        centro.add(buildRack(1), BorderLayout.NORTH);

        // ── Contenido principal ──────────────────────────────────────────
        JPanel wrap = new JPanel();
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setBackground(C_NEGRO);
        wrap.setBorder(new EmptyBorder(24, 32, 32, 32));

        wrap.add(buildSecTitulo("ORDEN DE TRABAJO"));
        wrap.add(buildSecSub("// selecciona acabado y cantidad por referencia"));
        wrap.add(Box.createVerticalStrut(16));

        // Grid de 3 columnas
        JPanel grid = new JPanel(new GridLayout(1, 3, 2, 0));
        grid.setBackground(C_GRAFITO);
        grid.setBorder(new LineBorder(C_GRAFITO, 2));
        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 340));

        for (int i = 0; i < 3; i++) {
            grid.add(buildItemPedido(i));
        }
        wrap.add(grid);
        wrap.add(Box.createVerticalStrut(20));

        // Info de entrega (simplificado — fecha + teléfono)
        wrap.add(buildEntregaBox());
        wrap.add(Box.createVerticalStrut(20));

        // Botones
        JPanel btnFila = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnFila.setBackground(C_NEGRO);
        btnFila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JButton btnVolver = buildBotonSecundario("← VOLVER");
        btnVolver.addActionListener(e -> {
            new catalogo().setVisible(true);
            dispose();
        });

        JButton btnSiguiente = buildBotonNaranja("GENERAR TICKET  →");
        btnSiguiente.addActionListener(e -> intentarContinuar());

        btnFila.add(btnVolver);
        btnFila.add(btnSiguiente);
        wrap.add(btnFila);

        JScrollPane scroll = new JScrollPane(wrap);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(C_NEGRO);
        centro.add(scroll, BorderLayout.CENTER);

        root.add(centro, BorderLayout.CENTER);
    }

    // ─── Panel de entrega (fecha y teléfono) ─────────────────────────────
    private JTextField campoFecha;
    private JTextField campoTelefono;
    private JTextField campoDireccion;
    private JLabel alertaVacia, alertaFecha, alertaTelefono;
    private String tipoEntrega = "TALLER";

    private JPanel buildEntregaBox() {
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(C_ACERO);
        box.setBorder(new CompoundBorder(
                new LineBorder(C_GRAFITO, 2),
                new EmptyBorder(16, 20, 16, 20)));
        box.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

        JLabel tituloEnt = new JLabel("DATOS DE ENTREGA");
        tituloEnt.setFont(new Font("Arial Black", Font.BOLD, 16));
        tituloEnt.setForeground(C_AMARILLO);
        box.add(tituloEnt);
        box.add(Box.createVerticalStrut(12));

        // Fila: fecha + teléfono
        JPanel filaInputs = new JPanel(new GridLayout(1, 2, 20, 0));
        filaInputs.setOpaque(false);
        filaInputs.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JPanel grupoFecha = buildGrupoInput("FECHA DE ENTREGA");
        campoFecha = (JTextField) ((JPanel)grupoFecha.getComponent(1)).getComponent(0);

        JPanel grupoTel = buildGrupoInput("TELÉFONO (10 dígitos)");
        campoTelefono = (JTextField) ((JPanel)grupoTel.getComponent(1)).getComponent(0);

        filaInputs.add(grupoFecha);
        filaInputs.add(grupoTel);
        box.add(filaInputs);
        box.add(Box.createVerticalStrut(10));

        // Tipo entrega
        JPanel filaEntrega = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 4));
        filaEntrega.setOpaque(false);
        filaEntrega.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        JLabel lblTipo = new JLabel("ENTREGA: ");
        lblTipo.setFont(F_MONO);
        lblTipo.setForeground(C_GRIS);
        JButton btnTaller = buildBotonAcabadoSmall("TALLER (GRATIS)");
        JButton btnDom    = buildBotonAcabadoSmall("A DOMICILIO (+$150)");
        btnTaller.setBackground(C_NARANJA);
        btnTaller.setForeground(C_NEGRO);
        btnTaller.addActionListener(e -> {
            tipoEntrega = "TALLER";
            btnTaller.setBackground(C_NARANJA); btnTaller.setForeground(C_NEGRO);
            btnDom.setBackground(C_METAL);      btnDom.setForeground(C_GRIS);
        });
        btnDom.addActionListener(e -> {
            tipoEntrega = "DOMICILIO";
            btnDom.setBackground(C_NARANJA);    btnDom.setForeground(C_NEGRO);
            btnTaller.setBackground(C_METAL);   btnTaller.setForeground(C_GRIS);
        });
        filaEntrega.add(lblTipo);
        filaEntrega.add(Box.createHorizontalStrut(8));
        filaEntrega.add(btnTaller);
        filaEntrega.add(Box.createHorizontalStrut(6));
        filaEntrega.add(btnDom);
        box.add(filaEntrega);
        box.add(Box.createVerticalStrut(6));

        // Dirección (solo domicilio)
        JPanel grupoDom = buildGrupoInput("DIRECCIÓN (si es a domicilio)");
        campoDireccion = (JTextField) ((JPanel)grupoDom.getComponent(1)).getComponent(0);
        grupoDom.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        box.add(grupoDom);
        box.add(Box.createVerticalStrut(6));

        // Alertas
        alertaVacia     = buildAlerta("⚠  Debes seleccionar al menos 1 pieza.");
        alertaFecha     = buildAlerta("⚠  Fecha inválida (mínimo 3 días desde hoy).");
        alertaTelefono  = buildAlerta("⚠  El teléfono debe tener exactamente 10 dígitos.");
        box.add(alertaVacia);
        box.add(alertaFecha);
        box.add(alertaTelefono);

        return box;
    }

    private JPanel buildGrupoInput(String label) {
        JPanel g = new JPanel();
        g.setLayout(new BoxLayout(g, BoxLayout.Y_AXIS));
        g.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Courier New", Font.PLAIN, 10));
        lbl.setForeground(C_GRIS);
        g.add(lbl);
        JPanel inputWrap = new JPanel(new BorderLayout());
        inputWrap.setOpaque(false);
        JTextField tf = new JTextField();
        tf.setBackground(C_METAL);
        tf.setForeground(C_BLANCO);
        tf.setCaretColor(C_NARANJA);
        tf.setFont(F_MONO);
        tf.setBorder(new CompoundBorder(
                new MatteBorder(0, 4, 0, 0, C_AMARILLO),
                new EmptyBorder(6, 8, 6, 8)));
        inputWrap.add(tf, BorderLayout.CENTER);
        g.add(inputWrap);
        return g;
    }

    private JButton buildBotonAcabadoSmall(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Courier New", Font.BOLD, 10));
        btn.setBackground(C_METAL);
        btn.setForeground(C_GRIS);
        btn.setBorder(new LineBorder(C_GRAFITO, 1));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JLabel buildAlerta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(F_MONO);
        lbl.setForeground(new Color(0xE07070));
        lbl.setBorder(new CompoundBorder(
                new MatteBorder(1, 4, 1, 1, C_ROJO_ERR),
                new EmptyBorder(6, 10, 6, 10)));
        lbl.setBackground(new Color(192, 57, 43, 40));
        lbl.setOpaque(true);
        lbl.setVisible(false);
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        return lbl;
    }

    // ─── Item de pedido por color ─────────────────────────────────────────
    private JPanel buildItemPedido(int idx) {
        JPanel item = new JPanel();
        item.setLayout(new BoxLayout(item, BoxLayout.Y_AXIS));
        item.setBackground(C_ACERO);
        item.setBorder(new EmptyBorder(16, 16, 16, 16));

        // Barra de color
        JPanel barra = new JPanel();
        barra.setBackground(BARRAS[idx]);
        barra.setMaximumSize(new Dimension(Integer.MAX_VALUE, 4));
        item.add(barra);
        item.add(Box.createVerticalStrut(10));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new MatteBorder(0, 0, 1, 0, C_GRAFITO));
        JLabel nombre = new JLabel(COLORES_NOMBRES[idx]);
        nombre.setFont(new Font("Arial Black", Font.BOLD, 18));
        nombre.setForeground(C_BLANCO);
        JLabel ref = new JLabel(REFS[idx]);
        ref.setFont(new Font("Courier New", Font.PLAIN, 10));
        ref.setForeground(C_GRIS);
        JPanel headerTextos = new JPanel();
        headerTextos.setOpaque(false);
        headerTextos.setLayout(new BoxLayout(headerTextos, BoxLayout.Y_AXIS));
        headerTextos.add(nombre);
        headerTextos.add(ref);
        header.add(headerTextos, BorderLayout.WEST);
        item.add(header);
        item.add(Box.createVerticalStrut(14));

        // Acabado
        JLabel lblAcabado = new JLabel("ACABADO");
        lblAcabado.setFont(new Font("Courier New", Font.PLAIN, 10));
        lblAcabado.setForeground(C_GRIS);
        item.add(lblAcabado);
        item.add(Box.createVerticalStrut(4));

        JPanel grupoAcabado = new JPanel(new GridLayout(1, 2, 4, 0));
        grupoAcabado.setOpaque(false);
        grupoAcabado.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        btnAcabado[idx][0] = buildBtnAcabadoEstilo("CROMADO", true);
        btnAcabado[idx][1] = buildBtnAcabadoEstilo("PINTADO",  false);

        final int ii = idx;
        btnAcabado[idx][0].addActionListener(e -> seleccionarAcabado(ii, 0));
        btnAcabado[idx][1].addActionListener(e -> seleccionarAcabado(ii, 1));

        grupoAcabado.add(btnAcabado[idx][0]);
        grupoAcabado.add(btnAcabado[idx][1]);
        item.add(grupoAcabado);
        item.add(Box.createVerticalStrut(14));

        // Cantidad
        JLabel lblCant = new JLabel("CANTIDAD");
        lblCant.setFont(new Font("Courier New", Font.PLAIN, 10));
        lblCant.setForeground(C_GRIS);
        item.add(lblCant);
        item.add(Box.createVerticalStrut(4));

        JPanel qtyCtrl = new JPanel(new BorderLayout());
        qtyCtrl.setBackground(C_METAL);
        qtyCtrl.setBorder(new LineBorder(C_GRAFITO, 1));
        qtyCtrl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JButton btnMenos = buildQtyBtn("−");
        JButton btnMas   = buildQtyBtn("+");
        qtyLabels[idx] = new JLabel("0", JLabel.CENTER);
        qtyLabels[idx].setFont(new Font("Courier New", Font.BOLD, 20));
        qtyLabels[idx].setForeground(C_BLANCO);
        qtyLabels[idx].setBorder(new MatteBorder(0, 1, 0, 1, C_GRAFITO));

        btnMenos.addActionListener(e -> cambiarCantidad(ii, -1));
        btnMas.addActionListener(e   -> cambiarCantidad(ii, +1));

        qtyCtrl.add(btnMenos,      BorderLayout.WEST);
        qtyCtrl.add(qtyLabels[idx],BorderLayout.CENTER);
        qtyCtrl.add(btnMas,        BorderLayout.EAST);
        item.add(qtyCtrl);
        item.add(Box.createVerticalStrut(8));

        // Subtotal
        JPanel subFila = new JPanel(new BorderLayout());
        subFila.setOpaque(false);
        subFila.setBorder(new MatteBorder(1, 0, 0, 0, new Color(0x3D3D3D, false) {
            // dashed emulation via color transparente por defecto
        }));
        JLabel subLbl = new JLabel("SUBTOTAL:");
        subLbl.setFont(F_MONO);
        subLbl.setForeground(C_GRIS);
        subLabels[idx] = new JLabel("$0");
        subLabels[idx].setFont(new Font("Courier New", Font.BOLD, 14));
        subLabels[idx].setForeground(C_NARANJA);
        subFila.add(subLbl,         BorderLayout.WEST);
        subFila.add(subLabels[idx], BorderLayout.EAST);
        item.add(subFila);

        return item;
    }

    private JButton buildBtnAcabadoEstilo(String texto, boolean activo) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Courier New", Font.BOLD, 11));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        if (activo) {
            btn.setBackground(C_NARANJA);
            btn.setForeground(C_NEGRO);
            btn.setBorder(new LineBorder(C_NARANJA, 1));
        } else {
            btn.setBackground(C_METAL);
            btn.setForeground(C_GRIS);
            btn.setBorder(new LineBorder(C_GRAFITO, 1));
        }
        return btn;
    }

    private JButton buildQtyBtn(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Courier New", Font.BOLD, 20));
        btn.setBackground(C_METAL);
        btn.setForeground(C_NARANJA);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(40, 40));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(C_GRAFITO); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(C_METAL);   }
        });
        return btn;
    }

    private void seleccionarAcabado(int idx, int tipo) {
        acabados[idx] = (tipo == 0) ? "CROMADO" : "PINTADO";
        btnAcabado[idx][0].setBackground(tipo == 0 ? C_NARANJA : C_METAL);
        btnAcabado[idx][0].setForeground(tipo == 0 ? C_NEGRO   : C_GRIS);
        btnAcabado[idx][0].setBorder(new LineBorder(tipo == 0 ? C_NARANJA : C_GRAFITO, 1));
        btnAcabado[idx][1].setBackground(tipo == 1 ? C_NARANJA : C_METAL);
        btnAcabado[idx][1].setForeground(tipo == 1 ? C_NEGRO   : C_GRIS);
        btnAcabado[idx][1].setBorder(new LineBorder(tipo == 1 ? C_NARANJA : C_GRAFITO, 1));
        actualizarSubtotal(idx);
    }

    private void cambiarCantidad(int idx, int delta) {
        cantidades[idx] = Math.max(0, Math.min(10, cantidades[idx] + delta));
        qtyLabels[idx].setText(String.valueOf(cantidades[idx]));
        actualizarSubtotal(idx);
    }

    private void actualizarSubtotal(int idx) {
        int precio = acabados[idx].equals("CROMADO") ? PRECIOS[0] : PRECIOS[1];
        int sub = cantidades[idx] * precio;
        subLabels[idx].setText("$" + sub);
    }

    private void intentarContinuar() {
        alertaVacia.setVisible(false);
        alertaFecha.setVisible(false);
        alertaTelefono.setVisible(false);

        int totalPiezas = cantidades[0] + cantidades[1] + cantidades[2];
        if (totalPiezas == 0) { alertaVacia.setVisible(true); return; }

        String fechaStr = campoFecha.getText().trim();
        if (fechaStr.isEmpty()) { alertaFecha.setVisible(true); return; }

        String tel = campoTelefono.getText().trim().replaceAll("[^0-9]", "");
        if (tel.length() != 10) { alertaTelefono.setVisible(true); return; }

        // Todo OK → pasar al ticket
        int total = 0;
        StringBuilder resumen = new StringBuilder();
        String[] refs = {"REF-AS-001 — AZUL", "REF-AS-002 — ROJO", "REF-AS-003 — NEGRO"};
        for (int i = 0; i < 3; i++) {
            if (cantidades[i] > 0) {
                int precio = acabados[i].equals("CROMADO") ? 200 : 100;
                int sub = cantidades[i] * precio;
                total += sub;
                resumen.append(refs[i]).append(" / ").append(acabados[i])
                       .append(" x").append(cantidades[i]).append(" = $").append(sub).append("\n");
            }
        }
        if (tipoEntrega.equals("DOMICILIO")) total += 150;

        new ticket(resumen.toString(), total, fechaStr, tel,
                tipoEntrega, campoDireccion.getText().trim()).setVisible(true);
        dispose();
    }
}