/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.billeteracine.services;

import com.pablocompany.rest.api.proyectono2ipc2.administradorsistema.models.CantidadRegistrosDTO;
import com.pablocompany.rest.api.proyectono2ipc2.billeteracine.database.BilleteraCineDB;
import com.pablocompany.rest.api.proyectono2ipc2.billeteracine.models.BilleteraCineDTO;
import com.pablocompany.rest.api.proyectono2ipc2.cine.dtos.CantidadCargaRequest;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author pablo
 */
//Clase delegada para generar todo tipo de itneraccion con la billetera de cine
public class BilleteraCineCrudService {

    //Metodo delegado para retornar todo el listado de billeteras asociadas al cine en el sistema
    public List<BilleteraCineDTO> obtenerCinesAsociados(String limite, String offset, String idUsuario) throws FormatoInvalidoException, ErrorInesperadoException {
        
        CantidadCargaRequest cantidadCargaRequest = extraerLimites(limite, offset);
        
        if (cantidadCargaRequest.validarRequest()) {
            
            BilleteraCineDB billeteraCineDb = new BilleteraCineDB();
            
            return billeteraCineDb.billeterasAsociadasCine(idUsuario, cantidadCargaRequest);
            
        }
        throw new ErrorInesperadoException("No se ha podido obtener el listado de cines asociados");
    }

    //Metodo delegado para poder retornar y validar el request de limite de muestreo de cines
    private CantidadCargaRequest extraerLimites(String limite, String offset) throws FormatoInvalidoException {
        
        if (StringUtils.isBlank(limite)) {
            throw new FormatoInvalidoException("El limite superior de la peticion vacio");
        }
        
        if (StringUtils.isBlank(offset)) {
            throw new FormatoInvalidoException("El limite inferior de la peticion vacio");
        }
        
        if (!StringUtils.isNumeric(limite)) {
            throw new FormatoInvalidoException("El limite superior de la peticion no es numerico");
        }
        
        if (!StringUtils.isNumeric(offset)) {
            throw new FormatoInvalidoException("El limite inferior de la peticion no es numerico");
        }
        
        try {
            
            return new CantidadCargaRequest(
                    Integer.parseInt(offset),
                    Integer.parseInt(limite));
            
        } catch (NumberFormatException e) {
            throw new FormatoInvalidoException("Los limites no son numericos");
        }
        
    }

    //Metodo que permite retornar la cantidad de billeteras asociadas en la aplicacion
    public CantidadRegistrosDTO obtenerCantidadBilleteras(String idUsuario) throws ErrorInesperadoException, DatosNoEncontradosException, FormatoInvalidoException {
        if (StringUtils.isBlank(idUsuario)) {
            throw new FormatoInvalidoException("el id del usuario esta vacio");
        }
        
        BilleteraCineDB billeteraCineDb = new BilleteraCineDB();
        return new CantidadRegistrosDTO(billeteraCineDb.cantidadCinesAsignados(idUsuario));
    }
    
}
