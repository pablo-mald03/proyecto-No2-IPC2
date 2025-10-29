/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.usuarios.services;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import java.util.Base64;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author pablo
 */
//Clase delegada para ocuparse del cifrado y salteado de passwords
public class CifrarPasswordService {

    //Metodo encargado de cifrar la password del usuario
    public final String cifrarPassword(String password, String correo) throws FormatoInvalidoException {

        if (StringUtils.isBlank(password)) {
            throw new FormatoInvalidoException("La contraseña del usuario se encentra vacio");

        }

        if (StringUtils.isBlank(correo)) {
            throw new FormatoInvalidoException("El correo del usuario se encentra vacio");

        }

        int indiceArroba = correo.trim().indexOf("@");

        String inicio = correo.trim().substring(0, 1);
        String fin = correo.trim().substring(indiceArroba - 1, indiceArroba);

        String consecuente = correo.trim().substring(indiceArroba + 1, indiceArroba + 2);

        String passwordSalteada = consecuente + inicio + password.trim() + fin;

        return Base64.getEncoder().encodeToString(passwordSalteada.getBytes());

    }

}
