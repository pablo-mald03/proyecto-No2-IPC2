/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.administradorsistema.database;

import com.pablocompany.rest.api.proyectono2ipc2.administradorsistema.models.PagoBloqueoAnuncios;
import com.pablocompany.rest.api.proyectono2ipc2.administradorsistema.models.PagoOcultacionAnuncios;
import com.pablocompany.rest.api.proyectono2ipc2.billeteracine.database.BilleteraCineDB;
import com.pablocompany.rest.api.proyectono2ipc2.billeteracine.models.BilleteraCineDTO;
import com.pablocompany.rest.api.proyectono2ipc2.connectiondb.DBConnectionSingleton;
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
//Clase delegada para operar la transaccion de pago de ocultacion de anuncios
public class PagoOcultacionAnuncioDB {

    //Constante que permite registrar el pago de ocultacion del cine como una transaccion 
    private final String GENERAR_PAGO = "INSERT INTO pago_ocultacion_cine ( monto, fecha_pago, codigo_cine) VALUES (?,?,?)";

    //Metodo delegado para poder reestablecer/cambiar la password del usuario
    public boolean generarTransaccion(PagoOcultacionAnuncios pagoOcultacion, double saldoActual) throws ErrorInesperadoException, FormatoInvalidoException {

        if (pagoOcultacion == null) {
            throw new FormatoInvalidoException("No se ha enviado ninguna informacion sobre la transaccion");
        }

        Connection conexion = DBConnectionSingleton.getInstance().getConnection();

        try {

            conexion.setAutoCommit(false);
            
            BilleteraCineDB billeteraCine = new BilleteraCineDB();
            PagoBloqueoAnunciosDB pagoBloqueoAnunciosDb = new PagoBloqueoAnunciosDB();

            int filasAfectadas = generarPago(pagoOcultacion, conexion);
            
            int filasAfectadasBloqueo = pagoBloqueoAnunciosDb.generarPagoBloqueo(new PagoBloqueoAnuncios(true, pagoOcultacion.getFechaPago(), pagoOcultacion.getCodigoCine()), conexion);
            
            int filasAfectadasBilletera = billeteraCine.modificarSaldo(conexion, new BilleteraCineDTO(pagoOcultacion.getCodigoCine(), saldoActual, "null"));
            

            if (filasAfectadas > 0 && filasAfectadasBilletera > 0 && filasAfectadasBloqueo > 0) {

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
                throw new ErrorInesperadoException("Error al reactivar la autoconfirmacion al recargar la billetera.");
            }
        }
    }

    //Metodo que sirve para poder registrar un usuario en el sistema
    public int generarPago(PagoOcultacionAnuncios pagoOcultacion, Connection conexion) throws ErrorInesperadoException, FormatoInvalidoException {

        try (PreparedStatement preparedStmt = conexion.prepareStatement(GENERAR_PAGO);) {

            preparedStmt.setBigDecimal(1, new BigDecimal(pagoOcultacion.getMonto()));
            preparedStmt.setDate(2, java.sql.Date.valueOf(pagoOcultacion.getFechaPago()));
            preparedStmt.setString(3, pagoOcultacion.getCodigoCine());

            int filasAfectadas = preparedStmt.executeUpdate();

            return filasAfectadas;

        } catch (SQLException ex) {

            throw new ErrorInesperadoException("No se ha podido debitar el saldo de la billetera digital. ");
        }

    }
}
