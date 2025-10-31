/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models;

import java.time.LocalDate;

/**
 *
 * @author pablo
 */
//Clase delegada para poder operara con el respectivo reporte de las peliculas proyectadas en una sala
public class PeliculaProyectadaDTO {
    
    private LocalDate fechaProyeccion;
    private String nombre; 
    private String sinopsis;
    private String clasificacion; 
    private double duracion; 

    public PeliculaProyectadaDTO(LocalDate fechaProyeccion, String nombre, String sinopsis, String clasificacion, double duracion) {
        this.fechaProyeccion = fechaProyeccion;
        this.nombre = nombre;
        this.sinopsis = sinopsis;
        this.clasificacion = clasificacion;
        this.duracion = duracion;
    }

    public LocalDate getFechaProyeccion() {
        return fechaProyeccion;
    }

    public void setFechaProyeccion(LocalDate fechaProyeccion) {
        this.fechaProyeccion = fechaProyeccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public double getDuracion() {
        return duracion;
    }

    public void setDuracion(double duracion) {
        this.duracion = duracion;
    }

    
}
