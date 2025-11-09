/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.billeteracine.database;

import com.pablocompany.rest.api.proyectono2ipc2.billeteracine.models.BilleteraCine;
import com.pablocompany.rest.api.proyectono2ipc2.billeteracine.models.BilleteraCineDTO;
import com.pablocompany.rest.api.proyectono2ipc2.cine.dtos.CantidadCargaRequest;
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
//Clase delegada para poder mostrar la billetera digital del cine y operar con sus datos
public class BilleteraCineDB {

    //Constante que permite crear la billetera digital de un cine 
    private final String CREAR_BILLETERA = "INSERT INTO billetera_cine(saldo, codigo_cine ) VALUES(?,?)";

    //Constante que permite consultar el saldo de la billetera asociado al cine
    private final String MODIFICAR_BILLETERA = "UPDATE billetera_cine  SET saldo  = ? WHERE codigo_cine = ?";

    //Constante que permite mostrarle al usuario  las billeteras a las que se encuentra asociado 
    private final String CONSULTAR_BILLETERA_CINE_ASOCIADO = "SELECT bc.codigo_cine, bc.saldo, ci.nombre FROM billetera_cine AS `bc` JOIN cine AS `ci` ON bc.codigo_cine = ci.codigo JOIN gestion_cine AS `gc` ON bc.codigo_cine = gc.codigo_cine WHERE gc.id_usuario = ? LIMIT ? OFFSET ?";

    //Constante que permite mostrar la billetera de cine
    private final String CONSULTAR_BILLETERA_CINE = "SELECT bc.codigo_cine, bc.saldo, ci.nombre FROM billetera_cine AS `bc` JOIN cine AS `ci` ON bc.codigo_cine = ci.codigo JOIN gestion_cine AS `gc` ON bc.codigo_cine = gc.codigo_cine WHERE ci.codigo_cine = ? ";

    //Constante que permite indicarle al usuario la cantidad de billetearas digitales que tiene
    private final String CONSULTAR_BILLETERA_CANTIDAD = "SELECT COUNT(*) AS `cantidad` FROM billetera_cine AS `bc` JOIN cine AS `ci` ON bc.codigo_cine = ci.codigo JOIN gestion_cine AS `gc` ON bc.codigo_cine = gc.codigo_cine WHERE gc.id_usuario = ?";

    //Metodo que permite crear la nueva billetera del cine
    public int crearBilleteraCine(BilleteraCine billeteraCine, Connection conexion) throws ErrorInesperadoException {

        try (PreparedStatement preparedStmt = conexion.prepareStatement(CREAR_BILLETERA);) {

            preparedStmt.setBigDecimal(1, new BigDecimal(billeteraCine.getSaldo()));
            preparedStmt.setString(2, billeteraCine.getCodigoCine());

            int filasAfectadas = preparedStmt.executeUpdate();

            return filasAfectadas;

        } catch (SQLException ex) {
            throw new ErrorInesperadoException("No se ha podido crear la billetera asociada al nuevo cine.");
        }
    }

    //Metodo que permite obtener el listado completo de cines asociados a un administrador de cine
    public BilleteraCineDTO billeteraPorCodigo(String idCine) throws FormatoInvalidoException, ErrorInesperadoException, DatosNoEncontradosException {

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CONSULTAR_BILLETERA_CINE);) {

            query.setString(1, idCine.trim());

            ResultSet resultSet = query.executeQuery();

            if (resultSet.next()) {
                BilleteraCineDTO cineEncontrado = new BilleteraCineDTO(
                        resultSet.getString("codigo_cine"),
                        resultSet.getBigDecimal("saldo").doubleValue(),
                        resultSet.getString("nombre")
                );

                return cineEncontrado;
            }else{
                throw new DatosNoEncontradosException("No se ha encontrado ningun registro de billetera digital con el codigo de cine");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de la billetera digital de cine");
        }

    }

    //Metodo que permite obtener el listado completo de cines asociados a un administrador de cine
    public List<BilleteraCineDTO> billeterasAsociadasCine(String idUsuario, CantidadCargaRequest cargaRequest) throws FormatoInvalidoException, ErrorInesperadoException {

        if (StringUtils.isBlank(idUsuario)) {
            throw new FormatoInvalidoException("El id del usuario esta vacio");
        }

        List<BilleteraCineDTO> listadoBilleteras = new ArrayList<>();

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CONSULTAR_BILLETERA_CINE_ASOCIADO);) {

            query.setString(1, idUsuario.trim());
            query.setInt(2, cargaRequest.getLimit());
            query.setInt(3, cargaRequest.getOffset());

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                BilleteraCineDTO cineEncontrado = new BilleteraCineDTO(
                        resultSet.getString("codigo_cine"),
                        resultSet.getBigDecimal("saldo").doubleValue(),
                        resultSet.getString("nombre")
                );

                listadoBilleteras.add(cineEncontrado);
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de la billetera digital de cine");
        }

        return listadoBilleteras;
    }

    //Metodo delegado para obtener la cantidad de cines a los que se encuentra asignado el administrador de cine
    public int cantidadCinesAsignados(String idUsuario) throws ErrorInesperadoException, DatosNoEncontradosException, FormatoInvalidoException {

        if (StringUtils.isBlank(idUsuario)) {
            throw new FormatoInvalidoException("El id del usuario esta vacio");
        }
        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CONSULTAR_BILLETERA_CANTIDAD);) {

            query.setString(1, idUsuario.trim());
            ResultSet result = query.executeQuery();
            if (result.next()) {
                return result.getInt("cantidad");
            } else {

                throw new DatosNoEncontradosException("No hay registros de billeteras digitales de cine");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se ha podido conectar con la base de datos para obtener las billeteraas digitales de cine");
        }
    }

    //Metodo que sirve para poder generar la transaccion para recargar billetera
    public int modificarSaldo(Connection conexion, BilleteraCineDTO billeteraDto) throws ErrorInesperadoException {

        try (PreparedStatement preparedStmt = conexion.prepareStatement(MODIFICAR_BILLETERA);) {

            preparedStmt.setBigDecimal(1, new BigDecimal(billeteraDto.getSaldo()));

            preparedStmt.setString(2, billeteraDto.getCodigoCine());

            int filasAfectadas = preparedStmt.executeUpdate();

            return filasAfectadas;

        } catch (SQLException ex) {
            throw new ErrorInesperadoException("No se ha podido crear la transaccion de la billtera.");
        }

    }

    //Metodo que permite recargar la billetera del cine
    public boolean recargarBilletera(BilleteraCineDTO billeteraDto) throws ErrorInesperadoException {

        Connection conexion = DBConnectionSingleton.getInstance().getConnection();

        try {

            conexion.setAutoCommit(false);

            int filasAfectadas = modificarSaldo(conexion, billeteraDto);

            if (filasAfectadas > 0) {

                conexion.commit();
                return true;

            } else {
                conexion.rollback();
                throw new ErrorInesperadoException("No se ha podido generar transaccion en la billetera del cine");
            }

        } catch (SQLException ex) {

            try {
                conexion.rollback();
            } catch (SQLException ex1) {
                throw new ErrorInesperadoException("Error al hacer Rollback en la billetera del cine");
            }

            throw new ErrorInesperadoException("No se permiten inyecciones sql o partones diferentes a los que se piden la billetera del cine");
        } finally {

            try {

                conexion.setAutoCommit(true);

            } catch (SQLException ex) {
                throw new ErrorInesperadoException("Error al reactivar la autoconfirmacion en la billetera del cine");
            }
        }

    }

}
