package Gimnasio.Vistas;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

/**
 * Panel reutilizable con tabla estilizada y barra de acciones.
 * Las subclases solo definen columnas, datos y acciones.
 */
public abstract class TablaBase extends JPanel {

    protected JTable           tabla;
    protected DefaultTableModel modelo;
    protected JTextField        txtBuscar;

    public TablaBase() {
        setLayout(new BorderLayout());
        setBackground(EstilosGym.COLOR_FONDO);
    }

    protected JPanel crearHeader(String titulo, boolean conBuscar, String labelBoton) {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(EstilosGym.COLOR_FONDO);
        h.setBorder(BorderFactory.createEmptyBorder(20,25,14,25));
        JLabel lbl = new JLabel(titulo);
        lbl.setFont(EstilosGym.FUENTE_TITULO); lbl.setForeground(EstilosGym.COLOR_TEXTO);
        JPanel acc = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0)); acc.setOpaque(false);
        if (conBuscar) {
            txtBuscar = EstilosGym.crearCampoTexto(); txtBuscar.setPreferredSize(new Dimension(200,36));
            JButton btnB = EstilosGym.crearBotonSecundario("Buscar");
            java.net.URL buscarUrl = getClass().getResource("/Gimnasio/Iconos/buscar.png");
            if (buscarUrl != null) {
                ImageIcon buscarIco = new ImageIcon(new ImageIcon(buscarUrl).getImage().getScaledInstance(16,16,java.awt.Image.SCALE_SMOOTH));
                btnB.setIcon(buscarIco);
                btnB.setIconTextGap(6);
            }
            btnB.addActionListener(e -> onBuscar(txtBuscar.getText().trim()));
            txtBuscar.addActionListener(e -> onBuscar(txtBuscar.getText().trim()));
            acc.add(txtBuscar); acc.add(btnB);
        }
        if (labelBoton != null) {
            JButton btnN = EstilosGym.crearBotonPrimario(labelBoton);
            btnN.addActionListener(e -> onNuevo());
            acc.add(btnN);
        }
        h.add(lbl, BorderLayout.WEST); h.add(acc, BorderLayout.EAST);
        return h;
    }

    protected DefaultTableModel crearModelo(String[] cols, int colAcciones) {
        modelo = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == colAcciones; }
            @Override public Class<?> getColumnClass(int c) { return c==0 ? Integer.class : String.class; }
        };
        tabla = new JTable(modelo);
        estilizarTabla();
        return modelo;
    }

    private void estilizarTabla() {
        tabla.setBackground(EstilosGym.COLOR_PANEL);
        tabla.setForeground(EstilosGym.COLOR_TEXTO);
        tabla.setSelectionBackground(new Color(255,87,34,55));
        tabla.setSelectionForeground(EstilosGym.COLOR_TEXTO);
        tabla.setGridColor(EstilosGym.COLOR_BORDE);
        tabla.setRowHeight(46);
        tabla.setFont(EstilosGym.FUENTE_NORMAL);
        tabla.setShowHorizontalLines(true); tabla.setShowVerticalLines(false);
        JTableHeader th = tabla.getTableHeader();
        th.setBackground(new Color(16,16,24)); th.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
        th.setFont(new Font("Segoe UI",Font.BOLD,11));
        th.setBorder(BorderFactory.createMatteBorder(0,0,1,0,EstilosGym.COLOR_BORDE));
        th.setReorderingAllowed(false);
    }

    protected void aplicarRendererEstado(int colEstado, java.util.Map<String,Color> mapa) {
        DefaultTableCellRenderer r = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t,Object v,boolean s,boolean f,int row,int col) {
                super.getTableCellRendererComponent(t,v,s,f,row,col);
                setHorizontalAlignment(col==0 ? CENTER : LEFT);
                setBackground(s ? new Color(255,87,34,55) : EstilosGym.COLOR_PANEL);
                setForeground(EstilosGym.COLOR_TEXTO);
                setBorder(BorderFactory.createEmptyBorder(0,8,0,8));
                if (col == colEstado && v != null) {
                    Color c = mapa.get(v.toString());
                    if (c != null) { setForeground(c); setFont(new Font("Segoe UI",Font.BOLD,12)); }
                }
                return this;
            }
        };
        for (int i=0; i<tabla.getColumnCount()-1; i++) tabla.getColumnModel().getColumn(i).setCellRenderer(r);
    }

    protected JPanel crearFooter(String... stats) {
        JPanel f = new JPanel(new FlowLayout(FlowLayout.LEFT,18,7));
        f.setBackground(EstilosGym.COLOR_PANEL);
        f.setBorder(BorderFactory.createMatteBorder(1,0,0,0,EstilosGym.COLOR_BORDE));
        for (int i=0;i<stats.length;i+=2) {
            if (i>0) { JSeparator sep=new JSeparator(JSeparator.VERTICAL);
                sep.setPreferredSize(new Dimension(1,28)); sep.setForeground(EstilosGym.COLOR_BORDE); f.add(sep); }
            JLabel l=new JLabel("<html><font color='#9696a5'>"+stats[i]+": </font>"
                    +"<font color='#ff5722'><b>"+stats[i+1]+"</b></font></html>");
            l.setFont(EstilosGym.FUENTE_NORMAL); f.add(l);
        }
        return f;
    }

    protected JScrollPane crearScroll() {
        JScrollPane s = new JScrollPane(tabla);
        s.setBorder(BorderFactory.createEmptyBorder());
        s.getViewport().setBackground(EstilosGym.COLOR_PANEL);
        return s;
    }

    protected void onBuscar(String filtro) {}
    protected void onNuevo() {}
}
