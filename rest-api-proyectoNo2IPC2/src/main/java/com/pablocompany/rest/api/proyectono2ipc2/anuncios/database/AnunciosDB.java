/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.anuncios.database;

import com.pablocompany.rest.api.proyectono2ipc2.anuncios.dtos.AnuncioRegistradoDTOResponse;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.models.AnuncioRegistradoDTO;
import com.pablocompany.rest.api.proyectono2ipc2.cine.dtos.CantidadCargaRequest;
import com.pablocompany.rest.api.proyectono2ipc2.connectiondb.DBConnectionSingleton;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.dtos.ReporteAnuncioRequest;
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
//Clase delegada para poder operar con los recursos de los anuncios en la base de datos
public class AnunciosDB {

    //--------------------APARTADO DE ANUNCIOS POR ANUNCIANTE-----------------------------
    //Constante que permite retornar los datos obtenidos de la base de datos para poder operar y gestionar a los anuncios
    private final String ANUNCIOS_COMPRADOS_ANUNCIANTE = "SELECT a.codigo, a.estado, a.nombre, a.caducacion, a.fecha_expiracion, a.fecha_compra, a.url, a.texto, a.foto,  ca.codigo AS `codigoTipo`, us.id AS `idUsuario`, us.nombre AS `nombreUsuario` "
            + "FROM anuncio AS a JOIN configuracion_anuncio AS ca ON a.codigo_tipo = ca.codigo JOIN usuario AS `us` ON us.id = a.id_usuario "
            + "WHERE us.id= ? ORDER BY a.fecha_compra ASC LIMIT ? OFFSET ?";

    //Constante que permite retornar la cantidad de anuncios que se han comprado POR ANUNCIANTE en la web para cargar dinamicamente
    private final String CANTIDAD_ANUNCIOS_COMPRADOS_ANUNCIANTE = "SELECT COUNT(*) AS `cantidad` "
            + "FROM anuncio AS a JOIN configuracion_anuncio AS ca ON a.codigo_tipo = ca.codigo JOIN usuario AS `us` ON us.id = a.id_usuario "
            + "WHERE us.id= ? ORDER BY a.fecha_compra ASC LIMIT ? OFFSET ?";

    //--------------------FIN DEL APARTADO DE ANUNCIOS POR ANUNCIANTE-----------------------------
    //=====================APARTADO DE ANUNCIOS VISTOS POR LOS ADMINISTRADORES===============
    //Constante que permite retornar los datos obtenidos de la base de datos para poder operar y gestionar a los anuncios
    private final String ANUNCIOS_COMPRADOS_SISTEMA = "SELECT a.codigo, a.estado, a.nombre, a.caducacion, a.fecha_expiracion, a.fecha_compra, a.url, a.texto, a.foto,  ca.codigo AS `codigoTipo`, us.id AS `idUsuario`, us.nombre AS `nombreUsuario` "
            + "FROM anuncio AS a JOIN configuracion_anuncio AS ca ON a.codigo_tipo = ca.codigo JOIN usuario AS `us` ON us.id = a.id_usuario "
            + "ORDER BY a.fecha_compra DESC LIMIT ? OFFSET ?";

    //Constante que permite retornar la cantidad de anuncios que se han comprado en la web para cargar dinamicamente
    private final String CANTIDAD_ANUNCIOS_COMPRADOS = "SELECT COUNT(*) AS `cantidad` FROM anuncio";

    //Metodo delegado para obtener la cantidad de reportes que se tienen en el intervalo de fechas con filtro
    public int cantidadAnunciosSistema() throws ErrorInesperadoException, DatosNoEncontradosException {

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CANTIDAD_ANUNCIOS_COMPRADOS);) {

            ResultSet result = query.executeQuery();
            if (result.next()) {
                return result.getInt("cantidad");
            } else {

                throw new DatosNoEncontradosException("No hay registros de anuncios comprados en la web");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se ha podido conectar con la base de datos para obtener la cantidad de anuncios comprados en la web");
        }
    }

    //Metodo que sirve para poder consultar el reporte de ganancias por anunciante SIN FLITRO
    public List<AnuncioRegistradoDTOResponse> anunciosRegistradosSistema(CantidadCargaRequest cantidadCarga) throws ErrorInesperadoException, FormatoInvalidoException {

        if (cantidadCarga == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        ConvertirBase64Service convertirBase64Service = new ConvertirBase64Service();

        List<AnuncioRegistradoDTO> listadoAnuncios = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(ANUNCIOS_COMPRADOS_SISTEMA);) {

            query.setInt(1, cantidadCarga.getLimit());
            query.setInt(2, cantidadCarga.getOffset());

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                AnuncioRegistradoDTO anuncioEncontrado = new AnuncioRegistradoDTO(
                        resultSet.getString("codigo"),
                        resultSet.getBoolean("estado"),
                        resultSet.getString("nombre"),
                        resultSet.getBoolean("caducacion"),
                        resultSet.getDate("fecha_expiracion").toLocalDate(),
                        resultSet.getDate("fecha_compra").toLocalDate(),
                        resultSet.getString("url"),
                        resultSet.getString("texto"),
                        convertirBase64Service.convertirImagenAPngBase64(resultSet.getBytes("foto"), resultSet.getInt("codigoTipo")),
                        resultSet.getInt("codigoTipo"),
                        resultSet.getString("idUsuario"),
                        resultSet.getString("nombreUsuario")
                );

                listadoAnuncios.add(anuncioEncontrado);
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de los anuncios comprados en el sistema");
        }

        List<AnuncioRegistradoDTOResponse> listadoResponse = new ArrayList<>();

        for (AnuncioRegistradoDTO anuncioRegistradoDTO : listadoAnuncios) {

            listadoResponse.add(new AnuncioRegistradoDTOResponse(anuncioRegistradoDTO));
        }

        return listadoResponse;
    }

}
