/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.costocine.services;

import com.pablocompany.rest.api.proyectono2ipc2.costocine.database.CostoCineDB;
import com.pablocompany.rest.api.proyectono2ipc2.costocine.dtos.CostoDTORequest;
import com.pablocompany.rest.api.proyectono2ipc2.costocine.models.CostoCine;
import com.pablocompany.rest.api.proyectono2ipc2.costocine.models.CostoModificacionDTO;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import java.time.LocalDate;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author pablo
 */
//Clase delegada para poder gestionar u operar con los costos de cine 
//Permite visualizar y crear nuevos costos
public class CostosCineCrudService {

    //Metodo que se encarga de crear/modificar el nuevo costo de cine
    public boolean modificarCosto(CostoDTORequest request) throws FormatoInvalidoException, ErrorInesperadoException, DatosNoEncontradosException {

        CostoCine costoNuevo = extraerDatos(request);

        if (costoNuevo.validarCosto()) {

            CostoCineDB costoDb = new CostoCineDB();

            LocalDate fechaPrincipal = costoDb.fechaPrimerCostoCine(costoNuevo.getCodigoCine());

            if (costoNuevo.getFechaModificacion().isBefore(fechaPrincipal)) {
                throw new FormatoInvalidoException("La fecha modificacion nueva no puede ser inferior a la fecha en la que se creo el cine ");
            }

            return costoDb.definirNuevoCosto(costoNuevo);

        }

        throw new ErrorInesperadoException("No se ha podido registrar el nuevo costo de cine");

    }

    //Metodo encargado para poder validar el costo de cine 
    private CostoCine extraerDatos(CostoDTORequest request) throws FormatoInvalidoException {

        if (StringUtils.isBlank(request.getCosto())) {
            throw new FormatoInvalidoException("El costo de cine esta vacio");
        }

        try {

            return new CostoCine(
                    Double.parseDouble(request.getCosto()),
                    request.getFechaModificacion(),
                    request.getCodigoCine());

        } catch (NumberFormatException e) {
            throw new FormatoInvalidoException("El costo de cine no es numerico");
        }

    }

    //Metodo que permite retornar el listado de costos que ha tenido el cine
    public List<CostoModificacionDTO> obtenerListadoCostos(String codigoCine) throws FormatoInvalidoException, ErrorInesperadoException {

        if (StringUtils.isBlank(codigoCine)) {
            throw new FormatoInvalidoException("El codigo del cine esta vacio");
        }
        
        CostoCineDB costoDb = new CostoCineDB();
        
        return costoDb.obtenerListadoCinesAsociados(codigoCine);
    }
}
