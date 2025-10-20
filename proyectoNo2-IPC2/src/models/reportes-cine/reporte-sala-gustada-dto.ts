import { SalaCalificacionDTO } from "../salas-cine/sala-calificacion-dto";

export interface ReporteSalasGustadasDTO {

    codigo: string,
    cineAsociado: string,
    nombre: string,
    filas: number,
    columnas: number,
    ubicacion: string,
    comentarios: SalaCalificacionDTO[],


}