/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.pagoanuncio.database;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.pagoanuncio.models.PagoAnuncio;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author pablo
 */
//Clase delegada para poder operar con los pagos para anuncios 
public class PagoAnuncioDB {
    
    //Constante que permite ejecutar el pago de los anuncios
    private final String PAGAR_ANUNCIO = "INSERT INTO pago_anuncio (monto, fecha_pago, id_usuario) VALUES(?,?,?)";
    
    
     //Metodo que sirve para poder ejecutar la transaccion de pago del anunciante
    public int generarPagoAnuncio(PagoAnuncio pagoAnuncio, Connection conexion) throws ErrorInesperadoException, FormatoInvalidoException {

        try (PreparedStatement preparedStmt = conexion.prepareStatement(PAGAR_ANUNCIO);) {

            preparedStmt.setBigDecimal(1, new BigDecimal(pagoAnuncio.getMonto()));
            preparedStmt.setDate(2, java.sql.Date.valueOf(pagoAnuncio.getFechaPago()));
            preparedStmt.setString(3, pagoAnuncio.getIdUsuario().trim());
            int filasAfectadas = preparedStmt.executeUpdate();

            return filasAfectadas;

        } catch (SQLException ex) {

            throw new ErrorInesperadoException("No se ha podido registrar el pago del anuncio. ");
        }

    }

    
    
    
    
    
}
