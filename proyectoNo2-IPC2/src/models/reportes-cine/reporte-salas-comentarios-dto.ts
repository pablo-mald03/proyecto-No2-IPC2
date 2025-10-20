import { SalaComentarioDTO } from "../salas-cine/sala-comentada-dto";

export interface ReporteSalasComentadasDTO {

    codigo: string,
    cineAsociado: string,
    nombre: string,
    filas: number,
    columnas: number,
    ubicacion: string,
    comentarios: SalaComentarioDTO[],


}