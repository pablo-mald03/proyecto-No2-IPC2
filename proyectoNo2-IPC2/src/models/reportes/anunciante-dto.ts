import { AnuncioDTO } from "../anuncios/anuncioDTO";

export interface ReporteAnuncianteDTO{

    id: string,
    nombre: string,
    correo: string,
    total: number,
    anuncios: AnuncioDTO[],
    
}