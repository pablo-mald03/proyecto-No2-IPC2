import { SalaComentarioDTO } from "../salas-cine/sala-comentada-dto";

export interface SalaMasComenadaDTO {

    codigo: string,
    nombre: string,
    cineAsociado: String,
    filas: number,
    columnas: number,
    ubicacion: string,
    comentarios: SalaComentarioDTO[],
}