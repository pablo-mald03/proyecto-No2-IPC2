/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.anuncios.models;

/**
 *
 * @author pablo
 */
//Enums que representan los tipos de anuncios admitidos por la web
public enum TipoAnuncioEnum {
    
    ANUNCIO_TEXTO(1),
    IMAGEN_TEXTO(2),
    VIDEO_TEXTO(3);
    
    private int contexto; 

    private TipoAnuncioEnum(int contexto) {
        this.contexto = contexto;
    }

    public int getContexto() {
        return contexto;
    }
    
}
