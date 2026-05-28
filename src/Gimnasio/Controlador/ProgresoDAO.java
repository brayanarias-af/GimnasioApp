package Gimnasio.Controlador;
import Gimnasio.Conexion.Conexion;
import Gimnasio.Modelo.ProgresoFisico;
import java.sql.*;
import java.util.*;

public class ProgresoDAO {
    public List<ProgresoFisico> listarPorCliente(int idCliente) {
        List<ProgresoFisico> l = new ArrayList<>();
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "SELECT p.*,c.nombres||' '||c.apellidos as nc FROM progreso_fisico p "
               +"JOIN clientes c ON p.id_cliente=c.id_cliente WHERE p.id_cliente=? ORDER BY p.fecha_registro DESC")) {
            ps.setInt(1,idCliente); ResultSet rs=ps.executeQuery();
            while(rs.next()) l.add(map(rs)); rs.close();
        } catch(SQLException e){ e.printStackTrace(); } return l;
    }
    public List<ProgresoFisico> listarTodos() {
        List<ProgresoFisico> l = new ArrayList<>();
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(
                "SELECT p.*,c.nombres||' '||c.apellidos as nc FROM progreso_fisico p "
               +"JOIN clientes c ON p.id_cliente=c.id_cliente ORDER BY p.fecha_registro DESC");
             ResultSet rs=ps.executeQuery()){ while(rs.next()) l.add(map(rs)); }
        catch(SQLException e){ e.printStackTrace(); } return l;
    }
    public int insertar(ProgresoFisico p) {
        try(PreparedStatement ps=Conexion.getConexion().prepareStatement(
                "INSERT INTO progreso_fisico(fecha_registro,peso,porcentaje_grasa,masa_muscular,imc,observaciones,id_cliente) VALUES(?,?,?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1,p.getFechaRegistro()); ps.setDouble(2,p.getPeso());
            ps.setDouble(3,p.getPorcentajeGrasa()); ps.setDouble(4,p.getMasaMuscular());
            ps.setDouble(5,p.getImc()); ps.setString(6,p.getObservaciones()); ps.setInt(7,p.getIdCliente());
            ps.executeUpdate(); ResultSet g=ps.getGeneratedKeys(); int id=g.next()?g.getInt(1):-1; g.close(); return id;
        } catch(SQLException e){ e.printStackTrace(); return -1; }
    }
    public boolean eliminar(int id){
        try(PreparedStatement ps=Conexion.getConexion().prepareStatement("DELETE FROM progreso_fisico WHERE id_progreso=?")){
            ps.setInt(1,id); return ps.executeUpdate()>0;
        } catch(SQLException e){ e.printStackTrace(); return false; }
    }
    private ProgresoFisico map(ResultSet rs) throws SQLException {
        ProgresoFisico p=new ProgresoFisico(); p.setIdProgreso(rs.getInt("id_progreso"));
        p.setFechaRegistro(rs.getString("fecha_registro")); p.setPeso(rs.getDouble("peso"));
        p.setPorcentajeGrasa(rs.getDouble("porcentaje_grasa")); p.setMasaMuscular(rs.getDouble("masa_muscular"));
        p.setImc(rs.getDouble("imc")); p.setObservaciones(rs.getString("observaciones"));
        p.setIdCliente(rs.getInt("id_cliente"));
        try{ p.setNombreCliente(rs.getString("nc")); } catch(SQLException ex){}
        return p;
    }
}
