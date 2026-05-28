package Gimnasio.Conexion;

import java.io.*;
import java.sql.*;

public class Conexion {
    private static final String DB_FILE = "GymAPP.db";
    private static final String URL     = "jdbc:sqlite:" + DB_FILE;
    private static Connection instancia;

    private Conexion() {}

    public static Connection getConexion() {
        try {
            if (instancia == null || instancia.isClosed()) {
                Class.forName("org.sqlite.JDBC");
                boolean nueva = !new java.io.File(DB_FILE).exists();
                instancia = DriverManager.getConnection(URL);
                try (Statement st = instancia.createStatement()) {
                    st.execute("PRAGMA foreign_keys = ON;");
                    st.execute("PRAGMA journal_mode = WAL;");
                }
                if (nueva) inicializar();
                else verificar();
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver SQLite no encontrado. Agrega sqlite-jdbc.jar a lib/", e);
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar con SQLite: " + e.getMessage(), e);
        }
        return instancia;
    }

    private static void inicializar() {
        System.out.println(">>> Creando base de datos...");
        ejecutarScript("/Gimnasio/BaseDatos/CodigoTablas.sql");
        ejecutarScript("/Gimnasio/BaseDatos/InsercionDatos.sql");
        System.out.println(">>> BD lista.");
    }

    private static void verificar() {
        try (Statement st = instancia.createStatement();
             ResultSet rs = st.executeQuery(
                 "SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name='usuarios'")) {
            if (rs.next() && rs.getInt(1) == 0) inicializar();
        } catch (SQLException e) { inicializar(); }
    }

    private static void ejecutarScript(String ruta) {
        InputStream is = Conexion.class.getResourceAsStream(ruta);
        if (is == null) { System.err.println("Script no encontrado: " + ruta); return; }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
            StringBuilder sb = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) {
                String t = linea.trim();
                if (t.startsWith("--") || t.isEmpty()) continue;
                sb.append(linea).append("\n");
                if (t.endsWith(";")) {
                    String sql = sb.toString().trim();
                    if (!sql.isEmpty()) {
                        try (Statement st = instancia.createStatement()) { st.execute(sql); }
                        catch (SQLException e) { System.err.println("SQL skip: " + e.getMessage()); }
                    }
                    sb.setLength(0);
                }
            }
        } catch (IOException e) { System.err.println("Error leyendo script: " + e.getMessage()); }
    }

    public static void cerrar() {
        try { if (instancia != null && !instancia.isClosed()) { instancia.close(); instancia = null; } }
        catch (SQLException e) { System.err.println("Error cerrando conexión: " + e.getMessage()); }
    }
}
