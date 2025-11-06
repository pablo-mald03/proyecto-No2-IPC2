import { Component, OnInit } from '@angular/core';
import { SalasComentadasCardsComponent } from "../salas-comentadas-cards/salas-comentadas-cards.component";
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { NgClass, NgIf } from '@angular/common';
import { Popup } from '../../../shared/popup/popup';
import { SharedPopupComponent } from '../../pop-ups/shared-popup.component/shared-popup.component';
import { SalaMasComentadaDTO } from '../../../models/reportes/sala-mas-comentada-dto';
import { ReporteSalaMasComentadaService } from '../../../services/reportes-sistema-service/reporte-sala-mas-comentada.service';
import { ExportarReporteSalaMasComentadasService } from '../../../services/reportes-sistema-service/exportar-reporte-sala-mas-comentadas.service';
import { CantidadReportesDTO } from '../../../models/reportes/cantidad-reportes-dto';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-reporte-salas-comentadas',
  imports: [SalasComentadasCardsComponent, NgClass, ReactiveFormsModule, SharedPopupComponent, NgIf],
  templateUrl: './reporte-salas-comentadas.component.html',
  styleUrl: './reporte-salas-comentadas.component.scss',
  providers: [Popup],

})
export class ReporteSalasComentadasComponent implements OnInit {


  reporteSalaComentada: SalaMasComentadaDTO[] = [];
  reportesMostrados: SalaMasComentadaDTO[] = [];

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
    private reporteSalasComentadasService: ReporteSalaMasComentadaService,
    private exportarReporteSalaComentadaService: ExportarReporteSalaMasComentadasService,
    private popUp: Popup,

  ) { }

  //Metodo que se encarga de inicializar la pagina
  ngOnInit(): void {

    //Se teneran los filtros reactivos
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


  //Apartado de metodos para gestionar los filtros
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

    this.reporteSalasComentadasService.cantidadReportes(inicioISO, finISO).subscribe({
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


  //Metodo que sirve para ir cargando dinamicamente los reportes de salas mas comentadas
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

    this.reporteSalasComentadasService.reporteSalasComentadas(fechaInicioISO, fechaFinISO, this.cantidadPorCarga, this.indiceActual).subscribe({
      next: (response: SalaMasComentadaDTO[]) => {
        this.ampliarResultados(response);

      },
      error: (error: any) => {

        this.mostrarError(error);

      }
    });



  }

  //Metodo que permite ir amplieando el arreglo de datos acorde a los request
  ampliarResultados(response: SalaMasComentadaDTO[]): void {

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



    this.exportarReporteSalaComentadaService.exportarReporteSalasComentadas(inicioISO, finISO, this.indiceActual, 0).subscribe({
      next: (response: HttpResponse<Blob>) => {

        this.descargarReporte(response);

      },
      error: (error: any) => {

        this.mostrarError(error);

      }
    });

  }

  //Metodo que sirve para redireccionar cuando se exporta un reporte de las 5 salas mas comentadas 
  descargarReporte(respuesta: HttpResponse<Blob>) {
    const contentDisposition = respuesta.headers.get('Content-Disposition');

    let fileName = 'ReporteSalasMasGustadas.pdf';
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
  }

}
