/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.billetera.database;

import com.pablocompany.rest.api.proyectono2ipc2.billetera.models.BilleteraDigital;
import com.pablocompany.rest.api.proyectono2ipc2.billetera.models.SaldoBilleteraDTO;
import com.pablocompany.rest.api.proyectono2ipc2.connectiondb.DBConnectionSingleton;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.models.CambioCredencialesDTO;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.models.DatosUsuario;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.models.Usuario;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author pablo
 */
//Clase delegada para poder interactuar con los diferentes usuarios que cuentan con una billetera 
public class BilleteraDigitalDB {

    //Constante que sirve para crear la billetera digital del usuario 
    private final String CREAR_BILLETERA = "INSERT INTO billetera_digital(saldo, id_usuario) VALUES (?,?)";

    //Constante que sirve para obtener el saldo actual de la cuenta del usuario 
    private final String CONSULTAR_SALDO = "SELECT bi.saldo FROM usuario AS `us` JOIN billetera_digital AS `bi` ON  bi.id_usuario = us.id WHERE us.id = ?";

    //Constante que sirve para poder regargar la billetera digital del usuario 
    private final String RECARGAR_BILLETERA = "UPDATE billetera_digital SET  saldo = ? WHERE id_usuario = ?";

    //Metodo delegado para poder reestablecer/cambiar la password del usuario
    public boolean recargarBilletera(BilleteraDigital billeteraDigital) throws ErrorInesperadoException, FormatoInvalidoException {

        if (billeteraDigital == null) {
            throw new FormatoInvalidoException("No se ha enviado ninguna informacion sobre la transaccion");
        }

        Connection conexion = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement preparedStmt = conexion.prepareStatement(RECARGAR_BILLETERA);) {

            conexion.setAutoCommit(false);
            preparedStmt.setBigDecimal(1, new BigDecimal(billeteraDigital.getSaldo()));
            preparedStmt.setString(2, billeteraDigital.getIdUsuario());

            int filasAfectadas = preparedStmt.executeUpdate();

            if (filasAfectadas > 0) {

                conexion.commit();
                return true;

            } else {
                conexion.rollback();
                throw new ErrorInesperadoException("No se ha podido generar la transaccion para recargar la billetera digital. ");
            }

        } catch (SQLException ex) {

            try {
                conexion.rollback();
            } catch (SQLException ex1) {
                throw new ErrorInesperadoException("Error al hacer Rollback al recargar la billetera digital");
            }

            throw new ErrorInesperadoException("No se permiten inyecciones sql o partones diferentes a los que se piden al recargar la billetera");
        } finally {

            try {

                conexion.setAutoCommit(true);

            } catch (SQLException ex) {
                System.out.println("Error al reactivar la autoconfirmacion al recargar la billetera.");
            }
        }
    }

    //Metodo que sirve para poder registrar un usuario en el sistema
    public int insertarBilletera(BilleteraDigital billetera, Connection conexion) throws ErrorInesperadoException, IOException, FormatoInvalidoException {

        try (PreparedStatement preparedStmt = conexion.prepareStatement(CREAR_BILLETERA);) {

            preparedStmt.setBigDecimal(1, new BigDecimal(billetera.getSaldo()));
            preparedStmt.setString(2, billetera.getIdUsuario());

            int filasAfectadas = preparedStmt.executeUpdate();

            return filasAfectadas;

        } catch (SQLException ex) {

            throw new ErrorInesperadoException("No se ha podido recargar la billetera digital del usuario. ");
        }

    }

    //Metodo que permite retornar las credenciales del usuario en base a los datos ingresados en el login
    public SaldoBilleteraDTO obtenerSaldoActual(String idUsuario) throws ErrorInesperadoException, FormatoInvalidoException, DatosNoEncontradosException {

        if (StringUtils.isBlank(idUsuario)) {
            throw new FormatoInvalidoException("El codigo del usuario esta vacio");
        }

        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(CONSULTAR_SALDO);) {

            query.setString(1, idUsuario.trim());

            ResultSet resultSet = query.executeQuery();

            if (resultSet.next()) {
                return new SaldoBilleteraDTO(
                        resultSet.getBigDecimal("saldo").doubleValue()
                );
            } else {
                throw new DatosNoEncontradosException("No se ha encontrado el registro de la billetera digital del usuario");
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("No se han podido obtener los datos de la billetera digital del usuario.");
        }

    }

}
