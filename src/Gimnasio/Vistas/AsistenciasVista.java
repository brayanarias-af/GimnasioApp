package Gimnasio.Vistas;

import Gimnasio.Controlador.AsistenciaDAO;
import Gimnasio.Controlador.ClienteDAO;
import Gimnasio.Modelo.Asistencia;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List; 
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AsistenciasVista extends TablaBase {

    private final AsistenciaDAO dao    = new AsistenciaDAO();
    private final ClienteDAO    cliDAO = new ClienteDAO();

    public AsistenciasVista() {
        super();
        construir();
        cargar();
    }

    private void construir() {
        add(crearHeader("📅  Control de Asistencias", false, "📌 Registrar Entrada"), BorderLayout.NORTH);

        String[] cols = {"ID","Fecha","Cliente","Entrada","Salida","Acciones"};
        crearModelo(cols, 5);

        DefaultTableCellRenderer r = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t,Object v,boolean s,boolean f,int row,int col){
                super.getTableCellRendererComponent(t,v,s,f,row,col);
                setHorizontalAlignment(col==0||col==3||col==4 ? CENTER : LEFT);
                setBackground(s?new Color(255,87,34,55):EstilosGym.COLOR_PANEL);
                setForeground(EstilosGym.COLOR_TEXTO);
                setBorder(BorderFactory.createEmptyBorder(0,8,0,8));
                if (col==3) { setForeground(new Color(80,220,80)); setFont(new Font("Segoe UI",Font.BOLD,12)); }
                if (col==4) {
                    String val = v!=null?v.toString():"";
                    if (val.isEmpty() || val.equals("—")) setForeground(new Color(255,180,0));
                    else { setForeground(EstilosGym.COLOR_TEXTO_GRIS); }
                }
                return this;
            }
        };
        for (int i=0;i<5;i++) tabla.getColumnModel().getColumn(i).setCellRenderer(r);
        tabla.getColumnModel().getColumn(5).setCellRenderer(new BtnRender());
        tabla.getColumnModel().getColumn(5).setCellEditor(new BtnEdit());
        tabla.getColumnModel().getColumn(5).setPreferredWidth(200);
        tabla.getColumnModel().getColumn(0).setMaxWidth(48);
        tabla.getColumnModel().getColumn(3).setMaxWidth(90);
        tabla.getColumnModel().getColumn(4).setMaxWidth(90);

        add(crearScroll(), BorderLayout.CENTER);
        add(crearFooter("Asistencias hoy", String.valueOf(dao.contarHoy())), BorderLayout.SOUTH);
    }

    @Override protected void onNuevo() { registrarEntrada(); }

    private void cargar() {
        modelo.setRowCount(0);
        for (Asistencia a : dao.listarTodas()) {
            modelo.addRow(new Object[]{
                a.getIdAsistencia(), a.getFecha(), a.getNombreCliente(),
                a.getHoraEntrada() != null ? a.getHoraEntrada() : "—",
                a.getHoraSalida()  != null ? a.getHoraSalida()  : "—",
                a.getIdAsistencia()
            });
        }
    }

    private void registrarEntrada() {
        JDialog dlg = new JDialog((JFrame)SwingUtilities.getWindowAncestor(this),"Registrar Entrada",true);
        dlg.setSize(420,260); dlg.setLocationRelativeTo(this);

        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(EstilosGym.COLOR_PANEL);
        p.setBorder(BorderFactory.createEmptyBorder(20,24,20,24));
        GridBagConstraints gbc=new GridBagConstraints(); gbc.insets=new Insets(7,6,7,6);
        gbc.fill=GridBagConstraints.HORIZONTAL;

        JComboBox<String> cmbCli = new JComboBox<>();
        List<Object[]> clientes = cliDAO.listarParaCombo();
        int[] ids = new int[clientes.size()];
        for (int i=0;i<clientes.size();i++) { cmbCli.addItem(clientes.get(i)[1].toString()); ids[i]=(int)clientes.get(i)[0]; }
        cmbCli.setBackground(EstilosGym.COLOR_FONDO); cmbCli.setForeground(EstilosGym.COLOR_TEXTO); cmbCli.setFont(EstilosGym.FUENTE_NORMAL);

        DatePickerField dpFecha = new DatePickerField(LocalDate.now().toString());
        JTextField tfHora  = EstilosGym.crearCampoTexto();
        tfHora.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));

        Object[][] rows={{"Cliente *",cmbCli},{"Fecha *",dpFecha},{"Hora entrada *",tfHora}};
        for (int i=0;i<rows.length;i++){
            gbc.gridy=i; gbc.gridx=0; gbc.weightx=0.35;
            JLabel l=EstilosGym.crearEtiqueta(rows[i][0].toString()); l.setHorizontalAlignment(SwingConstants.RIGHT); p.add(l,gbc);
            gbc.gridx=1; gbc.weightx=0.65; p.add((Component)rows[i][1],gbc);
        }
        JLabel lblErr=new JLabel(""); lblErr.setForeground(EstilosGym.COLOR_PELIGRO);
        gbc.gridy=3; gbc.gridx=0; gbc.gridwidth=2; p.add(lblErr,gbc);
        JPanel btns=new JPanel(new FlowLayout(FlowLayout.RIGHT,10,0)); btns.setOpaque(false);
        JButton bc=EstilosGym.crearBotonSecundario("Cancelar"); JButton bg=EstilosGym.crearBotonPrimario("✓ Registrar");
        bc.addActionListener(e->dlg.dispose());
        bg.addActionListener(e->{
            if(clientes.isEmpty()){lblErr.setText("⚠  No hay clientes.");return;}
            if(dpFecha.getFecha().isEmpty()||tfHora.getText().trim().isEmpty()){lblErr.setText("⚠  Fecha y hora requeridas.");return;}
            Asistencia a=new Asistencia(); a.setIdCliente(ids[cmbCli.getSelectedIndex()]);
            a.setFecha(dpFecha.getFecha()); a.setHoraEntrada(tfHora.getText().trim());
            if(dao.insertar(a)<0){lblErr.setText("⚠  Error al guardar.");return;}
            dlg.dispose(); cargar();
        });
        btns.add(bc); btns.add(bg);
        gbc.gridy++; p.add(btns,gbc);
        dlg.setContentPane(p); dlg.setVisible(true);
    }

    private void registrarSalida(int id) {
        String hora = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        String input = JOptionPane.showInputDialog(this, "Hora de salida:", hora);
        if (input != null && !input.trim().isEmpty()) {
            dao.registrarSalida(id, input.trim()); cargar();
        }
    }

    private void eliminar(int id) {
        if (JOptionPane.showConfirmDialog(this,"¿Eliminar registro #"+id+"?","Confirmar",
                JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
            dao.eliminar(id); cargar();
        }
    }

    class BtnRender extends JPanel implements TableCellRenderer {
        BtnRender(){
            setLayout(new FlowLayout(FlowLayout.CENTER,4,5)); setBackground(EstilosGym.COLOR_PANEL);
            JButton s=EstilosGym.crearBotonSecundario("🚪 Salida"); JButton d=EstilosGym.crearBotonPeligro("🗑");
            s.setPreferredSize(new Dimension(100,34)); d.setPreferredSize(new Dimension(48,34));
            add(s); add(d);
        }
        @Override public Component getTableCellRendererComponent(JTable t,Object v,boolean s,boolean f,int r,int c){
            setBackground(s?new Color(255,87,34,55):EstilosGym.COLOR_PANEL); return this; }
    }
    class BtnEdit extends AbstractCellEditor implements TableCellEditor {
        JPanel panel=new JPanel(new FlowLayout(FlowLayout.CENTER,4,5)); int id;
        BtnEdit(){
            panel.setBackground(EstilosGym.COLOR_PANEL);
            JButton s=EstilosGym.crearBotonSecundario("🚪 Salida"); JButton d=EstilosGym.crearBotonPeligro("🗑");
            s.setPreferredSize(new Dimension(100,34)); d.setPreferredSize(new Dimension(48,34));
            s.addActionListener(ev->{stopCellEditing();registrarSalida(id);});
            d.addActionListener(ev->{stopCellEditing();eliminar(id);});
            panel.add(s); panel.add(d);
        }
        @Override public Component getTableCellEditorComponent(JTable t,Object v,boolean s,int r,int c){
            id=v instanceof Integer?(Integer)v:-1; return panel; }
        @Override public Object getCellEditorValue(){return id;}
    }
}
