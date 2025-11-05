/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models;

/**
 *
 * @author pablo
 */
//Clase delegada para poder generar el listado de usuarios que pertenecen al reporte
public class UsuarioReporteDTO {

    private String id;
    private String nombre;
    private String correo;
    private int boletosComprados;
    private double totalPagado;

    public UsuarioReporteDTO(String id, String nombre, String correo, int boletosComprados, double totalPagado) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.boletosComprados = boletosComprados;
        this.totalPagado = totalPagado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getBoletosComprados() {
        return boletosComprados;
    }

    public void setBoletosComprados(int boletosComprados) {
        this.boletosComprados = boletosComprados;
    }

    public double getTotalPagado() {
        return totalPagado;
    }

    public void setTotalPagado(double totalPagado) {
        this.totalPagado = totalPagado;
    }

}
