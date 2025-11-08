/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.anuncios.dtos;

/**
 *
 * @author pablo
 */
//Clase delegada para poder manejar el objeto que permite retornar la vigencia que tienen los anuncios
public class VigenciaAnuncioDTO {
    
    private int codigo;
    private String contexto;
    private double precio;
    private double duracion;

    public VigenciaAnuncioDTO(int codigo, String contexto, double precio, double duracion) {
        this.codigo = codigo;
        this.contexto = contexto;
        this.precio = precio;
        this.duracion = duracion;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getContexto() {
        return contexto;
    }

    public void setContexto(String contexto) {
        this.contexto = contexto;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getDuracion() {
        return duracion;
    }

    public void setDuracion(double duracion) {
        this.duracion = duracion;
    }
    
    
    
    
}
