/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.peliculas.dtos;

import java.io.InputStream;
import java.time.LocalDate;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

/**
 *
 * @author pablo
 */
//Clase delegada para manejar el objeto indicado para poder crear la nueva pelicula
public class PeliculaRequest {

    private String nombre;
    private String sinopsis;

    private String cast;
    private String director;
    private String fechaEstreno;
    private String clasificacion;
    private double duracion;
    private String precio;
    private InputStream poster;
    
    private FormDataContentDisposition fileDetail;

    public PeliculaRequest(String nombre, String sinopsis, String cast, String pdirector, String fechaEstreno, String clasificacion, double duracion, String precio, InputStream poster, FormDataContentDisposition fileDetail) {
        this.nombre = nombre;
        this.sinopsis = sinopsis;
        this.cast = cast;
        this.director = pdirector;
        this.fechaEstreno = fechaEstreno;
        this.clasificacion = clasificacion;
        this.duracion = duracion;
        this.precio = precio;
        this.poster = poster;
        this.fileDetail = fileDetail;
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

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String pdirector) {
        this.director = pdirector;
    }

    public String getFechaEstreno() {
        return fechaEstreno;
    }

    public void setFechaEstreno(String fechaEstreno) {
        this.fechaEstreno = fechaEstreno;
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

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public InputStream getPoster() {
        return poster;
    }

    public void setPoster(InputStream poster) {
        this.poster = poster;
    }

    public FormDataContentDisposition getFileDetail() {
        return fileDetail;
    }

    public void setFileDetail(FormDataContentDisposition fileDetail) {
        this.fileDetail = fileDetail;
    }

   

}
