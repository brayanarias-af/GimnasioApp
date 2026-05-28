package Gimnasio.Vistas;

import Gimnasio.Controlador.MembresiaDAO;
import Gimnasio.Modelo.Membresia;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;

public class MembresiasVista extends TablaBase {

    private final MembresiaDAO dao = new MembresiaDAO();

    public MembresiasVista() {
        super();
        construir();
        cargar();
    }

    private void construir() {
        add(crearHeader("🎫  Planes de Membresía", false, "+ Nuevo Plan"), BorderLayout.NORTH);

        String[] cols = {"ID","Nombre","Precio ($)","Duración (días)","Descripción","Acciones"};
        crearModelo(cols, 5);

        DefaultTableCellRenderer r = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t,Object v,boolean s,boolean f,int row,int col){
                super.getTableCellRendererComponent(t,v,s,f,row,col);
                setHorizontalAlignment(col==0||col==3?CENTER : col==2?RIGHT : LEFT);
                setBackground(s?new Color(255,87,34,55):EstilosGym.COLOR_PANEL);
                setBorder(BorderFactory.createEmptyBorder(0,8,0,8));
                if(col==2){ setForeground(new Color(80,220,80)); setFont(new Font("Segoe UI",Font.BOLD,13)); }
                else setForeground(EstilosGym.COLOR_TEXTO);
                return this;
            }
        };
        for(int i=0;i<5;i++) tabla.getColumnModel().getColumn(i).setCellRenderer(r);
        tabla.getColumnModel().getColumn(5).setCellRenderer(new BtnRender());
        tabla.getColumnModel().getColumn(5).setCellEditor(new BtnEdit());
        tabla.getColumnModel().getColumn(5).setPreferredWidth(172);
        tabla.getColumnModel().getColumn(0).setMaxWidth(48);
        tabla.getColumnModel().getColumn(3).setMaxWidth(110);

        add(crearScroll(), BorderLayout.CENTER);

        // Cards de planes debajo de la tabla
        JPanel cards = crearCards();
        JScrollPane scrollCards = new JScrollPane(cards);
        scrollCards.setPreferredSize(new Dimension(0, 150));
        scrollCards.setBorder(BorderFactory.createMatteBorder(1,0,0,0,EstilosGym.COLOR_BORDE));
        scrollCards.getViewport().setBackground(EstilosGym.COLOR_FONDO);
        add(scrollCards, BorderLayout.SOUTH);
    }

    @Override protected void onNuevo() { abrirForm(null); }

    private void cargar() {
        modelo.setRowCount(0);
        for (Membresia m : dao.listarTodas())
            modelo.addRow(new Object[]{m.getIdMembresia(), m.getNombre(),
                "$"+String.format("%,.0f",m.getPrecio()), m.getDuracionDias(), m.getDescripcion(), m.getIdMembresia()});
    }

    private JPanel crearCards() {
        JPanel outer = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 12));
        outer.setBackground(EstilosGym.COLOR_FONDO);
        outer.setBorder(BorderFactory.createEmptyBorder(0,18,0,18));

        Color[] paleta = {new Color(100,60,200), new Color(255,160,0), new Color(0,200,160)};
        int idx = 0;
        for (Membresia m : dao.listarTodas()) {
            Color c = paleta[idx % paleta.length]; idx++;
            JPanel card = new JPanel(null) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2=(Graphics2D)g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(EstilosGym.COLOR_PANEL_CLARO); g2.fillRoundRect(0,0,getWidth(),getHeight(),12,12);
                    g2.setColor(c); g2.fillRoundRect(0,0,getWidth(),5,4,4); g2.dispose();
                }
            };
            card.setOpaque(false); card.setPreferredSize(new Dimension(210,110));
            JLabel lN=new JLabel(m.getNombre()); lN.setFont(new Font("Segoe UI",Font.BOLD,14));
            lN.setForeground(EstilosGym.COLOR_TEXTO); lN.setBounds(14,18,190,20); card.add(lN);
            JLabel lP=new JLabel("$"+String.format("%,.0f",m.getPrecio()));
            lP.setFont(new Font("Segoe UI",Font.BOLD,22)); lP.setForeground(c);
            lP.setBounds(14,40,190,28); card.add(lP);
            JLabel lD=new JLabel(m.getDuracionDias()+" días · "+
                (m.getDescripcion()!=null&&m.getDescripcion().length()>28?m.getDescripcion().substring(0,25)+"...":m.getDescripcion()));
            lD.setFont(EstilosGym.FUENTE_PEQUEÑA); lD.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
            lD.setBounds(14,72,190,16); card.add(lD);
            outer.add(card);
        }
        return outer;
    }

    private void abrirForm(Membresia orig) {
        JDialog dlg=new JDialog((JFrame)SwingUtilities.getWindowAncestor(this),
                orig==null?"Nuevo Plan":"Editar Plan",true);
        dlg.setSize(460,330); dlg.setLocationRelativeTo(this);

        JPanel p=new JPanel(new GridBagLayout()); p.setBackground(EstilosGym.COLOR_PANEL);
        p.setBorder(BorderFactory.createEmptyBorder(20,24,20,24));
        GridBagConstraints gbc=new GridBagConstraints(); gbc.insets=new Insets(7,6,7,6); gbc.fill=GridBagConstraints.HORIZONTAL;

        JTextField tfN=EstilosGym.crearCampoTexto(), tfP=EstilosGym.crearCampoTexto(), tfDias=EstilosGym.crearCampoTexto();
        JTextArea taDesc=new JTextArea(2,20); taDesc.setBackground(EstilosGym.COLOR_FONDO);
        taDesc.setForeground(EstilosGym.COLOR_TEXTO); taDesc.setFont(EstilosGym.FUENTE_NORMAL);
        taDesc.setLineWrap(true); taDesc.setWrapStyleWord(true);
        taDesc.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(EstilosGym.COLOR_BORDE),
                BorderFactory.createEmptyBorder(5,8,5,8)));
        if(orig!=null){ tfN.setText(orig.getNombre()); tfP.setText(String.valueOf((int)orig.getPrecio()));
            tfDias.setText(String.valueOf(orig.getDuracionDias())); taDesc.setText(orig.getDescripcion()); }

        Object[][] rows={{"Nombre *",tfN},{"Precio COP *",tfP},{"Duración (días) *",tfDias}};
        for(int i=0;i<rows.length;i++){
            gbc.gridy=i; gbc.gridx=0; gbc.weightx=0.38;
            JLabel l=EstilosGym.crearEtiqueta(rows[i][0].toString()); l.setHorizontalAlignment(SwingConstants.RIGHT); p.add(l,gbc);
            gbc.gridx=1; gbc.weightx=0.62; p.add((Component)rows[i][1],gbc);
        }
        gbc.gridy=3; gbc.gridx=0; gbc.weightx=0.38;
        JLabel ld=EstilosGym.crearEtiqueta("Descripción"); ld.setHorizontalAlignment(SwingConstants.RIGHT); p.add(ld,gbc);
        gbc.gridx=1; gbc.weightx=0.62; p.add(new JScrollPane(taDesc),gbc);
        JLabel lblErr=new JLabel(""); lblErr.setForeground(EstilosGym.COLOR_PELIGRO);
        gbc.gridy=4; gbc.gridx=0; gbc.gridwidth=2; p.add(lblErr,gbc);
        JPanel btns=new JPanel(new FlowLayout(FlowLayout.RIGHT,10,0)); btns.setOpaque(false);
        JButton bc=EstilosGym.crearBotonSecundario("Cancelar"); JButton bg=EstilosGym.crearBotonPrimario("Guardar");
        bc.addActionListener(e->dlg.dispose());
        bg.addActionListener(e->{
            if(tfN.getText().trim().isEmpty()){lblErr.setText("⚠  Nombre obligatorio.");return;}
            double precio; int dias;
            try{ precio=Double.parseDouble(tfP.getText().trim()); dias=Integer.parseInt(tfDias.getText().trim()); }
            catch(NumberFormatException ex){lblErr.setText("⚠  Precio y días deben ser números.");return;}
            Membresia m=new Membresia(); m.setIdMembresia(orig!=null?orig.getIdMembresia():0);
            m.setNombre(tfN.getText().trim()); m.setPrecio(precio); m.setDuracionDias(dias);
            m.setDescripcion(taDesc.getText().trim());
            boolean ok=orig==null?dao.insertar(m)>=0:dao.actualizar(m);
            if(!ok){lblErr.setText("⚠  Error al guardar.");return;}
            dlg.dispose(); cargar();
        });
        btns.add(bc); btns.add(bg);
        gbc.gridy++; p.add(btns,gbc);
        dlg.setContentPane(p); dlg.setVisible(true);
    }

    private void eliminar(int id){
        if(JOptionPane.showConfirmDialog(this,"¿Eliminar membresía #"+id+"?\nVerifica que no tenga pagos asociados.",
                "Confirmar",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE)==JOptionPane.YES_OPTION){
            if(!dao.eliminar(id)) JOptionPane.showMessageDialog(this,"No se puede eliminar: tiene pagos asociados.","Error",JOptionPane.ERROR_MESSAGE);
            cargar();
        }
    }

    class BtnRender extends JPanel implements TableCellRenderer {
        BtnRender(){ setLayout(new FlowLayout(FlowLayout.CENTER,4,5)); setBackground(EstilosGym.COLOR_PANEL);
            JButton e=EstilosGym.crearBotonSecundario("✏ Editar"); JButton d=EstilosGym.crearBotonPeligro("🗑");
            e.setPreferredSize(new Dimension(84,34)); d.setPreferredSize(new Dimension(46,34)); add(e); add(d); }
        @Override public Component getTableCellRendererComponent(JTable t,Object v,boolean s,boolean f,int r,int c){
            setBackground(s?new Color(255,87,34,55):EstilosGym.COLOR_PANEL); return this; }
    }
    class BtnEdit extends AbstractCellEditor implements TableCellEditor {
        JPanel panel=new JPanel(new FlowLayout(FlowLayout.CENTER,4,5)); int id;
        BtnEdit(){ panel.setBackground(EstilosGym.COLOR_PANEL);
            JButton e=EstilosGym.crearBotonSecundario("✏ Editar"); JButton d=EstilosGym.crearBotonPeligro("🗑");
            e.setPreferredSize(new Dimension(84,34)); d.setPreferredSize(new Dimension(46,34));
            e.addActionListener(ev->{ stopCellEditing();
                Membresia m=dao.listarTodas().stream().filter(x->x.getIdMembresia()==id).findFirst().orElse(null);
                abrirForm(m); });
            d.addActionListener(ev->{ stopCellEditing(); eliminar(id); }); panel.add(e); panel.add(d); }
        @Override public Component getTableCellEditorComponent(JTable t,Object v,boolean s,int r,int c){
            id=v instanceof Integer?(Integer)v:-1; return panel; }
        @Override public Object getCellEditorValue(){return id;}
    }
}
