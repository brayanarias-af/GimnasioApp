package Gimnasio.Modelo;

public class Cliente extends Persona {

    private int    idCliente;
    private String telefonoEmergencia;
    private String eps;
    private double peso;
    private double altura;
    private String objetivo;
    private String fechaIngreso;
    private String estadoMembresia;
    private String observaciones;
    private int    idUsuario;

    public Cliente() {}

    public Cliente(int idCliente, String cedula, String nombres, String apellidos,
                   int edad, String sexo, String telefono, String telefonoEmergencia,
                   String correo, String direccion, String eps,
                   double peso, double altura, String objetivo,
                   String fechaIngreso, String estadoMembresia, String observaciones, int idUsuario) {
        super(0, cedula, nombres, apellidos, edad, sexo, telefono, correo, direccion);
        this.idCliente          = idCliente;
        this.telefonoEmergencia = telefonoEmergencia;
        this.eps                = eps;
        this.peso               = peso;
        this.altura             = altura;
        this.objetivo           = objetivo;
        this.fechaIngreso       = fechaIngreso;
        this.estadoMembresia    = estadoMembresia;
        this.observaciones      = observaciones;
        this.idUsuario          = idUsuario;
    }

    // Getters
    public int    getIdCliente()           { return idCliente; }
    public String getTelefonoEmergencia()  { return telefonoEmergencia; }
    public String getEps()                 { return eps; }
    public double getPeso()                { return peso; }
    public double getAltura()              { return altura; }
    public String getObjetivo()            { return objetivo; }
    public String getFechaIngreso()        { return fechaIngreso; }
    public String getEstadoMembresia()     { return estadoMembresia; }
    public String getObservaciones()       { return observaciones; }
    public int    getIdUsuario()           { return idUsuario; }

    // Setters
    public void setIdCliente(int v)              { this.idCliente = v; }
    public void setTelefonoEmergencia(String v)  { this.telefonoEmergencia = v; }
    public void setEps(String v)                 { this.eps = v; }
    public void setPeso(double v)                { this.peso = v; }
    public void setAltura(double v)              { this.altura = v; }
    public void setObjetivo(String v)            { this.objetivo = v; }
    public void setFechaIngreso(String v)        { this.fechaIngreso = v; }
    public void setEstadoMembresia(String v)     { this.estadoMembresia = v; }
    public void setObservaciones(String v)       { this.observaciones = v; }
    public void setIdUsuario(int v)              { this.idUsuario = v; }
}
