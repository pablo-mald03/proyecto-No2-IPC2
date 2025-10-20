import { NgClass } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ReporteBoletosVendidosDTO } from '../../../models/reportes-cine/reporte-boletos-vendidos-dto';
import { BoletosVendidosCardsComponent } from "../boletos-vendidos-cards/boletos-vendidos-cards.component";

@Component({
  selector: 'app-reporte-boletos-vendidos',
  imports: [NgClass, ReactiveFormsModule, BoletosVendidosCardsComponent],
  templateUrl: './reporte-boletos-vendidos.component.html',
  styleUrl: './reporte-boletos-vendidos.component.scss'
})
export class ReporteBoletosVendidosComponent implements OnInit {

  //Apartado de atributos que sirven para exponer los dto
  reporteBoletos: ReporteBoletosVendidosDTO[] = [];
  reportesMostrados: ReporteBoletosVendidosDTO[] = [];

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

    this.reporteBoletos = [
      {
        codigo: 'REP-001',
        cineAsociado: 'Cine Aurora',
        nombre: 'Sala Estelar',
        ubicacion: 'Zona 10, Guatemala',
        total: 1250.50,
        usuarios: [
          { idUsuario: 'USR-101', nombre: 'María López', boletosComprados: '4', totalPagado: 200 },
          { idUsuario: 'USR-102', nombre: 'Carlos Pérez', boletosComprados: '3', totalPagado: 150 },
          { idUsuario: 'USR-103', nombre: 'Ana García', boletosComprados: '5', totalPagado: 250 },
        ],
      },
      {
        codigo: 'REP-002',
        cineAsociado: 'Cine Capitol',
        nombre: 'Sala Premier',
        ubicacion: 'Zona 1, Guatemala',
        total: 890.75,
        usuarios: [
          { idUsuario: 'USR-201', nombre: 'Luis Ramírez', boletosComprados: '2', totalPagado: 100 },
          { idUsuario: 'USR-202', nombre: 'Sofía Méndez', boletosComprados: '6', totalPagado: 300 },
        ],
      },
    ];


    this.cargarMasReportes();
  }


  //Metodo que sirve para cargar mas y no mostrar todos de golpe
  cargarMasReportes(): void {
    const siguienteBloque = this.reporteBoletos.slice(
      this.indiceActual,
      this.indiceActual + this.cantidadPorCarga
    );

    this.reportesMostrados.push(...siguienteBloque);
    this.indiceActual += this.cantidadPorCarga;

    if (this.indiceActual >= this.reporteBoletos.length) {
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
