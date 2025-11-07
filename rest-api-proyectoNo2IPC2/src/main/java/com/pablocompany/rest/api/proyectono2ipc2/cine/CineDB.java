/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.cine;

/**
 *
 * @author pablo
 */
//Clase Delgada para poder interactuar con la base de datos para poder gestionar los cines
public class CineDB {
    
    //Constante que sirve para poder crear a los cines
    private final String CREAR_CINE = "INSERT INTO cine (codigo, nombre, estado_anuncios, monto_ocultacion, fecha_creacion, descripcion, ubicacion) VALUES(?,?,?,?,?,?.?)";
    
    //Constante que permite obtener la cantidad de cines registrados en el sistema
    private final String CANTIDAD_CINES = "SELECT COUNT(*) AS `cantidad` FROM cine";
    
    //Constante que permite obtener los cines que estan creados en el sistema 
    private final String CONSULTAR_CINES = "SELECT codigo, nombre, estado_anuncios, monto_ocultacion, fecha_creacion, descripcion, ubicacion FROM cine";
    
    //Constante que permite obtener los cines que estan creados en el sistema 
    private final String CONSULTAR_CINES_CODIGO = "SELECT codigo, nombre, estado_anuncios, monto_ocultacion, fecha_creacion, descripcion, ubicacion FROM cine WHERE codigo = ?";
    
    //Constante que permite obtener los cines registrados en la aplicacion para el dashboard del usuario 
    private final String CONSULTAR_CINE_DASHBOARD = "SELECT codigo, nombre, descripcion, ubicacion FROM cine";
   
    //Constante que permite obtener el cine seleccionado para el dashboard del usuario 
    private final String CONSULTAR_CINE_CODIGO_DASHBOARD = "SELECT nombre, descripcion, ubicacion FROM cine WHERE codigo = ?";
    
    
    
    
    
}
