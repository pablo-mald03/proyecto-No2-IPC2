/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.anuncios.dtos;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author pablo
 */
//Clase delegada pra retornar los anuncios relacionados al anunciante
public class CantidadAnunciosClienteRequest {

    private int offset;
    private int limit;
    private String idUsuario;

    public CantidadAnunciosClienteRequest(int offset, int limit, String idUsuario) {
        this.offset = offset;
        this.limit = limit;
        this.idUsuario = idUsuario;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    //Metodo delegado para validar el request de datos
    public boolean validarRequest() throws FormatoInvalidoException {

        if (limit < 0) {
            throw new FormatoInvalidoException("No se puede enviar un limite menor que cero");
        }

        if (offset < 0) {
            throw new FormatoInvalidoException("No se puede enviar un offset menor que cero");
        }

        if (StringUtils.isBlank(idUsuario)) {
            throw new FormatoInvalidoException("El id del usuario esta vacio");
        }

        return true;

    }

}
