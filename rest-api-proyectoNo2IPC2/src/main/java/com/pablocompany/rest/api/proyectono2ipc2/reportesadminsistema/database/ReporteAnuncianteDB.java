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
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.dtos.ReporteAnuncianteRequest;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models.AnuncioDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models.ReporteAnuncianteDTO;
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
//Clase delegada para poder comunicarse y realizar las respectivas operaciones con la base de datos 
public class ReporteAnuncianteDB {

    //Constante que sirve para poder obtener el anunciante 
    private final String REPORTE_ANUNCIANTE = "SELECT us.id, us.nombre, us.correo, SUM(pa.monto) AS `total`,  MAX(pa.fecha_pago) AS `fecha_reciente`  FROM usuario AS `us` JOIN pago_anuncio AS `pa` ON pa.id_usuario = us.id WHERE pa.fecha_pago BETWEEN ? AND ? GROUP BY us.id, us.nombre, us.correo ORDER BY fecha_reciente ASC LIMIT ? OFFSET ?";

    //Constante que sirve para obtener el reporte de anunciantes con filtro
    private final String REPORTE_ANUNCIANTE_FILTRO = "SELECT us.id, us.nombre, us.correo, SUM(pa.monto) AS `total`,  MAX(pa.fecha_pago) AS `fecha_reciente`  FROM usuario AS `us` JOIN pago_anuncio AS `pa` ON pa.id_usuario = us.id WHERE us.id = ? AND pa.fecha_pago BETWEEN ? AND ? GROUP BY us.id, us.nombre, us.correo ORDER BY fecha_reciente ASC LIMIT ? OFFSET ?";

    //Constante que permite saber la cantidad de reportes sin filtro
    private final String CANTIDAD_REPORTES = "SELECT COUNT(DISTINCT us.id) AS `cantidad` FROM usuario AS us JOIN pago_anuncio AS `pa` ON pa.id_usuario = us.id WHERE pa.fecha_pago BETWEEN ? AND ?";

    //Constante que permite saber la cantidad de reportes con filtro
    private final String CANTIDAD_REPORTES_FILTRO = "SELECT COUNT(DISTINCT us.id) AS `cantidad` FROM usuario AS us JOIN pago_anuncio AS `pa` ON pa.id_usuario = us.id WHERE us.id = ? AND pa.fecha_pago BETWEEN ? AND ?";

    //Constante que permite retornar todos los anuncios realacionados a los reportes en un intervalo de tiempo 
    private final String ANUNCIOS_REPORTES = "SELECT a.codigo, a.estado, a.nombre, ca.tipo, a.fecha_expiracion, a.fecha_compra FROM anuncio AS `a` JOIN configuracion_anuncio AS `ca` ON a.codigo_tipo = ca.codigo WHERE a.id_usuario = ? AND a.fecha_compra BETWEEN ? AND ? ORDER BY a.fecha_compra ASC;";

    //Constante que permite obtener todos los reportes de anunciante
    private final String REPORTE_TODO_ANUNCIANTE = "SELECT us.id, us.nombre, us.correo, SUM(pa.monto) AS `total`,  MAX(pa.fecha_pago) AS `fecha_reciente`  FROM usuario AS `us` JOIN pago_anuncio AS `pa` ON pa.id_usuario = us.id GROUP BY us.id, us.nombre, us.correo ORDER BY fecha_reciente ASC LIMIT ? OFFSET ?";

    //Constante que permite  obtener todos los reportes de anunciate por filtro
    private final String REPORTE_ANUNCIANTE_TODO_FILTRO = "SELECT us.id, us.nombre, us.correo, SUM(pa.monto) AS `total`,  MAX(pa.fecha_pago) AS `fecha_reciente`  FROM usuario AS `us` JOIN pago_anuncio AS `pa` ON pa.id_usuario = us.id WHERE us.id = ? GROUP BY us.id, us.nombre, us.correo ORDER BY fecha_reciente ASC LIMIT ? OFFSET ?";

    //Constante que permite obtener la cantidad de reportes en TODO INTERVALO
    private final String CANTIDAD_TODO_REPORTES = "SELECT COUNT(DISTINCT us.id) AS `cantidad` FROM usuario AS us JOIN pago_anuncio AS `pa` ON pa.id_usuario = us.id";

    //Constante que permite obtener la cantidad de reportes con filtro en todo intervalo
    private final String CANTIDAD_REPORTES_TODO_FILTRO = "SELECT COUNT(DISTINCT us.id) AS `cantidad` FROM usuario AS us JOIN pago_anuncio AS `pa` ON pa.id_usuario = us.id WHERE us.id = ?";

    //Constante que permite obtener todos los anuncios relacionados al usuario
    private final String ANUNCIOS_TODO_REPORTES = "SELECT a.codigo, a.estado, a.nombre, ca.tipo, a.fecha_expiracion, a.fecha_compra FROM anuncio AS `a` JOIN configuracion_anuncio AS `ca` ON a.codigo_tipo = ca.codigo WHERE a.id_usuario = ? ORDER BY a.fecha_compra ASC";

    //Metodo delegado para obtener la cantidad de reportes que se tienen en el intervalo de fechas
    public int cantidadReportesSinFiltro(ReporteAnuncianteRequest reporteSalas) throws ErrorInesperadoException, DatosNoEncontradosException {

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
    public int cantidadReportesFiltro(ReporteAnuncianteRequest reporteRequest) throws ErrorInesperadoException, DatosNoEncontradosException {

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CANTIDAD_REPORTES_FILTRO);) {

            query.setString(1, reporteRequest.getIdUsuario());
            query.setDate(2, java.sql.Date.valueOf(reporteRequest.getFechaInicio()));
            query.setDate(3, java.sql.Date.valueOf(reporteRequest.getFechaFin()));

            ResultSet result = query.executeQuery();
            if (result.next()) {
                return result.getInt("cantidad");
            } else {

                throw new DatosNoEncontradosException("No hay registros de reportes de ganancias por anunciante con filtro");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se ha podido conectar con la base de datos para obtener la cantidad de reportes de ganancias por anunciante con filtro");
        }
    }

    //Metodo que sirve para poder consultar el reporte de ganancias por anunciante SIN FLITRO
     public List<ReporteAnuncianteDTO> obtenerReporteAnuncianteSinFiltro(ReporteAnuncianteRequest reporteAnunciante) throws ErrorInesperadoException, FormatoInvalidoException {

        if (reporteAnunciante == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        List<ReporteAnuncianteDTO> listadoReportes = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(REPORTE_ANUNCIANTE);) {

            query.setDate(1, java.sql.Date.valueOf(reporteAnunciante.getFechaInicio()));
            query.setDate(2, java.sql.Date.valueOf(reporteAnunciante.getFechaFin()));
            query.setInt(3, reporteAnunciante.getLimit());
            query.setInt(4, reporteAnunciante.getOffset());

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                ReporteAnuncianteDTO reporteEncontrado = new ReporteAnuncianteDTO(
                        resultSet.getString("id"),
                        resultSet.getString("nombre"),
                        resultSet.getString("correo"),
                        resultSet.getBigDecimal("total").doubleValue()
                );

                listadoReportes.add(reporteEncontrado);
            }

            for (ReporteAnuncianteDTO reporte : listadoReportes) {
                reporte.setAnuncios(obtenerAnuncios(reporteAnunciante, reporte.getId()));
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de reporte de las ganancias por anunciante en un intervalo sin filtro");
        }

        return listadoReportes;
    }

    //Metodo que sirve para poder consultar el reporte de ganancias por anunciante en un intervalo de tiempo CON FLITRO
    public List<ReporteAnuncianteDTO> obtenerReporteAnunciantesFiltro(ReporteAnuncianteRequest reporteAnunciante) throws ErrorInesperadoException, FormatoInvalidoException {

        if (reporteAnunciante == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        List<ReporteAnuncianteDTO> listadoReportes = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(REPORTE_ANUNCIANTE_FILTRO);) {

            query.setString(1, reporteAnunciante.getIdUsuario());
            query.setDate(2, java.sql.Date.valueOf(reporteAnunciante.getFechaInicio()));
            query.setDate(3, java.sql.Date.valueOf(reporteAnunciante.getFechaFin()));
            query.setInt(4, reporteAnunciante.getLimit());
            query.setInt(5, reporteAnunciante.getOffset());

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                ReporteAnuncianteDTO reporteEncontrado = new ReporteAnuncianteDTO(
                        resultSet.getString("id"),
                        resultSet.getString("nombre"),
                        resultSet.getString("correo"),
                        resultSet.getBigDecimal("total").doubleValue()
                );

                listadoReportes.add(reporteEncontrado);
            }

            for (ReporteAnuncianteDTO reporte : listadoReportes) {
                reporte.setAnuncios(obtenerAnuncios(reporteAnunciante, reporte.getId()));
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de reporte de ganancias por anunciante con filtro ");
        }

        return listadoReportes;
    }

    //Metodo que sirve para obtener los anuncios comprados en relacion al anunciante  en un intervalo de fechas
    private List<AnuncioDTO> obtenerAnuncios(ReporteAnuncianteRequest reporteAnunciante, String idUsuario) throws FormatoInvalidoException, ErrorInesperadoException {

        if (reporteAnunciante == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        List<AnuncioDTO> listadoAnuncios = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(ANUNCIOS_REPORTES);) {

            query.setString(1, idUsuario.trim());
            query.setDate(2, java.sql.Date.valueOf(reporteAnunciante.getFechaInicio()));
            query.setDate(3, java.sql.Date.valueOf(reporteAnunciante.getFechaFin()));

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                AnuncioDTO anuncioEncontrado = new AnuncioDTO(
                        resultSet.getString("codigo"),
                        resultSet.getBoolean("estado"),
                        resultSet.getString("nombre"),
                        resultSet.getString("tipo"),
                        resultSet.getDate("fecha_expiracion").toLocalDate(),
                        resultSet.getDate("fecha_compra").toLocalDate()
                );

                listadoAnuncios.add(anuncioEncontrado);
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de los comentarios del reporte de ganancias por anunciante en un intervalo de fechas");
        }

        return listadoAnuncios;

    }

    //================================APARTADO DONDE SE MUESTRAN TODOS LOS REPORTES EN CUALQUIER INTERVALO============================
    //Metodo delegado para obtener la cantidad de reportes que se tienen EN TODO INTERVALO DE FECHAS
    public int cantidadReportesTodoSinFiltro(ReporteAnuncianteRequest reporteAnunciante) throws ErrorInesperadoException, DatosNoEncontradosException {

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CANTIDAD_TODO_REPORTES);) {

            ResultSet result = query.executeQuery();
            if (result.next()) {
                return result.getInt("cantidad");
            } else {

                throw new DatosNoEncontradosException("No hay registros de reportes de ganancias por anunciante sin filtro ");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se ha podido conectar con la base de datos para obtener la cantidad de todas las ganancias por anunciante sin filtro");
        }
    }

    //Metodo delegado para obtener la cantidad de reportes que se tienen de ganancias por anunciante en todo intervalo de tiempo
    public int cantidadReportesTodoFiltro(ReporteAnuncianteRequest reporteAnunciante) throws ErrorInesperadoException, DatosNoEncontradosException {

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CANTIDAD_REPORTES_TODO_FILTRO);) {

            query.setString(1, reporteAnunciante.getIdUsuario());

            ResultSet result = query.executeQuery();
            if (result.next()) {
                return result.getInt("cantidad");
            } else {

                throw new DatosNoEncontradosException("No hay registros de reportes de ganancias por anunciante con filtro");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se ha podido conectar con la base de datos para obtener la cantidad de todas las ganancias por anunciante con filtro");
        }
    }

    //Metodo que sirve para poder consultar el reporte de todos los anunciantes SIN FLITRO
    public List<ReporteAnuncianteDTO> obtenerReporteTodoAnunciante(ReporteAnuncianteRequest reporteAnunciante) throws ErrorInesperadoException, FormatoInvalidoException {

        if (reporteAnunciante == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        List<ReporteAnuncianteDTO> listadoReportes = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(REPORTE_TODO_ANUNCIANTE);) {

            query.setInt(1, reporteAnunciante.getLimit());
            query.setInt(2, reporteAnunciante.getOffset());

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                ReporteAnuncianteDTO reporteEncontrado = new ReporteAnuncianteDTO(
                        resultSet.getString("id"),
                        resultSet.getString("nombre"),
                        resultSet.getString("correo"),
                        resultSet.getBigDecimal("total").doubleValue()
                );

                listadoReportes.add(reporteEncontrado);
            }

            for (ReporteAnuncianteDTO reporte : listadoReportes) {
                reporte.setAnuncios(obtenerTodosAnuncios(reporteAnunciante, reporte.getId()));
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de reporte de todas las ganancias por anunciante");
        }

        return listadoReportes;
    }

    //Metodo que sirve para poder consultar el reporte de todos los anunciantes CON FILTRO
    public List<ReporteAnuncianteDTO> obtenerReporteTodoAnunciantesFiltro(ReporteAnuncianteRequest reporteAnunciante) throws ErrorInesperadoException, FormatoInvalidoException {

        if (reporteAnunciante == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        List<ReporteAnuncianteDTO> listadoReportes = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(REPORTE_ANUNCIANTE_TODO_FILTRO);) {

            query.setString(1, reporteAnunciante.getIdUsuario());
            query.setInt(2, reporteAnunciante.getLimit());
            query.setInt(3, reporteAnunciante.getOffset());

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                ReporteAnuncianteDTO reporteEncontrado = new ReporteAnuncianteDTO(
                        resultSet.getString("id"),
                        resultSet.getString("nombre"),
                        resultSet.getString("correo"),
                        resultSet.getBigDecimal("total").doubleValue()
                );

                listadoReportes.add(reporteEncontrado);
            }

            for (ReporteAnuncianteDTO reporte : listadoReportes) {
                reporte.setAnuncios(obtenerTodosAnuncios(reporteAnunciante, reporte.getId()));
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de reporte de ganancias por anunciante ");
        }

        return listadoReportes;
    }

    //Metodo que sirve para obtener los anuncios comprados en realacion al anunciante 
    private List<AnuncioDTO> obtenerTodosAnuncios(ReporteAnuncianteRequest reporteAnunciante, String idUsuario) throws FormatoInvalidoException, ErrorInesperadoException {

        if (reporteAnunciante == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        List<AnuncioDTO> listadoAnuncios = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(ANUNCIOS_TODO_REPORTES);) {

            query.setString(1, idUsuario.trim());

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                AnuncioDTO anuncioEncontrado = new AnuncioDTO(
                        resultSet.getString("codigo"),
                        resultSet.getBoolean("estado"),
                        resultSet.getString("nombre"),
                        resultSet.getString("tipo"),
                        resultSet.getDate("fecha_expiracion").toLocalDate(),
                        resultSet.getDate("fecha_compra").toLocalDate()
                );

                listadoAnuncios.add(anuncioEncontrado);
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de los comentarios del reporte de ganancias por anunciante");
        }

        return listadoAnuncios;

    }

}
