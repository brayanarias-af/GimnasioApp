package Gimnasio.Vistas;
import Gimnasio.Controlador.Sesion;
import Gimnasio.Modelo.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginVista extends JFrame {
    private JTextField     txtUsuario;
    private JPasswordField txtClave;
    private JLabel         lblError;
    private JButton        btnIngresar;

    public LoginVista() {
        setTitle("GymUTS — Sistema de Gestión"); setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900,580); setLocationRelativeTo(null); setResizable(false); setUndecorated(true);

        JPanel root=new JPanel(new BorderLayout()){
            @Override protected void paintComponent(Graphics g){
                super.paintComponent(g); g.setColor(EstilosGym.COLOR_FONDO); g.fillRect(0,0,getWidth(),getHeight());
            }
        };
        root.setBorder(BorderFactory.createLineBorder(EstilosGym.COLOR_BORDE,1));

        JPanel izq=new JPanel(null){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0,0,new Color(18,8,3),getWidth(),getHeight(),new Color(70,25,8)));
                g2.fillRect(0,0,getWidth(),getHeight());
                g2.setColor(new Color(255,87,34,35)); g2.fillOval(-80,-80,280,280);
                g2.setColor(new Color(255,87,34,18)); g2.fillOval(180,340,320,320); g2.dispose();
            }
        };
        izq.setPreferredSize(new Dimension(400,580));
        JLabel ico=new JLabel("🏋️",JLabel.CENTER); ico.setFont(new Font("Segoe UI Emoji",Font.PLAIN,68)); ico.setBounds(0,110,400,84); izq.add(ico);
        JLabel marca=new JLabel("GymUTS",JLabel.CENTER); marca.setFont(new Font("Segoe UI",Font.BOLD,44));
        marca.setForeground(EstilosGym.COLOR_ACENTO); marca.setBounds(0,200,400,54); izq.add(marca);
        JLabel slogan=new JLabel("Sistema de Gestión Integral",JLabel.CENTER); slogan.setFont(new Font("Segoe UI",Font.ITALIC,14));
        slogan.setForeground(new Color(190,165,148)); slogan.setBounds(0,258,400,26); izq.add(slogan);
        JPanel linea=new JPanel(){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setPaint(new GradientPaint(0,0,new Color(255,87,34,0),140,0,EstilosGym.COLOR_ACENTO));
                g2.setStroke(new BasicStroke(2)); g2.drawLine(0,0,140,0); g2.dispose();
            }
        };
        linea.setOpaque(false); linea.setBounds(130,294,140,3); izq.add(linea);
        String[] feats={"👥  Gestión completa de clientes","🏋️  Máquinas y rutinas semanales","💳  Membresías y pagos","🔐  Control de permisos por módulo"};
        int fy=308; for(String f:feats){JLabel l=new JLabel(f); l.setFont(new Font("Segoe UI",Font.PLAIN,12)); l.setForeground(new Color(185,162,148)); l.setBounds(70,fy,300,22); izq.add(l); fy+=26;}

        JPanel der=new JPanel(null); der.setBackground(EstilosGym.COLOR_PANEL);
        JPanel barra=new JPanel(new FlowLayout(FlowLayout.RIGHT,8,4)); barra.setOpaque(false); barra.setBounds(0,0,500,36);
        JButton btnX=new JButton("✕"); btnX.setFont(new Font("Segoe UI",Font.BOLD,13)); btnX.setForeground(EstilosGym.COLOR_TEXTO_GRIS);
        btnX.setBackground(EstilosGym.COLOR_PANEL); btnX.setBorder(BorderFactory.createEmptyBorder(2,8,2,8));
        btnX.setFocusPainted(false); btnX.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnX.addActionListener(e->System.exit(0));
        btnX.addMouseListener(new MouseAdapter(){public void mouseEntered(MouseEvent e){btnX.setForeground(EstilosGym.COLOR_PELIGRO);}public void mouseExited(MouseEvent e){btnX.setForeground(EstilosGym.COLOR_TEXTO_GRIS);}});
        barra.add(btnX); der.add(barra);
        final Point[] off={null};
        barra.addMouseListener(new MouseAdapter(){public void mousePressed(MouseEvent e){off[0]=e.getPoint();}});
        barra.addMouseMotionListener(new MouseMotionAdapter(){public void mouseDragged(MouseEvent e){Point p=getLocation();setLocation(p.x+e.getX()-off[0].x,p.y+e.getY()-off[0].y);}});

        JLabel titulo=new JLabel("Bienvenido de nuevo"); titulo.setFont(new Font("Segoe UI",Font.BOLD,24));
        titulo.setForeground(EstilosGym.COLOR_TEXTO); titulo.setBounds(60,52,360,34); der.add(titulo);
        JLabel sub=new JLabel("Inicia sesión en tu cuenta"); sub.setFont(EstilosGym.FUENTE_NORMAL);
        sub.setForeground(EstilosGym.COLOR_TEXTO_GRIS); sub.setBounds(60,88,320,20); der.add(sub);
        JSeparator sep=new JSeparator(); sep.setForeground(EstilosGym.COLOR_BORDE); sep.setBounds(60,118,360,2); der.add(sep);

        der.add(lbl("USUARIO",60,134));
        txtUsuario=EstilosGym.crearCampoTexto(); txtUsuario.setBounds(60,156,360,40); der.add(txtUsuario);
        der.add(lbl("CONTRASEÑA",60,208));
        txtClave=EstilosGym.crearCampoPassword(); txtClave.setBounds(60,230,360,40); der.add(txtClave);

        lblError=new JLabel(""); lblError.setFont(EstilosGym.FUENTE_PEQUEÑA);
        lblError.setForeground(EstilosGym.COLOR_PELIGRO); lblError.setBounds(60,278,360,20); der.add(lblError);

        btnIngresar=EstilosGym.crearBotonPrimario("INGRESAR"); btnIngresar.setBounds(60,306,360,44); der.add(btnIngresar);

        // Hint credenciales
        JLabel hint=new JLabel("<html><font color='#50506a'>Admin: admin/gimnasio123 &nbsp;·&nbsp; Entrenador: entrenador1/12345 &nbsp;·&nbsp; Cliente: cliente1/12345</font></html>");
        hint.setFont(EstilosGym.FUENTE_PEQUEÑA); hint.setBounds(60,362,380,22); der.add(hint);

        ActionListener login=e->autenticar();
        btnIngresar.addActionListener(login); txtClave.addActionListener(login); txtUsuario.addActionListener(login);
        root.add(izq,BorderLayout.WEST); root.add(der,BorderLayout.CENTER); setContentPane(root);
    }

    private JLabel lbl(String t,int x,int y){JLabel l=EstilosGym.crearEtiqueta(t); l.setBounds(x,y,240,20); return l;}

    private void autenticar(){
        String u=txtUsuario.getText().trim(); String c=new String(txtClave.getPassword());
        if(u.isEmpty()||c.isEmpty()){lblError.setText("⚠  Completa todos los campos.");return;}
        lblError.setText("Verificando..."); btnIngresar.setEnabled(false);
        new Thread(()->{
            boolean ok=new Sesion().iniciarSesion(new Usuario(u,c));
            SwingUtilities.invokeLater(()->{
                btnIngresar.setEnabled(true);
                if(!ok){lblError.setText("⚠  Usuario o contraseña incorrectos.");txtClave.setText("");}
                else{
                    dispose();
                    // Admin y Recepcionista → Panel Admin. Entrenador y Cliente → Panel Usuario
                    if(Sesion.esAdmin()||Sesion.esRecepcionista()) new PanelAdminVista().setVisible(true);
                    else new PanelUsuarioVista().setVisible(true);
                }
            });
        }).start();
    }
}
