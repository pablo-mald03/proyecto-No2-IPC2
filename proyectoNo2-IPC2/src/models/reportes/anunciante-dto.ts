import { AnuncioDTO } from "../anuncios/anuncio-dto";

export interface ReporteAnuncianteDTO{

    id: string,
    nombre: string,
    correo: string,
    total: number,
    anuncios: AnuncioDTO[],
    
}