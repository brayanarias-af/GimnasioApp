package Gimnasio.Controlador;
import Gimnasio.Conexion.Conexion;
import Gimnasio.Modelo.Rutina;
import java.sql.*;
import java.util.*;

public class RutinaDAO {
    public List<Rutina> listarTodas() {
        List<Rutina> l=new ArrayList<>();
        try(PreparedStatement ps=Conexion.getConexion().prepareStatement(
                "SELECT r.*,e.nombres||' '||e.apellidos as nent FROM rutinas r LEFT JOIN entrenadores e ON r.id_entrenador=e.id_entrenador ORDER BY r.nombre");
            ResultSet rs=ps.executeQuery()){ while(rs.next()) l.add(map(rs)); }
        catch(SQLException e){ e.printStackTrace(); } return l;
    }
    /** Rutinas asignadas a un cliente via tabla cliente_rutina */
    public List<Rutina> listarPorCliente(int idCliente) {
        List<Rutina> l=new ArrayList<>();
        String sql="SELECT r.*,e.nombres||' '||e.apellidos as nent FROM rutinas r "
                  +"LEFT JOIN entrenadores e ON r.id_entrenador=e.id_entrenador "
                  +"JOIN cliente_rutina cr ON cr.id_rutina=r.id_rutina "
                  +"WHERE cr.id_cliente=? ORDER BY r.nombre";
        try(PreparedStatement ps=Conexion.getConexion().prepareStatement(sql)){
            ps.setInt(1,idCliente); ResultSet rs=ps.executeQuery();
            while(rs.next()) l.add(map(rs)); rs.close();
        } catch(SQLException e){ e.printStackTrace(); } return l;
    }
    public int insertar(Rutina r){
        try(PreparedStatement ps=Conexion.getConexion().prepareStatement(
                "INSERT INTO rutinas(nombre,objetivo,nivel,duracion_semanas,descripcion,id_entrenador) VALUES(?,?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1,r.getNombreRutina()); ps.setString(2,r.getObjetivo()); ps.setString(3,r.getNivel());
            ps.setInt(4,r.getDuracionSemanas()); ps.setString(5,r.getDescripcion()); ps.setInt(6,r.getIdEntrenador());
            ps.executeUpdate(); ResultSet g=ps.getGeneratedKeys(); int id=g.next()?g.getInt(1):-1; g.close(); return id;
        } catch(SQLException e){ e.printStackTrace(); return -1; }
    }
    public boolean actualizar(Rutina r){
        try(PreparedStatement ps=Conexion.getConexion().prepareStatement(
                "UPDATE rutinas SET nombre=?,objetivo=?,nivel=?,duracion_semanas=?,descripcion=?,id_entrenador=? WHERE id_rutina=?")){
            ps.setString(1,r.getNombreRutina()); ps.setString(2,r.getObjetivo()); ps.setString(3,r.getNivel());
            ps.setInt(4,r.getDuracionSemanas()); ps.setString(5,r.getDescripcion()); ps.setInt(6,r.getIdEntrenador());
            ps.setInt(7,r.getIdRutina()); return ps.executeUpdate()>0;
        } catch(SQLException e){ e.printStackTrace(); return false; }
    }
    public boolean eliminar(int id){
        // Limpiar ejercicios y asignaciones primero
        try(PreparedStatement p1=Conexion.getConexion().prepareStatement("DELETE FROM rutina_ejercicios WHERE id_rutina=?");
            PreparedStatement p2=Conexion.getConexion().prepareStatement("DELETE FROM cliente_rutina WHERE id_rutina=?");
            PreparedStatement ps=Conexion.getConexion().prepareStatement("DELETE FROM rutinas WHERE id_rutina=?")){
            p1.setInt(1,id); p1.executeUpdate();
            p2.setInt(1,id); p2.executeUpdate();
            ps.setInt(1,id); return ps.executeUpdate()>0;
        } catch(SQLException e){ e.printStackTrace(); return false; }
    }
    public int contarTotal(){
        try(ResultSet rs=Conexion.getConexion().createStatement().executeQuery("SELECT COUNT(*) FROM rutinas")){
            if(rs.next()) return rs.getInt(1); } catch(SQLException e){} return 0;
    }
    public List<Object[]> listarEntrenadoresCombo(){
        List<Object[]> l=new ArrayList<>();
        try(PreparedStatement ps=Conexion.getConexion().prepareStatement(
                "SELECT id_entrenador,nombres||' '||apellidos FROM entrenadores ORDER BY nombres");
            ResultSet rs=ps.executeQuery()){ while(rs.next()) l.add(new Object[]{rs.getInt(1),rs.getString(2)}); }
        catch(SQLException e){} return l;
    }
    private Rutina map(ResultSet rs) throws SQLException {
        Rutina r=new Rutina(); r.setIdRutina(rs.getInt("id_rutina")); r.setNombreRutina(rs.getString("nombre"));
        r.setObjetivo(rs.getString("objetivo")); r.setNivel(rs.getString("nivel"));
        r.setDuracionSemanas(rs.getInt("duracion_semanas")); r.setDescripcion(rs.getString("descripcion"));
        r.setIdEntrenador(rs.getInt("id_entrenador"));
        try{ r.setNombreEntrenador(rs.getString("nent")); } catch(SQLException ex){}  return r;
    }
}
