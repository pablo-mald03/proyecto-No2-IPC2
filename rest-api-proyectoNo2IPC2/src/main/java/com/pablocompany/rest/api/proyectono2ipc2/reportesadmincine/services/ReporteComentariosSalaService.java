/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.services;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.database.ReporteSalaCineDB;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.dtos.ReporteRequest;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models.CantidadReportesDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models.ReporteSalasComentadasDTO;
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
public class ReporteComentariosSalaService {

    //Metodo que sirve para poder retornar el request del reporte
    public List<ReporteSalasComentadasDTO> obtenerReporteSalaSinFiltro(String fechaInicio, String fechaFin, String limit, String offset) throws FormatoInvalidoException, ErrorInesperadoException {

        ReporteRequest reporteSalasCineComentarios = extraerDatosReporte(fechaInicio, fechaFin, limit, offset);

        if (reporteSalasCineComentarios.validarRequest()) {

            ReporteSalaCineDB reporteSalaCineDb = new ReporteSalaCineDB();
            return reporteSalaCineDb.obtenerReporteComentarios(reporteSalasCineComentarios);
        }

        throw new ErrorInesperadoException("No se ha podido procesar la solicitud del reporte");
    }

    //Metodo delegado para poder validar y extraer la solicitud de request
    private ReporteRequest extraerDatosReporte(String fechaInicio, String fechaFin, String limit, String offset) throws FormatoInvalidoException {

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

        try {
            // Parsear a formato ISO (yyyy-MM-dd)
            LocalDate inicio = LocalDate.parse(fechaInicio, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate fin = LocalDate.parse(fechaFin, DateTimeFormatter.ISO_LOCAL_DATE);

            return new ReporteRequest(
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
            LocalDate inicio = LocalDate.parse(fechaInicio, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate fin = LocalDate.parse(fechaFin, DateTimeFormatter.ISO_LOCAL_DATE);

            if (inicio.isAfter(fin)) {
                throw new FormatoInvalidoException("La fecha de inicio no puede ser posterior a la fecha de fin.");
            }

            ReporteRequest reporteSalasCineComentariosRequest = new ReporteRequest(inicio, fin, 0, 0);

            ReporteSalaCineDB reporteSalaCineDb = new ReporteSalaCineDB();

            int cantidad = reporteSalaCineDb.cantidadReportesSinFiltro(reporteSalasCineComentariosRequest);
            return new CantidadReportesDTO(cantidad);

        } catch (DateTimeParseException e) {
            throw new FormatoInvalidoException("El formato de fecha solicitado debe ser ISO (yyyy-MM-dd)");
        } catch (NumberFormatException e) {
            throw new FormatoInvalidoException("Los limites de peticion no son numericos");
        }

    }

    //Metodo que obtiene la cantidad de reportes de salas de cine sin filtro
    public CantidadReportesDTO cantidadReportesConFiltro(String fechaInicio, String fechaFin, String idSala) throws FormatoInvalidoException, ErrorInesperadoException, DatosNoEncontradosException {

        if (StringUtils.isBlank(fechaInicio)) {
            throw new FormatoInvalidoException("La fecha de inicio esta vacia");
        }

        if (StringUtils.isBlank(fechaFin)) {
            throw new FormatoInvalidoException("La fecha final esta vacia");
        }

        if (StringUtils.isBlank(idSala)) {
            throw new FormatoInvalidoException("El id de la sala esta vacio");
        }

        try {
            LocalDate inicio = LocalDate.parse(fechaInicio, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate fin = LocalDate.parse(fechaFin, DateTimeFormatter.ISO_LOCAL_DATE);

            if (inicio.isAfter(fin)) {
                throw new FormatoInvalidoException("La fecha de inicio no puede ser posterior a la fecha de fin.");
            }

            ReporteRequest reporteSalasCineComentariosRequest = new ReporteRequest(idSala.trim(), inicio, fin, 0, 0);

            ReporteSalaCineDB reporteSalaCineDb = new ReporteSalaCineDB();

            int cantidad = reporteSalaCineDb.cantidadReportesFiltro(reporteSalasCineComentariosRequest);
            return new CantidadReportesDTO(cantidad);

        } catch (DateTimeParseException e) {
            throw new FormatoInvalidoException("El formato de fecha solicitado debe ser ISO (yyyy-MM-dd)");
        } catch (NumberFormatException e) {
            throw new FormatoInvalidoException("Los limites de peticion no son numericos");
        }

    }

    //Metodo que sirve para poder retornar el request del reporte
    public List<ReporteSalasComentadasDTO> obtenerReporteSalaConFiltro(String idSala, String fechaInicio, String fechaFin, String limit, String offset) throws FormatoInvalidoException, ErrorInesperadoException {

        if (!StringUtils.isNumeric(limit)) {
            throw new FormatoInvalidoException("El limite superior de la peticion no es numerico");
        }

        if (!StringUtils.isNumeric(offset)) {
            throw new FormatoInvalidoException("El limite inferior de la peticion no es numerico");
        }

        ReporteRequest reporteSalasCineComentarios = extraerDatosReporteFiltro(idSala, fechaInicio, fechaFin, limit, offset);

        if (reporteSalasCineComentarios.validarRequest()) {

            ReporteSalaCineDB reporteSalaCineDb = new ReporteSalaCineDB();
            return reporteSalaCineDb.obtenerReporteComentariosFiltro(reporteSalasCineComentarios);
        }

        throw new ErrorInesperadoException("No se ha podido procesar la solicitud del reporte");
    }

    //Metodo delegado para poder validar y extraer la solicitud de request
    private ReporteRequest extraerDatosReporteFiltro(String idSala, String fechaInicio, String fechaFin, String limit, String offset) throws FormatoInvalidoException {

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

        try {
            LocalDate inicio = LocalDate.parse(fechaInicio, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate fin = LocalDate.parse(fechaFin, DateTimeFormatter.ISO_LOCAL_DATE);

            return new ReporteRequest(
                    idSala,
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

}
