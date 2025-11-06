/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.services;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models.CantidadReportesDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.database.ReporteSalasMasComentadasDB;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.dtos.ReporteSistemaRequest;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models.SalaMasComenadaDTO;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author pablo
 */
//Clase delegada para poder interactuar con todo lo que se necesita de los reportes de salas mas comentadas 
public class ReporteSalasMaComentadasService {

    //Metodo que sirve para poder retornar el request del reporte de las salas mas comentadas en todo o en un intervalo de tiempo 
    public List<SalaMasComenadaDTO> obtenerReporteSala(String fechaInicio, String fechaFin, String limit, String offset) throws FormatoInvalidoException, ErrorInesperadoException {

        ReporteSistemaRequest reporteSalasCineComentarios = extraerDatosReporte(fechaInicio, fechaFin, limit, offset);

        ReporteSalasMasComentadasDB reporteSalaCineDb = new ReporteSalasMasComentadasDB();

        //Retorna todo el listado 
        if (reporteSalasCineComentarios.estaVacia()) {
            return reporteSalaCineDb.obtenerReporteTodoSalas(reporteSalasCineComentarios);
        }

        if (reporteSalasCineComentarios.validarRequest()) {

            return reporteSalaCineDb.obtenerReporteSalas(reporteSalasCineComentarios);
        }

        throw new ErrorInesperadoException("No se ha podido procesar la solicitud del reporte");
    }

    //Metodo delegado para poder validar y extraer la solicitud de request
    private ReporteSistemaRequest extraerDatosReporte(String fechaInicio, String fechaFin, String limit, String offset) throws FormatoInvalidoException {

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
            return new ReporteSistemaRequest(
                    Integer.parseInt(offset),
                    Integer.parseInt(limit));
        }

        try {
            LocalDate inicio = LocalDate.parse(fechaInicio, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate fin = LocalDate.parse(fechaFin, DateTimeFormatter.ISO_LOCAL_DATE);

            return new ReporteSistemaRequest(
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

    //Metodo que obtiene la cantidad de reportes de salas mas comentadas en todo o en un intervalo de tiempo 
    public CantidadReportesDTO cantidadReportes(String fechaInicio, String fechaFin) throws FormatoInvalidoException, ErrorInesperadoException, DatosNoEncontradosException {

        if (StringUtils.isBlank(fechaInicio)) {
            throw new FormatoInvalidoException("La fecha de inicio esta vacia");
        }

        if (StringUtils.isBlank(fechaFin)) {
            throw new FormatoInvalidoException("La fecha final esta vacia");
        }

        try {

            ReporteSalasMasComentadasDB reporteSalaCineDb = new ReporteSalasMasComentadasDB();

            if (fechaInicio.equals("null") && fechaFin.equals("null")) {

                ReporteSistemaRequest reporteTodoRequest = new ReporteSistemaRequest(0, 0);

                int cantidad = reporteSalaCineDb.cantidadReportesTodo(reporteTodoRequest);

                return new CantidadReportesDTO(cantidad);

            }

            LocalDate inicio = LocalDate.parse(fechaInicio, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate fin = LocalDate.parse(fechaFin, DateTimeFormatter.ISO_LOCAL_DATE);

            ReporteSistemaRequest reporteSalasCineComentariosRequest = new ReporteSistemaRequest(inicio, fin, 0, 0);

            int cantidad = reporteSalaCineDb.cantidadReportes(reporteSalasCineComentariosRequest);
            return new CantidadReportesDTO(cantidad);

        } catch (DateTimeParseException e) {
            throw new FormatoInvalidoException("El formato de fecha solicitado debe ser ISO (yyyy-MM-dd)");
        } catch (NumberFormatException e) {
            throw new FormatoInvalidoException("Los limites de peticion no son numericos");
        }

    }

}
