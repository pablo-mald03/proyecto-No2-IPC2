import { Component, OnInit } from '@angular/core';
import { GananciasAnuncianteCardsComponent } from "../ganancias-anunciante-cards/ganancias-anunciante-cards.component";
import { ReporteAnuncianteDTO } from '../../../models/reportes/anunciante-dto';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { NgClass } from '@angular/common';
import { Popup } from '../../../shared/popup/popup';
import { SharedPopupComponent } from '../../pop-ups/shared-popup.component/shared-popup.component';

@Component({
  selector: 'app-reporte-ganancias-anunciante.component',
  imports: [GananciasAnuncianteCardsComponent, ReactiveFormsModule, NgClass, SharedPopupComponent],
  templateUrl: './reporte-ganancias-anunciante.component.html',
  styleUrl: './reporte-ganancias-anunciante.component.scss',
  providers: [Popup],
})
export class ReporteGananciasAnuncianteComponent implements OnInit {

  reporteAnunciante: ReporteAnuncianteDTO[] = [];
  reportesMostrados: ReporteAnuncianteDTO[] = [];

  //Apartado de atributos que sirven para cargar dinamicamente los atributos
  indiceActual = 0;
  cantidadPorCarga = 2;
  totalReportes = 0;
  todosCargados = false;

  //Flag que sirve para saber si se ha filtrado
  estaFiltrado = false;

  //Atributos que sirven para gestionar los filtros
  filtrosForm!: FormGroup;

  //Atributo que permite filtrar por anunciante
  filtrosFormUsuario!: FormGroup;

  constructor(
    private formBuild: FormBuilder,
    private formBuildUsuario: FormBuilder,

  ) { }

  //Inicializa todos los botones e interacciones que se tendran para poder solicitar las querys
  ngOnInit(): void {

    this.filtrosForm = this.formBuild.group({
      fechaInicio: [null],
      fechaFin: [null]
    });

    this.filtrosFormUsuario = this.formBuildUsuario.group({
      idUsuario: ['']
    });




    this.cargarMasReportes();
  }


  //Metodo que sirve para generar el reporte
  generarReporte(): void {



  }


  //Apartado de metodos para gestionar los filtros
  cargarMasReportes(): void {
    const siguienteBloque = this.reporteAnunciante.slice(
      this.indiceActual,
      this.indiceActual + this.cantidadPorCarga
    );

    this.reportesMostrados.push(...siguienteBloque);
    this.indiceActual += this.cantidadPorCarga;

    if (this.indiceActual >= this.reporteAnunciante.length) {
      this.todosCargados = true;
    }
  }

  //Metodo que sirve para exportar reportes
  exportarReporte() {


  }

  //Metodo que sirve para filtrar
  filtrar(): void {
    const { idUsuario } = this.filtrosFormUsuario.value;

    if (!idUsuario) {
      return;
    }

    console.log('Filtrando usuario:', idUsuario);
  }


  //Metodo encargado para poder limpiar el id del usuario especificado 
  limpiarUsuario(): void {
    this.filtrosFormUsuario.patchValue({
      idUsuario: ''
    });
  }


  //Metodo get que sirve para validar si la fecha inicial antecede a la final
  get fechaInvalida(): boolean {
    const inicio = this.filtrosForm.get('fechaInicio')?.value;
    const fin = this.filtrosForm.get('fechaFin')?.value;

    if (!inicio || !fin) return false;
    return new Date(inicio) > new Date(fin);
  }

  //Sirve para poder validar las fechas
  validarFechas() {
    const { fechaInicio, fechaFin } = this.filtrosForm.value;

    if (fechaInicio && fechaFin && new Date(fechaInicio) > new Date(fechaFin)) {
      this.filtrosForm.setErrors({ fechaInvalida: true });
    } else {
      this.filtrosForm.setErrors(null);
    }
  }


  //Metodo delegador para poder limpiar las fechas
  limpiarFechas(): void {
    this.filtrosForm.patchValue({
      fechaInicio: null,
      fechaFin: null
    });
    this.filtrosForm.setErrors(null);
  }



}
