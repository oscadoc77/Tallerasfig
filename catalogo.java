package taller;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class catalogo extends JFrame {

    // ── Paleta (igual que el CSS) ──────────────────────────────────────────
    static final Color C_NEGRO    = new Color(0x0D0D0D);
    static final Color C_ACERO    = new Color(0x1A1A1A);
    static final Color C_METAL    = new Color(0x2A2A2A);
    static final Color C_GRAFITO  = new Color(0x3D3D3D);
    static final Color C_GRIS     = new Color(0x6E6E6E);
    static final Color C_PLATA    = new Color(0xB0B0B0);
    static final Color C_BLANCO   = new Color(0xE8E8E8);
    static final Color C_NARANJA  = new Color(0xE8500A);
    static final Color C_NARANJA2 = new Color(0xC43D00);
    static final Color C_AMARILLO = new Color(0xF0B429);
    static final Color C_ROJO_ERR = new Color(0xC0392B);
    static final Color C_AZUL_AS  = new Color(0x2980B9);
    static final Color C_ROJO_AS  = new Color(0xC0392B);

    // ── Fuentes ────────────────────────────────────────────────────────────
    static final Font F_TITULO  = new Font("Arial Black",  Font.BOLD,  20);
    static final Font F_MONO    = new Font("Courier New",  Font.PLAIN, 11);
    static final Font F_MONO_B  = new Font("Courier New",  Font.BOLD,  11);
    static final Font F_BODY    = new Font("Arial",        Font.PLAIN, 12);
    static final Font F_GRANDE  = new Font("Arial Black",  Font.BOLD,  28);

    private static final long serialVersionUID = 1L;

    public catalogo() {
        setTitle("TALLERASFIG — Sistema de Pedido de Asientos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 680);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(C_NEGRO);
        setContentPane(root);

        // ── CABECERA ──────────────────────────────────────────────────────
        root.add(buildCabecera(), BorderLayout.NORTH);

        // ── RACK DE PASOS ─────────────────────────────────────────────────
        JPanel rackWrap = new JPanel(new BorderLayout());
        rackWrap.setBackground(C_ACERO);
        rackWrap.setBorder(new MatteBorder(0, 0, 2, 0, C_GRAFITO));
        rackWrap.add(buildRack(0), BorderLayout.CENTER);
        root.add(rackWrap, BorderLayout.CENTER);

        // ── CONTENIDO ─────────────────────────────────────────────────────
        JPanel wrap = new JPanel();
        wrap.setBackground(C_NEGRO);
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setBorder(new EmptyBorder(24, 32, 32, 32));

        // Título sección
        wrap.add(buildSecTitulo("CATÁLOGO DE PIEZAS"));
        wrap.add(buildSecSub("// selección de modelos disponibles — temporada 2026"));
        wrap.add(Box.createVerticalStrut(16));

        // Grid de fichas
        JPanel grid = new JPanel(new GridLayout(1, 3, 2, 0));
        grid.setBackground(C_GRAFITO);
        grid.setBorder(new LineBorder(C_GRAFITO, 2));
        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));
       grid.add(buildFicha(
    "REF-AS-001 // AZUL",
    "ASIENTO AZUL",
    "Estructura tubular de acero con tejido\nde plástico resistente en color azul.\nMarco reforzado.",
    C_AZUL_AS,
    "asientoazul.jpg"
));

grid.add(buildFicha(
    "REF-AS-002 // ROJO",
    "ASIENTO ROJO",
    "Diseño macramé con tejido en color rojo,\nestructura metálica de alta resistencia.\nAcabado artesanal.",
    C_ROJO_AS,
    "asientorojo.jpg"
));

grid.add(buildFicha(
    "REF-AS-003 // NEGRO",
    "ASIENTO NEGRO",
    "Acabado en piel negra con detalles en rojo.\nEstructura ergonómica de alta durabilidad.",
    C_PLATA,
    "asientonegro.jpg"
));
        wrap.add(grid);

        wrap.add(Box.createVerticalStrut(16));

        // Specs box
        JPanel specs = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        specs.setBackground(C_ACERO);
        specs.setBorder(new CompoundBorder(
                new MatteBorder(0, 4, 0, 0, C_AMARILLO),
                new EmptyBorder(8, 12, 8, 12)));
        specs.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        JLabel specsTxt = new JLabel("⚙   CANTIDAD MÁX. POR REF: 10 UNIDADES   |   CROMADO: $200/PZA   |   PINTADO: $100/PZA");
        specsTxt.setFont(F_MONO);
        specsTxt.setForeground(C_PLATA);
        specs.add(specsTxt);
        wrap.add(specs);

        wrap.add(Box.createVerticalStrut(20));

        // Botón continuar
        JPanel btnFila = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        btnFila.setBackground(C_NEGRO);
        JPanel btnFila2 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
btnFila.setBackground(C_NEGRO);

JButton btnBuscar = buildBotonSecundario("🔍 BUSCAR ASIENTO");
btnBuscar.addActionListener(e -> {
    new buscador().setVisible(true);
});

JButton btnCont = buildBotonNaranja("CONFIGURAR PEDIDO →");
btnCont.addActionListener(e -> {
    new pedido().setVisible(true);
    dispose();
});

btnFila.add(btnBuscar);
btnFila.add(btnCont);
        btnFila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        JButton btnCont2 = buildBotonNaranja("CONFIGURAR PEDIDO  →");
        btnCont.addActionListener(e -> {
            new pedido().setVisible(true);
            dispose();
        });
        btnFila.add(btnCont);
        wrap.add(btnFila);

        // Panel central con scroll
        JScrollPane scroll = new JScrollPane(wrap);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(C_NEGRO);

        // Reemplazamos el CENTER con un BorderLayout real
        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(C_NEGRO);
        centro.add(rackWrap, BorderLayout.NORTH);
        centro.add(scroll, BorderLayout.CENTER);

        root.remove(rackWrap); // sacamos el que pusimos antes
        root.add(centro, BorderLayout.CENTER);
    }

    // ────────────────────────────────────────────────────────────────────────
    // HELPERS DE CONSTRUCCIÓN
    // ────────────────────────────────────────────────────────────────────────

    static JPanel buildCabecera() {
        JPanel cab = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Fondo acero con líneas diagonales (como el CSS ::before)
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(C_ACERO);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(0xE8500A, false));
                AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.05f);
                g2.setComposite(ac);
                for (int i = -getHeight(); i < getWidth() + getHeight(); i += 20) {
                    g2.drawLine(i, 0, i + getHeight(), getHeight());
                }
            }
        };
        cab.setBackground(C_ACERO);
        cab.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 4, 0, C_NARANJA),
                new EmptyBorder(12, 24, 12, 24)));
        cab.setPreferredSize(new Dimension(0, 80));

        // Logo lado izquierdo
        JPanel logoBloque = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        logoBloque.setOpaque(false);

        JLabel logoSvg = new JLabel(buildLogoIcon());
        logoBloque.add(logoSvg);

        JPanel textoBloque = new JPanel();
        textoBloque.setOpaque(false);
        textoBloque.setLayout(new BoxLayout(textoBloque, BoxLayout.Y_AXIS));
        JLabel titulo = new JLabel("TALLERASFIG");
        titulo.setFont(new Font("Arial Black", Font.BOLD, 32));
        titulo.setForeground(C_BLANCO);
        JLabel sub = new JLabel("SISTEMA DE PEDIDO DE ASIENTOS");
        sub.setFont(F_MONO);
        sub.setForeground(C_NARANJA);
        textoBloque.add(titulo);
        textoBloque.add(sub);
        logoBloque.add(textoBloque);

        cab.add(logoBloque, BorderLayout.WEST);

        // Badge lado derecho
        JLabel badge = new JLabel("REF-2026 // ACTIVO") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int[] xs = {8, getWidth(), getWidth()-8, 0};
                int[] ys = {0, 0, getHeight(), getHeight()};
                g2.setColor(C_NARANJA);
                g2.fillPolygon(xs, ys, 4);
                super.paintComponent(g);
            }
        };
        badge.setFont(F_MONO_B);
        badge.setForeground(C_NEGRO);
        badge.setOpaque(false);
        badge.setBorder(new EmptyBorder(6, 20, 6, 20));
        JPanel badgeWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 10));
        badgeWrap.setOpaque(false);
        badgeWrap.add(badge);
        cab.add(badgeWrap, BorderLayout.EAST);

        return cab;
    }

    static JPanel buildRack(int activo) {
        JPanel rack = new JPanel(new GridLayout(1, 3));
        rack.setBackground(C_ACERO);
        rack.setPreferredSize(new Dimension(0, 44));
        String[] nombres = {"CATÁLOGO", "PEDIDO", "TICKET"};
        for (int i = 0; i < 3; i++) {
            final int idx = i;
            boolean isActivo = (i == activo);
            JPanel paso = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10)) {
                @Override protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (isActivo) {
                        g.setColor(C_NARANJA);
                        g.fillRect(0, getHeight() - 3, getWidth(), 3);
                    }
                }
            };
            paso.setBackground(isActivo ? new Color(0xE8500A, false) : C_ACERO);
            if (i < 2) paso.setBorder(new MatteBorder(0, 0, 0, 1, C_GRAFITO));

            JLabel num = new JLabel("0" + (i + 1));
            num.setFont(new Font("Arial Black", Font.BOLD, 18));
            num.setForeground(isActivo ? C_NARANJA : C_GRIS);
            JLabel lbl = new JLabel(nombres[i]);
            lbl.setFont(F_MONO);
            lbl.setForeground(isActivo ? C_NARANJA : C_GRIS);
            paso.add(num);
            paso.add(lbl);
            rack.add(paso);
        }
        return rack;
    }

    static JLabel buildSecTitulo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Arial Black", Font.BOLD, 24));
        lbl.setForeground(C_BLANCO);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        return lbl;
    }

    static JLabel buildSecSub(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(F_MONO);
        lbl.setForeground(C_GRIS);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        return lbl;
    }

    static JPanel buildFicha(String ref,
                         String nombre,
                         String desc,
                         Color barraColor,
                         String rutaImagen) {
        JPanel ficha = new JPanel();
        ficha.setLayout(new BoxLayout(ficha, BoxLayout.Y_AXIS));
        ficha.setBackground(C_ACERO);
        ficha.setBorder(new EmptyBorder(20, 18, 20, 18));

        ImageIcon icono = new ImageIcon(rutaImagen);
Image imagenEscalada = icono.getImage().getScaledInstance(
        220,
        150,
        Image.SCALE_SMOOTH);

JLabel foto = new JLabel(new ImageIcon(imagenEscalada));
foto.setAlignmentX(Component.CENTER_ALIGNMENT);

ficha.add(foto);
ficha.add(Box.createVerticalStrut(10));

        // Barra de color superior
        JPanel barra = new JPanel();
        barra.setBackground(barraColor);
        barra.setMaximumSize(new Dimension(Integer.MAX_VALUE, 4));
        barra.setPreferredSize(new Dimension(0, 4));
        ficha.add(barra);
        ficha.add(Box.createVerticalStrut(10));

        JLabel refLbl = new JLabel(ref);
        refLbl.setFont(F_MONO);
        refLbl.setForeground(C_NARANJA);
        ficha.add(refLbl);

        JLabel nomLbl = new JLabel(nombre);
        nomLbl.setFont(new Font("Arial Black", Font.BOLD, 20));
        nomLbl.setForeground(C_BLANCO);
        ficha.add(nomLbl);
        ficha.add(Box.createVerticalStrut(8));

        JTextArea descTxt = new JTextArea(desc);
        descTxt.setFont(F_BODY);
        descTxt.setForeground(C_PLATA);
        descTxt.setBackground(C_ACERO);
        descTxt.setEditable(false);
        descTxt.setLineWrap(true);
        descTxt.setWrapStyleWord(true);
        descTxt.setFocusable(false);
        ficha.add(descTxt);
        ficha.add(Box.createVerticalStrut(12));

        // Tags de precio
        ficha.add(buildPrecioTag("CROMADO", "$200", C_PLATA));
        ficha.add(Box.createVerticalStrut(4));
        ficha.add(buildPrecioTag("PINTADO",  "$100", C_NARANJA));

        return ficha;
    }

    static JPanel buildPrecioTag(String etiqueta, String valor, Color acento) {
        JPanel tag = new JPanel(new BorderLayout());
        tag.setBackground(C_METAL);
        tag.setBorder(new CompoundBorder(
                new MatteBorder(0, 3, 0, 0, acento),
                new EmptyBorder(6, 8, 6, 8)));
        tag.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        JLabel lEtq = new JLabel(etiqueta);
        lEtq.setFont(new Font("Courier New", Font.PLAIN, 10));
        lEtq.setForeground(C_GRIS);
        JLabel lVal = new JLabel(valor);
        lVal.setFont(new Font("Courier New", Font.BOLD, 14));
        lVal.setForeground(C_BLANCO);
        tag.add(lEtq, BorderLayout.WEST);
        tag.add(lVal, BorderLayout.EAST);
        return tag;
    }

    static JButton buildBotonNaranja(String texto) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isPressed() ? C_NARANJA2 :
                           getModel().isRollover() ? C_NARANJA2 : C_NARANJA;
                // Hexágono recortado como clip-path del CSS
                int[] xs = {10, getWidth(), getWidth()-10, 0};
                int[] ys = {0,  0, getHeight(), getHeight()};
                g2.setColor(bg);
                g2.fillPolygon(xs, ys, 4);
                g2.dispose();
                // Texto encima
                FontMetrics fm = g.getFontMetrics();
                g.setColor(C_NEGRO);
                g.setFont(getFont());
                int tx = (getWidth() - fm.stringWidth(getText())) / 2;
                int ty = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g.drawString(getText(), tx, ty);
            }
        };
        btn.setFont(new Font("Courier New", Font.BOLD, 12));
        btn.setForeground(C_NEGRO);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(220, 42));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    static JButton buildBotonSecundario(String texto) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                Color bg = getModel().isPressed() ? C_GRAFITO :
                           getModel().isRollover() ? C_GRAFITO : C_METAL;
                int[] xs = {10, getWidth(), getWidth()-10, 0};
                int[] ys = {0,  0, getHeight(), getHeight()};
                g2.setColor(bg);
                g2.fillPolygon(xs, ys, 4);
                g2.dispose();
                FontMetrics fm = g.getFontMetrics();
                g.setColor(C_PLATA);
                g.setFont(getFont());
                int tx = (getWidth() - fm.stringWidth(getText())) / 2;
                int ty = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g.drawString(getText(), tx, ty);
            }
        };
        btn.setFont(new Font("Courier New", Font.BOLD, 12));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(160, 42));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // Logo SVG convertido a icono Java2D
    static ImageIcon buildLogoIcon() {
        int w = 60, h = 60;
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(w, h,
                java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Elipses (cuerdas del asiento)
        g2.setStroke(new BasicStroke(2.5f));
        g2.setColor(new Color(0x42A5F5));
        g2.rotate(Math.toRadians(-30), 30, 30);
        g2.drawOval(4, 12, 52, 32);
        g2.rotate(Math.toRadians(30), 30, 30);

        g2.setColor(new Color(0x1A237E));
        g2.setStroke(new BasicStroke(2f));
        g2.rotate(Math.toRadians(20), 30, 30);
        g2.drawOval(6, 14, 44, 28);
        g2.rotate(Math.toRadians(-20), 30, 30);

        // Cuerpo silla
        g2.setColor(new Color(0x1A237E));
        g2.fillRoundRect(14, 20, 30, 18, 4, 4);
        g2.fillRoundRect(36, 16, 14, 22, 4, 4);
        g2.setColor(new Color(0xE8F4FD));
        g2.fillRoundRect(38, 18, 10, 8, 2, 2);

        // Ruedas
        g2.setColor(C_NEGRO);
        g2.fillOval(16, 36, 12, 12);
        g2.fillOval(36, 36, 12, 12);
        g2.setColor(C_GRAFITO);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawOval(16, 36, 12, 12);
        g2.drawOval(36, 36, 12, 12);

        g2.dispose();
        return new ImageIcon(img);
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); }
        catch (Exception ignore) {}
        SwingUtilities.invokeLater(() -> new catalogo().setVisible(true));
    }
}