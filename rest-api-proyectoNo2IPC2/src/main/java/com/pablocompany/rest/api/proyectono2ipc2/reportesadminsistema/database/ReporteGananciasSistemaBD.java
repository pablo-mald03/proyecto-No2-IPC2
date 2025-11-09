/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.database;

import com.pablocompany.rest.api.proyectono2ipc2.connectiondb.DBConnectionSingleton;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.dtos.ReporteAnuncioRequest;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.dtos.ReporteSistemaRequest;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models.AnunciosCompradosDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models.CineCostoDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models.CostoModificacionCineDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models.GananciasSistemaDTO;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.models.PagoCineAnuncioDTO;
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
//Clase delegada para poder operar con las consultas para poder generar los reportes de ganancias
public class ReporteGananciasSistemaBD {

    //=====Apartado de las constantes que sirven para poder obtener todos los subreportes en un intervalo de tiempo======
    //Constante que permite obtener los cines dentro de esas fechas 
    private final String COSTOS_CINE = "SELECT c.codigo, c.nombre, c.monto_ocultacion, c.fecha_creacion FROM cine AS `c` JOIN costo_cine AS `cc` ON c.codigo = cc.codigo_cine WHERE cc.fecha_modificacion BETWEEN ? AND ? GROUP BY cc.codigo_cine";

    //Constante que permite obtener la cantidad total de modificacion de cine
    private final String MODIFICACIONES_CINE = "SELECT costo, fecha_modificacion FROM costo_cine  WHERE codigo_cine = ? AND fecha_modificacion BETWEEN ? AND ?";

    //=====Apartado de las constantes que sirven para poder obtener todos los subreportes en un intervalo de tiempo======
    //Constante que permite saber los anuncios comprados en un intervalo de tiempo
    private final String ANUNCIOS_COMPRADOS = " SELECT  a.codigo,  a.nombre,  a.fecha_compra,  pa.monto,  pa.id_usuario FROM anuncio AS a JOIN ( SELECT id_usuario, MAX(fecha_pago) AS ultima_fecha,  MAX(monto) AS monto FROM pago_anuncio WHERE fecha_pago BETWEEN ? AND ? GROUP BY id_usuario ) pa ON a.id_usuario = pa.id_usuario WHERE a.fecha_compra <= pa.ultima_fecha";

    //constante que permite saber el pago de ocultacion de anuncios en un invervalo de tiempo
    private final String PAGO_OCULTACION_ANUNCIOS = "SELECT codigo_cine, monto, fecha_pago FROM pago_ocultacion_cine WHERE fecha_pago BETWEEN ? AND ?;";

    //COnstante que permite obtener los reportes en todo intervalo de tiempo 
    private final String COSTOS_TODO_CINE = "SELECT c.codigo, c.nombre, c.monto_ocultacion, c.fecha_creacion FROM cine AS `c` JOIN costo_cine AS `cc` ON c.codigo = cc.codigo_cine GROUP BY cc.codigo_cine";

    //Constante que permite obtener la cantidad total de modificacion de cine
    private final String MODIFICACIONES_TODO_CINE = "SELECT costo, fecha_modificacion FROM costo_cine  WHERE codigo_cine = ?";

    //=====Apartado de las constantes que sirven para poder obtener todos los subreportes en un intervalo de tiempo======
    //Constante que permite saber los anuncios comprados en un intervalo de tiempo
    private final String ANUNCIOS_TODO_COMPRADOS = " SELECT  a.codigo,  a.nombre,  a.fecha_compra,  pa.monto,  pa.id_usuario FROM anuncio AS a JOIN ( SELECT id_usuario, MAX(fecha_pago) AS ultima_fecha,  MAX(monto) AS monto FROM pago_anuncio GROUP BY id_usuario ) pa ON a.id_usuario = pa.id_usuario WHERE a.fecha_compra <= pa.ultima_fecha";

    //constante que permite saber el pago de ocultacion de anuncios en un invervalo de tiempo
    private final String PAGO_OCULTACION_TODO_ANUNCIOS = "SELECT codigo_cine, monto, fecha_pago FROM pago_ocultacion_cine";

    //Metodo que permite retornar el reporte de ganancias en un intervalo de tiempo
    public GananciasSistemaDTO obtenerReporteGanancias(ReporteSistemaRequest reporteRequest) throws ErrorInesperadoException, FormatoInvalidoException, FormatoInvalidoException {

        GananciasSistemaDTO gananciasSistemaDTO = new GananciasSistemaDTO();

        gananciasSistemaDTO.setCostosCine(obtenerCostosCine(reporteRequest));
        gananciasSistemaDTO.setAnunciosComprados(listadoAnunciosCompradosFecha(reporteRequest));
        gananciasSistemaDTO.setPagoBloqueoAnuncios(listadoBloqueoAnunciosFecha(reporteRequest));

        gananciasSistemaDTO.setTotalCostoCine(calcularCostosCine(gananciasSistemaDTO.getCostosCine()));
        gananciasSistemaDTO.setTotalIngresos(calcularIngresos(gananciasSistemaDTO.getAnunciosComprados()));

        double totalGanancia = calcularPagoBloqueos(gananciasSistemaDTO.getPagoBloqueoAnuncios()) + gananciasSistemaDTO.getTotalIngresos();

        gananciasSistemaDTO.setTotalGanancia(totalGanancia);

        return gananciasSistemaDTO;
    }

    //Metodo que ayuda a calcular el total de costos de cine 
    private double calcularCostosCine(List<CineCostoDTO> listaCostosCine) {

        double totalCosto = 0;

        for (CineCostoDTO cineCosto : listaCostosCine) {

            for (CostoModificacionCineDTO costoModifiacion : cineCosto.getCostosAsociados()) {

                totalCosto += costoModifiacion.getCosto();

            }

        }

        return totalCosto;
    }

    //Metodo que ayuda a calcular el total de ingresos
    private double calcularIngresos(List<AnunciosCompradosDTO> listadoAnunciosComprados) {

        double totalIngresos = 0;

        for (AnunciosCompradosDTO anunciosComprados : listadoAnunciosComprados) {

            totalIngresos += anunciosComprados.getMonto();

        }

        return totalIngresos;
    }

    //Metodo que ayuda a calcular el total de ganancia
    private double calcularPagoBloqueos(List<PagoCineAnuncioDTO> pagosBloqueo) {

        double totalBloqueos = 0;

        for (PagoCineAnuncioDTO pago : pagosBloqueo) {

            totalBloqueos += pago.getMonto();

        }

        return totalBloqueos;
    }

    //Submetodo que sirve para obtener los costos de cine en un intervalo de tiempo 
    public List<CineCostoDTO> obtenerCostosCine(ReporteSistemaRequest reporteRequest) throws ErrorInesperadoException, FormatoInvalidoException {

        if (reporteRequest == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        List<CineCostoDTO> listadoCostos = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(COSTOS_CINE);) {

            query.setDate(1, java.sql.Date.valueOf(reporteRequest.getFechaInicio()));
            query.setDate(2, java.sql.Date.valueOf(reporteRequest.getFechaFin()));

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                CineCostoDTO cineCostoEncontrado = new CineCostoDTO(
                        resultSet.getString("codigo"),
                        resultSet.getString("nombre"),
                        resultSet.getBigDecimal("monto_ocultacion").doubleValue(),
                        resultSet.getDate("fecha_creacion").toLocalDate()
                );

                listadoCostos.add(cineCostoEncontrado);
            }

            for (CineCostoDTO costo : listadoCostos) {

                costo.setCostosAsociados(costoModificacionPorSalaFecha(reporteRequest, costo.getCodigo()));

            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de reporte de los anuncios comprados en un intervalo sin filtro");
        }

        return listadoCostos;
    }

    //Metodo que sirve para obtener el sublistado de costos que han tenido los cines en un intervalo de tiempo 
    private List<CostoModificacionCineDTO> costoModificacionPorSalaFecha(ReporteSistemaRequest reporteRequest, String idCine) throws ErrorInesperadoException, FormatoInvalidoException {

        if (reporteRequest == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        List<CostoModificacionCineDTO> listadoCostos = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(MODIFICACIONES_CINE);) {

            query.setString(1, idCine.trim());
            query.setDate(2, java.sql.Date.valueOf(reporteRequest.getFechaInicio()));
            query.setDate(3, java.sql.Date.valueOf(reporteRequest.getFechaFin()));

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                CostoModificacionCineDTO cineCostoEncontrado = new CostoModificacionCineDTO(
                        resultSet.getBigDecimal("costo").doubleValue(),
                        resultSet.getDate("fecha_modificacion").toLocalDate()
                );

                listadoCostos.add(cineCostoEncontrado);
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos del sublistado de costos de modificaciones de cine");
        }

        return listadoCostos;
    }

    //Metodo que ayuda a obtener el sublistado de ganancias  de anuncios comprados en un intervalod intervalo de tiempo
    private List<AnunciosCompradosDTO> listadoAnunciosCompradosFecha(ReporteSistemaRequest request) throws FormatoInvalidoException, ErrorInesperadoException {

        if (request == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        List<AnunciosCompradosDTO> listadoComprados = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(ANUNCIOS_COMPRADOS);) {

            query.setDate(1, java.sql.Date.valueOf(request.getFechaInicio()));
            query.setDate(2, java.sql.Date.valueOf(request.getFechaFin()));

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                AnunciosCompradosDTO anuncioComprado = new AnunciosCompradosDTO(
                        resultSet.getString("codigo"),
                        resultSet.getString("nombre"),
                        resultSet.getDate("fecha_compra").toLocalDate(),
                        resultSet.getBigDecimal("monto").doubleValue(),
                        resultSet.getString("id_usuario")
                );

                listadoComprados.add(anuncioComprado);
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos del sublistado de los anuncios comprados");
        }

        return listadoComprados;

    }

    //Metodo que ayuda a obtener el listado de pagos de bloqueos en un intervalo de tiempo
    private List<PagoCineAnuncioDTO> listadoBloqueoAnunciosFecha(ReporteSistemaRequest request) throws FormatoInvalidoException, ErrorInesperadoException {

        if (request == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        List<PagoCineAnuncioDTO> listadoPagos = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(PAGO_OCULTACION_ANUNCIOS);) {

            query.setDate(1, java.sql.Date.valueOf(request.getFechaInicio()));
            query.setDate(2, java.sql.Date.valueOf(request.getFechaFin()));

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                PagoCineAnuncioDTO pagoEncontrado = new PagoCineAnuncioDTO(
                        resultSet.getString("codigo_cine"),
                        resultSet.getBigDecimal("monto").doubleValue(),
                        resultSet.getDate("fecha_pago").toLocalDate()
                );

                listadoPagos.add(pagoEncontrado);
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos del sublistado de los anuncios comprados");
        }

        return listadoPagos;

    }

    //Metodo que permite retornar el reporte de ganancias en todo intervalo de tiempo
    public GananciasSistemaDTO obtenerReporteTodoGanancias() throws ErrorInesperadoException, FormatoInvalidoException {

        GananciasSistemaDTO gananciasSistemaDTO = new GananciasSistemaDTO();

        gananciasSistemaDTO.setCostosCine(obtenerTodoCostosCine());
        gananciasSistemaDTO.setAnunciosComprados(listadoAnunciosTodoCompradosFecha());
        gananciasSistemaDTO.setPagoBloqueoAnuncios(listadoBloqueoAnunciosTodoFecha());

        gananciasSistemaDTO.setTotalCostoCine(calcularCostosCine(gananciasSistemaDTO.getCostosCine()));
        gananciasSistemaDTO.setTotalIngresos(calcularIngresos(gananciasSistemaDTO.getAnunciosComprados()));

        double totalGanancia = calcularPagoBloqueos(gananciasSistemaDTO.getPagoBloqueoAnuncios()) + gananciasSistemaDTO.getTotalIngresos();

        gananciasSistemaDTO.setTotalGanancia(totalGanancia);

        return gananciasSistemaDTO;
    }

    //Submetodo que sirve para obtener los costos de cine en todo intervalo de tiempo 
    public List<CineCostoDTO> obtenerTodoCostosCine() throws ErrorInesperadoException, FormatoInvalidoException {

        List<CineCostoDTO> listadoCostos = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(COSTOS_TODO_CINE);) {

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                CineCostoDTO cineCostoEncontrado = new CineCostoDTO(
                        resultSet.getString("codigo"),
                        resultSet.getString("nombre"),
                        resultSet.getBigDecimal("monto_ocultacion").doubleValue(),
                        resultSet.getDate("fecha_creacion").toLocalDate()
                );

                listadoCostos.add(cineCostoEncontrado);
            }

            for (CineCostoDTO costo : listadoCostos) {

                costo.setCostosAsociados(costoModificacionTodoPorSalaFecha(costo.getCodigo()));

            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de reporte de los anuncios comprados en un intervalo sin filtro");
        }

        return listadoCostos;
    }

    //Metodo que sirve para obtener el sublistado de costos que han tenido los cines en todo intervalo de tiempo 
    private List<CostoModificacionCineDTO> costoModificacionTodoPorSalaFecha(String idCine) throws ErrorInesperadoException, FormatoInvalidoException {

        List<CostoModificacionCineDTO> listadoCostos = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(MODIFICACIONES_TODO_CINE);) {

            query.setString(1, idCine.trim());

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                CostoModificacionCineDTO cineCostoEncontrado = new CostoModificacionCineDTO(
                        resultSet.getBigDecimal("costo").doubleValue(),
                        resultSet.getDate("fecha_modificacion").toLocalDate()
                );

                listadoCostos.add(cineCostoEncontrado);
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos del sublistado de costos de modificaciones de cine");
        }

        return listadoCostos;
    }

    //Metodo que ayuda a obtener el sublistado de ganancias  de anuncios comprados en todo intervalo de tiempo
    private List<AnunciosCompradosDTO> listadoAnunciosTodoCompradosFecha() throws FormatoInvalidoException, ErrorInesperadoException {

        List<AnunciosCompradosDTO> listadoComprados = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(ANUNCIOS_TODO_COMPRADOS);) {

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                AnunciosCompradosDTO anuncioComprado = new AnunciosCompradosDTO(
                        resultSet.getString("codigo"),
                        resultSet.getString("nombre"),
                        resultSet.getDate("fecha_compra").toLocalDate(),
                        resultSet.getBigDecimal("monto").doubleValue(),
                        resultSet.getString("id_usuario")
                );

                listadoComprados.add(anuncioComprado);
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos del sublistado de los anuncios comprados");
        }

        return listadoComprados;

    }

    //Metodo que ayuda a obtener el listado de pagos de bloqueos en todo intervalo de tiempo
    private List<PagoCineAnuncioDTO> listadoBloqueoAnunciosTodoFecha() throws FormatoInvalidoException, ErrorInesperadoException {

        List<PagoCineAnuncioDTO> listadoPagos = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(PAGO_OCULTACION_TODO_ANUNCIOS);) {

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                PagoCineAnuncioDTO pagoEncontrado = new PagoCineAnuncioDTO(
                        resultSet.getString("codigo_cine"),
                        resultSet.getBigDecimal("monto").doubleValue(),
                        resultSet.getDate("fecha_pago").toLocalDate()
                );

                listadoPagos.add(pagoEncontrado);
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos del sublistado de los anuncios comprados");
        }

        return listadoPagos;

    }

}
