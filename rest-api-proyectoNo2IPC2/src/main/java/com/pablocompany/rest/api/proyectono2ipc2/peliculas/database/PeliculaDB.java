/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono2ipc2.peliculas.database;

/**
 *
 * @author pablo
 */
//Clase delegada para operar 
public class PeliculaDB {
    
    //constante utilizada para crear las peliculas
    private final String CREAR_ANUNCIO = "INSERT INTO pelicula (codigo, nombre, poster, sinopsis, casteo, fecha_estreno, director, precio, clasificacion, duracion) VALUES (?,?,?,?,?,?,?,?,?,?)";

    
    //Constante utilizada para crear las peliculas
    private final String CONSULTAR_PELICULAS = "SELECT codigo, nombre, poster, sinopsis, casteo, fecha_estreno, director, precio, clasificacion, duracion FROM pelicula LIMIT ? OFFSET ?" ;
    
    //Constante utilizada para obtener las peliculas por codigo
    private final String CONSULTAR_PELICULAS_POR_CODIGO = "SELECT codigo, nombre, poster, sinopsis, casteo, fecha_estreno, director, precio, clasificacion, duracion FROM pelicula WHERE codigo = ? LIMIT ? OFFSET ?" ;
    
    
    
    
    
}
