/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.anuncios.models;

/**
 *
 * @author pablo
 */
//Clase delegada para operar con el objeto dto para poder cambiar el estado de los anuncios
public class CambiarEstadoDTO {
    
    private boolean estado;
    private String idAnuncio;

    public CambiarEstadoDTO(boolean estado, String idAnuncio) {
        this.estado = estado;
        this.idAnuncio = idAnuncio;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getIdAnuncio() {
        return idAnuncio;
    }

    public void setIdAnuncio(String idAnuncio) {
        this.idAnuncio = idAnuncio;
    }
    
}
