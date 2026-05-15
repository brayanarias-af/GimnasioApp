package Gimnasio.Vistas;

import Gimnasio.Controlador.Sesion;
import Gimnasio.Modelo.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class LoginVista extends JFrame {

    private JTextField    txtUsuario;
    private JPasswordField txtContrasena;
    private JLabel         lblError;

    public LoginVista() {
        setTitle("GymUTS — Sistema de Gestión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 580);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);

        JPanel contenedor = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(EstilosGym.COLOR_FONDO);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        contenedor.setBorder(BorderFactory.createLineBorder(EstilosGym.COLOR_BORDE, 1));

        // ── Panel izquierdo branding ──
        JPanel panelIzq = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(20, 10, 5),
                        getWidth(), getHeight(), new Color(80, 30, 10));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(255, 87, 34, 40)); g2.fillOval(-60, -60, 250, 250);
                g2.setColor(new Color(255, 87, 34, 20)); g2.fillOval(200, 350, 300, 300);
                g2.dispose();
            }
        };
        panelIzq.setPreferredSize(new Dimension(400, 580));

        JLabel lblIcono = new JLabel("🏋️", JLabel.CENTER);
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 72));
        lblIcono.setBounds(0, 120, 400, 90);
        panelIzq.add(lblIcono);

        JLabel lblGym = new JLabel("GymUTS", JLabel.CENTER);
        lblGym.setFont(new Font("Segoe UI", Font.BOLD, 42));
        lblGym.setForeground(EstilosGym.COLOR_ACENTO);
        lblGym.setBounds(0, 210, 400, 50);
        panelIzq.add(lblGym);

        JLabel lblSlogan = new JLabel("Sistema de Gestión Deportiva", JLabel.CENTER);
        lblSlogan.setFont(new Font("Segoe UI", Font.ITALIC, 15));
        lblSlogan.setForeground(new Color(200, 180, 160));
        lblSlogan.setBounds(0, 265, 400, 30);
        panelIzq.add(lblSlogan);

        JPanel sep = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 87, 34, 0),
                        150, 0, EstilosGym.COLOR_ACENTO);
                g2.setPaint(gp); g2.setStroke(new BasicStroke(2));
                g2.drawLine(0, 0, 150, 0); g2.dispose();
            }
        };
        sep.setOpaque(false); sep.setBounds(125, 310, 150, 4);
        panelIzq.add(sep);

        String[] bullets = {"✓  Gestión de clientes", "✓  Control de rutinas", "✓  Seguimiento de progreso"};
        int by = 330;
        for (String b : bullets) {
            JLabel l = new JLabel(b);
            l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            l.setForeground(new Color(200, 180, 165));
            l.setBounds(80, by, 280, 26);
            panelIzq.add(l); by += 28;
        }

        // ── Panel derecho formulario ──
        JPanel panelDer = new JPanel(null);
        panelDer.setBackground(EstilosGym.COLOR_PANEL);

        JPanel barraTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        barraTop.setOpaque(false); barraTop.setBounds(0, 0, 500, 40);
        JButton btnCerrar = new JButton("✕");
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCerrar.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
        btnCerrar.setBackground(EstilosGym.COLOR_PANEL);
        btnCerrar.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
        btnCerrar.setFocusPainted(false);
        btnCerrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> System.exit(0));
        btnCerrar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnCerrar.setForeground(EstilosGym.COLOR_PELIGRO); }
            public void mouseExited (MouseEvent e) { btnCerrar.setForeground(EstilosGym.COLOR_TEXTO_GRIS); }
        });
        barraTop.add(btnCerrar);
        panelDer.add(barraTop);

        final Point[] offset = {null};
        barraTop.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { offset[0] = e.getPoint(); }
        });
        barraTop.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point loc = getLocation();
                setLocation(loc.x + e.getX() - offset[0].x, loc.y + e.getY() - offset[0].y);
            }
        });

        JLabel lblBienvenida = new JLabel("Bienvenido de nuevo");
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblBienvenida.setForeground(EstilosGym.COLOR_TEXTO);
        lblBienvenida.setBounds(60, 60, 360, 36);
        panelDer.add(lblBienvenida);

        JLabel lblSub = new JLabel("Inicia sesión para continuar");
        lblSub.setFont(EstilosGym.FUENTE_NORMAL);
        lblSub.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
        lblSub.setBounds(60, 98, 320, 22);
        panelDer.add(lblSub);

        JSeparator sep2 = new JSeparator();
        sep2.setForeground(EstilosGym.COLOR_BORDE);
        sep2.setBounds(60, 130, 360, 2);
        panelDer.add(sep2);

        JLabel lblU = EstilosGym.crearEtiqueta("USUARIO");
        lblU.setBounds(60, 150, 200, 22); panelDer.add(lblU);
        txtUsuario = EstilosGym.crearCampoTexto();
        txtUsuario.setBounds(60, 174, 360, 40); panelDer.add(txtUsuario);

        JLabel lblP = EstilosGym.crearEtiqueta("CONTRASEÑA");
        lblP.setBounds(60, 228, 200, 22); panelDer.add(lblP);
        txtContrasena = EstilosGym.crearCampoPassword();
        txtContrasena.setBounds(60, 252, 360, 40); panelDer.add(txtContrasena);

        lblError = new JLabel("");
        lblError.setFont(EstilosGym.FUENTE_PEQUEÑA);
        lblError.setForeground(EstilosGym.COLOR_PELIGRO);
        lblError.setBounds(60, 300, 360, 22);
        panelDer.add(lblError);

        JButton btnIngresar = EstilosGym.crearBotonPrimario("INGRESAR");
        btnIngresar.setBounds(60, 330, 360, 44);
        panelDer.add(btnIngresar);

        JLabel lblHint = new JLabel(
            "<html><font color='#55556a'>Admin: admin / gimnasio123 &nbsp;|&nbsp; Cliente: cliente1 / 12345</font></html>");
        lblHint.setFont(EstilosGym.FUENTE_PEQUEÑA);
        lblHint.setBounds(60, 384, 380, 30);
        panelDer.add(lblHint);

        ActionListener accionLogin = e -> autenticar();
        btnIngresar.addActionListener(accionLogin);
        txtContrasena.addActionListener(accionLogin);
        txtUsuario.addActionListener(accionLogin);

        contenedor.add(panelIzq, BorderLayout.WEST);
        contenedor.add(panelDer, BorderLayout.CENTER);
        setContentPane(contenedor);
    }

    private void autenticar() {
        String usuario = txtUsuario.getText().trim();
        String clave   = new String(txtContrasena.getPassword());

        if (usuario.isEmpty() || clave.isEmpty()) {
            lblError.setText("⚠  Por favor completa todos los campos.");
            return;
        }

        lblError.setText("Conectando...");
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        // Ejecutar en hilo separado para no bloquear EDT
        new Thread(() -> {
            Usuario user = new Usuario(usuario, clave);
            Sesion sesion = new Sesion();
            boolean exito = sesion.iniciarSesion(user);

            SwingUtilities.invokeLater(() -> {
                setCursor(Cursor.getDefaultCursor());
                if (!exito) {
                    lblError.setText("⚠  Usuario o contraseña incorrectos.");
                    txtContrasena.setText("");
                } else {
                    dispose();
                    String rol = user.getRol();
                    if ("ADMIN".equalsIgnoreCase(rol) || "ENTRENADOR".equalsIgnoreCase(rol)
                            || "RECEPCIONISTA".equalsIgnoreCase(rol)) {
                        new PanelAdminVista().setVisible(true);
                    } else {
                        new PanelUsuarioVista().setVisible(true);
                    }
                }
            });
        }).start();
    }
}
