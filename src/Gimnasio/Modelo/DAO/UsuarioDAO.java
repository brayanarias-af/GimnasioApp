package Gimnasio.Modelo.DAO;

import Gimnasio.Conexion.Conexion;
import Gimnasio.Modelo.Entidades.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsuarioDAO {

    
    public Usuario iniciarSesion(String nombreUsuario, String contrasena) {
        String sql = "SELECT u.id_usuario, u.usuario, u.estado, u.id_rol, r.nombre as rol_nombre " +
                     "FROM usuarios u JOIN roles r ON u.id_rol = r.id_rol " +
                     "WHERE u.usuario = ? AND u.contrasena = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, nombreUsuario);
            ps.setString(2, contrasena);
            rs = ps.executeQuery();

            if (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setNombreUsuario(rs.getString("usuario"));
                u.setEstado(rs.getString("estado"));
                u.setIdRol(rs.getInt("id_rol"));
                u.setNombreRol(rs.getString("rol_nombre"));
                return u;
            }
        } catch (Exception e) {
            System.out.println("Error en inicio de sesión: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
        return null;
    }
            /**
 * Inserta un nuevo usuario y devuelve el ID generado, o -1 si falla.
 */
public int insertarUsuario(Usuario user, String nombreRol) {
    // Primero obtenemos el id_rol según el nombre
    String sqlRol = "SELECT id_rol FROM roles WHERE nombre = ?";
    String sqlInsert = "INSERT INTO usuarios (usuario, contrasena, estado, id_rol) VALUES (?, ?, ?, ?)";
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
        con = Conexion.getConexion();
        con.setAutoCommit(false);

        int idRol;
        ps = con.prepareStatement(sqlRol);
        ps.setString(1, nombreRol);
        rs = ps.executeQuery();
        if (rs.next()) {
            idRol = rs.getInt("id_rol");
        } else {
            return -1;
        }
        ps.close();

        ps = con.prepareStatement(sqlInsert, PreparedStatement.RETURN_GENERATED_KEYS);
        ps.setString(1, user.getNombreUsuario());
        ps.setString(2, user.getContrasena());
        ps.setString(3, user.getEstado());
        ps.setInt(4, idRol);
        ps.executeUpdate();

        rs = ps.getGeneratedKeys();
        int idGenerado = -1;
        if (rs.next()) {
            idGenerado = rs.getInt(1);
        }
        con.commit();
        return idGenerado;
    } catch (Exception e) {
        try { if (con != null) con.rollback(); } catch (Exception ex) {}
        System.out.println("Error al insertar usuario: " + e.getMessage());
        return -1;
    } finally {
        try { if (rs != null) rs.close(); } catch (Exception e) {}
        try { if (ps != null) ps.close(); } catch (Exception e) {}
        try { if (con != null) con.close(); } catch (Exception e) {}
    }
}

   
}