/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.database;

import com.pablocompany.rest.api.proyectono2ipc2.connectiondb.DBConnectionSingleton;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.dtos.ReporteRequest;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models.SalaMasGustadaDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models.UsuarioReporteDTO;
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
public class ReporteSalasMasGustadasDB {

    //Constante que permite obtener el reporte de 5 salas mas gustadas en un intervalo de fechas 
    private final String REPORTE_SALAS_POPULARES
            = "SELECT sa.codigo, ci.nombre AS `cineAsociado`, sa.nombre, sa.filas, sa.columnas, sa.ubicacion, "
            + "AVG(CAST(va.calificacion AS DECIMAL(10,2))) AS promedio_calificacion, "
            + "COUNT(va.calificacion) AS cantidad_votos, "
            + "AVG(CAST(va.calificacion AS DECIMAL(10,2))) * COUNT(va.calificacion) AS `puntuacion_total`, "
            + "COUNT(DISTINCT pb.numero) AS `totalBoletosVendidos` "
            + "FROM sala AS `sa` "
            + "JOIN cine AS `ci` ON sa.codigo_cine = ci.codigo "
            + "JOIN valoracion_sala AS `va` ON sa.codigo = va.codigo_sala "
            + "LEFT JOIN pago_boleto AS `pb` ON sa.codigo = pb.codigo_sala AND pb.fecha_pago BETWEEN ? AND ? "
            + "WHERE va.fecha_posteo BETWEEN ? AND ? "
            + "GROUP BY sa.codigo, ci.nombre, sa.nombre, sa.filas, sa.columnas, sa.ubicacion "
            + "ORDER BY puntuacion_total DESC "
            + "LIMIT ? OFFSET ?";

    //Constante que permite obtener a los usuarios que realizaron la compra de boletos en las salas en un intervalo de cine
    private final String USUARIOS_SALA
            = "SELECT u.id AS `idUsuario`, u.nombre, u.correo, "
            + "COUNT(pb.numero) AS `boletosComprados`, "
            + "SUM(pb.monto) AS `totalPagado` "
            + "FROM pago_boleto AS pb "
            + "JOIN usuario AS u ON pb.id_usuario = u.id "
            + "WHERE pb.codigo_sala = ? AND pb.fecha_pago BETWEEN ? AND ? "
            + "GROUP BY u.id, u.nombre, u.correo "
            + "ORDER BY totalPagado DESC;";

    //Constante que permite obtener el reporte de 5 salas mas gustadas en un intervalo de fechas 
    private final String REPORTE_SALAS_TODO_POPULARES
            = "SELECT sa.codigo, ci.nombre AS `cineAsociado`, sa.nombre, sa.filas, sa.columnas, sa.ubicacion, "
            + "AVG(CAST(va.calificacion AS DECIMAL(10,2))) AS promedio_calificacion, "
            + "COUNT(va.calificacion) AS cantidad_votos, "
            + "AVG(CAST(va.calificacion AS DECIMAL(10,2))) * COUNT(va.calificacion) AS `puntuacion_total`, "
            + "COUNT(DISTINCT pb.numero) AS `totalBoletosVendidos` "
            + "FROM sala AS `sa` "
            + "JOIN cine AS `ci` ON sa.codigo_cine = ci.codigo "
            + "JOIN valoracion_sala AS `va` ON sa.codigo = va.codigo_sala "
            + "LEFT JOIN pago_boleto AS `pb` ON sa.codigo = pb.codigo_sala AND pb.fecha_pago "
            + "GROUP BY sa.codigo, ci.nombre, sa.nombre, sa.filas, sa.columnas, sa.ubicacion "
            + "ORDER BY puntuacion_total DESC "
            + "LIMIT ? OFFSET ?";

    //Constante que permite obtener a los usuarios que realizaron la compra de boletos en las salas en un intervalo de cine
    private final String USUARIOS_TODO_SALA
            = "SELECT u.id AS `idUsuario`, u.nombre, u.correo, "
            + "COUNT(pb.numero) AS `boletosComprados`, "
            + "SUM(pb.monto) AS `totalPagado` "
            + "FROM pago_boleto AS pb "
            + "JOIN usuario AS u ON pb.id_usuario = u.id "
            + "WHERE pb.codigo_sala = ? "
            + "GROUP BY u.id, u.nombre, u.correo "
            + "ORDER BY totalPagado DESC;";

    //Constante que permite obtener la cantidad total de salas populares (sin filtro de fechas)
    private final String CANTIDAD_SALAS_TODO_POPULARES
            = "SELECT COUNT(*) AS `cantidad` "
            + "FROM ( "
            + "  SELECT sa.codigo "
            + "  FROM sala AS `sa` "
            + "  JOIN cine AS `ci` ON sa.codigo_cine = ci.codigo "
            + "  JOIN valoracion_sala AS `va` ON sa.codigo = va.codigo_sala "
            + "  LEFT JOIN pago_boleto AS `pb` ON sa.codigo = pb.codigo_sala AND pb.fecha_pago  "
            + "  GROUP BY sa.codigo, ci.nombre, sa.nombre, sa.filas, sa.columnas, sa.ubicacion LIMIT 5"
            + ") AS subconsulta ";

    //Constante que permite obtener la cantidad total de salas populares dentro de un intervalo de fechas
    private final String CANTIDAD_SALAS_POPULARES
            = "SELECT COUNT(*) AS `cantidad` "
            + "FROM ( "
            + "  SELECT sa.codigo "
            + "  FROM sala AS `sa` "
            + "  JOIN cine AS `ci` ON sa.codigo_cine = ci.codigo "
            + "  JOIN valoracion_sala AS `va` ON sa.codigo = va.codigo_sala "
            + "  LEFT JOIN pago_boleto AS `pb` ON sa.codigo = pb.codigo_sala AND pb.fecha_pago BETWEEN ? AND ? "
            + "  WHERE va.fecha_posteo BETWEEN ? AND ? "
            + "  GROUP BY sa.codigo, ci.nombre, sa.nombre, sa.filas, sa.columnas, sa.ubicacion LIMIT 5"
            + ") AS subconsulta ";

    //Metodo delegado para obtener la cantidad de reportes de 5 salas mas gustadas que se tienen en el intervalo de fechas
    public int cantidadReportes(ReporteRequest reporteSalas) throws ErrorInesperadoException, DatosNoEncontradosException {

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CANTIDAD_SALAS_POPULARES);) {

            query.setDate(1, java.sql.Date.valueOf(reporteSalas.getFechaInicio()));
            query.setDate(2, java.sql.Date.valueOf(reporteSalas.getFechaFin()));

            query.setDate(3, java.sql.Date.valueOf(reporteSalas.getFechaInicio()));
            query.setDate(4, java.sql.Date.valueOf(reporteSalas.getFechaFin()));

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

    //Metodo delegado para obtener la cantidad de reportes de 5 salas mas gustadas que se tienen en el intervalo de fechas EN TODO INTERVALO DE TIEMPO
    public int cantidadTodosReportes(ReporteRequest reporteSalas) throws ErrorInesperadoException, DatosNoEncontradosException {

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CANTIDAD_SALAS_TODO_POPULARES);) {

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

    //Metodo que sirve para poder consultar todas las salas en todo intervalo
    public List<SalaMasGustadaDTO> obtenerSalasFecha(ReporteRequest reporteSalas) throws ErrorInesperadoException, FormatoInvalidoException {

        if (reporteSalas == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        List<SalaMasGustadaDTO> listadoReportes = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(REPORTE_SALAS_POPULARES);) {

            query.setDate(1, java.sql.Date.valueOf(reporteSalas.getFechaInicio()));
            query.setDate(2, java.sql.Date.valueOf(reporteSalas.getFechaFin()));

            query.setDate(3, java.sql.Date.valueOf(reporteSalas.getFechaInicio()));
            query.setDate(4, java.sql.Date.valueOf(reporteSalas.getFechaFin()));

            query.setInt(5, reporteSalas.getLimit());
            query.setInt(6, reporteSalas.getOffset());

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                SalaMasGustadaDTO reporteEncontrado = new SalaMasGustadaDTO(
                        resultSet.getString("codigo"),
                        resultSet.getString("cineAsociado"),
                        resultSet.getString("nombre"),
                        resultSet.getInt("filas"),
                        resultSet.getInt("columnas"),
                        resultSet.getString("ubicacion"),
                        resultSet.getBigDecimal("totalBoletosVendidos").doubleValue()
                );

                listadoReportes.add(reporteEncontrado);
            }

            for (SalaMasGustadaDTO reporte : listadoReportes) {
                reporte.setUsuarios(obtenerUsuariosFecha(reporteSalas, reporte.getCodigo()));
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de reporte de las 5 salas mas gustadas");
        }

        return listadoReportes;
    }

    //Metodo que sirve para obtener las todas calificaciones en todos los intervalos de tiempo
    private List< UsuarioReporteDTO> obtenerUsuariosFecha(ReporteRequest reporteSalas, String idSala) throws FormatoInvalidoException, ErrorInesperadoException {

        if (reporteSalas == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        List<UsuarioReporteDTO> listadoUsuarios = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(USUARIOS_SALA);) {

            query.setString(1, idSala);
            query.setDate(2, java.sql.Date.valueOf(reporteSalas.getFechaInicio()));
            query.setDate(3, java.sql.Date.valueOf(reporteSalas.getFechaFin()));

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                UsuarioReporteDTO usuarioEncontrado = new UsuarioReporteDTO(
                        resultSet.getString("idUsuario"),
                        resultSet.getString("nombre"),
                        resultSet.getString("correo"),
                        resultSet.getInt("boletosComprados"),
                        resultSet.getBigDecimal("totalPagado").doubleValue()
                );

                listadoUsuarios.add(usuarioEncontrado);
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de los usuarios con boletos comprados");
        }

        return listadoUsuarios;

    }

    //Metodo que sirve para poder consultar todas las salas en todo intervalo
    public List<SalaMasGustadaDTO> obtenerTodasSalas(ReporteRequest reporteSalas) throws ErrorInesperadoException, FormatoInvalidoException {

        if (reporteSalas == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        List<SalaMasGustadaDTO> listadoReportes = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(REPORTE_SALAS_TODO_POPULARES);) {

            query.setInt(1, reporteSalas.getLimit());
            query.setInt(2, reporteSalas.getOffset());

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                SalaMasGustadaDTO reporteEncontrado = new SalaMasGustadaDTO(
                        resultSet.getString("codigo"),
                        resultSet.getString("cineAsociado"),
                        resultSet.getString("nombre"),
                        resultSet.getInt("filas"),
                        resultSet.getInt("columnas"),
                        resultSet.getString("ubicacion"),
                        resultSet.getBigDecimal("totalBoletosVendidos").doubleValue()
                );

                listadoReportes.add(reporteEncontrado);
            }

            for (SalaMasGustadaDTO reporte : listadoReportes) {
                reporte.setUsuarios(obtenerTodosUsuarios(reporteSalas, reporte.getCodigo()));
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de reporte de las 5 salas mas gustadas");
        }

        return listadoReportes;
    }

    //Metodo que sirve para obtener las todas calificaciones en todos los intervalos de tiempo
    private List< UsuarioReporteDTO> obtenerTodosUsuarios(ReporteRequest reporteSalas, String idSala) throws FormatoInvalidoException, ErrorInesperadoException {

        if (reporteSalas == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        List<UsuarioReporteDTO> listadoUsuarios = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(USUARIOS_TODO_SALA);) {

            query.setString(1, idSala);

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                UsuarioReporteDTO usuarioEncontrado = new UsuarioReporteDTO(
                        resultSet.getString("idUsuario"),
                        resultSet.getString("nombre"),
                        resultSet.getString("correo"),
                        resultSet.getInt("boletosComprados"),
                        resultSet.getBigDecimal("totalPagado").doubleValue()
                );

                listadoUsuarios.add(usuarioEncontrado);
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de los usuarios con boletos comprados");
        }

        return listadoUsuarios;

    }

}
