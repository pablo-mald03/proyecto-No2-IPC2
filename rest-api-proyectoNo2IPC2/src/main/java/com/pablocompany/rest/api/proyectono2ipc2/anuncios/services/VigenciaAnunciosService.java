/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.anuncios.services;

import com.pablocompany.rest.api.proyectono2ipc2.anuncios.database.VigenciaAnuncioDB;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.dtos.VigenciaAnuncioDTO;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.models.CambiarPrecioDTO;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.models.CambiarPrecioDTORequest;
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
public class VigenciaAnunciosService {

    //Metodo utilizado para poder retornar el listado de vigencias de anuncios registrados en el sistema
    public List<VigenciaAnuncioDTO> obtenerVigencias() throws FormatoInvalidoException, ErrorInesperadoException {

        VigenciaAnuncioDB vigenciaAnuncioDb = new VigenciaAnuncioDB();

        return vigenciaAnuncioDb.obtenerListadoVigencia();

    }

    //Metodo utilizado para poder retornar una vigencia de anuncio en especifico 
    public VigenciaAnuncioDTO obtenerVigenciaCodigo(String codigo) throws FormatoInvalidoException, ErrorInesperadoException, DatosNoEncontradosException {

        if (StringUtils.isBlank(codigo)) {
            throw new FormatoInvalidoException("El codigo de vigencia esta vacio");
        }

        VigenciaAnuncioDB vigenciaAnuncioDb = new VigenciaAnuncioDB();

        try {

            int codigoBuscado = Integer.parseInt(codigo);

            return vigenciaAnuncioDb.obtenerVigenciaCodigo(codigoBuscado);

        } catch (NumberFormatException e) {
            throw new FormatoInvalidoException("El codigo del request no es numerico");
        }

    }

    //Metodo que permite indicar si el cambio de precio fue exitoso
    public boolean cambiarPrecioVigencia(CambiarPrecioDTORequest precioRequest) throws FormatoInvalidoException, ErrorInesperadoException, DatosNoEncontradosException {

        if (StringUtils.isBlank(precioRequest.getCodigo())) {
            throw new FormatoInvalidoException("El codigo de la vigencia esta vacio");
        }

        if (StringUtils.isBlank(precioRequest.getPrecio())) {
            throw new FormatoInvalidoException("El valor del precio esta vacio");
        }

        try {

            int codigo = Integer.parseInt(precioRequest.getCodigo());
            double precio = Double.parseDouble(precioRequest.getPrecio());

            CambiarPrecioDTO cambiarPrecioDto = new CambiarPrecioDTO(codigo, precio);

            VigenciaAnuncioDB vigenciaAnuncioDb = new VigenciaAnuncioDB();

            return vigenciaAnuncioDb.cambiarPrecio(cambiarPrecioDto);

        } catch (NumberFormatException e) {
            throw new FormatoInvalidoException("Los datos de entrada no son numericos");
        }

    }

}
