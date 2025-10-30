/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.usuarios.services;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.EntidadNoExistenteException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.database.UsuarioDB;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.dtos.CambioCredencialesRequest;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.models.CambioCredencialesDTO;

/**
 *
 * @author pablo
 */
//Clase delegada para poder ejecutar la logica de negocio de cambio de credenciales
public class CambioCredencialesService {

    //metodo delegado para poder indicar si es correcto el cambio de credenciales
    public boolean cambiarCredenciales(CambioCredencialesRequest request) throws FormatoInvalidoException, ErrorInesperadoException, EntidadNoExistenteException {

        CambioCredencialesDTO cambioCredencialesDTO = extraerCredenciales(request);

        if (cambioCredencialesDTO.esValido()) {

            UsuarioDB usuarioDb = new UsuarioDB();

            if (usuarioDb.credencialesExistentes(cambioCredencialesDTO.getCorreo(), cambioCredencialesDTO.getIdUsuario())) {

                if (usuarioDb.reestablecerCredenciales(cambioCredencialesDTO)) {
                    return true;
                }

            } else {
                throw new EntidadNoExistenteException("No se posee ningun registro con esas credenciales");
            }

        }

        throw new ErrorInesperadoException("No se ha podido reestablecer las credenciales del usuario");

    }

    //Metodo delegado para extraer la data extraida
    private CambioCredencialesDTO extraerCredenciales(CambioCredencialesRequest request) throws FormatoInvalidoException {

        if (request == null) {
            throw new FormatoInvalidoException("La solicitud de datos esta vacia");
        }

        return new CambioCredencialesDTO(
                request.getCorreo().trim(),
                request.getIdUsuario().trim(),
                request.getPassword().trim(),
                request.getPasswordConfirm().trim());

    }

}
