/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.peliculas.models;

import java.time.LocalDate;

/**
 *
 * @author pablo
 */
//Clase delegada para manejar el objeto indicado para poder crear la nueva pelicula
public class Pelicula {

    private String codigo;
    private String nombre;
    private byte[] poster;
    private String sinopsis;
    private String cast;
    private LocalDate fechaEstreno;
    private String pdirector;
    private double precio;
    private String clasificacion;
    private double duracion;

    public Pelicula(String codigo, String nombre, byte[] poster, String sinopsis, String cast, LocalDate fechaEstreno, String pdirector, double precio, String clasificacion, double duracion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.poster = poster;
        this.sinopsis = sinopsis;
        this.cast = cast;
        this.fechaEstreno = fechaEstreno;
        this.pdirector = pdirector;
        this.precio = precio;
        this.clasificacion = clasificacion;
        this.duracion = duracion;
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

    public byte[] getPoster() {
        return poster;
    }

    public void setPoster(byte[] poster) {
        this.poster = poster;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public LocalDate getFechaEstreno() {
        return fechaEstreno;
    }

    public void setFechaEstreno(LocalDate fechaEstreno) {
        this.fechaEstreno = fechaEstreno;
    }

    public String getPdirector() {
        return pdirector;
    }

    public void setPdirector(String pdirector) {
        this.pdirector = pdirector;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
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
