/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.database;

import com.pablocompany.rest.api.proyectono2ipc2.connectiondb.DBConnectionSingleton;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.dtos.ReporteRequest;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models.ReporteSalasGustadasDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models.SalaCalificacionDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pablo
 */
//Clase delegada para operar con la base de datos para retornar el reporte de salas gustadas
public class ReporteSalasGustadasDB {

    //Constante que permite obtener el reporte de 5 salas mas gustadas sin filtro de sala
    private final String REPORTE_SALAS_GUSTADAS
            = "SELECT sa.codigo, ci.nombre AS `cineAsociado`, sa.nombre, sa.filas, sa.columnas, sa.ubicacion, "
            + "AVG(CAST(va.calificacion AS DECIMAL(10,2))) AS promedio_calificacion, "
            + "COUNT(va.calificacion) AS cantidad_votos, "
            + "AVG(CAST(va.calificacion AS DECIMAL(10,2))) * COUNT(va.calificacion) AS puntuacion_total "
            + "FROM sala AS sa "
            + "JOIN cine AS ci ON sa.codigo_cine = ci.codigo "
            + "JOIN valoracion_sala AS va ON sa.codigo = va.codigo_sala "
            + "WHERE va.fecha_posteo BETWEEN ? AND ? "
            + "GROUP BY sa.codigo, ci.nombre, sa.nombre, sa.filas, sa.columnas, sa.ubicacion "
            + "ORDER BY puntuacion_total DESC "
            + "LIMIT ? OFFSET ?;";

    // Constante que permite el onteo total de reportes (sin filtro)
    private final String CANTIDAD_REPORTES
            = "SELECT COUNT(*) AS cantidad FROM ( "
            + "SELECT sa.codigo "
            + "FROM sala AS sa "
            + "JOIN cine AS ci ON sa.codigo_cine = ci.codigo "
            + "JOIN valoracion_sala AS va ON sa.codigo = va.codigo_sala "
            + "WHERE va.fecha_posteo BETWEEN ? AND ? "
            + "GROUP BY sa.codigo "
            + "ORDER BY AVG(CAST(va.calificacion AS DECIMAL(10,2))) * COUNT(va.calificacion) DESC "
            + "LIMIT 5 "
            + ") AS conteo;";

    // Constante que permite el conteo total de reportes (con filtro por sala)
    private final String CANTIDAD_REPORTES_FILTRO
            = "SELECT COUNT(*) AS cantidad FROM ( "
            + "SELECT sa.codigo "
            + "FROM sala AS sa "
            + "JOIN cine AS ci ON sa.codigo_cine = ci.codigo "
            + "JOIN valoracion_sala AS va ON sa.codigo = va.codigo_sala "
            + "WHERE sa.codigo = ? AND va.fecha_posteo BETWEEN ? AND ? "
            + "GROUP BY sa.codigo "
            + "ORDER BY AVG(CAST(va.calificacion AS DECIMAL(10,2))) * COUNT(va.calificacion) DESC "
            + "LIMIT 5 "
            + ") AS conteo;";

    //Constante que permite obtener las calificaciones hechas en las distintas salas (por sala)
    private final String VALORACION_SALAS
            = "SELECT va.id_usuario, va.calificacion, va.fecha_posteo "
            + "FROM sala AS sa "
            + "JOIN cine AS ci ON sa.codigo_cine = ci.codigo "
            + "JOIN valoracion_sala AS va ON sa.codigo = va.codigo_sala "
            + "WHERE sa.codigo = ? AND va.fecha_posteo BETWEEN ? AND ? "
            + "ORDER BY va.fecha_posteo DESC;";

    // Constante que permite obtener el reporte de 5 salas más gustadas CON FILTRO
    private final String REPORTE_SALAS_GUSTADAS_FILTRO
            = "SELECT sa.codigo, ci.nombre AS `cineAsociado`, sa.nombre, sa.filas, sa.columnas, sa.ubicacion, "
            + "AVG(CAST(va.calificacion AS DECIMAL(10,2))) AS promedio_calificacion, "
            + "COUNT(va.calificacion) AS cantidad_votos, "
            + "AVG(CAST(va.calificacion AS DECIMAL(10,2))) * COUNT(va.calificacion) AS puntuacion_total "
            + "FROM sala AS sa "
            + "JOIN cine AS ci ON sa.codigo_cine = ci.codigo "
            + "JOIN valoracion_sala AS va ON sa.codigo = va.codigo_sala "
            + "WHERE sa.codigo = ? AND va.fecha_posteo BETWEEN ? AND ? "
            + "GROUP BY sa.codigo, ci.nombre, sa.nombre, sa.filas, sa.columnas, sa.ubicacion "
            + "ORDER BY puntuacion_total DESC "
            + "LIMIT ? OFFSET ?;";

    //Metodo delegado para obtener la cantidad de reportes de 5 salas mas gustadas que se tienen en el intervalo de fechas
    public int cantidadReportesSinFiltro(ReporteRequest reporteSalas) throws ErrorInesperadoException, DatosNoEncontradosException {

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CANTIDAD_REPORTES);) {

            query.setDate(1, java.sql.Date.valueOf(reporteSalas.getFechaInicio()));
            query.setDate(2, java.sql.Date.valueOf(reporteSalas.getFechaFin()));

            ResultSet result = query.executeQuery();
            if (result.next()) {
                return result.getInt("cantidad");
            } else {

                throw new DatosNoEncontradosException("No hay registros de reportes de 5 salas mas gustadas");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se ha podido conectar con la base de datos para obtener la cantidad de reportes de 5 salas mas guastadas sin filtro");
        }
    }

    //Metodo delegado para obtener la cantidad de reportes de 5 salas mas gustadas que se tienen en el intervalo de fechas con filtro
    public int cantidadReportesFiltro(ReporteRequest reporteSalas) throws ErrorInesperadoException, DatosNoEncontradosException {

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CANTIDAD_REPORTES_FILTRO);) {

            query.setString(1, reporteSalas.getIdSala());
            query.setDate(2, java.sql.Date.valueOf(reporteSalas.getFechaInicio()));
            query.setDate(3, java.sql.Date.valueOf(reporteSalas.getFechaFin()));

            ResultSet result = query.executeQuery();
            if (result.next()) {
                return result.getInt("cantidad");
            } else {

                throw new DatosNoEncontradosException("No hay registros de reportes de 5 salas mas gustadas");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se ha podido conectar con la base de datos para obtener la cantidad de reportes de 5 salas mas guastadas con filtro");
        }
    }

    //Metodo que sirve para poder consultar el reporte de 5 salas mas gustadas SIN FLITRO
    public List<ReporteSalasGustadasDTO> obtenerReporteSalasGustadas(ReporteRequest reporteSalas) throws ErrorInesperadoException, FormatoInvalidoException {

        if (reporteSalas == null) {
            throw new FormatoInvalidoException("La referencia de request de 5 salas mas gustadas esta vacia");
        }

        List<ReporteSalasGustadasDTO> listadoReportes = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(REPORTE_SALAS_GUSTADAS);) {

            query.setDate(1, java.sql.Date.valueOf(reporteSalas.getFechaInicio()));
            query.setDate(2, java.sql.Date.valueOf(reporteSalas.getFechaFin()));
            query.setInt(3, reporteSalas.getLimit());
            query.setInt(4, reporteSalas.getOffset());

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                ReporteSalasGustadasDTO salaEncontrada = new ReporteSalasGustadasDTO(
                        resultSet.getString("codigo"),
                        resultSet.getString("cineAsociado"),
                        resultSet.getString("nombre"),
                        resultSet.getInt("filas"),
                        resultSet.getInt("columnas"),
                        resultSet.getString("ubicacion")
                );

                listadoReportes.add(salaEncontrada);
            }

            for (ReporteSalasGustadasDTO reporte : listadoReportes) {
                reporte.setComentarios(obtenerCalificaciones(reporteSalas, reporte.getCodigo()));
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de reporte de 5 salas mas gustadas");
        }

        return listadoReportes;
    }

    //Metodo que sirve para poder consultar el reporte de 5 salas mas gustadas CON FLITRO
    public List<ReporteSalasGustadasDTO> obtenerReporteSalasGustadasFiltro(ReporteRequest reporteSalas) throws ErrorInesperadoException, FormatoInvalidoException {

        if (reporteSalas == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        List<ReporteSalasGustadasDTO> listadoReportes = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(REPORTE_SALAS_GUSTADAS_FILTRO);) {

            query.setString(1, reporteSalas.getIdSala());
            query.setDate(2, java.sql.Date.valueOf(reporteSalas.getFechaInicio()));
            query.setDate(3, java.sql.Date.valueOf(reporteSalas.getFechaFin()));
            query.setInt(4, reporteSalas.getLimit());
            query.setInt(5, reporteSalas.getOffset());

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                ReporteSalasGustadasDTO reporteEncontrado = new ReporteSalasGustadasDTO(
                        resultSet.getString("codigo"),
                        resultSet.getString("cineAsociado"),
                        resultSet.getString("nombre"),
                        resultSet.getInt("filas"),
                        resultSet.getInt("columnas"),
                        resultSet.getString("ubicacion")
                );

                listadoReportes.add(reporteEncontrado);
            }

            for (ReporteSalasGustadasDTO reporte : listadoReportes) {
                reporte.setComentarios(obtenerCalificaciones(reporteSalas, reporte.getCodigo()));
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de reporte de las 5 salas mas gustadas");
        }

        return listadoReportes;
    }

    //Metodo que sirve para obtener las valificaciones que hacen los usuarios en las salas
    private List< SalaCalificacionDTO> obtenerCalificaciones(ReporteRequest reporteSalas, String idSala) throws FormatoInvalidoException, ErrorInesperadoException {

        if (reporteSalas == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        List<SalaCalificacionDTO> listadoValoracion = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(VALORACION_SALAS);) {

            query.setString(1, idSala);
            query.setDate(2, java.sql.Date.valueOf(reporteSalas.getFechaInicio()));
            query.setDate(3, java.sql.Date.valueOf(reporteSalas.getFechaFin()));

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                SalaCalificacionDTO valoracionEncontrada = new SalaCalificacionDTO(
                        resultSet.getString("id_usuario"),
                        resultSet.getString("calificacion"),
                        resultSet.getDate("fecha_posteo").toLocalDate()
                );

                listadoValoracion.add(valoracionEncontrada);
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de la valoraciones del reporte de 5 salas mas gustadas");
        }

        return listadoValoracion;

    }

}
