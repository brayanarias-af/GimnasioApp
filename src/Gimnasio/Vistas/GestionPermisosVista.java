package Gimnasio.Vistas;

import Gimnasio.Controlador.PermisoDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List; 
import java.util.*;

public class GestionPermisosVista extends JPanel {

    private final PermisoDAO dao = new PermisoDAO();
    private JList<String>    listaUsuarios;
    private DefaultListModel<String> userModel = new DefaultListModel<>();
    private List<Object[]>           usuariosData = new ArrayList<>();
    private JPanel                   panelPermisos;
    private int                      idUsuarioSel = -1;
    private String                   rolUsuarioSel = "";

    public GestionPermisosVista() {
        setLayout(new BorderLayout());
        setBackground(EstilosGym.COLOR_FONDO);
        construir();
        cargarUsuarios();
    }

    private void construir() {
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(EstilosGym.COLOR_FONDO);
        header.setBorder(new EmptyBorder(18,24,12,24));
        JLabel titulo = new JLabel("Gestión de Permisos");
        titulo.setFont(EstilosGym.FUENTE_TITULO); titulo.setForeground(EstilosGym.COLOR_TEXTO);
        JLabel sub = new JLabel("Controla el acceso de los entrenadores a los módulos del sistema");
        sub.setFont(EstilosGym.FUENTE_NORMAL); sub.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
        JPanel textos = new JPanel(new GridLayout(2,1,0,3)); textos.setOpaque(false);
        textos.add(titulo); textos.add(sub);
        header.add(textos, BorderLayout.WEST);

        // Split: lista usuarios izq + permisos der
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerLocation(280); split.setDividerSize(4); split.setBorder(null);

        // Panel izquierdo
        JPanel pIzq = new JPanel(new BorderLayout()); pIzq.setBackground(EstilosGym.COLOR_PANEL);
        pIzq.setBorder(BorderFactory.createMatteBorder(0,0,0,1,EstilosGym.COLOR_BORDE));
        JLabel lblU = new JLabel("  Entrenadores");
        lblU.setFont(new Font("Segoe UI",Font.BOLD,12)); lblU.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
        lblU.setBorder(new EmptyBorder(10,10,8,10)); lblU.setBackground(new Color(14,14,20)); lblU.setOpaque(true);

        listaUsuarios = new JList<>(userModel);
        listaUsuarios.setBackground(EstilosGym.COLOR_PANEL); listaUsuarios.setForeground(EstilosGym.COLOR_TEXTO);
        listaUsuarios.setFont(EstilosGym.FUENTE_NORMAL); listaUsuarios.setFixedCellHeight(56);
        listaUsuarios.setSelectionBackground(new Color(255,87,34,80));
        listaUsuarios.setCellRenderer(new UsuarioCellRenderer());
        listaUsuarios.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && listaUsuarios.getSelectedIndex() >= 0) {
                int idx = listaUsuarios.getSelectedIndex();
                idUsuarioSel = (int) usuariosData.get(idx)[0];
                rolUsuarioSel = usuariosData.get(idx)[2].toString();
                mostrarPermisos();
            }
        });
        JScrollPane scU = new JScrollPane(listaUsuarios); scU.setBorder(BorderFactory.createEmptyBorder());
        scU.getViewport().setBackground(EstilosGym.COLOR_PANEL);
        pIzq.add(lblU, BorderLayout.NORTH); pIzq.add(scU, BorderLayout.CENTER);

        panelPermisos = new JPanel(new BorderLayout()); panelPermisos.setBackground(EstilosGym.COLOR_FONDO);
        mostrarPlaceholder();

        split.setLeftComponent(pIzq); split.setRightComponent(panelPermisos);
        add(header, BorderLayout.NORTH); add(split, BorderLayout.CENTER);
    }

    private void cargarUsuarios() {
        userModel.clear(); usuariosData.clear();
        usuariosData = dao.listarUsuariosGestionables();
        for (Object[] u : usuariosData)
            userModel.addElement(u[3].toString()); // nombre_completo
        if (!userModel.isEmpty()) listaUsuarios.setSelectedIndex(0);
    }

    private void mostrarPlaceholder() {
        panelPermisos.removeAll();
        JLabel m = new JLabel("← Selecciona un usuario para gestionar sus permisos", JLabel.CENTER);
        m.setFont(new Font("Segoe UI",Font.ITALIC,14)); m.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
        panelPermisos.add(m, BorderLayout.CENTER);
        panelPermisos.revalidate(); panelPermisos.repaint();
    }

    private void mostrarPermisos() {
        panelPermisos.removeAll();

        // Solo entrenadores gestionables; siempre usar módulos de entrenador
        String[] modulos = PermisoDAO.MODULOS_ENTRENADOR;
        Map<String,Boolean> permisos = dao.obtenerPermisos(idUsuarioSel, modulos);

        int idx = listaUsuarios.getSelectedIndex();
        String nombreCompleto = idx >= 0 ? usuariosData.get(idx)[3].toString() : "Usuario";
        String rol = idx >= 0 ? usuariosData.get(idx)[2].toString() : "";
        String usuario = idx >= 0 ? usuariosData.get(idx)[1].toString() : "";

        JPanel contenido = new JPanel(); contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBackground(EstilosGym.COLOR_FONDO);
        contenido.setBorder(new EmptyBorder(24,28,24,28));

        // Info usuario
        JPanel infoCard = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(EstilosGym.COLOR_PANEL_CLARO); g2.fillRoundRect(0,0,getWidth(),getHeight(),12,12);
                g2.dispose();
            }
        };
        infoCard.setOpaque(false); infoCard.setPreferredSize(new Dimension(0,80));
        infoCard.setMaximumSize(new Dimension(Integer.MAX_VALUE,80));
        infoCard.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lNom = new JLabel("👤  "+nombreCompleto); lNom.setFont(new Font("Segoe UI",Font.BOLD,16));
        lNom.setForeground(EstilosGym.COLOR_TEXTO); lNom.setBounds(18,14,400,22); infoCard.add(lNom);
        Color rolColor = "ENTRENADOR".equalsIgnoreCase(rol) ? new Color(0,180,200) : new Color(100,60,200);
        JLabel lRol = new JLabel("@"+usuario+"  ·  "+rol); lRol.setFont(EstilosGym.FUENTE_NORMAL);
        lRol.setForeground(rolColor); lRol.setBounds(18,40,400,18); infoCard.add(lRol);

        contenido.add(infoCard);
        contenido.add(Box.createVerticalStrut(22));

        // Título sección
        JLabel lTit = new JLabel("Módulos accesibles");
        lTit.setFont(EstilosGym.FUENTE_SUBTITULO); lTit.setForeground(EstilosGym.COLOR_TEXTO);
        lTit.setAlignmentX(LEFT_ALIGNMENT); contenido.add(lTit);
        JLabel lSub2 = new JLabel("Activa o desactiva las secciones que puede ver en su panel");
        lSub2.setFont(EstilosGym.FUENTE_PEQUEÑA); lSub2.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
        lSub2.setAlignmentX(LEFT_ALIGNMENT); contenido.add(lSub2);
        contenido.add(Box.createVerticalStrut(16));

        // Toggle por módulo
        Map<String,JToggleButton> toggles = new LinkedHashMap<>();
        for (String modulo : modulos) {
            boolean hab = permisos.getOrDefault(modulo, false);
            JPanel row = crearFilaPermiso(modulo, hab, (habilitado) -> {
                dao.setPermiso(idUsuarioSel, modulo, habilitado);
            });
            row.setAlignmentX(LEFT_ALIGNMENT);
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
            contenido.add(row);
            contenido.add(Box.createVerticalStrut(8));
        }

        contenido.add(Box.createVerticalStrut(20));

        // Botones acción masiva
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0)); acciones.setOpaque(false);
        acciones.setAlignmentX(LEFT_ALIGNMENT);
        JButton btnTodo = EstilosGym.crearBotonSecundario("✅ Habilitar todo");
        JButton btnNada = EstilosGym.crearBotonPeligro("🚫 Deshabilitar todo");
        btnTodo.addActionListener(e -> {
            for (String m : modulos) dao.setPermiso(idUsuarioSel, m, true);
            mostrarPermisos();
        });
        btnNada.addActionListener(e -> {
            for (String m : modulos) dao.setPermiso(idUsuarioSel, m, false);
            mostrarPermisos();
        });
        acciones.add(btnTodo); acciones.add(btnNada);
        contenido.add(acciones);

        JScrollPane scroll = new JScrollPane(contenido); scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(EstilosGym.COLOR_FONDO);
        panelPermisos.add(scroll, BorderLayout.CENTER);
        panelPermisos.revalidate(); panelPermisos.repaint();
    }

    private JPanel crearFilaPermiso(String modulo, boolean habilitado, java.util.function.Consumer<Boolean> onChange) {
        JPanel row = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(EstilosGym.COLOR_PANEL_CLARO); g2.fillRoundRect(0,0,getWidth(),getHeight(),10,10);
                g2.dispose();
            }
        };
        row.setOpaque(false);

        JLabel lMod = new JLabel("  "+modulo);
        java.net.URL icoUrl = getClass().getResource("/Gimnasio/Iconos/" + moduloIcon(modulo) + ".png");
        if (icoUrl != null) {
            ImageIcon icoScaled = new ImageIcon(new ImageIcon(icoUrl).getImage().getScaledInstance(18,18,java.awt.Image.SCALE_SMOOTH));
            lMod.setIcon(icoScaled); lMod.setIconTextGap(6);
        }
        lMod.setFont(new Font("Segoe UI",Font.BOLD,13)); lMod.setForeground(EstilosGym.COLOR_TEXTO);
        lMod.setBounds(16,10,280,20); row.add(lMod);

        JLabel lDesc = new JLabel(moduloDesc(modulo));
        lDesc.setFont(EstilosGym.FUENTE_PEQUEÑA); lDesc.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
        lDesc.setBounds(16,32,280,16); row.add(lDesc);

        // Toggle switch visual
        JToggleButton toggle = new JToggleButton(habilitado ? "ON" : "OFF", habilitado) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = isSelected() ? EstilosGym.COLOR_EXITO : new Color(80,80,100);
                g2.setColor(bg); g2.fillRoundRect(0,0,getWidth(),getHeight(),getHeight(),getHeight());
                int cx = isSelected() ? getWidth()-getHeight()+2 : 2;
                g2.setColor(Color.WHITE); g2.fillOval(cx,2,getHeight()-4,getHeight()-4);
                g2.setColor(Color.WHITE); g2.setFont(new Font("Segoe UI",Font.BOLD,10));
                String txt = isSelected()?"ON":"OFF";
                FontMetrics fm=g2.getFontMetrics(); int tx=isSelected()?6:getHeight();
                g2.drawString(txt,tx,(getHeight()+fm.getAscent()-fm.getDescent())/2); g2.dispose();
            }
        };
        toggle.setPreferredSize(new Dimension(64,28)); toggle.setOpaque(false);
        toggle.setContentAreaFilled(false); toggle.setBorderPainted(false); toggle.setFocusPainted(false);
        toggle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toggle.setBounds(300,14,64,28);
        toggle.addActionListener(e -> {
            toggle.setText(toggle.isSelected()?"ON":"OFF");
            onChange.accept(toggle.isSelected());
            toggle.repaint();
        });
        row.add(toggle);

        return row;
    }

    private String moduloIcon(String m) {
        return switch (m) {
            case "Mis Rutinas","Clientes Asignados" -> "rutina";
            case "Máquinas"         -> "maquina";
            case "Mis Pagos"        -> "pago";
            case "Mi Progreso","Progreso Clientes" -> "ingresos-de-dinero";
            default -> "inicio";
        };
    }
    private String moduloDesc(String m) {
        return switch (m) {
            case "Mis Rutinas"       -> "Ver rutinas de entrenamiento asignadas";
            case "Máquinas"          -> "Consultar equipos disponibles";
            case "Mis Pagos"         -> "Historial de pagos y membresías";
            case "Mi Progreso"       -> "Seguimiento de progreso físico";
            case "Clientes Asignados"-> "Ver clientes del entrenador";
            case "Progreso Clientes" -> "Registrar progreso de clientes";
            default -> "";
        };
    }

    class UsuarioCellRenderer extends DefaultListCellRenderer {
        @Override public Component getListCellRendererComponent(JList<?> l,Object v,int i,boolean sel,boolean foc){
            super.getListCellRendererComponent(l,v,i,sel,foc);
            if (i < usuariosData.size()) {
                String rol = usuariosData.get(i)[2].toString();
                String usr = usuariosData.get(i)[1].toString();
                Color rc = "ENTRENADOR".equalsIgnoreCase(rol)?new Color(0,180,200):new Color(100,60,200);
                setText("<html><b style='color:white'>"+v+"</b><br/>"
                    +"<font color='#"+String.format("%02x%02x%02x",rc.getRed(),rc.getGreen(),rc.getBlue())+"'>"+rol+"</font>"
                    +" <font color='#9696a5'>@"+usr+"</font></html>");
            }
            setBorder(new EmptyBorder(10,14,10,14));
            setBackground(sel?new Color(255,87,34,80):EstilosGym.COLOR_PANEL);
            setForeground(EstilosGym.COLOR_TEXTO); return this;
        }
    }
}
