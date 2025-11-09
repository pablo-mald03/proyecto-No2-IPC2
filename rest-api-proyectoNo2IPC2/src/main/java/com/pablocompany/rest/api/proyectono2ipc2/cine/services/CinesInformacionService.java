/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.cine.services;

import com.pablocompany.rest.api.proyectono2ipc2.administradorsistema.models.CantidadRegistrosDTO;
import com.pablocompany.rest.api.proyectono2ipc2.cine.database.CineDB;
import com.pablocompany.rest.api.proyectono2ipc2.cine.dtos.CantidadCargaRequest;
import com.pablocompany.rest.api.proyectono2ipc2.cine.models.CineAsociadoDTOResponse;
import com.pablocompany.rest.api.proyectono2ipc2.cine.models.CineInformacionDTO;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author pablo
 */
//Clase delegada para producir datos para que se puedan ver en general informacion segura 
public class CinesInformacionService {

    //Metodo delegado para proporcionar los datos llave valor de cines
    public List<CineAsociadoDTOResponse> obtenerCinesAsociadosValores(String limite, String offset) throws FormatoInvalidoException, ErrorInesperadoException {

        CantidadCargaRequest cantidadCargaRequest = extraerLimites(limite, offset);

        if (cantidadCargaRequest.validarRequest()) {

            CineDB cineDb = new CineDB();

            return cineDb.obtenerListadoCinesLlaveValor(cantidadCargaRequest);

        }
        throw new ErrorInesperadoException("No se ha podido obtener el listado de cines asociados llave valor");
    }

    //Metodo delegado para poder retornar y validar el request de limite de muestreo de cines
    private CantidadCargaRequest extraerLimites(String limite, String offset) throws FormatoInvalidoException {

        if (StringUtils.isBlank(limite)) {
            throw new FormatoInvalidoException("El limite superior de la peticion vacio");
        }

        if (StringUtils.isBlank(offset)) {
            throw new FormatoInvalidoException("El limite inferior de la peticion vacio");
        }

        if (!StringUtils.isNumeric(limite)) {
            throw new FormatoInvalidoException("El limite superior de la peticion no es numerico");
        }

        if (!StringUtils.isNumeric(offset)) {
            throw new FormatoInvalidoException("El limite inferior de la peticion no es numerico");
        }

        try {

            return new CantidadCargaRequest(
                    Integer.parseInt(offset),
                    Integer.parseInt(limite));

        } catch (NumberFormatException e) {
            throw new FormatoInvalidoException("Los limites no son numericos");
        }

    }

    //Metodo que permite retornar la cantidad de cines asociados en la aplicacion
    public CantidadRegistrosDTO obtenerCantidadCines() throws ErrorInesperadoException, DatosNoEncontradosException {
        CineDB cineDb = new CineDB();
        return new CantidadRegistrosDTO(cineDb.cantidadCinesRegistrados());
    }

    //Metodo que retorna los datos de informacion para la pagina principal de usuarios
    public List<CineInformacionDTO> obtenerCinesPrincipal(String limite, String offset) throws FormatoInvalidoException, ErrorInesperadoException {

        CantidadCargaRequest cantidadCargaRequest = extraerLimites(limite, offset);

        if (cantidadCargaRequest.validarRequest()) {

            CineDB cineDb = new CineDB();

            return cineDb.obtenerListadoCinesPrincipal(cantidadCargaRequest);

        }
        throw new ErrorInesperadoException("No se ha podido obtener el listado de cines asociados llave valor");
    }

    //Metodo que retorna los datos de informacion de un cine en especifico
    public CineInformacionDTO obtenerCinePrincipalCodigo(String idCine) throws FormatoInvalidoException, ErrorInesperadoException, DatosNoEncontradosException {

        if (StringUtils.isBlank(idCine)) {
            throw new FormatoInvalidoException("El id del cine esta vacio");
        }

        CineDB cineDb = new CineDB();

        return cineDb.obtenerCinesPrincipalCodigo(idCine);

    }

}
