package Gimnasio.Vistas;

import Gimnasio.Controlador.ClienteDAO;
import Gimnasio.Modelo.Cliente;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class FormularioClienteVista extends JDialog {

    private final int        idEditar;
    private final ClienteDAO dao;

    // Campos del formulario
    private JTextField    tfNombres, tfApellidos, tfCedula, tfEdad;
    private JTextField    tfTelefono, tfTelEmergencia, tfCorreo, tfDireccion;
    private JTextField    tfEps, tfPeso, tfAltura, tfFechaIngreso;
    private JTextField    tfUsuario;
    private JPasswordField tfContrasena;
    private JComboBox<String> cmbSexo, cmbObjetivo, cmbMembresia;
    private JTextArea     taObservaciones;
    private JLabel        lblError;

    public FormularioClienteVista(JFrame parent, int idEditar, ClienteDAO dao) {
        super(parent, idEditar < 0 ? "Registrar Nuevo Cliente" : "Editar Cliente", true);
        this.idEditar = idEditar;
        this.dao      = dao;
        setSize(720, 660);
        setLocationRelativeTo(parent);
        setResizable(false);
        construirUI();
        if (idEditar >= 0) cargarDatos();
    }

    private void construirUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(EstilosGym.COLOR_PANEL);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(18, 18, 26));
        header.setBorder(BorderFactory.createEmptyBorder(14, 22, 14, 22));
        JLabel lblTit = new JLabel(idEditar < 0 ? "➕  Registrar Nuevo Cliente" : "✏  Editar Cliente — ID " + idEditar);
        lblTit.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTit.setForeground(EstilosGym.COLOR_TEXTO);
        header.add(lblTit);

        // Form grid
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(EstilosGym.COLOR_PANEL);
        form.setBorder(BorderFactory.createEmptyBorder(18, 22, 6, 22));
        GridBagConstraints g = new GridBagConstraints();
        g.insets  = new Insets(5, 5, 5, 5);
        g.fill    = GridBagConstraints.HORIZONTAL;

        tfNombres       = EstilosGym.crearCampoTexto();
        tfApellidos     = EstilosGym.crearCampoTexto();
        tfCedula        = EstilosGym.crearCampoTexto();
        tfEdad          = EstilosGym.crearCampoTexto();
        tfTelefono      = EstilosGym.crearCampoTexto();
        tfTelEmergencia = EstilosGym.crearCampoTexto();
        tfCorreo        = EstilosGym.crearCampoTexto();
        tfDireccion     = EstilosGym.crearCampoTexto();
        tfEps           = EstilosGym.crearCampoTexto();
        tfPeso          = EstilosGym.crearCampoTexto();
        tfAltura        = EstilosGym.crearCampoTexto();
        tfFechaIngreso  = EstilosGym.crearCampoTexto();
        tfUsuario       = EstilosGym.crearCampoTexto();
        tfContrasena    = EstilosGym.crearCampoPassword();
        taObservaciones = new JTextArea(2, 20);
        taObservaciones.setBackground(EstilosGym.COLOR_FONDO);
        taObservaciones.setForeground(EstilosGym.COLOR_TEXTO);
        taObservaciones.setFont(EstilosGym.FUENTE_NORMAL);
        taObservaciones.setLineWrap(true);
        taObservaciones.setWrapStyleWord(true);
        taObservaciones.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstilosGym.COLOR_BORDE),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));

        cmbSexo      = combo("Masculino", "Femenino", "Otro");
        cmbObjetivo  = combo("Aumento de masa muscular", "Pérdida de peso", "Resistencia", "Acondicionamiento", "Flexibilidad");
        cmbMembresia = combo("Activa", "Vencida", "Suspendida");

        // Fecha por defecto = hoy
        tfFechaIngreso.setText(LocalDate.now().toString());

        // Layout: etiqueta (col 0), campo (col 1), etiqueta (col 2), campo (col 3)
        Object[][] filas = {
            {"Nombres *",        tfNombres,       "Apellidos *",    tfApellidos},
            {"Cédula *",         tfCedula,        "Edad",           tfEdad},
            {"Sexo",             cmbSexo,         "Teléfono",       tfTelefono},
            {"Tel. Emergencia",  tfTelEmergencia, "Correo",         tfCorreo},
            {"Dirección",        tfDireccion,     "EPS",            tfEps},
            {"Peso (kg)",        tfPeso,          "Altura (m)",     tfAltura},
            {"Objetivo",         cmbObjetivo,     "Membresía",      cmbMembresia},
            {"Fecha Ingreso",    tfFechaIngreso,  "Usuario Login",  tfUsuario},
            {"Contraseña Login", tfContrasena,    "",               null},
        };

        for (int i = 0; i < filas.length; i++) {
            g.gridy = i;
            // col 0 etiq
            g.gridx = 0; g.weightx = 0.18;
            JLabel le = EstilosGym.crearEtiqueta(filas[i][0].toString());
            le.setHorizontalAlignment(SwingConstants.RIGHT); form.add(le, g);
            // col 1 campo
            g.gridx = 1; g.weightx = 0.32;
            if (filas[i][1] != null) form.add((Component) filas[i][1], g);
            // col 2 etiq
            g.gridx = 2; g.weightx = 0.18;
            JLabel le2 = EstilosGym.crearEtiqueta(filas[i][2].toString());
            le2.setHorizontalAlignment(SwingConstants.RIGHT); form.add(le2, g);
            // col 3 campo
            g.gridx = 3; g.weightx = 0.32;
            if (filas[i][3] != null) form.add((Component) filas[i][3], g);
        }

        // Observaciones: ancho completo
        g.gridy = filas.length; g.gridx = 0; g.weightx = 0.18;
        JLabel lObs = EstilosGym.crearEtiqueta("Observaciones");
        lObs.setHorizontalAlignment(SwingConstants.RIGHT); form.add(lObs, g);
        g.gridx = 1; g.gridwidth = 3; g.weightx = 0.82;
        form.add(new JScrollPane(taObservaciones), g);
        g.gridwidth = 1;

        if (idEditar >= 0) {
            tfUsuario.setEditable(false);
            tfUsuario.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
            tfContrasena.setToolTipText("Dejar vacío para no cambiar la contraseña");
        }

        // Footer
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(EstilosGym.COLOR_PANEL);
        footer.setBorder(BorderFactory.createEmptyBorder(8, 22, 14, 22));
        lblError = new JLabel("");
        lblError.setFont(EstilosGym.FUENTE_PEQUEÑA);
        lblError.setForeground(EstilosGym.COLOR_PELIGRO);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btns.setOpaque(false);
        JButton btnC = EstilosGym.crearBotonSecundario("Cancelar");
        JButton btnG = EstilosGym.crearBotonPrimario(idEditar < 0 ? "✓  Registrar" : "✓  Guardar");
        btnC.addActionListener(e -> dispose());
        btnG.addActionListener(e -> guardar());
        btns.add(btnC); btns.add(btnG);
        footer.add(lblError, BorderLayout.WEST);
        footer.add(btns,     BorderLayout.EAST);

        root.add(header, BorderLayout.NORTH);
        root.add(form,   BorderLayout.CENTER);
        root.add(footer, BorderLayout.SOUTH);
        setContentPane(root);
    }

    private JComboBox<String> combo(String... opts) {
        JComboBox<String> c = new JComboBox<>(opts);
        c.setBackground(EstilosGym.COLOR_FONDO);
        c.setForeground(EstilosGym.COLOR_TEXTO);
        c.setFont(EstilosGym.FUENTE_NORMAL);
        return c;
    }

    private void cargarDatos() {
        Cliente c = dao.buscarPorId(idEditar);
        if (c == null) return;
        tfNombres.setText(c.getNombres());
        tfApellidos.setText(c.getApellidos());
        tfCedula.setText(c.getCedula());
        tfEdad.setText(String.valueOf(c.getEdad()));
        cmbSexo.setSelectedItem(c.getSexo());
        tfTelefono.setText(c.getTelefono());
        tfTelEmergencia.setText(c.getTelefonoEmergencia());
        tfCorreo.setText(c.getCorreo());
        tfDireccion.setText(c.getDireccion());
        tfEps.setText(c.getEps());
        tfPeso.setText(String.valueOf(c.getPeso()));
        tfAltura.setText(String.valueOf(c.getAltura()));
        cmbObjetivo.setSelectedItem(c.getObjetivo());
        cmbMembresia.setSelectedItem(c.getEstadoMembresia());
        tfFechaIngreso.setText(c.getFechaIngreso());
        taObservaciones.setText(c.getObservaciones());
    }

    private void guardar() {
        // Validaciones básicas
        if (tfNombres.getText().trim().isEmpty() || tfApellidos.getText().trim().isEmpty()
                || tfCedula.getText().trim().isEmpty()) {
            lblError.setText("⚠  Nombres, Apellidos y Cédula son obligatorios."); return;
        }
        int edad = 0;
        if (!tfEdad.getText().trim().isEmpty()) {
            try { edad = Integer.parseInt(tfEdad.getText().trim()); }
            catch (NumberFormatException e) { lblError.setText("⚠  Edad debe ser número."); return; }
        }
        double peso = 0, altura = 0;
        try {
            if (!tfPeso.getText().trim().isEmpty())   peso   = Double.parseDouble(tfPeso.getText().trim());
            if (!tfAltura.getText().trim().isEmpty())  altura = Double.parseDouble(tfAltura.getText().trim());
        } catch (NumberFormatException e) { lblError.setText("⚠  Peso/Altura deben ser números."); return; }

        Cliente c = new Cliente();
        c.setIdCliente       (idEditar < 0 ? 0 : idEditar);
        c.setNombres         (tfNombres.getText().trim());
        c.setApellidos       (tfApellidos.getText().trim());
        c.setCedula          (tfCedula.getText().trim());
        c.setEdad            (edad);
        c.setSexo            (cmbSexo.getSelectedItem().toString());
        c.setTelefono        (tfTelefono.getText().trim());
        c.setTelefonoEmergencia(tfTelEmergencia.getText().trim());
        c.setCorreo          (tfCorreo.getText().trim());
        c.setDireccion       (tfDireccion.getText().trim());
        c.setEps             (tfEps.getText().trim());
        c.setPeso            (peso);
        c.setAltura          (altura);
        c.setObjetivo        (cmbObjetivo.getSelectedItem().toString());
        c.setEstadoMembresia (cmbMembresia.getSelectedItem().toString());
        c.setFechaIngreso    (tfFechaIngreso.getText().trim());
        c.setObservaciones   (taObservaciones.getText().trim());

        if (idEditar < 0) {
            // CREAR — requiere usuario
            String uLogin = tfUsuario.getText().trim();
            String uPass  = new String(tfContrasena.getPassword()).trim();
            if (uLogin.isEmpty() || uPass.isEmpty()) {
                lblError.setText("⚠  Usuario y contraseña de login son requeridos al registrar."); return;
            }
            int idGen = dao.insertar(c, uLogin, uPass);
            if (idGen < 0) {
                lblError.setText("⚠  Error al guardar. Verifica que la cédula/usuario no estén duplicados."); return;
            }
            JOptionPane.showMessageDialog(this, "Cliente registrado con ID " + idGen + ".", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            // ACTUALIZAR
            boolean ok = dao.actualizar(c);
            if (!ok) { lblError.setText("⚠  Error al actualizar el cliente."); return; }
            JOptionPane.showMessageDialog(this, "Cliente actualizado correctamente.", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        dispose();
    }
}
