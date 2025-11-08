/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.anuncios.services;

import com.pablocompany.rest.api.proyectono2ipc2.anuncios.database.ConfiguracionAnuncioDB;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.models.CambiarPrecioDTO;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.models.CambiarPrecioDTORequest;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.models.ConfiguracionAnuncioDTO;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author pablo
 */
//Clase delegada para poder proporcionar el servicio de datos sobre la configuracion del anuncio
public class ConfiguracionAnunciosService {

    //Metodo utilizado para poder retornar el listado de configuraciones del sistema
    public List<ConfiguracionAnuncioDTO> obtenerConfiguraciones() throws FormatoInvalidoException, ErrorInesperadoException {

        ConfiguracionAnuncioDB configuracionAnuncioDb = new ConfiguracionAnuncioDB();

        return configuracionAnuncioDb.obtenerListadoConfiguracion();
   
    }
    
    //Metodo utilizado para poder retornar el listado de anuncios comprados en el sistema
    public ConfiguracionAnuncioDTO obtenerConfiguracionCodigo(String codigo) throws FormatoInvalidoException, ErrorInesperadoException, DatosNoEncontradosException {
        
        if(StringUtils.isBlank(codigo)){
            throw new FormatoInvalidoException("El codigo de configuracion esta vacio");
        }

        ConfiguracionAnuncioDB configuracionAnuncioDb = new ConfiguracionAnuncioDB();

        try {
            
            int codigoBuscado = Integer.parseInt(codigo);
            
             return configuracionAnuncioDb.obtenerConfiguracionCodigo(codigoBuscado);
   
        } catch (NumberFormatException e) {
            throw new FormatoInvalidoException("El codigo del request no es numerico");
        }
       
    }
    
     //Metodo que permite indicar si el cambio de estado fue exitoso
    public boolean cambiarPrecioConfiguracion(CambiarPrecioDTORequest precioRequest) throws FormatoInvalidoException, ErrorInesperadoException, DatosNoEncontradosException {

        if (StringUtils.isBlank(precioRequest.getCodigo())) {
            throw new FormatoInvalidoException("El codigo de la configuracion esta vacio");
        }

        if (StringUtils.isBlank(precioRequest.getPrecio())) {
            throw new FormatoInvalidoException("El valor del precio esta vacio");
        }

        try {
            
            int codigo = Integer.parseInt(precioRequest.getCodigo());
            double precio = Double.parseDouble(precioRequest.getPrecio());
            
            CambiarPrecioDTO cambiarPrecioDto = new CambiarPrecioDTO(codigo, precio);
            
                  ConfiguracionAnuncioDB configuracionAnuncioDb = new ConfiguracionAnuncioDB();
                  
                  return configuracionAnuncioDb.cambiarPrecio(cambiarPrecioDto);
            
        } catch (NumberFormatException e) {
            throw new FormatoInvalidoException("Los datos de entrada no son numericos");
        }

    }

}
