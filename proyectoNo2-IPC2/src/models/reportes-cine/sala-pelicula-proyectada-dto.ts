import { PeliculaProyectadaDTO } from "./peliculas-proyectadas-dto";

export interface SalaPeliculaProyectadaDTO {

    codigo: string,
    cineAsociado: string,
    nombre: string,
    filas: number,
    columnas: number,
    ubicacion: string,
    peliculaProyectada: PeliculaProyectadaDTO [],

}