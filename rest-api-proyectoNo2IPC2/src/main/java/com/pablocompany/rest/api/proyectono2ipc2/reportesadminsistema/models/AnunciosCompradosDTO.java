/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models;

import java.time.LocalDate;

/**
 *
 * @author pablo
 */
//Clase delegada para poder generar el listado de compras en  un intervalo de tiempo para el reporte de ganancias
public class AnunciosCompradosDTO {

    private String codigo;
    private String nombre;
    private LocalDate fechaCompra;
    private double monto;
    private String usuario;

    public AnunciosCompradosDTO(String codigo, String nombre, LocalDate fechaCompra, double monto, String usuario) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.fechaCompra = fechaCompra;
        this.monto = monto;
        this.usuario = usuario;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDate fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

}
