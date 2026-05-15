package Gimnasio.Main;

import Gimnasio.Vistas.LoginVista;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { /* usa default */ }

        SwingUtilities.invokeLater(() -> {
            new LoginVista().setVisible(true);
        });
    }
}
