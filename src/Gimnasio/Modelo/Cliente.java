package Gimnasio.Modelo;
public class Cliente extends Persona {
    private int idCliente, idUsuario, edad;
    private String telefonoEmergencia, eps, objetivo, fechaIngreso, estadoMembresia, observaciones, sexo;
    private double peso, altura;

    public Cliente() {}

    public Cliente(int idCliente, String cedula, String nombres, String apellidos,
                   int edad, String sexo, String telefono, String correo, String direccion,
                   String telefonoEmergencia, String eps, double peso, double altura,
                   String objetivo, String fechaIngreso, String estadoMembresia,
                   String observaciones, int idUsuario) {
        super(0, cedula, nombres, apellidos, edad, sexo, telefono, correo, direccion);
        this.idCliente          = idCliente;
        this.idUsuario          = idUsuario;
        this.edad               = edad;
        this.telefonoEmergencia = telefonoEmergencia;
        this.eps                = eps;
        this.objetivo           = objetivo;
        this.fechaIngreso       = fechaIngreso;
        this.estadoMembresia    = estadoMembresia;
        this.observaciones      = observaciones;
        this.sexo               = sexo;
        this.peso               = peso;
        this.altura             = altura;
    }
    public int getIdCliente()           { return idCliente; }
    public int getIdUsuario()           { return idUsuario; }
    public String getTelefonoEmergencia(){ return telefonoEmergencia; }
    public String getEps()              { return eps; }
    public String getObjetivo()         { return objetivo; }
    public String getFechaIngreso()     { return fechaIngreso; }
    public String getEstadoMembresia()  { return estadoMembresia; }
    public String getObservaciones()    { return observaciones; }
    public String getSexo()             { return sexo; }
    public double getPeso()             { return peso; }
    public double getAltura()           { return altura; }
    public int getEdad()                { return edad; }
    public void setIdCliente(int v)              { idCliente = v; }
    public void setIdUsuario(int v)              { idUsuario = v; }
    public void setTelefonoEmergencia(String v)  { telefonoEmergencia = v; }
    public void setEps(String v)                 { eps = v; }
    public void setObjetivo(String v)            { objetivo = v; }
    public void setFechaIngreso(String v)        { fechaIngreso = v; }
    public void setEstadoMembresia(String v)     { estadoMembresia = v; }
    public void setObservaciones(String v)       { observaciones = v; }
    public void setSexo(String v)                { sexo = v; }
    public void setPeso(double v)                { peso = v; }
    public void setAltura(double v)              { altura = v; }
    public void setEdad(int v)                   { edad = v; }
}
