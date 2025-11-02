/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models;

/**
 *
 * @author pablo
 */
//Clase delegada para operar con el objeto de usuario que a comprado los boletos
public class UsuarioBoletosCompradosDTO {

    private String idUsuario;
    private String nombre;
    private int boletosComprados;
    private double totalPagado;

    public UsuarioBoletosCompradosDTO(String idUsuario, String nombre, int boletosComprados, double totalPagado) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.boletosComprados = boletosComprados;
        this.totalPagado = totalPagado;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
