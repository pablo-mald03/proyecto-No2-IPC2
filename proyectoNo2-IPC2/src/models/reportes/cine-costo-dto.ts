import { CostoModificacionCineDTO } from "./costo-modificacion-cine-dto";

export interface CineCostoDTO {

    codigo: string,
    nombre: string,
    montoOcultacion: number,
    fechaCreacion: Date,
    costosAsociados: CostoModificacionCineDTO[],

}