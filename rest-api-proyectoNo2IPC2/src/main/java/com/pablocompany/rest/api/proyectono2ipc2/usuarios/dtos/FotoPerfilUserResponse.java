/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.usuarios.dtos;

/**
 *
 * @author pablo
 */
//Clase que permite retornar la foto de perfil en base64
public class FotoPerfilUserResponse {
    
    private String fotoPerfil;

    public FotoPerfilUserResponse(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }
    
    
}
