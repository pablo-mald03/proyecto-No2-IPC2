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
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models.PeliculaProyectadaDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadmincine.models.ReporteSalaPeliculaProyectadaDTO;
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
//Clase delegada para poder operar con las peliculas proyectadas en las salas de cine 
public class ReportePeliculasDB {

    //Constante que permite obtener el reporte de peliculas proyectadas sin filtro de sala
    private final String REPORTE_PELICULAS = "SELECT DISTINCT sa.codigo, ci.nombre AS `cineAsociado`, sa.nombre, sa.filas, sa.columnas, sa.ubicacion FROM sala AS `sa` JOIN cine AS `ci` ON sa.codigo_cine = ci.codigo JOIN peliculas_sala AS `ps` ON sa.codigo = ps.codigo_sala JOIN  pelicula AS `pe` ON pe.codigo = ps.codigo_pelicula WHERE ps.fecha_proyeccion BETWEEN ? AND ? LIMIT ? OFFSET ?";

    //Constante que permite obtener la cantidad de salas del reporte de peliculas proyectadas sin filtro de sala
    private final String CANTIDAD_REPORTES = "SELECT DISTINCT COUNT(DISTINCT sa.codigo) AS `cantidad` FROM sala AS `sa` JOIN cine AS `ci` ON sa.codigo_cine = ci.codigo JOIN peliculas_sala AS `ps` ON sa.codigo = ps.codigo_sala JOIN  pelicula AS `pe` ON pe.codigo = ps.codigo_pelicula WHERE ps.fecha_proyeccion BETWEEN ? AND ?";

    //Constante que permite obtener la cantidad de salas del reporte de peliculas proyectadas con filtro de sala
    private final String CANTIDAD_REPORTES_FILTRO = "SELECT DISTINCT COUNT(DISTINCT sa.codigo) AS `cantidad` FROM sala AS `sa` JOIN cine AS `ci` ON sa.codigo_cine = ci.codigo JOIN peliculas_sala AS `ps` ON sa.codigo = ps.codigo_sala JOIN  pelicula AS `pe` ON pe.codigo = ps.codigo_pelicula WHERE sa.codigo = ? AND ps.fecha_proyeccion BETWEEN ? AND ? ";

    //Constante que permite obtener el reporte de peliculas proyectadas con filtro de sala
    private final String REPORTE_PELICULAS_FILTRO = "SELECT DISTINCT sa.codigo, ci.nombre AS `cineAsociado`, sa.nombre, sa.filas, sa.columnas, sa.ubicacion FROM sala AS `sa` JOIN cine AS `ci` ON sa.codigo_cine = ci.codigo JOIN peliculas_sala AS `ps` ON sa.codigo = ps.codigo_sala JOIN  pelicula AS `pe` ON pe.codigo = ps.codigo_pelicula WHERE sa.codigo = ? AND ps.fecha_proyeccion BETWEEN ? AND ?  LIMIT ? OFFSET ?";

    //Constante que permite saber las peliculas que son pertenecientes a la sala
    private final String PELICULAS_SALAS = "SELECT pe.fecha_estreno, pe.nombre, pe.sinopsis, pe.clasificacion, pe.duracion FROM sala AS `sa` JOIN cine AS `ci` ON sa.codigo_cine = ci.codigo JOIN peliculas_sala AS `ps` ON sa.codigo = ps.codigo_sala JOIN  pelicula AS `pe` ON pe.codigo = ps.codigo_pelicula WHERE sa.codigo = ? AND ps.fecha_proyeccion BETWEEN ? AND ? ORDER BY pe.fecha_estreno DESC";

    //Constante que permite obtener el reporte de peliculas proyectadas sin filtro de sala EN TODO INTERVALO DE TIEMPO
    private final String REPORTE_TODO_PELICULAS = "SELECT DISTINCT sa.codigo, ci.nombre AS `cineAsociado`, sa.nombre, sa.filas, sa.columnas, sa.ubicacion FROM sala AS `sa` JOIN cine AS `ci` ON sa.codigo_cine = ci.codigo JOIN peliculas_sala AS `ps` ON sa.codigo = ps.codigo_sala JOIN  pelicula AS `pe` ON pe.codigo = ps.codigo_pelicula LIMIT ? OFFSET ?";

    //Constante que permite obtener la cantidad de salas del reporte de peliculas proyectadas sin filtro de sala EN TODO INTERVALO DE TIEMPO
    private final String CANTIDAD_TODO_REPORTES = "SELECT DISTINCT COUNT(DISTINCT sa.codigo) AS `cantidad` FROM sala AS `sa` JOIN cine AS `ci` ON sa.codigo_cine = ci.codigo JOIN peliculas_sala AS `ps` ON sa.codigo = ps.codigo_sala JOIN  pelicula AS `pe` ON pe.codigo = ps.codigo_pelicula";

    //Constante que permite obtener la cantidad de salas del reporte de peliculas proyectadas con filtro de sala EN TODO INTERVALO DE TIEMPO
    private final String CANTIDAD_REPORTES_TODO_FILTRO = "SELECT DISTINCT COUNT(DISTINCT sa.codigo) AS `cantidad` FROM sala AS `sa` JOIN cine AS `ci` ON sa.codigo_cine = ci.codigo JOIN peliculas_sala AS `ps` ON sa.codigo = ps.codigo_sala JOIN  pelicula AS `pe` ON pe.codigo = ps.codigo_pelicula WHERE sa.codigo = ?";

    //Constante que permite obtener el reporte de peliculas proyectadas con filtro de sala EN TODO INTERVALO DE TIEMPO
    private final String REPORTE_PELICULAS_TODO_FILTRO = "SELECT DISTINCT sa.codigo, ci.nombre AS `cineAsociado`, sa.nombre, sa.filas, sa.columnas, sa.ubicacion FROM sala AS `sa` JOIN cine AS `ci` ON sa.codigo_cine = ci.codigo JOIN peliculas_sala AS `ps` ON sa.codigo = ps.codigo_sala JOIN  pelicula AS `pe` ON pe.codigo = ps.codigo_pelicula WHERE sa.codigo = ?  LIMIT ? OFFSET ?";

    //Constante que permite saber las peliculas que son pertenecientes a la sala EN TODO INTERVALO DE TIEMPO
    private final String PELICULAS_TODO_SALAS = "SELECT pe.fecha_estreno, pe.nombre, pe.sinopsis, pe.clasificacion, pe.duracion FROM sala AS `sa` JOIN cine AS `ci` ON sa.codigo_cine = ci.codigo JOIN peliculas_sala AS `ps` ON sa.codigo = ps.codigo_sala JOIN  pelicula AS `pe` ON pe.codigo = ps.codigo_pelicula WHERE sa.codigo = ? ORDER BY pe.fecha_estreno DESC";

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

                throw new DatosNoEncontradosException("No hay registros de reportes de peliculas de salas de cine");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se ha podido conectar con la base de datos para obtener la cantidad de reportes de peliculas en salas de cine");
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

                throw new DatosNoEncontradosException("No hay registros de reportes de peliculas de salas de cine");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se ha podido conectar con la base de datos para obtener la cantidad de reportes de peliculas en salas de cine");
        }
    }

    //Metodo que sirve para poder consultar el reporte de comentarios SIN FLITRO
    public List<ReporteSalaPeliculaProyectadaDTO> obtenerReportePeliculas(ReporteRequest reporteSalas) throws ErrorInesperadoException, FormatoInvalidoException {

        if (reporteSalas == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        List<ReporteSalaPeliculaProyectadaDTO> listadoReportes = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(REPORTE_PELICULAS);) {

            query.setDate(1, java.sql.Date.valueOf(reporteSalas.getFechaInicio()));
            query.setDate(2, java.sql.Date.valueOf(reporteSalas.getFechaFin()));
            query.setInt(3, reporteSalas.getLimit());
            query.setInt(4, reporteSalas.getOffset());

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                ReporteSalaPeliculaProyectadaDTO salaEncontrada = new ReporteSalaPeliculaProyectadaDTO(
                        resultSet.getString("codigo"),
                        resultSet.getString("cineAsociado"),
                        resultSet.getString("nombre"),
                        resultSet.getInt("filas"),
                        resultSet.getInt("columnas"),
                        resultSet.getString("ubicacion")
                );

                listadoReportes.add(salaEncontrada);
            }

            for (ReporteSalaPeliculaProyectadaDTO listadoReporte : listadoReportes) {
                listadoReporte.setPeliculaProyectada(obtenerPeliculas(reporteSalas, listadoReporte.getCodigo()));
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de reporte de peliculas en salas de cine" + e.getMessage());
        }

        return listadoReportes;
    }

    //Metodo que sirve para poder consultar el reporte de peliculas en salas de cine CON FLITRO
    public List<ReporteSalaPeliculaProyectadaDTO> obtenerReportePeliculasFiltro(ReporteRequest reporteSalas) throws ErrorInesperadoException, FormatoInvalidoException {

        if (reporteSalas == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        List<ReporteSalaPeliculaProyectadaDTO> listadoReportes = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(REPORTE_PELICULAS_FILTRO);) {

            query.setString(1, reporteSalas.getIdSala());
            query.setDate(2, java.sql.Date.valueOf(reporteSalas.getFechaInicio()));
            query.setDate(3, java.sql.Date.valueOf(reporteSalas.getFechaFin()));
            query.setInt(4, reporteSalas.getLimit());
            query.setInt(5, reporteSalas.getOffset());

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                ReporteSalaPeliculaProyectadaDTO salaEncontrada = new ReporteSalaPeliculaProyectadaDTO(
                        resultSet.getString("codigo"),
                        resultSet.getString("cineAsociado"),
                        resultSet.getString("nombre"),
                        resultSet.getInt("filas"),
                        resultSet.getInt("columnas"),
                        resultSet.getString("ubicacion")
                );

                listadoReportes.add(salaEncontrada);
            }

            for (ReporteSalaPeliculaProyectadaDTO listadoReporte : listadoReportes) {
                listadoReporte.setPeliculaProyectada(obtenerPeliculas(reporteSalas, listadoReporte.getCodigo()));
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de reporte de peliculas en salas de cine" + e.getMessage());
        }

        return listadoReportes;
    }

    //Metodo que sirve para obtener las peliculas relacionados a una sala
    private List< PeliculaProyectadaDTO> obtenerPeliculas(ReporteRequest reporteSalas, String idSala) throws FormatoInvalidoException, ErrorInesperadoException {

        if (reporteSalas == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        List<PeliculaProyectadaDTO> listadoPeliculas = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(PELICULAS_SALAS);) {

            query.setString(1, idSala);
            query.setDate(2, java.sql.Date.valueOf(reporteSalas.getFechaInicio()));
            query.setDate(3, java.sql.Date.valueOf(reporteSalas.getFechaFin()));

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                PeliculaProyectadaDTO peliculaEncontrada = new PeliculaProyectadaDTO(
                        resultSet.getDate("fecha_estreno").toLocalDate(),
                        resultSet.getString("nombre"),
                        resultSet.getString("sinopsis"),
                        resultSet.getString("clasificacion"),
                        resultSet.getBigDecimal("duracion").doubleValue()
                );

                listadoPeliculas.add(peliculaEncontrada);
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de las peliculas del reporte de salas de cine");
        }

        return listadoPeliculas;

    }
    //Metodo delegado para obtener la cantidad de reportes que se tienen EN TODO INTERVALO DE TIEMPO
    public int cantidadTodoReportesSinFiltro(ReporteRequest reporteSalas) throws ErrorInesperadoException, DatosNoEncontradosException {

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CANTIDAD_TODO_REPORTES);) {

            ResultSet result = query.executeQuery();
            if (result.next()) {
                return result.getInt("cantidad");
            } else {

                throw new DatosNoEncontradosException("No hay registros de reportes de peliculas de salas de cine");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se ha podido conectar con la base de datos para obtener la cantidad de reportes de peliculas en salas de cine");
        }
    }

    //Metodo delegado para obtener la cantidad de reportes con filtro EN TODO INTERVALO DE TIEMPO
    public int cantidadTodoReportesFiltro(ReporteRequest reporteSalas) throws ErrorInesperadoException, DatosNoEncontradosException {

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CANTIDAD_REPORTES_TODO_FILTRO);) {

            query.setString(1, reporteSalas.getIdSala());
            
            ResultSet result = query.executeQuery();
            if (result.next()) {
                return result.getInt("cantidad");
            } else {

                throw new DatosNoEncontradosException("No hay registros de reportes de peliculas de salas de cine");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se ha podido conectar con la base de datos para obtener la cantidad de reportes de peliculas en salas de cine");
        }
    }

    //Metodo que sirve para poder consultar el reporte de comentarios SIN FLITRO EN TODO INTERVALO DE TIEMPO
    public List<ReporteSalaPeliculaProyectadaDTO> obtenerTodoReportePeliculas(ReporteRequest reporteSalas) throws ErrorInesperadoException, FormatoInvalidoException {

        if (reporteSalas == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        List<ReporteSalaPeliculaProyectadaDTO> listadoReportes = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(REPORTE_TODO_PELICULAS);) {


            query.setInt(1, reporteSalas.getLimit());
            query.setInt(2, reporteSalas.getOffset());

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                ReporteSalaPeliculaProyectadaDTO salaEncontrada = new ReporteSalaPeliculaProyectadaDTO(
                        resultSet.getString("codigo"),
                        resultSet.getString("cineAsociado"),
                        resultSet.getString("nombre"),
                        resultSet.getInt("filas"),
                        resultSet.getInt("columnas"),
                        resultSet.getString("ubicacion")
                );

                listadoReportes.add(salaEncontrada);
            }

            for (ReporteSalaPeliculaProyectadaDTO listadoReporte : listadoReportes) {
                listadoReporte.setPeliculaProyectada(obtenerTodoPeliculas(reporteSalas, listadoReporte.getCodigo()));
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de reporte de peliculas en salas de cine" + e.getMessage());
        }

        return listadoReportes;
    }

    //Metodo que sirve para poder consultar el reporte de peliculas en salas de cine CON FLITRO EN TODO INTERVALO DE TIEMPO
    public List<ReporteSalaPeliculaProyectadaDTO> obtenerReporteTodoPeliculasFiltro(ReporteRequest reporteSalas) throws ErrorInesperadoException, FormatoInvalidoException {

        if (reporteSalas == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        List<ReporteSalaPeliculaProyectadaDTO> listadoReportes = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(REPORTE_PELICULAS_TODO_FILTRO);) {

            query.setString(1, reporteSalas.getIdSala());
            query.setInt(2, reporteSalas.getLimit());
            query.setInt(3, reporteSalas.getOffset());

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                ReporteSalaPeliculaProyectadaDTO salaEncontrada = new ReporteSalaPeliculaProyectadaDTO(
                        resultSet.getString("codigo"),
                        resultSet.getString("cineAsociado"),
                        resultSet.getString("nombre"),
                        resultSet.getInt("filas"),
                        resultSet.getInt("columnas"),
                        resultSet.getString("ubicacion")
                );

                listadoReportes.add(salaEncontrada);
            }

            for (ReporteSalaPeliculaProyectadaDTO listadoReporte : listadoReportes) {
                listadoReporte.setPeliculaProyectada(obtenerTodoPeliculas(reporteSalas, listadoReporte.getCodigo()));
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de reporte de peliculas en salas de cine" + e.getMessage());
        }

        return listadoReportes;
    }

    //Metodo que sirve para obtener las peliculas relacionados a una sala
    private List< PeliculaProyectadaDTO> obtenerTodoPeliculas(ReporteRequest reporteSalas, String idSala) throws FormatoInvalidoException, ErrorInesperadoException {

        if (reporteSalas == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        List<PeliculaProyectadaDTO> listadoPeliculas = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(PELICULAS_TODO_SALAS);) {

            query.setString(1, idSala);
            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                PeliculaProyectadaDTO peliculaEncontrada = new PeliculaProyectadaDTO(
                        resultSet.getDate("fecha_estreno").toLocalDate(),
                        resultSet.getString("nombre"),
                        resultSet.getString("sinopsis"),
                        resultSet.getString("clasificacion"),
                        resultSet.getBigDecimal("duracion").doubleValue()
                );

                listadoPeliculas.add(peliculaEncontrada);
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de las peliculas del reporte de salas de cine");
        }

        return listadoPeliculas;

    }

}
