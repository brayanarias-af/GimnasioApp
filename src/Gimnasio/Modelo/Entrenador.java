package Gimnasio.Modelo;

public class Entrenador extends Persona {

    private int    idEntrenador;
    private String especialidad;
    private int    aniosExperiencia;
    private String horario;
    private double salario;
    private String fechaContratacion;
    private int    idUsuario;

    public Entrenador() {}

    public Entrenador(int idEntrenador, String cedula, String nombres, String apellidos,
                      String telefono, String correo, String especialidad,
                      int aniosExperiencia, String horario, double salario,
                      String fechaContratacion, int idUsuario) {
        super(0, cedula, nombres, apellidos, 0, "", telefono, correo, "");
        this.idEntrenador     = idEntrenador;
        this.especialidad     = especialidad;
        this.aniosExperiencia = aniosExperiencia;
        this.horario          = horario;
        this.salario          = salario;
        this.fechaContratacion= fechaContratacion;
        this.idUsuario        = idUsuario;
    }

    public int    getIdEntrenador()      { return idEntrenador; }
    public String getEspecialidad()      { return especialidad; }
    public int    getAniosExperiencia()  { return aniosExperiencia; }
    public String getHorario()           { return horario; }
    public double getSalario()           { return salario; }
    public String getFechaContratacion() { return fechaContratacion; }
    public int    getIdUsuario()         { return idUsuario; }

    public void setIdEntrenador(int v)      { this.idEntrenador = v; }
    public void setEspecialidad(String v)   { this.especialidad = v; }
    public void setAniosExperiencia(int v)  { this.aniosExperiencia = v; }
    public void setHorario(String v)        { this.horario = v; }
    public void setSalario(double v)        { this.salario = v; }
    public void setFechaContratacion(String v) { this.fechaContratacion = v; }
    public void setIdUsuario(int v)         { this.idUsuario = v; }
}
