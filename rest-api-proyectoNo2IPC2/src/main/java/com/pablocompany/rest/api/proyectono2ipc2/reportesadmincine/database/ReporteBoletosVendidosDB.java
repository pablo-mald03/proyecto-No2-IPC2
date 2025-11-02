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
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models.ReporteBoletosVendidosDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models.ReporteSalasGustadasDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models.SalaCalificacionDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models.UsuarioBoletosCompradosDTO;
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
//Clase delegada para poder obtener los reportes de boletos vendidos
public class ReporteBoletosVendidosDB {

    //Constante que permite obtener el reporte de boletos comprados en un intervalo de tiempo SIN FILTRO
    private final String REPORTE_BOLETOS = "SELECT sa.codigo, ci.nombre AS `cineAsociado`, sa.nombre, sa.ubicacion, COUNT(pb.numero) AS `totalBoletosVendidos`, SUM(pb.monto) AS `totalRecaudado` FROM sala AS `sa` JOIN cine AS `ci` ON sa.codigo_cine = ci.codigo JOIN pago_boleto AS `pb` ON sa.codigo = pb.codigo_sala WHERE pb.fecha_pago BETWEEN ? AND ? GROUP BY sa.codigo, ci.nombre, sa.nombre, sa.filas, sa.columnas, sa.ubicacion ORDER BY totalRecaudado DESC LIMIT ? OFFSET ?";

    //Constante que permite obtener el conteo de cantidad de reportes que hay en base a una fecha
    private final String CANTIDAD_REPORTES = "SELECT COUNT(*) AS `cantidad` FROM sala AS `sa` JOIN cine AS `ci` ON sa.codigo_cine = ci.codigo JOIN pago_boleto AS `pb` ON sa.codigo = pb.codigo_sala WHERE pb.fecha_pago BETWEEN ? AND ? GROUP BY sa.codigo, ci.nombre, sa.nombre, sa.filas, sa.columnas, sa.ubicacion";

    //Constante que permite obtener el conteo de cantidad de reportes que hay en base a una fecha con filtro por sala de cine
    private final String CANTIDAD_REPORTES_FILTRO = "SELECT COUNT(*) AS `cantidad` FROM sala AS `sa` JOIN cine AS `ci` ON sa.codigo_cine = ci.codigo JOIN pago_boleto AS `pb` ON sa.codigo = pb.codigo_sala WHERE sa.codigo = ? AND pb.fecha_pago BETWEEN ? AND ? GROUP BY sa.codigo, ci.nombre, sa.nombre, sa.filas, sa.columnas, sa.ubicacion";

    //Constante que permite obtener el reporte de los boletos que ha comprado el usuario
    private final String USUARIOS_SALA = "SELECT u.id AS `idUsuario`, u.nombre, COUNT(pb.numero) AS `boletosComprados`, SUM(pb.monto) AS `totalPagado`  FROM sala AS `sa` JOIN pago_boleto AS `pb` ON sa.codigo = pb.codigo_sala JOIN usuario AS `u` ON pb.id_usuario = u.id WHERE sa.codigo =  ? AND  pb.fecha_pago BETWEEN ? AND ? GROUP  BY  u.id, u.nombre ORDER BY totalPagado DESC";

    //Constante que permite obtener el registro de pagos de boleto en un intervalo de tiempo CON FILTRO
    private final String REPORTE_BOLETOS_FILTRO = "SELECT sa.codigo, ci.nombre AS `cineAsociado`, sa.nombre, sa.ubicacion, COUNT(pb.numero) AS `totalBoletosVendidos`, SUM(pb.monto) AS `totalRecaudado` FROM sala AS `sa` JOIN cine AS `ci` ON sa.codigo_cine = ci.codigo JOIN pago_boleto AS `pb` ON sa.codigo = pb.codigo_sala WHERE sa.codigo = ? AND pb.fecha_pago BETWEEN ? AND ? GROUP BY sa.codigo, ci.nombre, sa.nombre, sa.filas, sa.columnas, sa.ubicacion ORDER BY totalRecaudado DESC LIMIT ? OFFSET ?";

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
            throw new ErrorInesperadoException("No se ha podido conectar con la base de datos para obtener la cantidad de reportes de boletos vendidos Sin filtro" + e.getMessage());
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
            throw new ErrorInesperadoException("No se ha podido conectar con la base de datos para obtener la cantidad boletos vendidos con filtro");
        }
    }

    //Metodo que sirve para poder consultar el reporte de 5 salas mas gustadas SIN FLITRO
    public List<ReporteBoletosVendidosDTO> obtenerReporteBoletodVendidos(ReporteRequest reporteSalas) throws ErrorInesperadoException, FormatoInvalidoException {

        if (reporteSalas == null) {
            throw new FormatoInvalidoException("La referencia de request de reporte de boletos esta vacia");
        }

        List<ReporteBoletosVendidosDTO> listadoReportes = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(REPORTE_BOLETOS);) {

            query.setDate(1, java.sql.Date.valueOf(reporteSalas.getFechaInicio()));
            query.setDate(2, java.sql.Date.valueOf(reporteSalas.getFechaFin()));
            query.setInt(3, reporteSalas.getLimit());
            query.setInt(4, reporteSalas.getOffset());

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                ReporteBoletosVendidosDTO reporteEncontrado = new ReporteBoletosVendidosDTO(
                        resultSet.getString("codigo"),
                        resultSet.getString("cineAsociado"),
                        resultSet.getString("nombre"),
                        resultSet.getString("ubicacion"),
                        resultSet.getBigDecimal("totalRecaudado").doubleValue()
                );

                listadoReportes.add(reporteEncontrado);
            }

            for (ReporteBoletosVendidosDTO reporte : listadoReportes) {
                reporte.setUsuarios(obtenerBoletos(reporteSalas, reporte.getCodigo()));
            }
            
        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de reporte de boletos vendidos SIN FILTRO");
        }

        return listadoReportes;
    }

    //Metodo que sirve para poder consultar el reporte de 5 salas mas gustadas CON FLITRO
    public List<ReporteBoletosVendidosDTO> obtenerReporteBoletosVendidosFiltro(ReporteRequest reporteSalas) throws ErrorInesperadoException, FormatoInvalidoException {

        if (reporteSalas == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        List<ReporteBoletosVendidosDTO> listadoReportes = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(REPORTE_BOLETOS_FILTRO);) {

            query.setString(1, reporteSalas.getIdSala());
            query.setDate(2, java.sql.Date.valueOf(reporteSalas.getFechaInicio()));
            query.setDate(3, java.sql.Date.valueOf(reporteSalas.getFechaFin()));
            query.setInt(4, reporteSalas.getLimit());
            query.setInt(5, reporteSalas.getOffset());

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                ReporteBoletosVendidosDTO reporteEncontrado = new ReporteBoletosVendidosDTO(
                        resultSet.getString("codigo"),
                        resultSet.getString("cineAsociado"),
                        resultSet.getString("nombre"),
                        resultSet.getString("ubicacion"),
                        resultSet.getBigDecimal("totalRecaudado").doubleValue()
                );

                listadoReportes.add(reporteEncontrado);
            }

            for (ReporteBoletosVendidosDTO reporte : listadoReportes) {
                reporte.setUsuarios(obtenerBoletos(reporteSalas, reporte.getCodigo()));
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de reporte de boletos vendidos en un intervalo de tiempo");
        }

        return listadoReportes;
    }

    //Metodo que sirve para obtener las valificaciones que hacen los usuarios en las salas
    private List< UsuarioBoletosCompradosDTO> obtenerBoletos(ReporteRequest reporteSalas, String idSala) throws FormatoInvalidoException, ErrorInesperadoException {

        if (reporteSalas == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        List<UsuarioBoletosCompradosDTO> listadoUsuarios = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(USUARIOS_SALA);) {

            query.setString(1, idSala);
            query.setDate(2, java.sql.Date.valueOf(reporteSalas.getFechaInicio()));
            query.setDate(3, java.sql.Date.valueOf(reporteSalas.getFechaFin()));

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                UsuarioBoletosCompradosDTO usuarioEncontrado = new UsuarioBoletosCompradosDTO(
                        resultSet.getString("idUsuario"),
                        resultSet.getString("nombre"),
                        resultSet.getInt("boletosComprados"),
                        resultSet.getBigDecimal("totalPagado").doubleValue()
                );

                listadoUsuarios.add(usuarioEncontrado);
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de los usuarios que compraron boletos en salas de cine" + e.getMessage());
        }

        return listadoUsuarios;

    }

}
