/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.anuncios.models;

/**
 *
 * @author pablo
 */
//Clase delegada para representar los objetos que se retornarn a las paginas principales para ser mostrados
public class AnunciosPublicidadDTO {
    
    
    private String nombre;
    private String url;
    private String foto;
    private int codigoTipo;

    public AnunciosPublicidadDTO(String nombre, String url, String foto, int codigoTipo) {
        this.nombre = nombre;
        this.url = url;
        this.foto = foto;
        this.codigoTipo = codigoTipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getCodigoTipo() {
        return codigoTipo;
    }

    public void setCodigoTipo(int codigoTipo) {
        this.codigoTipo = codigoTipo;
    }
    
    
    
    
    
}
