package Gimnasio.Vistas;

import Gimnasio.Controlador.MaquinaDAO;
import Gimnasio.Controlador.RutinaDAO;
import Gimnasio.Modelo.Maquina;
import Gimnasio.Modelo.Rutina;
import javax.swing.*;
import java.awt.*;

public class PanelUsuarioVista extends JFrame {

    private JPanel     contenidoCentral;
    private NavLateral nav;

    private static final String[] SECCIONES = {"Inicio", "Mis Rutinas", "Máquinas"};
    private static final String[] ICONOS    = {"🏠", "📋", "🏋️"};

    public PanelUsuarioVista() {
        setTitle("GymUTS — Mi Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 660);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(860, 560));
        construirUI();
    }

    private void construirUI() {
        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setBackground(EstilosGym.COLOR_FONDO);

        JPanel barraTop = new JPanel(new BorderLayout());
        barraTop.setBackground(new Color(12, 12, 18));
        barraTop.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, EstilosGym.COLOR_BORDE),
                BorderFactory.createEmptyBorder(8, 226, 8, 20)));
        JLabel lblPath = new JLabel("Mi Panel de Entrenamiento");
        lblPath.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblPath.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
        barraTop.add(lblPath, BorderLayout.WEST);

        contenidoCentral = new JPanel(new BorderLayout());
        contenidoCentral.setBackground(EstilosGym.COLOR_FONDO);

        nav = new NavLateral(SECCIONES, ICONOS);
        nav.setNavListener(this::cambiarVista);
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
            case "Mis Rutinas" -> crearVistaRutinas();
            case "Máquinas"    -> crearVistaMaquinas();
            default            -> new DashboardVista();
        };
        contenidoCentral.add(vista, BorderLayout.CENTER);
        contenidoCentral.revalidate();
        contenidoCentral.repaint();
    }

    // ── Rutinas en cards (solo lectura) ──
    private JPanel crearVistaRutinas() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(EstilosGym.COLOR_FONDO);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(EstilosGym.COLOR_FONDO);
        header.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 25));
        JLabel titulo = new JLabel("📋  Rutinas Disponibles");
        titulo.setFont(EstilosGym.FUENTE_TITULO);
        titulo.setForeground(EstilosGym.COLOR_TEXTO);
        header.add(titulo, BorderLayout.WEST);

        JPanel grid = new JPanel(new GridLayout(0, 2, 16, 16));
        grid.setBackground(EstilosGym.COLOR_FONDO);
        grid.setBorder(BorderFactory.createEmptyBorder(4, 25, 25, 25));

        RutinaDAO dao = new RutinaDAO();
        for (Rutina r : dao.listarTodas()) {
            grid.add(crearCardRutina(r));
        }

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(EstilosGym.COLOR_FONDO);

        root.add(header, BorderLayout.NORTH);
        root.add(scroll,  BorderLayout.CENTER);
        return root;
    }

    private JPanel crearCardRutina(Rutina r) {
        Color nivelColor = "Avanzado".equals(r.getNivel()) ? EstilosGym.COLOR_PELIGRO
                : "Intermedio".equals(r.getNivel()) ? new Color(255, 200, 0) : EstilosGym.COLOR_EXITO;

        JPanel card = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(EstilosGym.COLOR_PANEL_CLARO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(nivelColor);
                g2.fillRoundRect(0, 0, getWidth(), 5, 4, 4);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(0, 160));

        JLabel lNombre = new JLabel(r.getNombreRutina());
        lNombre.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lNombre.setForeground(EstilosGym.COLOR_TEXTO);
        lNombre.setBounds(16, 18, 300, 20);
        card.add(lNombre);

        JLabel lNivel = new JLabel("  " + (r.getNivel() != null ? r.getNivel() : "—") + "  ");
        lNivel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lNivel.setForeground(nivelColor);
        lNivel.setBorder(BorderFactory.createLineBorder(nivelColor));
        lNivel.setBounds(16, 44, 90, 18);
        card.add(lNivel);

        JLabel lObj = new JLabel("🎯 " + (r.getObjetivo() != null ? r.getObjetivo() : "—"));
        lObj.setFont(EstilosGym.FUENTE_PEQUEÑA);
        lObj.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
        lObj.setBounds(16, 72, 320, 16);
        card.add(lObj);

        JLabel lDur = new JLabel("⏱ " + r.getDuracionSemanas() + " semanas");
        lDur.setFont(EstilosGym.FUENTE_PEQUEÑA);
        lDur.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
        lDur.setBounds(16, 92, 200, 16);
        card.add(lDur);

        JLabel lEnt = new JLabel("👤 " + (r.getNombreEntrenador() != null ? r.getNombreEntrenador() : "—"));
        lEnt.setFont(EstilosGym.FUENTE_PEQUEÑA);
        lEnt.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
        lEnt.setBounds(16, 112, 280, 16);
        card.add(lEnt);

        String descRaw = r.getDescripcion() != null ? r.getDescripcion() : "";
        JLabel lDesc = new JLabel(descRaw.length() > 60 ? descRaw.substring(0, 57) + "..." : descRaw);
        lDesc.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lDesc.setForeground(new Color(120, 120, 140));
        lDesc.setBounds(16, 132, 320, 16);
        card.add(lDesc);

        return card;
    }

    // ── Máquinas en cards (solo lectura) ──
    private JPanel crearVistaMaquinas() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(EstilosGym.COLOR_FONDO);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(EstilosGym.COLOR_FONDO);
        header.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 25));
        JLabel titulo = new JLabel("🏋️  Máquinas Disponibles");
        titulo.setFont(EstilosGym.FUENTE_TITULO);
        titulo.setForeground(EstilosGym.COLOR_TEXTO);
        header.add(titulo, BorderLayout.WEST);

        JPanel grid = new JPanel(new GridLayout(0, 3, 16, 16));
        grid.setBackground(EstilosGym.COLOR_FONDO);
        grid.setBorder(BorderFactory.createEmptyBorder(4, 25, 25, 25));

        MaquinaDAO dao = new MaquinaDAO();
        for (Maquina m : dao.listarTodas()) {
            grid.add(crearCardMaquina(m));
        }

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(EstilosGym.COLOR_FONDO);

        root.add(header, BorderLayout.NORTH);
        root.add(scroll,  BorderLayout.CENTER);
        return root;
    }

    private JPanel crearCardMaquina(Maquina m) {
        Color estadoColor = "Disponible".equals(m.getEstado()) ? EstilosGym.COLOR_EXITO
                : "En uso".equals(m.getEstado()) ? new Color(255, 200, 0) : EstilosGym.COLOR_PELIGRO;

        JPanel card = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(EstilosGym.COLOR_PANEL_CLARO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(estadoColor);
                g2.fillRoundRect(0, 0, getWidth(), 5, 4, 4);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(0, 155));

        JLabel icono = new JLabel("Cardio".equalsIgnoreCase(m.getTipo()) ? "🚴" : "💪");
        icono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        icono.setBounds(14, 16, 40, 38);
        card.add(icono);

        JLabel lNom = new JLabel(m.getNombre());
        lNom.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lNom.setForeground(EstilosGym.COLOR_TEXTO);
        lNom.setBounds(60, 16, 220, 18);
        card.add(lNom);

        JLabel lTipo = new JLabel((m.getTipo() != null ? m.getTipo() : "—") + " · " + (m.getMarca() != null ? m.getMarca() : "—"));
        lTipo.setFont(EstilosGym.FUENTE_PEQUEÑA);
        lTipo.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
        lTipo.setBounds(60, 38, 220, 16);
        card.add(lTipo);

        JLabel lMod = new JLabel("Modelo: " + (m.getModelo() != null ? m.getModelo() : "—"));
        lMod.setFont(EstilosGym.FUENTE_PEQUEÑA);
        lMod.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
        lMod.setBounds(14, 70, 220, 16);
        card.add(lMod);

        JLabel lSer = new JLabel("Serial: " + (m.getSerial() != null ? m.getSerial() : "—"));
        lSer.setFont(EstilosGym.FUENTE_PEQUEÑA);
        lSer.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
        lSer.setBounds(14, 90, 220, 16);
        card.add(lSer);

        JLabel lEst = new JLabel("● " + (m.getEstado() != null ? m.getEstado() : "—"));
        lEst.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lEst.setForeground(estadoColor);
        lEst.setBounds(14, 114, 200, 18);
        card.add(lEst);

        JLabel lMant = new JLabel("Mant: " + (m.getFechaMantenimiento() != null ? m.getFechaMantenimiento() : "—"));
        lMant.setFont(EstilosGym.FUENTE_PEQUEÑA);
        lMant.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
        lMant.setBounds(14, 134, 220, 16);
        card.add(lMant);

        return card;
    }
}
