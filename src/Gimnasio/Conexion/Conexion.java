
package Gimnasio.Conexion;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    
    private static final String URL = "jdbc:sqlite:GymAPP";

    private static Connection instancia;

    private Conexion() {
        
    }

    public static Connection getConexion() {
        try {
            if (instancia == null || instancia.isClosed()) {
                instancia = DriverManager.getConnection(URL);
                System.out.println("Conectado a SQLite");
            }
        } catch (SQLException e) {
            System.err.println(" Error de conexión: " + e.getMessage());
        }
        return instancia;
    }

    public static void cerrar() {
        try {
            if (instancia != null && !instancia.isClosed()) {
                instancia.close();
                instancia = null;
                System.out.println("Conexión cerrada");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
}

