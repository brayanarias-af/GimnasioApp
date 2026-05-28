package Gimnasio.Vistas;

import Gimnasio.Controlador.*;
import Gimnasio.Controlador.Sesion;
import Gimnasio.Modelo.*;
import Gimnasio.Conexion.Conexion;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class PanelUsuarioVista extends JFrame {

    private JPanel     contenido;
    private NavLateral nav;
    private List<String> seccionesHabilitadas = new ArrayList<>();
    private List<String> iconosHabilitados    = new ArrayList<>();

    private static final String[] MODULOS_CLIENTE    = {"Inicio","Mis Rutinas","Máquinas","Mis Pagos","Mi Progreso"};
    private static final String[] MODULOS_ENTRENADOR = {"Inicio","Mis Rutinas","Máquinas","Clientes Asignados","Progreso Clientes"};
    private static final Map<String,String> ICONOS_MAP = Map.of(
        "Inicio","🏠","Mis Rutinas","📋","Máquinas","🏋️",
        "Mis Pagos","💳","Mi Progreso","📈",
        "Clientes Asignados","👥","Progreso Clientes","📊"
    );

    public PanelUsuarioVista() {
        setTitle("GymUTS — " + (Sesion.esEntrenador() ? "Panel Entrenador" : "Mi Panel"));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100,680); setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900,580));
        resolverModulos();
        construir();
    }

    private void resolverModulos() {
        seccionesHabilitadas.clear(); iconosHabilitados.clear();
        seccionesHabilitadas.add("Inicio"); iconosHabilitados.add("🏠");

        // Cliente con membresía vencida o pendiente: solo ve "Mis Pagos"
        if (Sesion.esCliente() && Sesion.getIdCliente() > 0) {
            String est = obtenerEstadoMembresia();
            if ("Vencida".equalsIgnoreCase(est) || "Pendiente".equalsIgnoreCase(est)) {
                seccionesHabilitadas.add("Mis Pagos"); iconosHabilitados.add("💳");
                return;
            }
        }

        String[] base = Sesion.esEntrenador() ? MODULOS_ENTRENADOR : MODULOS_CLIENTE;
        for (String m : base) {
            if ("Inicio".equals(m)) continue;
            if (Sesion.esCliente() || Sesion.tienePermiso(m)) {
                seccionesHabilitadas.add(m);
                iconosHabilitados.add(ICONOS_MAP.getOrDefault(m,"📌"));
            }
        }
    }

    private String obtenerEstadoMembresia() {
        try (var ps = Gimnasio.Conexion.Conexion.getConexion().prepareStatement(
                "SELECT estado_membresia FROM clientes WHERE id_cliente=?")) {
            ps.setInt(1, Sesion.getIdCliente());
            var rs = ps.executeQuery();
            if (rs.next()) { String s = rs.getString(1); rs.close(); return s != null ? s : ""; }
            rs.close();
        } catch (Exception e) {}
        return "";
    }

    private void construir() {
        JPanel raiz = new JPanel(new BorderLayout()); raiz.setBackground(EstilosGym.COLOR_FONDO);

        JPanel top = new JPanel(new BorderLayout()); top.setBackground(new Color(10,10,16));
        top.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0,0,1,0,EstilosGym.COLOR_BORDE),
            BorderFactory.createEmptyBorder(7,226,7,18)));
        String rolLabel = Sesion.esEntrenador() ? "Panel Entrenador" : "Mi Panel de Entrenamiento";
        JLabel lbl = new JLabel(rolLabel);
        lbl.setFont(new Font("Segoe UI",Font.PLAIN,12)); lbl.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
        top.add(lbl,BorderLayout.WEST);

        // Alerta pago pendiente solo para clientes con id válido
        if (Sesion.esCliente() && Sesion.getIdCliente() > 0) {
            try {
                boolean tienePendiente = new PagoDAO().listarPorCliente(Sesion.getIdCliente())
                    .stream().anyMatch(p -> "Pendiente".equalsIgnoreCase(p.getEstado()));
                if (tienePendiente) {
                    JLabel alerta = new JLabel("⚠  Tienes un pago pendiente");
                    alerta.setFont(new Font("Segoe UI",Font.BOLD,11));
                    alerta.setForeground(new Color(255,200,0));
                    alerta.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(255,200,0,100),1),
                        new EmptyBorder(2,10,2,10)));
                    top.add(alerta,BorderLayout.EAST);
                }
            } catch (Exception ignored) {}
        }

        contenido = new JPanel(new BorderLayout()); contenido.setBackground(EstilosGym.COLOR_FONDO);
        nav = new NavLateral(
            seccionesHabilitadas.toArray(new String[0]),
            iconosHabilitados.toArray(new String[0])
        );
        nav.setNavListener(this::cambiar);
        nav.setActivo("Inicio"); cambiar("Inicio");

        raiz.add(top,BorderLayout.NORTH); raiz.add(nav,BorderLayout.WEST); raiz.add(contenido,BorderLayout.CENTER);
        setContentPane(raiz);
    }

    private void cambiar(String s) {
        contenido.removeAll();
        JPanel v = switch (s) {
            case "Mis Rutinas"        -> vistaRutinas();
            case "Máquinas"           -> vistaMaquinas();
            case "Mis Pagos"          -> vistaMisPagos();
            case "Mi Progreso"        -> vistaMiProgreso();
            case "Clientes Asignados" -> vistaClientesAsignados();
            case "Progreso Clientes"  -> vistaProgresoClientes();
            default                   -> vistaInicio();
        };
        contenido.add(v, BorderLayout.CENTER);
        contenido.revalidate(); contenido.repaint();
    }

    private JPanel vistaInicio() {
        // Si cliente con membresía vencida/pendiente, mostrar pantalla bloqueada
        if (Sesion.esCliente() && Sesion.getIdCliente() > 0) {
            String est = obtenerEstadoMembresia();
            if ("Vencida".equalsIgnoreCase(est) || "Pendiente".equalsIgnoreCase(est)) {
                return pantallaMembresiaVencida(est);
            }
        }
        return new DashboardVista();
    }

    private JPanel pantallaMembresiaVencida(String estado) {
        JPanel root = panel();
        root.setLayout(new GridBagLayout());

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(25, 18, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255,140,0,180), 2),
            new EmptyBorder(40, 60, 40, 60)));

        JLabel icono = new JLabel("🔒", JLabel.CENTER); icono.setAlignmentX(0.5f);
        icono.setFont(new Font("Segoe UI Emoji",Font.PLAIN,64));

        JLabel titulo = new JLabel("Acceso Restringido", JLabel.CENTER); titulo.setAlignmentX(0.5f);
        titulo.setFont(new Font("Segoe UI",Font.BOLD,26));
        titulo.setForeground(new Color(255,150,0));

        boolean esVencida = "Vencida".equalsIgnoreCase(estado);
        String msg = esVencida
            ? "<html><center>Tu membresía ha <b>vencido</b>.<br><br>"
             + "Para continuar usando el gimnasio,<br>"
             + "debes renovar tu membresía.<br><br>"
             + "Dirígete a <b>Mis Pagos</b> para regularizarla<br>"
             + "o comunícate con recepción.</center></html>"
            : "<html><center>Tu membresía tiene un <b>pago pendiente</b>.<br><br>"
             + "Mientras no regularices tu pago,<br>"
             + "no puedes acceder a los módulos.<br><br>"
             + "Ve a <b>Mis Pagos</b> para solucionarlo.</center></html>";

        JLabel desc = new JLabel(msg, JLabel.CENTER); desc.setAlignmentX(0.5f);
        desc.setFont(new Font("Segoe UI",Font.PLAIN,14));
        desc.setForeground(EstilosGym.COLOR_TEXTO);

        JButton btnPagos = EstilosGym.crearBotonPrimario("💳  Ir a Mis Pagos");
        btnPagos.setAlignmentX(0.5f); btnPagos.setPreferredSize(new Dimension(200,42));
        btnPagos.setMaximumSize(new Dimension(200,42));
        btnPagos.addActionListener(e -> { nav.setActivo("Mis Pagos"); cambiar("Mis Pagos"); });

        card.add(icono); card.add(Box.createVerticalStrut(16));
        card.add(titulo); card.add(Box.createVerticalStrut(20));
        card.add(desc);   card.add(Box.createVerticalStrut(28));
        card.add(btnPagos);

        root.add(card);
        return root;
    }

    // ─────────────── RUTINAS ───────────────
    private JPanel vistaRutinas() {
        JPanel root=panel(); JPanel hdr=header("📋  Mis Rutinas de Entrenamiento"); root.add(hdr,BorderLayout.NORTH);

        JPanel grid=new JPanel(new GridLayout(0,2,16,16));
        grid.setBackground(EstilosGym.COLOR_FONDO); grid.setBorder(new EmptyBorder(4,25,25,25));

        List<Rutina> rutinas;
        try {
            if (Sesion.esCliente() && Sesion.getIdCliente() > 0) {
                rutinas = new RutinaDAO().listarPorCliente(Sesion.getIdCliente());
            } else {
                rutinas = new RutinaDAO().listarTodas();
            }
        } catch (Exception e) {
            rutinas = new ArrayList<>();
        }

        if (rutinas.isEmpty()) {
            JLabel vacio=new JLabel("No tienes rutinas asignadas aún.",JLabel.CENTER);
            vacio.setFont(new Font("Segoe UI",Font.ITALIC,14)); vacio.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
            root.add(vacio,BorderLayout.CENTER); return root;
        }
        for (Rutina r : rutinas) grid.add(cardRutina(r));
        root.add(scroll(grid),BorderLayout.CENTER); return root;
    }

    private JPanel cardRutina(Rutina r) {
        Color nc="Avanzado".equals(r.getNivel())?EstilosGym.COLOR_PELIGRO
               :"Intermedio".equals(r.getNivel())?new Color(255,200,0):EstilosGym.COLOR_EXITO;
        JPanel c=cardBase(nc); c.setPreferredSize(new Dimension(0,190));

        txt(c,r.getNombreRutina(),"Segoe UI",Font.BOLD,14,EstilosGym.COLOR_TEXTO,16,18,320,20);
        JLabel lniv=new JLabel("  "+r.getNivel()+"  "); lniv.setFont(new Font("Segoe UI",Font.BOLD,10));
        lniv.setForeground(nc); lniv.setBorder(BorderFactory.createLineBorder(nc));
        lniv.setBounds(16,44,90,18); c.add(lniv);
        txt(c,"🎯 "+(r.getObjetivo()!=null?r.getObjetivo():"—"),"Segoe UI",Font.PLAIN,11,EstilosGym.COLOR_TEXTO_GRIS,16,70,320,16);
        txt(c,"⏱ "+r.getDuracionSemanas()+" semanas","Segoe UI",Font.PLAIN,11,EstilosGym.COLOR_TEXTO_GRIS,16,90,220,16);
        txt(c,"👤 "+(r.getNombreEntrenador()!=null?r.getNombreEntrenador():"—"),"Segoe UI",Font.PLAIN,11,EstilosGym.COLOR_TEXTO_GRIS,16,110,300,16);

        EjercicioDAO ejDAO = new EjercicioDAO();
        Map<String,List<Ejercicio>> porDia = ejDAO.obtenerPorDia(r.getIdRutina());
        StringBuilder diasTxt = new StringBuilder();
        for (String dia : EjercicioDAO.DIAS) {
            int n = porDia.getOrDefault(dia,List.of()).size();
            if (n > 0) diasTxt.append(dia.substring(0,3)).append("(").append(n).append(") ");
        }
        txt(c,"📅 "+diasTxt.toString().trim(),"Segoe UI",Font.PLAIN,10,new Color(120,120,140),16,132,360,16);

        JButton btn=new JButton("Ver plan semanal →"); btn.setFont(new Font("Segoe UI",Font.BOLD,10));
        btn.setForeground(nc); btn.setBackground(new Color(0,0,0,0));
        btn.setBorderPainted(false); btn.setContentAreaFilled(false);
        btn.setFocusPainted(false); btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBounds(16,156,200,22);
        btn.addActionListener(e->mostrarDetalleSemana(r, nc));
        c.add(btn);
        return c;
    }

    private void mostrarDetalleSemana(Rutina r, Color color) {
        JDialog dlg=new JDialog(this,"Plan Semanal — "+r.getNombreRutina(),true);
        dlg.setSize(980,580); dlg.setLocationRelativeTo(this);

        JPanel root=new JPanel(new BorderLayout()); root.setBackground(EstilosGym.COLOR_FONDO);
        JPanel top=new JPanel(new BorderLayout()); top.setBackground(EstilosGym.COLOR_FONDO);
        top.setBorder(new EmptyBorder(16,20,12,20));
        JLabel lN=new JLabel("📅  "+r.getNombreRutina()+" — Plan Semanal");
        lN.setFont(new Font("Segoe UI",Font.BOLD,16)); lN.setForeground(EstilosGym.COLOR_TEXTO);
        JLabel lS=new JLabel(r.getNivel()+" · "+r.getDuracionSemanas()+" semanas · Entrenador: "+(r.getNombreEntrenador()!=null?r.getNombreEntrenador():"—"));
        lS.setFont(EstilosGym.FUENTE_PEQUEÑA); lS.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
        JPanel textos=new JPanel(new GridLayout(2,1,0,3)); textos.setOpaque(false);
        textos.add(lN); textos.add(lS); top.add(textos,BorderLayout.WEST);

        JPanel grid=new JPanel(new GridLayout(1,7,8,0));
        grid.setBackground(EstilosGym.COLOR_FONDO); grid.setBorder(new EmptyBorder(4,16,16,16));

        Color[] cols={new Color(100,60,200),new Color(255,160,0),new Color(0,180,200),
                      EstilosGym.COLOR_ACENTO,new Color(80,200,120),new Color(200,80,200),new Color(100,160,255)};
        EjercicioDAO ejDAO=new EjercicioDAO();
        Map<String,List<Ejercicio>> porDia=ejDAO.obtenerPorDia(r.getIdRutina());
        int ci=0;
        for (String dia:EjercicioDAO.DIAS) {
            grid.add(columDiaSoloLectura(dia, porDia.getOrDefault(dia,List.of()), cols[ci%cols.length])); ci++;
        }
        JScrollPane sc=new JScrollPane(grid); sc.setBorder(BorderFactory.createEmptyBorder());
        sc.getViewport().setBackground(EstilosGym.COLOR_FONDO);
        root.add(top,BorderLayout.NORTH); root.add(sc,BorderLayout.CENTER);
        dlg.setContentPane(root); dlg.setVisible(true);
    }

    private JPanel columDiaSoloLectura(String dia, List<Ejercicio> ejs, Color color) {
        JPanel col=new JPanel(new BorderLayout(0,4));
        col.setBackground(EstilosGym.COLOR_PANEL);
        col.setBorder(BorderFactory.createLineBorder(EstilosGym.COLOR_BORDE,1));

        JPanel cab=new JPanel(null){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setColor(color); g2.fillRect(0,0,getWidth(),getHeight()); g2.dispose(); super.paintComponent(g);
            }
        };
        cab.setPreferredSize(new Dimension(0,34)); cab.setOpaque(false);
        JLabel ld=new JLabel(dia,JLabel.CENTER); ld.setFont(new Font("Segoe UI",Font.BOLD,12));
        ld.setForeground(Color.WHITE); ld.setBounds(0,0,300,34); cab.add(ld);

        DefaultListModel<Ejercicio> mod=new DefaultListModel<>();
        for (Ejercicio e:ejs) mod.addElement(e);
        JList<Ejercicio> lista=new JList<>(mod);
        lista.setBackground(EstilosGym.COLOR_PANEL); lista.setFont(new Font("Segoe UI",Font.PLAIN,11));
        lista.setFixedCellHeight(50); lista.setEnabled(false);
        lista.setCellRenderer(new DefaultListCellRenderer(){
            @Override public Component getListCellRendererComponent(JList<?> l,Object v,int i,boolean s,boolean f){
                super.getListCellRendererComponent(l,v,i,s,f);
                Ejercicio ej=(Ejercicio)v;
                setText("<html><b style='color:white'>"+(i+1)+". "+ej.getNombre()+"</b><br/>"
                    +"<font color='#9696a5'>"+ej.getSeries()+"x"+ej.getRepeticiones()+" · "+ej.getDescansoSegundos()+"s</font></html>");
                setBorder(new EmptyBorder(5,8,5,8));
                setBackground(EstilosGym.COLOR_PANEL); setForeground(EstilosGym.COLOR_TEXTO); return this;
            }
        });

        if (ejs.isEmpty()) {
            JLabel libre=new JLabel("Descanso 💤",JLabel.CENTER);
            libre.setFont(new Font("Segoe UI",Font.ITALIC,11)); libre.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
            JPanel p=new JPanel(new BorderLayout()); p.setBackground(EstilosGym.COLOR_PANEL); p.add(libre,BorderLayout.CENTER);
            col.add(cab,BorderLayout.NORTH); col.add(p,BorderLayout.CENTER); return col;
        }
        JScrollPane sc=new JScrollPane(lista); sc.setBorder(BorderFactory.createEmptyBorder());
        sc.getViewport().setBackground(EstilosGym.COLOR_PANEL);
        col.add(cab,BorderLayout.NORTH); col.add(sc,BorderLayout.CENTER); return col;
    }

    // ─────────────── MÁQUINAS ───────────────
    private JPanel vistaMaquinas() {
        JPanel root=panel(); root.add(header("🏋️  Máquinas del Gimnasio"),BorderLayout.NORTH);
        JPanel grid=new JPanel(new GridLayout(0,3,16,16));
        grid.setBackground(EstilosGym.COLOR_FONDO); grid.setBorder(new EmptyBorder(4,25,25,25));
        try {
            for (Maquina m:new MaquinaDAO().listarTodas()) grid.add(cardMaquina(m));
        } catch (Exception ignored) {}
        root.add(scroll(grid),BorderLayout.CENTER); return root;
    }

    private JPanel cardMaquina(Maquina m) {
        Color ec="Disponible".equals(m.getEstado())?EstilosGym.COLOR_EXITO
               :"En uso".equals(m.getEstado())?new Color(255,200,0):EstilosGym.COLOR_PELIGRO;
        JPanel c=cardBase(ec); c.setPreferredSize(new Dimension(0,150));
        JLabel ico=new JLabel("Cardio".equalsIgnoreCase(m.getTipo())?"🚴":"💪");
        ico.setFont(new Font("Segoe UI Emoji",Font.PLAIN,26)); ico.setBounds(12,14,36,36); c.add(ico);
        txt(c,m.getNombre(),"Segoe UI",Font.BOLD,13,EstilosGym.COLOR_TEXTO,54,14,210,18);
        txt(c,(m.getTipo()!=null?m.getTipo():"—")+" · "+(m.getMarca()!=null?m.getMarca():"—"),"Segoe UI",Font.PLAIN,11,EstilosGym.COLOR_TEXTO_GRIS,54,34,210,16);
        txt(c,"Modelo: "+(m.getModelo()!=null?m.getModelo():"—"),"Segoe UI",Font.PLAIN,11,EstilosGym.COLOR_TEXTO_GRIS,14,64,220,16);
        txt(c,"Serial: "+(m.getSerial()!=null?m.getSerial():"—"),"Segoe UI",Font.PLAIN,11,EstilosGym.COLOR_TEXTO_GRIS,14,82,220,16);
        txt(c,"● "+(m.getEstado()!=null?m.getEstado():"—"),"Segoe UI",Font.BOLD,12,ec,14,106,180,18);
        txt(c,"Mant: "+(m.getFechaMantenimiento()!=null?m.getFechaMantenimiento():"—"),"Segoe UI",Font.PLAIN,11,EstilosGym.COLOR_TEXTO_GRIS,14,126,220,16);
        return c;
    }

    // ─────────────── MIS PAGOS ───────────────
    private JPanel vistaMisPagos() {
        JPanel root = panel();
        int idCliente = Sesion.getIdCliente();

        if (idCliente <= 0) {
            root.add(header("💳  Mis Pagos"), BorderLayout.NORTH);
            JLabel err = new JLabel("No se pudo identificar tu cuenta. Contacta al administrador.", JLabel.CENTER);
            err.setFont(new Font("Segoe UI",Font.ITALIC,13)); err.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
            root.add(err, BorderLayout.CENTER); return root;
        }

        PagoDAO pagoDAO = new PagoDAO();
        List<Pago> pagos;
        try { pagos = pagoDAO.listarPorCliente(idCliente); } catch (Exception e) { pagos = new ArrayList<>(); }
        String estMem = obtenerEstadoMembresia();

        // Header con estado membresía
        JPanel norte = new JPanel(new BorderLayout()); norte.setOpaque(false);
        norte.add(header("💳  Mis Pagos"), BorderLayout.WEST);

        // Badge estado membresía
        Color bColor = "Activa".equalsIgnoreCase(estMem) ? EstilosGym.COLOR_EXITO
                     : "Vencida".equalsIgnoreCase(estMem) ? EstilosGym.COLOR_PELIGRO
                     : new Color(255,200,0);
        JLabel badgeEst = new JLabel("  Membresía: " + (estMem != null ? estMem : "Sin datos") + "  ");
        badgeEst.setFont(new Font("Segoe UI",Font.BOLD,12));
        badgeEst.setForeground(bColor);
        badgeEst.setBorder(BorderFactory.createLineBorder(bColor, 1));
        JPanel badgePan = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 18)); badgePan.setOpaque(false);
        badgePan.add(badgeEst); norte.add(badgePan, BorderLayout.EAST);

        // Alerta si pendiente o vencida
        boolean tienePendiente = pagos.stream().anyMatch(p -> "Pendiente".equalsIgnoreCase(p.getEstado()));
        boolean membVencida    = "Vencida".equalsIgnoreCase(estMem);
        if (tienePendiente || membVencida) {
            JPanel alerta = new JPanel(new BorderLayout()); alerta.setBackground(new Color(80,30,0));
            alerta.setBorder(new EmptyBorder(10,20,10,20));
            String msgAlert = tienePendiente
                ? "⚠  Tienes pagos PENDIENTES. Comunícate con la recepción para regularizar tu membresía."
                : "⚠  Tu membresía ha VENCIDO. Renuévala en recepción para recuperar el acceso completo.";
            JLabel la = new JLabel(msgAlert);
            la.setFont(new Font("Segoe UI",Font.BOLD,12)); la.setForeground(new Color(255,220,60));
            alerta.add(la, BorderLayout.WEST); norte.add(alerta, BorderLayout.SOUTH);
        }
        root.add(norte, BorderLayout.NORTH);

        if (pagos.isEmpty()) {
            JLabel vacio = new JLabel("No tienes pagos registrados.", JLabel.CENTER);
            vacio.setFont(new Font("Segoe UI",Font.ITALIC,14)); vacio.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
            root.add(vacio, BorderLayout.CENTER); return root;
        }

        String[] cols = {"Fecha Pago","Membresía","Monto","Método","Vigencia Inicio","Vigencia Fin","Estado"};
        Object[][] data = new Object[pagos.size()][7];
        for (int i = 0; i < pagos.size(); i++) {
            Pago p = pagos.get(i);
            data[i] = new Object[]{
                p.getFechaPago(), p.getNombreMembresia(),
                "$"+String.format("%,.0f",p.getMonto()), p.getMetodoPago(),
                p.getFechaInicio() != null ? p.getFechaInicio() : "—",
                p.getFechaFin()    != null ? p.getFechaFin()    : "—",
                p.getEstado()
            };
        }
        JTable tabla = new JTable(data, cols) { @Override public boolean isCellEditable(int r, int c){return false;} };
        tabla.setBackground(EstilosGym.COLOR_PANEL); tabla.setForeground(EstilosGym.COLOR_TEXTO);
        tabla.setGridColor(EstilosGym.COLOR_BORDE); tabla.setRowHeight(44);
        tabla.setFont(EstilosGym.FUENTE_NORMAL); tabla.setShowHorizontalLines(true); tabla.setShowVerticalLines(false);
        tabla.setSelectionBackground(new Color(255,87,34,55));
        tabla.getTableHeader().setBackground(new Color(16,16,24));
        tabla.getTableHeader().setForeground(EstilosGym.COLOR_TEXTO_GRIS);
        tabla.getTableHeader().setFont(new Font("Segoe UI",Font.BOLD,11));
        tabla.getTableHeader().setBorder(BorderFactory.createMatteBorder(0,0,1,0,EstilosGym.COLOR_BORDE));

        DefaultTableCellRenderer rend = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t,Object v,boolean s,boolean f,int r,int c) {
                super.getTableCellRendererComponent(t,v,s,f,r,c);
                setBackground(s ? new Color(255,87,34,55) : EstilosGym.COLOR_PANEL);
                setForeground(EstilosGym.COLOR_TEXTO);
                setBorder(new EmptyBorder(0,8,0,8));
                if (c == 6 && v != null) {
                    if ("Pagado".equalsIgnoreCase(v.toString())) { setForeground(EstilosGym.COLOR_EXITO); setFont(new Font("Segoe UI",Font.BOLD,12)); }
                    else if ("Pendiente".equalsIgnoreCase(v.toString())) { setForeground(new Color(255,200,0)); setFont(new Font("Segoe UI",Font.BOLD,12)); }
                    else { setForeground(EstilosGym.COLOR_PELIGRO); setFont(new Font("Segoe UI",Font.BOLD,12)); }
                }
                return this;
            }
        };
        for (int i = 0; i < 7; i++) tabla.getColumnModel().getColumn(i).setCellRenderer(rend);

        JScrollPane sc = new JScrollPane(tabla); sc.setBorder(BorderFactory.createEmptyBorder());
        sc.getViewport().setBackground(EstilosGym.COLOR_PANEL);

        long countPag  = pagos.stream().filter(p -> "Pagado".equalsIgnoreCase(p.getEstado())).count();
        long countPend = pagos.stream().filter(p -> "Pendiente".equalsIgnoreCase(p.getEstado())).count();
        double totalPag = pagos.stream().filter(p -> "Pagado".equalsIgnoreCase(p.getEstado())).mapToDouble(Pago::getMonto).sum();
        JPanel foot = new JPanel(new FlowLayout(FlowLayout.LEFT,20,7));
        foot.setBackground(EstilosGym.COLOR_PANEL);
        foot.setBorder(BorderFactory.createMatteBorder(1,0,0,0,EstilosGym.COLOR_BORDE));
        foot.add(stat("Pagados", String.valueOf(countPag)));
        foot.add(stat("Pendientes", String.valueOf(countPend)));
        foot.add(stat("Total pagado", "$"+String.format("%,.0f", totalPag)));

        JPanel inner = new JPanel(new BorderLayout()); inner.setBackground(EstilosGym.COLOR_FONDO);
        inner.setBorder(new EmptyBorder(0,25,0,25));
        inner.add(sc, BorderLayout.CENTER); inner.add(foot, BorderLayout.SOUTH);
        root.add(inner, BorderLayout.CENTER); return root;
    }

    // ─────────────── MI PROGRESO ───────────────
    private JPanel vistaMiProgreso() {
        JPanel root = panel();

        int idCliente = Sesion.getIdCliente();
        if (idCliente <= 0) {
            root.add(header("📈  Mi Progreso Físico"), BorderLayout.NORTH);
            JLabel err = new JLabel("No se pudo identificar tu cuenta. Contacta al administrador.", JLabel.CENTER);
            err.setFont(new Font("Segoe UI",Font.ITALIC,13)); err.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
            root.add(err, BorderLayout.CENTER); return root;
        }

        ProgresoDAO pDao = new ProgresoDAO();
        List<ProgresoFisico> lista;
        try { lista = pDao.listarPorCliente(idCliente); } catch (Exception e) { lista = new ArrayList<>(); }

        // Header con botón registrar
        JPanel norte = new JPanel(new BorderLayout()); norte.setOpaque(false);
        norte.add(header("📈  Mi Progreso Físico"), BorderLayout.WEST);
        JButton btnReg = EstilosGym.crearBotonPrimario("+ Registrar medición");
        btnReg.setPreferredSize(new Dimension(180, 38));
        JPanel btnPan = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 18)); btnPan.setOpaque(false);
        btnPan.add(btnReg); norte.add(btnPan, BorderLayout.EAST);
        root.add(norte, BorderLayout.NORTH);

        ProgresoFisico[] ultimoRef = {lista.isEmpty() ? null : lista.get(0)};

        // Panel de contenido (actualizable)
        JPanel[] contenidoRef = {null};
        Runnable recargar = () -> {
            List<ProgresoFisico> actual;
            try { actual = pDao.listarPorCliente(idCliente); } catch (Exception e) { actual = new ArrayList<>(); }
            ultimoRef[0] = actual.isEmpty() ? null : actual.get(0);

            JPanel cards = new JPanel(new GridLayout(0, 2, 16, 16));
            cards.setBackground(EstilosGym.COLOR_FONDO);
            cards.setBorder(new EmptyBorder(8,25,25,25));

            if (actual.isEmpty()) {
                JLabel vacio = new JLabel("No hay registros de progreso aún. ¡Registra tu primera medición!", JLabel.CENTER);
                vacio.setFont(new Font("Segoe UI",Font.ITALIC,14)); vacio.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
                if (contenidoRef[0] != null) root.remove(contenidoRef[0]);
                contenidoRef[0] = wrapLabel(vacio);
                root.add(contenidoRef[0], BorderLayout.CENTER);
            } else {
                for (int i = 0; i < actual.size(); i++) {
                    ProgresoFisico curr = actual.get(i);
                    ProgresoFisico prev = (i + 1 < actual.size()) ? actual.get(i + 1) : null;
                    cards.add(cardProgreso2(curr, prev));
                }
                JScrollPane sc = scroll(cards);
                if (contenidoRef[0] != null) root.remove(contenidoRef[0]);
                contenidoRef[0] = new JPanel(new BorderLayout()); contenidoRef[0].setOpaque(false);
                contenidoRef[0].add(sc, BorderLayout.CENTER);
                root.add(contenidoRef[0], BorderLayout.CENTER);
            }
            root.revalidate(); root.repaint();
        };

        // Contenido inicial
        if (lista.isEmpty()) {
            JLabel vacio = new JLabel("No hay registros de progreso aún. ¡Registra tu primera medición!", JLabel.CENTER);
            vacio.setFont(new Font("Segoe UI",Font.ITALIC,14)); vacio.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
            contenidoRef[0] = wrapLabel(vacio);
            root.add(contenidoRef[0], BorderLayout.CENTER);
        } else {
            JPanel cards = new JPanel(new GridLayout(0,2,16,16));
            cards.setBackground(EstilosGym.COLOR_FONDO); cards.setBorder(new EmptyBorder(8,25,25,25));
            for (int i = 0; i < lista.size(); i++) {
                cards.add(cardProgreso2(lista.get(i), i+1 < lista.size() ? lista.get(i+1) : null));
            }
            contenidoRef[0] = new JPanel(new BorderLayout()); contenidoRef[0].setOpaque(false);
            contenidoRef[0].add(scroll(cards), BorderLayout.CENTER);
            root.add(contenidoRef[0], BorderLayout.CENTER);
        }

        btnReg.addActionListener(e -> abrirFormProgreso(idCliente, ultimoRef[0], pDao, recargar));
        return root;
    }

    private JPanel wrapLabel(JLabel lbl) {
        JPanel p = new JPanel(new BorderLayout()); p.setOpaque(false); p.add(lbl, BorderLayout.CENTER); return p;
    }

    /** Tarjeta de progreso con comparación vs medición anterior */
    private JPanel cardProgreso2(ProgresoFisico curr, ProgresoFisico prev) {
        Color acento = new Color(80, 200, 120);
        JPanel c = cardBase(acento); c.setPreferredSize(new Dimension(0, 230));

        txt(c,"📅  "+curr.getFechaRegistro(),"Segoe UI",Font.BOLD,13,EstilosGym.COLOR_TEXTO,14,14,280,18);
        Color imcColor = curr.getImc()<18.5?new Color(100,160,255):curr.getImc()<25?EstilosGym.COLOR_EXITO:
                         curr.getImc()<30?new Color(255,200,0):EstilosGym.COLOR_PELIGRO;
        String imcLabel = curr.getImc()<18.5?"Bajo peso":curr.getImc()<25?"Normal":
                          curr.getImc()<30?"Sobrepeso":"Obesidad";
        txt(c,"IMC: "+String.format("%.1f",curr.getImc())+" ("+imcLabel+")","Segoe UI",Font.BOLD,11,imcColor,14,36,280,16);

        // Métricas con delta
        int y = 60;
        y = metricaDelta(c, "⚖  Peso",    curr.getPeso(),    prev!=null?prev.getPeso()    :Double.NaN, "kg", y);
        y = metricaDelta(c, "💪 Masa musc.",curr.getMasaMuscular(),prev!=null?prev.getMasaMuscular():Double.NaN,"kg",y);
        y = metricaDelta(c, "🔥 % Grasa",  curr.getPorcentajeGrasa(),prev!=null?prev.getPorcentajeGrasa():Double.NaN,"%",y);

        if (prev == null) {
            txt(c,"(primera medición)","Segoe UI",Font.ITALIC,10,EstilosGym.COLOR_TEXTO_GRIS,14,y,260,16); y+=16;
        }
        if (curr.getObservaciones()!=null&&!curr.getObservaciones().isEmpty()) {
            String obs = curr.getObservaciones().length()>55?curr.getObservaciones().substring(0,52)+"...":curr.getObservaciones();
            txt(c,"📝 "+obs,"Segoe UI",Font.ITALIC,10,new Color(120,120,140),14,y+4,310,16);
        }
        return c;
    }

    /** Dibuja una métrica con flecha de cambio vs anterior */
    private int metricaDelta(JPanel c, String etiq, double curr, double prev, String unit, int y) {
        String valorStr = String.format("%.1f", curr) + " " + unit;
        txt(c, etiq+": "+valorStr, "Segoe UI", Font.PLAIN, 12, EstilosGym.COLOR_TEXTO, 14, y, 200, 18);
        if (!Double.isNaN(prev)) {
            double delta = curr - prev;
            String signo = delta > 0 ? "▲" : delta < 0 ? "▼" : "—";
            Color col = delta > 0 ? EstilosGym.COLOR_PELIGRO : delta < 0 ? EstilosGym.COLOR_EXITO : EstilosGym.COLOR_TEXTO_GRIS;
            // Para grasa, sube=malo; ya se refleja con los colores
            txt(c, signo+" "+String.format("%.1f",Math.abs(delta)), "Segoe UI", Font.BOLD, 10, col, 220, y+1, 80, 16);
        }
        return y + 22;
    }

    /** Formulario para registrar una nueva medición de progreso */
    private void abrirFormProgreso(int idCliente, ProgresoFisico ultimo, ProgresoDAO pDao, Runnable recargar) {
        JDialog dlg = new JDialog((JFrame)SwingUtilities.getWindowAncestor(contenido),
            "📈 Registrar Medición", true);
        dlg.setSize(480, 500); dlg.setLocationRelativeTo(contenido);

        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(EstilosGym.COLOR_PANEL); p.setBorder(new EmptyBorder(22,24,22,24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6); gbc.fill = GridBagConstraints.HORIZONTAL;

        // Si hay una medición anterior, mostrar datos de referencia
        if (ultimo != null) {
            JLabel ref = new JLabel("<html><b>Última medición (" + ultimo.getFechaRegistro() + "):</b>  "
                + "Peso: "+ultimo.getPeso()+"kg  |  Grasa: "+ultimo.getPorcentajeGrasa()
                + "%  |  Masa: "+ultimo.getMasaMuscular()+"kg</html>");
            ref.setFont(new Font("Segoe UI",Font.PLAIN,11)); ref.setForeground(new Color(120,200,255));
            ref.setBackground(new Color(20,40,60)); ref.setOpaque(true);
            ref.setBorder(new EmptyBorder(8,10,8,10));
            gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=2; gbc.weightx=1;
            p.add(ref, gbc); gbc.gridwidth=1;
        }

        DatePickerField dpFecha = new DatePickerField(java.time.LocalDate.now().toString());
        JTextField tfPeso    = EstilosGym.crearCampoTexto();
        JTextField tfGrasa   = EstilosGym.crearCampoTexto();
        JTextField tfMasa    = EstilosGym.crearCampoTexto();
        JTextField tfObs     = EstilosGym.crearCampoTexto();

        // Pre-fill con datos anteriores como ayuda
        if (ultimo != null) {
            tfPeso.setText(String.valueOf(ultimo.getPeso()));
            tfGrasa.setText(String.valueOf(ultimo.getPorcentajeGrasa()));
            tfMasa.setText(String.valueOf(ultimo.getMasaMuscular()));
        }

        // Auto-cálculo IMC (necesita altura del cliente)
        double[] alturaRef = {0};
        try (var ps = Conexion.getConexion().prepareStatement("SELECT altura FROM clientes WHERE id_cliente=?")) {
            ps.setInt(1, idCliente); var rs = ps.executeQuery();
            if (rs.next()) alturaRef[0] = rs.getDouble(1); rs.close();
        } catch (Exception ignored) {}

        JLabel lblIMC = new JLabel("IMC: (completa peso)");
        lblIMC.setFont(new Font("Segoe UI",Font.ITALIC,11)); lblIMC.setForeground(EstilosGym.COLOR_TEXTO_GRIS);

        Runnable calcIMC = () -> {
            try {
                double peso = Double.parseDouble(tfPeso.getText().trim());
                double alt  = alturaRef[0] > 0 ? alturaRef[0] : 1.70;
                double imc  = peso / (alt * alt);
                lblIMC.setText(String.format("IMC calculado: %.2f", imc));
                lblIMC.setForeground(imc < 25 ? EstilosGym.COLOR_EXITO : new Color(255,200,0));
            } catch (Exception e2) { lblIMC.setText("IMC: (completa peso)"); }
        };
        tfPeso.addFocusListener(new FocusAdapter() { @Override public void focusLost(FocusEvent e) { calcIMC.run(); } });

        int startRow = (ultimo != null) ? 1 : 0;
        Object[][] rows = {
            {"Fecha *",          dpFecha},
            {"Peso (kg) *",      tfPeso},
            {"% Grasa corporal", tfGrasa},
            {"Masa muscular (kg)",tfMasa},
            {"",                 lblIMC},
            {"Observaciones",    tfObs}
        };

        for (int i = 0; i < rows.length; i++) {
            gbc.gridy = startRow + i; gbc.gridx = 0; gbc.weightx = 0.38;
            JLabel l = EstilosGym.crearEtiqueta(rows[i][0].toString());
            l.setHorizontalAlignment(SwingConstants.RIGHT); p.add(l, gbc);
            gbc.gridx = 1; gbc.weightx = 0.62; p.add((Component)rows[i][1], gbc);
        }

        JLabel lblErr = new JLabel(""); lblErr.setForeground(EstilosGym.COLOR_PELIGRO);
        gbc.gridy = startRow + rows.length; gbc.gridx = 0; gbc.gridwidth = 2; p.add(lblErr, gbc);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,0)); btns.setOpaque(false);
        JButton bc = EstilosGym.crearBotonSecundario("Cancelar");
        JButton bg = EstilosGym.crearBotonPrimario("✓ Guardar");
        bc.addActionListener(e -> dlg.dispose());
        bg.addActionListener(e -> {
            if (dpFecha.getFecha().isEmpty()) { lblErr.setText("⚠ Fecha obligatoria."); return; }
            double peso;
            try { peso = Double.parseDouble(tfPeso.getText().trim()); }
            catch (NumberFormatException ex) { lblErr.setText("⚠ Peso inválido."); return; }
            double grasa = 0, masa = 0;
            try { grasa = Double.parseDouble(tfGrasa.getText().trim()); } catch (Exception ignored) {}
            try { masa  = Double.parseDouble(tfMasa.getText().trim());  } catch (Exception ignored) {}
            double alt = alturaRef[0] > 0 ? alturaRef[0] : 1.70;
            double imc = peso / (alt * alt);
            ProgresoFisico pf = new ProgresoFisico(idCliente, dpFecha.getFecha(), peso, grasa, masa,
                Math.round(imc*100.0)/100.0, tfObs.getText().trim());
            if (pDao.insertar(pf) < 0) { lblErr.setText("⚠ Error al guardar."); return; }
            dlg.dispose();
            recargar.run();
        });
        btns.add(bc); btns.add(bg);
        gbc.gridy++; p.add(btns, gbc);

        JScrollPane sc = new JScrollPane(p);
        sc.setBorder(BorderFactory.createEmptyBorder());
        sc.getViewport().setBackground(EstilosGym.COLOR_PANEL);
        dlg.setContentPane(sc); dlg.setVisible(true);
    }

    // ─────────────── CLIENTES ASIGNADOS (Entrenador) ───────────────
    private JPanel vistaClientesAsignados() {
        JPanel root=panel(); root.add(header("👥  Clientes Asignados"),BorderLayout.NORTH);
        String[] cols={"ID","Nombre","Membresía","Teléfono","Correo","Objetivo"};
        DefaultTableModel mod=new DefaultTableModel(cols,0){ @Override public boolean isCellEditable(int r,int c){return false;} };
        try {
            for (Cliente cli:new ClienteDAO().listarTodos())
                mod.addRow(new Object[]{cli.getIdCliente(),cli.getNombres()+" "+cli.getApellidos(),
                    cli.getEstadoMembresia(),cli.getTelefono(),cli.getCorreo(),cli.getObjetivo()});
        } catch (Exception ignored) {}
        root.add(scroll(tablaSimple(mod)),BorderLayout.CENTER); return root;
    }

    // ─────────────── PROGRESO CLIENTES (Entrenador) ───────────────
    private JPanel vistaProgresoClientes() {
        JPanel root=panel(); root.add(header("📊  Progreso de Clientes"),BorderLayout.NORTH);
        String[] cols={"Fecha","Cliente","Peso","% Grasa","Masa Musc.","IMC","Observaciones"};
        DefaultTableModel mod=new DefaultTableModel(cols,0){ @Override public boolean isCellEditable(int r,int c){return false;} };
        try {
            for (ProgresoFisico p:new ProgresoDAO().listarTodos())
                mod.addRow(new Object[]{p.getFechaRegistro(),p.getNombreCliente(),p.getPeso(),
                    p.getPorcentajeGrasa(),p.getMasaMuscular(),p.getImc(),p.getObservaciones()});
        } catch (Exception ignored) {}
        root.add(scroll(tablaSimple(mod)),BorderLayout.CENTER); return root;
    }

    // ──────── Helpers ────────
    private JPanel panel(){JPanel p=new JPanel(new BorderLayout()); p.setBackground(EstilosGym.COLOR_FONDO); return p;}
    private JPanel header(String titulo){
        JPanel h=new JPanel(new BorderLayout()); h.setBackground(EstilosGym.COLOR_FONDO);
        h.setBorder(new EmptyBorder(20,25,14,25));
        JLabel l=new JLabel(titulo); l.setFont(EstilosGym.FUENTE_TITULO); l.setForeground(EstilosGym.COLOR_TEXTO);
        h.add(l,BorderLayout.WEST); return h;
    }
    private JScrollPane scroll(JComponent c){
        JScrollPane s=new JScrollPane(c); s.setBorder(BorderFactory.createEmptyBorder());
        s.getViewport().setBackground(EstilosGym.COLOR_FONDO); return s;
    }
    private JPanel cardBase(Color acento){
        JPanel card = new JPanel(null){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(EstilosGym.COLOR_PANEL_CLARO); g2.fillRoundRect(0,0,getWidth(),getHeight(),12,12);
                g2.setColor(acento); g2.fillRoundRect(0,0,getWidth(),5,4,4); g2.dispose();
            }
        };
        card.setOpaque(false);
        return card;
    }
    private void txt(JPanel c,String t,String font,int style,int size,Color col,int x,int y,int w,int h){
        JLabel l=new JLabel(t); l.setFont(new Font(font,style,size)); l.setForeground(col); l.setBounds(x,y,w,h); c.add(l);
    }
    private JLabel stat(String e,String v){
        JLabel l=new JLabel("<html><font color='#9696a5'>"+e+": </font><font color='#ff5722'><b>"+v+"</b></font></html>");
        l.setFont(EstilosGym.FUENTE_NORMAL); return l;
    }
    private JTable tablaSimple(DefaultTableModel mod){
        JTable t=new JTable(mod); t.setBackground(EstilosGym.COLOR_PANEL); t.setForeground(EstilosGym.COLOR_TEXTO);
        t.setGridColor(EstilosGym.COLOR_BORDE); t.setRowHeight(44); t.setFont(EstilosGym.FUENTE_NORMAL);
        t.setShowHorizontalLines(true); t.setShowVerticalLines(false);
        t.setSelectionBackground(new Color(255,87,34,55));
        t.getTableHeader().setBackground(new Color(16,16,24));
        t.getTableHeader().setForeground(EstilosGym.COLOR_TEXTO_GRIS);
        t.getTableHeader().setFont(new Font("Segoe UI",Font.BOLD,11)); return t;
    }
}
