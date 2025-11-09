/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.principal;

import com.pablocompany.rest.api.proyectono2ipc2.anuncios.database.AnunciosDB;
import com.pablocompany.rest.api.proyectono2ipc2.anuncios.models.AnunciosPublicidadDTO;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author pablo
 */
//Clase delegada para poder generar el request y enviar la publicidad hacia la pagina principal 
public class ConsultarPublicidadService {
    
    //Metodo delegado para generar el retorno de publicidad desde la base de datos
    public List<AnunciosPublicidadDTO> obtenerAnunciosPublicidad(String numero) throws FormatoInvalidoException, ErrorInesperadoException{
        
        if(StringUtils.isBlank(numero)){
            throw new  FormatoInvalidoException("El numero del request esta vacio");
        }
        
        try {
            
            int aleatorio = Integer.parseInt(numero); 
            AnunciosDB anunciosDb = new AnunciosDB();
            return anunciosDb.anunciosPublicidad(aleatorio);
            
            
        } catch (NumberFormatException e) {
            throw new  FormatoInvalidoException("El numero del request no es numerico");
        }
        
    }
    
}
