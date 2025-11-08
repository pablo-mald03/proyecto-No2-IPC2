/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.cine.services;

import com.pablocompany.rest.api.proyectono2ipc2.administradorsistema.models.CantidadRegistrosDTO;
import com.pablocompany.rest.api.proyectono2ipc2.cine.database.CineDB;
import com.pablocompany.rest.api.proyectono2ipc2.cine.dtos.CantidadCargaRequest;
import com.pablocompany.rest.api.proyectono2ipc2.cine.dtos.CineRequest;
import com.pablocompany.rest.api.proyectono2ipc2.cine.dtos.EditarCineRequest;
import com.pablocompany.rest.api.proyectono2ipc2.cine.models.Cine;
import com.pablocompany.rest.api.proyectono2ipc2.cine.models.CineDTO;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author pablo
 */
//Clase delegada para poder operar con el crud de los cines 
public class CineCrudService {

    //Metodo utilizado para crear cines
    public boolean crearCine(CineRequest request) throws FormatoInvalidoException, ErrorInesperadoException, DatosNoEncontradosException {

        CineDTO nuevoCine = extraerDatos(request);

        CineDB cineDb = new CineDB();

        int cantidadAcutal = cineDb.cantidadCinesRegistrados();

        String codigoNuevoCine = generarCodigoCine(cantidadAcutal);

        nuevoCine.setCodigo(codigoNuevoCine);

        if (nuevoCine.validarCine()) {

            String nombreBuscado = cineDb.verificarNombreDuplicado(nuevoCine.getNombre());

            if (StringUtils.isNotBlank(nombreBuscado) && nombreBuscado.equals(nuevoCine.getNombre())) {
                throw new FormatoInvalidoException("Este nombre de cine ya existe");
            }

            return cineDb.crearNuevoCine(nuevoCine);

        }

        throw new ErrorInesperadoException("No se ha podido crear el nuevo cine");
    }

    //Metodo delegado para poder extraer los datos del cine request 
    private CineDTO extraerDatos(CineRequest request) {

        return new CineDTO(
                request.getCodigo(),
                request.getNombre(),
                request.isEstadoAnuncio(),
                request.getMontoOcultacion(),
                request.getFechaCreacion(),
                request.getDescripcion(),
                request.getUbicacion(),
                request.getCostoCine());

    }

    //Metodo que ayuda a auto nombrar el codigo de cine
    private String generarCodigoCine(int cantidadActual) {
        int siguiente = cantidadActual + 1;
        String numeroFormateado = String.format("%03d", siguiente);
        return "C-" + numeroFormateado;
    }

    //Metodo delegado para retornar todo el listado de cines asociados en el sistema
    public List<CineDTO> obtenerCinesAsociados(String limite, String offset) throws FormatoInvalidoException, ErrorInesperadoException {

        CantidadCargaRequest cantidadCargaRequest = extraerLimites(limite, offset);

        if (cantidadCargaRequest.validarRequest()) {

            CineDB cineDb = new CineDB();

            return cineDb.obtenerListadoCinesAsociados(cantidadCargaRequest);

        }
        throw new ErrorInesperadoException("No se ha podido obtener el listado de cines asociados");
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

    //Metodo utilizado para obtener la informacion del cine seleccionado
    public Cine obtenerCinePorCodigo(String codigoCine) throws FormatoInvalidoException, ErrorInesperadoException, DatosNoEncontradosException {

        if (!StringUtils.isNumeric(codigoCine)) {
            throw new FormatoInvalidoException("El codigo del cine esta vacio");
        }

        CineDB cineDb = new CineDB();

        return cineDb.obtenerCine(codigoCine);

    }

    //Metodo utilizado para crear cines
    public boolean editarCine(EditarCineRequest request) throws FormatoInvalidoException, ErrorInesperadoException, DatosNoEncontradosException {

        Cine cineEditado = extraerDatosEdicion(request);

        CineDB cineDb = new CineDB();

        if (cineEditado.validarCineEdicion()) {

            String nombreBuscado = cineDb.verificarNombreDuplicado(cineEditado.getNombre());

            if (StringUtils.isNotBlank(nombreBuscado) && nombreBuscado.equals(cineEditado.getNombre())) {
                throw new FormatoInvalidoException("Este nombre de cine ya existe");
            }

            return cineDb.editarCine(cineEditado);

        }

        throw new ErrorInesperadoException("No se ha podido editar el cine");
    }

    //Metodo que permite extraer los datos del request para editar
    private Cine extraerDatosEdicion(EditarCineRequest request) throws FormatoInvalidoException {

        try {

            if (StringUtils.isBlank(request.getMontoOcultacion())) {
                throw new FormatoInvalidoException("El monto de ocultacion nuevo esta vacio");
            }

            return new Cine(
                    request.getCodigo(),
                    request.getNombre(),
                    Double.parseDouble(request.getMontoOcultacion()),
                    request.getDescripcion(),
                    request.getUbicacion());

        } catch (NumberFormatException e) {
            throw new FormatoInvalidoException("El monto de ocultacion nuevo no es numerico");
        }

    }

}
