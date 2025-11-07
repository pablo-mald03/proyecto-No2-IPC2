/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.costocine.database;

import com.pablocompany.rest.api.proyectono2ipc2.connectiondb.DBConnectionSingleton;
import com.pablocompany.rest.api.proyectono2ipc2.costocine.models.CostoCine;
import com.pablocompany.rest.api.proyectono2ipc2.costocine.models.CostoModificacionDTO;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import java.math.BigDecimal;
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
//Clase delegada para poder gestionar la tascendencia de costos de cine
public class CostoCineDB {

    //Constante que permite crear el costo de cine actual al momento de crear el cine 
    private final String CREAR_COSTO_CINE = "INSERT INTO costo_cine(costo, fecha_modificacion, codigo_cine) VALUES(?,?,?)";

    //Constante que permite mostrar el listado de costos que ha tenido el cine
    private final String CONSULTAR_COSTO_CINE = "SELECT costo, fecha_modificacion FROM costo_cine WHERE codigo_cine= ?";

    //Constante que permite mostrar el listado de costos que ha tenido el cine
    private final String CONSULTAR_COSTO_CINE_ACTUAL = "SELECT costo FROM costo_cine WHERE codigo_cine = ? ORDER BY codigo DESC LIMIT 1";

    //Constante que permite validar el costo nuevo de cine
    private final String COSTO_CREACION_SALA = "SELECT fecha_modificacion FROM costo_cine WHERE codigo_cine = ? ORDER BY codigo ASC LIMIT 1";

    //Constante que permite consultar la cantidad de costos que ha tenido el cine 
    private final String CANTIDAD_COSTO_CINE = "SELECT COUNT(*) AS `cantidad` FROM costo_cine WHERE codigo_cine= ?";

    //Metodo que retorna la cantidad de costos que ha tenido el cine 
    public int cantidadCostosCine() throws ErrorInesperadoException, DatosNoEncontradosException {

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CANTIDAD_COSTO_CINE);) {

            ResultSet result = query.executeQuery();
            if (result.next()) {
                return result.getInt("cantidad");
            } else {

                throw new DatosNoEncontradosException("No hay registros del historial de costos de cine");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se ha podido conectar con la base de datos para obtener el historial de costos de cine");
        }
    }

    //Metodo que retornar el primer costo de cine que se tuvo para validar 
    public LocalDate fechaPrimerCostoCine(String idCine) throws ErrorInesperadoException, DatosNoEncontradosException, FormatoInvalidoException {

        if (StringUtils.isBlank(idCine)) {
            throw new FormatoInvalidoException("El id del cine esta vacio");
        }
        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(COSTO_CREACION_SALA);) {

            query.setString(1, idCine.trim());
            ResultSet result = query.executeQuery();
            if (result.next()) {
                LocalDate fechaPrincipal = result.getDate("fecha_modificacion").toLocalDate();
                return fechaPrincipal;
            } else {

                throw new DatosNoEncontradosException("No hay registros del historial de costos de cine");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se ha podido conectar con la base de datos para obtener el historial de costos de cine");
        }
    }

    //Metodo que permite crear el costo de modificacion de cine principal
    public int registrarCostoCine(CostoCine costoCine, Connection conexion) throws ErrorInesperadoException {

        try (PreparedStatement preparedStmt = conexion.prepareStatement(CREAR_COSTO_CINE);) {

            preparedStmt.setBigDecimal(1, new BigDecimal(costoCine.getCosto()));
            preparedStmt.setDate(2, java.sql.Date.valueOf(costoCine.getFechaModificacion()));
            preparedStmt.setString(3, costoCine.getCodigoCine());

            int filasAfectadas = preparedStmt.executeUpdate();

            return filasAfectadas;

        } catch (SQLException ex) {
            throw new ErrorInesperadoException("No se ha podido crear la fecha de modificacion del cine.");
        }
    }

    //Metodo que permite obtener el costo de cine actual
    public double obtenerCostoActual(String idCine) throws FormatoInvalidoException, ErrorInesperadoException {

        if (StringUtils.isBlank(idCine)) {
            throw new FormatoInvalidoException("El id del cine esta vacio");
        }

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CONSULTAR_COSTO_CINE_ACTUAL);) {

            query.setString(1, idCine);

            ResultSet resultSet = query.executeQuery();

            if (resultSet.next()) {

                double costoActual = resultSet.getBigDecimal("costo").doubleValue();
                return costoActual;
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener el costo actual del cine ");
        }

        throw new ErrorInesperadoException("No se han podido obtener el costo actual del cine");
    }

    //Metodo que sirve para poder crear un nuevo costo de cine 
    public boolean definirNuevoCosto(CostoCine costo) throws FormatoInvalidoException, ErrorInesperadoException {

        if (costo == null) {
            throw new FormatoInvalidoException("la referencia de costos de cine esta vacia");
        }

        Connection conexion = DBConnectionSingleton.getInstance().getConnection();

        try {

            conexion.setAutoCommit(false);

            int filasAfectadasCosto = registrarCostoCine(costo, conexion);

            if (filasAfectadasCosto > 0) {

                conexion.commit();
                return true;

            } else {
                conexion.rollback();
                throw new ErrorInesperadoException("No se ha podido definir el nuevo costo del cine");
            }

        } catch (SQLException ex) {

            try {
                conexion.rollback();
            } catch (SQLException ex1) {
                throw new ErrorInesperadoException("Error al hacer Rollback al definir el nuevo costo del cine");
            }

            throw new ErrorInesperadoException("No se permiten inyecciones sql o partones diferentes a los que se piden al definir un nuevo costo de cine");
        } finally {

            try {

                conexion.setAutoCommit(true);

            } catch (SQLException ex) {
                System.out.println("Error al reactivar la autoconfirmacion al definir un nuevo costo de cine");
            }
        }

    }

    //Metodo que permite obtener el listado completo del costo de ciertos cines
    public List<CostoModificacionDTO> obtenerListadoCinesAsociados(String idCine) throws FormatoInvalidoException, ErrorInesperadoException {

        if (StringUtils.isBlank(idCine)) {
            throw new FormatoInvalidoException("El id del cine esta vacio");
        }

        List<CostoModificacionDTO> listadoCostos = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CONSULTAR_COSTO_CINE);) {

            query.setString(1, idCine.trim());

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                CostoModificacionDTO costoEncontrado = new CostoModificacionDTO(
                        resultSet.getBigDecimal("costo").doubleValue(),
                        resultSet.getDate("fecha_modificacion").toLocalDate()
                );

                listadoCostos.add(costoEncontrado);
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de los costos que ha tenido el cine");
        }

        return listadoCostos;
    }

}
