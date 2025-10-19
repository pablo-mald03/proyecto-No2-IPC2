import { AnunciosCompradosDTO } from "./anuncios-comprados-dto";
import { CineCostoDTO } from "./cine-costo-dto";
import { PagoCineAnuncioDTO } from "./pago-cine-anuncio-dto";

export interface GananciasSistemaDTO {

    gastosCine: CineCostoDTO,
    anunciosComprados: AnunciosCompradosDTO,
    pagoBloqueoAnuncios: PagoCineAnuncioDTO,

    totalCostoCine: number,
    totalIngresos: number,
    totalGanancia: number,
}