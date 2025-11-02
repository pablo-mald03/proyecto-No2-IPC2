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
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models.ReporteSalasComentadasDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models.SalaComentarioDTO;
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
    private final String REPORTE_COMENTARIOS = "SELECT sa.codigo, ci.nombre AS `cineAsociado`, sa.nombre, sa.filas, sa.columnas, sa.ubicacion FROM sala AS `sa` JOIN cine AS `ci` ON sa.codigo_cine = ci.codigo JOIN comentario_sala AS `co` ON sa.codigo = co.codigo_sala WHERE co.fecha_posteo BETWEEN ? AND ? ORDER BY co.fecha_posteo DESC LIMIT ? OFFSET ?";

    //Constante que permite obtener el conteo de cantidad de reportes que hay en base a una fecha
    private final String CANTIDAD_REPORTES = "SELECT COUNT(*) AS `cantidad` FROM sala AS `sa` JOIN comentario_sala AS `co` ON sa.codigo = co.codigo_sala WHERE co.fecha_posteo BETWEEN ? AND ? ";

    //Constante que permite obtener el conteo de cantidad de reportes que hay en base a una fecha con filtro por sala de cine
    private final String CANTIDAD_REPORTES_FILTRO = "SELECT COUNT(*) AS `cantidad` FROM sala AS `sa` JOIN comentario_sala AS `co` ON sa.codigo = co.codigo_sala WHERE sa.codigo = ? AND co.fecha_posteo BETWEEN ? AND ? ";

    //Constante que permite obtener el reporte de calificaciones en base al filtro de sala
    private final String COMENTARIOS_SALAS = "SELECT co.id_usuario, co.contenido, co.fecha_posteo FROM sala AS `sa` JOIN comentario_sala AS `co` ON sa.codigo = co.codigo_sala WHERE sa.codigo = ? AND co.fecha_posteo BETWEEN ? AND ? ORDER BY co.fecha_posteo DESC";

    //Constante que permite obtener el reporte de 5 salas mas gustadas con filtro de sala de cine
    private final String REPORTE_COMENTARIOS_FILTRO = "SELECT sa.codigo, ci.nombre AS `cineAsociado`, sa.nombre, sa.filas, sa.columnas, sa.ubicacion FROM sala AS `sa` JOIN cine AS `ci` ON sa.codigo_cine = ci.codigo JOIN comentario_sala AS `co` ON sa.codigo = co.codigo_sala WHERE sa.codigo = ? AND co.fecha_posteo BETWEEN ? AND ? ORDER BY co.fecha_posteo DESC LIMIT ? OFFSET ?";

    //Metodo delegado para obtener la cantidad de reportes que se tienen en el intervalo de fechas
    public int cantidadReportesSinFiltro(ReporteRequest reporteSalas) throws ErrorInesperadoException, DatosNoEncontradosException {

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CANTIDAD_REPORTES);) {

            query.setDate(1, java.sql.Date.valueOf(reporteSalas.getFechaInicio()));
            query.setDate(2, java.sql.Date.valueOf(reporteSalas.getFechaFin()));

            ResultSet result = query.executeQuery();
            if (result.next()) {
                return result.getInt("cantidad");
            } else {

                throw new DatosNoEncontradosException("No hay registros de reportes de comentarios de salas de cine");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se ha podido conectar con la base de datos para obtener la cantidad de reportes de salas de cine");
        }
    }

    //Metodo delegado para obtener la cantidad de reportes que se tienen en el intervalo de fechas con filtro
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

                throw new DatosNoEncontradosException("No hay registros de reportes de comentarios de salas de cine");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se ha podido conectar con la base de datos para obtener la cantidad de reportes de salas de cine");
        }
    }

    //Metodo que sirve para poder consultar el reporte de comentarios SIN FLITRO
    public List<ReporteSalasComentadasDTO> obtenerReporteSalasGustadas(ReporteRequest reporteSalas) throws ErrorInesperadoException, FormatoInvalidoException {

        if (reporteSalas == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        List<ReporteSalasComentadasDTO> listadoReportes = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(REPORTE_COMENTARIOS);) {

            query.setDate(1, java.sql.Date.valueOf(reporteSalas.getFechaInicio()));
            query.setDate(2, java.sql.Date.valueOf(reporteSalas.getFechaFin()));
            query.setInt(3, reporteSalas.getLimit());
            query.setInt(4, reporteSalas.getOffset());

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                ReporteSalasComentadasDTO usuarioEncontrado = new ReporteSalasComentadasDTO(
                        resultSet.getString("codigo"),
                        resultSet.getString("cineAsociado"),
                        resultSet.getString("nombre"),
                        resultSet.getInt("filas"),
                        resultSet.getInt("columnas"),
                        resultSet.getString("ubicacion")
                );

                listadoReportes.add(usuarioEncontrado);
            }

            for (ReporteSalasComentadasDTO listadoReporte : listadoReportes) {
                listadoReporte.setComentarios(obtenerCalificaciones(reporteSalas, listadoReporte.getCodigo()));
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de reporte de comentarios en salas de cine");
        }

        return listadoReportes;
    }

    //Metodo que sirve para poder consultar el reporte de comentarios SIN FLITRO
    public List<ReporteSalasComentadasDTO> obtenerReporteSalasGustadasFiltro(ReporteRequest reporteSalas) throws ErrorInesperadoException, FormatoInvalidoException {

        if (reporteSalas == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        List<ReporteSalasComentadasDTO> listadoReportes = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(REPORTE_COMENTARIOS_FILTRO);) {

            query.setString(1, reporteSalas.getIdSala());
            query.setDate(2, java.sql.Date.valueOf(reporteSalas.getFechaInicio()));
            query.setDate(3, java.sql.Date.valueOf(reporteSalas.getFechaFin()));
            query.setInt(4, reporteSalas.getLimit());
            query.setInt(5, reporteSalas.getOffset());

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                ReporteSalasComentadasDTO reporteEncontrado = new ReporteSalasComentadasDTO(
                        resultSet.getString("codigo"),
                        resultSet.getString("cineAsociado"),
                        resultSet.getString("nombre"),
                        resultSet.getInt("filas"),
                        resultSet.getInt("columnas"),
                        resultSet.getString("ubicacion")
                );

                listadoReportes.add(reporteEncontrado);
            }

            for (ReporteSalasComentadasDTO listadoReporte : listadoReportes) {
                listadoReporte.setComentarios(obtenerCalificaciones(reporteSalas, listadoReporte.getCodigo()));
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de reporte de comentarios en salas de cine");
        }

        return listadoReportes;
    }

    //Metodo que sirve para obtener los comentarios relacionados a una sala
    private List< SalaComentarioDTO> obtenerCalificaciones(ReporteRequest reporteSalas, String idSala) throws FormatoInvalidoException, ErrorInesperadoException {

        if (reporteSalas == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        List<SalaComentarioDTO> listadoComentarios = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(COMENTARIOS_SALAS);) {

            query.setString(1, idSala);
            query.setDate(2, java.sql.Date.valueOf(reporteSalas.getFechaInicio()));
            query.setDate(3, java.sql.Date.valueOf(reporteSalas.getFechaFin()));

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                SalaComentarioDTO comentarioEncontrado = new SalaComentarioDTO(
                        resultSet.getString("id_usuario"),
                        resultSet.getString("contenido"),
                        resultSet.getDate("fecha_posteo").toLocalDate()
                );

                listadoComentarios.add(comentarioEncontrado);
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de los comentarios del reporte de salas de cine");
        }

        return listadoComentarios;

    }
    
}
