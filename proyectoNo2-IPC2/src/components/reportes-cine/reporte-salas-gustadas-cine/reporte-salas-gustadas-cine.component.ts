import { NgClass } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ReporteSalasGustadasDTO } from '../../../models/reportes-cine/reporte-sala-gustada-dto';
import { SalasGustadasCineCardsComponent } from "../salas-gustadas-cine-cards/salas-gustadas-cine-cards.component";

@Component({
  selector: 'app-reporte-salas-gustadas-cine',
  imports: [NgClass, ReactiveFormsModule, SalasGustadasCineCardsComponent],
  templateUrl: './reporte-salas-gustadas-cine.component.html',
  styleUrl: './reporte-salas-gustadas-cine.component.scss'
})
export class ReporteSalasGustadasCineComponent {

  //Apartado de atributos que sirven para exponer los dto
  reporteSalas: ReporteSalasGustadasDTO[] = [];
  reportesMostrados: ReporteSalasGustadasDTO[] = [];

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

    this.reporteSalas = [
      {
        codigo: 'S001',
        cineAsociado: 'Cinepolis',
        nombre: 'Sala Premium 3D',
        filas: 12,
        columnas: 20,
        ubicacion: 'Nivel 2, ala norte',
        comentarios: [
          { idUsuario: 'U001', calificacion: 2, fechaPosteo: new Date('2025-09-10') },
          { idUsuario: 'U002', calificacion: 3, fechaPosteo: new Date('2025-10-12') },
          { idUsuario: 'pablofsd-02', calificacion: 5, fechaPosteo: new Date('2025-10-15') }
        ]
      },
    ];


    this.cargarMasReportes();
  }


  //Metodo que sirve para cargar mas y no mostrar todos de golpe
  cargarMasReportes(): void {
    const siguienteBloque = this.reporteSalas.slice(
      this.indiceActual,
      this.indiceActual + this.cantidadPorCarga
    );

    this.reportesMostrados.push(...siguienteBloque);
    this.indiceActual += this.cantidadPorCarga;

    if (this.indiceActual >= this.reporteSalas.length) {
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
