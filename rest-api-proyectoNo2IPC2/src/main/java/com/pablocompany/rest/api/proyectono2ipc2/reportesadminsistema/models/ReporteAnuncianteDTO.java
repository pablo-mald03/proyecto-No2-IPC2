/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pablo
 */
//Clase delegada para poder enviar el listado de los reportes de anunciantes
public class ReporteAnuncianteDTO {

    private String id;
    private String nombre;
    private String correo;
    private double total;
    private List<AnuncioDTO> anuncios;

    public ReporteAnuncianteDTO(String id, String nombre, String correo, double total) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.total = total;
        this.anuncios = new ArrayList<>();
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

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<AnuncioDTO> getAnuncios() {
        return anuncios;
    }

    public void setAnuncios(List<AnuncioDTO> anuncios) {
        this.anuncios = anuncios;
    }

    
}
