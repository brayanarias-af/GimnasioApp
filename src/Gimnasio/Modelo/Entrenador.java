package Gimnasio.Modelo;
public class Entrenador extends Persona {
    private int idEntrenador, aniosExperiencia, idUsuario;
    private String especialidad, horario, fechaContratacion;
    private double salario;

    public Entrenador() {}

    public Entrenador(String cedula, String nombres, String apellidos, int edad, String sexo,
                      String telefono, String correo, String direccion,
                      String especialidad, int aniosExperiencia, String horario,
                      double salario, String fechaContratacion, int idUsuario) {
        super(0, cedula, nombres, apellidos, edad, sexo, telefono, correo, direccion);
        this.especialidad       = especialidad;
        this.aniosExperiencia   = aniosExperiencia;
        this.horario            = horario;
        this.salario            = salario;
        this.fechaContratacion  = fechaContratacion;
        this.idUsuario          = idUsuario;
    }
    public int getIdEntrenador()         { return idEntrenador; }
    public String getEspecialidad()      { return especialidad; }
    public int getAniosExperiencia()     { return aniosExperiencia; }
    public String getHorario()           { return horario; }
    public double getSalario()           { return salario; }
    public String getFechaContratacion() { return fechaContratacion; }
    public int getIdUsuario()            { return idUsuario; }
    public void setIdEntrenador(int v)          { idEntrenador = v; }
    public void setEspecialidad(String v)       { especialidad = v; }
    public void setAniosExperiencia(int v)      { aniosExperiencia = v; }
    public void setHorario(String v)            { horario = v; }
    public void setSalario(double v)            { salario = v; }
    public void setFechaContratacion(String v)  { fechaContratacion = v; }
    public void setIdUsuario(int v)             { idUsuario = v; }
}
