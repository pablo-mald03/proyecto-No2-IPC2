/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.services;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models.CantidadReportesDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.database.ReporteAnunciosCompradosDB;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.dtos.ReporteAnuncioRequest;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models.ReporteAnuncioDTO;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author pablo
 */
//Clase delegada para poder generar los reportes de anuncios comprados
public class ReporteAnunciosCompradosService {

    //Metodo utilizado para poder retornar el reporte de anuncios comprados en un intervalo de tiempo SIN FILTRO
    public List<ReporteAnuncioDTO> obtenerReporteAnunciosCompradosSinFiltro(String fechaInicio, String fechaFin, String limit, String offset) throws FormatoInvalidoException, ErrorInesperadoException {

        ReporteAnuncioRequest reporteRequest = extraerDatosReporte(fechaInicio, fechaFin, limit, offset);

        ReporteAnunciosCompradosDB reporteAnunciosCompradosDb = new ReporteAnunciosCompradosDB();

        //Retorna todo el listado 
        if (reporteRequest.estaVacia()) {
            return reporteAnunciosCompradosDb.obtenerReporteTodoAnuncios(reporteRequest);
        }

        if (reporteRequest.validarRequest()) {

            return reporteAnunciosCompradosDb.obtenerReporteAnunciosSinFiltro(reporteRequest);
        }

        throw new ErrorInesperadoException("No se ha podido procesar la solicitud del reporte de anuncios comprados");
    }

    //Metodo delegado para poder validar y extraer la solicitud de request
    private ReporteAnuncioRequest extraerDatosReporte(String fechaInicio, String fechaFin, String limit, String offset) throws FormatoInvalidoException {

        if (StringUtils.isBlank(fechaInicio)) {
            throw new FormatoInvalidoException("La fecha de inicio esta vacia");
        }

        if (StringUtils.isBlank(fechaFin)) {
            throw new FormatoInvalidoException("La fecha final esta vacia");
        }

        if (StringUtils.isBlank(limit)) {
            throw new FormatoInvalidoException("El limite superior de la peticion vacio");
        }

        if (StringUtils.isBlank(offset)) {
            throw new FormatoInvalidoException("El limite inferior de la peticion vacio");
        }

        if (!StringUtils.isNumeric(limit)) {
            throw new FormatoInvalidoException("El limite superior de la peticion no es numerico");
        }

        if (!StringUtils.isNumeric(offset)) {
            throw new FormatoInvalidoException("El limite inferior de la peticion no es numerico");
        }

        if (fechaInicio.equals("null") && fechaFin.equals("null")) {
            return new ReporteAnuncioRequest(
                    Integer.parseInt(offset),
                    Integer.parseInt(limit));
        }

        try {
            // Parsear a formato ISO (yyyy-MM-dd)
            LocalDate inicio = LocalDate.parse(fechaInicio, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate fin = LocalDate.parse(fechaFin, DateTimeFormatter.ISO_LOCAL_DATE);

            return new ReporteAnuncioRequest(
                    inicio,
                    fin,
                    Integer.parseInt(offset),
                    Integer.parseInt(limit));

        } catch (DateTimeParseException e) {
            throw new FormatoInvalidoException("El formato de fecha solicitado debe ser ISO (yyyy-MM-dd)");
        } catch (NumberFormatException e) {
            throw new FormatoInvalidoException("Los limites de peticion no son enteros");
        }

    }

    //Metodo que sirve para retornar la cantidad de reportes sin filtro
    public CantidadReportesDTO cantidadReportesSinFiltro(String fechaInicio, String fechaFin) throws FormatoInvalidoException, ErrorInesperadoException, DatosNoEncontradosException {

        if (StringUtils.isBlank(fechaInicio)) {
            throw new FormatoInvalidoException("La fecha de inicio esta vacia");
        }

        if (StringUtils.isBlank(fechaFin)) {
            throw new FormatoInvalidoException("La fecha final esta vacia");
        }

        try {

            ReporteAnunciosCompradosDB reporteAnunciosCompradosDb = new ReporteAnunciosCompradosDB();

            if (fechaInicio.equals("null") && fechaFin.equals("null")) {

                ReporteAnuncioRequest reporteTodoRequest = new ReporteAnuncioRequest(0, 0);

                int cantidad = reporteAnunciosCompradosDb.cantidadReportesTodoSinFiltro(reporteTodoRequest);

                return new CantidadReportesDTO(cantidad);

            }

            LocalDate inicio = LocalDate.parse(fechaInicio, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate fin = LocalDate.parse(fechaFin, DateTimeFormatter.ISO_LOCAL_DATE);

            ReporteAnuncioRequest reporteRequest = new ReporteAnuncioRequest(inicio, fin, 0, 0);

            if (reporteRequest.validarRequest()) {

                int cantidad = reporteAnunciosCompradosDb.cantidadReportesSinFiltro(reporteRequest);

                return new CantidadReportesDTO(cantidad);

            }

        } catch (DateTimeParseException e) {
            throw new FormatoInvalidoException("El formato de fecha solicitado debe ser ISO (yyyy-MM-dd)");
        } catch (NumberFormatException e) {
            throw new FormatoInvalidoException("Los limites de peticion no son numericos");
        }

        throw new ErrorInesperadoException("No se pudo obtener la cantidad de anuncios comprados sin filtro.");
    }

    //Metodo utilizado para poder retornar la cantidad de reporte de boletos vendidos en un intervalo de tiempo CON FILTRO
    public List<ReporteAnuncioDTO> obtenerReporteAnunciosCompradosConFiltro(String fechaInicio, String fechaFin, String limit, String offset, String tipoAnuncio) throws FormatoInvalidoException, ErrorInesperadoException {

        ReporteAnuncioRequest reporteRequest = extraerDatosReporte(fechaInicio, fechaFin, limit, offset);

        if (StringUtils.isBlank(tipoAnuncio)) {
            throw new FormatoInvalidoException("El id de la sala se encuentra vacio");
        }

        reporteRequest.setTipoAnuncio(tipoAnuncio.trim());

        ReporteAnunciosCompradosDB reporteAnunciosCompradosDb = new ReporteAnunciosCompradosDB();

        //Retorna todo el listado 
        if (reporteRequest.estaVacia() && reporteRequest.validarVacio()) {
            return reporteAnunciosCompradosDb.obtenerReporteTodoAnunciosFiltro(reporteRequest);
        }

        if (reporteRequest.validarRequest()) {

            return reporteAnunciosCompradosDb.obtenerReporteAnunciosFiltro(reporteRequest);
        }

        throw new ErrorInesperadoException("No se ha podido procesar la solicitud del reporte de anuncios comprados");
    }

    //Metodo que sirve para retornar la cantidad de reportes de boletos CON FILTRO
    public CantidadReportesDTO cantidadReportesConFiltro(String fechaInicio, String fechaFin, String tipoAnuncio) throws FormatoInvalidoException, ErrorInesperadoException, DatosNoEncontradosException {

        if (StringUtils.isBlank(fechaInicio)) {
            throw new FormatoInvalidoException("La fecha de inicio esta vacia");
        }

        if (StringUtils.isBlank(fechaFin)) {
            throw new FormatoInvalidoException("La fecha final esta vacia");
        }

        try {

            ReporteAnunciosCompradosDB reporteAnunciosCompradosDb = new ReporteAnunciosCompradosDB();

            if (fechaInicio.equals("null") && fechaFin.equals("null")) {

                ReporteAnuncioRequest reporteTodoRequest = new ReporteAnuncioRequest(0, 0);
                reporteTodoRequest.setTipoAnuncio(tipoAnuncio);

                if (reporteTodoRequest.validarVacio()) {
                    int cantidad = reporteAnunciosCompradosDb.cantidadReportesTodoFiltro(reporteTodoRequest);

                    return new CantidadReportesDTO(cantidad);
                }

            }

            LocalDate inicio = LocalDate.parse(fechaInicio, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate fin = LocalDate.parse(fechaFin, DateTimeFormatter.ISO_LOCAL_DATE);

            ReporteAnuncioRequest reporteRequest = new ReporteAnuncioRequest(tipoAnuncio, inicio, fin, 0, 0);

            if (reporteRequest.validarRequestFiltro()) {

                int cantidad = reporteAnunciosCompradosDb.cantidadReportesFiltro(reporteRequest);

                return new CantidadReportesDTO(cantidad);

            }

        } catch (DateTimeParseException e) {
            throw new FormatoInvalidoException("El formato de fecha solicitado debe ser ISO (yyyy-MM-dd)");
        } catch (NumberFormatException e) {
            throw new FormatoInvalidoException("Los limites de peticion no son numericos");
        }

        throw new ErrorInesperadoException("No se pudo obtener la cantidad de reporte de anuncios comprados con filtro.");
    }

}
