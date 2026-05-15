package Gimnasio.Vistas;

import Gimnasio.Controlador.ClienteDAO;
import Gimnasio.Modelo.Cliente;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class ClientesVista extends JPanel {

    private final ClienteDAO dao = new ClienteDAO();
    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtBuscar;

    public ClientesVista() {
        setLayout(new BorderLayout());
        setBackground(EstilosGym.COLOR_FONDO);
        construirUI();
        cargarDatos(null);
    }

    private void construirUI() {
        // ── HEADER ──
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(EstilosGym.COLOR_FONDO);
        header.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 25));

        JLabel titulo = new JLabel("👥  Gestión de Clientes");
        titulo.setFont(EstilosGym.FUENTE_TITULO);
        titulo.setForeground(EstilosGym.COLOR_TEXTO);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        acciones.setOpaque(false);
        txtBuscar = EstilosGym.crearCampoTexto();
        txtBuscar.setPreferredSize(new Dimension(200, 36));

        JButton btnBuscar = EstilosGym.crearBotonSecundario("🔍 Buscar");
        JButton btnNuevo  = EstilosGym.crearBotonPrimario("+ Nuevo Cliente");

        btnBuscar.addActionListener(e -> cargarDatos(txtBuscar.getText().trim()));
        btnNuevo.addActionListener(e  -> abrirFormulario(-1));
        txtBuscar.addActionListener(e -> cargarDatos(txtBuscar.getText().trim()));

        acciones.add(txtBuscar); acciones.add(btnBuscar); acciones.add(btnNuevo);
        header.add(titulo, BorderLayout.WEST);
        header.add(acciones, BorderLayout.EAST);

        // ── TABLA ──
        String[] cols = {"ID", "Nombres", "Apellidos", "Cédula", "Teléfono",
                         "EPS", "Membresía", "Objetivo", "Acciones"};
        modelo = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 8; }
            @Override public Class<?> getColumnClass(int c) {
                return c == 0 ? Integer.class : String.class;
            }
        };
        tabla = new JTable(modelo);
        estilizarTabla();

        tabla.getColumnModel().getColumn(8).setCellRenderer(new AccionRenderer());
        tabla.getColumnModel().getColumn(8).setCellEditor(new AccionEditor());
        tabla.getColumnModel().getColumn(8).setPreferredWidth(185);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(EstilosGym.COLOR_PANEL);

        // ── FOOTER stats ──
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 8));
        footer.setBackground(EstilosGym.COLOR_PANEL);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, EstilosGym.COLOR_BORDE));
        footer.add(stat("Total clientes",       String.valueOf(dao.contarTotal())));
        footer.add(new JSeparator(JSeparator.VERTICAL) {{ setPreferredSize(new Dimension(1,28)); setForeground(EstilosGym.COLOR_BORDE); }});
        footer.add(stat("Membresías activas",   String.valueOf(dao.contarActivos())));

        add(header, BorderLayout.NORTH);
        add(scroll,  BorderLayout.CENTER);
        add(footer,  BorderLayout.SOUTH);
    }

    private JLabel stat(String e, String v) {
        JLabel l = new JLabel("<html><font color='#9696a5'>" + e + ": </font><font color='#ff5722'><b>" + v + "</b></font></html>");
        l.setFont(EstilosGym.FUENTE_NORMAL);
        return l;
    }

    private void estilizarTabla() {
        tabla.setBackground(EstilosGym.COLOR_PANEL);
        tabla.setForeground(EstilosGym.COLOR_TEXTO);
        tabla.setSelectionBackground(new Color(255, 87, 34, 60));
        tabla.setSelectionForeground(EstilosGym.COLOR_TEXTO);
        tabla.setGridColor(EstilosGym.COLOR_BORDE);
        tabla.setRowHeight(46);
        tabla.setFont(EstilosGym.FUENTE_NORMAL);
        tabla.setShowHorizontalLines(true);
        tabla.setShowVerticalLines(false);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JTableHeader th = tabla.getTableHeader();
        th.setBackground(new Color(18, 18, 26));
        th.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
        th.setFont(new Font("Segoe UI", Font.BOLD, 11));
        th.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, EstilosGym.COLOR_BORDE));
        th.setReorderingAllowed(false);

        DefaultTableCellRenderer render = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setHorizontalAlignment(col == 0 ? CENTER : LEFT);
                setBackground(sel ? new Color(255, 87, 34, 60) : EstilosGym.COLOR_PANEL);
                setForeground(EstilosGym.COLOR_TEXTO);
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                // Membresía en color
                if (col == 6) {
                    String val = v != null ? v.toString() : "";
                    setForeground("Activa".equalsIgnoreCase(val) ? EstilosGym.COLOR_EXITO : EstilosGym.COLOR_PELIGRO);
                    setFont(new Font("Segoe UI", Font.BOLD, 12));
                }
                return this;
            }
        };
        for (int i = 0; i < 8; i++) tabla.getColumnModel().getColumn(i).setCellRenderer(render);
    }

    public void cargarDatos(String filtro) {
        modelo.setRowCount(0);
        List<Cliente> lista = (filtro == null || filtro.isEmpty())
                ? dao.listarTodos() : dao.buscar(filtro);
        for (Cliente c : lista) {
            modelo.addRow(new Object[]{
                c.getIdCliente(), c.getNombres(), c.getApellidos(),
                c.getCedula(), c.getTelefono(), c.getEps(),
                c.getEstadoMembresia(), c.getObjetivo(), c.getIdCliente()
            });
        }
    }

    private void abrirFormulario(int idEditar) {
        FormularioClienteVista form = new FormularioClienteVista(
                (JFrame) SwingUtilities.getWindowAncestor(this), idEditar, dao);
        form.setVisible(true);
        cargarDatos(null);
    }

    private void eliminarCliente(int id) {
        Cliente c = dao.buscarPorId(id);
        String nombre = c != null ? c.getNombres() + " " + c.getApellidos() : String.valueOf(id);
        int op = JOptionPane.showConfirmDialog(this,
                "¿Eliminar al cliente " + nombre + "?\nEsta acción también eliminará su usuario de acceso.",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (op == JOptionPane.YES_OPTION) {
            if (dao.eliminar(id)) {
                JOptionPane.showMessageDialog(this, "Cliente eliminado correctamente.", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar. Puede tener registros relacionados.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            cargarDatos(null);
        }
    }

    // ── RENDERER botones Acciones ──
    class AccionRenderer extends JPanel implements TableCellRenderer {
        AccionRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 4, 5));
            setBackground(EstilosGym.COLOR_PANEL);
            JButton e = EstilosGym.crearBotonSecundario("✏ Editar");
            JButton d = EstilosGym.crearBotonPeligro("🗑 Eliminar");
            e.setPreferredSize(new Dimension(88, 34));
            d.setPreferredSize(new Dimension(88, 34));
            add(e); add(d);
        }
        @Override public Component getTableCellRendererComponent(
                JTable t, Object v, boolean s, boolean f, int r, int c) {
            setBackground(s ? new Color(255, 87, 34, 60) : EstilosGym.COLOR_PANEL);
            return this;
        }
    }

    // ── EDITOR botones Acciones ──
    class AccionEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 5));
        private int idActual = -1;

        AccionEditor() {
            panel.setBackground(EstilosGym.COLOR_PANEL);
            JButton btnEdit = EstilosGym.crearBotonSecundario("✏ Editar");
            JButton btnDel  = EstilosGym.crearBotonPeligro("🗑 Eliminar");
            btnEdit.setPreferredSize(new Dimension(88, 34));
            btnDel.setPreferredSize(new Dimension(88, 34));
            btnEdit.addActionListener(e -> { stopCellEditing(); abrirFormulario(idActual); });
            btnDel.addActionListener(e  -> { stopCellEditing(); eliminarCliente(idActual); });
            panel.add(btnEdit); panel.add(btnDel);
        }

        @Override public Component getTableCellEditorComponent(
                JTable t, Object v, boolean s, int r, int c) {
            idActual = v instanceof Integer ? (Integer) v : -1;
            return panel;
        }
        @Override public Object getCellEditorValue() { return idActual; }
    }
}
