package Gimnasio.Vistas;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class PanelAdminVista extends JFrame {

    private JPanel     contenido;
    private NavLateral nav;
    private JLabel     lblRuta;

    private static final String[] SECCIONES = {
        "Inicio","Clientes","Pagos","Asistencias","Máquinas","Rutinas","Membresías","Permisos"
    };
    // nombres de los archivos PNG en src/Gimnasio/Iconos/ (sin extensión)
    private static final String[] ICONOS = {
        "inicio","clientes","pago","asistencias","maquina","rutina","membresias","permisos-de-usuario"
    };

    public PanelAdminVista() {
        setTitle("GymUTS — Administración");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1260, 740); setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1050, 640));
        construir();
    }

    private ImageIcon iconoTop(String nombre) {
        URL url = getClass().getResource("/Gimnasio/Iconos/" + nombre + ".png");
        if (url == null) return null;
        ImageIcon raw = new ImageIcon(url);
        Image scaled = raw.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private void construir() {
        JPanel raiz = new JPanel(new BorderLayout()); raiz.setBackground(EstilosGym.COLOR_FONDO);

        JPanel top = new JPanel(new BorderLayout()); top.setBackground(new Color(10,10,16));
        top.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0,0,1,0,EstilosGym.COLOR_BORDE),
            BorderFactory.createEmptyBorder(7,226,7,18)));
        lblRuta = new JLabel("  Inicio", iconoTop("inicio"), JLabel.LEFT);
        lblRuta.setFont(new Font("Segoe UI",Font.PLAIN,12)); lblRuta.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
        lblRuta.setIconTextGap(6);
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

        String[] rutaInfo = switch (s) {
            case "Clientes"    -> new String[]{"clientes",    "Clientes"};
            case "Pagos"       -> new String[]{"pago",        "Pagos & Facturación"};
            case "Asistencias" -> new String[]{"asistencias", "Control de Asistencias"};
            case "Máquinas"    -> new String[]{"maquina",     "Máquinas"};
            case "Rutinas"     -> new String[]{"rutina",      "Rutinas"};
            case "Membresías"  -> new String[]{"membresias",  "Membresías"};
            case "Permisos"    -> new String[]{"permisos-de-usuario", "Gestión de Permisos"};
            default            -> new String[]{"inicio",      "Inicio"};
        };
        ImageIcon ico = iconoTop(rutaInfo[0]);
        lblRuta.setIcon(ico);
        lblRuta.setText("  " + rutaInfo[1]);

        contenido.add(v, BorderLayout.CENTER);
        contenido.revalidate(); contenido.repaint();
    }
}
