package taller;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.*;
import static taller.catalogo.*;

public class buscador extends JFrame {

    private JTextField txtBuscar;
    private JPanel resultadosPanel;

    public buscador() {
        setTitle("TALLERASFIG — Buscador de Asientos");
        setSize(780, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(C_NEGRO);
        setContentPane(root);

        // ── CABECERA ──────────────────────────────────────────────────────
        root.add(buildCabecera(), BorderLayout.NORTH);

        // ── CENTRO ────────────────────────────────────────────────────────
        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(C_NEGRO);

        // Rack de pasos (paso 0 = catálogo)
        centro.add(buildRack(0), BorderLayout.NORTH);

        // Contenido scrolleable
        JPanel wrap = new JPanel();
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setBackground(C_NEGRO);
        wrap.setBorder(new EmptyBorder(24, 32, 32, 32));

        wrap.add(buildSecTitulo("BUSCADOR DE ASIENTOS"));
        wrap.add(buildSecSub("// filtra por referencia, color o descripción"));
        wrap.add(Box.createVerticalStrut(20));

        // ── Barra de búsqueda ─────────────────────────────────────────────
        JPanel barraBusq = new JPanel(new BorderLayout(8, 0));
        barraBusq.setBackground(C_ACERO);
        barraBusq.setBorder(new CompoundBorder(
                new MatteBorder(0, 4, 0, 0, C_NARANJA),
                new EmptyBorder(10, 12, 10, 12)));
        barraBusq.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));

        JLabel lbBuscar = new JLabel("🔍");
        lbBuscar.setFont(new Font("Arial", Font.PLAIN, 18));
        lbBuscar.setForeground(C_NARANJA);

        txtBuscar = new JTextField();
        txtBuscar.setFont(new Font("Courier New", Font.PLAIN, 14));
        txtBuscar.setBackground(C_METAL);
        txtBuscar.setForeground(C_BLANCO);
        txtBuscar.setCaretColor(C_NARANJA);
        txtBuscar.setBorder(new CompoundBorder(
                new LineBorder(C_GRAFITO, 1),
                new EmptyBorder(4, 8, 4, 8)));

        JButton btnBuscar = buildBotonNaranja("BUSCAR");
        btnBuscar.setPreferredSize(new Dimension(120, 36));

        barraBusq.add(lbBuscar,   BorderLayout.WEST);
        barraBusq.add(txtBuscar,  BorderLayout.CENTER);
        barraBusq.add(btnBuscar,  BorderLayout.EAST);
        wrap.add(barraBusq);
        wrap.add(Box.createVerticalStrut(20));

        // ── Panel de resultados (dinámico) ────────────────────────────────
        resultadosPanel = new JPanel();
        resultadosPanel.setLayout(new BoxLayout(resultadosPanel, BoxLayout.Y_AXIS));
        resultadosPanel.setBackground(C_NEGRO);
        wrap.add(resultadosPanel);

        // ── Botón volver ──────────────────────────────────────────────────
        wrap.add(Box.createVerticalStrut(16));
        JPanel btnFila = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        btnFila.setBackground(C_NEGRO);
        JButton btnVolver = buildBotonSecundario("← VOLVER AL CATÁLOGO");
        btnVolver.setPreferredSize(new Dimension(220, 42));
        btnVolver.addActionListener(e -> {
            new catalogo().setVisible(true);
            dispose();
        });
        btnFila.add(btnVolver);
        btnFila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        wrap.add(btnFila);

        JScrollPane scroll = new JScrollPane(wrap);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(C_NEGRO);
        centro.add(scroll, BorderLayout.CENTER);
        root.add(centro, BorderLayout.CENTER);

        // ── Acciones ──────────────────────────────────────────────────────
        btnBuscar.addActionListener(e -> buscar());
        txtBuscar.addActionListener(e -> buscar()); // Enter también busca

        mostrarCatalogo();
    }

    // ── Muestra todos los asientos al abrir ───────────────────────────────
    private void mostrarCatalogo() {
        resultadosPanel.removeAll();
        try {
            BufferedReader br = new BufferedReader(new FileReader("catalogo.txt"));
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] d = linea.split("\\|");
                if (d.length >= 5) resultadosPanel.add(buildTarjeta(d));
            }
            br.close();
        } catch (Exception ex) {
            resultadosPanel.add(buildMensajeError("No se pudo leer catalogo.txt"));
        }
        resultadosPanel.revalidate();
        resultadosPanel.repaint();
    }

    // ── Filtra por criterio ───────────────────────────────────────────────
    private void buscar() {
        resultadosPanel.removeAll();
        String criterio = txtBuscar.getText().trim().toUpperCase();

        if (criterio.isEmpty()) {
            mostrarCatalogo();
            return;
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader("catalogo.txt"));
            String linea;
            boolean encontrado = false;
            while ((linea = br.readLine()) != null) {
                if (linea.toUpperCase().contains(criterio)) {
                    String[] d = linea.split("\\|");
                    if (d.length >= 5) {
                        resultadosPanel.add(buildTarjeta(d));
                        encontrado = true;
                    }
                }
            }
            br.close();
            if (!encontrado) {
                resultadosPanel.add(buildMensajeVacio(criterio));
            }
        } catch (Exception ex) {
            resultadosPanel.add(buildMensajeError("Error al leer catalogo.txt"));
        }

        resultadosPanel.revalidate();
        resultadosPanel.repaint();
    }

    // ── Tarjeta estilizada por asiento ────────────────────────────────────
    private JPanel buildTarjeta(String[] d) {
        // d[0]=REF  d[1]=COLOR  d[2]=DESC  d[3]=CROMADO  d[4]=PINTADO
        Color barra = colorBarra(d[1]);

        JPanel card = new JPanel(new BorderLayout(0, 0));
        card.setBackground(C_ACERO);
        card.setBorder(new CompoundBorder(
                new MatteBorder(0, 4, 0, 0, barra),
                new EmptyBorder(14, 16, 14, 16)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        // Izquierda: ref + descripción
        JPanel izq = new JPanel();
        izq.setLayout(new BoxLayout(izq, BoxLayout.Y_AXIS));
        izq.setBackground(C_ACERO);

        JLabel refLbl = new JLabel(d[0]);
        refLbl.setFont(new Font("Courier New", Font.BOLD, 11));
        refLbl.setForeground(C_NARANJA);

        JLabel colLbl = new JLabel("COLOR: " + d[1]);
        colLbl.setFont(new Font("Courier New", Font.PLAIN, 11));
        colLbl.setForeground(C_GRIS);

        JLabel descLbl = new JLabel(d[2]);
        descLbl.setFont(new Font("Arial", Font.PLAIN, 13));
        descLbl.setForeground(C_BLANCO);

        izq.add(refLbl);
        izq.add(Box.createVerticalStrut(2));
        izq.add(colLbl);
        izq.add(Box.createVerticalStrut(4));
        izq.add(descLbl);

        // Derecha: precios
        JPanel der = new JPanel();
        der.setLayout(new BoxLayout(der, BoxLayout.Y_AXIS));
        der.setBackground(C_ACERO);

        der.add(buildPrecioTag("CROMADO", "$" + d[3], C_PLATA));
        der.add(Box.createVerticalStrut(4));
        der.add(buildPrecioTag("PINTADO",  "$" + d[4], C_NARANJA));

        card.add(izq, BorderLayout.CENTER);
        card.add(der, BorderLayout.EAST);

        // Separador
        JPanel sep = new JPanel();
        sep.setBackground(C_GRAFITO);
        sep.setPreferredSize(new Dimension(0, 1));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(C_NEGRO);
        wrapper.add(card);
        wrapper.add(sep);
        wrapper.add(Box.createVerticalStrut(4));

        return wrapper;
    }

    // ── Color de barra lateral según color del asiento ────────────────────
    private Color colorBarra(String color) {
        switch (color.trim().toUpperCase()) {
            case "AZUL":  return new Color(0x2980B9);
            case "ROJO":  return new Color(0xC0392B);
            case "NEGRO": return C_PLATA;
            default:      return C_NARANJA;
        }
    }

    // ── Mensaje cuando no hay resultados ──────────────────────────────────
    private JPanel buildMensajeVacio(String criterio) {
        JPanel p = new JPanel();
        p.setBackground(C_ACERO);
        p.setBorder(new CompoundBorder(
                new MatteBorder(0, 4, 0, 0, C_GRAFITO),
                new EmptyBorder(20, 16, 20, 16)));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        JLabel lbl = new JLabel("Sin resultados para: \"" + criterio + "\"");
        lbl.setFont(new Font("Courier New", Font.PLAIN, 13));
        lbl.setForeground(C_GRIS);
        p.add(lbl);
        return p;
    }

    // ── Mensaje de error de archivo ───────────────────────────────────────
    private JPanel buildMensajeError(String msg) {
        JPanel p = new JPanel();
        p.setBackground(new Color(0x2C1010));
        p.setBorder(new CompoundBorder(
                new MatteBorder(0, 4, 0, 0, C_ROJO_ERR),
                new EmptyBorder(16, 16, 16, 16)));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        JLabel lbl = new JLabel("⚠  " + msg);
        lbl.setFont(new Font("Courier New", Font.BOLD, 12));
        lbl.setForeground(new Color(0xFF6B6B));
        p.add(lbl);
        return p;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); }
        catch (Exception ignore) {}
        SwingUtilities.invokeLater(() -> new buscador().setVisible(true));
    }
}