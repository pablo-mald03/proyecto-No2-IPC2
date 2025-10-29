/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.usuarios.models;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.services.CifrarPasswordService;

/**
 *
 * @author pablo
 */
//Clase que permite comprobar que el correo y password exista
public class LoginDTO {

    private String correo;
    private String password;

    public LoginDTO(String correo, String password) {
        this.correo = correo;
        this.password = password;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //Metodo delegado para obtener la password cifrada
    public String getPasswordCifrada() throws FormatoInvalidoException {
        CifrarPasswordService cifrarPasswordService = new CifrarPasswordService();
        return cifrarPasswordService.cifrarPassword(this.password, this.correo);
    }

}
