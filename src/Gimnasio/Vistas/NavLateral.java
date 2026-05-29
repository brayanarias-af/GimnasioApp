package Gimnasio.Vistas;

import Gimnasio.Controlador.Sesion;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.*;

public class NavLateral extends JPanel {
    public interface NavListener { void onNavClick(String seccion); }
    private final java.util.List<JButton> botones = new ArrayList<>();
    private String seccionActiva = "";
    private NavListener listener;

    /** Carga un PNG del classpath y lo escala a size×size. Devuelve null si no existe. */
    private static ImageIcon icono(String nombre, int size) {
        URL url = NavLateral.class.getResource("/Gimnasio/Iconos/" + nombre + ".png");
        if (url == null) return null;
        ImageIcon raw = new ImageIcon(url);
        Image scaled = raw.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    public NavLateral(String[] secciones, String[] iconosNombres) {
        setPreferredSize(new Dimension(220, 600));
        setBackground(new Color(12, 12, 18));
        setLayout(null);
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, EstilosGym.COLOR_BORDE));

        // Logo con icono gimnasio.png
        ImageIcon logoIcon = icono("gimnasio", 24);
        JLabel logo;
        if (logoIcon != null) {
            logo = new JLabel(" GymUTS", logoIcon, JLabel.LEFT);
        } else {
            logo = new JLabel("🏋️ GymUTS", JLabel.LEFT);
        }
        logo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logo.setForeground(EstilosGym.COLOR_ACENTO);
        logo.setBounds(20, 22, 180, 32);
        add(logo);

        JLabel lblUser = new JLabel("  " + Sesion.getNombreCompleto());
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblUser.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
        lblUser.setBounds(20, 58, 190, 18);
        add(lblUser);

        String rolLabel = Sesion.esAdmin() ? "Administrador" : Sesion.esEntrenador() ? "Entrenador"
                : Sesion.esRecepcionista() ? "Recepcionista" : "Cliente";
        Color rolColor = Sesion.esAdmin() || Sesion.esRecepcionista() ? EstilosGym.COLOR_ACENTO
                : Sesion.esEntrenador() ? new Color(0, 180, 200) : new Color(100, 60, 200);
        JLabel lblRol = new JLabel("  " + rolLabel);
        lblRol.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblRol.setForeground(rolColor);
        lblRol.setBounds(20, 74, 180, 16);
        add(lblRol);

        JSeparator sep = new JSeparator();
        sep.setForeground(EstilosGym.COLOR_BORDE);
        sep.setBounds(15, 98, 190, 2);
        add(sep);

        int y = 114;
        for (int i = 0; i < secciones.length; i++) {
            final String sec = secciones[i];
            final String icoNombre = (i < iconosNombres.length) ? iconosNombres[i] : "";
            ImageIcon ico = icono(icoNombre, 20);
            JButton btn = crearBotonNav(ico, sec);
            btn.setBounds(10, y, 200, 44);
            add(btn);
            botones.add(btn);
            y += 50;
        }

        // Botón cerrar sesión
        JButton btnC = new JButton("  Cerrar Sesión") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(231, 76, 60, 30) : new Color(0, 0, 0, 0));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        ImageIcon salirIco = icono("controlar", 18);
        if (salirIco != null) btnC.setIcon(salirIco);
        btnC.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnC.setForeground(EstilosGym.COLOR_PELIGRO);
        btnC.setBackground(new Color(0, 0, 0, 0));
        btnC.setOpaque(false); btnC.setContentAreaFilled(false);
        btnC.setBorderPainted(false); btnC.setFocusPainted(false);
        btnC.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnC.setHorizontalAlignment(SwingConstants.LEFT);
        btnC.setBounds(10, 530, 200, 40);
        btnC.addActionListener(e -> {
            Window w = SwingUtilities.getWindowAncestor(this);
            if (w != null) w.dispose();
            Sesion.cerrarSesion();
            new LoginVista().setVisible(true);
        });
        add(btnC);
    }

    private JButton crearBotonNav(ImageIcon ico, String seccion) {
        JButton btn = new JButton("  " + seccion) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean activo = seccion.equals(seccionActiva);
                if (activo) {
                    g2.setColor(new Color(255, 87, 34, 25));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    g2.setColor(EstilosGym.COLOR_ACENTO);
                    g2.setStroke(new BasicStroke(3));
                    g2.drawLine(0, 6, 0, getHeight() - 6);
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 255, 255, 10));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        if (ico != null) btn.setIcon(ico);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(EstilosGym.COLOR_TEXTO);
        btn.setBackground(new Color(0, 0, 0, 0));
        btn.setOpaque(false); btn.setContentAreaFilled(false);
        btn.setBorderPainted(false); btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setIconTextGap(8);
        btn.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));
        btn.addActionListener(e -> { setActivo(seccion); if (listener != null) listener.onNavClick(seccion); });
        return btn;
    }

    public void setActivo(String sec) { seccionActiva = sec; repaint(); botones.forEach(JButton::repaint); }
    public void setNavListener(NavListener l) { this.listener = l; }
}
