/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.anuncios.services;

import com.pablocompany.rest.api.proyectono2ipc2.anuncios.database.AnunciosDB;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.dtos.AnuncioRegistradoDTOResponse;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.dtos.CambiarEstadoClienteRequest;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.dtos.CambiarEstadoRequest;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.dtos.CantidadAnunciosClienteRequest;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.models.CambiarEstadoDTO;
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
//Clase delegada para retornarle al usuario los anuncios que ha comprado
public class AnunciosRegistradosClienteService {

    //Metodo utilizado para poder retornar el listado de anuncios comprados en el sistema
    public List<AnuncioRegistradoDTOResponse> obtenerAnunciosRegistradosCliente(String limit, String offset, String idUsuario) throws FormatoInvalidoException, ErrorInesperadoException {
        
        CantidadAnunciosClienteRequest cantidadCargaRequest = extraerLimites(limit, offset, idUsuario);
        
        AnunciosDB anuncioDb = new AnunciosDB();
        
        if (cantidadCargaRequest.validarRequest()) {
            
            return anuncioDb.anunciosRegistradosAnunciante(cantidadCargaRequest);
        }
        
        throw new ErrorInesperadoException("No se ha podido procesar la solicitud del reporte de anuncios comprados");
    }

    //Metodo delegado para poder retornar y validar el request de limite de muestreo de los anuncios
    private CantidadAnunciosClienteRequest extraerLimites(String limite, String offset, String idUsuario) throws FormatoInvalidoException {
        
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
            
            return new CantidadAnunciosClienteRequest(
                    Integer.parseInt(offset),
                    Integer.parseInt(limite),
                    idUsuario);
            
        } catch (NumberFormatException e) {
            throw new FormatoInvalidoException("Los limites no son numericos");
        }
        
    }

    //Metodo que sirve para retornar la cantidad de anuncios que se han registrado/comprado por el cliente
    public CantidadReportesDTO cantidadAnunciosComprados(String idUsuario) throws FormatoInvalidoException, ErrorInesperadoException, DatosNoEncontradosException {
        
        if (StringUtils.isBlank(idUsuario)) {
            throw new FormatoInvalidoException("El id del usuario esta vacio");
        }
        
        AnunciosDB anuncioDb = new AnunciosDB();
        
        int cantidadRegistros = anuncioDb.cantidadAnunciosCliente(idUsuario);
        
        return new CantidadReportesDTO(cantidadRegistros);
    }

    //Metodo que permite indicar si el cambio de estado fue exitoso para el cliente
    public boolean cambiarEstadoAnuncio(CambiarEstadoClienteRequest estadoRequest) throws FormatoInvalidoException, ErrorInesperadoException, DatosNoEncontradosException {
        
        if (StringUtils.isBlank(estadoRequest.getEstado())) {
            throw new FormatoInvalidoException("El valor de cambio del anuncio esta vacio");
        }
        
        if (StringUtils.isBlank(estadoRequest.getIdAnuncio())) {
            throw new FormatoInvalidoException("El valor de id del anuncio esta vacio");
        }
        
        if (StringUtils.isBlank(estadoRequest.getIdCliente())) {
            throw new FormatoInvalidoException("El valor de id del cliente esta vacio");
        }
        
        String estadoStr = estadoRequest.getEstado().trim().toLowerCase();
        if (!estadoStr.equals("true") && !estadoStr.equals("false")) {
            throw new FormatoInvalidoException("El estado solo puede ser 'true' o 'false'");
        }
        
        CambiarEstadoDTO cambiarEstadoDto = new CambiarEstadoDTO(
                Boolean.parseBoolean(estadoRequest.getEstado().trim()),
                estadoRequest.getIdAnuncio());
        
        AnunciosDB anuncioDb = new AnunciosDB();
        
        return anuncioDb.cambiarEstado(cambiarEstadoDto);
        
    }
}
