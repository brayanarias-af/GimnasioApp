package Gimnasio.Vistas;

import Gimnasio.Controlador.PagoDAO;
import Gimnasio.Controlador.ClienteDAO;
import Gimnasio.Controlador.MembresiaDAO;
import Gimnasio.Modelo.Cliente;
import Gimnasio.Modelo.Pago;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.List;
import java.util.*;

public class PagosVista extends TablaBase {

    private final PagoDAO      dao    = new PagoDAO();
    private final ClienteDAO   cliDAO = new ClienteDAO();
    private final MembresiaDAO memDAO = new MembresiaDAO();

    public PagosVista() {
        super();
        construir();
        cargar();
    }

    private void construir() {
        add(crearHeader("💳  Pagos & Facturación", false, "+ Registrar Pago"), BorderLayout.NORTH);

        String[] cols = {"ID","Fecha","Cliente","Membresía","Monto","Método","Inicio","Fin","Estado","Acciones"};
        crearModelo(cols, 9);

        // Un solo renderer unificado: maneja monto (col 4) y estado (col 8) con colores
        DefaultTableCellRenderer uniRend = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean s, boolean f, int r, int c) {
                super.getTableCellRendererComponent(t, v, s, f, r, c);
                setBackground(s ? new Color(255,87,34,55) : EstilosGym.COLOR_PANEL);
                setForeground(EstilosGym.COLOR_TEXTO);
                setBorder(BorderFactory.createEmptyBorder(0,8,0,8));
                setFont(EstilosGym.FUENTE_NORMAL);
                setHorizontalAlignment(c == 4 ? RIGHT : LEFT);

                if (c == 4) {  // Monto: verde
                    setForeground(new Color(80,220,80));
                } else if (c == 8 && v != null) {  // Estado: color según valor
                    String est = v.toString();
                    setFont(new Font("Segoe UI", Font.BOLD, 12));
                    if ("Pagado".equalsIgnoreCase(est))      setForeground(EstilosGym.COLOR_EXITO);
                    else if ("Pendiente".equalsIgnoreCase(est)) setForeground(new Color(255,200,0));
                    else if ("Vencido".equalsIgnoreCase(est))   setForeground(new Color(255,140,0));
                    else                                         setForeground(EstilosGym.COLOR_PELIGRO);
                }
                return this;
            }
        };

        for (int i = 0; i < 9; i++) tabla.getColumnModel().getColumn(i).setCellRenderer(uniRend);
        tabla.getColumnModel().getColumn(9).setCellRenderer(new BtnRender());
        tabla.getColumnModel().getColumn(9).setCellEditor(new BtnEdit());
        tabla.getColumnModel().getColumn(9).setPreferredWidth(120);
        tabla.getColumnModel().getColumn(0).setMaxWidth(48);
        tabla.getColumnModel().getColumn(6).setPreferredWidth(90);
        tabla.getColumnModel().getColumn(7).setPreferredWidth(90);
        tabla.getColumnModel().getColumn(8).setPreferredWidth(90);

        add(crearScroll(), BorderLayout.CENTER);

        double total = dao.totalIngresos();
        add(crearFooter(
            "Total pagos",     String.valueOf(dao.contarTotal()),
            "Ingresos totales","$" + String.format("%,.0f", total)
        ), BorderLayout.SOUTH);
    }

    @Override protected void onNuevo() { abrirFormNuevo(); }

    private void cargar() {
        modelo.setRowCount(0);
        for (Pago p : dao.listarTodos()) {
            modelo.addRow(new Object[]{
                p.getIdPago(), p.getFechaPago(),
                p.getNombreCliente(), p.getNombreMembresia(),
                "$" + String.format("%,.0f", p.getMonto()),
                p.getMetodoPago(),
                p.getFechaInicio() != null ? p.getFechaInicio() : "—",
                p.getFechaFin()    != null ? p.getFechaFin()    : "—",
                p.getEstado(), p.getIdPago()
            });
        }
    }

    // ── FORM NUEVO PAGO ───────────────────────────────────────
    private void abrirFormNuevo() {
        JDialog dlg = dlgBase("Registrar Nuevo Pago", 560, 540);

        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(EstilosGym.COLOR_PANEL);
        p.setBorder(new EmptyBorder(22, 24, 22, 24));
        GridBagConstraints gbc = gbc();

        // ── Búsqueda por cédula ──────────────────────────────
        JTextField tfCedula  = EstilosGym.crearCampoTexto();
        tfCedula.setToolTipText("Ingresa el número de cédula del cliente");
        JLabel lblNombreCli  = new JLabel("—");
        lblNombreCli.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblNombreCli.setForeground(EstilosGym.COLOR_EXITO);

        int[] idClienteRef = {-1};

        JButton btnBuscar = EstilosGym.crearBotonSecundario("🔍 Buscar");
        btnBuscar.setPreferredSize(new Dimension(110, 36));
        btnBuscar.addActionListener(e -> {
            String ced = tfCedula.getText().trim();
            if (ced.isEmpty()) return;
            Cliente cli = cliDAO.buscarPorCedula(ced);
            if (cli == null) {
                lblNombreCli.setText("⚠ Cliente no encontrado");
                lblNombreCli.setForeground(EstilosGym.COLOR_PELIGRO);
                idClienteRef[0] = -1;
            } else {
                lblNombreCli.setText("✓ " + cli.getNombres() + " " + cli.getApellidos());
                lblNombreCli.setForeground(EstilosGym.COLOR_EXITO);
                idClienteRef[0] = cli.getIdCliente();
                // ¿Tiene pagos pendientes/vencidos?
                List<Pago> pendientes = dao.listarPendientesOVencidos(cli.getIdCliente());
                if (!pendientes.isEmpty()) {
                    dlg.dispose();
                    abrirFormCancelarPendiente(cli, pendientes);
                }
            }
        });

        // Panel búsqueda
        JPanel panBusq = new JPanel(new BorderLayout(6, 0)); panBusq.setOpaque(false);
        panBusq.add(tfCedula, BorderLayout.CENTER);
        panBusq.add(btnBuscar, BorderLayout.EAST);

        // ── Membresía ────────────────────────────────────────
        JComboBox<String> cmbMem = new JComboBox<>();
        List<Object[]> mems = memDAO.listarCombo();
        int[]    idsM  = new int[mems.size()];
        double[] precM = new double[mems.size()];
        int[]    diasM = new int[mems.size()];
        for (int i = 0; i < mems.size(); i++) {
            cmbMem.addItem(mems.get(i)[1] + " — $" + String.format("%,.0f",(double)mems.get(i)[2]));
            idsM[i]  = (int)mems.get(i)[0];
            precM[i] = (double)mems.get(i)[2];
            // Obtener duracion_dias de la membresía para calcular fecha_fin
            diasM[i] = obtenerDiasMem(idsM[i]);
        }
        estiloCmb(cmbMem);

        JTextField tfMonto = EstilosGym.crearCampoTexto();
        JTextField tfRef   = EstilosGym.crearCampoTexto();
        JComboBox<String> cmbMet = new JComboBox<>(
            new String[]{"Efectivo","Transferencia","Tarjeta débito","Tarjeta crédito","Nequi","Daviplata"});
        estiloCmb(cmbMet);
        JComboBox<String> cmbEst = new JComboBox<>(new String[]{"Pagado","Pendiente"});
        estiloCmb(cmbEst);

        // Date pickers
        DatePickerField dpFechaPago   = new DatePickerField(LocalDate.now().toString());
        DatePickerField dpFechaInicio = new DatePickerField(LocalDate.now().toString());
        DatePickerField dpFechaFin    = new DatePickerField();

        // Auto-calcular fecha_fin y monto al cambiar membresía/inicio
        Runnable recalcFin = () -> {
            int idx = cmbMem.getSelectedIndex();
            if (idx >= 0 && idx < diasM.length) {
                try {
                    LocalDate ini = LocalDate.parse(dpFechaInicio.getFecha());
                    dpFechaFin.setFecha(ini.plusDays(diasM[idx]).toString());
                } catch (Exception ignored) {}
            }
        };

        cmbMem.addActionListener(e -> {
            int idx = cmbMem.getSelectedIndex();
            if (idx >= 0 && idx < precM.length)
                tfMonto.setText(String.valueOf((int)precM[idx]));
            recalcFin.run();
        });
        dpFechaInicio.getTextField().addFocusListener(new FocusAdapter() {
            @Override public void focusLost(FocusEvent e) { recalcFin.run(); }
        });

        if (mems.size() > 0) {
            tfMonto.setText(String.valueOf((int)precM[0]));
            recalcFin.run();
        }

        Object[][] rows = {
            {"Cédula cliente *", panBusq},
            {"Nombre",           lblNombreCli},
            {"Membresía *",      cmbMem},
            {"Monto * ($)",      tfMonto},
            {"Método pago",      cmbMet},
            {"Referencia",       tfRef},
            {"Fecha pago *",     dpFechaPago},
            {"Fecha inicio *",   dpFechaInicio},
            {"Fecha fin *",      dpFechaFin},
            {"Estado",           cmbEst}
        };

        for (int i = 0; i < rows.length; i++) {
            gbc.gridy = i; gbc.gridx = 0; gbc.weightx = 0.32;
            JLabel lbl = EstilosGym.crearEtiqueta(rows[i][0].toString());
            lbl.setHorizontalAlignment(SwingConstants.RIGHT); p.add(lbl, gbc);
            gbc.gridx = 1; gbc.weightx = 0.68; p.add((Component)rows[i][1], gbc);
        }

        JLabel lblErr = new JLabel(""); lblErr.setForeground(EstilosGym.COLOR_PELIGRO);
        gbc.gridy = rows.length; gbc.gridx = 0; gbc.gridwidth = 2; p.add(lblErr, gbc);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0)); btns.setOpaque(false);
        JButton bc = EstilosGym.crearBotonSecundario("Cancelar");
        JButton bg = EstilosGym.crearBotonPrimario("✓  Registrar");
        bc.addActionListener(e -> dlg.dispose());
        bg.addActionListener(e -> {
            if (idClienteRef[0] < 0) { lblErr.setText("⚠ Busca un cliente por cédula primero."); return; }
            if (mems.isEmpty())      { lblErr.setText("⚠ No hay membresías."); return; }
            double monto;
            try { monto = Double.parseDouble(tfMonto.getText().trim()); }
            catch (NumberFormatException ex) { lblErr.setText("⚠ Monto inválido."); return; }
            if (dpFechaPago.getFecha().isEmpty() || dpFechaInicio.getFecha().isEmpty()
                || dpFechaFin.getFecha().isEmpty()) {
                lblErr.setText("⚠ Todas las fechas son obligatorias."); return;
            }
            Pago pg = new Pago();
            pg.setIdCliente(idClienteRef[0]);
            pg.setIdMembresia(idsM[cmbMem.getSelectedIndex()]);
            pg.setMonto(monto);
            pg.setMetodoPago(cmbMet.getSelectedItem().toString());
            pg.setReferenciaPago(tfRef.getText().trim());
            pg.setFechaPago(dpFechaPago.getFecha());
            pg.setFechaInicio(dpFechaInicio.getFecha());
            pg.setFechaFin(dpFechaFin.getFecha());
            pg.setEstado(cmbEst.getSelectedItem().toString());
            if (dao.insertar(pg) < 0) { lblErr.setText("⚠ Error al guardar."); return; }
            dlg.dispose(); cargar();
        });
        btns.add(bc); btns.add(bg);
        gbc.gridy++; p.add(btns, gbc);

        JScrollPane sc = new JScrollPane(p);
        sc.setBorder(BorderFactory.createEmptyBorder());
        sc.getViewport().setBackground(EstilosGym.COLOR_PANEL);
        dlg.setContentPane(sc);
        dlg.setVisible(true);
    }

    // ── CANCELAR PAGO PENDIENTE/VENCIDO ───────────────────────
    private void abrirFormCancelarPendiente(Cliente cli, List<Pago> pendientes) {
        JDialog dlg = dlgBase("💳 Pagos Pendientes — " + cli.getNombres() + " " + cli.getApellidos(), 600, 560);

        JPanel root = new JPanel(new BorderLayout(0, 12));
        root.setBackground(EstilosGym.COLOR_PANEL);
        root.setBorder(new EmptyBorder(18, 22, 18, 22));

        // Aviso
        JLabel aviso = new JLabel("<html><b>⚠ Este cliente tiene pagos pendientes o vencidos.</b><br>"
            + "Selecciona el pago que deseas cancelar y completa el formulario.</html>");
        aviso.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        aviso.setForeground(new Color(255, 215, 60));
        aviso.setBackground(new Color(70, 50, 0));
        aviso.setOpaque(true);
        aviso.setBorder(new EmptyBorder(10, 14, 10, 14));
        root.add(aviso, BorderLayout.NORTH);

        // Tabla pagos pendientes
        String[] cols = {"ID","Membresía","Monto","Estado","Inicio","Fin"};
        DefaultTableModel mod = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        for (Pago pg : pendientes) {
            mod.addRow(new Object[]{
                pg.getIdPago(), pg.getNombreMembresia(),
                "$" + String.format("%,.0f", pg.getMonto()), pg.getEstado(),
                pg.getFechaInicio() != null ? pg.getFechaInicio() : "—",
                pg.getFechaFin()    != null ? pg.getFechaFin()    : "—"
            });
        }
        JTable tbl = new JTable(mod);
        tbl.setBackground(EstilosGym.COLOR_PANEL); tbl.setForeground(EstilosGym.COLOR_TEXTO);
        tbl.setGridColor(EstilosGym.COLOR_BORDE); tbl.setRowHeight(38);
        tbl.setFont(EstilosGym.FUENTE_NORMAL); tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbl.setSelectionBackground(new Color(255,87,34,80));
        tbl.getTableHeader().setBackground(new Color(14,14,22));
        tbl.getTableHeader().setForeground(EstilosGym.COLOR_TEXTO_GRIS);
        tbl.getTableHeader().setFont(new Font("Segoe UI",Font.BOLD,11));
        JScrollPane scTbl = new JScrollPane(tbl);
        scTbl.setBorder(BorderFactory.createLineBorder(EstilosGym.COLOR_BORDE));
        scTbl.setPreferredSize(new Dimension(0, 150));

        // Form pago
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(EstilosGym.COLOR_PANEL);
        GridBagConstraints gbc = gbc();

        JComboBox<String> cmbMet = new JComboBox<>(
            new String[]{"Efectivo","Transferencia","Tarjeta débito","Tarjeta crédito","Nequi","Daviplata"});
        estiloCmb(cmbMet);
        JTextField tfRef = EstilosGym.crearCampoTexto();
        DatePickerField dpFechaPago   = new DatePickerField(LocalDate.now().toString());
        DatePickerField dpFechaInicio = new DatePickerField(LocalDate.now().toString());
        DatePickerField dpFechaFin    = new DatePickerField();

        // Auto-fill fechas del pago seleccionado
        tbl.getSelectionModel().addListSelectionListener(e -> {
            int row = tbl.getSelectedRow();
            if (row >= 0 && row < pendientes.size()) {
                Pago sel = pendientes.get(row);
                if (sel.getFechaInicio() != null && !sel.getFechaInicio().equals("—"))
                    dpFechaInicio.setFecha(sel.getFechaInicio());
                if (sel.getFechaFin() != null && !sel.getFechaFin().equals("—"))
                    dpFechaFin.setFecha(sel.getFechaFin());
            }
        });
        if (!pendientes.isEmpty()) tbl.setRowSelectionInterval(0, 0);

        Object[][] rows2 = {
            {"Método pago",   cmbMet},
            {"Referencia",    tfRef},
            {"Fecha pago *",  dpFechaPago},
            {"Fecha inicio *",dpFechaInicio},
            {"Fecha fin *",   dpFechaFin}
        };
        for (int i = 0; i < rows2.length; i++) {
            gbc.gridy = i; gbc.gridx = 0; gbc.weightx = 0.35;
            JLabel l = EstilosGym.crearEtiqueta(rows2[i][0].toString());
            l.setHorizontalAlignment(SwingConstants.RIGHT); form.add(l, gbc);
            gbc.gridx = 1; gbc.weightx = 0.65; form.add((Component)rows2[i][1], gbc);
        }

        JLabel lblErr = new JLabel(""); lblErr.setForeground(EstilosGym.COLOR_PELIGRO);
        gbc.gridy = rows2.length; gbc.gridx = 0; gbc.gridwidth = 2; form.add(lblErr, gbc);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0)); btns.setOpaque(false);
        JButton bCan  = EstilosGym.crearBotonSecundario("Cancelar");
        JButton bPagar = EstilosGym.crearBotonPrimario("✓ Registrar Pago");
        bCan.addActionListener(e  -> dlg.dispose());
        bPagar.addActionListener(e -> {
            int row = tbl.getSelectedRow();
            if (row < 0) { lblErr.setText("⚠ Selecciona un pago de la lista."); return; }
            if (dpFechaPago.getFecha().isEmpty() || dpFechaInicio.getFecha().isEmpty()
                || dpFechaFin.getFecha().isEmpty()) {
                lblErr.setText("⚠ Todas las fechas son obligatorias."); return;
            }
            int idPago = pendientes.get(row).getIdPago();
            boolean ok = dao.cancelarPago(idPago,
                cmbMet.getSelectedItem().toString(), tfRef.getText().trim(),
                dpFechaPago.getFecha(), dpFechaInicio.getFecha(), dpFechaFin.getFecha());
            if (!ok) { lblErr.setText("⚠ Error al actualizar."); return; }
            JOptionPane.showMessageDialog(dlg,
                "✅ Pago registrado exitosamente.\nMembresía actualizada a Activa.",
                "Pago exitoso", JOptionPane.INFORMATION_MESSAGE);
            dlg.dispose(); cargar();
        });
        btns.add(bCan); btns.add(bPagar);
        gbc.gridy++; form.add(btns, gbc);

        JPanel centro = new JPanel(new BorderLayout(0, 10)); centro.setOpaque(false);
        centro.add(scTbl, BorderLayout.NORTH);
        centro.add(form, BorderLayout.CENTER);
        root.add(centro, BorderLayout.CENTER);

        dlg.setContentPane(root);
        dlg.setVisible(true);
    }

    private void eliminar(int id) {
        if (JOptionPane.showConfirmDialog(this, "¿Anular pago #" + id + "?", "Confirmar",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            dao.eliminar(id); cargar();
        }
    }

    // ── Helpers ───────────────────────────────────────────────
    private JDialog dlgBase(String titulo, int w, int h) {
        JDialog d = new JDialog((JFrame)SwingUtilities.getWindowAncestor(this), titulo, true);
        d.setSize(w, h); d.setLocationRelativeTo(this);
        return d;
    }

    private GridBagConstraints gbc() {
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6,6,6,6); g.fill = GridBagConstraints.HORIZONTAL;
        return g;
    }

    private void estiloCmb(JComboBox<?> c) {
        c.setBackground(EstilosGym.COLOR_FONDO); c.setForeground(EstilosGym.COLOR_TEXTO);
        c.setFont(EstilosGym.FUENTE_NORMAL);
    }

    private int obtenerDiasMem(int idMem) {
        try (var ps = Gimnasio.Conexion.Conexion.getConexion().prepareStatement(
                "SELECT duracion_dias FROM membresias WHERE id_membresia=?")) {
            ps.setInt(1, idMem);
            var rs = ps.executeQuery();
            if (rs.next()) { int d = rs.getInt(1); rs.close(); return d; }
            rs.close();
        } catch (Exception e) {}
        return 30;
    }

    // ── Botones tabla ─────────────────────────────────────────
    class BtnRender extends JPanel implements TableCellRenderer {
        BtnRender() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 4, 6));
            setBackground(EstilosGym.COLOR_PANEL);
            JButton d = EstilosGym.crearBotonPeligro("🗑 Anular");
            d.setPreferredSize(new Dimension(100,34)); add(d);
        }
        @Override public Component getTableCellRendererComponent(
            JTable t, Object v, boolean s, boolean f, int r, int c) {
            setBackground(s ? new Color(255,87,34,55) : EstilosGym.COLOR_PANEL); return this;
        }
    }

    class BtnEdit extends AbstractCellEditor implements TableCellEditor {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER,4,6));
        int id;
        BtnEdit() {
            panel.setBackground(EstilosGym.COLOR_PANEL);
            JButton d = EstilosGym.crearBotonPeligro("🗑 Anular");
            d.setPreferredSize(new Dimension(100, 34));
            d.addActionListener(ev -> { stopCellEditing(); eliminar(id); });
            panel.add(d);
        }
        @Override public Component getTableCellEditorComponent(
            JTable t, Object v, boolean s, int r, int c) {
            id = v instanceof Integer ? (Integer)v : -1; return panel;
        }
        @Override public Object getCellEditorValue() { return id; }
    }
}
