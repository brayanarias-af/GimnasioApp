package Gimnasio.Vistas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.time.format.*;
import java.util.Locale;

/**
 * Campo de fecha con mini-calendario popup.
 * Los días se pintan con Graphics2D → nunca trunca a "...".
 */
public class DatePickerField extends JPanel {

    private final JTextField tfFecha;
    private YearMonth        mesActual;

    // Paleta del calendario
    private static final Color C_FONDO = new Color(18, 18, 26);
    private static final Color C_NAV   = new Color(12, 12, 20);
    private static final Color C_HOVER = new Color(255, 87, 34, 110);
    private static final Color C_SEL   = new Color(255, 87, 34);
    private static final Color C_HOY   = new Color(55, 55, 80);
    private static final Color C_TEXTO = new Color(230, 230, 240);
    private static final Color C_GRIS  = new Color(130, 130, 150);
    private static final Color C_BORDE = new Color(50, 50, 70);

    public DatePickerField() { this(LocalDate.now().toString()); }

    public DatePickerField(String valorInicial) {
        setLayout(new BorderLayout(3, 0));
        setOpaque(false);
        mesActual = YearMonth.now();

        tfFecha = EstilosGym.crearCampoTexto();
        tfFecha.setText(valorInicial != null ? valorInicial : "");

        JButton btnCal = botonIcono("📅");
        btnCal.addActionListener(e -> mostrarCalendario(btnCal));

        add(tfFecha, BorderLayout.CENTER);
        add(btnCal,  BorderLayout.EAST);
    }

    public String    getFecha()            { return tfFecha.getText().trim(); }
    public void      setFecha(String f)    { tfFecha.setText(f != null ? f : ""); }
    public JTextField getTextField()       { return tfFecha; }

    // ── Popup ──────────────────────────────────────────────
    private void mostrarCalendario(Component origen) {
        try {
            String t = tfFecha.getText().trim();
            if (!t.isEmpty()) mesActual = YearMonth.from(LocalDate.parse(t));
        } catch (Exception ignored) {}

        JPopupMenu pop = new JPopupMenu();
        pop.setBackground(C_FONDO);
        pop.setBorder(BorderFactory.createLineBorder(C_BORDE, 1));
        pop.add(buildPanel(pop));
        pop.show(origen, 0, origen.getHeight() + 2);
    }

    // ── Panel del mes ──────────────────────────────────────
    private JPanel buildPanel(JPopupMenu pop) {
        JPanel root = new JPanel(new BorderLayout(0, 4));
        root.setBackground(C_FONDO);
        root.setPreferredSize(new Dimension(294, 276));
        root.setBorder(BorderFactory.createEmptyBorder(6, 6, 8, 6));

        // ── Barra de navegación ───────────────────────────
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(C_NAV);
        nav.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));

        JButton prev = botonNav("◀");
        JButton next = botonNav("▶");

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("es","CO"));
        String nomMes = mesActual.format(fmt);
        nomMes = Character.toUpperCase(nomMes.charAt(0)) + nomMes.substring(1);
        JLabel lblMes = new JLabel(nomMes, JLabel.CENTER);
        lblMes.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblMes.setForeground(C_TEXTO);

        prev.addActionListener(e -> refresh(pop, -1));
        next.addActionListener(e -> refresh(pop, +1));

        nav.add(prev,   BorderLayout.WEST);
        nav.add(lblMes, BorderLayout.CENTER);
        nav.add(next,   BorderLayout.EAST);
        root.add(nav, BorderLayout.NORTH);

        // ── Grilla: 7 col × 7 filas ───────────────────────
        JPanel grid = new JPanel(new GridLayout(7, 7, 1, 1));
        grid.setBackground(C_FONDO);
        grid.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));

        // Cabecera
        for (String d : new String[]{"Do","Lu","Ma","Mi","Ju","Vi","Sa"}) {
            JLabel lc = new JLabel(d, JLabel.CENTER);
            lc.setFont(new Font("Segoe UI", Font.BOLD, 11));
            lc.setForeground(C_GRIS);
            grid.add(lc);
        }

        // Celdas vacías iniciales
        int offset = mesActual.atDay(1).getDayOfWeek().getValue() % 7; // lun=1..dom=0
        for (int i = 0; i < offset; i++) grid.add(new JLabel());

        // Parsear selección actual
        LocalDate hoy = LocalDate.now();
        LocalDate selParsed = null;
        try {
            String t = tfFecha.getText().trim();
            if (!t.isEmpty()) selParsed = LocalDate.parse(t);
        } catch (Exception ignored) {}
        final LocalDate sel = selParsed;

        // Celdas de días
        for (int d = 1; d <= mesActual.lengthOfMonth(); d++) {
            final LocalDate fecha = mesActual.atDay(d);
            final boolean esSel = fecha.equals(sel);
            final boolean esHoy = fecha.equals(hoy);

            grid.add(buildDiaCell(fecha, esSel, esHoy, pop));
        }

        // Completar filas sobrantes
        int total  = offset + mesActual.lengthOfMonth();
        int sobran = (7 - total % 7) % 7;
        for (int i = 0; i < sobran; i++) grid.add(new JLabel());

        root.add(grid, BorderLayout.CENTER);
        return root;
    }

    /** Celda de un día pintada con Graphics2D — nunca muestra "..." */
    private JPanel buildDiaCell(LocalDate fecha, boolean esSel, boolean esHoy, JPopupMenu pop) {
        boolean[] hover = {false};

        JPanel cell = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);

                // Fondo de la celda
                Color bg;
                if      (esSel)    bg = C_SEL;
                else if (hover[0]) bg = C_HOVER;
                else if (esHoy)    bg = C_HOY;
                else               bg = C_FONDO;

                int m = 2;
                g2.setColor(bg);
                g2.fillRoundRect(m, m, getWidth()-2*m, getHeight()-2*m, 8, 8);

                // Número del día
                String num = String.valueOf(fecha.getDayOfMonth());
                g2.setFont(new Font("Segoe UI", esSel ? Font.BOLD : Font.PLAIN, 12));
                g2.setColor(esSel ? Color.WHITE : C_TEXTO);
                FontMetrics fm = g2.getFontMetrics();
                int tx = (getWidth()  - fm.stringWidth(num)) / 2;
                int ty = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(num, tx, ty);

                g2.dispose();
            }
        };

        cell.setOpaque(false);
        cell.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        cell.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                hover[0] = true;  cell.repaint();
            }
            @Override public void mouseExited(MouseEvent e) {
                hover[0] = false; cell.repaint();
            }
            @Override public void mouseClicked(MouseEvent e) {
                tfFecha.setText(fecha.toString());
                pop.setVisible(false);
            }
        });

        return cell;
    }

    private void refresh(JPopupMenu pop, int delta) {
        mesActual = mesActual.plusMonths(delta);
        pop.removeAll();
        pop.add(buildPanel(pop));
        pop.revalidate();
        pop.repaint();
    }

    // ── Helpers de botones ─────────────────────────────────
    private JButton botonIcono(String icono) {
        JButton b = new JButton(icono) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? EstilosGym.COLOR_ACENTO : C_BORDE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(C_TEXTO);
                g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
                FontMetrics fm = g2.getFontMetrics();
                String t = getText();
                g2.drawString(t, (getWidth()-fm.stringWidth(t))/2,
                              (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        b.setPreferredSize(new Dimension(36, 36));
        b.setOpaque(false); b.setContentAreaFilled(false);
        b.setBorderPainted(false); b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JButton botonNav(String txt) {
        JButton b = new JButton(txt) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(40,40,60) : C_NAV);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.setColor(C_TEXTO);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2,
                              (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        b.setPreferredSize(new Dimension(28, 26));
        b.setOpaque(false); b.setContentAreaFilled(false);
        b.setBorderPainted(false); b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
}
