package Gimnasio.Vistas;

import java.awt.*;

public class EstilosGym {

    // Colores principales
    public static final Color COLOR_FONDO         = new Color(15, 15, 20);
    public static final Color COLOR_PANEL         = new Color(22, 22, 30);
    public static final Color COLOR_PANEL_CLARO   = new Color(30, 30, 42);
    public static final Color COLOR_ACENTO        = new Color(255, 87, 34);   // naranja fuego
    public static final Color COLOR_ACENTO2       = new Color(255, 120, 60);
    public static final Color COLOR_TEXTO         = new Color(240, 240, 245);
    public static final Color COLOR_TEXTO_GRIS    = new Color(150, 150, 165);
    public static final Color COLOR_EXITO         = new Color(46, 204, 113);
    public static final Color COLOR_PELIGRO       = new Color(231, 76, 60);
    public static final Color COLOR_BORDE         = new Color(50, 50, 65);

    // Fuentes
    public static final Font FUENTE_TITULO   = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font FUENTE_SUBTITULO= new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FUENTE_NORMAL   = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FUENTE_PEQUEÑA  = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FUENTE_BOTON    = new Font("Segoe UI", Font.BOLD, 13);

    /** Panel con fondo oscuro */
    public static void aplicarFondoPanel(java.awt.Container c) {
        c.setBackground(COLOR_PANEL);
    }

    /** Botón naranja principal */
    public static javax.swing.JButton crearBotonPrimario(String texto) {
        javax.swing.JButton btn = new javax.swing.JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(COLOR_ACENTO.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(COLOR_ACENTO2);
                } else {
                    g2.setColor(COLOR_ACENTO);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(Color.WHITE);
                g2.setFont(FUENTE_BOTON);
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(160, 38));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /** Botón secundario (borde naranja) */
    public static javax.swing.JButton crearBotonSecundario(String texto) {
        javax.swing.JButton btn = new javax.swing.JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isRollover() ? new Color(255,87,34,30) : COLOR_PANEL_CLARO;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(COLOR_ACENTO);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 10, 10);
                g2.setColor(COLOR_ACENTO);
                g2.setFont(FUENTE_BOTON);
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(140, 36));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /** Botón danger (eliminar) */
    public static javax.swing.JButton crearBotonPeligro(String texto) {
        javax.swing.JButton btn = new javax.swing.JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? COLOR_PELIGRO : COLOR_PELIGRO.darker());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(Color.WHITE);
                g2.setFont(FUENTE_BOTON);
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(120, 36));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /** Campo de texto con estilo oscuro */
    public static javax.swing.JTextField crearCampoTexto() {
        javax.swing.JTextField tf = new javax.swing.JTextField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_FONDO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(isFocusOwner() ? COLOR_ACENTO : COLOR_BORDE);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        tf.setOpaque(false);
        tf.setForeground(COLOR_TEXTO);
        tf.setCaretColor(COLOR_ACENTO);
        tf.setFont(FUENTE_NORMAL);
        tf.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10));
        tf.setPreferredSize(new Dimension(200, 36));
        return tf;
    }

    /** Campo de contraseña con estilo oscuro */
    public static javax.swing.JPasswordField crearCampoPassword() {
        javax.swing.JPasswordField pf = new javax.swing.JPasswordField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_FONDO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(isFocusOwner() ? COLOR_ACENTO : COLOR_BORDE);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        pf.setOpaque(false);
        pf.setForeground(COLOR_TEXTO);
        pf.setCaretColor(COLOR_ACENTO);
        pf.setFont(FUENTE_NORMAL);
        pf.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10));
        pf.setPreferredSize(new Dimension(200, 36));
        return pf;
    }

    /** Etiqueta estándar */
    public static javax.swing.JLabel crearEtiqueta(String texto) {
        javax.swing.JLabel lbl = new javax.swing.JLabel(texto);
        lbl.setForeground(COLOR_TEXTO_GRIS);
        lbl.setFont(FUENTE_NORMAL);
        return lbl;
    }
}
