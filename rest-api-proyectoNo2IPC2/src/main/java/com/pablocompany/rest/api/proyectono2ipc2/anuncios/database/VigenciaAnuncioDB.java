/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.anuncios.database;

import com.pablocompany.rest.api.proyectono2ipc2.anuncios.dtos.VigenciaAnuncioDTO;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.models.CambiarPrecioDTO;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.models.ConfiguracionAnuncioDTO;
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

/**
 *
 * @author pablo
 */
//Clase que permite mostrar la configuracion de la vigencia de los anuncios
public class VigenciaAnuncioDB {

    //Constante que permite obtener los valores actuales de configuracion del anuncio 
    private final String CONSULTAR_VIGENCIA = "SELECT codigo, contexto, precio, duracion FROM vigencia_anuncio";

    //Constante que permite obtener los valores actuales de configuracion del anuncio 
    private final String CONSULTAR_VIGENCIA_CODIGO = "SELECT codigo, contexto, precio, duracion FROM vigencia_anuncio WHERE codigo = ?";

    //Constante que permite editar los precios de las configuraciones de anuncios 
    private final String MODIFICAR_PRECIOS = "UPDATE vigencia_anuncio SET precio = ? WHERE codigo = ?";

    //Constante que permite editar obtener los precios de las configuraciones de 
    private final String CONSULTAR_PRECIO_CODIGO = "SELECT precio FROM vigencia_anuncio WHERE codigo = ?";

    //Metodo que permite obtener el listado completo de las vigencias de anuncios
    public List<VigenciaAnuncioDTO> obtenerListadoVigencia() throws FormatoInvalidoException, ErrorInesperadoException {

        List<VigenciaAnuncioDTO> listaVigencia = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CONSULTAR_VIGENCIA);) {

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                VigenciaAnuncioDTO vigenciaEncontrado = new VigenciaAnuncioDTO(
                         resultSet.getInt("codigo"),
                        resultSet.getString("contexto"),
                        resultSet.getBigDecimal("precio").doubleValue(),
                        resultSet.getBigDecimal("duracion").doubleValue()
                       
                );

                listaVigencia.add(vigenciaEncontrado);
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de los registros de vigencia de los anuncios");
        }

        return listaVigencia;
    }

    //Metodo que permite obtener una vigencia especifica
    public VigenciaAnuncioDTO obtenerVigenciaCodigo(int codigo) throws FormatoInvalidoException, ErrorInesperadoException, DatosNoEncontradosException {

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CONSULTAR_VIGENCIA_CODIGO);) {

            query.setInt(1, codigo);
            ResultSet resultSet = query.executeQuery();

            if (resultSet.next()) {
                VigenciaAnuncioDTO vigenciaEncontrado = new VigenciaAnuncioDTO(
                         resultSet.getInt("codigo"),
                        resultSet.getString("contexto"),
                        resultSet.getBigDecimal("precio").doubleValue(),
                        resultSet.getBigDecimal("duracion").doubleValue()
                       
                );

                return vigenciaEncontrado;
            } else {
                throw new DatosNoEncontradosException("No se ha encontrado informacion de configuracion con el codigo enviado");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de una configuracion especifica");
        }
    }

    //Metodo que permite obtener el precio actual de las vigencias de anucios
    public double obtenerPrecioCodigo(int codigo) throws FormatoInvalidoException, ErrorInesperadoException {

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CONSULTAR_PRECIO_CODIGO);) {

            query.setInt(1, codigo);

            ResultSet resultSet = query.executeQuery();

            if (resultSet.next()) {

                double costoActual = resultSet.getBigDecimal("precio").doubleValue();
                return costoActual;
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener precio actual del la vigencia solicitada");
        }

        throw new ErrorInesperadoException("No se han podido obtener el percio actual de la vigencia solicitada");
    }

    //Metodo que sirve para poder cambiar el precio de la vigencia
    public boolean cambiarPrecio(CambiarPrecioDTO cambioPrecio) throws ErrorInesperadoException, FormatoInvalidoException, DatosNoEncontradosException {

        if (cambioPrecio == null) {
            throw new FormatoInvalidoException("No se ha enviado ninguna informacion sobre la vigencia a editar");
        }

        Connection conexion = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement preparedStmt = conexion.prepareStatement(MODIFICAR_PRECIOS);) {

            conexion.setAutoCommit(false);

            preparedStmt.setBigDecimal(1, new BigDecimal(cambioPrecio.getPrecio()));
            preparedStmt.setInt(2, cambioPrecio.getCodigo());

            int filasAfectadas = preparedStmt.executeUpdate();

            if (filasAfectadas > 0) {

                conexion.commit();
                return true;

            } else {
                conexion.rollback();
                throw new DatosNoEncontradosException("No se ha podido cambiar el precio de la vigencia de anuncios");
            }

        } catch (SQLException ex) {

            try {
                conexion.rollback();
            } catch (SQLException ex1) {
                throw new ErrorInesperadoException("Error al hacer Rollback al cambiar el precio de la vigencia de anuncios");
            }

            throw new ErrorInesperadoException("No se permiten inyecciones sql o patrones diferentes a los que se piden en el cambio de precio de la vigencia de anuncios");
        } finally {

            try {

                conexion.setAutoCommit(true);

            } catch (SQLException ex) {
                throw new ErrorInesperadoException("Error al reactivar la autoconfirmacion al cambiar el precio de la vigencia de anuncios");
            }
        }
    }

}
