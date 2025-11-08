/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.anuncios.database;

import com.pablocompany.rest.api.proyectono2ipc2.anuncios.dtos.AnuncioRegistradoDTOResponse;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.dtos.CantidadAnunciosClienteRequest;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.models.Anuncio;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.models.AnuncioRegistradoDTO;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.models.CambiarEstadoDTO;
import com.pablocompany.rest.api.proyectono2ipc2.cine.dtos.CantidadCargaRequest;
import com.pablocompany.rest.api.proyectono2ipc2.connectiondb.DBConnectionSingleton;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.pagoanuncio.database.PagoAnuncioDB;
import com.pablocompany.rest.api.proyectono2ipc2.pagoanuncio.models.PagoAnuncio;
import com.pablocompany.rest.api.proyectono2ipc2.reportesadminsistema.services.ConvertirBase64Service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author pablo
 */
//Clase delegada para poder operar con los recursos de los anuncios en la base de datos
public class AnunciosDB {

    //--------------------APARTADO DE ANUNCIOS POR ANUNCIANTE-----------------------------
    //Constante que permite crear un nuevo anuncio en el sistema 
    private final String COMPRAR_ANUNCIO = "INSERT INTO anuncio (codigo, estado, nombre, caducacion, fecha_expiracion, fecha_compra, url, texto, foto, codigo_tipo, id_usuario) VALUES(?,?,?,?,?,?,?,?,?,?,?)";

    //Constante que permite retornar los datos obtenidos de la base de datos para poder operar y gestionar a los anuncios
    private final String ANUNCIOS_COMPRADOS_ANUNCIANTE = "SELECT a.codigo, a.estado, a.nombre, a.caducacion, a.fecha_expiracion, a.fecha_compra, a.url, a.texto, a.foto,  ca.codigo AS `codigoTipo`, us.id AS `idUsuario`, us.nombre AS `nombreUsuario` "
            + "FROM anuncio AS a JOIN configuracion_anuncio AS ca ON a.codigo_tipo = ca.codigo JOIN usuario AS `us` ON us.id = a.id_usuario "
            + "WHERE us.id= ? ORDER BY a.fecha_compra ASC LIMIT ? OFFSET ?";

    //Constante que permite retornar la cantidad de anuncios que se han comprado POR ANUNCIANTE en la web para cargar dinamicamente
    private final String CANTIDAD_ANUNCIOS_COMPRADOS_ANUNCIANTE = "SELECT COUNT(*) AS `cantidad` "
            + "FROM anuncio AS a JOIN configuracion_anuncio AS ca ON a.codigo_tipo = ca.codigo JOIN usuario AS `us` ON us.id = a.id_usuario "
            + "WHERE us.id= ?";

    //--------------------FIN DEL APARTADO DE ANUNCIOS POR ANUNCIANTE-----------------------------
    //=====================APARTADO DE ANUNCIOS VISTOS POR LOS ADMINISTRADORES===============
    //Constante que permite retornar los datos obtenidos de la base de datos para poder operar y gestionar a los anuncios
    private final String ANUNCIOS_COMPRADOS_SISTEMA = "SELECT a.codigo, a.estado, a.nombre, a.caducacion, a.fecha_expiracion, a.fecha_compra, a.url, a.texto, a.foto,  ca.codigo AS `codigoTipo`, us.id AS `idUsuario`, us.nombre AS `nombreUsuario` "
            + "FROM anuncio AS a JOIN configuracion_anuncio AS ca ON a.codigo_tipo = ca.codigo JOIN usuario AS `us` ON us.id = a.id_usuario "
            + "ORDER BY a.fecha_compra DESC LIMIT ? OFFSET ?";

    //Constante que permite retornar la cantidad de anuncios que se han comprado en la web para cargar dinamicamente
    private final String CANTIDAD_ANUNCIOS_COMPRADOS = "SELECT COUNT(*) AS `cantidad` FROM anuncio";

    //Constante que permite cambiar el estado de los anuncios independientemente quien lo ejecute 
    private final String CAMBIAR_ESTADO_ANUNCIOS = "UPDATE anuncio SET estado = ? WHERE codigo = ?";

    //Metodo que sirve para poder cambiar el estado de un anuncio
    public boolean cambiarEstado(CambiarEstadoDTO cambioEstado) throws ErrorInesperadoException, FormatoInvalidoException, DatosNoEncontradosException {

        if (cambioEstado == null) {
            throw new FormatoInvalidoException("No se ha enviado ninguna informacion sobre el anuncio a editar");
        }

        Connection conexion = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement preparedStmt = conexion.prepareStatement(CAMBIAR_ESTADO_ANUNCIOS);) {

            conexion.setAutoCommit(false);

            preparedStmt.setBoolean(1, cambioEstado.isEstado());
            preparedStmt.setString(2, cambioEstado.getIdAnuncio());

            int filasAfectadas = preparedStmt.executeUpdate();

            if (filasAfectadas > 0) {

                conexion.commit();
                return true;

            } else {
                conexion.rollback();
                throw new DatosNoEncontradosException("No se ha podido cambiar el estado del anuncio.");
            }

        } catch (SQLException ex) {

            try {
                conexion.rollback();
            } catch (SQLException ex1) {
                throw new ErrorInesperadoException("Error al hacer Rollback al cambiar el estado del anuncio");
            }

            throw new ErrorInesperadoException("No se permiten inyecciones sql o patrones diferentes a los que se piden en el cambio de estado de anuncio");
        } finally {

            try {

                conexion.setAutoCommit(true);

            } catch (SQLException ex) {
                System.out.println("Error al reactivar la autoconfirmacion al cambiar el estado del anuncio");
            }
        }
    }

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

    //========APARATADO DE METODOS QUE SIRVEN PARA QUE EL USUARIO ANUNCIANTE PUEDA CONSULTAR SUS ANUNCIOS COMPRADOS=======
    //Metodo delegado para poder obtener la cantidad de anuncios comprados como usuario
    public int cantidadAnunciosCliente(String idUsuario) throws ErrorInesperadoException, DatosNoEncontradosException, FormatoInvalidoException {

        if (StringUtils.isBlank(idUsuario)) {
            throw new FormatoInvalidoException("El id del usuario esta vacio");
        }

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CANTIDAD_ANUNCIOS_COMPRADOS_ANUNCIANTE);) {

            query.setString(1, idUsuario.trim());
            ResultSet result = query.executeQuery();
            if (result.next()) {
                return result.getInt("cantidad");
            } else {

                throw new DatosNoEncontradosException("No hay registros de anuncios comprados por el usuario en la web");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se ha podido conectar con la base de datos para obtener la cantidad de anuncios comprados por el usuario");
        }
    }

    //Metodo que sirve para poder consultar el reporte de ganancias por anunciante SIN FLITRO
    public List<AnuncioRegistradoDTOResponse> anunciosRegistradosAnunciante(CantidadAnunciosClienteRequest cantidadCarga) throws ErrorInesperadoException, FormatoInvalidoException {

        if (cantidadCarga == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        ConvertirBase64Service convertirBase64Service = new ConvertirBase64Service();

        List<AnuncioRegistradoDTO> listadoAnuncios = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(ANUNCIOS_COMPRADOS_ANUNCIANTE);) {

            query.setString(1, cantidadCarga.getIdUsuario().trim());
            query.setInt(2, cantidadCarga.getLimit());
            query.setInt(3, cantidadCarga.getOffset());

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

    //Metodo delegado para poder reestablecer/cambiar la password del usuario
    public boolean comprarAnuncio(Anuncio anuncioNuevo, double cantidadPago) throws ErrorInesperadoException, FormatoInvalidoException {

        if (anuncioNuevo == null) {
            throw new FormatoInvalidoException("No se ha enviado ninguna informacion sobre la compra del anuncio");
        }

        PagoAnuncioDB pagoAnuncioDb = new PagoAnuncioDB();
        Connection conexion = DBConnectionSingleton.getInstance().getConnection();

        try{

            conexion.setAutoCommit(false);

            int filasAfectadas =insertarAnuncio(anuncioNuevo, conexion);
            int filasAfectadasPago =pagoAnuncioDb.generarPagoAnuncio(new PagoAnuncio(cantidadPago, anuncioNuevo.getFechaCompra(), anuncioNuevo.getIdUsuario()), conexion);

            if (filasAfectadas > 0 && filasAfectadasPago > 0) {
                conexion.commit();
                return true;

            } else {
                conexion.rollback();
                throw new ErrorInesperadoException("No se ha podido generar la transaccion para poder comprar el anuncio. ");
            }

        } catch (SQLException ex) {

            try {
                conexion.rollback();
            } catch (SQLException ex1) {
                throw new ErrorInesperadoException("Error al hacer Rollback al comprar el anuncio");
            }

            throw new ErrorInesperadoException("No se permiten inyecciones sql o partones diferentes a los que se piden al comprar un anuncio");
        } finally {

            try {

                conexion.setAutoCommit(true);

            } catch (SQLException ex) {
                throw new ErrorInesperadoException("Error al reactivar la autoconfirmacion al comprar anuncio");
            }
        }
    }

    private int insertarAnuncio(Anuncio anuncioNuevo, Connection conexion) throws ErrorInesperadoException {

        try (PreparedStatement preparedStmt = conexion.prepareStatement(COMPRAR_ANUNCIO);) {


            
            preparedStmt.setString(1,anuncioNuevo.getCodigo().trim());
            preparedStmt.setBoolean(2, anuncioNuevo.isEstado());
            preparedStmt.setString(3,anuncioNuevo.getNombre().trim());
            preparedStmt.setBoolean(4,anuncioNuevo.isCaducacion());
            preparedStmt.setDate(5, java.sql.Date.valueOf(anuncioNuevo.getFechaExpiracion()));
            preparedStmt.setDate(6, java.sql.Date.valueOf(anuncioNuevo.getFechaCompra()));
            preparedStmt.setString(7,anuncioNuevo.getUrl().trim());
            preparedStmt.setString(8,anuncioNuevo.getTexto().trim());
            preparedStmt.setBytes(9,anuncioNuevo.getFoto());
            preparedStmt.setInt(10, anuncioNuevo.getCodigoTipo());
            preparedStmt.setString(11, anuncioNuevo.getIdUsuario().trim());
            

            int filasAfectadas = preparedStmt.executeUpdate();

            return filasAfectadas;

        } catch (SQLException ex) {

            throw new ErrorInesperadoException("No se ha podido generar la transaccion para crear un nuevo usuario");
        }

    }

}
