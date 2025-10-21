/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.usuarios.models;

/**
 *
 * @author pablo
 */
public enum TipoUsuarioEnum {
    
    ADMINISTRADOR_SISTEMA("ADMINISTRADOR DE SISTEMA"),
    ADMINISTRADOR_CINE("ADMINISTRADOR DE CINE"),
    USUARIO("USUARIO"),
    USUARIO_ESPECIAL("ANUNCIANTE");
    
    
    TipoUsuarioEnum(String contextoUser){
        this.contexto = contextoUser;
    }
    
    
    private String contexto;
    
    public String getContexto(){
        return this.contexto;
    }
}
