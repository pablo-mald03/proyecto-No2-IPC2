/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.anuncios.services;

import com.pablocompany.rest.api.proyectono2ipc2.anuncios.database.AnunciosDB;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.dtos.AnuncioRegistradoDTOResponse;
import com.pablocompany.rest.api.proyectono2ipc2.cine.dtos.CantidadCargaRequest;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models.CantidadReportesDTO;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author pablo
 */
public class AnunciosRegistradosService {

    //Metodo utilizado para poder retornar el listado de anuncios comprados en el sistema
    public List<AnuncioRegistradoDTOResponse> obtenerAnunciosRegistrados(String limit, String offset) throws FormatoInvalidoException, ErrorInesperadoException {

        CantidadCargaRequest cantidadCargaRequest = extraerLimites(limit, offset);

        AnunciosDB anuncioDb = new AnunciosDB();

        if (cantidadCargaRequest.validarRequest()) {

            return anuncioDb.anunciosRegistradosSistema(cantidadCargaRequest);
        }

        throw new ErrorInesperadoException("No se ha podido procesar la solicitud del reporte de anuncios comprados");
    }

    //Metodo delegado para poder retornar y validar el request de limite de muestreo de los anuncios
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

    //Metodo que sirve para retornar la cantidad de anuncios que se han registrado/comprado en el sistema
    public CantidadReportesDTO cantidadReportesSinFiltro() throws FormatoInvalidoException, ErrorInesperadoException, DatosNoEncontradosException {

        AnunciosDB anuncioDb = new AnunciosDB();

        int cantidadRegistros = anuncioDb.cantidadAnunciosSistema();

        return new CantidadReportesDTO(cantidadRegistros);
    }
}
