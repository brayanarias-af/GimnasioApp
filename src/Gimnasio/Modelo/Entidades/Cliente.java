package Gimnasio.Modelo.Entidades;

public class Cliente extends Persona {
    private String codigoCliente;
    private double peso;
    private double altura;
    private String objetivo;
    private String estadoMembresia;
    private String fechaIngreso;
    private Rutina rutina;
    private ProgresoFisico progreso;

    public Cliente() {}

    public Cliente(int idUsuario, String cedula, String nombres, String apellidos, int edad,
                   String sexo, String telefono, String correo, String direccion, String rol,
                   String codigoCliente, double peso, double altura, String objetivo,
                   String estadoMembresia, String fechaIngreso) {
        super(idUsuario, cedula, nombres, apellidos, edad, sexo, telefono, correo, direccion, rol);
        this.codigoCliente = codigoCliente;
        this.peso = peso;
        this.altura = altura;
        this.objetivo = objetivo;
        this.estadoMembresia = estadoMembresia;
        this.fechaIngreso = fechaIngreso;
    }

   
    public String getCodigoCliente() { return codigoCliente; }
    public void setCodigoCliente(String codigoCliente) { this.codigoCliente = codigoCliente; }

    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }

    public double getAltura() { return altura; }
    public void setAltura(double altura) { this.altura = altura; }

    public String getObjetivo() { return objetivo; }
    public void setObjetivo(String objetivo) { this.objetivo = objetivo; }

    public String getEstadoMembresia() { return estadoMembresia; }
    public void setEstadoMembresia(String estadoMembresia) { this.estadoMembresia = estadoMembresia; }

    public String getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(String fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public Rutina getRutina() { return rutina; }
    public void setRutina(Rutina rutina) { this.rutina = rutina; }

    public ProgresoFisico getProgreso() { return progreso; }
    public void setProgreso(ProgresoFisico progreso) { this.progreso = progreso; }
}
