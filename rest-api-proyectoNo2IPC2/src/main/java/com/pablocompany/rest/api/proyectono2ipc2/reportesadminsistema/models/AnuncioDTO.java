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
//Clase delegada para poder operar con las caracteristicas del anuncio que se expondran
public class AnuncioDTO {
    
    private String codigo;
    private boolean estado;
    private String nombre;
    private String tipo;
    private LocalDate fechaExpiracion;
    private LocalDate fechaCompra;

    public AnuncioDTO(String codigo, boolean estado, String nombre, String tipo, LocalDate fechaExpiracion, LocalDate fechaCompra) {
        this.codigo = codigo;
        this.estado = estado;
        this.nombre = nombre;
        this.tipo = tipo;
        this.fechaExpiracion = fechaExpiracion;
        this.fechaCompra = fechaCompra;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDate getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(LocalDate fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public LocalDate getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDate fechaCompra) {
        this.fechaCompra = fechaCompra;
    }
    
    
    
    
    
}
