package Gimnasio.Controlador;

import Gimnasio.Conexion.Conexion;
import Gimnasio.Modelo.Usuario;
import java.sql.*;

/**
 * Gestiona la sesión activa.
 * - iniciarSesion(Usuario) hace la query real contra SQLite.
 * - Los métodos estáticos permiten acceder al usuario desde cualquier vista.
 */
public class Sesion {

    private static Usuario usuarioActual = null;

    /**
     * Autentica al usuario contra la BD SQLite.
     * Query: SELECT u.*, r.nombre as rol FROM usuarios u JOIN roles r ON u.id_rol=r.id_rol
     *        WHERE u.usuario=? AND u.contrasena=? AND u.estado=1
     */
    public boolean iniciarSesion(Usuario user) {
        String sql = "SELECT u.id_usuario, u.usuario, u.contrasena, u.estado, "
                   + "u.ultimo_acceso, u.intentos_fallidos, u.id_rol, r.nombre as nombre_rol "
                   + "FROM usuarios u "
                   + "JOIN roles r ON u.id_rol = r.id_rol "
                   + "WHERE u.usuario = ? AND u.contrasena = ? AND u.estado = 1";
        try {
            Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, user.getNombreUsuario());
            ps.setString(2, user.getContraseña());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user.setIdUsuario(rs.getInt("id_usuario"));
                user.setNombreRol(rs.getString("nombre_rol"));
                user.setIdRol(rs.getInt("id_rol"));
                user.setEstado(rs.getBoolean("estado"));

                // Intentar obtener nombre completo del cliente o entrenador
                resolverNombrePersona(con, user);

                // Actualizar último acceso
                actualizarUltimoAcceso(con, user.getIdUsuario());

                usuarioActual = user;
                ps.close(); rs.close();
                return true;
            }
            ps.close(); rs.close();
        } catch (Exception e) {
            System.err.println("Error en iniciarSesion: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /** Busca nombre/apellidos del cliente o entrenador asociado al usuario */
    private void resolverNombrePersona(Connection con, Usuario user) {
        String[] tablas = {"clientes", "entrenadores"};
        for (String tabla : tablas) {
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT nombres, apellidos FROM " + tabla + " WHERE id_usuario = ?")) {
                ps.setInt(1, user.getIdUsuario());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    user.setNombres(rs.getString("nombres"));
                    user.setApellidos(rs.getString("apellidos"));
                    rs.close();
                    return;
                }
                rs.close();
            } catch (SQLException e) { /* tabla no tiene el usuario */ }
        }
        // Si no hay persona asociada, usar el nombre de usuario
        if (user.getNombres() == null || user.getNombres().isEmpty()) {
            user.setNombres(user.getNombreUsuario());
            user.setApellidos("");
        }
    }

    private void actualizarUltimoAcceso(Connection con, int idUsuario) {
        try (PreparedStatement ps = con.prepareStatement(
                "UPDATE usuarios SET ultimo_acceso = CURRENT_TIMESTAMP WHERE id_usuario = ?")) {
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
        } catch (SQLException e) { /* no crítico */ }
    }

    // ====== Métodos estáticos para acceso global ======

    public static Usuario getUsuarioActual()   { return usuarioActual; }

    public static String getNombreCompleto() {
        if (usuarioActual == null) return "Invitado";
        String n = usuarioActual.getNombres()   != null ? usuarioActual.getNombres() : "";
        String a = usuarioActual.getApellidos() != null ? usuarioActual.getApellidos() : "";
        String completo = (n + " " + a).trim();
        return completo.isEmpty() ? usuarioActual.getNombreUsuario() : completo;
    }

    public static boolean esAdmin() {
        if (usuarioActual == null) return false;
        return "ADMIN".equalsIgnoreCase(usuarioActual.getRol());
    }

    public static String getRol() {
        return usuarioActual != null ? usuarioActual.getRol() : "";
    }

    public static void cerrarSesion() {
        if (usuarioActual != null) {
            System.out.println("Sesión cerrada: " + usuarioActual.getNombreUsuario());
            usuarioActual = null;
        }
    }

    public static boolean haySesionActiva() { return usuarioActual != null; }
}
