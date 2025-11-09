/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.administradorsistema.database;

import com.pablocompany.rest.api.proyectono2ipc2.administradorsistema.models.PagoBloqueoAnuncios;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author pablo
 */
//Clase delegada para poder crear el bloqueo de los anuncios durante el periodo de tiempo
public class PagoBloqueoAnunciosDB {
    
    
    private String REGITRAR_PAGO_BLOQUEO = "INSERT INTO bloqueo_anuncio(estado, fecha_inicio, fecha_fin, codigo_cine ) VALUES(?,?,?,?)";
    
    
    
    //Metodo que sirve para poder registrar un usuario en el sistema
    public int generarPagoBloqueo(PagoBloqueoAnuncios pagoBloqueo, Connection conexion) throws ErrorInesperadoException, FormatoInvalidoException {

        try (PreparedStatement preparedStmt = conexion.prepareStatement(REGITRAR_PAGO_BLOQUEO);) {

            preparedStmt.setBoolean(1, pagoBloqueo.isEstado());
            preparedStmt.setDate(2, java.sql.Date.valueOf(pagoBloqueo.getFechaInicio()));
            preparedStmt.setDate(3, java.sql.Date.valueOf(pagoBloqueo.getFechaFin()));
            preparedStmt.setString(4, pagoBloqueo.getCodigoCine());

            int filasAfectadas = preparedStmt.executeUpdate();

            return filasAfectadas;

        } catch (SQLException ex) {

            throw new ErrorInesperadoException("No se ha podido debitar el saldo de la billetera digital. ");
        }

    }
    
}
