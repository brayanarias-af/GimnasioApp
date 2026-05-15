package Gimnasio.Modelo;

public class Maquina {

    private int    idMaquina;
    private String nombre;
    private String tipo;
    private String marca;
    private String modelo;
    private String serial;
    private String estado;
    private String fechaMantenimiento;

    public Maquina() {}

    public Maquina(int idMaquina, String nombre, String tipo, String marca,
                   String modelo, String serial, String estado, String fechaMantenimiento) {
        this.idMaquina = idMaquina; this.nombre = nombre; this.tipo = tipo;
        this.marca = marca; this.modelo = modelo; this.serial = serial;
        this.estado = estado; this.fechaMantenimiento = fechaMantenimiento;
    }

    public int    getIdMaquina()          { return idMaquina; }
    public String getNombre()             { return nombre; }
    public String getTipo()               { return tipo; }
    public String getMarca()              { return marca; }
    public String getModelo()             { return modelo; }
    public String getSerial()             { return serial; }
    public String getEstado()             { return estado; }
    public String getFechaMantenimiento() { return fechaMantenimiento; }

    public void setIdMaquina(int v)             { this.idMaquina = v; }
    public void setNombre(String v)             { this.nombre = v; }
    public void setTipo(String v)               { this.tipo = v; }
    public void setMarca(String v)              { this.marca = v; }
    public void setModelo(String v)             { this.modelo = v; }
    public void setSerial(String v)             { this.serial = v; }
    public void setEstado(String v)             { this.estado = v; }
    public void setFechaMantenimiento(String v) { this.fechaMantenimiento = v; }
}
