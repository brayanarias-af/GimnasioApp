package Gimnasio.Controlador;
import Gimnasio.Conexion.Conexion;
import Gimnasio.Modelo.Membresia;
import java.sql.*;
import java.util.*;

public class MembresiaDAO {
    public List<Membresia> listarTodas() {
        List<Membresia> l = new ArrayList<>();
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement("SELECT * FROM membresias ORDER BY precio");
             ResultSet rs = ps.executeQuery()) { while (rs.next()) l.add(map(rs)); }
        catch (SQLException e) { e.printStackTrace(); } return l;
    }
    public int insertar(Membresia m) {
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "INSERT INTO membresias(nombre,precio,duracion_dias,descripcion) VALUES(?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1,m.getNombre()); ps.setDouble(2,m.getPrecio());
            ps.setInt(3,m.getDuracionDias()); ps.setString(4,m.getDescripcion()); ps.executeUpdate();
            ResultSet g = ps.getGeneratedKeys(); int id = g.next() ? g.getInt(1) : -1; g.close(); return id;
        } catch (SQLException e) { e.printStackTrace(); } return -1;
    }
    public boolean actualizar(Membresia m) {
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "UPDATE membresias SET nombre=?,precio=?,duracion_dias=?,descripcion=? WHERE id_membresia=?")) {
            ps.setString(1,m.getNombre()); ps.setDouble(2,m.getPrecio());
            ps.setInt(3,m.getDuracionDias()); ps.setString(4,m.getDescripcion()); ps.setInt(5,m.getIdMembresia());
            return ps.executeUpdate()>0;
        } catch (SQLException e) { e.printStackTrace(); } return false;
    }
    public boolean eliminar(int id) {
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement("DELETE FROM membresias WHERE id_membresia=?")) {
            ps.setInt(1,id); return ps.executeUpdate()>0; } catch (SQLException e) { e.printStackTrace(); } return false;
    }
    public List<Object[]> listarCombo() {
        List<Object[]> l = new ArrayList<>();
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "SELECT id_membresia,nombre,precio FROM membresias ORDER BY precio");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) l.add(new Object[]{rs.getInt(1),rs.getString(2),rs.getDouble(3)}); }
        catch (SQLException e) {} return l;
    }
    private Membresia map(ResultSet rs) throws SQLException {
        Membresia m = new Membresia(); m.setIdMembresia(rs.getInt("id_membresia")); m.setNombre(rs.getString("nombre"));
        m.setPrecio(rs.getDouble("precio")); m.setDuracionDias(rs.getInt("duracion_dias")); m.setDescripcion(rs.getString("descripcion")); return m;
    }
}
