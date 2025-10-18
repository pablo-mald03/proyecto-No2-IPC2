import { Component, OnInit } from '@angular/core';
import { TipoAnuncioEnum } from '../../../models/anuncios/tipo-anuncios-enum';
import { NgFor } from '@angular/common';
import { Anuncio } from '../../../models/anuncios/anuncio';
import { CardsReporteAnunciosComponent } from "../cards-reporte-anuncios/cards-reporte-anuncios.component";

@Component({
  selector: 'app-reporte-anuncios',
  imports: [NgFor, CardsReporteAnunciosComponent],
  templateUrl: './reporte-anuncios.component.html',
  styleUrl: './reporte-anuncios.component.scss'
})
export class ReporteAnunciosComponent implements OnInit {

  tipoAnuncioEnum = TipoAnuncioEnum;

  anuncios: Anuncio[] = [];
  anunciosMostrados: Anuncio[] = [];

  indiceActual = 0;
  cantidadPorCarga = 2;
  todosCargados = false;


  ngOnInit(): void {

    this.anuncios = [

      {
        codigo: 'ANU-003',
        estado: true,
        nombre: 'Cine Clásico',
        fechaExpiracion: new Date('2025-12-10'),
        fechaCompra: new Date('2025-10-18'),
        url: 'https://www.youtube.com/embed/tgbNymZ7vqY?autoplay=1&mute=1&loop=1&playlist=tgbNymZ7vqY',
        texto: 'Revive los mejores clásicos del cine.',
        foto: '',
        codigoTipo: 3,
        idUsuario: 'USR-003',
      },
      {
        codigo: 'ANU-002',
        estado: false,
        nombre: 'Nuevo Estreno: El Viaje del Tiempo',
        fechaExpiracion: new Date('2025-12-01'),
        fechaCompra: new Date('2025-09-28'),
        url: 'imgs-app/bussiness-cinemaimg.png',
        texto: 'Descubre la nueva película de ciencia ficción que todos están esperando.',
        foto: '',
        codigoTipo: 3,
        idUsuario: 'USR-002',
      },
      {
        codigo: 'ANU-002',
        estado: false,
        nombre: 'Nuevo Estreno: El Viaje del Tiempo',
        fechaExpiracion: new Date('2025-12-01'),
        fechaCompra: new Date('2025-09-28'),
        url: 'imgs-app/bussiness-cinemaimg.png',
        texto: 'Descubre la nueva película de ciencia ficción que todos están esperando.',
        foto: '',
        codigoTipo: 3,
        idUsuario: 'USR-002',
      },
      {
        codigo: 'ANU-003',
        estado: true,
        nombre: 'Cine Clásico',
        fechaExpiracion: new Date('2025-12-10'),
        fechaCompra: new Date('2025-10-18'),
        url: 'https://www.youtube.com/embed/tgbNymZ7vqY?autoplay=1&mute=1&loop=1&playlist=tgbNymZ7vqY',
        texto: 'Revive los mejores clásicos del cine.',
        foto: '',
        codigoTipo: 3,
        idUsuario: 'USR-003',
      }

    ];



    this.cargarMasAnuncios();
  }

  tipoAnuncioOptions = Object.keys(TipoAnuncioEnum)
    .filter(val => ['ANUNCIO_TEXTO', 'IMAGEN_TEXTO', 'VIDEO_TEXTO'].includes(val))
    .map(key => ({
      key: key,
      value: TipoAnuncioEnum[key as keyof typeof TipoAnuncioEnum]
    }));



  //Apartado de metodos para gestionar los filtros
  cargarMasAnuncios(): void {
    const siguienteBloque = this.anuncios.slice(
      this.indiceActual,
      this.indiceActual + this.cantidadPorCarga
    );

    this.anunciosMostrados.push(...siguienteBloque);
    this.indiceActual += this.cantidadPorCarga;

    if (this.indiceActual >= this.anuncios.length) {
      this.todosCargados = true;
    }
  }

}
