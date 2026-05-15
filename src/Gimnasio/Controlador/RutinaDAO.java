package Gimnasio.Controlador;

import Gimnasio.Conexion.Conexion;
import Gimnasio.Modelo.Rutina;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RutinaDAO {

    public List<Rutina> listarTodas() {
        List<Rutina> lista = new ArrayList<>();
        String sql = "SELECT r.*, e.nombres || ' ' || e.apellidos as nombre_entrenador "
                   + "FROM rutinas r "
                   + "LEFT JOIN entrenadores e ON r.id_entrenador = e.id_entrenador "
                   + "ORDER BY r.nombre";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("Error al listar rutinas: " + e.getMessage());
        }
        return lista;
    }

    public int insertar(Rutina r) {
        String sql = "INSERT INTO rutinas(nombre, objetivo, nivel, duracion_semanas, descripcion, id_entrenador) "
                   + "VALUES(?,?,?,?,?,?)";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, r.getNombreRutina());
            ps.setString(2, r.getObjetivo());
            ps.setString(3, r.getNivel());
            ps.setInt   (4, r.getDuracionSemanas());
            ps.setString(5, r.getDescripcion());
            ps.setInt   (6, r.getIdEntrenador());
            ps.executeUpdate();
            ResultSet gen = ps.getGeneratedKeys();
            int id = gen.next() ? gen.getInt(1) : -1;
            gen.close(); return id;
        } catch (SQLException e) {
            System.err.println("Error al insertar rutina: " + e.getMessage());
        }
        return -1;
    }

    public boolean actualizar(Rutina r) {
        String sql = "UPDATE rutinas SET nombre=?, objetivo=?, nivel=?, duracion_semanas=?, descripcion=?, id_entrenador=? "
                   + "WHERE id_rutina=?";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, r.getNombreRutina());
            ps.setString(2, r.getObjetivo());
            ps.setString(3, r.getNivel());
            ps.setInt   (4, r.getDuracionSemanas());
            ps.setString(5, r.getDescripcion());
            ps.setInt   (6, r.getIdEntrenador());
            ps.setInt   (7, r.getIdRutina());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar rutina: " + e.getMessage());
        }
        return false;
    }

    public boolean eliminar(int id) {
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "DELETE FROM rutinas WHERE id_rutina = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar rutina: " + e.getMessage());
        }
        return false;
    }

    public int contarTotal() {
        try (Statement st = Conexion.getConexion().createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM rutinas")) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { /* ignore */ }
        return 0;
    }

    /** Retorna lista de entrenadores para el combo del formulario */
    public List<int[]> listarEntrenadores() {
        List<int[]> lista = new ArrayList<>();
        String sql = "SELECT id_entrenador, nombres, apellidos FROM entrenadores ORDER BY nombres";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new int[]{rs.getInt("id_entrenador")});
            }
        } catch (SQLException e) { /* ignore */ }
        return lista;
    }

    /** Retorna pares [id_entrenador, "Nombre Apellido"] */
    public List<Object[]> listarEntrenadoresNombres() {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT id_entrenador, nombres || ' ' || apellidos as nombre_completo "
                   + "FROM entrenadores ORDER BY nombres";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Object[]{rs.getInt(1), rs.getString(2)});
            }
        } catch (SQLException e) { /* ignore */ }
        return lista;
    }

    private Rutina mapear(ResultSet rs) throws SQLException {
        Rutina r = new Rutina();
        r.setIdRutina      (rs.getInt   ("id_rutina"));
        r.setNombreRutina  (rs.getString("nombre"));
        r.setObjetivo      (rs.getString("objetivo"));
        r.setNivel         (rs.getString("nivel"));
        r.setDuracionSemanas(rs.getInt  ("duracion_semanas"));
        r.setDescripcion   (rs.getString("descripcion"));
        r.setIdEntrenador  (rs.getInt   ("id_entrenador"));
        try { r.setNombreEntrenador(rs.getString("nombre_entrenador")); } catch (SQLException ex) {}
        return r;
    }
}
