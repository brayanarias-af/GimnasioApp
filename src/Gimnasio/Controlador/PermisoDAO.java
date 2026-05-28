package Gimnasio.Controlador;
import Gimnasio.Conexion.Conexion;
import java.sql.*;
import java.util.*;

public class PermisoDAO {

    /** Todos los módulos posibles por rol */
    public static final String[] MODULOS_CLIENTE    = {"Mis Rutinas","Máquinas","Mis Pagos","Mi Progreso"};
    public static final String[] MODULOS_ENTRENADOR = {"Mis Rutinas","Máquinas","Clientes Asignados","Progreso Clientes"};

    /** Retorna mapa modulo->habilitado para un usuario */
    public Map<String,Boolean> obtenerPermisos(int idUsuario, String[] modulos) {
        Map<String,Boolean> mapa = new LinkedHashMap<>();
        for (String m : modulos) mapa.put(m, false);
        String sql = "SELECT modulo,habilitado FROM permisos_modulo WHERE id_usuario=?";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) mapa.put(rs.getString("modulo"), rs.getInt("habilitado")==1);
            rs.close();
        } catch (SQLException e) { e.printStackTrace(); }
        return mapa;
    }

    /** Inserta o actualiza un permiso */
    public void setPermiso(int idUsuario, String modulo, boolean habilitado) {
        String sql = "INSERT INTO permisos_modulo(id_usuario,modulo,habilitado) VALUES(?,?,?) "
                   + "ON CONFLICT(id_usuario,modulo) DO UPDATE SET habilitado=excluded.habilitado";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ps.setInt(1, idUsuario); ps.setString(2, modulo);
            ps.setInt(3, habilitado ? 1 : 0); ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    /** Inicializa permisos por defecto para un usuario nuevo */
    public void inicializarPermisos(int idUsuario, String[] modulos) {
        for (String m : modulos) setPermiso(idUsuario, m, true);
    }

    /**
     * Solo entrenadores — los clientes tienen permisos automáticos según su membresía.
     */
    public List<Object[]> listarUsuariosGestionables() {
        List<Object[]> l = new ArrayList<>();
        String sql = "SELECT u.id_usuario, u.usuario, r.nombre as rol, "
                   + "COALESCE(e.nombres||' '||e.apellidos, u.usuario) as nombre_completo "
                   + "FROM usuarios u JOIN roles r ON u.id_rol=r.id_rol "
                   + "LEFT JOIN entrenadores e ON e.id_usuario=u.id_usuario "
                   + "WHERE r.nombre='ENTRENADOR' AND u.estado=1 "
                   + "ORDER BY nombre_completo";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                l.add(new Object[]{rs.getInt("id_usuario"), rs.getString("usuario"),
                                   rs.getString("rol"), rs.getString("nombre_completo")});
        } catch (SQLException e) { e.printStackTrace(); }
        return l;
    }
}
