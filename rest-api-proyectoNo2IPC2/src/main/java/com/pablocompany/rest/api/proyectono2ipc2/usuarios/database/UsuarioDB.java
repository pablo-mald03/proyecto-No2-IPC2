/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.usuarios.database;

/**
 *
 * @author pablo
 */
//Clase delegada para operar con usuario en la base de datos
public class UsuarioDB {

    private final String USUARIO_EXISTENTE = "SELECT identificacion FROM usuario WHERE correo = ? AND identificacion = ?;";

    //Constante utilizada para crear el usuario en la pagina web
    private final String INSERTAR_USUARIO = "INSERT INTO usuario(id,correo,nombre,foto,password,telefono,pais,identificacion,codigo_rol) VALUES(?,?,?,?,?,?,?,?,?,?);";

    //Constante especial para el login
    private final String BUSCAR_USUARIO_ID = "SELECT nombre, identificacion, password, correo, telefono, organizacion, rol, dinero, estado FROM usuario WHERE correo = ?";

    //Constante que ayuda a saber si el usuario esta activo
    private final String USUARIO_ACTIVO = "SELECT estado FROM usuario WHERE correo = ? AND password = ?";

    private final String FOTO_USUARIO = "SELECT foto FROM usuario WHERE correo = ? AND identificacion = ?";

}
