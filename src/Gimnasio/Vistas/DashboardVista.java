package Gimnasio.Vistas;

import Gimnasio.Controlador.*;
import Gimnasio.Controlador.Sesion;
import javax.swing.*;
import java.awt.*;

public class DashboardVista extends JPanel {

    public DashboardVista() {
        setLayout(new BorderLayout());
        setBackground(EstilosGym.COLOR_FONDO);
        construirUI();
    }

    private void construirUI() {
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBackground(EstilosGym.COLOR_FONDO);
        contenido.setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));

        // Bienvenida
        JLabel lblBienvenida = new JLabel("Hola, " + Sesion.getNombreCompleto() + " 👋");
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblBienvenida.setForeground(EstilosGym.COLOR_TEXTO);
        lblBienvenida.setAlignmentX(LEFT_ALIGNMENT);

        String rolTexto = Sesion.esAdmin()
                ? "Panel de Administración — visión general del gimnasio"
                : "Bienvenido a tu panel de entrenamiento";
        JLabel lblSub = new JLabel(rolTexto);
        lblSub.setFont(EstilosGym.FUENTE_NORMAL);
        lblSub.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
        lblSub.setAlignmentX(LEFT_ALIGNMENT);

        contenido.add(lblBienvenida);
        contenido.add(Box.createVerticalStrut(6));
        contenido.add(lblSub);
        contenido.add(Box.createVerticalStrut(26));

        JSeparator sep = new JSeparator();
        sep.setForeground(EstilosGym.COLOR_BORDE);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setAlignmentX(LEFT_ALIGNMENT);
        contenido.add(sep);
        contenido.add(Box.createVerticalStrut(24));

        // Cargar stats desde BD
        ClienteDAO cliDAO = new ClienteDAO();
        MaquinaDAO maqDAO = new MaquinaDAO();
        RutinaDAO  rutDAO = new RutinaDAO();

        int totalClientes  = cliDAO.contarTotal();
        int activosClientes = cliDAO.contarActivos();
        int totalMaquinas  = maqDAO.contarTotal();
        int dispMaquinas   = maqDAO.contarDisponibles();
        int totalRutinas   = rutDAO.contarTotal();

        // ── Tarjetas de estadísticas ──
        JLabel lblTitStats = new JLabel("📊  Resumen en tiempo real");
        lblTitStats.setFont(EstilosGym.FUENTE_SUBTITULO);
        lblTitStats.setForeground(EstilosGym.COLOR_TEXTO);
        lblTitStats.setAlignmentX(LEFT_ALIGNMENT);
        contenido.add(lblTitStats);
        contenido.add(Box.createVerticalStrut(16));

        JPanel tarjetas = new JPanel(new GridLayout(1, Sesion.esAdmin() ? 4 : 3, 16, 0));
        tarjetas.setOpaque(false);
        tarjetas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        tarjetas.setAlignmentX(LEFT_ALIGNMENT);

        tarjetas.add(crearTarjeta("👥 Clientes",     String.valueOf(totalClientes),
                "registrados", new Color(100, 60, 200)));
        tarjetas.add(crearTarjeta("✅ Activos",       String.valueOf(activosClientes),
                "con membresía activa", EstilosGym.COLOR_EXITO));
        tarjetas.add(crearTarjeta("🏋️ Máquinas",     String.valueOf(dispMaquinas) + "/" + totalMaquinas,
                "disponibles", EstilosGym.COLOR_ACENTO));
        if (Sesion.esAdmin()) {
            tarjetas.add(crearTarjeta("📋 Rutinas",  String.valueOf(totalRutinas),
                    "disponibles", new Color(0, 180, 200)));
        }
        contenido.add(tarjetas);
        contenido.add(Box.createVerticalStrut(32));

        if (Sesion.esAdmin()) {
            // Accesos rápidos
            JLabel lblAcc = new JLabel("⚡  Módulos disponibles");
            lblAcc.setFont(EstilosGym.FUENTE_SUBTITULO);
            lblAcc.setForeground(EstilosGym.COLOR_TEXTO);
            lblAcc.setAlignmentX(LEFT_ALIGNMENT);
            contenido.add(lblAcc);
            contenido.add(Box.createVerticalStrut(14));

            JPanel cards = new JPanel(new GridLayout(1, 3, 16, 0));
            cards.setOpaque(false);
            cards.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
            cards.setAlignmentX(LEFT_ALIGNMENT);
            cards.add(crearCardModulo("👥", "Clientes", "CRUD completo — " + totalClientes + " registros", new Color(100,60,200)));
            cards.add(crearCardModulo("🏋️", "Máquinas", "Estado del equipo — " + totalMaquinas + " equipos", EstilosGym.COLOR_ACENTO));
            cards.add(crearCardModulo("📋", "Rutinas",  "Programas — " + totalRutinas + " disponibles", new Color(0,180,200)));
            contenido.add(cards);
        } else {
            JPanel info = new JPanel(new GridLayout(1, 2, 16, 0));
            info.setOpaque(false);
            info.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
            info.setAlignmentX(LEFT_ALIGNMENT);
            info.add(crearCardModulo("📋", "Mis Rutinas",  "Consulta tus programas asignados", new Color(0,180,200)));
            info.add(crearCardModulo("🏋️", "Máquinas",    "Ver equipos disponibles ahora", EstilosGym.COLOR_ACENTO));
            contenido.add(info);
        }

        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setBackground(EstilosGym.COLOR_FONDO);
        scroll.getViewport().setBackground(EstilosGym.COLOR_FONDO);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel crearTarjeta(String titulo, String valor, String sub, Color color) {
        JPanel t = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(EstilosGym.COLOR_PANEL_CLARO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(color);
                g2.fillRoundRect(0, 0, 6, getHeight(), 4, 4);
                g2.dispose();
            }
        };
        t.setOpaque(false);

        JLabel lTit = new JLabel(titulo);
        lTit.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lTit.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
        lTit.setBounds(18, 14, 220, 18);

        JLabel lVal = new JLabel(valor);
        lVal.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lVal.setForeground(color);
        lVal.setBounds(18, 34, 220, 38);

        JLabel lSub = new JLabel(sub);
        lSub.setFont(EstilosGym.FUENTE_PEQUEÑA);
        lSub.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
        lSub.setBounds(18, 76, 220, 16);

        t.add(lTit); t.add(lVal); t.add(lSub);
        return t;
    }

    private JPanel crearCardModulo(String emoji, String titulo, String desc, Color color) {
        JPanel c = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(EstilosGym.COLOR_PANEL_CLARO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 12, 12);
                g2.dispose();
            }
        };
        c.setOpaque(false);
        JLabel lEmo = new JLabel(emoji);
        lEmo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        lEmo.setBounds(14, 14, 36, 36);
        JLabel lTit = new JLabel(titulo);
        lTit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lTit.setForeground(EstilosGym.COLOR_TEXTO);
        lTit.setBounds(55, 14, 200, 20);
        JLabel lDesc = new JLabel(desc);
        lDesc.setFont(EstilosGym.FUENTE_PEQUEÑA);
        lDesc.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
        lDesc.setBounds(55, 36, 220, 16);
        c.add(lEmo); c.add(lTit); c.add(lDesc);
        return c;
    }
}
