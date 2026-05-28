package Gimnasio.Vistas;

import javax.swing.*;
import java.awt.*;

public class PanelAdminVista extends JFrame {

    private JPanel     contenido;
    private NavLateral nav;
    private JLabel     lblRuta;

    private static final String[] SECCIONES = {
        "Inicio","Clientes","Pagos","Asistencias","Máquinas","Rutinas","Membresías","Permisos"
    };
    private static final String[] ICONOS = {
        "🏠","👥","💳","📅","🏋️","📋","🎫","🔐"
    };

    public PanelAdminVista() {
        setTitle("GymUTS — Administración");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1260, 740); setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1050, 640));
        construir();
    }

    private void construir() {
        JPanel raiz = new JPanel(new BorderLayout()); raiz.setBackground(EstilosGym.COLOR_FONDO);

        JPanel top = new JPanel(new BorderLayout()); top.setBackground(new Color(10,10,16));
        top.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0,0,1,0,EstilosGym.COLOR_BORDE),
            BorderFactory.createEmptyBorder(7,226,7,18)));
        lblRuta = new JLabel("🏠  Inicio");
        lblRuta.setFont(new Font("Segoe UI",Font.PLAIN,12)); lblRuta.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
        JLabel bdTag = new JLabel("● SQLite  ");
        bdTag.setFont(new Font("Segoe UI",Font.PLAIN,11)); bdTag.setForeground(EstilosGym.COLOR_EXITO);
        top.add(lblRuta,BorderLayout.WEST); top.add(bdTag,BorderLayout.EAST);

        contenido = new JPanel(new BorderLayout()); contenido.setBackground(EstilosGym.COLOR_FONDO);
        nav = new NavLateral(SECCIONES, ICONOS);
        nav.setNavListener(s -> cambiar(s));
        nav.setActivo("Inicio"); cambiar("Inicio");

        raiz.add(top,BorderLayout.NORTH); raiz.add(nav,BorderLayout.WEST); raiz.add(contenido,BorderLayout.CENTER);
        setContentPane(raiz);
    }

    private void cambiar(String s) {
        contenido.removeAll();
        JPanel v = switch (s) {
            case "Clientes"    -> new ClientesVista();
            case "Pagos"       -> new PagosVista();
            case "Asistencias" -> new AsistenciasVista();
            case "Máquinas"    -> new MaquinasVista();
            case "Rutinas"     -> new RutinasVista();
            case "Membresías"  -> new MembresiasVista();
            case "Permisos"    -> new GestionPermisosVista();
            default            -> new DashboardVista();
        };
        lblRuta.setText(switch (s) {
            case "Clientes"    -> "👥  Clientes";
            case "Pagos"       -> "💳  Pagos & Facturación";
            case "Asistencias" -> "📅  Control de Asistencias";
            case "Máquinas"    -> "🏋️  Máquinas";
            case "Rutinas"     -> "📋  Rutinas";
            case "Membresías"  -> "🎫  Membresías";
            case "Permisos"    -> "🔐  Gestión de Permisos";
            default            -> "🏠  Inicio";
        });
        contenido.add(v,BorderLayout.CENTER);
        contenido.revalidate(); contenido.repaint();
    }
}
