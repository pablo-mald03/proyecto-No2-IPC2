/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.database;

import com.pablocompany.rest.api.proyectono2ipc2.connectiondb.DBConnectionSingleton;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.dtos.ReporteAnuncioRequest;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models.ReporteAnuncianteDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models.ReporteAnuncioDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.services.ConvertirBase64Service;
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
public class ReporteAnunciosCompradosDB {

    //Constante que sirve para poder obtener todos los anuncios en un intervalo de tiempo
    private final String REPORTE_ANUNCIOS = "SELECT a.codigo, a.estado, a.nombre, a.caducacion, a.fecha_expiracion, a.fecha_compra, a.url, a.texto, a.foto,  ca.codigo AS `codigoTipo`, us.id AS `idUsuario`, us.nombre AS `nombreUsuario` FROM anuncio AS a JOIN configuracion_anuncio AS ca ON a.codigo_tipo = ca.codigo JOIN usuario AS `us` ON us.id = a.id_usuario WHERE a.fecha_compra BETWEEN ? AND ? ORDER BY a.fecha_compra ASC LIMIT ? OFFSET ?";

    //Constante que sirve  para poder obtener todos los anuncios en un intervalo de tiempo con filtro
    private final String REPORTE_ANUNCIOS_FILTRO = "SELECT a.codigo, a.estado, a.nombre, a.caducacion, a.fecha_expiracion, a.fecha_compra, a.url, a.texto, a.foto,  ca.codigo AS `codigoTipo`, us.id AS `idUsuario`, us.nombre AS `nombreUsuario` FROM anuncio AS `a` JOIN configuracion_anuncio AS ca ON a.codigo_tipo = ca.codigo JOIN usuario AS `us` ON us.id = a.id_usuario WHERE ca.tipo = ? AND a.fecha_compra BETWEEN ? AND ? ORDER BY a.fecha_compra ASC LIMIT ? OFFSET ?";

    //Constante que permite saber la cantidad de reportes sin filtro
    private final String CANTIDAD_REPORTES = " SELECT COUNT(*) AS `cantidad` FROM anuncio WHERE fecha_compra BETWEEN ? AND ?";

    //Constante que permite saber la cantidad de reportes con filtro
    private final String CANTIDAD_REPORTES_FILTRO = "SELECT COUNT(*) AS `cantidad` FROM anuncio AS a JOIN configuracion_anuncio AS ca ON a.codigo_tipo = ca.codigo WHERE ca.tipo = ? AND a.fecha_compra BETWEEN ? AND ? ";

    //Constante que sirve para poder obtener todos los anuncios 
    private final String REPORTE_TODO_ANUNCIOS = "SELECT a.codigo, a.estado, a.nombre, a.caducacion, a.fecha_expiracion, a.fecha_compra, a.url, a.texto, a.foto,  ca.codigo AS `codigoTipo`, us.id AS `idUsuario`, us.nombre AS `nombreUsuario` FROM anuncio AS a JOIN configuracion_anuncio AS ca ON a.codigo_tipo = ca.codigo JOIN usuario AS `us` ON us.id = a.id_usuario ORDER BY a.fecha_compra ASC LIMIT ? OFFSET ?";

    //Constante que sirve para obtener todos los anuncios con filtro
    private final String REPORTE_ANUNCIOS_TODO_FILTRO = "SELECT a.codigo, a.estado, a.nombre, a.caducacion, a.fecha_expiracion, a.fecha_compra, a.url, a.texto, a.foto,  ca.codigo AS `codigoTipo`, us.id AS `idUsuario`, us.nombre AS `nombreUsuario` FROM anuncio AS `a` JOIN configuracion_anuncio AS ca ON a.codigo_tipo = ca.codigo JOIN usuario AS `us` ON us.id = a.id_usuario WHERE ca.tipo = ? ORDER BY a.fecha_compra ASC LIMIT ? OFFSET ?";

    //Constante que permite saber la cantidad de reportes sin filtro
    private final String CANTIDAD_TODO_REPORTES = " SELECT COUNT(*) AS `cantidad` FROM anuncio";

    //Constante que permite saber la cantidad de reportes con filtro
    private final String CANTIDAD_REPORTES_TODO_FILTRO = "SELECT COUNT(*) AS `cantidad` FROM anuncio AS a JOIN configuracion_anuncio AS ca ON a.codigo_tipo = ca.codigo WHERE ca.tipo = ?";

    //Metodo delegado para obtener la cantidad de reportes que se tienen en el intervalo de fechas
    public int cantidadReportesSinFiltro(ReporteAnuncioRequest reporteRequest) throws ErrorInesperadoException, DatosNoEncontradosException {

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CANTIDAD_REPORTES);) {

            query.setDate(1, java.sql.Date.valueOf(reporteRequest.getFechaInicio()));
            query.setDate(2, java.sql.Date.valueOf(reporteRequest.getFechaFin()));

            ResultSet result = query.executeQuery();
            if (result.next()) {
                return result.getInt("cantidad");
            } else {

                throw new DatosNoEncontradosException("No hay registros de reportes de anuncios comprados");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se ha podido conectar con la base de datos para obtener la cantidad de reportes de anuncios comprados");
        }
    }

    //Metodo delegado para obtener la cantidad de reportes que se tienen en el intervalo de fechas con filtro
    public int cantidadReportesFiltro(ReporteAnuncioRequest reporteRequest) throws ErrorInesperadoException, DatosNoEncontradosException {

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CANTIDAD_REPORTES_FILTRO);) {

            query.setString(1, reporteRequest.getTipoAnuncio().trim());
            query.setDate(2, java.sql.Date.valueOf(reporteRequest.getFechaInicio()));
            query.setDate(3, java.sql.Date.valueOf(reporteRequest.getFechaFin()));

            ResultSet result = query.executeQuery();
            if (result.next()) {
                return result.getInt("cantidad");
            } else {

                throw new DatosNoEncontradosException("No hay registros de reportes de anuncios comprados con filtro");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se ha podido conectar con la base de datos para obtener la cantidad de reportes de anuncios comprados con filtro");
        }
    }

    //Metodo que sirve para poder consultar el reporte de ganancias por anunciante SIN FLITRO
    public List<ReporteAnuncioDTO> obtenerReporteAnunciosSinFiltro(ReporteAnuncioRequest reporteAnunciante) throws ErrorInesperadoException, FormatoInvalidoException {

        if (reporteAnunciante == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        ConvertirBase64Service convertirBase64Service = new ConvertirBase64Service();

        List<ReporteAnuncioDTO> listadoReportes = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(REPORTE_ANUNCIOS);) {

            query.setDate(1, java.sql.Date.valueOf(reporteAnunciante.getFechaInicio()));
            query.setDate(2, java.sql.Date.valueOf(reporteAnunciante.getFechaFin()));
            query.setInt(3, reporteAnunciante.getLimit());
            query.setInt(4, reporteAnunciante.getOffset());

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                ReporteAnuncioDTO reporteEncontrado = new ReporteAnuncioDTO(
                        resultSet.getString("codigo"),
                        resultSet.getBoolean("estado"),
                        resultSet.getString("nombre"),
                        resultSet.getBoolean("caducacion"),
                        resultSet.getDate("fecha_expiracion").toLocalDate(),
                        resultSet.getDate("fecha_compra").toLocalDate(),
                        resultSet.getString("url"),
                        resultSet.getString("texto"),
                        convertirBase64Service.convertirImagenAPngBase64(resultSet.getBytes("foto")),
                        resultSet.getInt("codigoTipo"),
                        resultSet.getString("idUsuario"),
                        resultSet.getString("nombreUsuario")
                );

                listadoReportes.add(reporteEncontrado);
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de reporte de los anuncios comprados en un intervalo sin filtro");
        }

        return listadoReportes;
    }

    //Metodo que sirve para poder consultar el reporte de anuncios comprados en un intervalo de tiempo CON FLITRO
    public List<ReporteAnuncioDTO> obtenerReporteAnunciosFiltro(ReporteAnuncioRequest reporteAnunciante) throws ErrorInesperadoException, FormatoInvalidoException {

        if (reporteAnunciante == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        ConvertirBase64Service convertirBase64Service = new ConvertirBase64Service();

        List<ReporteAnuncioDTO> listadoReportes = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(REPORTE_ANUNCIOS_FILTRO);) {

            query.setString(1, reporteAnunciante.getTipoAnuncio());
            query.setDate(2, java.sql.Date.valueOf(reporteAnunciante.getFechaInicio()));
            query.setDate(3, java.sql.Date.valueOf(reporteAnunciante.getFechaFin()));
            query.setInt(4, reporteAnunciante.getLimit());
            query.setInt(5, reporteAnunciante.getOffset());

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                ReporteAnuncioDTO reporteEncontrado = new ReporteAnuncioDTO(
                        resultSet.getString("codigo"),
                        resultSet.getBoolean("estado"),
                        resultSet.getString("nombre"),
                        resultSet.getBoolean("caducacion"),
                        resultSet.getDate("fecha_expiracion").toLocalDate(),
                        resultSet.getDate("fecha_compra").toLocalDate(),
                        resultSet.getString("url"),
                        resultSet.getString("texto"),
                        convertirBase64Service.convertirImagenAPngBase64(resultSet.getBytes("foto")),
                        resultSet.getInt("codigoTipo"),
                        resultSet.getString("idUsuario"),
                        resultSet.getString("nombreUsuario")
                );

                listadoReportes.add(reporteEncontrado);
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de reporte de anuncios comprados en un intervalo de tiempo ");
        }

        return listadoReportes;
    }

    //================================APARTADO DONDE SE MUESTRAN TODOS LOS REPORTES EN CUALQUIER INTERVALO============================
    //Metodo delegado para obtener la cantidad de reportes que se tienen EN TODO INTERVALO DE FECHAS
    public int cantidadReportesTodoSinFiltro(ReporteAnuncioRequest reporteAnunciante) throws ErrorInesperadoException, DatosNoEncontradosException {

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CANTIDAD_TODO_REPORTES);) {

            ResultSet result = query.executeQuery();
            if (result.next()) {
                return result.getInt("cantidad");
            } else {

                throw new DatosNoEncontradosException("No hay registros de reportes de anuncios comprados sin filtro ");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se ha podido conectar con la base de datos para obtener la cantidad de los anuncios comprados sin filtro");
        }
    }

    //Metodo delegado para obtener la cantidad de reportes que se tienen de ganancias por anunciante en todo intervalo de tiempo
    public int cantidadReportesTodoFiltro(ReporteAnuncioRequest reporteAnunciante) throws ErrorInesperadoException, DatosNoEncontradosException {

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CANTIDAD_REPORTES_TODO_FILTRO);) {

            query.setString(1, reporteAnunciante.getTipoAnuncio().trim());

            ResultSet result = query.executeQuery();
            if (result.next()) {
                return result.getInt("cantidad");
            } else {

                throw new DatosNoEncontradosException("No hay registros de reportes de anuncios comprados con filtro");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se ha podido conectar con la base de datos para obtener la cantidad de todos los anuncios comprados con filtro");
        }
    }

    //Metodo que sirve para poder consultar el reporte de todos los anuncios SIN FLITRO
    public List<ReporteAnuncioDTO> obtenerReporteTodoAnuncios(ReporteAnuncioRequest reporteRequest) throws ErrorInesperadoException, FormatoInvalidoException {

        if (reporteRequest == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        ConvertirBase64Service convertirBase64Service = new ConvertirBase64Service();

        List<ReporteAnuncioDTO> listadoReportes = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(REPORTE_TODO_ANUNCIOS);) {

            query.setInt(1, reporteRequest.getLimit());
            query.setInt(2, reporteRequest.getOffset());

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                ReporteAnuncioDTO reporteEncontrado = new ReporteAnuncioDTO(
                        resultSet.getString("codigo"),
                        resultSet.getBoolean("estado"),
                        resultSet.getString("nombre"),
                        resultSet.getBoolean("caducacion"),
                        resultSet.getDate("fecha_expiracion").toLocalDate(),
                        resultSet.getDate("fecha_compra").toLocalDate(),
                        resultSet.getString("url"),
                        resultSet.getString("texto"),
                        convertirBase64Service.convertirImagenAPngBase64(resultSet.getBytes("foto")),
                        resultSet.getInt("codigoTipo"),
                        resultSet.getString("idUsuario"),
                        resultSet.getString("nombreUsuario")
                );

                listadoReportes.add(reporteEncontrado);
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de reporte de anuncios comprados");
        }

        return listadoReportes;
    }

    //Metodo que sirve para poder consultar el reporte de todos los anuncios comprados CON FILTRO
    public List<ReporteAnuncioDTO> obtenerReporteTodoAnunciosFiltro(ReporteAnuncioRequest reporteRequest) throws ErrorInesperadoException, FormatoInvalidoException {

        if (reporteRequest == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        ConvertirBase64Service convertirBase64Service = new ConvertirBase64Service();

        List<ReporteAnuncioDTO> listadoReportes = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(REPORTE_ANUNCIOS_TODO_FILTRO);) {

            query.setString(1, reporteRequest.getTipoAnuncio());
            query.setInt(2, reporteRequest.getLimit());
            query.setInt(3, reporteRequest.getOffset());

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                ReporteAnuncioDTO reporteEncontrado = new ReporteAnuncioDTO(
                        resultSet.getString("codigo"),
                        resultSet.getBoolean("estado"),
                        resultSet.getString("nombre"),
                        resultSet.getBoolean("caducacion"),
                        resultSet.getDate("fecha_expiracion").toLocalDate(),
                        resultSet.getDate("fecha_compra").toLocalDate(),
                        resultSet.getString("url"),
                        resultSet.getString("texto"),
                        convertirBase64Service.convertirImagenAPngBase64(resultSet.getBytes("foto")),
                        resultSet.getInt("codigoTipo"),
                        resultSet.getString("idUsuario"),
                        resultSet.getString("nombreUsuario")
                );

                listadoReportes.add(reporteEncontrado);
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de reporte de los anuncios comprados ");
        }

        return listadoReportes;
    }

}
