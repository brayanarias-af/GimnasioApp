package Gimnasio.Vistas;

import Gimnasio.Controlador.Sesion;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class NavLateral extends JPanel {

    public interface NavListener { void onNavClick(String seccion); }

    // Colores sin depender de EstilosGym
    private static final Color COLOR_BORDE = new Color(40, 40, 50);
    private static final Color COLOR_ACENTO = new Color(255, 87, 34);
    private static final Color COLOR_TEXTO = new Color(230, 230, 240);
    private static final Color COLOR_TEXTO_GRIS = new Color(160, 160, 170);
    private static final Color COLOR_PELIGRO = new Color(231, 76, 60);
    private static final Color COLOR_FONDO = new Color(12, 12, 18);

    private final List<JButton> botones = new ArrayList<>();
    private String seccionActiva = "";
    private NavListener listener;

    public NavLateral(String[] secciones, String[] iconos) {
        setPreferredSize(new Dimension(220, 600));
        setBackground(COLOR_FONDO);
        setLayout(null);
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, COLOR_BORDE));

        // Logo
        JLabel logo = new JLabel("🏋️ GymPro", JLabel.LEFT);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logo.setForeground(COLOR_ACENTO);
        logo.setBounds(20, 22, 180, 32);
        add(logo);

        // Info usuario (con manejo de sesión nula)
        String nombreCompleto = Sesion.getNombreCompleto(); // Ahora existe
        JLabel lblUser = new JLabel("👤 " + (nombreCompleto.isEmpty() ? "Usuario" : nombreCompleto));
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblUser.setForeground(COLOR_TEXTO_GRIS);
        lblUser.setBounds(20, 58, 180, 18);
        add(lblUser);

        boolean esAdmin = Sesion.esAdmin();
        JLabel lblRol = new JLabel(esAdmin ? "  Administrador" : "  Cliente");
        lblRol.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblRol.setForeground(COLOR_ACENTO);
        lblRol.setBounds(20, 74, 180, 16);
        add(lblRol);

        JSeparator sep = new JSeparator();
        sep.setForeground(COLOR_BORDE);
        sep.setBounds(15, 98, 190, 2);
        add(sep);

        // Botones de navegación
        int y = 114;
        for (int i = 0; i < secciones.length; i++) {
            final String seccion = secciones[i];
            final String icono = (i < iconos.length) ? iconos[i] : "▸";
            JButton btn = crearBotonNav(icono + "  " + seccion, seccion);
            btn.setBounds(10, y, 200, 44);
            add(btn);
            botones.add(btn);
            y += 50;
        }

        // Botón cerrar sesión (posición dinámica, debajo del último botón)
        JButton btnCerrar = crearBotonCerrarSesion();
        // Calcular posición: y actual + 30px de separación, pero sin pasarse del alto preferido
        int yCierre = y + 30;
        if (yCierre + 50 > getPreferredSize().height) {
            // Ajustar alto del panel si es necesario (opcional)
            setPreferredSize(new Dimension(220, yCierre + 60));
        }
        btnCerrar.setBounds(10, yCierre, 200, 40);
        add(btnCerrar);
    }

    private JButton crearBotonNav(String texto, String seccion) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean activo = seccion.equals(seccionActiva);
                if (activo) {
                    g2.setColor(new Color(255, 87, 34, 25));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    g2.setColor(COLOR_ACENTO);
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
        configurarBoton(btn, texto);
        btn.addActionListener(e -> {
            setActivo(seccion);
            if (listener != null) listener.onNavClick(seccion);
        });
        return btn;
    }

    private JButton crearBotonCerrarSesion() {
        JButton btn = new JButton("🚪  Cerrar Sesión") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(231, 76, 60, 30) : new Color(0, 0, 0, 0));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        configurarBoton(btn, "Cerrar Sesión");
        btn.setForeground(COLOR_PELIGRO);
        btn.addActionListener(e -> {
            Window w = SwingUtilities.getWindowAncestor(this);
            if (w != null) w.dispose();
            Sesion.cerrarSesion();
            new LoginVista().setVisible(true);
        });
        return btn;
    }

    private void configurarBoton(JButton btn, String texto) {
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(COLOR_TEXTO);
        btn.setBackground(new Color(0, 0, 0, 0));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));
    }

    public void setActivo(String seccion) {
        seccionActiva = seccion;
        repaint();
        botones.forEach(JButton::repaint);
    }

    public void setNavListener(NavListener l) {
        this.listener = l;
    }
}