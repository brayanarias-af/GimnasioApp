package Gimnasio.Vistas;

import Gimnasio.Controlador.ClienteDAO;
import Gimnasio.Modelo.Cliente;
import javax.swing.*;
import java.util.List; 
import javax.swing.table.*;
import java.awt.*;
import java.util.*;

public class ClientesVista extends TablaBase {

    private final ClienteDAO dao = new ClienteDAO();

    public ClientesVista() {
        super();
        construir();
        cargar(null);
    }

    private void construir() {
        add(crearHeader("👥  Gestión de Clientes", true, "+ Nuevo Cliente"), BorderLayout.NORTH);

        String[] cols = {"ID","Nombres","Apellidos","Cédula","Teléfono","EPS","Membresía","Objetivo","Acciones"};
        crearModelo(cols, 8);

        Map<String,Color> estados = new LinkedHashMap<>();
        estados.put("Activa", EstilosGym.COLOR_EXITO);
        estados.put("Vencida", EstilosGym.COLOR_PELIGRO);
        estados.put("Suspendida", new Color(255,200,0));
        aplicarRendererEstado(6, estados);

        tabla.getColumnModel().getColumn(8).setCellRenderer(new BtnRender());
        tabla.getColumnModel().getColumn(8).setCellEditor(new BtnEdit());
        tabla.getColumnModel().getColumn(8).setPreferredWidth(188);
        tabla.getColumnModel().getColumn(0).setMaxWidth(48);

        add(crearScroll(), BorderLayout.CENTER);
        add(crearFooter("Total clientes", String.valueOf(dao.contarTotal()),
                "Activos", String.valueOf(dao.contarActivos())), BorderLayout.SOUTH);
    }

    @Override protected void onBuscar(String f) { cargar(f); }
    @Override protected void onNuevo() { abrirForm(-1); }

    private void cargar(String filtro) {
        modelo.setRowCount(0);
        List<Cliente> lista = (filtro == null || filtro.isEmpty()) ? dao.listarTodos() : dao.buscar(filtro);
        for (Cliente c : lista)
            modelo.addRow(new Object[]{c.getIdCliente(),c.getNombres(),c.getApellidos(),
                c.getCedula(),c.getTelefono(),c.getEps(),c.getEstadoMembresia(),c.getObjetivo(),c.getIdCliente()});
    }

    private void abrirForm(int id) {
        new FormularioClienteVista((JFrame)SwingUtilities.getWindowAncestor(this), id, dao).setVisible(true);
        cargar(null);
    }

    private void eliminar(int id) {
        Cliente c = dao.buscarPorId(id);
        String nom = c != null ? c.getNombres()+" "+c.getApellidos() : "#"+id;
        if (JOptionPane.showConfirmDialog(this,"¿Eliminar a "+nom+"?\nSe eliminará también su usuario de acceso.",
                "Confirmar",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE)==JOptionPane.YES_OPTION) {
            dao.eliminar(id); cargar(null);
        }
    }

    class BtnRender extends JPanel implements TableCellRenderer {
        BtnRender() {
            setLayout(new FlowLayout(FlowLayout.CENTER,4,5)); setBackground(EstilosGym.COLOR_PANEL);
            JButton e=EstilosGym.crearBotonSecundario("✏ Editar"); JButton d=EstilosGym.crearBotonPeligro("🗑 Borrar");
            e.setPreferredSize(new Dimension(86,34)); d.setPreferredSize(new Dimension(86,34));
            add(e); add(d);
        }
        @Override public Component getTableCellRendererComponent(JTable t,Object v,boolean s,boolean f,int r,int c){
            setBackground(s?new Color(255,87,34,55):EstilosGym.COLOR_PANEL); return this; }
    }
    class BtnEdit extends AbstractCellEditor implements TableCellEditor {
        JPanel panel=new JPanel(new FlowLayout(FlowLayout.CENTER,4,5)); int id;
        BtnEdit(){
            panel.setBackground(EstilosGym.COLOR_PANEL);
            JButton e=EstilosGym.crearBotonSecundario("✏ Editar"); JButton d=EstilosGym.crearBotonPeligro("🗑 Borrar");
            e.setPreferredSize(new Dimension(86,34)); d.setPreferredSize(new Dimension(86,34));
            e.addActionListener(ev->{stopCellEditing();abrirForm(id);});
            d.addActionListener(ev->{stopCellEditing();eliminar(id);});
            panel.add(e); panel.add(d);
        }
        @Override public Component getTableCellEditorComponent(JTable t,Object v,boolean s,int r,int c){
            id=v instanceof Integer?(Integer)v:-1; return panel; }
        @Override public Object getCellEditorValue(){return id;}
    }
}
