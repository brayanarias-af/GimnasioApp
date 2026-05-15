package Gimnasio.Vistas;

import Gimnasio.Controlador.RutinaDAO;
import Gimnasio.Modelo.Rutina;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class RutinasVista extends JPanel {

    private final RutinaDAO dao = new RutinaDAO();
    private JTable tabla;
    private DefaultTableModel modelo;

    public RutinasVista() {
        setLayout(new BorderLayout());
        setBackground(EstilosGym.COLOR_FONDO);
        construirUI();
        cargarDatos();
    }

    private void construirUI() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(EstilosGym.COLOR_FONDO);
        header.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 25));
        JLabel titulo = new JLabel("📋  Rutinas de Entrenamiento");
        titulo.setFont(EstilosGym.FUENTE_TITULO);
        titulo.setForeground(EstilosGym.COLOR_TEXTO);
        JButton btnNueva = EstilosGym.crearBotonPrimario("+ Nueva Rutina");
        btnNueva.addActionListener(e -> abrirFormulario(null));
        header.add(titulo, BorderLayout.WEST);
        header.add(btnNueva, BorderLayout.EAST);

        String[] cols = {"ID", "Nombre", "Objetivo", "Nivel", "Semanas", "Entrenador", "Acciones"};
        modelo = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 6; }
        };
        tabla = new JTable(modelo);
        estilizarTabla();
        tabla.getColumnModel().getColumn(6).setCellRenderer(new BtnRenderer());
        tabla.getColumnModel().getColumn(6).setCellEditor(new BtnEditor());
        tabla.getColumnModel().getColumn(6).setPreferredWidth(170);
        tabla.getColumnModel().getColumn(0).setMaxWidth(45);
        tabla.getColumnModel().getColumn(4).setMaxWidth(70);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(EstilosGym.COLOR_PANEL);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 8));
        footer.setBackground(EstilosGym.COLOR_PANEL);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, EstilosGym.COLOR_BORDE));
        JLabel st = new JLabel("<html><font color='#9696a5'>Total rutinas: </font>"
                + "<font color='#ff5722'><b>" + dao.contarTotal() + "</b></font></html>");
        st.setFont(EstilosGym.FUENTE_NORMAL);
        footer.add(st);

        add(header, BorderLayout.NORTH);
        add(scroll,  BorderLayout.CENTER);
        add(footer,  BorderLayout.SOUTH);
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
                setHorizontalAlignment(col == 0 || col == 4 ? CENTER : LEFT);
                setBackground(s ? new Color(255, 87, 34, 60) : EstilosGym.COLOR_PANEL);
                setForeground(EstilosGym.COLOR_TEXTO);
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                if (col == 3) {
                    String val = v != null ? v.toString() : "";
                    setForeground("Avanzado".equals(val) ? EstilosGym.COLOR_PELIGRO
                            : "Intermedio".equals(val) ? new Color(255, 200, 0) : EstilosGym.COLOR_EXITO);
                    setFont(new Font("Segoe UI", Font.BOLD, 12));
                }
                return this;
            }
        };
        for (int i = 0; i < 6; i++) tabla.getColumnModel().getColumn(i).setCellRenderer(r);
    }

    public void cargarDatos() {
        modelo.setRowCount(0);
        for (Rutina r : dao.listarTodas()) {
            modelo.addRow(new Object[]{r.getIdRutina(), r.getNombreRutina(), r.getObjetivo(),
                r.getNivel(), r.getDuracionSemanas(),
                r.getNombreEntrenador() != null ? r.getNombreEntrenador() : "—",
                r.getIdRutina()});
        }
    }

    private void abrirFormulario(Rutina orig) {
        JDialog dlg = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this),
                orig == null ? "Nueva Rutina" : "Editar Rutina", true);
        dlg.setSize(540, 440);
        dlg.setLocationRelativeTo(this);

        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(EstilosGym.COLOR_PANEL);
        p.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        JTextField tfNombre = EstilosGym.crearCampoTexto();
        JTextField tfDur    = EstilosGym.crearCampoTexto();
        JTextArea  taDesc   = new JTextArea(3, 20);
        taDesc.setBackground(EstilosGym.COLOR_FONDO); taDesc.setForeground(EstilosGym.COLOR_TEXTO);
        taDesc.setFont(EstilosGym.FUENTE_NORMAL); taDesc.setLineWrap(true); taDesc.setWrapStyleWord(true);
        taDesc.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstilosGym.COLOR_BORDE),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));

        JComboBox<String> cmbObj  = new JComboBox<>(new String[]{
                "Ganancia muscular", "Pérdida de peso", "Resistencia", "Acondicionamiento", "Flexibilidad"});
        JComboBox<String> cmbNivel= new JComboBox<>(new String[]{"Principiante", "Intermedio", "Avanzado"});

        // Combo entrenadores desde BD
        List<Object[]> entrenadores = dao.listarEntrenadoresNombres();
        JComboBox<String> cmbEnt = new JComboBox<>();
        int[] idEntrenadores = new int[entrenadores.size()];
        for (int i = 0; i < entrenadores.size(); i++) {
            cmbEnt.addItem(entrenadores.get(i)[1].toString());
            idEntrenadores[i] = (int) entrenadores.get(i)[0];
        }

        for (JComboBox<?> c : new JComboBox[]{cmbObj, cmbNivel, cmbEnt}) {
            c.setBackground(EstilosGym.COLOR_FONDO); c.setForeground(EstilosGym.COLOR_TEXTO);
        }

        if (orig != null) {
            tfNombre.setText(orig.getNombreRutina());
            cmbObj.setSelectedItem(orig.getObjetivo());
            cmbNivel.setSelectedItem(orig.getNivel());
            tfDur.setText(String.valueOf(orig.getDuracionSemanas()));
            taDesc.setText(orig.getDescripcion());
            // Seleccionar entrenador correcto
            for (int i = 0; i < idEntrenadores.length; i++) {
                if (idEntrenadores[i] == orig.getIdEntrenador()) { cmbEnt.setSelectedIndex(i); break; }
            }
        }

        Object[][] filas = {{"Nombre *", tfNombre}, {"Objetivo", cmbObj}, {"Nivel", cmbNivel},
                            {"Duración (sem.)", tfDur}, {"Entrenador", cmbEnt}};
        for (int i = 0; i < filas.length; i++) {
            gbc.gridy = i; gbc.gridx = 0; gbc.weightx = 0.3;
            JLabel l = EstilosGym.crearEtiqueta(filas[i][0].toString());
            l.setHorizontalAlignment(SwingConstants.RIGHT); p.add(l, gbc);
            gbc.gridx = 1; gbc.weightx = 0.7; p.add((Component) filas[i][1], gbc);
        }
        gbc.gridy = filas.length; gbc.gridx = 0; gbc.weightx = 0.3;
        JLabel ld = EstilosGym.crearEtiqueta("Descripción"); ld.setHorizontalAlignment(SwingConstants.RIGHT);
        p.add(ld, gbc); gbc.gridx = 1; gbc.weightx = 0.7; p.add(new JScrollPane(taDesc), gbc);

        JLabel lblErr = new JLabel(""); lblErr.setForeground(EstilosGym.COLOR_PELIGRO);
        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 2; p.add(lblErr, gbc);

        JPanel bots = new JPanel(new FlowLayout(FlowLayout.RIGHT)); bots.setOpaque(false);
        JButton btnG = EstilosGym.crearBotonPrimario("Guardar");
        JButton btnC = EstilosGym.crearBotonSecundario("Cancelar");
        btnC.addActionListener(e -> dlg.dispose());
        btnG.addActionListener(e -> {
            if (tfNombre.getText().trim().isEmpty()) { lblErr.setText("⚠  Nombre obligatorio."); return; }
            int dur = 0;
            try { dur = Integer.parseInt(tfDur.getText().trim()); } catch (Exception ex) { lblErr.setText("⚠  Duración debe ser número."); return; }
            int idEnt = idEntrenadores.length > 0 ? idEntrenadores[cmbEnt.getSelectedIndex()] : 1;
            Rutina r = new Rutina(orig == null ? 0 : orig.getIdRutina(),
                    tfNombre.getText().trim(), cmbObj.getSelectedItem().toString(),
                    cmbNivel.getSelectedItem().toString(), dur, taDesc.getText(), idEnt);
            boolean ok = orig == null ? dao.insertar(r) >= 0 : dao.actualizar(r);
            if (!ok) { lblErr.setText("⚠  Error al guardar."); return; }
            dlg.dispose(); cargarDatos();
        });
        bots.add(btnC); bots.add(btnG);
        gbc.gridy++; p.add(bots, gbc);
        dlg.setContentPane(p); dlg.setVisible(true);
    }

    private void eliminar(int id) {
        int op = JOptionPane.showConfirmDialog(this, "¿Eliminar rutina #" + id + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (op == JOptionPane.YES_OPTION) { dao.eliminar(id); cargarDatos(); }
    }

    class BtnRenderer extends JPanel implements TableCellRenderer {
        BtnRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 4, 5)); setBackground(EstilosGym.COLOR_PANEL);
            JButton e = EstilosGym.crearBotonSecundario("✏ Editar"); JButton d = EstilosGym.crearBotonPeligro("🗑");
            e.setPreferredSize(new Dimension(82, 34)); d.setPreferredSize(new Dimension(46, 34));
            add(e); add(d);
        }
        @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
            setBackground(s ? new Color(255, 87, 34, 60) : EstilosGym.COLOR_PANEL); return this;
        }
    }
    class BtnEditor extends AbstractCellEditor implements TableCellEditor {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 5)); int id;
        BtnEditor() {
            panel.setBackground(EstilosGym.COLOR_PANEL);
            JButton e = EstilosGym.crearBotonSecundario("✏ Editar"); JButton d = EstilosGym.crearBotonPeligro("🗑");
            e.setPreferredSize(new Dimension(82, 34)); d.setPreferredSize(new Dimension(46, 34));
            e.addActionListener(ev -> {
                stopCellEditing();
                Rutina r = dao.listarTodas().stream().filter(x -> x.getIdRutina() == id).findFirst().orElse(null);
                abrirFormulario(r);
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
