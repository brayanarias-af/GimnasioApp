package Gimnasio.Controlador;
import Gimnasio.Conexion.Conexion;
import Gimnasio.Modelo.Asistencia;
import java.sql.*;
import java.util.*;

public class AsistenciaDAO {
    public List<Asistencia> listarTodas() {
        List<Asistencia> l = new ArrayList<>();
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "SELECT a.*,c.nombres||' '||c.apellidos as nc FROM asistencias a "
               +"JOIN clientes c ON a.id_cliente=c.id_cliente ORDER BY a.fecha DESC,a.hora_entrada DESC");
             ResultSet rs = ps.executeQuery()) { while (rs.next()) l.add(map(rs)); }
        catch (SQLException e) { e.printStackTrace(); } return l;
    }
    public List<Asistencia> listarPorCliente(int idCliente) {
        List<Asistencia> l = new ArrayList<>();
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "SELECT a.*,c.nombres||' '||c.apellidos as nc FROM asistencias a "
               +"JOIN clientes c ON a.id_cliente=c.id_cliente WHERE a.id_cliente=? ORDER BY a.fecha DESC")) {
            ps.setInt(1,idCliente); ResultSet rs = ps.executeQuery(); while (rs.next()) l.add(map(rs)); rs.close();
        } catch (SQLException e) { e.printStackTrace(); } return l;
    }
    public int insertar(Asistencia a) {
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "INSERT INTO asistencias(fecha,hora_entrada,hora_salida,id_cliente) VALUES(?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1,a.getFecha()); ps.setString(2,a.getHoraEntrada());
            ps.setString(3,a.getHoraSalida()); ps.setInt(4,a.getIdCliente()); ps.executeUpdate();
            ResultSet g = ps.getGeneratedKeys(); int id = g.next() ? g.getInt(1) : -1; g.close(); return id;
        } catch (SQLException e) { e.printStackTrace(); } return -1;
    }
    public boolean registrarSalida(int id, String hora) {
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "UPDATE asistencias SET hora_salida=? WHERE id_asistencia=?")) {
            ps.setString(1,hora); ps.setInt(2,id); return ps.executeUpdate()>0;
        } catch (SQLException e) { e.printStackTrace(); } return false;
    }
    public boolean eliminar(int id) {
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement("DELETE FROM asistencias WHERE id_asistencia=?")) {
            ps.setInt(1,id); return ps.executeUpdate()>0; } catch (SQLException e) { e.printStackTrace(); } return false;
    }
    public int contarHoy() {
        try (ResultSet rs = Conexion.getConexion().createStatement().executeQuery(
                "SELECT COUNT(*) FROM asistencias WHERE fecha=DATE('now')")) {
            if (rs.next()) return rs.getInt(1); } catch (SQLException e) {} return 0;
    }
    private Asistencia map(ResultSet rs) throws SQLException {
        Asistencia a = new Asistencia(); a.setIdAsistencia(rs.getInt("id_asistencia"));
        a.setFecha(rs.getString("fecha")); a.setHoraEntrada(rs.getString("hora_entrada"));
        a.setHoraSalida(rs.getString("hora_salida")); a.setIdCliente(rs.getInt("id_cliente"));
        try { a.setNombreCliente(rs.getString("nc")); } catch (SQLException ex) {} return a;
    }
}
