package Gimnasio.Vistas;

import Gimnasio.Controlador.*;
import Gimnasio.Controlador.Sesion;
import javax.swing.*;
import java.awt.*;

public class DashboardVista extends JPanel {

    public DashboardVista() {
        setLayout(new BorderLayout());
        setBackground(EstilosGym.COLOR_FONDO);
        build();
    }

    private void build() {
        JPanel wrap = new JPanel(); wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setBackground(EstilosGym.COLOR_FONDO);
        wrap.setBorder(BorderFactory.createEmptyBorder(28,28,28,28));

        // Saludo
        JLabel saludo = new JLabel("Buenos días, " + Sesion.getNombreCompleto() + " 👋");
        saludo.setFont(new Font("Segoe UI",Font.BOLD,26)); saludo.setForeground(EstilosGym.COLOR_TEXTO);
        saludo.setAlignmentX(LEFT_ALIGNMENT);
        JLabel sub = new JLabel(Sesion.esAdmin()
                ? "Aquí tienes el resumen del gimnasio en tiempo real."
                : Sesion.esEntrenador()
                    ? "Bienvenido a tu panel de entrenamiento."
                    : "Bienvenido a tu panel personal.");
        sub.setFont(EstilosGym.FUENTE_NORMAL); sub.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
        sub.setAlignmentX(LEFT_ALIGNMENT);
        wrap.add(saludo); wrap.add(Box.createVerticalStrut(6)); wrap.add(sub);
        wrap.add(Box.createVerticalStrut(26));

        JSeparator sep = new JSeparator(); sep.setForeground(EstilosGym.COLOR_BORDE);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE,1)); sep.setAlignmentX(LEFT_ALIGNMENT);
        wrap.add(sep); wrap.add(Box.createVerticalStrut(24));

        // ── ADMIN / RECEPCIONISTA ──
        if (Sesion.esPersonalAdmin()) {
            ClienteDAO   cli = new ClienteDAO();
            MaquinaDAO   maq = new MaquinaDAO();
            RutinaDAO    rut = new RutinaDAO();
            PagoDAO      pag = new PagoDAO();
            AsistenciaDAO asi = new AsistenciaDAO();

            int totCli  = cli.contarTotal();
            int actCli  = cli.contarActivos();
            int totMaq  = maq.contarTotal();
            int disMaq  = maq.contarDisponibles();
            int totRut  = rut.contarTotal();
            int totPag  = pag.contarTotal();
            double ingr = pag.totalIngresos();
            int hoy     = asi.contarHoy();

            addSectionTitle(wrap, "📊  KPIs Principales");
            wrap.add(Box.createVerticalStrut(14));

            JPanel fila1 = row(0);
            fila1.add(kpi("👥 Clientes",     fmt(totCli),           "registrados",        new Color(100,60,200)));
            fila1.add(kpi("✅ Activos",      fmt(actCli),           "membresía activa",   EstilosGym.COLOR_EXITO));
            fila1.add(kpi("📅 Hoy",          fmt(hoy),              "asistencias hoy",    new Color(0,180,200)));
            fila1.add(kpi("💳 Ingresos",     "$"+fmtMon(ingr),      "total facturado",    new Color(255,160,0)));
            wrap.add(fila1); wrap.add(Box.createVerticalStrut(16));

            JPanel fila2 = row(0);
            fila2.add(kpi("🏋️ Máquinas",    fmt(disMaq)+"/"+fmt(totMaq), "disponibles",  EstilosGym.COLOR_ACENTO));
            fila2.add(kpi("📋 Rutinas",      fmt(totRut),           "programas activos",  new Color(80,200,120)));
            fila2.add(kpi("💳 Pagos",        fmt(totPag),           "transacciones",      new Color(200,80,200)));
            fila2.add(kpi("📊 Membresías",   "3",                   "planes disponibles", new Color(100,160,255)));
            wrap.add(fila2); wrap.add(Box.createVerticalStrut(30));

            addSectionTitle(wrap, "⚡  Módulos del Sistema");
            wrap.add(Box.createVerticalStrut(14));

            JPanel mods = row(16);
            mods.add(modulo("👥","Clientes",    "Registro y gestión CRUD completo",    new Color(100,60,200)));
            mods.add(modulo("💳","Pagos",       "Facturación y control de membresías",  new Color(255,160,0)));
            mods.add(modulo("📅","Asistencias", "Control de entrada y salida diaria",   new Color(0,180,200)));
            mods.add(modulo("🏋️","Máquinas",   "Inventario y estado del equipo",       EstilosGym.COLOR_ACENTO));
            wrap.add(mods); wrap.add(Box.createVerticalStrut(12));

            JPanel mods2 = row(16);
            mods2.add(modulo("📋","Rutinas",    "Programas de entrenamiento",           new Color(80,200,120)));
            mods2.add(modulo("🎫","Membresías", "Planes y precios del gimnasio",        new Color(200,80,200)));
            mods2.add(panelVacio());
            mods2.add(panelVacio());
            wrap.add(mods2);

        // ── CLIENTE ──
        } else if (Sesion.esCliente()) {
            int idCliente = Sesion.getIdCliente();

            long pendientes  = 0;
            double totalPagado = 0;
            if (idCliente > 0) {
                java.util.List<Gimnasio.Modelo.Pago> misPagos = new PagoDAO().listarPorCliente(idCliente);
                pendientes   = misPagos.stream().filter(p -> "Pendiente".equalsIgnoreCase(p.getEstado())).count();
                totalPagado  = misPagos.stream().filter(p -> "Pagado".equalsIgnoreCase(p.getEstado())).mapToDouble(Gimnasio.Modelo.Pago::getMonto).sum();
            }
            int registrosProgreso = idCliente > 0 ? new ProgresoDAO().listarPorCliente(idCliente).size() : 0;

            addSectionTitle(wrap, "📊  Mi Resumen");
            wrap.add(Box.createVerticalStrut(14));

            JPanel fila1 = row(0);
            fila1.add(kpi("📋 Mis Rutinas",       "—",                    "ver en el menú",             new Color(80,200,120)));
            fila1.add(kpi("💳 Pagos Pendientes",  fmt((int)pendientes),   pendientes>0?"¡Contacta recepción!":"Al día ✓",
                          pendientes > 0 ? new Color(255,160,0) : EstilosGym.COLOR_EXITO));
            fila1.add(kpi("📈 Mi Progreso",       fmt(registrosProgreso), "registros físicos",          new Color(100,160,255)));
            fila1.add(kpi("💰 Total Pagado",      "$"+fmtMon(totalPagado),"en membresías",              new Color(100,60,200)));
            wrap.add(fila1); wrap.add(Box.createVerticalStrut(30));

            addSectionTitle(wrap, "⚡  Mis Módulos");
            wrap.add(Box.createVerticalStrut(14));

            JPanel mods = row(16);
            if (Sesion.tienePermiso("Mis Rutinas"))
                mods.add(modulo("📋","Mis Rutinas", "Tu plan de entrenamiento semanal", new Color(80,200,120)));
            else
                mods.add(panelVacio());

            if (Sesion.tienePermiso("Máquinas"))
                mods.add(modulo("🏋️","Máquinas",   "Estado del equipo disponible",      EstilosGym.COLOR_ACENTO));
            else
                mods.add(panelVacio());

            if (Sesion.tienePermiso("Mis Pagos"))
                mods.add(modulo("💳","Mis Pagos",   "Historial y estado de tus pagos",   new Color(255,160,0)));
            else
                mods.add(panelVacio());

            if (Sesion.tienePermiso("Mi Progreso"))
                mods.add(modulo("📈","Mi Progreso", "Registro de tu evolución física",   new Color(100,160,255)));
            else
                mods.add(panelVacio());

            wrap.add(mods);

        // ── ENTRENADOR ──
        } else if (Sesion.esEntrenador()) {
            int totRut = new RutinaDAO().contarTotal();

            addSectionTitle(wrap, "📊  Mi Resumen");
            wrap.add(Box.createVerticalStrut(14));

            JPanel fila1 = row(0);
            fila1.add(kpi("📋 Rutinas",  fmt(totRut), "programas creados",          new Color(80,200,120)));
            fila1.add(kpi("👥 Clientes", "—",         "ver en Clientes Asignados",  new Color(100,60,200)));
            fila1.add(panelVacio());
            fila1.add(panelVacio());
            wrap.add(fila1); wrap.add(Box.createVerticalStrut(30));

            addSectionTitle(wrap, "⚡  Mis Módulos");
            wrap.add(Box.createVerticalStrut(14));

            JPanel mods = row(16);
            mods.add(modulo("📋","Mis Rutinas",        "Programas de entrenamiento",       new Color(80,200,120)));
            mods.add(modulo("🏋️","Máquinas",           "Estado del equipo disponible",      EstilosGym.COLOR_ACENTO));
            mods.add(modulo("👥","Clientes Asignados",  "Tus clientes y su seguimiento",    new Color(100,60,200)));
            mods.add(modulo("📊","Progreso Clientes",   "Evolución física de tus clientes", new Color(100,160,255)));
            wrap.add(mods);
        }

        JScrollPane scroll = new JScrollPane(wrap);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(EstilosGym.COLOR_FONDO);
        add(scroll, BorderLayout.CENTER);
    }

    /** Panel transparente vacío para rellenar el grid sin doble-llave */
    private JPanel panelVacio() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        return p;
    }

    private void addSectionTitle(JPanel p, String t) {
        JLabel l = new JLabel(t); l.setFont(EstilosGym.FUENTE_SUBTITULO);
        l.setForeground(EstilosGym.COLOR_TEXTO); l.setAlignmentX(LEFT_ALIGNMENT); p.add(l);
    }

    private JPanel row(int gap) {
        JPanel p = new JPanel(new GridLayout(1,4,gap,0)); p.setOpaque(false);
        p.setAlignmentX(LEFT_ALIGNMENT); p.setMaximumSize(new Dimension(Integer.MAX_VALUE,110)); return p;
    }

    private JPanel kpi(String titulo, String valor, String sub, Color color) {
        JPanel c = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(EstilosGym.COLOR_PANEL_CLARO); g2.fillRoundRect(0,0,getWidth(),getHeight(),12,12);
                g2.setColor(color); g2.fillRoundRect(0,0,5,getHeight(),4,4); g2.dispose();
            }
        };
        c.setOpaque(false);
        JLabel lt=new JLabel(titulo); lt.setFont(new Font("Segoe UI",Font.PLAIN,11));
        lt.setForeground(EstilosGym.COLOR_TEXTO_GRIS); lt.setBounds(14,12,200,16); c.add(lt);
        JLabel lv=new JLabel(valor); lv.setFont(new Font("Segoe UI",Font.BOLD,26));
        lv.setForeground(color); lv.setBounds(14,30,200,36); c.add(lv);
        JLabel ls=new JLabel(sub); ls.setFont(EstilosGym.FUENTE_PEQUEÑA);
        ls.setForeground(EstilosGym.COLOR_TEXTO_GRIS); ls.setBounds(14,68,200,16); c.add(ls);
        return c;
    }

    private JPanel modulo(String emoji, String titulo, String desc, Color color) {
        JPanel c = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(EstilosGym.COLOR_PANEL_CLARO); g2.fillRoundRect(0,0,getWidth(),getHeight(),12,12);
                g2.setColor(color); g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1,1,getWidth()-2,getHeight()-2,12,12); g2.dispose();
            }
        };
        c.setOpaque(false);
        JLabel le=new JLabel(emoji); le.setFont(new Font("Segoe UI Emoji",Font.PLAIN,22));
        le.setBounds(12,12,34,34); c.add(le);
        JLabel lt=new JLabel(titulo); lt.setFont(new Font("Segoe UI",Font.BOLD,13));
        lt.setForeground(EstilosGym.COLOR_TEXTO); lt.setBounds(50,12,200,20); c.add(lt);
        JLabel ld=new JLabel(desc); ld.setFont(EstilosGym.FUENTE_PEQUEÑA);
        ld.setForeground(EstilosGym.COLOR_TEXTO_GRIS); ld.setBounds(50,34,240,16); c.add(ld);
        return c;
    }

    private String fmt(int n) { return String.valueOf(n); }
    private String fmtMon(double v) { return String.format("%,.0f", v); }
}
