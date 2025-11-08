/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.anuncios.models;

/**
 *
 * @author pablo
 */
//Clase delegada para poder operar con el request para poder cambiar el precio de la configuracion del anuncio
public class CambiarPrecioDTORequest {

    private String codigo;
    private String precio;

    public CambiarPrecioDTORequest() {
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

}
