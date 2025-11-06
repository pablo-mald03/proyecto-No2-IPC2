/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.services;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models.CantidadReportesDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.database.ReporteAnuncianteDB;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.dtos.ReporteAnuncianteRequest;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models.ReporteAnuncianteDTO;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author pablo
 */
//Clase delegada para poder operar con el reporte de comentarios en salas de cine
public class ReporteGananciasAnuncianteService {

    //Metodo que sirve para poder retornar el request del reporte
    public List<ReporteAnuncianteDTO> obtenerReporteSinFiltro(String fechaInicio, String fechaFin, String limit, String offset) throws FormatoInvalidoException, ErrorInesperadoException {

        ReporteAnuncianteRequest reporteRequest = extraerDatosReporte(fechaInicio, fechaFin, limit, offset);

        ReporteAnuncianteDB reporteAnunciante = new ReporteAnuncianteDB();

        //Retorna todo el listado 
        if (reporteRequest.estaVacia()) {
            return reporteAnunciante.obtenerReporteTodoAnunciante(reporteRequest);
        }

        if (reporteRequest.validarRequest()) {

            return reporteAnunciante.obtenerReporteAnuncianteSinFiltro(reporteRequest);
        }

        throw new ErrorInesperadoException("No se ha podido procesar la solicitud del reporte");
    }

    //Metodo delegado para poder validar y extraer la solicitud de request
    private ReporteAnuncianteRequest extraerDatosReporte(String fechaInicio, String fechaFin, String limit, String offset) throws FormatoInvalidoException {

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
            return new ReporteAnuncianteRequest(
                    Integer.parseInt(offset),
                    Integer.parseInt(limit));
        }

        try {
            // Parsear a formato ISO (yyyy-MM-dd)
            LocalDate inicio = LocalDate.parse(fechaInicio, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate fin = LocalDate.parse(fechaFin, DateTimeFormatter.ISO_LOCAL_DATE);

            return new ReporteAnuncianteRequest(
                    inicio,
                    fin,
                    Integer.parseInt(offset),
                    Integer.parseInt(limit));

        } catch (DateTimeParseException e) {
            throw new FormatoInvalidoException("El formato de fecha solicitado debe ser ISO (yyyy-MM-dd)");
        } catch (NumberFormatException e) {
            throw new FormatoInvalidoException("Los limites de peticion no son numericos");
        }

    }

    //Metodo que obtiene la cantidad de reportes de salas de cine sin filtro
    public CantidadReportesDTO cantidadReportesSinFiltro(String fechaInicio, String fechaFin) throws FormatoInvalidoException, ErrorInesperadoException, DatosNoEncontradosException {

        if (StringUtils.isBlank(fechaInicio)) {
            throw new FormatoInvalidoException("La fecha de inicio esta vacia");
        }

        if (StringUtils.isBlank(fechaFin)) {
            throw new FormatoInvalidoException("La fecha final esta vacia");
        }

        try {

            ReporteAnuncianteDB reporteAnunciante = new ReporteAnuncianteDB();

            if (fechaInicio.equals("null") && fechaFin.equals("null")) {

                ReporteAnuncianteRequest reporteTodoRequest = new ReporteAnuncianteRequest(0, 0);

                int cantidad = reporteAnunciante.cantidadReportesTodoSinFiltro(reporteTodoRequest);

                return new CantidadReportesDTO(cantidad);

            }

            LocalDate inicio = LocalDate.parse(fechaInicio, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate fin = LocalDate.parse(fechaFin, DateTimeFormatter.ISO_LOCAL_DATE);

            ReporteAnuncianteRequest reporteRequestAnuncinate = new ReporteAnuncianteRequest(inicio, fin, 0, 0);

            int cantidad = reporteAnunciante.cantidadReportesSinFiltro(reporteRequestAnuncinate);
            return new CantidadReportesDTO(cantidad);

        } catch (DateTimeParseException e) {
            throw new FormatoInvalidoException("El formato de fecha solicitado debe ser ISO (yyyy-MM-dd)");
        } catch (NumberFormatException e) {
            throw new FormatoInvalidoException("Los limites de peticion no son numericos");
        }

    }

    //Metodo que obtiene la cantidad de reportes de salas de cine sin filtro
    public CantidadReportesDTO cantidadReportesConFiltro(String fechaInicio, String fechaFin, String idUsuario) throws FormatoInvalidoException, ErrorInesperadoException, DatosNoEncontradosException {

        if (StringUtils.isBlank(fechaInicio)) {
            throw new FormatoInvalidoException("La fecha de inicio esta vacia");
        }

        if (StringUtils.isBlank(fechaFin)) {
            throw new FormatoInvalidoException("La fecha final esta vacia");
        }

        if (StringUtils.isBlank(idUsuario)) {
            throw new FormatoInvalidoException("El id del usuario esta vacio");
        }

        try {

            ReporteAnuncianteDB reporteAnunciante = new ReporteAnuncianteDB();

            if (fechaInicio.equals("null") && fechaFin.equals("null")) {

                ReporteAnuncianteRequest reporteTodoRequest = new ReporteAnuncianteRequest(0, 0);
                reporteTodoRequest.setIdUsuario(idUsuario);

                if (reporteTodoRequest.validarVacio()) {
                    int cantidad = reporteAnunciante.cantidadReportesTodoFiltro(reporteTodoRequest);

                    return new CantidadReportesDTO(cantidad);
                }

            }

            LocalDate inicio = LocalDate.parse(fechaInicio, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate fin = LocalDate.parse(fechaFin, DateTimeFormatter.ISO_LOCAL_DATE);

            ReporteAnuncianteRequest reporteAnuncianteRequest = new ReporteAnuncianteRequest(idUsuario.trim(), inicio, fin, 0, 0);

            int cantidad = reporteAnunciante.cantidadReportesFiltro(reporteAnuncianteRequest);
            return new CantidadReportesDTO(cantidad);

        } catch (DateTimeParseException e) {
            throw new FormatoInvalidoException("El formato de fecha solicitado debe ser ISO (yyyy-MM-dd)");
        } catch (NumberFormatException e) {
            throw new FormatoInvalidoException("Los limites de peticion no son numericos");
        }

    }

    //Metodo que sirve para poder retornar el request del reporte
    public List<ReporteAnuncianteDTO> obtenerReporteConFiltro(String idUsuario, String fechaInicio, String fechaFin, String limit, String offset) throws FormatoInvalidoException, ErrorInesperadoException {

        ReporteAnuncianteRequest reporteRequest = extraerDatosReporte(fechaInicio, fechaFin, limit, offset);

        if (StringUtils.isBlank(idUsuario)) {
            throw new FormatoInvalidoException("El id del usuario se encuentra vacio");
        }

        reporteRequest.setIdUsuario(idUsuario.trim());

        ReporteAnuncianteDB reporteAnunciante = new ReporteAnuncianteDB();

        if (reporteRequest.estaVacia() && reporteRequest.validarVacio()) {
            return reporteAnunciante.obtenerReporteTodoAnunciante(reporteRequest);
        }

        if (reporteRequest.validarRequestFiltro()) {

            return reporteAnunciante.obtenerReporteTodoAnunciantesFiltro(reporteRequest);
        }

        throw new ErrorInesperadoException("No se ha podido procesar la solicitud del reporte");
    }

}
