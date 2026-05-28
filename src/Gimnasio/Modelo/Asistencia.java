package Gimnasio.Modelo;
public class Asistencia {
    private int idAsistencia, idCliente;
    private String fecha, horaEntrada, horaSalida, nombreCliente;

    public Asistencia() {}

    public Asistencia(int idCliente, String fecha, String horaEntrada) {
        this.idCliente   = idCliente;
        this.fecha       = fecha;
        this.horaEntrada = horaEntrada;
    }
    public int getIdAsistencia()     { return idAsistencia; }
    public int getIdCliente()        { return idCliente; }
    public String getFecha()         { return fecha; }
    public String getHoraEntrada()   { return horaEntrada; }
    public String getHoraSalida()    { return horaSalida; }
    public String getNombreCliente() { return nombreCliente; }
    public void setIdAsistencia(int v)       { idAsistencia = v; }
    public void setIdCliente(int v)          { idCliente = v; }
    public void setFecha(String v)           { fecha = v; }
    public void setHoraEntrada(String v)     { horaEntrada = v; }
    public void setHoraSalida(String v)      { horaSalida = v; }
    public void setNombreCliente(String v)   { nombreCliente = v; }
}
