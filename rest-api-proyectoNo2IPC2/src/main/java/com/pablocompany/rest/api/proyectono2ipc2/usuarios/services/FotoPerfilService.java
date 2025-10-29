/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.usuarios.services;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.EntidadNoExistenteException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.database.UsuarioDB;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author pablo
 */
//Metodo util para poder obtener la foto de perfil del usuario
public class FotoPerfilService {

    //Clase delegada para obtener la foto de perfil del usuario
    public String obtenerFotoPerfil(String id) throws EntidadNoExistenteException, FormatoInvalidoException, ErrorInesperadoException {

        if (StringUtils.isBlank(id)) {

            throw new FormatoInvalidoException("No se ha enviado ningun id de usuario");
        }

        UsuarioDB usuarioDb = new UsuarioDB();

        byte[] fotoPerfil = usuarioDb.obtenerFotoPerfil(id);

        if (fotoPerfil == null) {
            throw new ErrorInesperadoException("No se han podido obtener la foto de perfil del usuario");
        }

        return convertirFotoBase64(fotoPerfil);

    }

    //Metodo utilizado para convertir a base 64 la foto
    public String convertirFotoBase64(byte[] fotoPerfil) throws ErrorInesperadoException {

        if (fotoPerfil != null) {

            return Base64.getEncoder().encodeToString(fotoPerfil);

        } else {
            try (InputStream defaultImg = getClass().getResourceAsStream("/icons/defalutUser.png")) {
                byte[] defaultFoto = defaultImg.readAllBytes();

                return Base64.getEncoder().encodeToString(defaultFoto);

            } catch (IOException ex) {
                throw new ErrorInesperadoException("No se han podido obtener la foto de perfil predeterminada");
            }
        }

    }
}
