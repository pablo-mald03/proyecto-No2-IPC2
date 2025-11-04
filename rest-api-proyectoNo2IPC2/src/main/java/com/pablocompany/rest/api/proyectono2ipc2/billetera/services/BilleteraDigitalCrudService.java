/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.billetera.services;

import com.pablocompany.rest.api.proyectono2ipc2.billetera.database.BilleteraDigitalDB;
import com.pablocompany.rest.api.proyectono2ipc2.billetera.dtos.BilleteraDigitalRequest;
import com.pablocompany.rest.api.proyectono2ipc2.billetera.models.BilleteraDigital;
import com.pablocompany.rest.api.proyectono2ipc2.billetera.models.SaldoBilleteraDTO;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author pablo
 */
//Clase delegada para poder generar el response acorde al servicio 
public class BilleteraDigitalCrudService {

    //Metodo que retorna la cantidad de la billetera digital del usuario 
    public SaldoBilleteraDTO obtenerSaldo(String idUsuario) throws DatosNoEncontradosException, ErrorInesperadoException, FormatoInvalidoException {

        if (StringUtils.isBlank(idUsuario)) {
            throw new FormatoInvalidoException("El id del usuario esta vacio");
        }

        BilleteraDigitalDB billeteraDigitalDb = new BilleteraDigitalDB();

        return billeteraDigitalDb.obtenerSaldoActual(idUsuario);
    }

    //Metodo que permite recargar el saldo de la cuenta de la billetera digital del usuario
    public boolean recargarBilletera(BilleteraDigitalRequest billeteraRequest) throws FormatoInvalidoException, ErrorInesperadoException {

        BilleteraDigital billeteraDigital = extraerDatos(billeteraRequest);
        
        BilleteraDigitalDB billeteraDigitalDb = new BilleteraDigitalDB();
        
        return billeteraDigitalDb.recargarBilletera(billeteraDigital);

    }

    //Metodo delegado para extraer y validar los datos del request 
    private BilleteraDigital extraerDatos(BilleteraDigitalRequest billeteraRequest) throws FormatoInvalidoException {

        if (StringUtils.isBlank(billeteraRequest.getIdUsuario())) {
            throw new FormatoInvalidoException("El id del usuario esta vacio");
        }

        if (StringUtils.isBlank(billeteraRequest.getSaldo())) {
            throw new FormatoInvalidoException("El saldo a recargar no fue especificado");
        }

        if (!StringUtils.isNumeric(billeteraRequest.getSaldo())) {
            throw new FormatoInvalidoException("El saldo a recargar no es numerico");
        }
        
        try {
        
            double saldo = Double.parseDouble(billeteraRequest.getSaldo());
            
            return new BilleteraDigital(
                    saldo, 
                    billeteraRequest.getIdUsuario());
            
            
        } catch (NumberFormatException e) {
             throw new FormatoInvalidoException("El saldo a transferir no es numerico");
        }
        
        

    }

}
