package Gimnasio.Vistas;

import Gimnasio.Controlador.ClienteDAO;
import Gimnasio.Modelo.Cliente;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class FormularioClienteVista extends JDialog {

    private final int id;
    private final ClienteDAO dao;

    private JTextField tfNombres,tfApellidos,tfCedula,tfEdad,tfTel,tfTelEm,tfCorreo,tfDir,tfEps,tfPeso,tfAltura,tfUsuario;
    private DatePickerField dpFecha;
    private JPasswordField tfClave;
    private JComboBox<String> cmbSexo,cmbObj,cmbMem;
    private JTextArea taObs;
    private JLabel lblErr;

    public FormularioClienteVista(JFrame p, int id, ClienteDAO dao) {
        super(p, id<0 ? "Registrar Nuevo Cliente" : "Editar Cliente — ID "+id, true);
        this.id=id; this.dao=dao;
        setSize(740,680); setLocationRelativeTo(p); setResizable(false);
        construir();
        if (id>=0) cargar();
    }

    private void construir() {
        JPanel root=new JPanel(new BorderLayout()); root.setBackground(EstilosGym.COLOR_PANEL);

        // Header
        JPanel hdr=new JPanel(new BorderLayout()); hdr.setBackground(new Color(16,16,24));
        hdr.setBorder(BorderFactory.createEmptyBorder(14,22,14,22));
        JLabel t=new JLabel(id<0?"➕  Nuevo Cliente":"✏  Editar Cliente");
        t.setFont(new Font("Segoe UI",Font.BOLD,18)); t.setForeground(EstilosGym.COLOR_TEXTO);
        hdr.add(t);

        // Formulario
        JPanel form=new JPanel(new GridBagLayout()); form.setBackground(EstilosGym.COLOR_PANEL);
        form.setBorder(BorderFactory.createEmptyBorder(16,22,8,22));
        GridBagConstraints g=new GridBagConstraints(); g.insets=new Insets(5,5,5,5); g.fill=GridBagConstraints.HORIZONTAL;

        tfNombres=f(); tfApellidos=f(); tfCedula=f(); tfEdad=f(); tfTel=f(); tfTelEm=f();
        tfCorreo=f(); tfDir=f(); tfEps=f(); tfPeso=f(); tfAltura=f();
        dpFecha = new DatePickerField(LocalDate.now().toString());
        tfUsuario=f(); tfClave=EstilosGym.crearCampoPassword();
        taObs=new JTextArea(2,20); taObs.setBackground(EstilosGym.COLOR_FONDO);
        taObs.setForeground(EstilosGym.COLOR_TEXTO); taObs.setFont(EstilosGym.FUENTE_NORMAL);
        taObs.setLineWrap(true); taObs.setWrapStyleWord(true);
        taObs.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(EstilosGym.COLOR_BORDE),BorderFactory.createEmptyBorder(6,8,6,8)));
        cmbSexo=cmb("Masculino","Femenino","Otro");
        cmbObj=cmb("Aumento de masa muscular","Pérdida de peso","Resistencia","Acondicionamiento","Flexibilidad");
        cmbMem=cmb("Activa","Vencida","Suspendida");

        Object[][] rows={
            {"Nombres *",tfNombres,   "Apellidos *",tfApellidos},
            {"Cédula *",tfCedula,     "Edad",tfEdad},
            {"Sexo",cmbSexo,          "Teléfono",tfTel},
            {"Tel. Emergencia",tfTelEm,"Correo",tfCorreo},
            {"Dirección",tfDir,        "EPS",tfEps},
            {"Peso (kg)",tfPeso,       "Altura (m)",tfAltura},
            {"Objetivo",cmbObj,        "Membresía",cmbMem},
            {"Fecha Ingreso",dpFecha,  "Usuario *",tfUsuario},
            {"Contraseña *",tfClave,   "",null}
        };
        for (int i=0;i<rows.length;i++) {
            g.gridy=i;
            g.gridx=0; g.weightx=0.18; JLabel l=EstilosGym.crearEtiqueta(rows[i][0].toString());
            l.setHorizontalAlignment(SwingConstants.RIGHT); form.add(l,g);
            g.gridx=1; g.weightx=0.32; if(rows[i][1]!=null) form.add((java.awt.Component)rows[i][1],g);
            g.gridx=2; g.weightx=0.18; JLabel l2=EstilosGym.crearEtiqueta(rows[i][2].toString());
            l2.setHorizontalAlignment(SwingConstants.RIGHT); form.add(l2,g);
            g.gridx=3; g.weightx=0.32; if(rows[i][3]!=null) form.add((java.awt.Component)rows[i][3],g);
        }
        g.gridy=rows.length; g.gridx=0; g.weightx=0.18;
        JLabel lo=EstilosGym.crearEtiqueta("Observaciones"); lo.setHorizontalAlignment(SwingConstants.RIGHT); form.add(lo,g);
        g.gridx=1; g.gridwidth=3; g.weightx=0.82; form.add(new JScrollPane(taObs),g); g.gridwidth=1;

        if (id>=0) { tfUsuario.setEditable(false); tfUsuario.setForeground(EstilosGym.COLOR_TEXTO_GRIS); }

        // Footer
        JPanel ftr=new JPanel(new BorderLayout()); ftr.setBackground(EstilosGym.COLOR_PANEL);
        ftr.setBorder(BorderFactory.createEmptyBorder(10,22,14,22));
        lblErr=new JLabel(""); lblErr.setFont(EstilosGym.FUENTE_PEQUEÑA); lblErr.setForeground(EstilosGym.COLOR_PELIGRO);
        JPanel btns=new JPanel(new FlowLayout(FlowLayout.RIGHT,10,0)); btns.setOpaque(false);
        JButton bc=EstilosGym.crearBotonSecundario("Cancelar");
        JButton bg=EstilosGym.crearBotonPrimario(id<0?"✓  Registrar":"✓  Guardar");
        bc.addActionListener(e->dispose()); bg.addActionListener(e->guardar());
        btns.add(bc); btns.add(bg);
        ftr.add(lblErr,BorderLayout.WEST); ftr.add(btns,BorderLayout.EAST);

        root.add(hdr,BorderLayout.NORTH); root.add(form,BorderLayout.CENTER); root.add(ftr,BorderLayout.SOUTH);
        setContentPane(root);
    }

    private JTextField f() { return EstilosGym.crearCampoTexto(); }
    private JComboBox<String> cmb(String... opts) {
        JComboBox<String> c = new JComboBox<>(opts);
        EstilosGym.aplicarEstiloCombo(c);
        return c;
    }

    private void cargar() {
        Cliente c=dao.buscarPorId(id); if(c==null) return;
        tfNombres.setText(c.getNombres()); tfApellidos.setText(c.getApellidos());
        tfCedula.setText(c.getCedula()); tfEdad.setText(String.valueOf(c.getEdad()));
        cmbSexo.setSelectedItem(c.getSexo()); tfTel.setText(c.getTelefono());
        tfTelEm.setText(c.getTelefonoEmergencia()); tfCorreo.setText(c.getCorreo());
        tfDir.setText(c.getDireccion()); tfEps.setText(c.getEps());
        tfPeso.setText(String.valueOf(c.getPeso())); tfAltura.setText(String.valueOf(c.getAltura()));
        cmbObj.setSelectedItem(c.getObjetivo()); cmbMem.setSelectedItem(c.getEstadoMembresia());
        dpFecha.setFecha(c.getFechaIngreso()); taObs.setText(c.getObservaciones());
    }

    private void guardar() {
        if(tfNombres.getText().trim().isEmpty()||tfApellidos.getText().trim().isEmpty()||tfCedula.getText().trim().isEmpty()){
            lblErr.setText("⚠  Nombres, Apellidos y Cédula son obligatorios."); return; }
        int edad=0; double peso=0,alt=0;
        try{ if(!tfEdad.getText().trim().isEmpty()) edad=Integer.parseInt(tfEdad.getText().trim()); }
        catch(NumberFormatException e){ lblErr.setText("⚠  Edad debe ser número."); return; }
        try{ if(!tfPeso.getText().trim().isEmpty()) peso=Double.parseDouble(tfPeso.getText().trim());
             if(!tfAltura.getText().trim().isEmpty()) alt=Double.parseDouble(tfAltura.getText().trim()); }
        catch(NumberFormatException e){ lblErr.setText("⚠  Peso/Altura deben ser números."); return; }

        Cliente c=new Cliente(); c.setIdCliente(Math.max(id,0));
        c.setNombres(tfNombres.getText().trim()); c.setApellidos(tfApellidos.getText().trim());
        c.setCedula(tfCedula.getText().trim()); c.setEdad(edad);
        c.setSexo(cmbSexo.getSelectedItem().toString()); c.setTelefono(tfTel.getText().trim());
        c.setTelefonoEmergencia(tfTelEm.getText().trim()); c.setCorreo(tfCorreo.getText().trim());
        c.setDireccion(tfDir.getText().trim()); c.setEps(tfEps.getText().trim());
        c.setPeso(peso); c.setAltura(alt); c.setObjetivo(cmbObj.getSelectedItem().toString());
        c.setEstadoMembresia(cmbMem.getSelectedItem().toString());
        c.setFechaIngreso(dpFecha.getFecha()); c.setObservaciones(taObs.getText().trim());

        if(id<0){
            String u=tfUsuario.getText().trim(); String p=new String(tfClave.getPassword()).trim();
            if(u.isEmpty()||p.isEmpty()){ lblErr.setText("⚠  Usuario y contraseña requeridos al crear."); return; }
            int gen=dao.insertar(c,u,p);
            if(gen<0){ lblErr.setText("⚠  Error: cédula o usuario ya existe."); return; }
            JOptionPane.showMessageDialog(this,"Cliente registrado con ID "+gen+".","✅ Registrado",JOptionPane.INFORMATION_MESSAGE);
        } else {
            if(!dao.actualizar(c)){ lblErr.setText("⚠  Error al actualizar."); return; }
            JOptionPane.showMessageDialog(this,"Cliente actualizado.","✅ Actualizado",JOptionPane.INFORMATION_MESSAGE);
        }
        dispose();
    }
}
