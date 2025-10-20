import { Component, OnInit } from '@angular/core';
import { SalaPeliculaProyectadaDTO } from '../../../models/reportes-cine/sala-pelicula-proyectada-dto';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { NgClass } from '@angular/common';
import { PeliculasProyectadasCardsComponent } from "../peliculas-proyectadas-cards/peliculas-proyectadas-cards.component";

@Component({
  selector: 'app-reporte-peliculas-proyectadas',
  imports: [NgClass, ReactiveFormsModule, PeliculasProyectadasCardsComponent],
  templateUrl: './reporte-peliculas-proyectadas.component.html',
  styleUrl: './reporte-peliculas-proyectadas.component.scss'
})
export class ReportePeliculasProyectadasComponent implements OnInit {


  //Apartado de atributos que sirven para exponer los dto
  reportePelicula: SalaPeliculaProyectadaDTO[] = [];
  reportesMostrados: SalaPeliculaProyectadaDTO[] = [];

  //Apartado de atributos que sirven para cargar dinamicamente los atributos
  indiceActual = 0;
  cantidadPorCarga = 2;
  todosCargados = false;

  //Atributos que sirven para gestionar los filtros
  filtrosForm!: FormGroup;

  filtroSalaForm!: FormGroup;

  constructor(
    private formBuild: FormBuilder,
    private formBuildSala: FormBuilder

  ) { }


  //Inicializa los atributos en nulos
  ngOnInit(): void {

    //Se teneran los filtros reactivos
    this.filtrosForm = this.formBuild.group({
      fechaInicio: [null],
      fechaFin: [null]
    });

    this.filtroSalaForm = this.formBuildSala.group({
      idSala: [null]
    });

    this.reportePelicula = [
      {
        codigo: 'SALA-001',
        cineAsociado: 'Cine Capitol',
        nombre: 'Sala Premium 1',
        filas: 10,
        columnas: 12,
        ubicacion: 'Nivel 2 - Pasillo Central',
        peliculaProyectada: [
          {
            fechaProyeccion: new Date('2025-10-19'),
            nombre: 'El Último Viaje del Tiempo',
            sinopsis: 'Un científico desafía las leyes del universo para salvar a su familia del pasado.',
            clasificacion: 'PG-13',
            duracion: 128,
          },
          {
            fechaProyeccion: new Date('2025-10-20'),
            nombre: 'Sueños de Acero',
            sinopsis: 'Robots y humanos compiten en una guerra por la supervivencia.',
            clasificacion: 'R',
            duracion: 142,
          },
          {
            fechaProyeccion: new Date('2025-10-20'),
            nombre: 'Sueños de Acero',
            sinopsis: 'Robots y humanos compiten en una guerra por la supervivencia.',
            clasificacion: 'R',
            duracion: 142,
          },
        ],
      },
      {
        codigo: 'SALA-002',
        cineAsociado: 'Cine Aurora',
        nombre: 'Sala Clásicos Dorados',
        filas: 8,
        columnas: 10,
        ubicacion: 'Nivel 1 - Zona Retro',
        peliculaProyectada: [
          {
            fechaProyeccion: new Date('2025-10-21'),
            nombre: 'Casablanca',
            sinopsis: 'Un clásico inmortal de amor y sacrificio durante la Segunda Guerra Mundial.',
            clasificacion: 'B',
            duracion: 102,
          },
        ],
      },
    ];


    this.cargarMasReportes();
  }


  //Metodo que sirve para cargar mas y no mostrar todos de golpe
  cargarMasReportes(): void {
    const siguienteBloque = this.reportePelicula.slice(
      this.indiceActual,
      this.indiceActual + this.cantidadPorCarga
    );

    this.reportesMostrados.push(...siguienteBloque);
    this.indiceActual += this.cantidadPorCarga;

    if (this.indiceActual >= this.reportePelicula.length) {
      this.todosCargados = true;
    }
  }


  //Metodo que sirve para mandar a exportar el reporte
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
  generarReporte(): void {

    if (this.filtrosForm.invalid || this.fechaInvalida) return;

    const { fechaInicio, fechaFin } = this.filtrosForm.value;

    const inicioISO = fechaInicio ? new Date(fechaInicio).toISOString() : null;
    const finISO = fechaFin ? new Date(fechaFin).toISOString() : null;

    //Pendiente hacer la query
    console.log('trilin');

  }

  //Metodo que sirve para limpiar las fechas de reporte
  limpiarFechas(): void {
    this.filtrosForm.reset();
  }


  //Metodo que sirve para limpiar la sala de filtro
  limpiarSala(): void {
    this.filtroSalaForm.patchValue({
      idSala: ''
    });
  }

  //Metodo que sirve para filtrar por sala
  filtrarSala(): void {

    const { idSala } = this.filtroSalaForm.value;

    if (idSala == null || idSala == '') {
      return;
    }

    //Pendiente hacer la query
    console.log('trilin');

  }

}
