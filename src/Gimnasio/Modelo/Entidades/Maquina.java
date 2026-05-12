/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Gimnasio.Modelo.Entidades;

/**
 *
 * @author brian
 */
public class Maquina {

    
    private String codigoMaquina;
    private String nombre;
    private String tipo;
    private String estado;
    private String marca;
    private String fechaMantenimiento;
    
    public Maquina() {
    }

    public Maquina(String codigoMaquina, String nombre, String tipo, String estado, String marca, String fechaMantenimiento) {
        this.codigoMaquina = codigoMaquina;
        this.nombre = nombre;
        this.tipo = tipo;
        this.estado = estado;
        this.marca = marca;
        this.fechaMantenimiento = fechaMantenimiento;
    }

    public String getCodigoMaquina() {
        return codigoMaquina;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public String getEstado() {
        return estado;
    }

    public String getMarca() {
        return marca;
    }

    public String getFechaMantenimiento() {
        return fechaMantenimiento;
    }

    public void setCodigoMaquina(String codigoMaquina) {
        this.codigoMaquina = codigoMaquina;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setFechaMantenimiento(String fechaMantenimiento) {
        this.fechaMantenimiento = fechaMantenimiento;
    }
    
}
