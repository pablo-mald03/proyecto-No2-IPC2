import { UsuarioBoletosCompradosDTO } from "../usuarios/usuario-boletos-comprados-dto";

export interface ReporteBoletosVendidosDTO {

    codigo: string,
    cineAsociado: string,
    nombre: string,
    ubicacion: string,
    total: number,
    usuarios: UsuarioBoletosCompradosDTO[],

}