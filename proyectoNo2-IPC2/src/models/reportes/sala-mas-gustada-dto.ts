import { UsuarioReporteDTO } from "../usuarios/usuario-report-dto";

export interface SalaMasGustadaDTO {

    codigo: string,
    cineAsociado: string,
    nombre: string,
    filas: number,
    columnas: number,
    ubicacion: string,
    totalVentasBoleto: number,
    usuarios: UsuarioReporteDTO[],
}