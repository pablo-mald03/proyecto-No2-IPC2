export interface AnuncioRegistradoDTO{

    codigo: string,
    estado: boolean,
    nombre: string,
    caducacion: boolean,
    fechaExpiracion: Date,
    fechaCompra: Date,
    url: string,
    texto: string,
    foto: string,
    codigoTipo: number,
    idUsuario: string,
    nombreUsuario: string,

}