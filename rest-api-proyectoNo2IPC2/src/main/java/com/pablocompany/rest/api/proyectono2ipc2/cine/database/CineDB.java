/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.cine.database;

import com.pablocompany.rest.api.proyectono2ipc2.billeteracine.database.BilleteraCineDB;
import com.pablocompany.rest.api.proyectono2ipc2.billeteracine.models.BilleteraCine;
import com.pablocompany.rest.api.proyectono2ipc2.cine.dtos.CantidadCargaRequest;
import com.pablocompany.rest.api.proyectono2ipc2.cine.models.Cine;
import com.pablocompany.rest.api.proyectono2ipc2.connectiondb.DBConnectionSingleton;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author pablo
 */
//Clase Delgada para poder interactuar con la base de datos para poder gestionar los cines
public class CineDB {

    //Constante que sirve para poder crear a los cines
    private final String CREAR_CINE = "INSERT INTO cine (codigo, nombre, estado_anuncios, monto_ocultacion, fecha_creacion, descripcion, ubicacion) VALUES(?,?,?,?,?,?,?)";

    //Constante que permite obtener la cantidad de cines registrados en el sistema
    private final String CANTIDAD_CINES = "SELECT COUNT(*) AS `cantidad` FROM cine";

    //Constante que permite obtener los cines que estan creados en el sistema 
    private final String CONSULTAR_CINES = "SELECT codigo, nombre, estado_anuncios, monto_ocultacion, fecha_creacion, descripcion, ubicacion FROM cine LIMIT ? OFFSET ?";

    //Constante que permite obtener los cines que estan creados en el sistema 
    private final String CONSULTAR_CINES_CODIGO = "SELECT codigo, nombre, estado_anuncios, monto_ocultacion, fecha_creacion, descripcion, ubicacion FROM cine WHERE codigo = ?";

    //Constante que permite obtener los cines registrados en la aplicacion para el dashboard del usuario 
    private final String CONSULTAR_CINE_DASHBOARD = "SELECT codigo, nombre, descripcion, ubicacion FROM cine";

    //Constante que permite obtener el cine seleccionado para el dashboard del usuario 
    private final String CONSULTAR_CINE_CODIGO_DASHBOARD = "SELECT nombre, descripcion, ubicacion FROM cine WHERE codigo = ?";

    //Metodo que sirve para poder registrar un nuevo cine en el sistema
    public boolean crearNuevoCine(Cine cineNuevo) throws ErrorInesperadoException, FormatoInvalidoException {

        if (cineNuevo == null) {
            throw new FormatoInvalidoException("No se ha enviado ninguna informacion sobre el cine a crear");
        }
        
        BilleteraCineDB billeteraCineDb = new BilleteraCineDB();

        Connection conexion = DBConnectionSingleton.getInstance().getConnection();

        try {

            conexion.setAutoCommit(false);

            int filasAfectadas = insertarNuevoCine(conexion, cineNuevo);
           
            
            int filasAfectadasBilletera = billeteraCineDb.crearBilleteraCine(new BilleteraCine(0, cineNuevo.getCodigo().trim()), conexion);

            if (filasAfectadas > 0 && filasAfectadasBilletera > 0) {

                conexion.commit();
                return true;

            } else {
                conexion.rollback();
                throw new ErrorInesperadoException("No se ha podido crear el cine.");
            }

        } catch (SQLException ex) {

            try {
                conexion.rollback();
            } catch (SQLException ex1) {
                throw new ErrorInesperadoException("Error al hacer Rollback al crear un nuevo cine");
            }

            throw new ErrorInesperadoException("No se permiten inyecciones sql o partones diferentes a los que se piden al crear un nuevo cine");
        } finally {

            try {

                conexion.setAutoCommit(true);

            } catch (SQLException ex) {
                System.out.println("Error al reactivar la autoconfirmacion al crear un nuevo cine");
            }
        }
    }

    //Metodo que sirve para poder generar la transaccion para insertar cine
    public int insertarNuevoCine(Connection conexion, Cine cineNuevo) throws ErrorInesperadoException {

        try (PreparedStatement preparedStmt = conexion.prepareStatement(CREAR_CINE);) {

            preparedStmt.setString(1, cineNuevo.getCodigo());
            preparedStmt.setString(2, cineNuevo.getNombre());
            preparedStmt.setBoolean(3, cineNuevo.isEstadoAnuncio());
            preparedStmt.setBigDecimal(4, new BigDecimal(cineNuevo.getMontoOcultacion()));
            preparedStmt.setDate(5, java.sql.Date.valueOf(cineNuevo.getFechaCreacion()));
            preparedStmt.setString(6, cineNuevo.getDescripcion());
            preparedStmt.setString(7, cineNuevo.getUbicacion());

            int filasAfectadas = preparedStmt.executeUpdate();

            return filasAfectadas;

        } catch (SQLException ex) {
            throw new ErrorInesperadoException("No se ha podido crear el nuevo registro de cine.");
        }

    }

    //Metodo delegado para obtener la cantidad de cines registrados en el sistema
    public int cantidadCinesRegistrados() throws ErrorInesperadoException, DatosNoEncontradosException {

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CANTIDAD_CINES);) {

            ResultSet result = query.executeQuery();
            if (result.next()) {
                return result.getInt("cantidad");
            } else {

                throw new DatosNoEncontradosException("No hay registros de cines registrados en el sistema");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se ha podido conectar con la base de datos para obtener la cantidad de cines");
        }
    }

    //Metodo que permite obtener el listado completo de Cines para el administrador de sistema
    public List<Cine> obtenerListadoCinesAsociados(CantidadCargaRequest cargaRequest) throws FormatoInvalidoException, ErrorInesperadoException {

        if (cargaRequest == null) {
            throw new FormatoInvalidoException("La referencia de request esta vacia");
        }

        List<Cine> listadoCines = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CONSULTAR_CINES);) {

            query.setInt(1, cargaRequest.getLimit());
            query.setInt(2, cargaRequest.getOffset());

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                Cine cineEncontrado = new Cine(
                        resultSet.getString("codigo"),
                        resultSet.getString("nombre"),
                        resultSet.getBoolean("estado_anuncios"),
                        resultSet.getBigDecimal("monto_ocultacion").doubleValue(),
                        resultSet.getDate("fecha_creacion").toLocalDate(),
                        resultSet.getString("descripcion"),
                        resultSet.getString("ubicacion")
                );

                listadoCines.add(cineEncontrado);
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de los cines asociados al sistema");
        }

        return listadoCines;
    }

    //Metodo que permite obtener un cine en especifico para el administrador de sistema
    public Cine obtenerCine(String idCine) throws FormatoInvalidoException, ErrorInesperadoException {

        if (StringUtils.isBlank(idCine)) {
            throw new FormatoInvalidoException("El id del cine esta vacio");
        }

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CONSULTAR_CINES_CODIGO);) {

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                Cine cineEncontrado = new Cine(
                        resultSet.getString("codigo"),
                        resultSet.getString("nombre"),
                        resultSet.getBoolean("estado_anuncios"),
                        resultSet.getBigDecimal("monto_ocultacion").doubleValue(),
                        resultSet.getDate("fecha_creacion").toLocalDate(),
                        resultSet.getString("descripcion"),
                        resultSet.getString("ubicacion")
                );

                return cineEncontrado;

            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos del cine asociado al sistema");
        }

        throw new ErrorInesperadoException("No se han podido obtener los datos del cine asociado al sistema");
    }

}
