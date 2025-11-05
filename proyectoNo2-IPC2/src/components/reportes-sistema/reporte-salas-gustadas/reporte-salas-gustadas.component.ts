import { Component, OnInit } from '@angular/core';
import { SalaMasGustadaDTO } from '../../../models/reportes/sala-mas-gustada-dto';
import { SalasGustadasCardsComponent } from "../salas-gustadas-cards/salas-gustadas-cards.component";
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { NgClass, NgIf } from '@angular/common';
import { Popup } from '../../../shared/popup/popup';
import { ReporteSalaMasGustadaService } from '../../../services/reportes-sistema-service/reporte-sala-mas-gustada.service';
import { ExportarReporteSalasMasGustadasService } from '../../../services/reportes-sistema-service/exportar-reporte-salas-mas-gustadas.service';
import { SharedPopupComponent } from '../../pop-ups/shared-popup.component/shared-popup.component';
import { HttpResponse } from '@angular/common/http';
import { CantidadReportesDTO } from '../../../models/reportes/cantidad-reportes-dto';

@Component({
  selector: 'app-reporte-salas-gustadas',
  imports: [SalasGustadasCardsComponent, NgClass, ReactiveFormsModule, SharedPopupComponent, NgIf],
  templateUrl: './reporte-salas-gustadas.component.html',
  styleUrl: './reporte-salas-gustadas.component.scss',
  providers: [Popup]
})
export class ReporteSalasGustadasComponent implements OnInit {

  reporteSalaGustada: SalaMasGustadaDTO[] = [];
  reportesMostrados: SalaMasGustadaDTO[] = [];

  //Apartado de atributos que sirven para cargar dinamicamente los atributos
  indiceActual = 0;
  cantidadPorCarga = 2;
  totalReportes = 0;
  todosCargados = false;

  //Atributos para mostrar el popup cuando haya un error
  mostrarPopup: boolean = false;
  mensajePopup: string = '';
  tipoPopup: 'error' | 'exito' | 'info' = 'info';
  popupKey = 0;

  //Atributos que sirven para gestionar los filtros
  filtrosForm!: FormGroup;

  constructor(
    private formBuild: FormBuilder,
    private reporteSalasGustadasService: ReporteSalaMasGustadaService,
    private exportarReporteSalaGustadaService: ExportarReporteSalasMasGustadasService,
    private popUp: Popup,

  ) { }


  ngOnInit(): void {

    //Se generan los reportes reactivos
    this.filtrosForm = this.formBuild.group({
      fechaInicio: [null],
      fechaFin: [null]
    });

    this.indiceActual = 0;
    this.reportesMostrados = [];
    this.todosCargados = true;


    this.popUp.popup$.subscribe(data => {
      this.mensajePopup = data.mensaje;
      this.tipoPopup = data.tipo;

      this.mostrarPopup = false;

      setTimeout(() => {
        this.popupKey++;
        this.mostrarPopup = true;
      }, 10);

      if (data.duracion) {
        setTimeout(() => {
          this.mostrarPopup = false;
        }, data.duracion);
      }
    });

  }

  //Metodo que sirve para filtrar
  generarReporte(): void {

    if (this.filtrosForm.invalid || this.fechaInvalida) return;

    const { fechaInicio, fechaFin } = this.filtrosForm.value;

    const inicioISO = fechaInicio ? new Date(fechaInicio).toISOString().split('T')[0] : 'null';
    const finISO = fechaFin ? new Date(fechaFin).toISOString().split('T')[0] : 'null';

    if ((inicioISO === 'null' && finISO !== 'null') ||
      (inicioISO !== 'null' && finISO === 'null')) {
      const mensaje = 'Selecciona ambos intervalos de fecha';
      this.popUp.mostrarPopup({ mensaje, tipo: 'error' });
      return;
    }

    this.reporteSalasGustadasService.cantidadReportes(inicioISO, finISO).subscribe({
      next: (cantidadDTO: CantidadReportesDTO) => {
        this.totalReportes = cantidadDTO.cantidad;
        this.indiceActual = 0;
        this.cantidadPorCarga = 2;
        this.reportesMostrados = [];
        this.todosCargados = false;

        this.cargarMasReportes(inicioISO, finISO);
      },
      error: (error: any) => {

        this.mostrarError(error);

      }
    });

  }

  //Metodo que sirve para ir cargando dinamicamente los reportes de salas mas gustadas
  mostrarMasReportes(): void {

    const { fechaInicio, fechaFin } = this.filtrosForm.value;

    const inicioISO = fechaInicio ? new Date(fechaInicio).toISOString().split('T')[0] : 'null';
    const finISO = fechaFin ? new Date(fechaFin).toISOString().split('T')[0] : 'null';

    if ((inicioISO === 'null' && finISO !== 'null') ||
      (inicioISO !== 'null' && finISO === 'null')) {
      const mensaje = 'Selecciona ambos intervalos de fecha';
      this.popUp.mostrarPopup({ mensaje, tipo: 'error' });
      return;
    }

    this.cargarMasReportes(inicioISO, finISO);

  }


  //Metodo que sirve para cargar mas dinamicamente y no mostrar todos de golpe
  cargarMasReportes(fechaInicioISO: string, fechaFinISO: string): void {

    if (this.todosCargados) return;

    if ((this.indiceActual + this.cantidadPorCarga) > 5) {
      this.indiceActual = 4;
      this.cantidadPorCarga = 1;
    }

    this.reporteSalasGustadasService.reporteSalasGustadas(fechaInicioISO, fechaFinISO, this.cantidadPorCarga, this.indiceActual).subscribe({
      next: (response: SalaMasGustadaDTO[]) => {
        this.ampliarResultados(response);

      },
      error: (error: any) => {

        this.mostrarError(error);

      }
    });



  }

  //Metodo que permite ir amplieando el arreglo de datos acorde a los request
  ampliarResultados(response: SalaMasGustadaDTO[]): void {

    if (!response || response.length === 0) {
      this.todosCargados = true;
      return;
    }

    this.reportesMostrados.push(...response);
    this.indiceActual += response.length;

    if (this.indiceActual >= this.totalReportes) {
      this.todosCargados = true;
    }

  }


  //Metodo implementado para mostrar los mensajes de errores
  mostrarError(errorEncontrado: any): void {
    let mensaje = 'Ocurrió un error';

    if (errorEncontrado.error && errorEncontrado.error.mensaje) {
      mensaje = errorEncontrado.error.mensaje;
    } else if (errorEncontrado.message) {
      mensaje = errorEncontrado.message;
    }

    this.popUp.mostrarPopup({ mensaje, tipo: 'error' });

  }


  //Metodo que sirve para exportar reportes
  exportarReporte() {


    if (this.reportesMostrados.length === 0) {
      const mensaje = 'Genera primero los reportes para poder exportarlos';
      this.popUp.mostrarPopup({ mensaje, tipo: 'info' });
      return;
    }

    const { fechaInicio, fechaFin } = this.filtrosForm.value;

    const inicioISO = fechaInicio ? new Date(fechaInicio).toISOString().split('T')[0] : 'null';
    const finISO = fechaFin ? new Date(fechaFin).toISOString().split('T')[0] : 'null';

    if ((inicioISO === 'null' && finISO !== 'null') ||
      (inicioISO !== 'null' && finISO === 'null')) {
      const mensaje = 'Selecciona ambos intervalos de fecha';
      this.popUp.mostrarPopup({ mensaje, tipo: 'error' });
      return;
    }



    this.exportarReporteSalaGustadaService.exportarReporteSalasGustadas(inicioISO, finISO, this.indiceActual, 0).subscribe({
      next: (response: HttpResponse<Blob>) => {

        this.descargarReporte(response);

      },
      error: (error: any) => {

        this.mostrarError(error);

      }
    });

  }

  //Metodo que sirve para redireccionar cuando se exporta un reporte de las 5 salas mas gustadas 
  descargarReporte(respuesta: HttpResponse<Blob>) {
    const contentDisposition = respuesta.headers.get('Content-Disposition');

    let fileName = 'ReporteSalasGustadas.pdf';
    if (contentDisposition) {
      const match = contentDisposition.match(/filename="(.+)"/);
      if (match && match[1]) {
        fileName = match[1];
      }
    }

    const url = window.URL.createObjectURL(respuesta.body!);
    const a = document.createElement('a');
    a.href = url;
    a.download = fileName;
    a.click();

    window.URL.revokeObjectURL(url);

    this.popUp.mostrarPopup({ mensaje: 'Reporte generado correctamente', tipo: 'exito' });

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



  //Metodo que sirve para limpiar las fechas de reporte de 5 salas mas gustadas
  limpiarFechas(): void {
    this.filtrosForm.reset();

    this.indiceActual = 0;
    this.cantidadPorCarga = 2;
    this.reportesMostrados = [];
    this.todosCargados = true;

    this.generarReporte();
  }

}
