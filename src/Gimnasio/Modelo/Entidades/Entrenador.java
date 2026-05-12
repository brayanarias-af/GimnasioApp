/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Gimnasio.Modelo.Entidades;

/**
 *
 * @author brian
 */
public class Entrenador extends Persona{

    private String codigoEntrenador;
    private String especialidad;
    private int anosExperiencia;
    private String horario;
    private int salario;
    private String FechaContratacion;
    
    public Entrenador() {
    }

    public Entrenador(String codigoEntrenador, String especialidad, int anosExperiencia, String horario, int salario, String FechaContratacion, int idUsuario, String cedula, String nombres, String apellidos, int edad, String sexo, String telefono, String correo, String direccion, String rol) {
        super(idUsuario, cedula, nombres, apellidos, edad, sexo, telefono, correo, direccion, rol);
        this.codigoEntrenador = codigoEntrenador;
        this.especialidad = especialidad;
        this.anosExperiencia = anosExperiencia;
        this.horario = horario;
        this.salario = salario;
        this.FechaContratacion = FechaContratacion;
    }

    public String getCodigoEntrenador() {
        return codigoEntrenador;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public int getAnosExperiencia() {
        return anosExperiencia;
    }

    public String getHorario() {
        return horario;
    }

    public int getSalario() {
        return salario;
    }

    public String getFechaContratacion() {
        return FechaContratacion;
    }

    public void setCodigoEntrenador(String codigoEntrenador) {
        this.codigoEntrenador = codigoEntrenador;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public void setAnosExperiencia(int anosExperiencia) {
        this.anosExperiencia = anosExperiencia;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public void setSalario(int salario) {
        this.salario = salario;
    }

    public void setFechaContratacion(String FechaContratacion) {
        this.FechaContratacion = FechaContratacion;
    }
    
    

}
