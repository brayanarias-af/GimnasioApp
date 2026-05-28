package Gimnasio.Vistas;

import Gimnasio.Controlador.EjercicioDAO;
import Gimnasio.Controlador.RutinaDAO;
import Gimnasio.Modelo.Ejercicio;
import Gimnasio.Modelo.Rutina;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class RutinasVista extends JPanel {

    private final RutinaDAO    rutinaDAO = new RutinaDAO();
    private final EjercicioDAO ejDAO     = new EjercicioDAO();
    private DefaultListModel<Rutina> listModel = new DefaultListModel<>();
    private JList<Rutina>  listaRutinas;
    private Rutina         rutinaSeleccionada;
    private JPanel         panelSemana;
    private Map<String, DefaultListModel<Ejercicio>> modelosDias = new LinkedHashMap<>();

    public RutinasVista() {
        setLayout(new BorderLayout());
        setBackground(EstilosGym.COLOR_FONDO);
        construir();
        cargarRutinas();
    }

    private void construir() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(EstilosGym.COLOR_FONDO);
        header.setBorder(new EmptyBorder(18,24,12,24));
        JLabel titulo = new JLabel("📋  Gestión de Rutinas");
        titulo.setFont(EstilosGym.FUENTE_TITULO); titulo.setForeground(EstilosGym.COLOR_TEXTO);
        JPanel acc = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0)); acc.setOpaque(false);
        JButton btnN=EstilosGym.crearBotonPrimario("+ Nueva Rutina");
        JButton btnE=EstilosGym.crearBotonSecundario("✏ Editar");
        JButton btnD=EstilosGym.crearBotonPeligro("🗑 Eliminar");
        btnN.addActionListener(e->abrirFormRutina(null));
        btnE.addActionListener(e->{if(rutinaSeleccionada!=null)abrirFormRutina(rutinaSeleccionada);});
        btnD.addActionListener(e->eliminarRutina());
        acc.add(btnE); acc.add(btnD); acc.add(btnN);
        header.add(titulo,BorderLayout.WEST); header.add(acc,BorderLayout.EAST);

        JSplitPane split=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerLocation(260); split.setDividerSize(4);
        split.setBorder(null); split.setBackground(EstilosGym.COLOR_FONDO);

        // Panel izquierdo
        JPanel pIzq=new JPanel(new BorderLayout()); pIzq.setBackground(EstilosGym.COLOR_PANEL);
        pIzq.setBorder(BorderFactory.createMatteBorder(0,0,0,1,EstilosGym.COLOR_BORDE));
        JLabel lblL=new JLabel("  Rutinas disponibles");
        lblL.setFont(new Font("Segoe UI",Font.BOLD,12)); lblL.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
        lblL.setBorder(new EmptyBorder(10,10,8,10)); lblL.setBackground(new Color(14,14,20)); lblL.setOpaque(true);

        listaRutinas=new JList<>(listModel);
        listaRutinas.setBackground(EstilosGym.COLOR_PANEL); listaRutinas.setForeground(EstilosGym.COLOR_TEXTO);
        listaRutinas.setFont(EstilosGym.FUENTE_NORMAL); listaRutinas.setFixedCellHeight(54);
        listaRutinas.setSelectionBackground(new Color(255,87,34,80));
        listaRutinas.setBorder(new EmptyBorder(4,0,4,0));
        listaRutinas.setCellRenderer(new RutinaCellRenderer());
        listaRutinas.addListSelectionListener(e->{
            if(!e.getValueIsAdjusting()){
                rutinaSeleccionada=listaRutinas.getSelectedValue();
                if(rutinaSeleccionada!=null) mostrarSemana(rutinaSeleccionada.getIdRutina());
            }
        });
        JScrollPane scL=new JScrollPane(listaRutinas); scL.setBorder(BorderFactory.createEmptyBorder());
        scL.getViewport().setBackground(EstilosGym.COLOR_PANEL);
        pIzq.add(lblL,BorderLayout.NORTH); pIzq.add(scL,BorderLayout.CENTER);

        panelSemana=new JPanel(new BorderLayout()); panelSemana.setBackground(EstilosGym.COLOR_FONDO);
        mostrarPlaceholder();

        split.setLeftComponent(pIzq); split.setRightComponent(panelSemana);
        add(header,BorderLayout.NORTH); add(split,BorderLayout.CENTER);
    }

    private void mostrarPlaceholder() {
        panelSemana.removeAll();
        JLabel m=new JLabel("← Selecciona una rutina para ver y editar su plan semanal",JLabel.CENTER);
        m.setFont(new Font("Segoe UI",Font.ITALIC,14)); m.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
        panelSemana.add(m,BorderLayout.CENTER); panelSemana.revalidate(); panelSemana.repaint();
    }

    private void cargarRutinas() {
        listModel.clear();
        for(Rutina r:rutinaDAO.listarTodas()) listModel.addElement(r);
        if(!listModel.isEmpty()) listaRutinas.setSelectedIndex(0);
        else mostrarPlaceholder();
    }

    private void mostrarSemana(int idRutina) {
        panelSemana.removeAll(); modelosDias.clear();
        Map<String,List<Ejercicio>> porDia=ejDAO.obtenerPorDia(idRutina);

        JPanel top=new JPanel(new BorderLayout()); top.setBackground(EstilosGym.COLOR_FONDO);
        top.setBorder(new EmptyBorder(12,16,8,16));
        JLabel lN=new JLabel("📅  Plan Semanal — "+rutinaSeleccionada.getNombreRutina());
        lN.setFont(new Font("Segoe UI",Font.BOLD,15)); lN.setForeground(EstilosGym.COLOR_TEXTO);
        JLabel lS=new JLabel(rutinaSeleccionada.getNivel()+"  ·  "+rutinaSeleccionada.getDuracionSemanas()+" semanas  ·  "+rutinaSeleccionada.getObjetivo());
        lS.setFont(EstilosGym.FUENTE_PEQUEÑA); lS.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
        JPanel tx=new JPanel(new GridLayout(2,1,0,2)); tx.setOpaque(false); tx.add(lN); tx.add(lS);
        top.add(tx,BorderLayout.WEST);

        JPanel gridDias=new JPanel(new GridLayout(1,7,8,0));
        gridDias.setBackground(EstilosGym.COLOR_FONDO); gridDias.setBorder(new EmptyBorder(4,12,12,12));
        Color[] cols={new Color(100,60,200),new Color(255,160,0),new Color(0,180,200),
                      EstilosGym.COLOR_ACENTO,new Color(80,200,120),new Color(200,80,200),new Color(100,160,255)};
        int ci=0;
        for(String dia:EjercicioDAO.DIAS){
            List<Ejercicio> ejs=porDia.getOrDefault(dia,new ArrayList<>());
            DefaultListModel<Ejercicio> mod=new DefaultListModel<>();
            for(Ejercicio ej:ejs) mod.addElement(ej);
            modelosDias.put(dia,mod);
            gridDias.add(crearColumnaDia(dia,mod,cols[ci%cols.length],idRutina)); ci++;
        }
        JScrollPane sc=new JScrollPane(gridDias); sc.setBorder(BorderFactory.createEmptyBorder());
        sc.getViewport().setBackground(EstilosGym.COLOR_FONDO);
        sc.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panelSemana.add(top,BorderLayout.NORTH); panelSemana.add(sc,BorderLayout.CENTER);
        panelSemana.revalidate(); panelSemana.repaint();
    }

    private JPanel crearColumnaDia(String dia, DefaultListModel<Ejercicio> modelo, Color color, int idRutina) {
        JPanel col=new JPanel(new BorderLayout(0,4));
        col.setBackground(EstilosGym.COLOR_PANEL);
        col.setBorder(BorderFactory.createLineBorder(EstilosGym.COLOR_BORDE,1));

        JPanel cab=new JPanel(null){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color); g2.fillRect(0,0,getWidth(),getHeight()); g2.dispose(); super.paintComponent(g);
            }
        };
        cab.setPreferredSize(new Dimension(0,36)); cab.setOpaque(false);
        JLabel ld=new JLabel(dia,JLabel.CENTER); ld.setFont(new Font("Segoe UI",Font.BOLD,12));
        ld.setForeground(Color.WHITE); ld.setBounds(0,0,300,36); cab.add(ld);

        JList<Ejercicio> lista=new JList<>(modelo);
        lista.setBackground(EstilosGym.COLOR_PANEL); lista.setForeground(EstilosGym.COLOR_TEXTO);
        lista.setFont(new Font("Segoe UI",Font.PLAIN,11)); lista.setFixedCellHeight(50);
        lista.setSelectionBackground(new Color(255,87,34,60));
        lista.setCellRenderer(new EjercicioCellRenderer(color));

        JLabel cnt=new JLabel("0",JLabel.CENTER); cnt.setFont(EstilosGym.FUENTE_PEQUEÑA);
        cnt.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
        Runnable upd=()->cnt.setText(modelo.size()+(modelo.size()==1?" ejercicio":" ejercicios"));
        modelo.addListDataListener(new javax.swing.event.ListDataListener(){
            public void intervalAdded(javax.swing.event.ListDataEvent e){upd.run();}
            public void intervalRemoved(javax.swing.event.ListDataEvent e){upd.run();}
            public void contentsChanged(javax.swing.event.ListDataEvent e){upd.run();}
        });
        upd.run();

        JScrollPane sc=new JScrollPane(lista); sc.setBorder(BorderFactory.createEmptyBorder());
        sc.getViewport().setBackground(EstilosGym.COLOR_PANEL);
        JPanel center=new JPanel(new BorderLayout()); center.setBackground(EstilosGym.COLOR_PANEL);
        center.add(cnt,BorderLayout.NORTH); center.add(sc,BorderLayout.CENTER);

        JPanel foot=new JPanel(new FlowLayout(FlowLayout.CENTER,4,4));
        foot.setBackground(new Color(16,16,22));
        foot.setBorder(BorderFactory.createMatteBorder(1,0,0,0,EstilosGym.COLOR_BORDE));
        JButton btnA=new JButton("+"); estilizarBtnDia(btnA,color); btnA.setToolTipText("Agregar ejercicio a "+dia);
        JButton btnD=new JButton("−"); estilizarBtnDia(btnD,EstilosGym.COLOR_PELIGRO); btnD.setToolTipText("Quitar seleccionado");
        btnA.addActionListener(e->agregarEjercicioADia(idRutina,dia,modelo));
        btnD.addActionListener(e->{
            Ejercicio sel=lista.getSelectedValue();
            if(sel==null){JOptionPane.showMessageDialog(this,"Selecciona un ejercicio.","Aviso",JOptionPane.WARNING_MESSAGE);return;}
            ejDAO.quitarDeRutina(sel.getIdRutinaEjercicio()); modelo.removeElement(sel);
        });
        foot.add(btnA); foot.add(btnD);

        col.add(cab,BorderLayout.NORTH); col.add(center,BorderLayout.CENTER); col.add(foot,BorderLayout.SOUTH);
        return col;
    }

    private void estilizarBtnDia(JButton b, Color c){
        b.setFont(new Font("Segoe UI",Font.BOLD,15)); b.setForeground(Color.WHITE); b.setBackground(c);
        b.setBorder(BorderFactory.createEmptyBorder(3,12,3,12));
        b.setFocusPainted(false); b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void agregarEjercicioADia(int idRutina, String dia, DefaultListModel<Ejercicio> modelo) {
        List<Object[]> ejercicios=ejDAO.listarParaCombo();
        if(ejercicios.isEmpty()){JOptionPane.showMessageDialog(this,"No hay ejercicios en la BD.","Aviso",JOptionPane.WARNING_MESSAGE);return;}
        JComboBox<String> cmb=new JComboBox<>(); int[] ids=new int[ejercicios.size()];
        for(int i=0;i<ejercicios.size();i++){cmb.addItem("["+ejercicios.get(i)[2]+"] "+ejercicios.get(i)[1]);ids[i]=(int)ejercicios.get(i)[0];}
        cmb.setBackground(EstilosGym.COLOR_FONDO); cmb.setForeground(EstilosGym.COLOR_TEXTO); cmb.setFont(EstilosGym.FUENTE_NORMAL);
        JPanel dlgP=new JPanel(new GridBagLayout()); dlgP.setBackground(EstilosGym.COLOR_PANEL); dlgP.setBorder(new EmptyBorder(16,18,16,18));
        GridBagConstraints g=new GridBagConstraints(); g.insets=new Insets(6,6,6,6); g.fill=GridBagConstraints.HORIZONTAL;
        g.gridx=0;g.gridy=0;g.weightx=0.3; JLabel lbl=EstilosGym.crearEtiqueta("Ejercicio *"); lbl.setHorizontalAlignment(SwingConstants.RIGHT); dlgP.add(lbl,g);
        g.gridx=1;g.weightx=0.7; dlgP.add(cmb,g);
        g.gridy=1;g.gridx=0;g.gridwidth=2;
        JButton btnNuevoEj=EstilosGym.crearBotonSecundario("+ Crear nuevo ejercicio");
        btnNuevoEj.addActionListener(e->crearNuevoEjercicio()); dlgP.add(btnNuevoEj,g);
        int res=JOptionPane.showConfirmDialog(this,dlgP,"Agregar a "+dia,JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
        if(res==JOptionPane.OK_OPTION){
            int idEj=ids[cmb.getSelectedIndex()];
            if(ejDAO.agregarADia(idRutina,idEj,dia)){
                Map<String,List<Ejercicio>> fresh=ejDAO.obtenerPorDia(idRutina);
                modelo.clear(); for(Ejercicio ej:fresh.getOrDefault(dia,List.of())) modelo.addElement(ej);
            }
        }
    }

    private void crearNuevoEjercicio(){
        JDialog dlg=new JDialog((JFrame)SwingUtilities.getWindowAncestor(this),"Nuevo Ejercicio",true);
        dlg.setSize(420,310); dlg.setLocationRelativeTo(this);
        JPanel p=new JPanel(new GridBagLayout()); p.setBackground(EstilosGym.COLOR_PANEL); p.setBorder(new EmptyBorder(16,20,16,20));
        GridBagConstraints g=new GridBagConstraints(); g.insets=new Insets(5,5,5,5); g.fill=GridBagConstraints.HORIZONTAL;
        JTextField tfN=EstilosGym.crearCampoTexto(),tfS=EstilosGym.crearCampoTexto(),tfR=EstilosGym.crearCampoTexto(),tfD=EstilosGym.crearCampoTexto();
        JComboBox<String> cmbG=new JComboBox<>(new String[]{"Pecho","Espalda","Piernas","Hombros","Bíceps","Tríceps","Core","Cardio","Full Body"});
        cmbG.setBackground(EstilosGym.COLOR_FONDO); cmbG.setForeground(EstilosGym.COLOR_TEXTO);
        Object[][] rows={{"Nombre *",tfN},{"Grupo",cmbG},{"Series",tfS},{"Repeticiones",tfR},{"Descanso (seg)",tfD}};
        for(int i=0;i<rows.length;i++){
            g.gridy=i;g.gridx=0;g.weightx=0.38; JLabel l=EstilosGym.crearEtiqueta(rows[i][0].toString()); l.setHorizontalAlignment(SwingConstants.RIGHT); p.add(l,g);
            g.gridx=1;g.weightx=0.62; p.add((Component)rows[i][1],g);
        }
        JLabel err=new JLabel(""); err.setForeground(EstilosGym.COLOR_PELIGRO);
        g.gridy=rows.length;g.gridx=0;g.gridwidth=2; p.add(err,g);
        JPanel btns=new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0)); btns.setOpaque(false);
        JButton bc=EstilosGym.crearBotonSecundario("Cancelar"),bg=EstilosGym.crearBotonPrimario("Guardar");
        bc.addActionListener(e->dlg.dispose());
        bg.addActionListener(e->{
            if(tfN.getText().trim().isEmpty()){err.setText("⚠ Nombre obligatorio.");return;}
            Ejercicio ej=new Ejercicio(); ej.setNombre(tfN.getText().trim()); ej.setGrupoMuscular(cmbG.getSelectedItem().toString());
            try{ej.setSeries(tfS.getText().isEmpty()?3:Integer.parseInt(tfS.getText()));}catch(Exception ex){ej.setSeries(3);}
            try{ej.setRepeticiones(tfR.getText().isEmpty()?12:Integer.parseInt(tfR.getText()));}catch(Exception ex){ej.setRepeticiones(12);}
            try{ej.setDescansoSegundos(tfD.getText().isEmpty()?60:Integer.parseInt(tfD.getText()));}catch(Exception ex){ej.setDescansoSegundos(60);}
            ejDAO.insertar(ej); dlg.dispose();
            JOptionPane.showMessageDialog(this,"Ejercicio creado correctamente.","✅",JOptionPane.INFORMATION_MESSAGE);
        });
        btns.add(bc); btns.add(bg); g.gridy++; p.add(btns,g);
        dlg.setContentPane(p); dlg.setVisible(true);
    }

    private void abrirFormRutina(Rutina orig){
        JDialog dlg=new JDialog((JFrame)SwingUtilities.getWindowAncestor(this),orig==null?"Nueva Rutina":"Editar Rutina",true);
        dlg.setSize(500,360); dlg.setLocationRelativeTo(this);
        JPanel p=new JPanel(new GridBagLayout()); p.setBackground(EstilosGym.COLOR_PANEL); p.setBorder(new EmptyBorder(18,22,18,22));
        GridBagConstraints g=new GridBagConstraints(); g.insets=new Insets(6,6,6,6); g.fill=GridBagConstraints.HORIZONTAL;
        JTextField tfN=EstilosGym.crearCampoTexto(),tfD=EstilosGym.crearCampoTexto();
        JTextArea taDesc=new JTextArea(3,20); taDesc.setBackground(EstilosGym.COLOR_FONDO); taDesc.setForeground(EstilosGym.COLOR_TEXTO);
        taDesc.setFont(EstilosGym.FUENTE_NORMAL); taDesc.setLineWrap(true); taDesc.setWrapStyleWord(true);
        taDesc.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(EstilosGym.COLOR_BORDE),new EmptyBorder(5,8,5,8)));
        JComboBox<String> cmbObj=cmb("Ganancia muscular","Pérdida de peso","Resistencia","Acondicionamiento","Flexibilidad");
        JComboBox<String> cmbNiv=cmb("Principiante","Intermedio","Avanzado");
        List<Object[]> ents=rutinaDAO.listarEntrenadoresCombo();
        JComboBox<String> cmbEnt=new JComboBox<>(); int[] idsE=new int[ents.size()];
        for(int i=0;i<ents.size();i++){cmbEnt.addItem(ents.get(i)[1].toString());idsE[i]=(int)ents.get(i)[0];}
        cmbEnt.setBackground(EstilosGym.COLOR_FONDO); cmbEnt.setForeground(EstilosGym.COLOR_TEXTO);
        if(orig!=null){tfN.setText(orig.getNombreRutina());cmbObj.setSelectedItem(orig.getObjetivo());
            cmbNiv.setSelectedItem(orig.getNivel());tfD.setText(String.valueOf(orig.getDuracionSemanas()));
            taDesc.setText(orig.getDescripcion());
            for(int i=0;i<idsE.length;i++) if(idsE[i]==orig.getIdEntrenador()){cmbEnt.setSelectedIndex(i);break;}}
        Object[][] rows={{"Nombre *",tfN},{"Objetivo",cmbObj},{"Nivel",cmbNiv},{"Duración (sem)",tfD},{"Entrenador",cmbEnt}};
        for(int i=0;i<rows.length;i++){
            g.gridy=i;g.gridx=0;g.weightx=0.3; JLabel l=EstilosGym.crearEtiqueta(rows[i][0].toString()); l.setHorizontalAlignment(SwingConstants.RIGHT); p.add(l,g);
            g.gridx=1;g.weightx=0.7; p.add((Component)rows[i][1],g);
        }
        g.gridy=rows.length;g.gridx=0;g.weightx=0.3; JLabel ld=EstilosGym.crearEtiqueta("Descripción"); ld.setHorizontalAlignment(SwingConstants.RIGHT); p.add(ld,g);
        g.gridx=1;g.weightx=0.7; p.add(new JScrollPane(taDesc),g);
        JLabel err=new JLabel(""); err.setForeground(EstilosGym.COLOR_PELIGRO);
        g.gridy++;g.gridx=0;g.gridwidth=2; p.add(err,g);
        JPanel btns=new JPanel(new FlowLayout(FlowLayout.RIGHT,10,0)); btns.setOpaque(false);
        JButton bc=EstilosGym.crearBotonSecundario("Cancelar"),bg=EstilosGym.crearBotonPrimario("Guardar");
        bc.addActionListener(e->dlg.dispose());
        bg.addActionListener(e->{
            if(tfN.getText().trim().isEmpty()){err.setText("⚠ Nombre obligatorio.");return;}
            int dur=0; try{dur=Integer.parseInt(tfD.getText().trim());}catch(Exception ex){}
            int idEnt=idsE.length>0?idsE[cmbEnt.getSelectedIndex()]:0;
            if(idEnt==0){err.setText("⚠ Selecciona un entrenador.");return;}
            Rutina r=new Rutina(); r.setIdRutina(orig!=null?orig.getIdRutina():0);
            r.setNombreRutina(tfN.getText().trim()); r.setObjetivo(cmbObj.getSelectedItem().toString());
            r.setNivel(cmbNiv.getSelectedItem().toString()); r.setDuracionSemanas(dur);
            r.setDescripcion(taDesc.getText()); r.setIdEntrenador(idEnt);
            boolean ok=orig==null?rutinaDAO.insertar(r)>=0:rutinaDAO.actualizar(r);
            if(!ok){err.setText("⚠ Error al guardar.");return;}
            dlg.dispose(); cargarRutinas();
        });
        btns.add(bc); btns.add(bg); g.gridy++; p.add(btns,g);
        dlg.setContentPane(p); dlg.setVisible(true);
    }

    private void eliminarRutina(){
        if(rutinaSeleccionada==null) return;
        if(JOptionPane.showConfirmDialog(this,"¿Eliminar '"+rutinaSeleccionada.getNombreRutina()+"'?",
                "Confirmar",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE)==JOptionPane.YES_OPTION){
            rutinaDAO.eliminar(rutinaSeleccionada.getIdRutina()); rutinaSeleccionada=null; cargarRutinas();
        }
    }

    private JComboBox<String> cmb(String... opts){
        JComboBox<String> c=new JComboBox<>(opts); c.setBackground(EstilosGym.COLOR_FONDO);
        c.setForeground(EstilosGym.COLOR_TEXTO); c.setFont(EstilosGym.FUENTE_NORMAL); return c;
    }

    class RutinaCellRenderer extends DefaultListCellRenderer {
        @Override public Component getListCellRendererComponent(JList<?> l,Object v,int i,boolean sel,boolean foc){
            super.getListCellRendererComponent(l,v,i,sel,foc);
            Rutina r=(Rutina)v;
            Color nc="Avanzado".equals(r.getNivel())?EstilosGym.COLOR_PELIGRO:"Intermedio".equals(r.getNivel())?new Color(255,200,0):EstilosGym.COLOR_EXITO;
            setText("<html><b style='color:white'>"+r.getNombreRutina()+"</b><br/>"
                +"<font color='#"+String.format("%02x%02x%02x",nc.getRed(),nc.getGreen(),nc.getBlue())+"'>"+r.getNivel()+"</font>"
                +" &nbsp;<font color='#9696a5'>"+r.getObjetivo()+"</font></html>");
            setBorder(new EmptyBorder(8,12,8,12));
            setBackground(sel?new Color(255,87,34,80):EstilosGym.COLOR_PANEL);
            setForeground(EstilosGym.COLOR_TEXTO); return this;
        }
    }
    class EjercicioCellRenderer extends DefaultListCellRenderer {
        private final Color ac;
        EjercicioCellRenderer(Color c){this.ac=c;}
        @Override public Component getListCellRendererComponent(JList<?> l,Object v,int i,boolean sel,boolean foc){
            super.getListCellRendererComponent(l,v,i,sel,foc);
            Ejercicio ej=(Ejercicio)v;
            setText("<html><b style='color:white'>"+(i+1)+". "+ej.getNombre()+"</b><br/>"
                +"<font color='#9696a5'>"+ej.getSeries()+"x"+ej.getRepeticiones()+" &nbsp;·&nbsp; "+ej.getDescansoSegundos()+"s</font></html>");
            setBorder(new EmptyBorder(5,8,5,8));
            setBackground(sel?new Color(255,87,34,60):EstilosGym.COLOR_PANEL);
            setForeground(EstilosGym.COLOR_TEXTO); return this;
        }
    }
}
