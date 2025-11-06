/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.database;

import com.pablocompany.rest.api.proyectono2ipc2.connectiondb.DBConnectionSingleton;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models.SalaComentarioDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.dtos.ReporteSistemaRequest;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models.SalaMasComenadaDTO;
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
//Clase delegada para poder operar con la base de datos y buscar las 5 salas mas comentadas A nivel global
public class ReporteSalasMasComentadasDB {

    //Constante que permite obtener el reporte de las 5 salas mas comentadas
    private final String SALAS_COMENTADAS
            = "SELECT sa.codigo, ci.nombre AS `cineAsociado`, sa.nombre, sa.filas, sa.columnas, sa.ubicacion, "
            + "COUNT(co.id) AS `totalComentarios`, "
            + "MAX(co.fecha_posteo) AS `ultimoComentario` "
            + "FROM sala AS `sa` "
            + "JOIN cine AS `ci` ON sa.codigo_cine = ci.codigo "
            + "JOIN comentario_sala AS `co` ON sa.codigo = co.codigo_sala "
            + "WHERE co.fecha_posteo BETWEEN ? AND ? "
            + "GROUP BY sa.codigo, ci.nombre, sa.nombre, sa.filas, sa.columnas, sa.ubicacion "
            + "ORDER BY totalComentarios DESC "
            + "LIMIT ? OFFSET ?";

    //Constante que permite obtener el conteo de cantidad de reportes que hay en base a una fecha
    private final String CANTIDAD_REPORTES = "SELECT COUNT(*) AS `cantidad` "
            + "FROM ( "
            + "  SELECT sa.codigo "
            + "  FROM sala AS `sa` "
            + "  JOIN comentario_sala AS `co` ON sa.codigo = co.codigo_sala "
            + "  WHERE co.fecha_posteo BETWEEN ? AND ? "
            + "  GROUP BY sa.codigo "
            + "  LIMIT 5 "
            + ") AS subconsulta";

    //Constante que permite obtener el preorte de comentarios en base al filtro de sala
    private final String COMENTARIOS_SALAS
            = "SELECT co.id_usuario, u.nombre AS `usuario`, co.contenido, co.fecha_posteo "
            + "FROM comentario_sala AS `co` "
            + "JOIN usuario AS `u` ON co.id_usuario = u.id "
            + "WHERE co.codigo_sala = ? AND co.fecha_posteo BETWEEN ? AND ? "
            + "ORDER BY co.fecha_posteo DESC";

    //Constante que permite obtener el TODOS LOS REGISTROS DEL reporte de las 5 salas mas comentadas
    private final String SALAS_TODO_COMENTADAS
            = "SELECT sa.codigo, ci.nombre AS `cineAsociado`, sa.nombre, sa.filas, sa.columnas, sa.ubicacion, "
            + "COUNT(co.id) AS `totalComentarios`, "
            + "MAX(co.fecha_posteo) AS `ultimoComentario` "
            + "FROM sala AS `sa` "
            + "JOIN cine AS `ci` ON sa.codigo_cine = ci.codigo "
            + "JOIN comentario_sala AS `co` ON sa.codigo = co.codigo_sala "
            + "GROUP BY sa.codigo, ci.nombre, sa.nombre, sa.filas, sa.columnas, sa.ubicacion "
            + "ORDER BY totalComentarios DESC "
            + "LIMIT ? OFFSET ?";

    //Constante que permite obtener el conteo de cantidad de  TODOS LOS REGISTROS DEL reporte que hay en base a una fecha
    private final String CANTIDAD_TODO_REPORTES = "SELECT COUNT(*) AS `cantidad` "
            + "FROM ( "
            + "  SELECT sa.codigo "
            + "  FROM sala AS `sa` "
            + "  JOIN comentario_sala AS `co` ON sa.codigo = co.codigo_sala "
            + "  GROUP BY sa.codigo "
            + "  LIMIT 5 "
            + ") AS subconsulta";

    //Constante que permite obtener el reporte de comentarios en base TODOS LOS REGISTROS 
    private final String COMENTARIOS_TODO_SALAS
            = "SELECT co.id_usuario, u.nombre AS `usuario`, co.contenido, co.fecha_posteo "
            + "FROM comentario_sala AS `co` "
            + "JOIN usuario AS `u` ON co.id_usuario = u.id "
            + "WHERE co.codigo_sala = ? "
            + "ORDER BY co.fecha_posteo DESC";

    //Metodo delegado para obtener la cantidad de reportes que se tienen en el intervalo de fechas
    public int cantidadReportes(ReporteSistemaRequest reporteSalas) throws ErrorInesperadoException, DatosNoEncontradosException {

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
            throw new ErrorInesperadoException("No se ha podido conectar con la base de datos para obtener la cantidad de reportes de comentarios en salas de cine");
        }
    }

    //Metodo delegado para obtener la cantidad de reportes que se tienen en todo intervalo de fechas
    public int cantidadTodoReportes(ReporteSistemaRequest reporteSalas) throws ErrorInesperadoException, DatosNoEncontradosException {

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CANTIDAD_TODO_REPORTES);) {
            ResultSet result = query.executeQuery();
            if (result.next()) {
                return result.getInt("cantidad");
            } else {

                throw new DatosNoEncontradosException("No hay registros de reportes de las 5 salas mas comentadas ");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se ha podido conectar con la base de datos para obtener la cantidad de reportes de las 5 salas mas comentadas");
        }
    }

    //Metodo que sirve para poder consultar el reporte de comentarios SIN FLITRO
    public List<SalaMasComenadaDTO> obtenerReporteSalas(ReporteSistemaRequest reporteSalas) throws ErrorInesperadoException, FormatoInvalidoException {

        if (reporteSalas == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        List<SalaMasComenadaDTO> listadoReportes = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(SALAS_COMENTADAS);) {

            query.setDate(1, java.sql.Date.valueOf(reporteSalas.getFechaInicio()));
            query.setDate(2, java.sql.Date.valueOf(reporteSalas.getFechaFin()));
            query.setInt(3, reporteSalas.getLimit());
            query.setInt(4, reporteSalas.getOffset());

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                SalaMasComenadaDTO salaComentada = new SalaMasComenadaDTO(
                        resultSet.getString("codigo"),
                        resultSet.getString("cineAsociado"),
                        resultSet.getString("nombre"),
                        resultSet.getInt("filas"),
                        resultSet.getInt("columnas"),
                        resultSet.getString("ubicacion")
                );

                listadoReportes.add(salaComentada);
            }

            for (SalaMasComenadaDTO listadoReporte : listadoReportes) {
                listadoReporte.setComentarios(obtenerComentarios(reporteSalas, listadoReporte.getCodigo()));
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de reporte de las 5 salas mas comentadas ");
        }

        return listadoReportes;
    }

    //Metodo que sirve para obtener los comentarios relacionados a una sala
    private List< SalaComentarioDTO> obtenerComentarios(ReporteSistemaRequest reporteSalas, String idSala) throws FormatoInvalidoException, ErrorInesperadoException {

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
            throw new ErrorInesperadoException("No se han podido obtener los datos de los comentarios del reporte de las 5 salas mas comentadas ");
        }

        return listadoComentarios;

    }

    //Metodo delegado para obtener la cantidad de reportes que se tienen EN TODO INTERVALO DE FECHAS
    public int cantidadReportesTodo(ReporteSistemaRequest reporteSalas) throws ErrorInesperadoException, DatosNoEncontradosException {

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CANTIDAD_TODO_REPORTES);) {

            ResultSet result = query.executeQuery();
            if (result.next()) {
                return result.getInt("cantidad");
            } else {

                throw new DatosNoEncontradosException("No hay registros de reportes de comentarios de reportes de las 5 salas mas comentadas ");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se ha podido conectar con la base de datos para obtener la cantidad de reportes de las 5 salas mas comentadas ");
        }
    }

    //Metodo que sirve para poder consultar el reporte de comentarios de las 5 salas de cine 
    private List< SalaComentarioDTO> obtenerComentariosTodo(ReporteSistemaRequest reporteSalas, String idSala) throws FormatoInvalidoException, ErrorInesperadoException {

        if (reporteSalas == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        List<SalaComentarioDTO> listadoComentarios = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(COMENTARIOS_TODO_SALAS);) {

            query.setString(1, idSala);

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
            throw new ErrorInesperadoException("No se han podido obtener los datos de los comentarios del reporte de las 5 salas mas comentadas ");
        }

        return listadoComentarios;

    }

    //Metodo que sirve para obtener todos los registros de las salas mas comentadas en todo intervalo
     public List<SalaMasComenadaDTO> obtenerReporteTodoSalas(ReporteSistemaRequest reporteSalas) throws ErrorInesperadoException, FormatoInvalidoException {

        if (reporteSalas == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        List<SalaMasComenadaDTO> listadoReportes = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(SALAS_TODO_COMENTADAS);) {

            query.setDate(1, java.sql.Date.valueOf(reporteSalas.getFechaInicio()));
            query.setDate(2, java.sql.Date.valueOf(reporteSalas.getFechaFin()));
            query.setInt(3, reporteSalas.getLimit());
            query.setInt(4, reporteSalas.getOffset());

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                SalaMasComenadaDTO salaComentada = new SalaMasComenadaDTO(
                        resultSet.getString("codigo"),
                        resultSet.getString("cineAsociado"),
                        resultSet.getString("nombre"),
                        resultSet.getInt("filas"),
                        resultSet.getInt("columnas"),
                        resultSet.getString("ubicacion")
                );

                listadoReportes.add(salaComentada);
            }

            for (SalaMasComenadaDTO listadoReporte : listadoReportes) {
                listadoReporte.setComentarios(obtenerComentariosTodo(reporteSalas, listadoReporte.getCodigo()));
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de reporte de las 5 salas mas comentadas ");
        }

        return listadoReportes;
    }

}
