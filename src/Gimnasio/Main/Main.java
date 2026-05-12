package Gimnasio.Main;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import Gimnasio.Conexion.Conexion;
import Gimnasio.Vistas.LoginVista;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            LoginVista login = new LoginVista();
            login.setVisible(true);
        });
        Conexion.getConexion();
    }
}

