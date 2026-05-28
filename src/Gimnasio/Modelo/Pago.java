package Gimnasio.Modelo;

public class Pago {
    private int    idPago, idCliente, idMembresia;
    private String fechaPago, fechaInicio, fechaFin;
    private String metodoPago, referenciaPago, estado;
    private String nombreCliente, nombreMembresia;
    private double monto;

    public Pago() {}

    // ── Getters ────────────────────────────────────────────────
    public int    getIdPago()            { return idPago; }
    public int    getIdCliente()         { return idCliente; }
    public int    getIdMembresia()       { return idMembresia; }
    public String getFechaPago()         { return fechaPago; }
    public String getFechaInicio()       { return fechaInicio; }
    public String getFechaFin()          { return fechaFin; }
    public String getMetodoPago()        { return metodoPago; }
    public String getReferenciaPago()    { return referenciaPago; }
    public String getEstado()            { return estado; }
    public String getNombreCliente()     { return nombreCliente; }
    public String getNombreMembresia()   { return nombreMembresia; }
    public double getMonto()             { return monto; }

    // ── Setters ────────────────────────────────────────────────
    public void setIdPago(int v)             { idPago = v; }
    public void setIdCliente(int v)          { idCliente = v; }
    public void setIdMembresia(int v)        { idMembresia = v; }
    public void setFechaPago(String v)       { fechaPago = v; }
    public void setFechaInicio(String v)     { fechaInicio = v; }
    public void setFechaFin(String v)        { fechaFin = v; }
    public void setMetodoPago(String v)      { metodoPago = v; }
    public void setReferenciaPago(String v)  { referenciaPago = v; }
    public void setEstado(String v)          { estado = v; }
    public void setNombreCliente(String v)   { nombreCliente = v; }
    public void setNombreMembresia(String v) { nombreMembresia = v; }
    public void setMonto(double v)           { monto = v; }
}
