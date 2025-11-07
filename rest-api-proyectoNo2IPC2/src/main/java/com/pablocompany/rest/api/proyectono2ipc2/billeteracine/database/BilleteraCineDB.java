/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.billeteracine.database;

import com.pablocompany.rest.api.proyectono2ipc2.billeteracine.models.BilleteraCine;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author pablo
 */
//Clase delegada para poder mostrar la billetera digital del cine y operar con sus datos
public class BilleteraCineDB {

    //Constante que permite crear la billetera digital de un cine 
    private final String CREAR_BILLETERA = "INSERT INTO billetera_cine(saldo, codigo_cine ) VALUES(?,?)";

    //Constante que permite consultar el saldo de la billetera asociado al cine
    private final String CONSULTAR_BILLETERA_CINE = "SELECT saldo FROM billetera_cine WHERE  codigo_cine = ?";

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

}
