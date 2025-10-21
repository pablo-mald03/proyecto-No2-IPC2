import { Component, OnInit } from '@angular/core';
import { TipoAnuncioEnum } from '../../../models/anuncios/tipo-anuncios-enum';
import { NgClass, NgFor } from '@angular/common';
import { Anuncio } from '../../../models/anuncios/anuncio';
import { CardsReporteAnunciosComponent } from "../cards-reporte-anuncios/cards-reporte-anuncios.component";
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-reporte-anuncios',
  imports: [NgFor, CardsReporteAnunciosComponent, ReactiveFormsModule, NgClass],
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

  //Atributos que sirven para gestionar los filtros
  filtrosForm!: FormGroup;

  constructor(
    private formBuild: FormBuilder

  ) { }



  ngOnInit(): void {

    //Se teneran los filtros reactivos
    this.filtrosForm = this.formBuild.group({
      tipoAnuncio: ['todos'],
      fechaInicio: [null],
      fechaFin: [null]
    });


    this.anuncios = [

      {
        codigo: 'ANU-003',
        estado: true,
        nombre: 'Cine Clásico',
        caducacion: true,
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
        caducacion: true,
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
        caducacion: true,
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
        caducacion: false,
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

  //Anuncios 
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

  //Metodo que sirve para exportar el reporte
  exportarReporte() {


  }


  //Metodo get que sirve para validar si la fecha inicial antecede a la final
  get fechaInvalida(): boolean {
    const inicio = this.filtrosForm.get('fechaInicio')?.value;
    const fin = this.filtrosForm.get('fechaFin')?.value;
    if (!inicio || !fin) return false;
    return new Date(inicio) > new Date(fin);
  }

  //Metodo que sirve para validar las fechas
  validarFechas() {
    const { fechaInicio, fechaFin } = this.filtrosForm.value;
    if (fechaInicio && fechaFin && new Date(fechaInicio) > new Date(fechaFin)) {
      this.filtrosForm.setErrors({ fechaInvalida: true });
    } else {
      this.filtrosForm.setErrors(null);
    }
  }

  //Metodo que sirve para filtrar
  filtrar(): void {

    if (this.filtrosForm.invalid || this.fechaInvalida) return;

    const { fechaInicio, fechaFin } = this.filtrosForm.value;

    const inicioISO = fechaInicio ? new Date(fechaInicio).toISOString() : null;
    const finISO = fechaFin ? new Date(fechaFin).toISOString() : null;

    //Pendiente hacer la query


  }

  //Metodo que se encarga de limpiar los filtros
  limpiarFiltros(): void {
    this.filtrosForm.patchValue({
      tipoAnuncio: 'Todos',
      fechaInicio: null,
      fechaFin: null
    });

  }

}
