package Gimnasio.Vistas;

import javax.swing.*;
import java.awt.*;

public class PanelAdminVista extends JFrame {

    private JPanel     contenidoCentral;
    private NavLateral nav;
    private JLabel     lblSeccion;

    private static final String[] SECCIONES = {"Inicio", "Clientes", "Máquinas", "Rutinas"};
    private static final String[] ICONOS    = {"🏠", "👥", "🏋️", "📋"};

    public PanelAdminVista() {
        setTitle("GymUTS — Panel de Administración");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1150, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(960, 600));
        construirUI();
    }

    private void construirUI() {
        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setBackground(EstilosGym.COLOR_FONDO);

        // ── Barra superior ──
        JPanel barraTop = new JPanel(new BorderLayout());
        barraTop.setBackground(new Color(12, 12, 18));
        barraTop.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, EstilosGym.COLOR_BORDE),
                BorderFactory.createEmptyBorder(8, 226, 8, 20)));

        lblSeccion = new JLabel("🏠  Inicio");
        lblSeccion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSeccion.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
        barraTop.add(lblSeccion, BorderLayout.WEST);

        JLabel lblDB = new JLabel("● BD SQLite conectada");
        lblDB.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblDB.setForeground(EstilosGym.COLOR_EXITO);
        barraTop.add(lblDB, BorderLayout.EAST);

        // ── Contenido central ──
        contenidoCentral = new JPanel(new BorderLayout());
        contenidoCentral.setBackground(EstilosGym.COLOR_FONDO);

        // ── Nav lateral ──
        nav = new NavLateral(SECCIONES, ICONOS);
        nav.setNavListener(seccion -> cambiarVista(seccion));
        nav.setActivo("Inicio");
        cambiarVista("Inicio");

        raiz.add(barraTop,         BorderLayout.NORTH);
        raiz.add(nav,              BorderLayout.WEST);
        raiz.add(contenidoCentral, BorderLayout.CENTER);
        setContentPane(raiz);
    }

    private void cambiarVista(String seccion) {
        contenidoCentral.removeAll();
        JPanel vista = switch (seccion) {
            case "Clientes" -> new ClientesVista();
            case "Máquinas" -> new MaquinasVista();
            case "Rutinas"  -> new RutinasVista();
            default         -> new DashboardVista();
        };
        lblSeccion.setText(switch (seccion) {
            case "Clientes" -> "👥  Clientes";
            case "Máquinas" -> "🏋️  Máquinas";
            case "Rutinas"  -> "📋  Rutinas";
            default         -> "🏠  Inicio";
        });
        contenidoCentral.add(vista, BorderLayout.CENTER);
        contenidoCentral.revalidate();
        contenidoCentral.repaint();
    }
}
