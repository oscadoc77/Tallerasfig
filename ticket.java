package taller;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static taller.catalogo.*;

public class ticket extends JFrame {

    private static final long serialVersionUID = 1L;

    public ticket(String resumen, int total, String fechaEntrega,
                          String telefono, String tipoEntrega, String direccion) {
        setTitle("TALLERASFIG — Ticket de Pedido");
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
        centro.add(buildRack(2), BorderLayout.NORTH);

        // ── Contenido ──────────────────────────────────────────────────────
        JPanel wrap = new JPanel();
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setBackground(C_NEGRO);
        wrap.setBorder(new EmptyBorder(24, 32, 32, 32));

        wrap.add(buildSecTitulo("RESUMEN DE PEDIDO"));
        wrap.add(buildSecSub("// confirma y envía tu orden de trabajo"));
        wrap.add(Box.createVerticalStrut(20));

        // ── Ticket (fondo blanco, como .ticket-wrap) ───────────────────────
        JPanel ticketWrap = buildTicket(resumen, total, fechaEntrega, telefono, tipoEntrega, direccion);
        ticketWrap.setAlignmentX(LEFT_ALIGNMENT);
        wrap.add(ticketWrap);
        wrap.add(Box.createVerticalStrut(20));

        // ── Botones ────────────────────────────────────────────────────────
        JPanel btnFila = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnFila.setBackground(C_NEGRO);
        btnFila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JButton btnVolver = buildBotonSecundario("← VOLVER");
        btnVolver.addActionListener(e -> {
            new pedido().setVisible(true);
            dispose();
        });

        JButton btnNuevo = buildBotonNaranja("🖨 NUEVO PEDIDO");
        btnNuevo.addActionListener(e -> mostrarModalDespedida());

        btnFila.add(btnVolver);
        btnFila.add(btnNuevo);
        wrap.add(btnFila);

        JScrollPane scroll = new JScrollPane(wrap);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(C_NEGRO);
        centro.add(scroll, BorderLayout.CENTER);
        root.add(centro, BorderLayout.CENTER);
    }

    private JPanel buildTicket(String resumen, int total, String fechaEntrega,
                                String telefono, String tipoEntrega, String direccion) {
        JPanel ticket = new JPanel();
        ticket.setLayout(new BoxLayout(ticket, BoxLayout.Y_AXIS));
        ticket.setBackground(Color.WHITE);
        ticket.setMaximumSize(new Dimension(520, Integer.MAX_VALUE));
        ticket.setBorder(new LineBorder(C_GRAFITO, 3));

        // Cabeza del ticket (fondo negro)
        JPanel cabeza = new JPanel();
        cabeza.setLayout(new BoxLayout(cabeza, BoxLayout.Y_AXIS));
        cabeza.setBackground(new Color(0x111111));
        cabeza.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 4, 0, C_NARANJA),
                new EmptyBorder(16, 20, 16, 20)));

        JLabel marca = new JLabel("TALLERASFIG");
        marca.setFont(new Font("Arial Black", Font.BOLD, 28));
        marca.setForeground(Color.WHITE);

        JLabel subMarca = new JLabel("SISTEMA DE PEDIDO DE ASIENTOS");
        subMarca.setFont(new Font("Courier New", Font.PLAIN, 10));
        subMarca.setForeground(C_NARANJA);

        String folio = String.valueOf(System.currentTimeMillis()).substring(7);
        String ahora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        JLabel fecha = new JLabel("FOLIO: " + folio + "   //   " + ahora);
        fecha.setFont(new Font("Courier New", Font.PLAIN, 10));
        fecha.setForeground(C_GRIS);

        cabeza.add(marca);
        cabeza.add(Box.createVerticalStrut(2));
        cabeza.add(subMarca);
        cabeza.add(Box.createVerticalStrut(4));
        cabeza.add(fecha);
        ticket.add(cabeza);

        // Cuerpo del ticket
        JPanel cuerpo = new JPanel();
        cuerpo.setLayout(new BoxLayout(cuerpo, BoxLayout.Y_AXIS));
        cuerpo.setBackground(Color.WHITE);
        cuerpo.setBorder(new EmptyBorder(16, 20, 16, 20));

        // Info despacho
        JPanel infoDespacho = new JPanel();
        infoDespacho.setLayout(new BoxLayout(infoDespacho, BoxLayout.Y_AXIS));
        infoDespacho.setBackground(new Color(0xF5F5F5));
        infoDespacho.setBorder(new CompoundBorder(
                new MatteBorder(0, 3, 0, 0, new Color(0x111111)),
                new EmptyBorder(10, 10, 10, 10)));
        infoDespacho.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        infoDespacho.add(buildInfoRow("ENTREGA REQUERIDA:", fechaEntrega));
        infoDespacho.add(buildInfoRow("TELÉFONO CONTACTO:", telefono));
        infoDespacho.add(buildInfoRow("MÉTODO DE ENTREGA:",
                tipoEntrega.equals("TALLER") ? "Recoger en taller" : "Envío a domicilio"));
        if (!direccion.isEmpty()) {
            infoDespacho.add(buildInfoRow("DIRECCIÓN:", direccion));
        }
        cuerpo.add(infoDespacho);
        cuerpo.add(Box.createVerticalStrut(12));

        // Filas de productos
        for (String linea : resumen.split("\n")) {
            if (!linea.isEmpty()) {
                cuerpo.add(buildFilaTicket(linea));
                cuerpo.add(Box.createVerticalStrut(2));
            }
        }

        if (tipoEntrega.equals("DOMICILIO")) {
            cuerpo.add(buildFilaTicketDestacada("CARGO POR ENVÍO A DOMICILIO", "+$150"));
        }

        ticket.add(cuerpo);

        // Total (fondo negro)
        JPanel totalFila = new JPanel(new BorderLayout());
        totalFila.setBackground(new Color(0x111111));
        totalFila.setBorder(new CompoundBorder(
                new MatteBorder(3, 0, 0, 0, C_NARANJA),
                new EmptyBorder(14, 20, 14, 20)));
        totalFila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel lEtiqueta = new JLabel("TOTAL A PAGAR");
        lEtiqueta.setFont(new Font("Courier New", Font.BOLD, 14));
        lEtiqueta.setForeground(Color.WHITE);

        JLabel lValor = new JLabel("$" + total);
        lValor.setFont(new Font("Arial Black", Font.BOLD, 28));
        lValor.setForeground(C_NARANJA);

        totalFila.add(lEtiqueta, BorderLayout.WEST);
        totalFila.add(lValor,    BorderLayout.EAST);
        ticket.add(totalFila);

        // Pie
        JPanel pie = new JPanel();
        pie.setBackground(Color.WHITE);
        pie.setBorder(new CompoundBorder(
                new MatteBorder(1, 0, 0, 0, new Color(0xDDDDDD)),
                new EmptyBorder(10, 20, 10, 20)));
        pie.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        JLabel pieTxt = new JLabel("GRACIAS POR SU PEDIDO — TALLERASFIG 2026");
        pieTxt.setFont(new Font("Courier New", Font.PLAIN, 10));
        pieTxt.setForeground(new Color(0x999999));
        pie.add(pieTxt);
        ticket.add(pie);

        return ticket;
    }

    private JLabel buildInfoRow(String etiqueta, String valor) {
        JLabel lbl = new JLabel("<html><b>" + etiqueta + "</b> " + valor + "</html>");
        lbl.setFont(new Font("Courier New", Font.PLAIN, 11));
        lbl.setForeground(new Color(0x333333));
        lbl.setBorder(new EmptyBorder(2, 0, 2, 0));
        return lbl;
    }

    private JPanel buildFilaTicket(String texto) {
        // Parsear "REF-AS-001 — AZUL / CROMADO x2 = $400"
        JPanel fila = new JPanel(new BorderLayout());
        fila.setBackground(Color.WHITE);
        fila.setBorder(new MatteBorder(0, 0, 1, 0, new Color(0xDDDDDD)));
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        String[] partes = texto.split(" = ");
        JLabel concepto = new JLabel(partes[0].trim());
        concepto.setFont(new Font("Courier New", Font.PLAIN, 12));
        concepto.setForeground(new Color(0x333333));
        fila.add(concepto, BorderLayout.WEST);

        if (partes.length > 1) {
            JLabel monto = new JLabel(partes[1].trim());
            monto.setFont(new Font("Courier New", Font.BOLD, 13));
            monto.setForeground(new Color(0x111111));
            fila.add(monto, BorderLayout.EAST);
        }
        return fila;
    }

    private JPanel buildFilaTicketDestacada(String texto, String valor) {
        JPanel fila = new JPanel(new BorderLayout());
        fila.setBackground(Color.WHITE);
        fila.setBorder(new MatteBorder(0, 0, 1, 0, new Color(0xDDDDDD)));
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        JLabel concepto = new JLabel(texto);
        concepto.setFont(new Font("Courier New", Font.BOLD, 12));
        concepto.setForeground(C_NARANJA.darker());
        JLabel monto = new JLabel(valor);
        monto.setFont(new Font("Courier New", Font.BOLD, 13));
        monto.setForeground(C_NARANJA.darker());
        fila.add(concepto, BorderLayout.WEST);
        fila.add(monto, BorderLayout.EAST);
        return fila;
    }

    // ─── Modal de despedida ───────────────────────────────────────────────
    private void mostrarModalDespedida() {
        JDialog modal = new JDialog(this, "Pedido Registrado", true);
        modal.setUndecorated(true);
        modal.setSize(420, 260);
        modal.setLocationRelativeTo(this);

        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(C_ACERO);
        box.setBorder(new CompoundBorder(
                new LineBorder(C_NARANJA, 2),
                new EmptyBorder(32, 32, 32, 32)));

        JLabel titulo = new JLabel("¡PEDIDO REGISTRADO!");
        titulo.setFont(new Font("Arial Black", Font.BOLD, 22));
        titulo.setForeground(C_AMARILLO);
        titulo.setAlignmentX(CENTER_ALIGNMENT);
        box.add(titulo);
        box.add(Box.createVerticalStrut(12));

        JLabel desc = new JLabel("<html><center>Tu orden ha sido generada con éxito.<br>" +
                "Nos pondremos en contacto contigo pronto.<br>" +
                "¡Gracias por confiar en TallerAsFig!</center></html>");
        desc.setFont(F_MONO);
        desc.setForeground(C_BLANCO);
        desc.setAlignmentX(CENTER_ALIGNMENT);
        box.add(desc);
        box.add(Box.createVerticalStrut(24));

        JButton btnCerrar = buildBotonNaranja("NUEVO PEDIDO");
        btnCerrar.setAlignmentX(CENTER_ALIGNMENT);
        btnCerrar.addActionListener(e -> {
            modal.dispose();
            new catalogo().setVisible(true);
            dispose();
        });
        box.add(btnCerrar);

        modal.setContentPane(box);
        modal.setVisible(true);
    }
}