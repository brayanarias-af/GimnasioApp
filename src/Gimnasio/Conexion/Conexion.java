package Gimnasio.Conexion;

import java.sql.*;
import java.io.*;
import java.nio.file.*;

/**
 * Conexión Singleton a SQLite.
 * La base de datos se crea en el directorio de trabajo con el nombre GymAPP.db.
 * Si el archivo no existe o las tablas están vacías, se ejecutan automáticamente
 * CodigoTablas.sql e InsercionDatos.sql desde el classpath.
 */
public class Conexion {

    private static final String DB_FILE = "GymAPP.db";
    private static final String URL     = "jdbc:sqlite:" + DB_FILE;

    private static Connection instancia;

    private Conexion() {}

    public static Connection getConexion() {
        try {
            if (instancia == null || instancia.isClosed()) {
                Class.forName("org.sqlite.JDBC");
                boolean nuevaDB = !new File(DB_FILE).exists();
                instancia = DriverManager.getConnection(URL);
                instancia.setAutoCommit(true);

                // Activar claves foráneas
                try (Statement st = instancia.createStatement()) {
                    st.execute("PRAGMA foreign_keys = ON;");
                }

                if (nuevaDB) {
                    inicializarBD();
                } else {
                    // Si la BD existe pero las tablas están vacías (primer arranque post-copia)
                    verificarTablas();
                }
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Driver SQLite no encontrado. Coloca sqlite-jdbc.jar en la carpeta lib/");
            throw new RuntimeException("Driver SQLite no encontrado", e);
        } catch (SQLException e) {
            System.err.println("Error de conexión SQLite: " + e.getMessage());
            throw new RuntimeException("Error al conectar con la base de datos", e);
        }
        return instancia;
    }

    /** Ejecuta los scripts SQL de creación e inserción de datos. */
    private static void inicializarBD() {
        System.out.println(">>> Inicializando base de datos...");
        ejecutarScript("/Gimnasio/BaseDatos/CodigoTablas.sql");
        ejecutarScript("/Gimnasio/BaseDatos/InsercionDatos.sql");
        System.out.println(">>> Base de datos inicializada correctamente.");
    }

    private static void verificarTablas() {
        try (Statement st = instancia.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name='usuarios'")) {
            if (rs.next() && rs.getInt(1) == 0) {
                inicializarBD();
            }
        } catch (SQLException e) {
            inicializarBD();
        }
    }

    private static void ejecutarScript(String rutaRecurso) {
        InputStream is = Conexion.class.getResourceAsStream(rutaRecurso);
        if (is == null) {
            System.err.println("No se encontró el script: " + rutaRecurso);
            return;
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
            StringBuilder sb = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) {
                String trimmed = linea.trim();
                if (trimmed.startsWith("--") || trimmed.isEmpty()) continue;
                sb.append(linea).append("\n");
                if (trimmed.endsWith(";")) {
                    String sql = sb.toString().trim();
                    if (!sql.isEmpty()) {
                        try (Statement st = instancia.createStatement()) {
                            st.execute(sql);
                        } catch (SQLException e) {
                            System.err.println("Error ejecutando SQL: " + sql.substring(0, Math.min(80, sql.length())));
                            System.err.println("  -> " + e.getMessage());
                        }
                    }
                    sb.setLength(0);
                }
            }
        } catch (IOException e) {
            System.err.println("Error leyendo script " + rutaRecurso + ": " + e.getMessage());
        }
    }

    public static void cerrar() {
        try {
            if (instancia != null && !instancia.isClosed()) {
                instancia.close();
                instancia = null;
                System.out.println("Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
}
