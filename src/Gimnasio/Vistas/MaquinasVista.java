package Gimnasio.Vistas;

import Gimnasio.Controlador.MaquinaDAO;
import Gimnasio.Modelo.Maquina;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;

public class MaquinasVista extends TablaBase {

    private final MaquinaDAO dao = new MaquinaDAO();

    public MaquinasVista() {
        super();
        construir();
        cargar();
    }

    private void construir() {
        add(crearHeader("Máquinas del Gimnasio", false, "+ Nueva Máquina"), BorderLayout.NORTH);

        String[] cols = {"ID","Nombre","Tipo","Marca","Modelo","Serial","Estado","Mantenimiento","Acciones"};
        crearModelo(cols, 8);

        Map<String,Color> est = new LinkedHashMap<>();
        est.put("Disponible",   EstilosGym.COLOR_EXITO);
        est.put("En uso",       new Color(255,200,0));
        est.put("Mantenimiento",EstilosGym.COLOR_PELIGRO);
        aplicarRendererEstado(6, est);

        tabla.getColumnModel().getColumn(8).setCellRenderer(new BtnRender());
        tabla.getColumnModel().getColumn(8).setCellEditor(new BtnEdit());
        tabla.getColumnModel().getColumn(8).setPreferredWidth(172);
        tabla.getColumnModel().getColumn(0).setMaxWidth(48);

        add(crearScroll(), BorderLayout.CENTER);
        add(crearFooter(
            "Total máquinas", String.valueOf(dao.contarTotal()),
            "Disponibles",    String.valueOf(dao.contarDisponibles())
        ), BorderLayout.SOUTH);
    }

    @Override protected void onNuevo() { abrirForm(null); }

    private void cargar() {
        modelo.setRowCount(0);
        for (Maquina m : dao.listarTodas())
            modelo.addRow(new Object[]{m.getIdMaquina(),m.getNombre(),m.getTipo(),m.getMarca(),
                m.getModelo(),m.getSerial(),m.getEstado(),m.getFechaMantenimiento(),m.getIdMaquina()});
    }

    private void abrirForm(Maquina orig) {
        JDialog dlg = new JDialog((JFrame)SwingUtilities.getWindowAncestor(this),
                orig==null?"Nueva Máquina":"Editar Máquina",true);
        dlg.setSize(480,400); dlg.setLocationRelativeTo(this);

        JPanel p=new JPanel(new GridBagLayout()); p.setBackground(EstilosGym.COLOR_PANEL);
        p.setBorder(BorderFactory.createEmptyBorder(20,24,20,24));
        GridBagConstraints gbc=new GridBagConstraints(); gbc.insets=new Insets(6,6,6,6); gbc.fill=GridBagConstraints.HORIZONTAL;

        JTextField tfN=tf(),tfT=tf(),tfMar=tf(),tfMod=tf(),tfSer=tf(),tfF=tf();
        JComboBox<String> cmbE=cmb("Disponible","En uso","Mantenimiento");
        if(orig!=null){ tfN.setText(orig.getNombre()); tfT.setText(orig.getTipo()); tfMar.setText(orig.getMarca());
            tfMod.setText(orig.getModelo()); tfSer.setText(orig.getSerial()); tfF.setText(orig.getFechaMantenimiento());
            cmbE.setSelectedItem(orig.getEstado()); }

        Object[][] rows={{"Nombre *",tfN},{"Tipo",tfT},{"Marca",tfMar},
                         {"Modelo",tfMod},{"Serial",tfSer},{"Estado",cmbE},{"Último Mant.",tfF}};
        for(int i=0;i<rows.length;i++){
            gbc.gridy=i; gbc.gridx=0; gbc.weightx=0.32;
            JLabel l=EstilosGym.crearEtiqueta(rows[i][0].toString()); l.setHorizontalAlignment(SwingConstants.RIGHT); p.add(l,gbc);
            gbc.gridx=1; gbc.weightx=0.68; p.add((Component)rows[i][1],gbc);
        }
        JLabel lblErr=new JLabel(""); lblErr.setForeground(EstilosGym.COLOR_PELIGRO);
        gbc.gridy=rows.length; gbc.gridx=0; gbc.gridwidth=2; p.add(lblErr,gbc);
        JPanel btns=new JPanel(new FlowLayout(FlowLayout.RIGHT,10,0)); btns.setOpaque(false);
        JButton bc=EstilosGym.crearBotonSecundario("Cancelar"); JButton bg=EstilosGym.crearBotonPrimario("Guardar");
        bc.addActionListener(e->dlg.dispose());
        bg.addActionListener(e->{
            if(tfN.getText().trim().isEmpty()){lblErr.setText("⚠  Nombre obligatorio.");return;}
            Maquina m=new Maquina(); m.setIdMaquina(orig!=null?orig.getIdMaquina():0);
            m.setNombre(tfN.getText().trim()); m.setTipo(tfT.getText().trim()); m.setMarca(tfMar.getText().trim());
            m.setModelo(tfMod.getText().trim()); m.setSerial(tfSer.getText().trim());
            m.setEstado(cmbE.getSelectedItem().toString()); m.setFechaMantenimiento(tfF.getText().trim());
            boolean ok=orig==null ? dao.insertar(m)>=0 : dao.actualizar(m);
            if(!ok){lblErr.setText("⚠  Error al guardar.");return;}
            dlg.dispose(); cargar();
        });
        btns.add(bc); btns.add(bg);
        gbc.gridy++; p.add(btns,gbc);
        dlg.setContentPane(p); dlg.setVisible(true);
    }

    private void eliminar(int id){
        if(JOptionPane.showConfirmDialog(this,"¿Eliminar máquina #"+id+"?","Confirmar",
                JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){ dao.eliminar(id); cargar(); }
    }

    private JTextField tf(){ return EstilosGym.crearCampoTexto(); }
    private JComboBox<String> cmb(String... opts){
        JComboBox<String> c=new JComboBox<>(opts);
        EstilosGym.aplicarEstiloCombo(c);
        return c;
    }

    class BtnRender extends JPanel implements TableCellRenderer {
        BtnRender(){
            setLayout(new FlowLayout(FlowLayout.CENTER,4,5)); setBackground(EstilosGym.COLOR_PANEL);
            JButton e=EstilosGym.crearBotonSecundario("✏ Editar"); JButton d=EstilosGym.crearBotonPeligro("🗑");
            e.setPreferredSize(new Dimension(84,34)); d.setPreferredSize(new Dimension(46,34)); add(e); add(d);
        }
        @Override public Component getTableCellRendererComponent(JTable t,Object v,boolean s,boolean f,int r,int c){
            setBackground(s?new Color(255,87,34,55):EstilosGym.COLOR_PANEL); return this; }
    }
    class BtnEdit extends AbstractCellEditor implements TableCellEditor {
        JPanel panel=new JPanel(new FlowLayout(FlowLayout.CENTER,4,5)); int id;
        BtnEdit(){
            panel.setBackground(EstilosGym.COLOR_PANEL);
            JButton e=EstilosGym.crearBotonSecundario("✏ Editar"); JButton d=EstilosGym.crearBotonPeligro("🗑");
            e.setPreferredSize(new Dimension(84,34)); d.setPreferredSize(new Dimension(46,34));
            e.addActionListener(ev->{stopCellEditing();
                Maquina m=dao.listarTodas().stream().filter(x->x.getIdMaquina()==id).findFirst().orElse(null);
                abrirForm(m);});
            d.addActionListener(ev->{stopCellEditing();eliminar(id);});
            panel.add(e); panel.add(d);
        }
        @Override public Component getTableCellEditorComponent(JTable t,Object v,boolean s,int r,int c){
            id=v instanceof Integer?(Integer)v:-1; return panel; }
        @Override public Object getCellEditorValue(){return id;}
    }
}
