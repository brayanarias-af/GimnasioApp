package Gimnasio.Vistas;

import Gimnasio.Controlador.MaquinaDAO;
import Gimnasio.Modelo.Maquina;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class MaquinasVista extends JPanel {

    private final MaquinaDAO dao = new MaquinaDAO();
    private JTable tabla;
    private DefaultTableModel modelo;

    public MaquinasVista() {
        setLayout(new BorderLayout());
        setBackground(EstilosGym.COLOR_FONDO);
        construirUI();
        cargarDatos();
    }

    private void construirUI() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(EstilosGym.COLOR_FONDO);
        header.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 25));
        JLabel titulo = new JLabel("🏋️  Máquinas del Gimnasio");
        titulo.setFont(EstilosGym.FUENTE_TITULO);
        titulo.setForeground(EstilosGym.COLOR_TEXTO);
        JButton btnNueva = EstilosGym.crearBotonPrimario("+ Nueva Máquina");
        btnNueva.addActionListener(e -> abrirFormulario(null));
        header.add(titulo, BorderLayout.WEST);
        header.add(btnNueva, BorderLayout.EAST);

        String[] cols = {"ID", "Nombre", "Tipo", "Marca", "Modelo", "Serial", "Estado", "Mantenimiento", "Acciones"};
        modelo = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 8; }
        };
        tabla = new JTable(modelo);
        estilizarTabla();
        tabla.getColumnModel().getColumn(8).setCellRenderer(new BtnRenderer());
        tabla.getColumnModel().getColumn(8).setCellEditor(new BtnEditor());
        tabla.getColumnModel().getColumn(8).setPreferredWidth(170);
        tabla.getColumnModel().getColumn(0).setMaxWidth(45);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(EstilosGym.COLOR_PANEL);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 8));
        footer.setBackground(EstilosGym.COLOR_PANEL);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, EstilosGym.COLOR_BORDE));
        footer.add(stat("Total", String.valueOf(dao.contarTotal())));
        footer.add(stat("Disponibles", String.valueOf(dao.contarDisponibles())));

        add(header, BorderLayout.NORTH);
        add(scroll,  BorderLayout.CENTER);
        add(footer,  BorderLayout.SOUTH);
    }

    private JLabel stat(String e, String v) {
        JLabel l = new JLabel("<html><font color='#9696a5'>" + e + ": </font><font color='#ff5722'><b>" + v + "</b></font></html>");
        l.setFont(EstilosGym.FUENTE_NORMAL); return l;
    }

    private void estilizarTabla() {
        tabla.setBackground(EstilosGym.COLOR_PANEL);
        tabla.setForeground(EstilosGym.COLOR_TEXTO);
        tabla.setSelectionBackground(new Color(255, 87, 34, 60));
        tabla.setGridColor(EstilosGym.COLOR_BORDE);
        tabla.setRowHeight(46);
        tabla.setFont(EstilosGym.FUENTE_NORMAL);
        tabla.setShowHorizontalLines(true);
        tabla.setShowVerticalLines(false);
        JTableHeader th = tabla.getTableHeader();
        th.setBackground(new Color(18, 18, 26));
        th.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
        th.setFont(new Font("Segoe UI", Font.BOLD, 11));
        th.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, EstilosGym.COLOR_BORDE));
        th.setReorderingAllowed(false);

        DefaultTableCellRenderer r = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean s, boolean f, int row, int col) {
                super.getTableCellRendererComponent(t, v, s, f, row, col);
                setHorizontalAlignment(col == 0 ? CENTER : LEFT);
                setBackground(s ? new Color(255, 87, 34, 60) : EstilosGym.COLOR_PANEL);
                setForeground(EstilosGym.COLOR_TEXTO);
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                if (col == 6) {
                    String val = v != null ? v.toString() : "";
                    setForeground("Disponible".equals(val) ? EstilosGym.COLOR_EXITO
                            : "En uso".equals(val) ? new Color(255, 200, 0) : EstilosGym.COLOR_PELIGRO);
                    setFont(new Font("Segoe UI", Font.BOLD, 12));
                }
                return this;
            }
        };
        for (int i = 0; i < 8; i++) tabla.getColumnModel().getColumn(i).setCellRenderer(r);
    }

    public void cargarDatos() {
        modelo.setRowCount(0);
        for (Maquina m : dao.listarTodas()) {
            modelo.addRow(new Object[]{m.getIdMaquina(), m.getNombre(), m.getTipo(),
                m.getMarca(), m.getModelo(), m.getSerial(),
                m.getEstado(), m.getFechaMantenimiento(), m.getIdMaquina()});
        }
    }

    private void abrirFormulario(Maquina mOrig) {
        JDialog dlg = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this),
                mOrig == null ? "Nueva Máquina" : "Editar Máquina", true);
        dlg.setSize(500, 430);
        dlg.setLocationRelativeTo(this);

        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(EstilosGym.COLOR_PANEL);
        p.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        JTextField tfNombre = EstilosGym.crearCampoTexto();
        JTextField tfTipo   = EstilosGym.crearCampoTexto();
        JTextField tfMarca  = EstilosGym.crearCampoTexto();
        JTextField tfModelo = EstilosGym.crearCampoTexto();
        JTextField tfSerial = EstilosGym.crearCampoTexto();
        JTextField tfFecha  = EstilosGym.crearCampoTexto();
        JComboBox<String> cmbEstado = new JComboBox<>(new String[]{"Disponible", "En uso", "Mantenimiento"});
        cmbEstado.setBackground(EstilosGym.COLOR_FONDO);
        cmbEstado.setForeground(EstilosGym.COLOR_TEXTO);

        if (mOrig != null) {
            tfNombre.setText(mOrig.getNombre()); tfTipo.setText(mOrig.getTipo());
            tfMarca.setText(mOrig.getMarca());   tfModelo.setText(mOrig.getModelo());
            tfSerial.setText(mOrig.getSerial()); tfFecha.setText(mOrig.getFechaMantenimiento());
            cmbEstado.setSelectedItem(mOrig.getEstado());
        }

        Object[][] filas = {{"Nombre *", tfNombre}, {"Tipo", tfTipo}, {"Marca", tfMarca},
                            {"Modelo", tfModelo}, {"Serial", tfSerial},
                            {"Estado", cmbEstado}, {"Último Mant.", tfFecha}};
        for (int i = 0; i < filas.length; i++) {
            gbc.gridy = i; gbc.gridx = 0; gbc.weightx = 0.3;
            JLabel l = EstilosGym.crearEtiqueta(filas[i][0].toString());
            l.setHorizontalAlignment(SwingConstants.RIGHT); p.add(l, gbc);
            gbc.gridx = 1; gbc.weightx = 0.7; p.add((Component) filas[i][1], gbc);
        }

        JLabel lblErr = new JLabel(""); lblErr.setForeground(EstilosGym.COLOR_PELIGRO);
        gbc.gridy = filas.length; gbc.gridx = 0; gbc.gridwidth = 2; p.add(lblErr, gbc);

        JPanel bots = new JPanel(new FlowLayout(FlowLayout.RIGHT)); bots.setOpaque(false);
        JButton btnG = EstilosGym.crearBotonPrimario("Guardar");
        JButton btnC = EstilosGym.crearBotonSecundario("Cancelar");
        btnC.addActionListener(e -> dlg.dispose());
        btnG.addActionListener(e -> {
            if (tfNombre.getText().trim().isEmpty()) { lblErr.setText("⚠  Nombre obligatorio."); return; }
            Maquina m = new Maquina(mOrig == null ? 0 : mOrig.getIdMaquina(),
                    tfNombre.getText().trim(), tfTipo.getText().trim(), tfMarca.getText().trim(),
                    tfModelo.getText().trim(), tfSerial.getText().trim(),
                    cmbEstado.getSelectedItem().toString(), tfFecha.getText().trim());
            boolean ok = mOrig == null ? dao.insertar(m) >= 0 : dao.actualizar(m);
            if (!ok) { lblErr.setText("⚠  Error al guardar en la base de datos."); return; }
            dlg.dispose(); cargarDatos();
        });
        bots.add(btnC); bots.add(btnG);
        gbc.gridy++; p.add(bots, gbc);
        dlg.setContentPane(p); dlg.setVisible(true);
    }

    private void eliminar(int id) {
        int op = JOptionPane.showConfirmDialog(this, "¿Eliminar máquina #" + id + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (op == JOptionPane.YES_OPTION) {
            dao.eliminar(id); cargarDatos();
        }
    }

    class BtnRenderer extends JPanel implements TableCellRenderer {
        BtnRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 4, 5));
            setBackground(EstilosGym.COLOR_PANEL);
            JButton e = EstilosGym.crearBotonSecundario("✏ Editar");
            JButton d = EstilosGym.crearBotonPeligro("🗑");
            e.setPreferredSize(new Dimension(82, 34)); d.setPreferredSize(new Dimension(46, 34));
            add(e); add(d);
        }
        @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
            setBackground(s ? new Color(255, 87, 34, 60) : EstilosGym.COLOR_PANEL); return this;
        }
    }

    class BtnEditor extends AbstractCellEditor implements TableCellEditor {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 5));
        int id;
        BtnEditor() {
            panel.setBackground(EstilosGym.COLOR_PANEL);
            JButton e = EstilosGym.crearBotonSecundario("✏ Editar");
            JButton d = EstilosGym.crearBotonPeligro("🗑");
            e.setPreferredSize(new Dimension(82, 34)); d.setPreferredSize(new Dimension(46, 34));
            e.addActionListener(ev -> {
                stopCellEditing();
                Maquina m = dao.listarTodas().stream()
                        .filter(x -> x.getIdMaquina() == id).findFirst().orElse(null);
                abrirFormulario(m);
            });
            d.addActionListener(ev -> { stopCellEditing(); eliminar(id); });
            panel.add(e); panel.add(d);
        }
        @Override public Component getTableCellEditorComponent(JTable t, Object v, boolean s, int r, int c) {
            id = v instanceof Integer ? (Integer) v : -1; return panel;
        }
        @Override public Object getCellEditorValue() { return id; }
    }
}
