import { CostoCineDTO } from "./costo-cine-dto";

export interface CineCostoDTO {

    codigo: string,
    nombre: string,
    montoOcultacion: number,
    fechaCreacion: Date,
    costos: CostoCineDTO[],

}