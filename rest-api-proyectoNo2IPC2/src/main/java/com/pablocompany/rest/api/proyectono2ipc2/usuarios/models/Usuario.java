/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.usuarios.models;

import com.pablocompany.rest.api.proyectono2ipc2.excepciones.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.services.CifrarPasswordService;
import com.pablocompany.rest.api.proyectono2ipc2.usuarios.services.ConvertirImagenService;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

/**
 *
 * @author pablo
 */

//Modelo encargado de manejar el request obtenido
public class Usuario {
    
    private String id;
    private String correo;
    private String nombre;
    private String password;
    private String telefono;
    private String pais;
    private String identificacion;
    private String codigoRol;
    
    private InputStream foto; 
    
    private FormDataContentDisposition formDetalle; 

    public Usuario(String id, String correo, String nombre, String password, String telefono, String pais, String identificacion, String codigoRol, InputStream foto, FormDataContentDisposition formDetalle) {
        this.id = id;
        this.correo = correo;
        this.nombre = nombre;
        this.password = password;
        this.telefono = telefono;
        this.pais = pais;
        this.identificacion = identificacion;
        this.codigoRol = codigoRol;
        this.foto = foto;
        this.formDetalle = formDetalle;
    }
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getCodigoRol() {
        return codigoRol;
    }

    public void setCodigoRol(String codigoRol) {
        this.codigoRol = codigoRol;
    }

    public InputStream getFoto() {
        return foto;
    }

    public void setFoto(InputStream foto) {
        this.foto = foto;
    }

    public FormDataContentDisposition getFormDetalle() {
        return formDetalle;
    }

    public void setFormDetalle(FormDataContentDisposition formDetalle) {
        this.formDetalle = formDetalle;
    }
    
    //Metodo que permite retornar el arreglo de bytes de la foto de perfil del usuario
    public byte[] getFotoPerfil() throws IOException, FormatoInvalidoException{
        ConvertirImagenService convertirImagen = new ConvertirImagenService();
        return convertirImagen.convertirFormatoImagen(this.foto);
    }
    
    //Metodo que sirve para obtener la password cifrada y salteada
    public String getPasswordCifrada() throws FormatoInvalidoException{
        CifrarPasswordService cifrarPasswordService = new CifrarPasswordService();
        return cifrarPasswordService.cifrarPassword(this.password, this.correo);
    }
    
     //Metodo que se encarga de validar que los campos esten llenos
    //1 significa que se esta creando un administrador o un administrador de cine
    public boolean esUsuarioValido(String passwordConfirmacion, int parametro) throws FormatoInvalidoException {

        StringBuilder erroresEncontrados = new StringBuilder();
        boolean error = false;
        if (StringUtils.isBlank(this.nombre)) {
            erroresEncontrados.append("\nNombre Vacio, ");
            error = true;
        }

        if (StringUtils.isBlank(this.identificacion)) {
            erroresEncontrados.append("\nIdentificacion Vacia, ");
            error = true;
        }

        if (StringUtils.isBlank(this.password)) {
            erroresEncontrados.append("\nContraseña Vacia, ");
            error = true;
        }
        
        if (!StringUtils.isBlank(this.password) && this.password.length() < 5) {
           throw new FormatoInvalidoException("No se puede ingresar una contraseña con menos de 5 caracteres");
        }

        if (StringUtils.isBlank(passwordConfirmacion)) {
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

        if (parametro != 1 && parametro != 2) {

            if (StringUtils.isNotBlank(this.correo) && correoRestringido(this.correo)) {
                throw new FormatoInvalidoException("No puedes crear usuarios con los dominios\n\"admin\" o \"congress\"");
            }
        }

        if (StringUtils.isNotBlank(passwordConfirmacion) && StringUtils.isNotBlank(password) && !password.equals(passwordConfirmacion)) {
            erroresEncontrados.append("\nLas credenciales de contraseña definidas no coinciden ");
            error = true;
        }

        if (StringUtils.isBlank(pais)) {
            erroresEncontrados.append("\nPais Vacio, ");
            error = true;
        }

        if (StringUtils.isBlank(telefono)) {
            erroresEncontrados.append("\nTelefono Vacio, ");
            error = true;
        }

        if (!StringUtils.isBlank(telefono) && !StringUtils.isNumeric(telefono)) {
            erroresEncontrados.append("\nTelefono contiene letras, ");
            error = true;
        }
        
        if (StringUtils.isBlank(codigoRol) ) {
             throw new FormatoInvalidoException("No se ha especificado el codigo de Rol que tendra el usuario");
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
        String expresion = "^[\\w.-]+@[\\w.-]+(\\.[a-zA-Z]{2,})+$";

        return correoEntrante.matches(expresion);
    }
    
     //Expresion regular que permite validar que los correos no contengan terminaciones propias de roles
    //True si hace match significa que no se puede usar el tipo de dominio
    private boolean correoRestringido(String correoEntrante) {
        String regex = "^[\\w.-]+@(admin\\.com|cinema\\.com)$";
        return correoEntrante.matches(regex);
    }
    
}
