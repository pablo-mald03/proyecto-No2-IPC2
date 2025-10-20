import { PeliculaProyectadaDTO } from "./peliculas-proyectadas-dto";

export interface ReporteSalaPeliculaProyectadaDTO {

    codigo: string,
    cineAsociado: string,
    nombre: string,
    filas: number,
    columnas: number,
    ubicacion: string,
    peliculaProyectada: PeliculaProyectadaDTO [],

}