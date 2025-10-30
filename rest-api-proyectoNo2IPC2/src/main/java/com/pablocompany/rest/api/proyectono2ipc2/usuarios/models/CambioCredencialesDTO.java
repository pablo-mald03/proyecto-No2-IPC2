/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.usuarios.models;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.services.CifrarPasswordService;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author pablo
 */
//Clase delegada para poder operar los datos para poder reestablecer las credenciales del usuario
public class CambioCredencialesDTO {

    private String correo;
    private String idUsuario;
    private String password;
    private String passwordConfirm;

    public CambioCredencialesDTO(String correo, String idUsuario, String password, String passwordConfirm) {
        this.correo = correo;
        this.idUsuario = idUsuario;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    //Metodo que retorna la nueva contrasenia del usuario salteada
    public String getPasswordNueva() throws FormatoInvalidoException {
        CifrarPasswordService cifrarPasswordService = new CifrarPasswordService();
        return cifrarPasswordService.cifrarPassword(this.password, this.correo);
    }

    //Apartado de metodos delegados por patron experto para poder operar con los datos del objeto
    public boolean esValido() throws FormatoInvalidoException {

        StringBuilder erroresEncontrados = new StringBuilder();
        boolean error = false;

        if (StringUtils.isBlank(this.idUsuario)) {
            erroresEncontrados.append("\nIdentificador de usuario vacio, ");
            error = true;
        }

        if (StringUtils.isBlank(this.password)) {
            erroresEncontrados.append("\nContraseña Vacia, ");
            error = true;
        }

        if (!StringUtils.isBlank(this.password) && this.password.length() < 5) {
            throw new FormatoInvalidoException("No se puede ingresar una contraseña con menos de 5 caracteres");
        }

        if (StringUtils.isBlank(this.passwordConfirm)) {
            erroresEncontrados.append("\nConfirmacion de contraseña Vacia, ");
            error = true;
        }

        if (StringUtils.isBlank(this.correo)) {
            erroresEncontrados.append("\nCorreo Vacio, ");
            error = true;
        }

        if (StringUtils.isNotBlank(this.correo) && !correoValido(this.correo)) {
            erroresEncontrados.append("\nCorreo no cumple con el formato correcto, ");
            error = true;
        }

        if (StringUtils.isNotBlank(this.passwordConfirm) && StringUtils.isNotBlank(this.password) && !this.password.equals(this.passwordConfirm)) {
            erroresEncontrados.append("\nLas credenciales de contraseña definidas no coinciden ");
            error = true;
        }

        if (!error) {
            return true;
        } else {
            throw new FormatoInvalidoException(String.valueOf(erroresEncontrados));
        }

    }

    //Metodo que ayuda a validar un correo que sea valido (IMPLEMENTACION DE EXPRESIONES REGULARES)
    /*
        SIGNIFICADOS:
        
        ^: Inicio de la cadena
    
        [\w.-]+:
        \w: Contiempla letras y guiones
        .: contiempla puntos
        -: Contiempla guiones literales
        +: Contiempla que tiene que haber al menos uno o mas caracteres
    
        @: Compara la arroba literal
        
        [\w.-]+: Permite lo mismo y valida los puntos dentro del dominio
        
        \.: Evalua el punto que separa al dominio
        [a-zA-Z]{2,}: 
    
        [a-zA-Z]: Indica la validacion de letras
        
        {2,}: Evalua que puedan haber dominios con mas de doble punto tales como: .edu.gt
    
        $: Indica el fin de la cadena
        
    
     */
    private boolean correoValido(String correoEntrante) {
        String expresion = "^[\\w.+-]+@([\\w-]+\\.)+[a-zA-Z]{2,}$";

        return correoEntrante.matches(expresion);
    }

}
